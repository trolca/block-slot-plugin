package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SeeSaveMenu extends Menu {

    private Menu backMenu;
    private SlotsManager slotsManager;
    private Save save;
    private int index;
    private boolean allowEdit;
    private ConfirmSaveFunction confirmFunction;

    public SeeSaveMenu(Menu backMenu, SlotsManager slotsManager, Save save,int index, boolean allowEdit, ConfirmSaveFunction confirmFunction){
        this.backMenu = backMenu;
        this.slotsManager = slotsManager;
        this.save = save;
        this.index = index;
        this.allowEdit = allowEdit;
        this.confirmFunction = confirmFunction;
    }

    @Override
    public String getTitle() {
        return "&2&lSave &a&l"+(index+1) + "&2&l: &a&l"+save.name;
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack freeSlot = SlotsManager.FREE_SLOT;
        ItemStack backButton = Utils.createItem(Material.RED_DYE, "&c&lBack", "back");
        ItemStack iconSave = Utils.createItem(save.icon, "&2&lSave &a&l"+(index+1) + "&2&l: &a&l"+save.name, "save-icon");
        ItemStack confirm = Utils.createItem(Material.GREEN_DYE, "&2&lConfirm", "confirm");

        for(int i=0; i < 9; i++){
            inventory.setItem(i, darkFiller);
        }

        SlotType[] slots = save.getSaveSlots();
        for(int i=9; i < 45; i++){
            ItemStack setItem;

            if(i <= 35){
                setItem = slots[i] == null ? freeSlot : slotsManager.getSlotClass(slots[i]).getItem();
                inventory.setItem(i, setItem);
            }else{
                setItem = slots[i-36] == null ? freeSlot : slotsManager.getSlotClass(slots[i-36]).getItem();
                inventory.setItem(i, setItem);
            }

        }

        inventory.setItem(4, slots[36] == null ? freeSlot : slotsManager.getSlotClass(slots[36]).getItem());
        inventory.setItem(0, backButton);
        inventory.setItem(2, iconSave);
        inventory.setItem(8, confirm);
        if(allowEdit)
            inventory.setItem(6, Utils.createItem(Material.ORANGE_DYE, "&6&lEdit save", "edit-save"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case RED_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;

                backMenu.open(player);
            }

            case GREEN_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "confirm")) return;

                confirmFunction.run(save);
            }

        }

    }
}
