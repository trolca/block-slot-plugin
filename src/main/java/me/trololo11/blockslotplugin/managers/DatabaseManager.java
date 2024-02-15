package me.trololo11.blockslotplugin.managers;

import me.trololo11.blockslotplugin.BlockSlotPlugin;
import me.trololo11.blockslotplugin.utils.SlotType;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
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

}
