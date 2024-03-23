package me.trololo11.blockslotplugin.menus;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SelectSaveMenu extends SaveListMenu {

    private MainEditSlotsMenu backMenu;
    private SlotsManager slotsManager;

    public SelectSaveMenu(MainEditSlotsMenu backMenu, SlotsManager slotsManager, SavesManager savesManager, Player savesPlayer) {
        super(savesManager, savesPlayer);
        this.backMenu = backMenu;
        this.slotsManager = slotsManager;
    }


    @Override
    public String getTitle() {
        return "&a&lSelect a save";
    }

    @Override
    public void setMenuItems(Player player) {
        super.setMenuItems(player);
        ItemStack backButton = Utils.createItem(Material.RED_DYE, "&c&lBack", "back");

        inventory.setItem(0, backButton);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void handleMenu(InventoryClickEvent e) {
        super.handleMenu(e);
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        if (item.getType() == Material.RED_DYE) {
            if (!Utils.isLocalizedEqual(item.getItemMeta(), "back")) return;

            backMenu.open(player);
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
        return List.of(Utils.chat("&7Click to select!"));
    }

    @Override
    protected void onSaveClick(Save save,int index, Player player) {
        ConfirmSaveFunction saveFunction = save1 -> {
            backMenu.setCurrSlots(save1.getSaveSlots());
            backMenu.open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
        };

        new SeeSaveMenu(this, slotsManager, save, index, false, saveFunction).open(player);
    }
}
