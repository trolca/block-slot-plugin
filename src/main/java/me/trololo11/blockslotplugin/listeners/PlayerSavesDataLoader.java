package me.trololo11.blockslotplugin.listeners;

import me.trololo11.blockslotplugin.managers.DatabaseManager;
import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.utils.Save;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerSavesDataLoader implements Listener {

    private SavesManager savesManager;
    private DatabaseManager databaseManager;

    public PlayerSavesDataLoader(SavesManager savesManager, DatabaseManager databaseManager){
        this.savesManager = savesManager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException, IOException {
        Player player = e.getPlayer();
        ArrayList<Save> playerSaves = databaseManager.getPlayerSaves(player.getUniqueId());

        savesManager.addPlayerSaves(player, playerSaves);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        savesManager.removePlayerSaves(player);
    }


}
