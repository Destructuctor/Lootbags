package com.dest.listeners;

import com.dest.Lootbags;
import com.dest.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerInteractLootbagListener implements Listener {
    public Lootbags lootbags;

    public PlayerInteractLootbagListener(Lootbags instance) {
        lootbags = instance;
    }

    public boolean getChance(double minimalChance) {
        Random random = new Random();
        return random.nextInt(99) + 1 <= minimalChance;
    }

    @EventHandler
    public void interactListener(PlayerInteractEvent e) {
        Player player = e.getPlayer();


        ItemStack interacted = e.getPlayer().getInventory().getItemInHand();

        String prefix = Utils.cc(lootbags.getConf().getString("prefix"));
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            for (String lootbag : lootbags.getLootbagsConf().getConfigurationSection("Lootbags").getKeys(false)) {

                if (interacted.hasItemMeta()) {
                    for (String loreLine : interacted.getItemMeta().getLore()) {

                    if (interacted.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.cc(lootbags.getLootbagsConf().getString("Lootbags." + lootbag + ".Name"))) && interacted.getItemMeta().hasLore() && interacted.getItemMeta().getLore().contains(Utils.cc(loreLine))) {


                        for (int t = 0; t < lootbags.getLootbagsConf().getInt("Lootbags." + lootbag + ".Amount-Of-Rewards"); t++) {
                            setupRewards(e.getPlayer(), interacted);
                        }
                    }



                        if (interacted.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.cc(lootbags.getLootbagsConf().getString("Lootbags." + lootbag + ".Name"))) && interacted.getItemMeta().hasLore() && interacted.getItemMeta().getLore().contains(Utils.cc(loreLine))) {
                            interacted.setAmount(interacted.getAmount() - 1);
                            if (interacted.getAmount() <= 0) {
                                player.getInventory().clear(player.getInventory().getHeldItemSlot());
                            }
                        }
                    }
                }
            }
        }


    }

    public void setupRewards(Player player, ItemStack interacted) {
        for (String lootbag : lootbags.getLootbagsConf().getConfigurationSection("Lootbags").getKeys(false)) {
            ArmorStand stand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            stand.setGravity(false);
            stand.setArms(false);
            stand.setVisible(false);
            stand.setHelmet(interacted);
            stand.teleport(stand.getLocation().add(0, 1, 0));
            int thingy = Bukkit.getScheduler().scheduleSyncRepeatingTask(lootbags, () -> {
                for (int radius = 0; radius < 1.5; radius++) {
                    for (Location loc : Utils.getCircle(stand.getLocation(), radius, 40)) {
                        for (int i = 0; i < 2; i++) {
                            player.getWorld().spawnParticle(Particle.REDSTONE, (float) loc.getX(), (float) loc.add(0, i + 0.1, 0).getY(), (float) loc.getZ(), 255, 255, 0.001, 0);
                        }

                    }
                }
                Location loc = stand.getLocation();
                loc.setYaw((loc.getYaw() + 7.5F));
                stand.teleport(loc);

            }
                    , 0, 0);

            ArrayList<String> messageValues = new ArrayList<String>();
            ArrayList<ItemStack> winnings = new ArrayList<>();
            String prefix = Utils.cc(lootbags.getConf().getString("prefix"));
            ArrayList<String> commandWinnings = new ArrayList<>();
            Bukkit.getScheduler().scheduleSyncDelayedTask(lootbags, () -> {


                    int i = 0;
                    String str = "";
                    ItemStack theItemToGive = new ItemStack(Material.FEATHER);

                    for (String item : lootbags.getLootbagsConf().getConfigurationSection("Lootbags." + lootbag + ".Winnings").getKeys(false)) {
                        str = item;
                        boolean items = getChance(lootbags.getLootbagsConf().getInt("Lootbags." + lootbag + ".Winnings." + item + ".Chance"));
                        messageValues.add(lootbags.getLootbagsConf().getString("Lootbags." + lootbag + ".Winnings." + item));
                        lootbags.getLootbagsConf().getConfigurationSection("Lootbags." + lootbag + ".Winnings");
                        items = getChance(lootbags.getLootbagsConf().getInt("Lootbags." + lootbag + ".Winnings." + item + ".Chance"));
                        int amount = lootbags.getLootbagsConf().getInt("Lootbags." + lootbag + ".Winnings." + item + ".Amount");
                        theItemToGive = new ItemStack(Material.valueOf(item.toUpperCase()), amount);
                        winnings.add(theItemToGive);
                        ItemMeta itemMeta = theItemToGive.getItemMeta();

                        if (!lootbags.getLootbagsConf().isSet("Lootbags." + lootbag + ".Winnings." + item + ".Enchants")) {

                        } else {
                            for (String enchant : lootbags.getLootbagsConf().getStringList("Lootbags." + lootbag + ".Winnings." + item + ".Enchants")) {
                                String theEnchant = enchant.substring(enchant.indexOf("(") + 1, enchant.indexOf(","));
                                String level = enchant.substring(enchant.indexOf(",") + 1, enchant.indexOf(")"));

                                itemMeta.addEnchant(Enchantment.getByName(theEnchant.toUpperCase()), Integer.valueOf(level), true);
                            }
                        }

                        if (!lootbags.getLootbagsConf().isSet("Lootbags." + lootbag + ".Winnings." + item + ".Name")) {
                            itemMeta.setDisplayName(itemMeta.getDisplayName());
                        } else {
                            itemMeta.setDisplayName(Utils.cc(lootbags.getLootbagsConf().getString("Lootbags." + lootbag + ".Winnings." + item + ".Name")));
                        }
                        if (!lootbags.getLootbagsConf().isSet("Lootbags." + lootbag + ".Winnings." + item + ".Lore")) {
                            itemMeta.setLore(Arrays.asList());
                        } else {
                            List<String> translated = new ArrayList<>();

                            for (String s : lootbags.getLootbagsConf().getStringList("Lootbags." + lootbag + ".Winnings." + item + ".Lore")) {
                                translated.add(Utils.cc(s));
                            }
                            itemMeta.setLore(translated);
                        }

                        if (!lootbags.getLootbagsConf().isSet("Lootbags." + lootbag + ".Winnings." + item + ".Chance")) {
                            Bukkit.getLogger().severe("Item:" + item + " does not have a chance set! ERROR!");
                            player.sendMessage(Utils.cc(prefix + " &cThere is a  error! please tell the server developers or owners immediately! Thanks!"));
                            items = false;
                        } else {


                        }
                        theItemToGive.setItemMeta(itemMeta);
                        if (items) {

                            for (String item1 : lootbags.getLootbagsConf().getConfigurationSection("Lootbags." + lootbag + ".Winnings").getKeys(false)) {
                                player.getInventory().addItem(theItemToGive);
                            }

                        }

                    }


                    Set<String> messageKeys = lootbags.getLootbagsConf().getConfigurationSection("Lootbags." + lootbag + ".Winnings").getKeys(false);
                    String commandToExec = "";
                    String chanceForCommand = "";
                    for (String command : lootbags.getLootbagsConf().getStringList("Lootbags." + lootbag + ".Commands")) {
                        if (!command.contains("cmd:") || !command.contains("chance:")) {
                            System.err.println("Command: " + command + " does not have either a cmd: or a chance: !");
                        } else {

                            commandToExec = command.substring(command.indexOf("("), command.indexOf(","));

                            chanceForCommand = command.substring(command.lastIndexOf("(") + 1, command.length() - 1);

                            commandToExec = commandToExec.replaceAll("\\(", "").replaceAll("%player%", player.getName()).replaceAll("\\)", "");

                        }
                    }
                    Boolean ifGot = getChance(Double.valueOf(chanceForCommand));

                    if (ifGot) {


                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExec);

                    }
                    if (lootbags.getLootbagsConf().getConfigurationSection("Lootbags." + lootbag + ".Winnings") == null) {

                    } else {

                    }

                    stand.remove();
                    Bukkit.getScheduler().

                            cancelTask(thingy);
            }, 60L);


        }
    }

}
