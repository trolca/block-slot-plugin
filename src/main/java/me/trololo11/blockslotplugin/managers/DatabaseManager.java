package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.utils.Save;
import me.trololo11.blockslotplugin.utils.SlotType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This interface defines which function every class whose role is
 * saving data to database should have.
 */
public interface DatabaseManager {

    /**
     * Initializes the database. Should be called to start the database.
     */
    void initialize() throws SQLException, IOException;

    void close();

    /**
     * Saves the player's current slots to the database. <br>
     * If the uuid doesn't exist in the database it's going to add a column with the slots and
     * if it exists it's going to update the existing record. <br>
     * This function should encode the list to Base64 using @link DatabaseManager#encodePlayerSlots(List)} and
     * saving that to the database.
     * @param playerUuid The uuid of the player we want to save the slots of
     * @param slots The list of slots of the player.
     */
    void savePlayerSlots(UUID playerUuid, SlotType[] slots) throws SQLException, IOException;

    /**
     * Gets the player slots that player had while last leaving the server.
     * @param playerUuid The player's uuid that we want to get the slots of
     * @return A list defined in the {@link SlotsManager} which contains the information of
     * every slots type that the player had.
     */
    SlotType[] getPlayerSlots(UUID playerUuid) throws SQLException, IOException;

    /**
     * Removes the specified player from the database. Should be used
     * when player has no modified slots and storing their data is useless.
     * @param playerUuid The uuid of the player to delete
     */
    void removePlayerSlots(UUID playerUuid) throws SQLException, IOException;

    /**
     * Checks if player has a record in the database.
     * @param playersUuid The uuid to check.
     * @return Whether the player has a record.
     */
    boolean playerSlotsModified(UUID playersUuid);

    /**
     * Adds a new save of slots to the database.
     * @param playersUuid The player's uuid which save it is.
     * @param saveIndex The index of the save.
     * @param save An array which length is 37. (Follow {@link SlotsManager})
     */
    void addPlayerSave(UUID playersUuid, int saveIndex, Save save) throws SQLException, IOException;

    /**
     * Updates an already existing save in the database.
     * @param playersUuid The player's uuid which save it is.
     * @param saveIndex The index of the save we want to modify.
     * @param save The array of the new save (Follow {@link SlotsManager})
     */
    void updatePlayerSave(UUID playersUuid, int saveIndex, Save save) throws SQLException, IOException;

    /**
     * Changes the save index and makes the save indexes after it
     * +1.
     * @param playerUuid The uuid of the player.
     * @param originalIndex The original index of the save.
     * @param changeToIndex The index you want to swap to.
     */
    void changePlayerSaveIndex(UUID playerUuid, int originalIndex, int changeToIndex);

    /**
     * Removes a specified players save. <br>
     * This function also remaps every save's index which is after.
     * @param playersUuid The player's uuid which save it is.
     * @param saveIndex The index of the save we want to delete.
     */
    void removePlayerSave(UUID playersUuid, int saveIndex) throws SQLException;

    /**
     * Gets all the saves of the specified player.
     * @param playersUuid The uuid of the player you want to get the saves from.
     * @return An array list of the players saves.
     */
    ArrayList<Save> getPlayerSaves(UUID playersUuid) throws SQLException, IOException;

}
