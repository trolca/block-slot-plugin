package me.trololo11.blockslotplugin;

import me.trololo11.blockslotplugin.commands.EditSlotsCommand;
import me.trololo11.blockslotplugin.commands.TestCommand;
import me.trololo11.blockslotplugin.listeners.MenuManager;
import me.trololo11.blockslotplugin.listeners.SlotsEditBlock;
import me.trololo11.blockslotplugin.listeners.PlayerSlotDataLoader;
import me.trololo11.blockslotplugin.managers.DatabaseManager;
import me.trololo11.blockslotplugin.managers.MySqlDatabase;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public final class BlockSlotPlugin extends JavaPlugin {

    private SlotsManager slotsManager;
    private DatabaseManager databaseManager;

    public final Properties dbProperties;

    public BlockSlotPlugin(){
        dbProperties = new Properties();
        dbProperties.setProperty("minimumIdle", "1");
        dbProperties.setProperty("maximumPoolSize", "4");
        dbProperties.setProperty("initializationFailTimeout", "20000");
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        databaseManager = new MySqlDatabase();
        try {
            databaseManager.initialize();
        } catch (SQLException | IOException e) {
            getLogger().severe("Error while connecting to the database!");
            getLogger().severe("Make sure you entered the correct information in the config.yml file.");
            e.printStackTrace(System.out);
            return;
        }
        slotsManager = new SlotsManager();

        try {
            loadData();
        } catch (SQLException | IOException e) {
            getLogger().severe("Error while loading data for online players!");
            throw new RuntimeException(e);
        }

        getCommand("testcommand").setExecutor(new TestCommand(slotsManager));
        getCommand("editslots").setExecutor(new EditSlotsCommand(slotsManager));

        getServer().getPluginManager().registerEvents(new MenuManager(), this);
        getServer().getPluginManager().registerEvents(new PlayerSlotDataLoader(databaseManager, slotsManager), this);
        getServer().getPluginManager().registerEvents(new SlotsEditBlock(), this);

    }

    @Override
    public void onDisable(){
        try {
            saveData();
        } catch (SQLException | IOException e) {
            getLogger().severe("Error while saving online's players data!");
            throw new RuntimeException(e);
        }
        databaseManager.close();
    }

    private void loadData() throws SQLException, IOException {
        for(Player player : Bukkit.getOnlinePlayers())
            slotsManager.addPlayerInvSlots(player, databaseManager.getPlayerSlots(player.getUniqueId()));
    }

    private void saveData() throws SQLException, IOException {
        for(Player player : Bukkit.getOnlinePlayers())
            databaseManager.savePlayerSlots(player.getUniqueId(), slotsManager.getPlayerInvSlots(player));
    }

    public static BlockSlotPlugin getPlugin(){
        return getPlugin(BlockSlotPlugin.class);
    }
}