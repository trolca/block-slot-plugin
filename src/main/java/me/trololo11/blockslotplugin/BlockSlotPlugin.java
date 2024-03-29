package me.trololo11.blockslotplugin;

import me.trololo11.blockslotplugin.commands.EditSlotsCommand;
import me.trololo11.blockslotplugin.commands.SeeSavesCommand;
import me.trololo11.blockslotplugin.commands.TestCommand;
import me.trololo11.blockslotplugin.listeners.*;
import me.trololo11.blockslotplugin.managers.DatabaseManager;
import me.trololo11.blockslotplugin.managers.MySqlDatabase;
import me.trololo11.blockslotplugin.managers.SavesManager;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public final class BlockSlotPlugin extends JavaPlugin {

    private SlotsManager slotsManager;
    private SavesManager savesManager;
    private DatabaseManager databaseManager;

    private int maxSaves;

    public final Properties dbProperties;

    public BlockSlotPlugin(){
        dbProperties = new Properties();
        dbProperties.setProperty("minimumIdle", "1");
        dbProperties.setProperty("maximumPoolSize", "4");
        dbProperties.setProperty("initializationFailTimeout", "20000");
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        maxSaves = getConfig().getInt("max-saves");

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
        savesManager = new SavesManager(databaseManager);
        RenameSavesListener renameSavesListener = new RenameSavesListener();

        try {
            loadData();
        } catch (SQLException | IOException e) {
            getLogger().severe("Error while loading data for online players!");
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new MenuManager(), this);
        getServer().getPluginManager().registerEvents(new PlayerSlotDataLoader(databaseManager, slotsManager), this);
        getServer().getPluginManager().registerEvents(new PlayerSavesDataLoader(savesManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new SlotsEditBlock(), this);
        getServer().getPluginManager().registerEvents(renameSavesListener, this);

        getCommand("testcommand").setExecutor(new TestCommand(slotsManager));
        getCommand("editslots").setExecutor(new EditSlotsCommand(slotsManager, savesManager, renameSavesListener));
        getCommand("seesaves").setExecutor(new SeeSavesCommand(savesManager, slotsManager));
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
        for(Player player : Bukkit.getOnlinePlayers()) {
            slotsManager.addPlayerInvSlots(player, databaseManager.getPlayerSlots(player.getUniqueId()));
            savesManager.addPlayerSaves(player, databaseManager.getPlayerSaves(player.getUniqueId()));
        }
    }

    private void saveData() throws SQLException, IOException {
        for(Player player : Bukkit.getOnlinePlayers())
            databaseManager.savePlayerSlots(player.getUniqueId(), slotsManager.getPlayerInvSlots(player));
    }

    public int getMaxSaves() {
        return maxSaves;
    }

    public static BlockSlotPlugin getPlugin(){
        return getPlugin(BlockSlotPlugin.class);
    }
}