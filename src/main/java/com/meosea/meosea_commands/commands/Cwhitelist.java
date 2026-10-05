package com.meosea.meosea_commands.commands;

import com.meosea.meosea_commands.Storages.WhitelistStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

@SuppressWarnings("null")
public class Cwhitelist {

    // Gợi ý tên người chơi đang online (dùng cho nhánh add)
    private static final SuggestionProvider<CommandSourceStack> ONLINE_NAMES =
        (context, builder) -> SharedSuggestionProvider.suggest(
            context.getSource().getServer().getPlayerList().getPlayerNamesArray(),
            builder);

    // Gợi ý tên đang có trong whitelist (dùng cho nhánh remove)
    private static final SuggestionProvider<CommandSourceStack> WHITELISTED_NAMES =
        (context, builder) -> SharedSuggestionProvider.suggest(WhitelistStorage.list(), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("cwhitelist")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))

                .then(Commands.literal("list")
                    .executes(Cwhitelist::list))

                .then(Commands.literal("add")
                    .then(Commands.argument("name", StringArgumentType.word())
                        .suggests(ONLINE_NAMES)
                        .executes(Cwhitelist::add)))

                .then(Commands.literal("remove")
                    .then(Commands.argument("name", StringArgumentType.word())
                        .suggests(WHITELISTED_NAMES)
                        .executes(Cwhitelist::remove)))
        );
    }

    private static int add(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        if (WhitelistStorage.add(name)) {
            context.getSource().sendSuccess(() -> Component.literal("Đã thêm " + name), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal(name + " đã có trong whitelist"));
        return 0;
    }

    private static int remove(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        if (WhitelistStorage.remove(name)) {
            context.getSource().sendSuccess(() -> Component.literal("Đã xóa " + name), true);
            return 1;
        }
        context.getSource().sendFailure(Component.literal(name + " không có trong whitelist"));
        return 0;
    }

    private static int list(CommandContext<CommandSourceStack> context) {
        String text = WhitelistStorage.list().isEmpty()
            ? "(trống)"
            : String.join(", ", WhitelistStorage.list());
        context.getSource().sendSuccess(() -> Component.literal("Whitelist: " + text), false);
        return 1;
    }
}