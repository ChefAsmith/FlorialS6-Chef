package net.florial.listeners;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.florial.Florial;
import net.florial.features.thirst.HydrateEvent;
import net.florial.features.thirst.ThirstManager;
import net.florial.models.PlayerData;
import net.florial.utils.Cooldown;
import net.florial.utils.general.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ClickablesListener implements Listener {

    private static final List<Integer> nbtData = List.of(

            32, 34, 35, 36, 37, 45, 50, 2, 3, 4, 5, 13, 14
    );

    @EventHandler
    public void clickableNoPlace(BlockPlaceEvent e) {

        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.PLAYER_HEAD
        || nbtData.contains(NBTEditor.getInt(e.getPlayer().getInventory().getItemInMainHand(), "CustomModelData"))) return;

        e.setCancelled(true);

    }


    @EventHandler
    public void clickableUse(PlayerInteractEvent e) {

        if (e.getAction() != Action.LEFT_CLICK_AIR
            || e.getItem() == null
            || (!(nbtData.contains(NBTEditor.getInt(e.getItem(), "CustomModelData"))))) return;

        int value = NBTEditor.getInt(e.getItem(), "CustomModelData");

        switch(value) {
            case 32 -> tulipsShadow(e.getPlayer());
            case 34 -> healingOrb(e.getPlayer());
            case 35 -> infiniteCookie(e.getPlayer());
            case 36 -> waterJug(e.getPlayer());
            case 37 -> weatherManipulation(e.getPlayer());
            case 45, 2, 3, 5 -> specialEat(e.getPlayer());
            case 4 -> useMoneyVoucher(e.getPlayer());
            case 13, 14 -> useDNAVoucher(e.getPlayer());
            case 50 -> gainFlories(e.getPlayer());

        }
    }


    private static void tulipsShadow(Player p) {

        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            if (!(Cooldown.isOnCooldown("c3", p))) p.removePotionEffect(PotionEffectType.INVISIBILITY);

        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, false, false, true));
            Cooldown.addCooldown("c3", p, 2);

        }
    }

    private static void healingOrb(Player p) {

        if (!(Cooldown.isOnCooldown("c4", p))) p.setHealth(p.getMaxHealth());
        Cooldown.addCooldown("c4", p, 240);
    }

    private static void waterJug(Player p) {

        HydrateEvent e = new HydrateEvent(
                p,
                p.getInventory().getItemInMainHand(),
                ThirstManager.getThirst(p),
                20

        );

        Bukkit.getPluginManager().callEvent(e);
    }

    private static void weatherManipulation(Player p){

        if (Cooldown.isOnCooldown("c4", p)) return;

        p.getWorld().setStorm(!p.getWorld().hasStorm());

        Cooldown.addCooldown("c4", p, 300);



    }

    private static void infiniteCookie(Player p) {

        p.setSaturation(20);
        p.setFoodLevel(20);
    }

    private static void specialEat(Player p) {

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        removeItem(p.getInventory().getItemInMainHand(), p);

        data.setDna(data.getDna() + 5);

        p.playSound(p.getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK, 1, (float) 7);

    }

    private static void gainFlories(Player p) {

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        p.getInventory().setItemInMainHand(null);

        data.setFlories(data.getFlories() + 1);

    }

    private static void removeItem(ItemStack heldItem, Player p) {

        if (heldItem.getAmount() > 1) {
            heldItem.setAmount(heldItem.getAmount() - 1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }

    }

    private static void useMoneyVoucher(Player p) {

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        removeItem(heldItem, p);

        String lore = heldItem.getItemMeta().getLore().get(0);
        lore = ChatColor.stripColor(lore);

        VaultHandler.addMoney(p, Integer.valueOf(lore));

    }

    private static void useDNAVoucher(Player p) {

        ItemStack heldItem = p.getInventory().getItemInMainHand();
        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        removeItem(heldItem, p);

        String lore = heldItem.getItemMeta().getLore().get(0);
        lore = ChatColor.stripColor(lore);

        data.setDna(data.getDna() + Integer.valueOf(lore));

    }

}
