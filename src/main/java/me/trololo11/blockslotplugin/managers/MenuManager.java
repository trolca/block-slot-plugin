package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.utils.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuManager implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){

        if(e.getInventory().getHolder() instanceof Menu menu) {
            ItemStack item = e.getCurrentItem();
            if (item == null) return;
            if (!item.hasItemMeta()) return;
            if (!item.getItemMeta().hasLocalizedName()) return;
            if (!(e.getWhoClicked() instanceof Player)) return;
            Player player = (Player) e.getWhoClicked();

            menu.open(player);
        }




    }
}
