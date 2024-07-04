package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.webhead1104.township.commands.TownshipCommand;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.listeners.InventoryClickListener;
import me.webhead1104.township.listeners.JoinListener;
import net.cytonic.cytosis.Cytosis;
import net.cytonic.cytosis.events.EventListener;
import net.cytonic.cytosis.logging.Logger;
import net.cytonic.cytosis.plugins.CytosisPlugin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Township implements CytosisPlugin {

    public static Township INSTANCE;
    public static JsonObject dataTable;
    @Getter
    private static Factories factories;
    @Getter
    private static WorldManager worldManager;
    @Getter
    private static LevelMenu levelMenu;
    @Getter
    private static Animals animals;
    @Getter
    private static Map<UUID, User> userMap = new HashMap<>();
    @Getter
    private static Map<UUID, Integer> userPageMap = new HashMap<>();


    public void initialize() {
        INSTANCE = this;
        File file = new File("township/config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Logger.error("Error", e);
            }
        }
        factories = new Factories();
        worldManager = new WorldManager();
        levelMenu = new LevelMenu();
        animals = new Animals();
        try {
            File file1 = new File("township/database.json");
            try {
                file1.createNewFile();
            } catch (Exception e) {
                Logger.error("Error", e);
            }
            dataTable = new Gson().fromJson(new String(
                    Files.readAllBytes(Paths.get("township/database.json"))), JsonObject.class);
            //noinspection ResultOfMethodCallIgnored
            new File("township/database.json").delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Cytosis.getCommandManager().register(new TownshipCommand());
        registerListeners();
    }

    public void shutdown() {
        Logger.debug("Township shutting down!");
    }

    public void registerListeners() {
        Cytosis.getEventHandler().registerListener(new EventListener<>("township:player-spawn", false, 1,
                PlayerSpawnEvent.class, (event -> new JoinListener().onJoin(event))));
        Cytosis.getEventHandler().registerListener(new EventListener<>("township:inventory-preclick", false, 1,
                InventoryPreClickEvent.class, (event -> new InventoryClickListener().onClick(event))));
    }
}