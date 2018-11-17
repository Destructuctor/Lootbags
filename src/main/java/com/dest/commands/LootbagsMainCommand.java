package com.dest.commands;

import com.dest.Lootbags;
import com.dest.config.ConfigManager;
import com.dest.items.Lootbag;
import com.dest.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class LootbagsMainCommand implements CommandExecutor {
    Lootbags plugin;
    ConfigManager manager;
    ItemStack lootbag;

    public LootbagsMainCommand(Lootbags instance) {
        plugin = instance;
        manager = new ConfigManager(plugin.getLootbagsConf(), this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("crates")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("permissions")) {
                        player.sendMessage(Utils.cc("&acrates.give: give players and yourself lootbags."));
                        player.sendMessage(Utils.cc("&acrates.reload: reload config"));
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("crates.reload")) {
                            player.sendMessage(Utils.cc("&aConfig reloaded successfully!"));
                            plugin.reloadConfig();
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        for (String bag : plugin.getLootbagsConf().getConfigurationSection("Lootbags").getKeys(false)) {
                            player.sendMessage(Utils.cc("&a" + bag));
                        }
                    }
                }
                if (args.length == 0) {

                    player.sendMessage(Utils.cc("&8&m------------------" + Utils.cc(plugin.getConf().getString("prefix")) + "&8&m------------------"));

                    player.sendMessage(Utils.cc(plugin.getConf().getString("prefix")));
                    player.sendMessage(Utils.cc("&cDeveloped by:&c&l Destructuctor"));


                    player.sendMessage(Utils.cc(" "));

                    player.sendMessage(Utils.cc("&6&l{} = Optional, <> = Required."));
                    TextComponent component = new TextComponent("/crates give <crate-name> {player} {amount}");
                    component.setColor(ChatColor.GOLD);
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crates give"));
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to autocomplete /crates give!").color(ChatColor.GREEN).create()));
                    player.spigot().sendMessage(component);

                    TextComponent component1 = new TextComponent("/crates permissions");
                    component1.setColor(ChatColor.GOLD);
                    component1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crates permissions"));
                    component1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to autocomplete /crates permissions!").color(ChatColor.GREEN).create()));
                    player.spigot().sendMessage(component1);

                    TextComponent component2 = new TextComponent("/crates list");
                    component2.setColor(ChatColor.GOLD);
                    component2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crates list"));
                    component2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to autocomplete /crates list!").color(ChatColor.GREEN).create()));
                    player.spigot().sendMessage(component2);

                    player.sendMessage(Utils.cc(" "));


                    player.sendMessage(Utils.cc("&8&m------------------" + Utils.cc(plugin.getConf().getString("prefix")) + "&8&m------------------"));
                } else if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("give")) {
                        if (player.hasPermission("lootbags.give")) {
                            if (!plugin.getLootbagsConf().isSet("Lootbags." + args[1])) {
                                player.sendMessage(Utils.cc("&c&lThat lootbag does not exist!"));
                            } else {


                                ArrayList<ItemStack> winnings = new ArrayList<>();
                                ArrayList<String> lore = new ArrayList<>();

                                if (plugin.getLootbagsConf().isSet("Lootbags." + args[1] + ".Name") && plugin.getLootbagsConf().isSet("Lootbags." + args[1] + ".Block")) {
                                    Lootbag bag = new Lootbag(plugin.getLootbagsConf().getString("Lootbags." + args[1] + ".Name"), Material.valueOf(plugin.getLootbagsConf().getString("Lootbags." + args[1] + ".Block").toUpperCase()), winnings, lore, manager);
                                    if (!plugin.getLootbagsConf().isSet("Lootbags." + args[1] + ".Lore")) {
                                        System.err.println("No lore set for " + args[1]);
                                    } else {
                                        for (String loreToAdd : plugin.getLootbagsConf().getStringList("Lootbags." + args[1] + ".Lore")) {
                                            lore.add(Utils.cc(loreToAdd));
                                        }
                                    }

                                    ItemStack lootbag = new ItemStack(bag.getBlock());

                                    ItemMeta bagMeta = lootbag.getItemMeta();

                                    bagMeta.setLore(lore);
                                    bagMeta.setDisplayName(Utils.cc(plugin.getLootbagsConf().getString("Lootbags." + args[1] + ".Name")));

                                    lootbag.setItemMeta(bagMeta);
                                    if (args.length == 2) {
                                        player.getInventory().addItem(lootbag);
                                    } else if (args.length > 2) {
                                        if (args.length == 3) {

                                            if (StringUtils.isNumeric(args[2])) {
                                                if (Integer.valueOf(args[2]) == 1) {
                                                    lootbag.setAmount(Integer.valueOf(args[2]) + 1);
                                                } else {
                                                    lootbag.setAmount(Integer.valueOf(args[2]));
                                                    player.getInventory().addItem(lootbag);
                                                }
                                            } else {
                                                Player target = Bukkit.getPlayer(args[2]);

                                                if (target == null) {
                                                    player.sendMessage(Utils.cc("&c&lThat player is not online!"));
                                                } else {
                                                    target.getInventory().addItem(lootbag);
                                                }
                                            }


                                        }
                                        if (args.length == 4) {
                                            if (args[3] != null) {
                                                Player target = Bukkit.getPlayer(args[2]);

                                                if (target == null) {
                                                    player.sendMessage(Utils.cc("&c&lThat player is not online!"));
                                                } else {
                                                    if (StringUtils.isNumeric(args[3])) {
                                                        if (Integer.valueOf(args[3]) == 1) {
                                                            lootbag.setAmount(Integer.valueOf(args[3]) + 1);
                                                        } else {
                                                            lootbag.setAmount(Integer.valueOf(args[3]));
                                                            target.getInventory().addItem(lootbag);
                                                        }
                                                    } else {
                                                        player.sendMessage(Utils.cc("&c&lPlease insert a number!"));
                                                    }
                                                }
                                            } else {

                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }


                }
            }
        } else {

        }
        return true;
    }

}

