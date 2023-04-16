package net.florial.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.val;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.florial.Florial;
import net.florial.database.FlorialDatabase;
import net.florial.features.thirst.ThirstManager;
import net.florial.menus.species.SpeciesMenu;
import net.florial.models.PlayerData;
import net.florial.species.disguises.Morph;
import net.florial.species.events.impl.SpeciesTablistEvent;
import net.florial.utils.Cooldown;
import net.florial.utils.Message;
import net.florial.utils.general.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.morphia.query.filters.Filters.eq;

public class PlayerListeners implements Listener {

    private static final ThirstManager ThirstManager = new ThirstManager();

    private static final SpeciesMenu speciesMenu = new SpeciesMenu();

    private static final Morph morph = new Morph();

    private static final Florial florial = Florial.getInstance();

    private static final HashMap<UUID, Integer> previousMessages = new HashMap<>();

    private static final String[] SLURS ={
            "n[i1$!]i?gg?[ea3]?r?",
            "(ph|f)agg?s?([e0aio]ts?|oted|otry)",
            "n[i!j1e]+gg?(rs?|ett?e?s?|lets?|ress?e?s?|r[a0oe]s?|[ie@ao0!]rs?|r[o0]ids?|ab[o0]s?|erest)",
            "trann(ys?|ies)?",
            "\\bfagg?(s?\\b|ot|y|ier)"

    };


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();

        FlorialDatabase.getPlayerData(p).thenAccept(playerData -> {
            Florial.getPlayerData().put(u, playerData);
            Florial.getPlayerData().get(u).refresh();
            new Message("&a[MONGO] &fLoaded your player data successfully!").showOnHover(playerData.toString()).send(p);
        });

        if (Florial.getPlayerData().get(u) == null) {
            val temp = FlorialDatabase.getDatastore().find(PlayerData.class).filter(eq("UUID", u.toString()));
            Florial.getPlayerData().put(u, temp.stream().findFirst().orElse(new PlayerData(u.toString())));
            new Message("&a[MONGO] &fLoaded your player data successfully!").showOnHover(Florial.getPlayerData().get(u).toString()).send(p);
        }
        if (p.hasPermission("florial.staff")) {

            if (Florial.getPlayerData().get(u).getDiscordId() == "") {
                new Message("&c&lPlease link your account immediately using /link").send(p);
            } else {
                Florial.getInstance().getStaffToVerify().add(u);
            }
        }

        PlayerData data = Florial.getPlayerData().get(u);
        ThirstManager.thirstRunnable(p);

        Bukkit.getScheduler().runTaskLater(Florial.getInstance(), data::refresh, 100L);

        if (Florial.getQuestBar().containsKey(u)) Florial.getQuestBar().get(u).addPlayer(p);

        if (data.getSpecieType().getSpecie() == null) speciesMenu.speciesMenu(p);

        Florial.getStaffWithShifts().forEach((uuid, shiftData) -> {
            if (Bukkit.getOnlinePlayers().size() > shiftData.getHighestPlayerCount()) shiftData.setHighestPlayerCount(Bukkit.getOnlinePlayers().size());
        });

        if (data.getSpecieType().getSpecie() == null) {
            speciesMenu.speciesMenu(p);
            return;
        }
        if (data.getSpecies().getMorph() == DisguiseType.FOX) morph.activate(p, 4, false, true, data.getSpecies());

        SpeciesTablistEvent e = new SpeciesTablistEvent(
                p
        );
        Bukkit.getPluginManager().callEvent(e);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        UUID u = event.getPlayer().getUniqueId();

        Florial.getInstance().getStaffToVerify().remove(u);

        Florial.getBoards().remove(u);

        previousMessages.remove(u);

        PlayerData data = Florial.getPlayerData().get(u);
        data.save(true);
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {

        if (e.getInventory().getType() != InventoryType.CHEST || (!(e.getReason().equals(InventoryCloseEvent.Reason.PLAYER)))) return;

        if (Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSpecieType().getSpecie() != null) return;

        Bukkit.getScheduler().runTaskLater(Florial.getInstance(), () -> speciesMenu.speciesMenu((Player) e.getPlayer()), 20L);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        Player p = event.getPlayer();
        UUID u = p.getUniqueId();

        if (Florial.getInstance().getStaffToVerify().contains(u)) {
            event.setCancelled(true);
            new Message("&c&lPlease verify through discord").send(p);
            return;
        }

        if (florial.ess.getUser(p).isMuted()) return;

        String prefix = Florial.getPlayerData().get(u).getPrefix();
        if (Objects.equals(prefix, "")) {
            try {
                prefix = Objects.requireNonNull(Florial.getInstance().getLpapi().getUserManager().getUser(u)).getCachedData().getMetaData().getPrefix();
            } catch (NullPointerException e) {
                prefix = "";
            }
        }


        if (prefix == null) {
            prefix = "Default";
        }

        event.setCancelled(true);

        String message = ((TextComponent) event.message()).content();

        for (String pattern : SLURS) {
            Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(message.replaceAll(" ", ""));
            if (!(matcher.find())) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 3h You were muted for Possible Slurs - Appeal: https://discord.com/invite/TRsjqSfHVq");
            return;

        }

        if (spamChecker(p)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 15m You were muted for Possible Spam - Appeal: https://discord.com/invite/TRsjqSfHVq (Slow your messages!)");
            return;
        }

        Bukkit.broadcast(Component.text(CC.translate("&7" + prefix + " " + PlainTextComponentSerializer.plainText().serialize(p.displayName()) + ": " + message)));
    }

    private static boolean spamChecker(Player p) {

        UUID u = p.getUniqueId();

        previousMessages.putIfAbsent(u, 0);
        previousMessages.put(u, previousMessages.get(u) + 1);

        if (Cooldown.isOnCooldown("spam", p) && previousMessages.get(u) > 3){
            previousMessages.put(u, 0);
            return true;
        } else {
            Cooldown.addCooldown("spam", p, 3);
            return false;
        }

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (Florial.getInstance().getStaffToVerify().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            new Message("&c&lPlease verify through discord").send(event.getPlayer());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (Florial.getInstance().getStaffToVerify().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            new Message("&c&lPlease verify through discord").send(event.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (Florial.getInstance().getStaffToVerify().contains(((Player) event.getDamager()).getPlayer().getUniqueId())) {
                event.setCancelled(true);
                new Message("&c&lPlease verify through discord").send(((Player) event.getDamager()).getPlayer());
            }
        } else if (event.getEntity() instanceof Player) {
            if (Florial.getInstance().getStaffToVerify().contains(((Player) event.getEntity()).getPlayer().getUniqueId())) {
                event.setCancelled(true);
                new Message("&c&lPlease verify through discord").send(((Player) event.getEntity()).getPlayer());
            }
        }
    }
}
