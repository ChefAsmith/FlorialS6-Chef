package net.florial.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.florial.Florial;
import net.florial.models.PlayerData;
import net.florial.utils.Cooldown;
import net.florial.utils.general.CC;
import net.florial.utils.general.CustomItem;
import net.florial.utils.general.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VoteListener implements Listener {

    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {

        Player p = Bukkit.getPlayer(event.getVote().getUsername());
        UUID u = p.getUniqueId();

        if (Cooldown.isOnCooldown("c4", p)) return;

        PlayerData data = Florial.getPlayerData().get(u);

        data.setFlories(data.getFlories() + 2);

        Cooldown.addCooldown("c4", p, 2);

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 2, (float) 1);

        VaultHandler.addMoney(p, 2000);

        ItemStack key4 = NBTEditor.set(CustomItem.MakeItem(new ItemStack(Material.GLISTERING_MELON_SLICE), "#ff7a8b&lVote Crate Key", "", false), 4, "CustomModelData");
        key4 = NBTEditor.set(key4, 4, "Crate");

        p.getInventory().addItem(key4);

        Bukkit.broadcastMessage(CC.translate("#ffd7dc&l&nF#ffb8c1&l&nl#ff99a6&l&no#ff7a8b&l&nr#ff5b70&l&ni#ff3c55&l&na#ff1d3a&l&nl&r #ff3c55&l➤ #ff5b70" + p.getName() + "&f just voted using #ff5b70/vote&f and #ff5b70&learned&f flories, money, and Vote Keys!!"));

    }
}