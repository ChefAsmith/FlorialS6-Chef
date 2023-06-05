package net.florial.menus.species;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.florial.Florial;
import net.florial.features.age.Age;
import net.florial.features.quests.Quest;
import net.florial.models.PlayerData;
import net.florial.utils.general.CC;
import net.florial.utils.general.CustomItem;
import net.florial.utils.general.VaultHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class GrowthMenu {

    private static final net.florial.menus.species.SpeciesMenu SpeciesMenu = new SpeciesMenu();

    public void growthMenu(Player p) {

        RyseInventory.builder()
                .title(CC.translate("&f七七七七七七七七七七七七七七七七\uE600"))
                .rows(6)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {

                        UUID u = p.getUniqueId();

                        PlayerData data = Florial.getPlayerData().get(u);

                        Age age = data.getAge();
                        int requiredDNA = age.getRequiredDNA();
                        int requiredQuests = age.getRequiredQuests();

                        String questDisplay = Florial.getQuest().get(u) != null ? "RE-ROLL QUEST:" : "GET QUEST:";
                        String rollPrice = Florial.getQuest().get(u) != null ? "Price: " + Quest.rollFormula(VaultHandler.getBalance(p)) : "";

                        List<ItemStack> entries = Stream.of(CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", "  #5a372c&l︳ " +
                                "CLICK HERE TO GROW UP\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳ • YOUR AGE: #6e4837 "
                                + age + "\n #5a372c&l︳  INCREASE BY:\n #6e4837&l︳ •#6e4837 "
                                + requiredDNA + " DNA\n #6e4837&l︳ • " + requiredQuests + " Quests\n#6e4837&l︳ • [CLICK HERE]\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳#5a372c&l INFORMATION\n #6e4837&l︳ • HOVER OVER:\n #6e4837&l︳#6e4837 the other menu options "
                                + "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙", false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", "  #5a372c&l︳ " +
                                                "GET-A-QUEST\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳ • COMPLETED QUESTS: #6e4837 "
                                                +  data.getGrowth() + "\n #5a372c&l︳  " + questDisplay + "\n #6e4837&l︳ •#6e4837 [CLICK ME]\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳#5a372c&l INFORMATION\n #6e4837&l︳ • INFO:" +
                                                "\n #6e4837&l︳#6e4837 • 1 quest gives 1 DNA" +
                                                "\n #6e4837&l︳#6e4837 • Complete " + requiredQuests + " and get " + requiredDNA + " DNA to Age Up"
                                                + "\n #6e4837&l︳#6e4837 • " + rollPrice
                                                + "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙", false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", "  #5a372c&l︳ " +
                                                "GO BACK\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙", false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "KIT", "BE BORN", "Less Health", "Can't Upgrade Skills", "Only 1 Den(/sethome)", "Small")), false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "ADOLESCENT", "Click the Grow Button in this GUI", "+1 Upgrade Slot; All Skills", "+1 heart", "+1 Den(/sethome)", "You're bigger now")), false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "YOUNG ADULT", "Click the Grow Button in this GUI", "+1 Upgrade Slot; All Skills", "+1 heart", "-", "-")), false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "ADULT", "Click the Grow Button in this GUI", "+1 Upgrade Slot; All Skills", "-", "Species-Unique Area; /colony", "-")), false),
                                        CustomItem.MakeItem(new ItemStack(Material.MAP), "#5a372c&l ┍━━━━━━━━━━━━━━━━━━┑", format(List.of(
                                                "ELDER", "Click the Grow Button in this GUI", "2x DNA Multiplier", "+1 Upgrade Slot; All Skills", "/warp potions", "Unlock Rebirth - /reincarnation")), false)).map(i -> NBTEditor.set(i, 1010, "CustomModelData"))
                                .toList();
                        //3,4,5,6
                        contents.set(List.of(0, 1), IntelligentItem.of(entries.get(2), event -> loadMenu(p)));
                        contents.set(List.of(3,4,5,6), IntelligentItem.of(entries.get(0), event -> {
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);
                            Age.up(p, data, age, requiredQuests, requiredDNA);
                        }));
                        contents.set(List.of(12,13,14), IntelligentItem.of(entries.get(1), event -> {
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);
                            Quest.give(p, false, null);
                        }));
                        contents.set(List.of(9,10,18,19,28), IntelligentItem.of(entries.get(3), event -> {p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);}));
                        contents.set(List.of(16,17,25,26), IntelligentItem.of(entries.get(4), event -> {p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);}));
                        contents.set(List.of(36,37,45,46), IntelligentItem.of(entries.get(5), event -> {p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);}));
                        contents.set(List.of(43,44,52,53), IntelligentItem.of(entries.get(6), event -> {p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);}));
                        contents.set(List.of(21,22,23,30,31,32,33,39,40,41), IntelligentItem.of(entries.get(7), event -> {p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 2, 1);}));
                    }
                })
                .build(Florial.getInstance())
                .open(p);

    }

    private static void loadMenu(Player p) {

        p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_STEP, 1, 1);

        p.closeInventory();

        SpeciesMenu.speciesMenu(p);
    }

    private static String format(List<String> iterations){

        return  "  #5a372c&l︳ "
                + iterations.get(0) + "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳ • HOW TO GET:\n #5a372c&l︳ "
                + iterations.get(1) + "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙\n #6e4837&l︳#5a372c&l INFORMATION\n #6e4837&l︳ • INFO:" +
                "\n #6e4837&l︳#6e4837 • "
                + iterations.get(2) +
                "\n #6e4837&l︳#6e4837 • "
                + iterations.get(3) +
                "\n #6e4837&l︳#6e4837 • "
                + iterations.get(4) +
                "\n #6e4837&l︳#6e4837 • "
                + iterations.get(5) + "\n #5a372c&l┕━━━━━━━━━━━━━━━━━━┙";
    }

}

