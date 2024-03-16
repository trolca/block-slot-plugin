package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.listeners.RenameSavesListener;
import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.sql.SQLException;

public class AddSaveMenu extends SlotsEditingMenu {

    private Save save;
    private Menu menuBack;
    private RenameSavesListener renameSavesListener;
    private SavesManager savesManager;

    public AddSaveMenu(SlotsManager slotsManager, @Nullable Menu menuBack, RenameSavesListener renameSavesListener, SavesManager savesManager, SlotType[] slots){
        super(slotsManager);
        this.renameSavesListener = renameSavesListener;
        this.menuBack = menuBack;
        this.savesManager = savesManager;
        save = new Save(Material.TNT, "New save", currSlots);
        currSlots = slots;
    }

    public AddSaveMenu(SlotsManager slotsManager, @Nullable Menu menuBack, RenameSavesListener renameSavesListener, SavesManager savesManager) {
        super(slotsManager);
        this.renameSavesListener = renameSavesListener;
        this.menuBack = menuBack;
        this.savesManager = savesManager;
        save = new Save(Material.TNT, "New save", currSlots);
    }

    @Override
    public String getTitle() {
        return "&2&lAdding save: &a&l"+save.name;
    }

    @Override
    public void setMenuItems(Player player) {
        super.setMenuItems(player);
        ItemStack editIcon = Utils.createItem(save.icon, "&6&lCurrent icon", "edit-icon", "&7Click to edit");
        ItemStack editTitle = Utils.createItem(Material.NAME_TAG, "&e&lTitle: "+ save.name, "edit-title", "&7Click to edit");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lBack", "back");
        ItemStack addButton = Utils.createItem(Material.GREEN_DYE, "&a&lAdd save", "add-save");

        inventory.setItem(0, back);
        inventory.setItem(2, editIcon);
        inventory.setItem(3, editTitle);
        inventory.setItem(8, addButton);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        super.handleMenu(e);
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        if(Utils.isLocalizedEqual(item.getItemMeta(), "edit-icon")){
            renameSavesListener.addChangeIconPlayer(player, save, this);
            player.sendMessage(Utils.chat("&6Type the name of the item that you want to change the icon of this save to "));
            player.sendMessage(Utils.chat("&6You can also right click the item you want to have as an item"));
            player.sendMessage(Utils.chat("&6Type &e\"cancel\" &6 to cancel"));
            player.closeInventory();
        }

        switch (item.getType()){

            case NAME_TAG -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "edit-title")) return;
                renameSavesListener.addRenameSavePlayer(player, save, this);
                player.sendMessage(Utils.chat("&6Type the new title that you want to rename this save to"));
                player.sendMessage(Utils.chat("&6Type &e\"cancel\" &6 to cancel"));
                player.closeInventory();
            }

            case RED_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;
                if(menuBack == null)
                    player.closeInventory();
                else
                    menuBack.open(player);
            }

            //TODO add a confirm menu
            case GREEN_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "add-save")) return;
                save.setSaveSlots(currSlots);

                try {
                    savesManager.addPlayerSave(player, save);
                } catch (SQLException | IOException ex) {
                    Bukkit.getLogger().severe("Error while adding save!");
                    ex.printStackTrace(System.out);
                    return;
                }

                player.sendMessage(ChatColor.GREEN + "Successfully added this save!");
                if(menuBack == null) player.closeInventory();
                else menuBack.open(player);

            }

        }

    }


}
