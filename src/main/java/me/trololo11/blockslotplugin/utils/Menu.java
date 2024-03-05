package me.trololo11.blockslotplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder {
    protected Inventory inventory;

    public abstract String getTitle();

    public abstract int getSlots();

    public abstract void setMenuItems(Player player);

    public abstract void handleMenu(InventoryClickEvent e);

    public void open(Player player){
        inventory = Bukkit.createInventory(this, getSlots(), Utils.chat(getTitle()));
        this.setMenuItems(player);

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory(){
        return inventory;
    }

}
