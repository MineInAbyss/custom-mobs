package com.offz.spigot.mobzy.GUI.Layouts;

import com.derongan.minecraft.guiy.gui.*;
import com.offz.spigot.mobzy.GUI.MobzyGUI;
import com.offz.spigot.mobzy.GUI.MobzyPropertyElement;
import com.offz.spigot.mobzy.Spawning.MobSpawn;
import de.erethon.headlib.HeadLib;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MobConfigLayout extends Layout {
    private MobzyGUI main;
    private Map<String, Object> spawn;
    private String regionName;
    private List<MobzyPropertyElement> mobProperties = new ArrayList<>();
    private List<MobzyPropertyElement> unusedProperties = new ArrayList<>();
    private FillableElement grid;
    private ScrollingPallet scrollingPallet;

    public MobConfigLayout(MobzyGUI main, Map<String, Object> spawn, String regionName) {
        super();
        this.main = main;
        this.spawn = spawn;
        this.regionName = regionName;
        getMobOptions();

        //create grid with properties
        grid = new FillableElement(4, 8);
        addElement(0, 0, grid);
        mobProperties.forEach(grid::addElement);

        //create scrolling pallet with unused properties
        scrollingPallet = new ScrollingPallet(9);
        addElement(0, 4, scrollingPallet);
        Bukkit.broadcastMessage(unusedProperties.toString());
        unusedProperties.forEach(scrollingPallet::addTool);

        ClickableElement save = new ClickableElement(Cell.forItemStack(HeadLib.CHECKMARK.toItemStack(), "Save"));
        addElement(4, 5, save);

        save.setClickAction(clickEvent -> main.saveConfigValues(spawn, mobProperties));

        main.addBackButton(this);
    }

    public static Cell itemTemplate(HeadLib head, String name, String lore) {
        ItemStack item = head.toItemStack();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList(lore));
        item.setItemMeta(meta);
        return (Cell) Cell.forItemStack(item);
    }

    public List<MobzyPropertyElement> getMobProperties() {
        return mobProperties;
    }

    public List<MobzyPropertyElement> getUnusedProperties() {
        return unusedProperties;
    }

    public void moveToUnused(MobzyPropertyElement property) {
        mobProperties.remove(property);
        unusedProperties.add(property);
        reloadProperties();
    }

    public void moveToUsed(MobzyPropertyElement property) {
        unusedProperties.remove(property);
        mobProperties.add(property);
        reloadProperties();
    }

    public void reloadProperties() {
        grid.clear();
        mobProperties.forEach(grid::addElement);
        scrollingPallet.clear();
        unusedProperties.forEach(scrollingPallet::addTool);
    }

    public void getMobOptions() {
        if (spawn.containsKey("reuse")) {
            makeProperty(HeadLib.QUARTZ_M, MobzyPropertyElement.PropertyType.STRING_INPUT, "reuse");

        }

        makeProperty(HeadLib.QUARTZ_M, MobzyPropertyElement.PropertyType.STRING_INPUT, "mob");
        makeProperty(HeadLib.QUARTZ_P, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "priority");
        makeProperty(HeadLib.STONE_A, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "min-amount");
        makeProperty(HeadLib.WOODEN_A, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "max-amount");
        makeProperty(HeadLib.STONE_G, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "min-gap");
        makeProperty(HeadLib.WOODEN_G, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "max-gap");
        makeProperty(HeadLib.OBJECT_BLUE_LANTERN, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "min-light");
        makeProperty(HeadLib.OBJECT_LIT_BLUE_LANTERN, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "max-light");
        makeProperty(HeadLib.PLAIN_DARK_YELLOW, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "min-time");
        makeProperty(HeadLib.PLAIN_YELLOW, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "max-time");
        makeProperty(HeadLib.STONE_Y, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "min-y");
        makeProperty(HeadLib.WOODEN_Y, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "max-y");
        makeProperty(HeadLib.QUARTZ_R, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "radius");
        makeProperty(HeadLib.PLAIN_GRASS_GREEN, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "spawn-pos");
        makeProperty(HeadLib.PLAIN_WHITE, MobzyPropertyElement.PropertyType.DOUBLE_INPUT, "block-whitelist");

    }

    private MobzyPropertyElement makeProperty(HeadLib head, MobzyPropertyElement.PropertyType type, String key) {
        if (spawn.containsKey(key)) {
            MobzyPropertyElement property = makeProperty(head, type, key, spawn.get(key));
            mobProperties.add(property);
            return property;
        } else {
            Bukkit.broadcastMessage("Loading: " + key);
            MobzyPropertyElement property = makeProperty(head, type, key, getDefaultValueForKey(key));
            unusedProperties.add(property);
            return property;
        }
    }

    public Object getDefaultValueForKey(String key){
        MobSpawn defaultSpawn = new MobSpawn.Builder().build();
        if (key.equals("mob"))
            return "ENITTY_TYPE_HERE";
        if (key.equals("priority"))
            return defaultSpawn.getBasePriority();
        if (key.equals("min-amount"))
            return defaultSpawn.getMinAmount();
        if (key.equals("max-amount"))
            return defaultSpawn.getMaxAmount();
        if (key.equals("min-gap"))
            return defaultSpawn.getMinGap();
        if (key.equals("max-gap"))
            return defaultSpawn.getMaxGap();
        if (key.equals("min-light"))
            return defaultSpawn.getMinLightLevel();
        if (key.equals("max-light"))
            return defaultSpawn.getMaxLightLevel();
        if (key.equals("min-time"))
            return defaultSpawn.getMinTime();
        if (key.equals("max-time"))
            return defaultSpawn.getMaxTime();
        if (key.equals("min-y"))
            return defaultSpawn.getMinY();
        if (key.equals("max-y"))
            return defaultSpawn.getMaxY();
        if (key.equals("radius"))
            return defaultSpawn.getRadius();
        if (key.equals("spawn-pos")) {
            return defaultSpawn.getSpawnPos();
        }
        if (key.equals("block-whitelist")) {
            return defaultSpawn.getWhitelist();
        } else
            return null;
    }

    private MobzyPropertyElement makeProperty(HeadLib head, MobzyPropertyElement.PropertyType type, String key, Object value) {
        return new MobzyPropertyElement(itemTemplate(head, key, value.toString()), type, key, value, main, spawn, this);
    }

}
