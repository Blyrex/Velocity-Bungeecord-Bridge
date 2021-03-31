package com.github.velocity.bridge.classloader;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.logging.Level;

@Getter
public final class VelocityClassLoaderInjector {
    private final BungeeVelocityBridgePlugin plugin;
    private Object velocityPluginClassLoader;
    private Method addPathMethod;
    private Method loadClassMethod;

    public VelocityClassLoaderInjector(BungeeVelocityBridgePlugin plugin) {
        this.plugin = plugin;
        try {
            this.velocityPluginClassLoader
                    = Class.forName("com.velocitypowered.proxy.plugin")
                    .getDeclaredConstructor()
                    .newInstance();
            this.addPathMethod = this.velocityPluginClassLoader
                    .getClass()
                    .getDeclaredMethod("addPath");
            this.loadClassMethod = this.velocityPluginClassLoader
                    .getClass()
                    .getDeclaredMethod("loadClass", String.class, Boolean.class);
        } catch (ClassNotFoundException exception) {
            this.plugin.getLogger().log(Level.WARNING, "Cannot find Velocity PluginClassLoader. Please make sure to use the newest Velocity Version!");
            exception.printStackTrace();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void addPath(Path path) {
        this.addPathMethod.invoke(this.velocityPluginClassLoader, path);
    }

    @SneakyThrows
    public Class<?> loadClass(String name) {
        return (Class<?>) this.loadClassMethod.invoke(this.velocityPluginClassLoader, name, true);
    }
}
