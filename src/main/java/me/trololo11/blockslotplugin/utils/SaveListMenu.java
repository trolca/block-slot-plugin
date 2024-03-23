package me.trololo11.blockslotplugin.utils;

import me.trololo11.blockslotplugin.managers.SavesManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Creates a menu with the list of a player's saves divided into pages. <br>
 * By itself it's going to create an inventory with 54 slots, a gray strip
 * on the top and the page navigation on the bottom. <br>
 * When using it is recommended that you override methods {@link Menu#handleMenu(InventoryClickEvent)} and
 * {@link Menu#setMenuItems(Player)} and add a call to the super method at the top of it to
 * customize the menu. <br> <br>
 * Every save item's localized name is "save-(save index)" so when handling the menu
 * to get the save that a player clicked you should check if the localized name
 * starts with start and the rest is the index of the save that the player clicked.
 */
public abstract class SaveListMenu extends Menu {

    protected Player savesPlayer;
    protected SavesManager savesManager;
    protected int page;

    /**
     * @param savesManager A saves manager instance
     * @param savesPlayer The player to get the saves from.
     */
    public SaveListMenu(SavesManager savesManager, Player savesPlayer){
        this.savesManager = savesManager;
        this.savesPlayer = savesPlayer;
        page = 0;
    }


    public void open(Player player, int page){
        super.open(player);
        this.page = page;
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

        for(int i=0; i < 9; i++){
            inventory.setItem(i, darkFiller);
        }

        for(int i=45; i < 54; i++){
            inventory.setItem(i, darkFiller);
        }
        List<Save> playerSaves = savesManager.getPlayerSaves(savesPlayer);

        for(int i=9; i < 45; i++){
            int currIndex = (i-9)+(36*page);

            if(currIndex >= playerSaves.size()) break;
            Save save = playerSaves.get(currIndex);

            inventory.setItem(i, save.createSaveItem(currIndex, getSavesDescription()));
        }

        int maxPages = playerSaves.size()/36;
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
            Save save = savesManager.getPlayerSaves(savesPlayer).get(index);

            onSaveClick(save,index, player);

        }else if (item.getType() == Material.ARROW) {
            if (Utils.isLocalizedEqual(item.getItemMeta(), "left"))
                page--;
            else if (Utils.isLocalizedEqual(item.getItemMeta(), "rig"))
                page++;
            else
                return;

            setMenuItems(player);
        }
    }

    /**
     * Gets the lore that is going to show on
     * every save item in the menu.
     * @return The lore that you want to display.
     */
    protected abstract List<String> getSavesDescription();

    /**
     * Called when a player clicks a save item in this menu.
     * @param save The class of the save that the player clicked.
     * @param index The index of the save.
     * @param player The player that clicked on the save.
     */
    protected abstract void onSaveClick(Save save,int index, Player player);
}
