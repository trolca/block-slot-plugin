package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.utils.Menu;
import me.trololo11.blockslotplugin.utils.Save;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SelectSaveMenu extends Menu {

    private MainEditSlotsMenu backMenu;
    private SavesManager savesManager;
    private int page;

    public SelectSaveMenu(MainEditSlotsMenu backMenu,SavesManager savesManager){
        this.backMenu = backMenu;
        this.savesManager = savesManager;
        page = 0;
    }

    public SelectSaveMenu(MainEditSlotsMenu backMenu,  SavesManager savesManager, int page){
        this.backMenu = backMenu;
        this.savesManager = savesManager;
        this.page = page;
    }

    @Override
    public String getTitle() {
        return Utils.chat("&a&lSelect a save");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack leftArrow = Utils.createItem(Material.ARROW, "&6Previous page", "left");
        ItemStack rightArrow = Utils.createItem(Material.ARROW, "&6Next page", "right");
        ItemStack pageLabel = Utils.createItem(Material.STONE_BUTTON, "&6Page &e"+(page+1), "page-label");
        ItemStack backButton = Utils.createItem(Material.RED_DYE, "&c&lBack", "back");

        for(int i=0; i < 9; i++){
            inventory.setItem(i, darkFiller);
        }

        for(int i=45; i < 54; i++){
            inventory.setItem(i, darkFiller);
        }
        List<Save> playerSaves = savesManager.getPlayerSaves(player);

        for(int i=9; i < 45; i++){
            int currIndex = (i-9)+(36*page);

            if(currIndex >= playerSaves.size()) break;
            Save save = playerSaves.get(currIndex);

            inventory.setItem(i, save.createSaveItem(currIndex, "&7Click to edit!"));
        }

        int maxPages = playerSaves.size()/36;
        inventory.setItem(0, backButton);
        if(page != 0) inventory.setItem(45, leftArrow);
        if(page != maxPages) inventory.setItem(53, rightArrow);
        inventory.setItem(49, pageLabel);


    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case ARROW -> {

                if(Utils.isLocalizedEqual(item.getItemMeta(), "left"))
                    page--;
                else if(Utils.isLocalizedEqual(item.getItemMeta(), "rig"))
                    page++;
                else
                    return;

                setMenuItems(player);
            }

            case RED_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;

                backMenu.open(player);
            }

        }

    }
}
