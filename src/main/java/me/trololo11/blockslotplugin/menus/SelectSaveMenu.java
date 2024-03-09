package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.ConfirmSaveFunction;
import me.trololo11.blockslotplugin.utils.Menu;
import me.trololo11.blockslotplugin.utils.Save;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SelectSaveMenu extends Menu {

    private MainEditSlotsMenu backMenu;
    private SavesManager savesManager;
    private SlotsManager slotsManager;
    private int page;

    public SelectSaveMenu(MainEditSlotsMenu backMenu,SavesManager savesManager, SlotsManager  slotsManager){
        this.backMenu = backMenu;
        this.savesManager = savesManager;
        this.slotsManager = slotsManager;
        page = 0;
    }

    @Override
    public String getTitle() {
        return "&a&lSelect a save";
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

            inventory.setItem(i, save.createSaveItem(currIndex, "&7Click to see!"));
        }

        int maxPages = playerSaves.size()/36;
        inventory.setItem(0, backButton);
        if(page != 0) inventory.setItem(45, leftArrow);
        if(page != maxPages) inventory.setItem(53, rightArrow);
        inventory.setItem(49, pageLabel);


    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        if(item.getItemMeta().getLocalizedName().startsWith("save")){
            String localizedName = item.getItemMeta().getLocalizedName();
            int index = Integer.parseInt(localizedName.split("-")[1]);
            Save save = savesManager.getPlayerSaves(player).get(index);

            ConfirmSaveFunction saveFunction = save1 -> {
                backMenu.setCurrSlots(save1.getSaveSlots());
                backMenu.open(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
            };

            new SeeSaveMenu(this, slotsManager, save, index, false, saveFunction).open(player);
            return;
        }
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
