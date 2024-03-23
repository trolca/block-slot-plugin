package me.trololo11.blockslotplugin.commands;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.menus.SeeSavesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeSavesCommand implements CommandExecutor {

    private SavesManager savesManager;
    private SlotsManager slotsManager;

    public SeeSavesCommand(SavesManager savesManager, SlotsManager slotsManager){
        this.savesManager = savesManager;
        this.slotsManager = slotsManager;
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        new SeeSavesMenu(savesManager, player, slotsManager).open(player);

        return true;
    }
}
