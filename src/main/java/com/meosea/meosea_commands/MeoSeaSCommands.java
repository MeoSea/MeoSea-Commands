package com.meosea.meosea_commands;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meosea.meosea_commands.Storages.WhitelistStorage;
import com.meosea.meosea_commands.commands.ModCommands;

public class MeoSeaSCommands implements ModInitializer {
	public static final String MOD_ID = "meosea-commands";

	// Logger này dùng để ghi chữ ra console và file log.
	// Cách tốt nhất là đặt tên logger theo mod id của bạn.
	// Như vậy sẽ biết rõ mod nào đã ghi thông tin, cảnh báo hoặc lỗi.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Đoạn code này chạy ngay khi Minecraft đã sẵn sàng để nạp mod.
		// Tuy nhiên, một số thứ (như resources) có thể vẫn chưa được khởi tạo.
		// Hãy tiến hành cẩn thận một chút.

		WhitelistStorage.load();
		ModCommands.registerAll();
		LOGGER.info("Hello Fabric world!");
	}

	@SuppressWarnings("null") 
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
