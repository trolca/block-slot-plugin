package me.trololo11.blockslotplugin.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomSlot {

    protected ItemStack item;

    public CustomSlot(){
        item = createItem();

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLocalizedName("customslot-"+getSlotType().toString());
        item.setItemMeta(itemMeta);
    }

    protected abstract ItemStack createItem();

    public ItemStack getItem(){
        return item;
    }

    public abstract SlotType getSlotType();

    public abstract boolean doPickup(ItemStack itemsToPickup);

}
