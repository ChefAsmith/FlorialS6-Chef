package net.florial.features.skills.scent;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.florial.utils.game.GetTarget;
import net.florial.utils.general.CC;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScentManager implements Listener {

    private static final ScentUI ScentUI = new ScentUI();

    private static final GetTarget GetTarget = new GetTarget();

    @EventHandler
    public void scentListener(PlayerInteractEvent e) {

        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (e.getItem() == null || NBTEditor.getInt(e.getItem(), "CustomModelData") != 1 || e.getItem().getType() != Material.PAPER) return;

        Player p = e.getPlayer();

        switch (e.getAction()) {
            case LEFT_CLICK_AIR -> ScentUI.scentUI(p);
            case RIGHT_CLICK_AIR -> handleRightClick(p);
            default -> {}
        }

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1, 1);
    }

    private static void handleRightClick(Player player) {
        LivingEntity entity = GetTarget.of(player);
        if (entity == null) return;

        player.sendMessage(CC.translate("#ffd7dc&l&nF#ffb8c1&l&nl#ff99a6&l&no#ff7a8b&l&nr#ff5b70&l&ni#ff3c55&l&na#ff1d3a&l&nl&r #ff3c55&l➤#ff5b70 Health of Target: &f" + entity.getHealth()));

    }

    @EventHandler
    public void scentProhibitDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() != Material.PAPER || (NBTEditor.getInt(e.getItemDrop().getItemStack(), "CustomModelData") != 1)) return;
        e.setCancelled(true);
    }
}
