package me.trololo11.blockslotplugin.utils;


/**
 * This lambda function is used when you click the confirm button in the {@link me.trololo11.blockslotplugin.menus.SeeSaveMenu}
 */
@FunctionalInterface
public interface ConfirmSaveFunction {
    void run(Save save);
}
