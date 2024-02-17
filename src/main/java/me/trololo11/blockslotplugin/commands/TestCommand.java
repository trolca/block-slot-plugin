package me.trololo11.blockslotplugin.commands;

import com.google.common.collect.ImmutableMap;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class TestCommand implements CommandExecutor {

    private SlotsManager slotsManager;

    public TestCommand(SlotsManager slotsManager){
        this.slotsManager = slotsManager;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        ItemStack sus = new ItemStack(Material.DIAMOND);
        ItemMeta susMeta = sus.getItemMeta();
        susMeta.setDisplayName("Sussy epic impostor");
        sus.setItemMeta(susMeta);

        ImmutableMap<String, ItemStack> sussy = ImmutableMap.of("epic", sus);
        player.getInventory().addItem(sussy.get("epic"));

        susMeta.setDisplayName("fsdhbasfdhkb");
        sussy.get("epic").setItemMeta(susMeta);

        player.getInventory().addItem(sussy.get("epic"));



        return true;
    }
}
