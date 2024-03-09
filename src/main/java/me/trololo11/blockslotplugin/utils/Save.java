package me.trololo11.blockslotplugin.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.trololo11.blockslotplugin.managers.SavesManager;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents a players save
 * of a specific slot arrangement. <br>
 * Note that you should use the {@link SavesManager#modifyPlayerSave(Player, Save, int)}
 * function after you do <b>any</b> modification to this class.
 */
public class Save {

    public Material icon;
    public String name;
    private SlotType[] saveSlots;

    public Save(Material icon, String name, SlotType[] saveSlots){
        this.icon = icon;
        this.name = name;
        this.saveSlots = saveSlots;
    }

    /**
     * Sets a new arrangement of slots in this save.
     * @param saveSlots The new slots.
     */
    public void setSaveSlots(SlotType[] saveSlots){
        if(saveSlots.length != 37)
            throw new IllegalArgumentException("The save slots array should have the length of 37!");

        this.saveSlots = saveSlots;
    }

    /**
     * Gets a <b>copy</b> of the slots state in this save.
     * @return A <b>copy</b> of the array of the slots in this save.
     */
    public SlotType[] getSaveSlots(){
        return Arrays.copyOf(saveSlots, saveSlots.length);
    }

    /**
     * Creates a display item of this save that can be used in an inventory.
     * It's localized name is "save-(index)"
     * @param index The index of the save
     * @param lore The lore to put in this item.
     * @return A display item of this save class.
     */
    public ItemStack createSaveItem(int index, String... lore){
        ArrayList<String> loreArray = new ArrayList<>(lore.length);
        for(String s : lore) {
            loreArray.add(Utils.chat(s));
        }
        ItemStack saveItem = new ItemStack(this.icon);
        ItemMeta saveMeta = saveItem.getItemMeta();

        saveMeta.setDisplayName(Utils.chat("&2&lSave &a&l"+(index+1)+"&2&l: &a&l"+this.name));
        saveMeta.setLocalizedName("save-"+index);
        saveMeta.setLore(loreArray);
        saveMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        saveItem.setItemMeta(saveMeta);

        return saveItem;
    }


}
