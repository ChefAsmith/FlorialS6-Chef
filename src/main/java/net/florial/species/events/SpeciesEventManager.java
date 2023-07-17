package net.florial.species.events;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.CatWatcher;
import net.florial.Florial;
import net.florial.features.age.Age;
import net.florial.models.PlayerData;
import net.florial.species.Species;
import net.florial.species.disguises.Morph;
import net.florial.species.events.impl.SpeciesSwitchEvent;
import net.florial.species.events.impl.SpeciesTablistEvent;
import net.florial.utils.GeneralUtils;
import net.florial.utils.game.ChangeTablistSkin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class SpeciesEventManager implements Listener  {

    private static final Florial florial = Florial.getInstance();

    private static final Morph morph = new Morph();

    private static final ChangeTablistSkin tabList = new ChangeTablistSkin();

    @EventHandler
    public void whenISwitch(SpeciesSwitchEvent event) {

        PlayerData data = event.getPlayerData();

        data.setSpecieId(event.getSpecie().getId());

        data.setAge(data.getSpecieId() == 3 ? Age.YOUNG_ADULT : Age.KIT);

        morph.activate(event.getPlayer(), 0, false, false, data.getSpecies());

        data.getSpecies().effects().forEach(effect -> data.getPlayer().removePotionEffect(effect.getType()));

        GeneralUtils.runAsync(new BukkitRunnable() {@Override public void run() {
            Bukkit.getScheduler().runTaskLater(florial, data::refresh, 40L);}});
    }

    @EventHandler
    public void tabListChange(SpeciesTablistEvent event) {

        PlayerData data = Florial.getPlayerData().get(event.getPlayer().getUniqueId());

        if (data.getSpecies().getMorph() == null) return;



        Player p = event.getPlayer();

        DisguiseAPI.getDisguise(p).setHideArmorFromSelf(true);

        Species.refreshTag(p);

        if (p.getName().contains(".")) return;


        switch (data.getSpecieType().getId()) {
            case 1 -> {
                MobDisguise mobDisguise = (MobDisguise) DisguiseAPI.getDisguise(p);

                CatWatcher catWatcher = ((CatWatcher) mobDisguise.getWatcher());
                int index = 0;
                for (String match : morph.textureNames) {
                    if (match.contains(catWatcher.getType().toString())) {
                        tabList.activate(p, morph.skinTextures.get(index));
                        break;
                    }
                    index++;
                }
            }
            case 2 -> tabList.activate(p, "http://textures.minecraft.net/texture/d59d8c84bda200491e151ecb6e6ba8478c05391d120709e1ef4dfa79d81e271d");

            case 4 -> tabList.activate(p, "http://textures.minecraft.net/texture/48ae65f811f1d95fcc2917eccec6e1dfe864ea147bba2d829c25dbc42596a96e");
        }

    }
}
