package com.dest;


import com.dest.commands.LootbagsMainCommand;
import com.dest.listeners.PlayerInteractLootbagListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public final class Lootbags extends JavaPlugin {
    private File file;
    private FileConfiguration fConf;

    private File configFile;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getCommand("crates").setExecutor(new LootbagsMainCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerInteractLootbagListener(this), this);
        createFiles();
        saveResource("lootbags.yml", false);
        saveResource("config.yml", false);
    }

    public FileConfiguration getLootbagsConf() {
        return this.fConf;
    }

    public FileConfiguration getConf() {
        return this.config;
    }

    public void createFiles() {
        if (file == null) {
            file = new File(getDataFolder(), "lootbags.yml");
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource("lootbags.yml", false);
        }

        fConf = new YamlConfiguration();

        try {
            fConf.load(file);

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();

        try {
            config.load(configFile);

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        fConf = YamlConfiguration.loadConfiguration(file);
    }
}
