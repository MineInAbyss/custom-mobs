package com.offz.spigot.mobzy.GUI;

import com.derongan.minecraft.guiy.gui.Cell;
import com.derongan.minecraft.guiy.gui.ClickableElement;
import com.derongan.minecraft.guiy.gui.Element;
import com.derongan.minecraft.guiy.gui.Layout;
import com.derongan.minecraft.guiy.gui.inputs.NumberInput;
import com.offz.spigot.mobzy.GUI.Layouts.MobConfigLayout;
import de.erethon.headlib.HeadLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Map;

public class MobzyPropertyElement extends ClickableElement {
    private Cell wrapped;
    private PropertyType type;
    private String key;
    private Object value;
    private MobzyGUI main;
    private Map<String, Object> spawn;
    private MobConfigLayout mobConfigLayout;

    /**
     * @param wrapped         Element to delegate all clicks to.
     * @param key             the key in the config that will be overridden when this element is saved
     * @param value           the value to be put into that key
     * @param mobConfigLayout
     */
    public MobzyPropertyElement(Cell wrapped, PropertyType type, String key, Object value, MobzyGUI main, Map<String, Object> spawn, MobConfigLayout mobConfigLayout) {
        super(wrapped);
        this.wrapped = wrapped;
        this.type = type;
        this.key = key;
        this.value = value;
        this.main = main;
        this.spawn = spawn;
        this.mobConfigLayout = mobConfigLayout;

        setClickAction(clickEvent -> {
            if (this.mobConfigLayout.getUnusedProperties().contains(this)) {
                this.mobConfigLayout.moveToUsed(this);
                Bukkit.broadcastMessage("moved to used");
            } else if (type == PropertyType.INTEGER_INPUT || type == PropertyType.DOUBLE_INPUT)
                main.setElement(getNumber());
        });
        //TODO read String input
        //else if(type == PropertyType.STRING_INPUT)

    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    private Layout inputLayoutTemplate() {
        Layout layout = new Layout();
        main.addBackButton(layout);
        if (mobConfigLayout.getMobProperties().contains(this)) {
            ClickableElement delete = new ClickableElement(Cell.forItemStack(HeadLib.PLAIN_RED.toItemStack(ChatColor.RED + "Delete")));
            delete.setClickAction(clickEvent -> {
                mobConfigLayout.moveToUnused(this);
                main.backInHistory();
            });
            layout.addElement(0, 5, delete);
        }
        return layout;
    }

    private Layout getNumber() {
        Layout layout = inputLayoutTemplate();
        NumberInput numberInput = new NumberInput();

        Element cell = Cell.forMaterial(Material.DIAMOND_BLOCK, "Submit Number");
        ClickableElement button = new ClickableElement(cell);

        layout.addElement(4, 5, button);
        layout.addElement(0, 0, numberInput);

        button.setClickAction(clickEvent -> {
            numberInput.onSubmit();
        });

        numberInput.setSubmitAction(value -> {
            main.backInHistory();
            ItemStack item = wrapped.itemStack;
            ItemMeta meta = item.getItemMeta();

            if (type == PropertyType.INTEGER_INPUT) {
                this.value = value.intValue();
                meta.setLore(Collections.singletonList(value.intValue() + ""));
            } else if (type == PropertyType.DOUBLE_INPUT) {
                this.value = value;
                meta.setLore(Collections.singletonList(value.toString()));
            }

            item.setItemMeta(meta);
        });

        return layout;
    }

    public enum PropertyType {
        STRING_INPUT, INTEGER_INPUT, DOUBLE_INPUT
    }
}
