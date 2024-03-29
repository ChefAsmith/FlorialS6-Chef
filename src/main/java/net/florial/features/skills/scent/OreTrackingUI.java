package net.florial.features.skills.scent;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.florial.Florial;
import net.florial.features.skills.Skill;
import net.florial.models.PlayerData;
import net.florial.utils.game.MaterialDetector;
import net.florial.utils.general.CC;
import net.florial.utils.general.CustomItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public class OreTrackingUI {

    private static final MaterialDetector MaterialDetector = new MaterialDetector();

    public void trackingUIOre(Player p) {

        RyseInventory.builder()
                .title(CC.translate("&f七七七七七七七七七七七七七七七七\uE250"))
                .rows(6)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {

                        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());

                        int scent = data.getSkills().get(Skill.SCENT);

                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1, 1);

                        List<ItemStack> entries = Stream.of(CustomItem.MakeItem(new ItemStack(Material.COAL_ORE), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "COAL", "1"), scent), false),
                                        CustomItem.MakeItem(new ItemStack(Material.IRON_ORE), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "IRON", "2"), scent), false),
                                        CustomItem.MakeItem(new ItemStack(Material.EMERALD_ORE), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "EMERALD", "3"), scent), false),
                                        CustomItem.MakeItem(new ItemStack(Material.LAPIS_ORE), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "LAPIS", "4"), scent), false),
                                        CustomItem.MakeItem(new ItemStack(Material.DIAMOND_ORE), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "DIAMOND", "5"), scent), false))
                                .toList();

                        contents.set(List.of(27), IntelligentItem.of(entries.get(0), event -> oreLocate(Material.COAL_ORE, p, scent, 0)));
                        contents.set(List.of(28), IntelligentItem.of(entries.get(1), event -> oreLocate(Material.IRON_ORE, p, scent, 2)));
                        contents.set(List.of(29), IntelligentItem.of(entries.get(2), event -> oreLocate(Material.EMERALD_ORE, p, scent, 3)));
                        contents.set(List.of(30), IntelligentItem.of(entries.get(3), event -> oreLocate(Material.LAPIS_LAZULI, p, scent, 4)));
                        contents.set(List.of(31), IntelligentItem.of(entries.get(4), event -> oreLocate(Material.DIAMOND_ORE, p, scent, 5)));


                    }
                })
                .build(Florial.getInstance())
                .open(p);

    }

    private static void oreLocate(Material mat, Player p, int scent, int required) {
        Sound sound;

        p.closeInventory();

        if (scent >= required) {

            int nearby = MaterialDetector.detectMaterial(p, mat, scent*5);

            p.sendMessage(CC.translate("#ffd7dc&l&nF#ffb8c1&l&nl#ff99a6&l&no#ff7a8b&l&nr#ff5b70&l&ni#ff3c55&l&na#ff1d3a&l&nl&r #ff3c55&l➤&f Amount Nearby: " + nearby));
            sound = Sound.ENTITY_PLAYER_BREATH;

        } else {
            sound = Sound.BLOCK_NOTE_BLOCK_BASS;
            p.sendMessage(CC.translate("#ffd7dc&l&nF#ffb8c1&l&nl#ff99a6&l&no#ff7a8b&l&nr#ff5b70&l&ni#ff3c55&l&na#ff1d3a&l&nl&r #ff3c55&l➤&c You need Scent level " +  required + " for this, but you only have " + scent));
        }
        p.playSound(p.getLocation(), sound, 1, 1);

    }


    private static String format(List<String> iterations, int scent){
        scent = 60-(scent*10);
        return "  #5a372c&l︳ " + iterations.get(0) +
                "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳ • REQUIRES: #6e4837\n #6e4837&l︳ • SCENT LVL: #6e4837 "
                + iterations.get(1) + "\n #6e4837&l︳ • COOLDOWN:#6e4837 "
                + scent + "\n #6e4837&l︳ • [CLICK HERE]" +
                "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙";}

}

