package net.florial.features.enemies.impl;

import net.florial.features.enemies.Mob;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Snapper extends Mob implements Listener {
    public Snapper(EntityType entity) {
        super(EntityType.RAVAGER, EntityType.SPIDER, 20, 5, 15, List.of(new ItemStack(Material.ROTTEN_FLESH)));

    }

}
