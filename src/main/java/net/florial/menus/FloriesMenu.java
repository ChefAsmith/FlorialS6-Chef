package net.florial.menus;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.florial.Florial;
import net.florial.features.upgrades.Upgrade;
import net.florial.models.PlayerData;
import net.florial.utils.general.CC;
import net.florial.utils.general.CustomItem;
import net.florial.utils.general.GetCustomSkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FloriesMenu {

    private static final GetCustomSkull heads = new GetCustomSkull();

    private static final List<ItemStack> florieItems = List.of(

            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFjNTEzMmYyOWU2NDRhNmQ2YTI0NzkzNWQwOTA2ZWZiZWY5MmEyYzVjMmNmZGFjOWJlNWY2MDVmMzc2Yjg4YSJ9fX0"),
                    "", "", false), 30, "CustomModelData"), List.of(
                    "SUPER-DRILL", "Left-Click with this on any block to", "instantly break it! Forever use.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE3ZDU0Y2E0NWEzOThjMzY0Y2ViYmZmYjUzOTBjZTVlMDM0NWUwYzdiYzVlODYzYWNhYmY1N2QxMzQyYzRiZCJ9fX0"),
                    "", "", false), 31, "CustomModelData"), List.of(
                    "BLUE SUPER-DRILL", "Left-Click with this near any water", "to instantly drain it. Forever use.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTg3OWVkMmIzOWZhMDQ2MmM3NDI5MmY1Y2EzZDE4ODQyMDEyOGI0YTYzYWM3NWRiOGM5N2EwOTRkMWFjNjNmNCJ9fX0"),
                    "", "", false), 32, "CustomModelData"), List.of(
                    "TULIP'S SHADOW", "Left-Click with this to go invisible", "and Left-Click again to be visible. Forever use.", "")),
            floriesArrange(300, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzM4YjBiMGYxZjM1YzU2YjdkNjRlMGUyYjk2NjE4MDFmOTEyZjMxOGZhOWM4YzFkODNlOTE3ZGI0ZjJlNjUyMSJ9fX0"),
                    "", "", false), 33, "CustomModelData"), List.of(
                    "VALHALLA'S BLESSING", "Immune to certain natural damages like", "falling, suffocation, and fire. Forever use.", "")),
            floriesArrange(50, CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhjMjE4YzNmMjFiYmMxYzZmODZhNDc5NTk1ODM3ZTg3M2E3NzA3YjVhYTk4YjQ5ZTAxMDgzMTNlNzFlIn19fQ"),
                    "", "", false), List.of(
                    "/fly command", "fly", "Permanent access to /fly", "")),
            floriesArrange(50, CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhjMjE4YzNmMjFiYmMxYzZmODZhNDc5NTk1ODM3ZTg3M2E3NzA3YjVhYTk4YjQ5ZTAxMDgzMTNlNzFlIn19fQ"),
                    "", "", false), List.of(
                    "/enderchest command", "enderchest", "Permanent access to /enderchest", "")),
            floriesArrange(50, CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhjMjE4YzNmMjFiYmMxYzZmODZhNDc5NTk1ODM3ZTg3M2E3NzA3YjVhYTk4YjQ5ZTAxMDgzMTNlNzFlIn19fQ"),
                    "", "", false), List.of(
                    "/skull command", "skull", "Permanent access to /skull", "")),
          //  floriesArrange(50, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTg3OWVkMmIzOWZhMDQ2MmM3NDI5MmY1Y2EzZDE4ODQyMDEyOGI0YTYzYWM3NWRiOGM5N2EwOTRkMWFjNjNmNCJ9fX0"),
                //    "", "", false), 43, "CustomModelData"), List.of(
                 //   "Rank Crate Key", "Use this by opening the Rank Crate", "at spawn. Chance for a rank. ONE-TIME-USE.", "")),
            floriesArrange(50, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQxMWMyOGVlZTVkNThkMWI4NjNiNTRlNWNjNjJjMzA3MjM0ZDQzN2MxN2YxZmY3NjMzOGRmZWNjM2NjNjhkNSJ9fX0"),
                    "", "", false), 43, "CustomModelData"), List.of(
                    "VIP Rank", "Buy this to get VIP rank!", "", "")),
            floriesArrange(200, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQwN2ZkMDNjMDNjMDViNzQ0Y2ZmN2FjMWE5NWQ4MTYxNzA2MjA2ZjY4YzEyYzRjMjJjOTcwNWY3YzM3ZDA4In19fQ"),
                    "", "", false), 34, "CustomModelData"), List.of(
                    "HEALING ORB", "Left-Click to regain full health.", "Cooldown. Forever use.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(new ItemStack(Material.COOKIE),
                    "", "", false), 35, "CustomModelData"), List.of(
                    "INFINITE COOKIE", "Get full food and saturation when Left-Clicking", "with this weirdly delicious cookie.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5NzNjNGFlOTEyZTQ3ZjcxNGZkZjg3NmU1NmIwOGNkNzI3MjdjODM0ZTg4M2I5MjYzNTJiMTBkMGMyMGEwOSJ9fX0"),
                    "", "", false), 36, "CustomModelData"), List.of(
                    "WATER JUG", "Left-Click with this item to fill", "your thirst. Infinite and permanent use.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhmNzdiMmVmZWZjZjMxYzMzMTUwYzAxNWRlNTMwMzg1MDk1NjMyY2RkNmIyNDZkMzFmOWY3NjY4NmMwZDBmOSJ9fX0"),
                    "", "", false), 37, "CustomModelData"), List.of(
                    "WEATHER MANIPULATOR", "Left-Click with this item to make it stormy.", "Left-Click again to make it sunny. Forever use.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjODhjNzZlM2Q5YTFlYmQ3ZjRhMmU1NWVjYmNjNDJhOGQyM2Y2OTY3ODRhYTQxMGYwOTUxMmEzYjUzYSJ9fX0"),
                    "", "", false), 38, "CustomModelData"), List.of(
                    "DOUBLE HEALTH-U", "Earn doubled health forever.", "Forever use.", "")),
            floriesArrange(75, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjODhjNzZlM2Q5YTFlYmQ3ZjRhMmU1NWVjYmNjNDJhOGQyM2Y2OTY3ODRhYTQxMGYwOTUxMmEzYjUzYSJ9fX0"),
                    "", "", false), 39, "CustomModelData"), List.of(
                    "SELL INCREASE-U", "Permanently increase your sell rates.", "Forever Upgrade.", "")),
            floriesArrange(60, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjODhjNzZlM2Q5YTFlYmQ3ZjRhMmU1NWVjYmNjNDJhOGQyM2Y2OTY3ODRhYTQxMGYwOTUxMmEzYjUzYSJ9fX0"),
                    "", "", false), 41, "CustomModelData"), List.of(
                    "STRONG NOSE-U", "Higher chances for scent to work.", "Forever Upgrade.", "")),
            floriesArrange(100, NBTEditor.set(CustomItem.MakeItem(heads.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjODhjNzZlM2Q5YTFlYmQ3ZjRhMmU1NWVjYmNjNDJhOGQyM2Y2OTY3ODRhYTQxMGYwOTUxMmEzYjUzYSJ9fX0"),
                    "", "", false), 42, "CustomModelData"), List.of(
                    "HASTE II-U", "Gives permanent Haste II.", "Forever Upgrade.", ""))



    );

    private static final List<Integer> floriePrices = List.of(
            100, 100, 100, 300, 50, 50, 50, 50, 50,
            200, 100, 100, 100, 100, 75, 100, 50, 100


    );


    public void floriesMenu(Player p) {

        RyseInventory.builder()
                .title(CC.translate("&f七七七七七七七七七七七七七七七七\uE609"))
                .rows(6)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {

                        Iterator<ItemStack> materialIter = (florieItems).iterator();
                        Iterator<Integer> priceIter = (floriePrices).iterator();

                        for (int i = 9; i < 44; i++) {
                            if (materialIter.hasNext() && priceIter.hasNext()) {
                                ItemStack item = materialIter.next();
                                int price = priceIter.next();
                                contents.set(i, IntelligentItem.of(item, event -> purchase(p, item, price)));
                            }
                        }



                    }
                })
                .build(Florial.getInstance())
                .open(p);

    }

    private static void purchase(Player p, ItemStack item, int price) {

        PlayerData data = Florial.getPlayerData().get(p.getUniqueId());
        p.closeInventory();

        if (data.getFlories() >= price) {

            String name = item.getItemMeta().getLore().get(1);
            name = ChatColor.stripColor(name);

            if (name.contains("command") || name.contains("rank") && (!(name.contains("-U")))) {

                String permission = name.contains("fly") ? "essentials.fly" : name.contains("enderchest") ? "essentials.enderchest" : name.contains("skull") ? "essentials.skull.*" : "";

                if (permission.isBlank()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add vip");
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set " + permission);
                }

            } else {
                if (name.contains("-U")) {

                    if (data.getUpgrades() == null) data.setUpgrades(new HashMap<>(Map.of(Upgrade.DOUBLEHEALTH, false)));


                    int value = NBTEditor.getInt(item, "CustomModelData");

                    switch(value) {
                        case 38 -> data.getUpgrades().put(Upgrade.DOUBLEHEALTH, true);
                        case 39 -> data.getUpgrades().put(Upgrade.SELLINCREASE, true);
                        case 33 -> data.getUpgrades().put(Upgrade.NATUREIMMUNITY, true);
                        case 41 -> data.getUpgrades().put(Upgrade.STRONGNOSE, true);
                        case 42 -> data.getUpgrades().put(Upgrade.HASTE, true);

                    }


                } else {
                    p.getInventory().addItem(item);
                }

            }

            data.setDna(data.getFlories() - price);
            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, (float) 1.3);


        } else {
            p.sendMessage("You don't have enough flories. You need " + price + " but you're short by " + (price - data.getFlories()) + "!");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, (float) 1.3);

        }
    }

    private static ItemStack floriesArrange(int price, ItemStack i, List<String> iterations){

        return CustomItem.MakeItem(i, "#ff79a1&l ┍━━━━━━━━━━━━━━━━━━┑", "  #ff79a1&l︳ "
                + iterations.get(0) + "\n #ff79a1&l┕━━━━━━━━━━━━━━━━━━┙\n #ffa2c4&l︳ • PRICE: #ffa2c4 "
                + price + " flories"
                + "\n#ff79a1&l︳#ffa2c4 "
                + iterations.get(1) + "\n"
                + "\n#ff79a1&l︳#ffa2c4 "
                + iterations.get(2) + "\n"
                + "\n#ff79a1&l︳#ffa2c4 "
                + iterations.get(3) + "\n"
                + " #ff79a1&l︳  [CLICK HERE]:\n#ff79a1&l┕━━━━━━━━━━━━━━━━━━┙", false);
    }

}