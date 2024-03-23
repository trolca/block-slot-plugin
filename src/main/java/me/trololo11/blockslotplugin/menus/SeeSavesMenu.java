package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.Save;
import me.trololo11.blockslotplugin.utils.SaveListMenu;
import me.trololo11.blockslotplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SeeSavesMenu extends SaveListMenu {

    private SlotsManager slotsManager;

    public SeeSavesMenu(SavesManager savesManager, Player savesPlayer, SlotsManager slotsManager) {
        super(savesManager, savesPlayer);
        this.slotsManager = slotsManager;
    }


    @Override
    public String getTitle() {
        return "&e&lYour saves";
    }


    @Override
    public void setMenuItems(Player player) {
        super.setMenuItems(player);
        ItemStack editOrderButton = Utils.createItem(Material.FEATHER, "&f&lEdit order", "edit-order", "&7Click here to edit the order", "&7of your saves.");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lExit", "back");

        inventory.setItem(0, back);
        inventory.setItem(4, editOrderButton);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        super.handleMenu(e);
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        switch (item.getType()){

            case FEATHER -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "edit-order")) return;

                new EditSavesOrderMenu(this, savesManager, player).open(player, page);
            }

            case RED_DYE -> {
                if(!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;

                player.closeInventory();
            }

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
        return List.of(Utils.chat("&7Click to see"));
    }

    @Override
    protected void onSaveClick(Save save, int index, Player player) {
        new SeeSaveMenu(this, slotsManager, save, index, true, null).open(player);
    }
}
