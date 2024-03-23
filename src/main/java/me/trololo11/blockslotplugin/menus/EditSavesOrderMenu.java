package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.utils.Save;
import me.trololo11.blockslotplugin.utils.SaveListMenu;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditSavesOrderMenu extends SaveListMenu {

    private SeeSavesMenu backMenu;
    private int selectedIndex = -1;

    /**
     * @param savesManager A saves manager instance
     * @param savesPlayer  The player to get the saves from.
     */
    public EditSavesOrderMenu(SeeSavesMenu backMenu, SavesManager savesManager, Player savesPlayer) {
        super(savesManager, savesPlayer);
        this.backMenu = backMenu;
    }

    @Override
    public String getTitle() {
        return "&f&lEditing the order";
    }

    @Override
    public void setMenuItems(Player player) {
        super.setMenuItems(player);
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lExit", "back");

        inventory.setItem(0, back);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        super.handleMenu(e);
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item.getType() == Material.RED_DYE) {
            if (!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;

            backMenu.open(player, page);
        }
    }

    /**
     * Gets the lore that is going to show on
     * every save item in the menu.
     *
     * @return The lore that you want to display.
     */
    @Override
    protected List<String> getSavesDescription() {
        return List.of(Utils.chat("&7Click this save to move it."));
    }

    @Override //TODO finish this
    public void onSaveClick(Save save, int index, Player player) {
        if(index == selectedIndex){
            selectedIndex = -1;
        }else if(selectedIndex == -1){
            selectedIndex = index;
        }


    }
}
