package net.md_5.bungee.api.plugin;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.classloader.VelocityClassLoaderInjector;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.event.EventBus;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

@RequiredArgsConstructor
public class PluginManager {

    /*========================================================================*/
    private final ProxyServer proxy;
    /*========================================================================*/
    private final Yaml yaml;
    private final EventBus eventBus;
    private final VelocityClassLoaderInjector velocityClassLoaderInjector;
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();
    private final Map<String, Command> commandMap = new HashMap<>();
    private Map<String, PluginDescription> toLoad = new HashMap<>();
    private final Multimap<Plugin, Command> commandsByPlugin = ArrayListMultimap.create();
    private final Multimap<Plugin, Listener> listenersByPlugin = ArrayListMultimap.create();

    public PluginManager(ProxyServer proxy, BungeeVelocityBridgePlugin bungeeVelocityBridgePlugin) {
        this.proxy = proxy;
        this.velocityClassLoaderInjector = new VelocityClassLoaderInjector(bungeeVelocityBridgePlugin);

        // Ignore unknown entries in the plugin descriptions
        Constructor yamlConstructor = new CustomClassLoaderConstructor(PluginDescription.class.getClassLoader());
        PropertyUtils propertyUtils = yamlConstructor.getPropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        yamlConstructor.setPropertyUtils(propertyUtils);
        this.yaml = new Yaml(yamlConstructor);

        this.eventBus = new EventBus(proxy.getLogger());
    }

    /**
     * Register a command so that it may be executed.
     *
     * @param plugin  the plugin owning this command
     * @param command the command to register
     */
    public void registerCommand(Plugin plugin, Command command) {
        this.commandMap.put(command.getName().toLowerCase(Locale.ROOT), command);
        for (String alias : command.getAliases()) {
            this.commandMap.put(alias.toLowerCase(Locale.ROOT), command);
        }
        this.commandsByPlugin.put(plugin, command);
    }

    /**
     * Unregister a command so it will no longer be executed.
     *
     * @param command the command to unregister
     */
    public void unregisterCommand(Command command) {
        while (this.commandMap.values().remove(command)) ;
        this.commandsByPlugin.values().remove(command);
    }

    /**
     * Unregister all commands owned by a {@link Plugin}
     *
     * @param plugin the plugin to register the commands of
     */
    public void unregisterCommands(Plugin plugin) {
        for (Iterator<Command> it = this.commandsByPlugin.get(plugin).iterator(); it.hasNext(); ) {
            Command command = it.next();
            while (this.commandMap.values().remove(command)) ;
            it.remove();
        }
    }

    private Command getCommandIfEnabled(String commandName, CommandSender sender) {
        String commandLower = commandName.toLowerCase(Locale.ROOT);

        // Check if command is disabled when a player sent the command
        if ((sender instanceof ProxiedPlayer) && proxy.getDisabledCommands().contains(commandLower)) {
            return null;
        }

        return this.commandMap.get(commandLower);
    }

    /**
     * Checks if the command is registered and can possibly be executed by the
     * sender (without taking permissions into account).
     *
     * @param commandName the name of the command
     * @param sender      the sender executing the command
     * @return whether the command will be handled
     */
    public boolean isExecutableCommand(String commandName, CommandSender sender) {
        return getCommandIfEnabled(commandName, sender) != null;
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        return dispatchCommand(sender, commandLine, null);
    }

    /**
     * Execute a command if it is registered, else return false.
     *
     * @param sender      the sender executing the command
     * @param commandLine the complete command line including command name and
     *                    arguments
     * @param tabResults  list to place tab results into. If this list is non
     *                    null then the command will not be executed and tab results will be
     *                    returned instead.
     * @return whether the command was handled
     */
    public boolean dispatchCommand(CommandSender sender, String commandLine, List<String> tabResults) {
        String[] split = commandLine.split(" ", -1);
        // Check for chat that only contains " "
        if (split.length == 0 || split[0].isEmpty()) {
            return false;
        }

        Command command = getCommandIfEnabled(split[0], sender);
        if (command == null) {
            return false;
        }

        if (!command.hasPermission(sender)) {
            if (tabResults == null) {
                sender.sendMessage((command.getPermissionMessage() == null) ? proxy.getTranslation("no_permission") : command.getPermissionMessage());
            }
            return true;
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);
        try {
            if (tabResults == null) {
                if (this.proxy.getConfig().isLogCommands()) {
                    this.proxy.getLogger().log(Level.INFO, "{0} executed command: /{1}", new Object[]
                            {
                                    sender.getName(), commandLine
                            });
                }
                command.execute(sender, args);
            } else if (commandLine.contains(" ") && command instanceof TabExecutor) {
                for (String s : ((TabExecutor) command).onTabComplete(sender, args)) {
                    tabResults.add(s);
                }
            }
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "An internal error occurred whilst executing this command, please check the console log for details.");
            this.proxy.getLogger().log(Level.WARNING, "Error in dispatching command", ex);
        }
        return true;
    }

    /**
     * Returns the {@link Plugin} objects corresponding to all loaded plugins.
     *
     * @return the set of loaded plugins
     */
    public Collection<Plugin> getPlugins() {
        return this.plugins.values();
    }

    /**
     * Returns a loaded plugin identified by the specified name.
     *
     * @param name of the plugin to retrieve
     * @return the retrieved plugin or null if not loaded
     */
    public Plugin getPlugin(String name) {
        return this.plugins.get(name);
    }

    public void loadPlugins() {
        Map<PluginDescription, Boolean> pluginStatuses = new HashMap<>();
        for (Map.Entry<String, PluginDescription> entry : this.toLoad.entrySet()) {
            PluginDescription plugin = entry.getValue();
            if (!this.enablePlugin(pluginStatuses, new Stack<>(), plugin)) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING, "Failed to enable {0}", entry.getKey());
            }
        }
        this.toLoad.clear();
        this.toLoad = null;
    }

    public void enablePlugins() {
        for (Plugin plugin : this.plugins.values()) {
            try {
                plugin.onEnable();
                this.proxy.getLogger().log(Level.INFO, "Enabled plugin {0} version {1} by {2}", new Object[]
                        {
                                plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthor()
                        });
            } catch (Throwable t) {
                this.proxy.getLogger().log(Level.WARNING, "Exception encountered when loading plugin: " + plugin.getDescription().getName(), t);
            }
        }
    }

    private boolean enablePlugin(Map<PluginDescription, Boolean> pluginStatuses, Stack<PluginDescription> dependStack, PluginDescription plugin) {
        if (pluginStatuses.containsKey(plugin)) {
            return pluginStatuses.get(plugin);
        }

        // combine all dependencies for 'for loop'
        Set<String> dependencies = new HashSet<>();
        dependencies.addAll(plugin.getDepends());
        dependencies.addAll(plugin.getSoftDepends());

        // success status
        boolean status = true;

        // try to load dependencies first
        for (String dependName : dependencies) {
            PluginDescription depend = this.toLoad.get(dependName);
            Boolean dependStatus = (depend != null) ? pluginStatuses.get(depend) : Boolean.FALSE;

            if (dependStatus == null) {
                if (dependStack.contains(depend)) {
                    StringBuilder dependencyGraph = new StringBuilder();
                    for (PluginDescription element : dependStack) {
                        dependencyGraph.append(element.getName()).append(" -> ");
                    }
                    dependencyGraph.append(plugin.getName()).append(" -> ").append(dependName);
                    this.proxy.getLogger().log(Level.WARNING, "Circular dependency detected: {0}", dependencyGraph);
                    status = false;
                } else {
                    dependStack.push(plugin);
                    dependStatus = this.enablePlugin(pluginStatuses, dependStack, depend);
                    dependStack.pop();
                }
            }

            if (dependStatus == Boolean.FALSE && plugin.getDepends().contains(dependName)) // only fail if this wasn't a soft dependency
            {
                ProxyServer.getInstance().getLogger().log(Level.WARNING, "{0} (required by {1}) is unavailable", new Object[]
                        {
                                String.valueOf(dependName), plugin.getName()
                        });
                status = false;
            }

            if (!status) {
                break;
            }
        }

        // do actual loading
        if (status) {

            try {
                Path path = plugin.getFile().toPath();
                this.velocityClassLoaderInjector.addPath(path);

                Class<?> main = this.velocityClassLoaderInjector.loadClass(plugin.getMain());
                Plugin clazz = (Plugin) main.getDeclaredConstructor().newInstance();

                clazz.init(this.proxy, plugin);

                this.plugins.put(plugin.getName(), clazz);
                clazz.onLoad();
                this.proxy.getLogger().log(Level.INFO, "Loaded plugin {0} version {1} by {2}", new Object[]
                        {
                                plugin.getName(), plugin.getVersion(), plugin.getAuthor()
                        });
            } catch (Exception exception) {
                this.proxy.getLogger().log(Level.WARNING, "Error enabling plugin " + plugin.getName(), exception);
            }
        }

        pluginStatuses.put(plugin, status);
        return status;
    }

    /**
     * Load all plugins from the specified folder.
     *
     * @param folder the folder to search for plugins in
     */
    public void detectPlugins(File folder) {
        Preconditions.checkNotNull(folder, "folder");
        Preconditions.checkArgument(folder.isDirectory(), "Must load from a directory");

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try (JarFile jar = new JarFile(file)) {
                    JarEntry pdf = jar.getJarEntry("bungee.yml");
                    if (pdf == null) {
                        pdf = jar.getJarEntry("plugin.yml");
                    }
                    Preconditions.checkNotNull(pdf, "Plugin must have a plugin.yml or bungee.yml");

                    try (InputStream in = jar.getInputStream(pdf)) {
                        PluginDescription desc = yaml.loadAs(in, PluginDescription.class);
                        Preconditions.checkNotNull(desc.getName(), "Plugin from %s has no name", file);
                        Preconditions.checkNotNull(desc.getMain(), "Plugin from %s has no main", file);

                        desc.setFile(file);
                        this.toLoad.put(desc.getName(), desc);
                    }
                } catch (Exception ex) {
                    ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not load plugin from file " + file, ex);
                }
            }
        }
    }

    /**
     * Dispatch an event to all subscribed listeners and return the event once
     * it has been handled by these listeners.
     *
     * @param <T>   the type bounds, must be a class which extends event
     * @param event the event to call
     * @return the called event
     */
    public <T extends Event> T callEvent(T event) {
        Preconditions.checkNotNull(event, "event");

        long start = System.nanoTime();
        this.eventBus.post(event);
        event.postCall();

        long elapsed = System.nanoTime() - start;
        if (elapsed > 250000000) {
            ProxyServer.getInstance().getLogger().log(Level.WARNING, "Event {0} took {1}ms to process!", new Object[]
                    {
                            event, elapsed / 1000000
                    });
        }
        return event;
    }

    /**
     * Register a {@link Listener} for receiving called events. Methods in this
     * Object which wish to receive events must be annotated with the
     *
     * @param plugin   the owning plugin
     * @param listener the listener to register events for
     */
    public void registerListener(Plugin plugin, Listener listener) {
        this.eventBus.register(listener);
        this.listenersByPlugin.put(plugin, listener);
    }

    /**
     * Unregister a {@link Listener} so that the events do not reach it anymore.
     *
     * @param listener the listener to unregister
     */
    public void unregisterListener(Listener listener) {
        this.eventBus.unregister(listener);
        this.listenersByPlugin.values().remove(listener);
    }

    /**
     * Unregister all of a Plugin's listener.
     *
     * @param plugin target plugin
     */
    public void unregisterListeners(Plugin plugin) {
        for (Iterator<Listener> it = this.listenersByPlugin.get(plugin).iterator(); it.hasNext(); ) {
            this.eventBus.unregister(it.next());
            it.remove();
        }
    }

    /**
     * Get an unmodifiable collection of all registered commands.
     *
     * @return commands
     */
    public Collection<Map.Entry<String, Command>> getCommands() {
        return Collections.unmodifiableCollection(this.commandMap.entrySet());
    }
}
