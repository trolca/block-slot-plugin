package me.trololo11.blockslotplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class SlotsEditBlock implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        e.setCancelled(checkIfCancel(e.getItemInHand()));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        e.setCancelled(checkIfCancel(e.getCurrentItem()));
    }

    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent e){
        e.setCancelled( checkIfCancel(e.getMainHandItem()) || checkIfCancel(e.getOffHandItem()) );
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(checkIfCancel(e.getItemDrop().getItemStack()));
    }

    private boolean checkIfCancel(ItemStack item){

        if(item == null) return false;
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().hasLocalizedName()) return false;

        return item.getItemMeta().getLocalizedName().startsWith("customslot");

    }

}
