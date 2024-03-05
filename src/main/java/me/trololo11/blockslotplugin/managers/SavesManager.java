package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.BlockSlotPlugin;
import me.trololo11.blockslotplugin.utils.Save;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * This class manages all the online player's saves.
 * The data is stored in a hash map with an array list of
 * every player's saves.
 */
public class SavesManager {
    private BlockSlotPlugin plugin = BlockSlotPlugin.getPlugin();
    private HashMap<Player, ArrayList<Save>> playerSaves = new HashMap<>();
    private DatabaseManager databaseManager;

    public SavesManager(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    /**
     * Adds new array list of saves record to the hash map. <br>
     * If the player is already contained within the hash map they won't
     * be added. When added every save in the array list is checked
     * for the correct length. If the save's length is incorrect then
     * it is deleted from the player's saves.
     * @param player The player to add the saves for
     * @param saves An array list of saves of the player.
     *              The maximum number of saves can be set in the config.
     *              <b>If the amount of saves is over the number set in the config the rest of saves will be ignored</b>
     */
    public void addPlayerSaves(Player player, ArrayList<Save> saves){
        if(playerSaves.containsKey(player)) return;
        if(plugin.getMaxSaves() > 0 && saves.size() > plugin.getMaxSaves())
            saves = (ArrayList<Save>) saves.subList(0, plugin.getMaxSaves());

        playerSaves.put(player, saves);
    }

    /**
     * Modifies the specified save in the database and in the hash map.
     * @param player The player to modify save for
     * @param save The new save
     * @param index The index of the save in the arrayList
     * @throws SQLException On database connection error
     * @throws IOException On encrypting error
     */
    public void modifyPlayerSave(Player player, Save save, int index) throws SQLException, IOException {
        ArrayList<Save> saves = playerSaves.get(player);

        if(saves.size() < index){
            throw new IndexOutOfBoundsException("The index of that save doesn't exist!");
        }
        playerSaves.get(player).set(index, save);
        databaseManager.updatePlayerSave(player.getUniqueId(), index, save);
    }

    /**
     * Adds a new save for the specified player. This function is going to append that save to the
     * end of the list. It doesn't check if the max save's limit has been overpassed
     * @param player The player to add the save for
     * @param save The save to add.
     * @throws SQLException On database connection error
     * @throws IOException On encrypting error
     */
    public void addPlayerSave(Player player, Save save) throws SQLException, IOException {
        ArrayList<Save> saves = playerSaves.get(player);
        saves.add(save);
        databaseManager.addPlayerSave(player.getUniqueId(), saves.size()-1, save);
    }

    /**
     * Removes a players save from the list.
     * @param player The player to remove the save from.
     * @param index The index of the save.
     * @throws SQLException On database connection error
     */
    public void removePlayerSave(Player player, int index) throws SQLException {
        playerSaves.get(player).remove(index);
        databaseManager.removePlayerSave(player.getUniqueId(), index);
    }

    /**
     * Returns an <b>unmodifiable</b> list with the specified player's saves.
     * @param player The player you want to get the saves for
     * @return An <b>copy of the</b> list of player's saves.
     */
    public List<Save> getPlayerSaves(Player player){
        return List.copyOf(playerSaves.get(player));
    }

    /**
     * Removes a specified players saves from the hash map. <br>
     * Should be used when player leaves.
     * @param player The player you want to remove saves.
     */
    public void removePlayerSaves(Player player){
        playerSaves.remove(player);
    }


}
