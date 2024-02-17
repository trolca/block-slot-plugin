package me.trololo11.blockslotplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Creates a new {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param name The display name of this item
     * @param localizedName The localized name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createItem(Material material, String name, String localizedName, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, loreArray));

        return item;
    }

    /**
     * Creates a new {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param name The display name of this item
     * @param localizedName The localized name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createItem(Material material, String name, String localizedName, List<String> lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, loreArray));

        return item;
    }

    /**
     * Creates a new {@link ItemStack} with the specified enchantment. <br>
     * This function should only be used to create items with the enchantment glint and
     * not for real items to enchant because <b> the enchant will not show on the item</b>
     * @param material The material of this item
     * @param name The name of this item (You can use alternate color codes with the '&' char)
     * @param localizedName The localized name of this item
     * @param enchantment The enchantment to set
     * @param lore The lore (description) of this item
     * @return A new {@link ItemStack} with the specified parameters
     */
    public static ItemStack createEnchantedItem(Material material, String name, String localizedName, Enchantment enchantment, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, loreArray));

        item.addUnsafeEnchantment(enchantment, 1);

        return item;
    }

    private static ItemMeta getItemMeta(ItemMeta itemMeta, String displayName, String localizedName, List<String> lore){

        itemMeta.setDisplayName(Utils.chat(displayName));
        itemMeta.setLocalizedName(localizedName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        return itemMeta;
    }

    public static boolean isLocalizedEqual(ItemMeta itemMeta, String equalWhat){
        return itemMeta.getLocalizedName().equalsIgnoreCase(equalWhat);
    }

}
