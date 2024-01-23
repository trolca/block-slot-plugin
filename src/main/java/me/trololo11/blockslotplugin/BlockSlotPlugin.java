package me.trololo11.blockslotplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public final class BlockSlotPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitStream = null;
        try {
            bukkitStream = new BukkitObjectOutputStream(outputStream);

            bukkitStream.writeObject(null);

            bukkitStream.close();

            System.out.println(Base64.getEncoder().encodeToString(outputStream.toByteArray()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {

    }
}
