package me.trololo11.blockslotplugin.listeners;

import me.trololo11.blockslotplugin.managers.DatabaseManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import me.trololo11.blockslotplugin.utils.SlotType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.sql.SQLException;

public class PlayerSlotDataLoader implements Listener {

    private DatabaseManager databaseManager;
    private SlotsManager slotsManager;

    public PlayerSlotDataLoader(DatabaseManager databaseManager, SlotsManager slotsManager){
        this.databaseManager = databaseManager;
        this.slotsManager = slotsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException, IOException {
        Player player = e.getPlayer();

        SlotType[] slots = databaseManager.getPlayerSlots(player.getUniqueId());

        if(slots != null)
            slotsManager.addPlayerInvSlots(player, slots);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException, IOException {
        Player player = e.getPlayer();

        SlotType[] slots = slotsManager.getPlayerInvSlots(player);

        databaseManager.savePlayerSlots(player.getUniqueId(), slots);
        slotsManager.removePlayerInvSlot(player);

        if(arrayEntirelyNull(slots)){
            if(databaseManager.playerSlotsModified(player.getUniqueId()))
                databaseManager.removePlayerSlots(player.getUniqueId());
        }else{
            databaseManager.savePlayerSlots(player.getUniqueId(), slots);
            slotsManager.removePlayerInvSlot(player);
        }
    }

    private boolean arrayEntirelyNull(SlotType[] slotTypes){

        for(SlotType slotType : slotTypes)
            if(slotType != null) return false;

        return true;
    }

}
