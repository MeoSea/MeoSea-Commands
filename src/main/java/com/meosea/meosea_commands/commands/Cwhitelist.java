package com.meosea.meosea_commands.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SuppressWarnings("null")
public class Cwhitelist {

    // Gợi ý tên đang có trong whitelist (dùng cho nhánh remove)
    private static final SuggestionProvider<CommandSourceStack> WHITELISTED_NAMES =
        (context, builder) -> SharedSuggestionProvider.suggest(
            whitelist(context).getUserList(), builder);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("cwhitelist")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))

                .then(Commands.literal("list")
                    .executes(Cwhitelist::list))

                .then(Commands.literal("add")
                    .then(Commands.argument("name", StringArgumentType.word())
                        .executes(Cwhitelist::add)))

                .then(Commands.literal("remove")
                    .then(Commands.argument("name", StringArgumentType.word())
                        .suggests(WHITELISTED_NAMES)
                        .executes(Cwhitelist::remove)))
        );
    }

    // Whitelist vanilla của server (ghi vào whitelist.json, tự lưu khi add/remove)
    private static UserWhiteList whitelist(CommandContext<CommandSourceStack> context) {
        return context.getSource().getServer().getPlayerList().getWhiteList();
    }

    // UUID offline chuẩn của vanilla: phân biệt hoa/thường theo tên
    static UUID offlineUuid(String name) {
        return UUID.nameUUIDFromBytes(
            ("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }

    static boolean isValidName(String name) {
        return name.matches("[A-Za-z0-9_]{3,16}");
    }

    private static int add(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        if (!isValidName(name)) {
            context.getSource().sendFailure(
                Component.literal("Tên không hợp lệ (3-16 ký tự: chữ, số, dấu gạch dưới)"));
            return 0;
        }

        UserWhiteList whitelist = whitelist(context);
        NameAndId player = new NameAndId(offlineUuid(name), name);

        if (whitelist.isWhiteListed(player)) {
            context.getSource().sendFailure(Component.literal(name + " đã có trong whitelist"));
            return 0;
        }

        // Cùng tên nhưng khác hoa/thường sẽ ra UUID khác -> chặn để khỏi thêm nhầm
        for (String existing : whitelist.getUserList()) {
            if (existing.equalsIgnoreCase(name)) {
                context.getSource().sendFailure(Component.literal(
                    "Đã có tên gần giống: " + existing + " (khác hoa/thường nên UUID khác nhau)"));
                return 0;
            }
        }

        whitelist.add(new UserWhiteListEntry(player));
        context.getSource().sendSuccess(
            () -> Component.literal("Đã thêm " + name + " (" + player.id() + ")"), true);
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        UserWhiteList whitelist = whitelist(context);
        NameAndId player = new NameAndId(offlineUuid(name), name);

        if (!whitelist.isWhiteListed(player)) {
            context.getSource().sendFailure(Component.literal(
                name + " không có trong whitelist (hoặc không phải acc offline, gõ đúng hoa/thường)"));
            return 0;
        }
        whitelist.remove(player);
        context.getSource().sendSuccess(() -> Component.literal("Đã xóa " + name), true);
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> context) {
        String[] names = whitelist(context).getUserList();
        String text = names.length == 0 ? "(trống)" : String.join(", ", names);
        context.getSource().sendSuccess(() -> Component.literal("Whitelist: " + text), false);
        return 1;
    }
}