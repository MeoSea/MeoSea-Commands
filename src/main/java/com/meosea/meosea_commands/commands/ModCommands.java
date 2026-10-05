package com.meosea.meosea_commands.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void registerAll() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Cwhitelist.register(dispatcher);
            // thêm lệnh mới thì thêm một dòng ở đây
        });
    }
}