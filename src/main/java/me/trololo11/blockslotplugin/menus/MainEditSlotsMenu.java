package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.CustomSlot;
import me.trololo11.blockslotplugin.utils.Menu;
import me.trololo11.blockslotplugin.utils.SlotType;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class MainEditSlotsMenu extends Menu {

    private Player editPlayer;
    private SlotsManager slotsManager;
    private SavesManager savesManager;
    private final ItemStack freeSlot;
    private HashMap<SlotType, ItemStack> slotsItems = new HashMap<>();
    private SlotType[] currSlots;

    public MainEditSlotsMenu(Player editPlayer, SlotsManager slotsManager, SavesManager savesManager){
        this.editPlayer = editPlayer;
        this.slotsManager = slotsManager;
        this.savesManager = savesManager;

        //We change every item's localized name to be "invcustomslot" to not interfere with the custom slots that the player could have in their inventory
        for(Map.Entry<SlotType, CustomSlot> customSlot : slotsManager.getCustomSlotsMap().entrySet()){
            ItemStack item = customSlot.getValue().getItem();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLocalizedName("inv"+itemMeta.getLocalizedName());
            item.setItemMeta(itemMeta);

            slotsItems.put(customSlot.getKey(), item);
        }

        currSlots = slotsManager.getPlayerInvSlots(editPlayer);
        freeSlot = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "free-slot");
    }

    @Override
    public String getTitle() {
        return "&6&lEditing slots of " + editPlayer.getName();
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack grayFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack loadSave = Utils.createItem(Material.BLUE_DYE, "&9&lLoad save", "load-save", "&7Choose a save that you want", "&7to load for this player");
        ItemStack addSave = Utils.createItem(Material.LIGHT_BLUE_DYE, "&3&lAdd save", "add-save", "&7Add this configuration", "&7as a new save.");
        ItemStack fillInventory = Utils.createItem(Material.RED_CONCRETE, "&c&lFill inventory", "fill-inv", "&7Click to fill this inventory", "&7with blocked slots");
        ItemStack clearInventory = Utils.createItem(Material.FEATHER, "&f&lClear inventory", "clear-inv", "&7Clears the inventory of every custom slot");
        ItemStack offhandSlot = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, "&b&lOffhand slot", "offhand-slot");
        ItemStack apply = Utils.createItem(Material.GREEN_DYE, "&a&lApply slots", "apply");
        ItemStack exit = Utils.createItem(Material.RED_DYE, "&c&lExit", "exit");

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

        inventory.setItem(2, loadSave);
        inventory.setItem(3, addSave);
        inventory.setItem(4, offhandSlot);
        inventory.setItem(5, fillInventory);
        inventory.setItem(6, clearInventory);
        inventory.setItem(0, exit);
        inventory.setItem(8, apply);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

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

            case RED_CONCRETE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "fill-inv")) return;

                for(int i=9; i < 45; i++){
                    setSlotType(i, SlotType.BLOCKED);
                }
            }

            case FEATHER -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "clear-inv")) return;

                for(int i=9; i < 45; i++){
                    inventory.setItem(i, freeSlot);
                }
                currSlots = new SlotType[37];
            }

            case BLUE_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "load-save")) return;

                new SelectSaveMenu(this, savesManager).open(player);
            }

            case RED_DYE -> {
                if (!Utils.isLocalizedEqual(item.getItemMeta(), "exit")) return;

                player.closeInventory();
            }

            case GREEN_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "apply")) return;

                slotsManager.setPlayerInvSlots(editPlayer, currSlots);
                player.sendMessage(ChatColor.GREEN + "Successfully set the custom slots for "+ editPlayer.getName());
                player.closeInventory();
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
        }else{
            currSlots[slot] = slotType;
        }
        inventory.setItem(slot, slotType == null ? freeSlot : slotsItems.get(slotType));
    }

}
