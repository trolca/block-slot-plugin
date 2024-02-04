package me.trololo11.blockslotplugin;

import me.trololo11.blockslotplugin.commands.TestCommand;
import me.trololo11.blockslotplugin.listeners.SlotsEditBlock;
import me.trololo11.blockslotplugin.managers.SlotsManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class BlockSlotPlugin extends JavaPlugin {

    private SlotsManager slotsManager;

    @Override
    public void onEnable() {
        slotsManager = new SlotsManager();

        getCommand("testcommand").setExecutor(new TestCommand(slotsManager));

        getServer().getPluginManager().registerEvents(new SlotsEditBlock(), this);

    }

    public static BlockSlotPlugin getPlugin(){
        return getPlugin(BlockSlotPlugin.class);
    }
}
