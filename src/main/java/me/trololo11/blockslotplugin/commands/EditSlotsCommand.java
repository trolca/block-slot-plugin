package me.trololo11.blockslotplugin.commands;

import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.menus.MainEditSlotsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditSlotsCommand implements CommandExecutor {

    private SlotsManager slotsManager;
    private SavesManager savesManager;

    public EditSlotsCommand(SlotsManager slotsManager, SavesManager savesManager){
        this.slotsManager = slotsManager;
        this.savesManager = savesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length == 0){
            player.sendMessage(ChatColor.RED + "Type the name of the player you want to modify!");
            return true;
        }

        Player editPlayer = Bukkit.getPlayer(args[0]);
        if(editPlayer == null){
            player.sendMessage(ChatColor.RED + "This player isn't online!");
            return true;
        }

        new MainEditSlotsMenu(editPlayer,slotsManager,savesManager).open(player);

        return true;
    }
}
