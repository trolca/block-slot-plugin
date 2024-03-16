package me.trololo11.blockslotplugin.listeners;

import me.trololo11.blockslotplugin.BlockSlotPlugin;
import me.trololo11.blockslotplugin.utils.Menu;
import me.trololo11.blockslotplugin.utils.Save;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class RenameSavesListener implements Listener {

    private HashMap<Player, Save> changeIconPlayers = new HashMap<>();
    private HashMap<Player, Save> renameSavePlayers = new HashMap<>();
    private HashMap<Player, Menu> backMenus = new HashMap<>();

    private BlockSlotPlugin plugin = BlockSlotPlugin.getPlugin();

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        boolean changeIconPlayer = changeIconPlayers.containsKey(player);
        boolean renameSavePlayer = renameSavePlayers.containsKey(player);

        if(changeIconPlayer || renameSavePlayer)
            e.setCancelled(true);

        if((changeIconPlayer || renameSavePlayer) && e.getMessage().equals("cancel")){
            resetPlayer(player);
            return;
        }

        if(changeIconPlayer){
            Material newIcon = Material.getMaterial(e.getMessage().toUpperCase());
            Save save = changeIconPlayers.get(player);
            if(newIcon == null){
                player.sendMessage(ChatColor.RED + "That item doesn't exist!");
                return;
            }
            save.icon = newIcon;
            player.sendMessage(ChatColor.GREEN + "Successfully set the icon!");
        }
        if(renameSavePlayer){
            Save save = renameSavePlayers.get(player);
            if(e.getMessage().isBlank()){
                player.sendMessage(ChatColor.RED + "Title cannot be empty!");
                return;
            }
            save.name = e.getMessage();
            player.sendMessage(ChatColor.GREEN + "Successfully set the title!");
        }

        //idc
        if(changeIconPlayer || renameSavePlayer){
            resetPlayer(player);
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(!changeIconPlayers.containsKey(player)) return;
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        Save save = changeIconPlayers.get(player);
        save.icon = player.getInventory().getItemInMainHand().getType();

        backMenus.get(player).open(player);
        changeIconPlayers.remove(player);
        backMenus.remove(player);
        player.sendMessage(ChatColor.GREEN + "Successfully set the icon!");
        e.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        renameSavePlayers.remove(player);
        changeIconPlayers.remove(player);
        backMenus.remove(player);
    }

    private void resetPlayer(Player player){
        changeIconPlayers.remove(player);
        renameSavePlayers.remove(player);
        Menu menu = backMenus.get(player);
        Bukkit.getScheduler().callSyncMethod(plugin, () ->  {
            menu.open(player);
            return menu;
        } );

        backMenus.remove(player);
    }

    public void addChangeIconPlayer(Player player,Save save, Menu backMenu){
        changeIconPlayers.put(player, save);
        backMenus.put(player, backMenu);
    }

    public void addRenameSavePlayer(Player player,Save save, Menu backMenu){
        renameSavePlayers.put(player, save);
        backMenus.put(player, backMenu);
    }


}
