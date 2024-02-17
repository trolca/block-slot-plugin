package me.trololo11.blockslotplugin.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.blockslotplugin.BlockSlotPlugin;
import me.trololo11.blockslotplugin.utils.SlotType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class MySqlDatabase implements DatabaseManager{

    private HikariDataSource ds = null;
    private BlockSlotPlugin plugin = BlockSlotPlugin.getPlugin();
    private ArrayList<UUID> existingUuids = new ArrayList<>(); //Stores all the uuids that exist in the players_slots table

    private Connection getConnection() throws SQLException {
        if(ds != null) return ds.getConnection();
        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("database-name");

        if(databaseName == null || databaseName.isEmpty()){
            databaseName = "block_slot_database";
        }

        Connection checkConnection = DriverManager.getConnection(url, user, password);

        Statement statement = checkConnection.createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS "+ databaseName);
        statement.close();
        checkConnection.close();

        HikariConfig config = new HikariConfig();
        config.setPassword(password);
        config.setUsername(user);
        config.setJdbcUrl(url + "/" + databaseName);
        config.setDataSourceProperties(plugin.dbProperties);

        ds = new HikariDataSource(config);

        return ds.getConnection();
    }

    public void initialize() throws SQLException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS players_slots(uuid char(36) primary key not null, slots_info text)");

        ResultSet results = statement.executeQuery("SELECT uuid FROM players_slots");

        while(results.next())
            existingUuids.add(UUID.fromString(results.getString("uuid")));

        results.close();
        statement.close();
        connection.close();

    }

    @Override
    public void close() {
        ds.close();
    }


    @Override
    public void savePlayerSlots(UUID playerUuid, SlotType[] slots) throws SQLException, IOException {
        if(slots == null)
            return;
        Connection connection = getConnection();

        String encodedSlots = encodePlayerSlots(slots);
        PreparedStatement statement;

        if(existingUuids.contains(playerUuid)){
            statement = connection.prepareStatement("UPDATE players_slots SET slots_info = ? WHERE uuid = ?");

            statement.setString(1, encodedSlots);
            statement.setString(2, playerUuid.toString());
        }else{
            statement = connection.prepareStatement("INSERT INTO players_slots VALUES (?, ?)");

            statement.setString(1, playerUuid.toString());
            statement.setString(2, encodedSlots);
            existingUuids.add(playerUuid);
        }

        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    @Override
    public SlotType[] getPlayerSlots(UUID playerUuid) throws SQLException, IOException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT slots_info FROM players_slots WHERE uuid = ?");

        statement.setString(1, playerUuid.toString());

        ResultSet results = statement.executeQuery();

        if(!results.next()){
            results.close();
            statement.close();
            connection.close();
            return null;
        }
        SlotType[] slots = decodePlayerSlots(results.getString("slots_info"));



        results.close();
        statement.close();
        connection.close();

        return slots;
    }

    @Override
    public void removePlayerSlots(UUID playerUuid) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("DELETE FROM players_slots WHERE uuid = ?");

        statement.setString(1, playerUuid.toString());

        statement.executeUpdate();
        existingUuids.remove(playerUuid);

        statement.close();
        connection.close();
    }

    @Override
    public boolean playerSlotsModified(UUID playersUuid) {
        return existingUuids.contains(playersUuid);
    }


    private String encodePlayerSlots(SlotType[] slots) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(out);

        for(SlotType slotType : slots){
            bukkitOut.writeObject(slotType);
        }

        bukkitOut.close();

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    private SlotType[] decodePlayerSlots(String encodedString) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(encodedString));
        BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(in);

        SlotType[] slots = new SlotType[37];

        for(int i=0; i < 37; i++){

            try {
                slots[i] = (SlotType) bukkitIn.readObject();
            } catch (ClassNotFoundException e) {
                BlockSlotPlugin.getPlugin().getLogger().severe("[BlockSlotPlugin] Error while decoding slots!");
                e.printStackTrace(System.out);
                return slots;
            }
        }

        bukkitIn.close();
        in.close();

        return slots;


    }
}
