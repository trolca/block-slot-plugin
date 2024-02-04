package me.trololo11.blockslotplugin.commands;

import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.SlotType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TestCommand implements CommandExecutor {

    private SlotsManager slotsManager;

    public TestCommand(SlotsManager slotsManager){
        this.slotsManager = slotsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        ArrayList<SlotType> inv = new ArrayList<>(37);

        for(int i=0; i < 37; i++)
            inv.add(SlotType.BLOCKED);

        slotsManager.setPlayerInvSlots(player, inv);

        return true;
    }
}
