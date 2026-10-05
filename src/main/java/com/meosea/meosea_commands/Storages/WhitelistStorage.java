package com.meosea.meosea_commands.Storages;

import com.meosea.meosea_commands.MeoSeaSCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WhitelistStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
        FabricLoader.getInstance().getConfigDir().resolve("meosea-whitelist.json");

    // LinkedHashSet: không trùng tên, giữ thứ tự thêm vào
    private static final Set<String> NAMES = new LinkedHashSet<>();

    public static void load() {
        if (!Files.exists(FILE)) return;
        try (Reader reader = Files.newBufferedReader(FILE)) {
            String[] arr = GSON.fromJson(reader, String[].class);
            NAMES.clear();
            if (arr != null) NAMES.addAll(Arrays.asList(arr));
        } catch (IOException e) {
            MeoSeaSCommands.LOGGER.error("Không đọc được whitelist", e);
        }
    }

    private static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(NAMES));
        } catch (IOException e) {
            MeoSeaSCommands.LOGGER.error("Không ghi được whitelist", e);
        }
    }

    public static boolean add(String name) {
        boolean added = NAMES.add(name.toLowerCase());
        if (added) save();
        return added;               // false nếu đã có sẵn
    }

    public static boolean remove(String name) {
        boolean removed = NAMES.remove(name.toLowerCase());
        if (removed) save();
        return removed;             // false nếu không có trong list
    }

    public static boolean contains(String name) {
        return NAMES.contains(name.toLowerCase());
    }

    public static Collection<String> list() {
        return Collections.unmodifiableCollection(NAMES);
    }
}