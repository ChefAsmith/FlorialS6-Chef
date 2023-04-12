package net.florial.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import me.santio.utils.bukkit.impl.MessageUtils;
import me.santio.utils.minecraft.message.Message;
import net.florial.Florial;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetDiscordIDCommand extends BaseCommand {

    @CommandAlias("setdiscordid")
    @CommandPermission("florial.staff")
    public void onSetIdCommand(Player player, String id) {
        String parsedId = id.substring(0, 17);
        Bukkit.getLogger().info(id);
        if (parsedId.matches(".*[a-z].*")) {
            MessageUtils.sendMessage(player, new Message("&cThat ID is not a valid discord ID"));
            return;
        }

        Florial.getPlayerData().get(player.getUniqueId()).setDiscordId(id);
        player.kick(Component.text("Please relog for changes to take effect"));
    }
}
