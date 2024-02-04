package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.utils.CustomSlot;
import me.trololo11.blockslotplugin.utils.SlotType;
import me.trololo11.blockslotplugin.utils.slots.BlockedSlot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class stores all the players slots data in an internal hash map.
 * The data of player slots is stored in an array where null is a normal slot and
 * a non-null value of {@link SlotType} is the specified slot type.
 */
public class SlotsManager {



    /**
     * A hash map of every online player's inventory slots data. The key is the player
     * and the array which is the slot data which is more explained in the class doc.
     * Every inv slots data list should have size of 37 (36 slots for the inv and 37 for the offhand)
     */
    private HashMap<Player, List<SlotType>> playerInventorySlots = new HashMap<>();
    private HashMap<SlotType, CustomSlot> customSlotsMap;

    public SlotsManager(){
        customSlotsMap = new HashMap<>();
        customSlotsMap.put(SlotType.BLOCKED, new BlockedSlot());
    }

    /**
     * Adds an inv slots value to the hash map.
     * If there is already a record then it will do nothing. <br>
     * This should be only used on setting the initial data when player joins.
     * @param player The player to add the data to.
     * @param invSlots The inv slots data. This should have the size of 37 (36 slots for the inv and 37 for the offhand).
     */
    public void addPlayerInvSlots(Player player, List<SlotType> invSlots){
        if(invSlots.size() != 37)
            throw new IllegalArgumentException("The size of inv slots list should be 37!");

        if(!playerInventorySlots.containsKey(player))
            setPlayerInvSlots(player, invSlots);
    }

    /**
     * Updates a player's inv slots data.
     * On update the player's inventory is going to be updated with the new slots' layout.
     * @param player The player to the slots data to.
     * @param inv The slots data.
     */
    public void setPlayerInvSlots(Player player, List<SlotType> inv){

        for(int i=0; i < 36; i++){
            SlotType slotType = inv.get(i);

            if(slotType != null){
                ItemStack oldItem = player.getInventory().getItem(i);

                if(oldItem != null) player.getWorld().dropItemNaturally(player.getLocation(), oldItem);
                player.getInventory().setItem(i, customSlotsMap.get(slotType).getItem());
            }
        }

        player.getInventory().setItem(40, customSlotsMap.get(inv.get(36)).getItem());

        playerInventorySlots.put(player, inv);

    }

    /**
     * Gets the player inv slots.
     * @param player The player to get the info.
     * @return A <b>unmodifiable</b> list of the players slots
     */
    public List<SlotType> getPlayerInvSlots(Player player){
        return Collections.unmodifiableList(playerInventorySlots.get(player));
    }

    /**
     * Removes a player from the hash map. <br>
     * Note this doesn't save the data to the database as it only removes
     * the data from the hash map, so it's recommended to save the data
     * before removing the player
     * @param player The player to remove
     */
    public void removePlayerInvSlot(Player player){
        playerInventorySlots.remove(player);
    }

}
