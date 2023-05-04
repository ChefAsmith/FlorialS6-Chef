package net.florial.species.impl;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.florial.Florial;
import net.florial.features.skills.Skill;
import net.florial.models.PlayerData;
import net.florial.species.Species;
import net.florial.species.disguises.Morph;
import net.florial.utils.Cooldown;
import net.florial.utils.math.AgeFormula;
import net.florial.utils.math.GetChance;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Fox extends Species implements Listener {

    private static final Morph morph = new Morph();
    
    public Fox(int id) {
        super("Fox", id, 16, true, DisguiseType.FOX);

    }

    @Override
    public HashMap<Integer, PotionEffect> specific() {

        return new HashMap<>(Map.ofEntries(
                Map.entry(1, new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, 0, false, false, true)),
                Map.entry(2, new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, 1, false, false, true)),
                Map.entry(3, new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, 2, false, false, true))));
    }

    @Override
    public Set<PotionEffect> effects() {

        return new HashSet<>(List.of(
                new PotionEffect(PotionEffectType.SPEED, 1000000, 1, false, false, true),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1, false, false, true)));
    }

    @Override
    public Set<String> descriptions() {
        return new HashSet<>(Arrays.asList(
                "BURROWING", "Haste, double materials, etc"));
    }

    @Override
    public Set<Material> diet() {
        return new HashSet<>((Species.boneFoods));
    }

    @EventHandler
    public void foxBite(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player p) || (!(e.getEntity() instanceof LivingEntity))) return;

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        if (Cooldown.isOnCooldown("c1", p) || data.getSpecies() != this) return;

        e.setDamage((4+data.getAge().getIncrease()));

        p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_FANGS_ATTACK, 1, (float) 0.5);

        Cooldown.addCooldown("c1", p, 6);

    }

    @EventHandler
    public void foxHeatAcclimation(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player p) || e.getCause() != EntityDamageEvent.DamageCause.LAVA && e.getCause() != EntityDamageEvent.DamageCause.FIRE) return;
        
        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

        if (data.getSpecies() != this || (Cooldown.isOnCooldown("c2", p))) return;

        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 1, false, false, true));

        Cooldown.addCooldown("c2", p, AgeFormula.get(30, data.getAge().getIncrease()));

    }

    @EventHandler
    public void doubleMaterials(BlockBreakEvent e) {

        if (Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSpecies() != this
            || (!(Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSkills().get(Skill.SPECIFIC) > 3))) return;

        int value = Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSkills().get(Skill.SPECIFIC);

        int chance = (value == 4) ? 30 : (value == 5) ? 50 : 0;

        if (GetChance.chanceOf(chance)) e.getBlock().getDrops().add(new ItemStack(e.getBlock().getType()));

    }

    @EventHandler
    public void onFoxSneak(PlayerToggleSneakEvent e) {

        if (Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSpecies() == null) return;
        if (DisguiseAPI.getDisguise(e.getPlayer()) == null || DisguiseAPI.getDisguise(e.getPlayer()).getType() != DisguiseType.FOX) return;
        if (Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSpecieId() == 2 || Florial.getPlayerData().get(e.getPlayer().getUniqueId()).getSpecieId() == 4) morph.activate(e.getPlayer(), 1, e.isSneaking(), true, this);

    }


}
