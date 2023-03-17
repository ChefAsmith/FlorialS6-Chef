package net.florial;

import net.florial.features.skills.Skill;
import net.florial.features.upgrades.Upgrade;
import net.florial.models.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Refresh {

    /*
    This is a necessary, unique class
    Over the course of a user's lifetime, he may get
    several various upgrades, or may have skills that give buffs.
    Because of how minecraft works, these buffs fade after a restart or on death
    therefore it's our responsibility to load it everytime he dies or rejoins.
    additionally he may get max health enhancements somehow, someway. it is
    therefore our responsibility to load that too in the off-case it is somehow lost
     */

    public static void load(Player p, PlayerData data) {

        // now additions shall be.. have we anything to add to the max health?
        int additions = 0;
        AtomicReference<Double> maxhealth = new AtomicReference<>(data.getSpecies().getMaxHealth());
        HashMap<Skill, Integer> skills = data.getSkills();
        // shall we get upgrades? if it is null leave it blank! We needn't assign upgrades to players who will never get them, so let that not be null overtime
        HashMap<Upgrade, Boolean> upgrades = data.getUpgrades() != null ? data.getUpgrades() : new HashMap<>();
        int resistance = skills.get(Skill.RESISTANCE);
        int survival = skills.get(Skill.SURVIVAL);
        int specific = skills.get(Skill.SPECIFIC);

        // let's see if the user has upgraded resistance?
        if (resistance > 1) p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, resistance-2, false, false, true));

        if (skills.get(Skill.STRENGTH) > 4) p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 1, false, false, true));
        //we use this statement to check if survival is 20 or just 5. because at those levels max health INCREASES!

        if (survival > 4) additions = survival >= 20 ? 6 : 4;

        if (survival > 14) p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 0, false, false, true));

        //let's loop through their specie's unique skill set and apply all necessary effects
        for (Map.Entry<Integer, PotionEffect> entry : data.getSpecies().specific().entrySet()) {

            boolean applicable = specific >= entry.getKey() && entry.getValue() != null & p.addPotionEffect(Objects.requireNonNull(entry.getValue()));

            if (!(applicable)) break;

        }

        // let's set their maxhealth now
        p.setMaxHealth(maxhealth.get() + additions);

        // OK! let's begin the Great Upgrade Check, and then just stop this whole code if our player hasn't got an upgrade yet!
        if (upgrades.isEmpty()) return;
        Map<Upgrade, Runnable> upgradeHandlers = new HashMap<>() {{
            put(Upgrade.DOUBLEHEALTH, () -> maxhealth.set(Math.max(maxhealth.get(), 40)));
        }};

        for (Map.Entry<Upgrade, Runnable> entry : upgradeHandlers.entrySet()) {
            Upgrade upgrade = entry.getKey();
            Runnable handler = entry.getValue();
            if (upgrades.get(upgrade)) handler.run();
        }
        //this runs twice for the aforementioned reasons.. let's see if we can get this down to running once in some way!
        p.setMaxHealth(maxhealth.get() + additions);
    }

    public static void load(Player player) {
        load(player, Florial.getInstance().getPlayerData(player));
    }
}
