package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String townName;
    private int level;
    private int population;
    private int coins;
    private int cash;
    private int section;
    private Barn barn;
    private Animals animals;
    private Factories factories;
    private World world;
    private Trains trains;

    public static User fromJson(String json) {
        return Township.GSON.fromJson(json, User.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}