package me.trololo11.blockslotplugin.utils.slots;

import me.trololo11.blockslotplugin.utils.CustomSlot;
import me.trololo11.blockslotplugin.utils.SlotType;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockedSlot extends CustomSlot {

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(Utils.chat("&4&lBlocked slot"));

        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.BLOCKED;
    }

    @Override
    public boolean doPickup(ItemStack itemsToPickup) {
        return false;
    }
}
