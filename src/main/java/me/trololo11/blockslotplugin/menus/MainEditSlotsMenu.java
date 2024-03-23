package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.listeners.RenameSavesListener;
import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainEditSlotsMenu extends SlotsEditingMenu {

    private Player editPlayer;
    private SlotsManager slotsManager;
    private SavesManager savesManager;
    private RenameSavesListener renameSavesListener;

    public MainEditSlotsMenu(Player editPlayer, SlotsManager slotsManager, SavesManager savesManager, RenameSavesListener renameSavesListener){
        super(slotsManager);
        this.editPlayer = editPlayer;
        this.slotsManager = slotsManager;
        this.savesManager = savesManager;
        this.renameSavesListener = renameSavesListener;

        currSlots = slotsManager.getPlayerInvSlots(editPlayer);
    }

    @Override
    public String getTitle() {
        return "&6&lEditing slots of " + editPlayer.getName();
    }

    @Override
    public void setMenuItems(Player player) {
        super.setMenuItems(player);
        ItemStack loadSave = Utils.createItem(Material.BLUE_DYE, "&9&lLoad save", "load-save", "&7Choose a save that you want", "&7to load for this player");
        ItemStack addSave = Utils.createItem(Material.LIGHT_BLUE_DYE, "&3&lAdd save", "add-save", "&7Add this configuration", "&7as a new save.");
        ItemStack apply = Utils.createItem(Material.GREEN_DYE, "&a&lApply slots", "apply");
        ItemStack exit = Utils.createItem(Material.RED_DYE, "&c&lExit", "exit");


        inventory.setItem(2, loadSave);
        inventory.setItem(3, addSave);
        inventory.setItem(0, exit);
        inventory.setItem(8, apply);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        super.handleMenu(e);
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();


        switch (item.getType()){


            //add save
            case LIGHT_BLUE_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "add-save")) return;

                new AddSaveMenu(slotsManager, this, renameSavesListener, savesManager, Arrays.copyOf(currSlots, currSlots.length)).open(player);
            }

            //load save
            case BLUE_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "load-save")) return;

                new SelectSaveMenu(this, slotsManager, savesManager, player).open(player);
            }

            //back
            case RED_DYE -> {
                if (!Utils.isLocalizedEqual(item.getItemMeta(), "exit")) return;

                player.closeInventory();
            }

            //confirm
            case GREEN_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "apply")) return;

                slotsManager.setPlayerInvSlots(editPlayer, currSlots);
                player.sendMessage(ChatColor.GREEN + "Successfully set the custom slots for "+ editPlayer.getName());
                player.closeInventory();
            }
        }
    }

    public void setCurrSlots(SlotType[] slots){
        currSlots = slots;
    }


}
