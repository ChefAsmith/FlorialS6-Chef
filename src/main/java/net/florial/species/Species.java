package net.florial.species;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.florial.Florial;
import net.florial.models.PlayerData;
import net.florial.utils.Cooldown;
import net.florial.utils.general.CC;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Species implements Listener {
    private static final Florial florial = Florial.getInstance();

    private static final LuckPerms api = LuckPermsProvider.get();

    private static final Map<Material, Integer> fillingValues = Map.ofEntries(
            Map.entry(Material.CHICKEN, 20),
            Map.entry(Material.PORKCHOP, 15),
            Map.entry(Material.BEEF, 20),
            Map.entry(Material.SWEET_BERRIES, 10),
            Map.entry(Material.COD, 13),
            Map.entry(Material.SALMON, 13),
            Map.entry(Material.MUTTON, 20)
    );


    String name;
    int id;
    double maxHealth;
    boolean canSmell;
    DisguiseType morph;

    protected Species(String name, int id, double maxHealth, boolean canSmell, DisguiseType morph) {
        this.name = name;
        this.id = id;
        this.maxHealth = maxHealth;
        this.canSmell = canSmell;
        this.morph = morph;

        Bukkit.getPluginManager().registerEvents(this, Florial.getInstance());

    }

    /*
    1 = no fall dmg
    2 = flight
    3 = added water damage
    4 = night weakness
    5 = fire vulnerability
     */
    public Set<Integer> sharedAbilities() {
        return new HashSet<>();
    }

    public Map<Integer, PotionEffect> specific() {
        return new HashMap<>();
    }


    public Set<PotionEffect> effects() {
        return new HashSet<>();
    }

    public Set<Material> diet() {
        return new HashSet<>();
    }

    public Set<String> descriptions() {
        return new HashSet<>();
    }


    public static void become(Player p, String type) {

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        p.closeInventory();

        if (data.getSpecieId() == 0) {

            SpeciesWrapper.setSpecies(p.getUniqueId(), SpecieType.valueOf(type.toUpperCase().replace(" ", "_")));

            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 1, 1);

        } else {
            p.sendMessage("You already have a species! Remove it through /resetspecies for 25 DNA.");
        }
    }
    public static void refreshTag(Player p) {

        if (Florial.getPlayerData().get(p.getUniqueId()).getSpecies().getMorph() == null) return;

        Bukkit.getServer().getScheduler().runTaskLater(florial, () -> {

            MobDisguise mobDisguise = (MobDisguise) DisguiseAPI.getDisguise(p);
            FlagWatcher watcher = mobDisguise.getWatcher();

            User user = api.getUserManager().getUser(p.getUniqueId());

            String prefix = "";

            String nickname = florial.ess.getUser(p).getNickname() != null ? florial.ess.getUser(p).getNickname() : p.getName();

            assert user != null;
            if (user.getCachedData().getMetaData().getPrefix() != null) {prefix = user.getCachedData().getMetaData().getPrefix();}

            watcher.setCustomName(CC.translate(prefix + nickname));

        }, 40);

    }


    @EventHandler
    public void whenIEat(PlayerItemConsumeEvent event) {

        Player p = event.getPlayer();

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        if (data.getSpecies() != this || data.getSpecies().diet() == null) return;

        Material mat = event.getItem().getType();

        event.setCancelled(true);

        if (this.diet().contains(mat)) {

            int foodBonus = fillingValues.get(mat) != null ? fillingValues.get(mat) : 0;

            p.setFoodLevel(p.getFoodLevel() + foodBonus);

            if (!(p.getSaturation() >= 20)) p.setSaturation(p.getSaturation() + (float) foodBonus/2);
        } else {
            p.setFoodLevel(p.getFoodLevel() + 1);
        }

    }

    @EventHandler
    public void noFallDamage(EntityDamageEvent e) {

        if (e.getCause() != EntityDamageEvent.DamageCause.FALL || (!(e.getEntity() instanceof Player p))) return;

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        if (data.getSpecies() != this || data.getSpecies().sharedAbilities() == null) return;

        if ((this.sharedAbilities().contains(1))) e.setCancelled(true);

    }

    @EventHandler
    public void waterVulnerability(EntityDamageEvent e) {

        if (e.getCause() != EntityDamageEvent.DamageCause.DROWNING || (!(e.getEntity() instanceof Player p))) return;

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        if (data.getSpecies() != this || data.getSpecies().sharedAbilities() == null) return;

        if ((this.sharedAbilities().contains(3))) e.setCancelled(true);

    }

    @EventHandler
    public void flight(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR
        || (!(e.getPlayer().isSneaking()))) return;

        PlayerData data = Florial.getPlayerData().get(e.getPlayer().getUniqueId());

        if (data.getSpecies() != this
                || data.getSpecies().sharedAbilities() == null
                || Cooldown.isOnCooldown("c2", e.getPlayer())
                || this.sharedAbilities().contains(2)) return;

        Player p = e.getPlayer();


        if (!(p.isGliding()) && (p.getInventory().getItemInMainHand().getType() == Material.AIR)) {

            Vector unitVector = new Vector(p.getLocation().getDirection().getX(), 0, p.getLocation().getDirection().getZ()).normalize();
            p.setVelocity(unitVector.multiply(3));

             p.setGliding(true);

             p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 2);

             String text = CC.translate("#FFCDDA&lF#FFA0B9&ll#FFA9BF&lo#FFB0C4&lr#FF84A3&li#FF6A90&la#FF5882&ll ⚫&f You are now flying!#fc7878 Left-Click + Sneak to get out of this!");

             p.sendMessage(text);

             Cooldown.addCooldown("c2", p, 4);
        }
        else if (p.isGliding()) {
            p.setGliding(false);
        }

    }

    @EventHandler
    public void lowLightVulnerable(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)
                || Florial.getPlayerData().get(event.getEntity().getUniqueId()).getSpecies() != this
                || this.sharedAbilities() == null || (!(this.sharedAbilities().contains(4)))) return;


        if (event.getEntity().getLocation().getBlock().getLightLevel() > 10) return;


        event.setDamage(event.getDamage() + 6);

    }

    @EventHandler
    public void fireVulnerability(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)
                || (event.getCause() != EntityDamageEvent.DamageCause.FIRE)
                || Florial.getPlayerData().get(event.getEntity().getUniqueId()).getSpecies() != this
                || this.sharedAbilities() == null
                || ((!this.sharedAbilities().contains(5)))) return;

        event.setDamage(event.getDamage() + 6);
    }


}
