package com.koyeyu.copyeye;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

public final class CopyEye extends JavaPlugin implements Listener {
    LinkedList<UUID> on = new LinkedList<>();

    public static Entity getTarget(final LivingEntity entity) {
        Entity target = null;
        double targetDistanceSquared = 0;
        final double radiusSquared = 1;
        final Vector l = entity.getEyeLocation().toVector(), n = entity.getLocation().getDirection().normalize();
        final double cos45 = Math.cos(Math.PI / 4);
        for (final Entity other : entity.getWorld().getNearbyEntities(entity.getLocation(),100,100,100)) {
            if (other == null || other == entity
                    || other.getLocation().distance(entity.getEyeLocation()) > entity.getTargetBlock(Set.of(Material.AIR, Material.WATER, Material.GRASS, Material.DANDELION, Material.POPPY), 100).getLocation().distance(entity.getLocation()))
                continue;
            if (target == null || targetDistanceSquared > other.getLocation().distanceSquared(entity.getLocation())) {
                final Vector t = other.getLocation().add(0, 1, 0).toVector().subtract(l);
                if (n.clone().crossProduct(t).lengthSquared() < radiusSquared && t.normalize().dot(n) >= cos45) {
                    target = other;
                    targetDistanceSquared = target.getLocation().distanceSquared(entity.getLocation());
                }
            }
        }
        return target;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (on.contains(onlinePlayer.getUniqueId())) {
                    Material type = onlinePlayer.getTargetBlock(null, 5).getType();
                    LivingEntity target = (LivingEntity) getTarget(onlinePlayer);
                    if (target != null) {
                        if (onlinePlayer.getLocation().distance(target.getLocation()) <= 5) {
                            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP,0.5f,1);
                            switch (target.getType()){
                                case PIG, HOGLIN:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.PORKCHOP));
                                    break;
                                case COW:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.BEEF));
                                    break;
                                case CHICKEN:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.CHICKEN));
                                    break;
                                case SHEEP:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.MUTTON));
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.WHITE_WOOL));
                                    break;
                                case VILLAGER:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.EMERALD));
                                    break;
                                case HORSE:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.LEATHER));
                                    break;
                                case ZOMBIE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN, DROWNED, HUSK:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH));
                                    break;
                                case SKELETON:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.ARROW));
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.BONE));
                                    break;
                                case SPIDER, CAVE_SPIDER:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.STRING));
                                    break;
                                case CREEPER:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.GUNPOWDER));
                                    break;
                                case ENDERMAN:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                                    break;
                                case BLAZE:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
                                    break;
                                case WITHER_SKELETON:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.COAL));
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.BONE));
                                    break;
                                case COD:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.COD));
                                    break;
                                case SALMON:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.SALMON));
                                    break;
                                case SQUID:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.INK_SAC));
                                    break;
                                case GLOW_SQUID:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.GLOW_INK_SAC));
                                    break;
                                case MAGMA_CUBE:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
                                    break;
                                case SLIME:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.SLIME_BALL));
                                    break;
                                case ENDER_DRAGON:
                                    onlinePlayer.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
                                    break;
                            }
                            continue;
                        }
                    }
                    if (type != Material.AIR && type != Material.CAVE_AIR && type != Material.VOID_AIR ) {
                        onlinePlayer.getInventory().addItem(new ItemStack(type));
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP,0.5f,1);
                    }
                }
            }
        },0,10);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (label.equalsIgnoreCase("copyeye")) {
                if (on.contains(p.getUniqueId())) {
                    on.remove(p.getUniqueId());
                    p.sendMessage(ChatColor.RED + "OFF");
                }else {
                    on.add(p.getUniqueId());
                    p.sendMessage(ChatColor.GOLD + "ON");
                }
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
