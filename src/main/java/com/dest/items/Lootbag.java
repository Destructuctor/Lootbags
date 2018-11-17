package com.dest.items;

import com.dest.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Lootbag {
    private String name;
    private Material block;
    private ArrayList<ItemStack> winnings;
    private ArrayList<String> lore;
    private ConfigManager manager;
    private ChatColor color;
    public Lootbag(String name, Material block, ArrayList<ItemStack> winnings, ArrayList<String> lore , ConfigManager manager) {
        this.name = name;
        this.manager = manager;
        this.block = block;
        this.winnings = winnings;
        this.lore = lore;

    }

    public String getName() {
        return this.name;
    }

    public Material getBlock() {
        return this.block;
    }

    public ArrayList<ItemStack> getWinnings() {
        return this.winnings;
    }

    public ArrayList<String> getLore() {
        return this.lore;
    }

    public ConfigManager getManager() {
        return this.manager;
    }

}
