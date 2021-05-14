package com.github.velocity.bridge.player;

import com.velocitypowered.api.proxy.player.SkinParts;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.SkinConfiguration;

import java.util.Objects;

@RequiredArgsConstructor
public class BridgeSkinConfiguration implements SkinConfiguration {

    private final SkinParts skinParts;

    @Override
    public boolean hasCape() {
        return this.skinParts.hasCape();
    }

    @Override
    public boolean hasJacket() {
        return this.skinParts.hasJacket();
    }

    @Override
    public boolean hasLeftSleeve() {
        return this.skinParts.hasLeftSleeve();
    }

    @Override
    public boolean hasRightSleeve() {
        return this.skinParts.hasRightSleeve();
    }

    @Override
    public boolean hasLeftPants() {
        return this.skinParts.hasLeftPants();
    }

    @Override
    public boolean hasRightPants() {
        return this.skinParts.hasRightPants();
    }

    @Override
    public boolean hasHat() {
        return this.skinParts.hasHat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BridgeSkinConfiguration)) return false;
        BridgeSkinConfiguration that = (BridgeSkinConfiguration) o;
        return this.skinParts.equals(that.skinParts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.skinParts);
    }
}
