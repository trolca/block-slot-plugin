package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.utils.CustomSlot;
import me.trololo11.blockslotplugin.utils.SlotType;
import me.trololo11.blockslotplugin.utils.Utils;
import me.trololo11.blockslotplugin.utils.slots.BlockedSlot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This class stores all the players slots data in an internal hash map.
 * The data of player slots is stored in an array where null is a normal slot and
 * a non-null value of {@link SlotType} is the specified slot type. <br>
 * Every inv slots data list have the size of 37 (36 slots for the inv and 1 for the offhand)
 * Indexes from 0-8 are the hotbar of the player, from 9-35 the inventory starting from the top left and index 36 is for the offhand.
 */
public class SlotsManager {

    /**
     * A general item representing the null value in the custom slots array. It's localized name is "free-slot".
     * It should be used to show a slot with no custom slot in the inventory
     */
    public static final ItemStack FREE_SLOT = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "free-slot");
    /**
     * A general item representing the null value in the custom slots array for the index 36 the offhand slot.
     * It's localized name is "offhand-slot".
     * It should be used to show that the offhand slot has no custom item in it.
     */
    public static final ItemStack OFFHAND_SLOT = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, "&b&lOffhand slot", "offhand-slot");

    /**
     * A hash map of every online player's inventory slots data. The key is the player
     * and the array which is the slot data which is more explained in the class doc.
     * Every inv slots data list should have size of 37 (36 slots for the inv and 1 for the offhand) <br>
     * Indexes from 0-8 are the hotbar of the player, from 9-35 the inventory starting from the top left and index 36 is for the offhand.
     */
    private HashMap<Player, SlotType[]> playerInventorySlots = new HashMap<>();
    private HashMap<SlotType, CustomSlot> customSlotsMap;

    public SlotsManager(){
        customSlotsMap = new HashMap<>();
        customSlotsMap.put(SlotType.BLOCKED, new BlockedSlot());
    }

    /**
     * Adds an inv slots value to the hash map.<br>
     * This function does not set any items for the player's inventory
     * If there is already a record then it will do nothing. <br>
     * This should be only used on setting the initial data when player joins.
     * @param player The player to add the data to.
     * @param invSlots The inv slots data. This should have the size of 37 (36 slots for the inv and 37 for the offhand).
     */
    public void addPlayerInvSlots(Player player, SlotType[] invSlots){
        if(invSlots.length != 37)
            throw new IllegalArgumentException("The size of inv slots list should be 37!");

        if(!playerInventorySlots.containsKey(player))
            playerInventorySlots.put(player, invSlots);
    }


    /**
     * Updates and sets a player's inv slots data.
     * On update the player's inventory is going to be updated with the new slots' layout.
     * @param player The player to the slots data to.
     * @param invSlots The slots' data. If null it's going to delete the player's slots data.
     */
    public void setPlayerInvSlots(Player player, SlotType[] invSlots){
        if(invSlots == null){
            removePlayerInvSlot(player);
            for(CustomSlot customSlot : customSlotsMap.values())
                player.getInventory().remove(customSlot.getItem());
            return;
        }
        if(invSlots.length != 37)
            throw new IllegalArgumentException("The size of inv slots list should be 37!");

        boolean everyNull = true;

        SlotType[] oldSlots = playerInventorySlots.get(player);

        for(int i=0; i < 36; i++){
            SlotType slotType = invSlots[i];

            //ik I'm checking 2 times but idc
            if(slotType != null)
                everyNull = false;

            if(slotType != null && (oldSlots == null || oldSlots[i] != slotType)){
                ItemStack oldItem = player.getInventory().getItem(i);

                if(oldItem != null) player.getWorld().dropItemNaturally(player.getLocation(), oldItem);
                player.getInventory().setItem(i, customSlotsMap.get(slotType).getItem());
            }else if(slotType == null && oldSlots != null && oldSlots[i] != null){
                player.getInventory().setItem(i, null);
            }

        }

        ItemStack offhandSlot = customSlotsMap.get(invSlots[36]) == null ? null : customSlotsMap.get(invSlots[36]).getItem();
        player.getInventory().setItem(40, offhandSlot);

        if(everyNull){
            removePlayerInvSlot(player);
            return;
        }

        playerInventorySlots.put(player, invSlots);
    }

    /**
     * Gets the player inv slots.
     * @param player The player to get the info.
     * @return The list of the players slots
     */
    public SlotType[] getPlayerInvSlots(Player player){
        SlotType[] slots = playerInventorySlots.get(player);

        return slots == null ? new SlotType[37] : Arrays.copyOf(slots, 37);
    }

    public CustomSlot getSlotClass(SlotType slotType){
        return customSlotsMap.get(slotType);
    }

    /**
     * Returns a copy of the hash map which stores all of the {@link CustomSlot} classes
     * of every {@link SlotType}.
     * @return A <b>copy</b> of the hash map
     */
    public HashMap<SlotType, CustomSlot> getCustomSlotsMap() {
        return (HashMap<SlotType, CustomSlot>) customSlotsMap.clone();
    }

    /**
     * Removes a player from the hash map. <br>
     * Note this doesn't save the data to the database as it only removes
     * the data from the hash map, so it's recommended to save the data
     * before removing the player.
     * @param player The player to remove
     */
    public void removePlayerInvSlot(Player player){
        playerInventorySlots.remove(player);
    }

}
