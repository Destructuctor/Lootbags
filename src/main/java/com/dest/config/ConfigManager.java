package com.dest.config;

import com.dest.Lootbags;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final Lootbags lootbags;

    public ConfigManager(FileConfiguration config, Lootbags lootbags) {
        this.lootbags = lootbags;
    }

    public Lootbags getLootbags() {
        return this.lootbags;
    }
}
