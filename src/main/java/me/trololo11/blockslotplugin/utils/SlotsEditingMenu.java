package me.trololo11.blockslotplugin.utils;

import me.trololo11.blockslotplugin.managers.SlotsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * This class makes the backbones system that allows for editing custom slots in a GUI. <br>
 * By itself it creates an inventory which first row of the inventory is
 * a gray strip where you can add custom options and the rest of the inventory
 * is a blank slots where the players can edit slots. <br>
 * When using it is recommended that you override methods {@link Menu#handleMenu(InventoryClickEvent)} and
 * {@link Menu#setMenuItems(Player)} and add a call to the super method at the top of it to
 * customize the menu. <br> <br>
 * The array that represents the current custom slots order is called <b>currSlots</b> and it
 * starts of blank.
 */
public abstract class SlotsEditingMenu extends Menu{

    private HashMap<SlotType, ItemStack> slotsItems = new HashMap<>();
    private final ItemStack freeSlot = SlotsManager.FREE_SLOT;
    private final ItemStack offhandSlot = SlotsManager.OFFHAND_SLOT;

    protected SlotsManager slotsManager;
    /**
     * The representation of the current slots order.
     */
    protected SlotType[] currSlots;


    public SlotsEditingMenu(SlotsManager slotsManager){
        this.slotsManager = slotsManager;

        //We change every item's localized name to be "invcustomslot" to not interfere with the custom slots that the player could have in their inventory
        for(Map.Entry<SlotType, CustomSlot> customSlot : slotsManager.getCustomSlotsMap().entrySet()){
            ItemStack item = customSlot.getValue().getItem();
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setLocalizedName("inv"+itemMeta.getLocalizedName());
            item.setItemMeta(itemMeta);

            slotsItems.put(customSlot.getKey(), item);

        }
        currSlots = new SlotType[37];
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack grayFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack fillInventory = Utils.createItem(Material.RED_CONCRETE, "&c&lFill inventory", "fill-inv", "&7Click to fill this inventory", "&7with blocked slots");
        ItemStack clearInventory = Utils.createItem(Material.FEATHER, "&f&lClear inventory", "clear-inv", "&7Clears the inventory of every custom slot");
        ItemStack offhandSlot;

        for(int i=0; i < 9; i++)
            inventory.setItem(i, grayFiller);

        for(int i=9; i < 45; i++){

            ItemStack slotItem;

            if(i <= 35){
                slotItem = slotsItems.get(currSlots[i]);
            }else{
                slotItem = slotsItems.get(currSlots[i-36]);
            }

            inventory.setItem(i, slotItem == null ? freeSlot : slotItem);
        }

        if(currSlots[36] == null)
            offhandSlot = SlotsManager.OFFHAND_SLOT;
        else
            offhandSlot = slotsItems.get(currSlots[36]);

        inventory.setItem(4, offhandSlot);
        inventory.setItem(5, fillInventory);
        inventory.setItem(6, clearInventory);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if(item.getItemMeta().getLocalizedName().startsWith("invcustomslot")) {
            setSlotType(e.getSlot(), null);
            return;
        }

        switch (item.getType()){
            case WHITE_STAINED_GLASS_PANE -> {
                if(Utils.isLocalizedEqual(item.getItemMeta(), "free-slot")) {
                    setSlotType(e.getSlot(), SlotType.BLOCKED);
                }else if(Utils.isLocalizedEqual(item.getItemMeta(), "offhand-slot")){
                    inventory.setItem(e.getSlot(), slotsItems.get(SlotType.BLOCKED));
                    currSlots[36] = SlotType.BLOCKED;
                }
            }

            case FEATHER -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "clear-inv")) return;

                for(int i=9; i < 45; i++){
                    inventory.setItem(i, freeSlot);
                }
                inventory.setItem(4, freeSlot);
                currSlots = new SlotType[37];
            }

            case RED_CONCRETE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "fill-inv")) return;

                for(int i=9; i < 45; i++){
                    setSlotType(i, SlotType.BLOCKED);
                }
                setSlotType(4, SlotType.BLOCKED);
            }
        }

    }

    /**
     * This function puts to the correct slot in the internal array
     * a correct slot type and also sets the item in the slot.
     * @param slot The slot that we need to the type and slot for
     * @param slotType The type of the slot we want to change it for
     */
    private void setSlotType(int slot, SlotType slotType){
        if(slot > 35){
            currSlots[slot-36] = slotType;
        }else if(slot != 4){
            currSlots[slot] = slotType;
        }else{
            currSlots[36] = slotType;
            inventory.setItem(slot, slotType == null ? offhandSlot : slotsItems.get(slotType));
            return;
        }
        inventory.setItem(slot, slotType == null ? freeSlot : slotsItems.get(slotType));
    }
}
