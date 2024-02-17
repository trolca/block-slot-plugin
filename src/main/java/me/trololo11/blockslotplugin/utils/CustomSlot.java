package me.trololo11.blockslotplugin.utils;

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

    /**
     * Returns a <b>copy</b> of the item that is the label of this slot
     * @return A <b>copy</b> of the item. <br>
     *      Every item returned with this function has a localized name in
     *      the template of "customslot-(slot type)"
     */
    public ItemStack getItem(){
        return item.clone();
    }

    public abstract SlotType getSlotType();

    public abstract boolean doPickup(ItemStack itemsToPickup);

}
