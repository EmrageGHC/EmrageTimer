package org.emrage.emrageTimer;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private final List<TimerProfile> profiles = new ArrayList<>();
    private final FileConfiguration config;

    public ProfileManager(FileConfiguration config) {
        this.config = config;
        loadProfiles();
    }

    public void addProfile(TimerProfile profile) {
        profiles.add(profile);
        saveProfiles();
    }

    public TimerProfile getProfile(String name) {
        for (TimerProfile profile : profiles) {
            if (profile.getName().equalsIgnoreCase(name)) {
                return profile;
            }
        }
        return null;
    }

    public List<TimerProfile> getProfiles() {
        return profiles;
    }

    private void loadProfiles() {
        if (config.isConfigurationSection("profiles")) {
            for (String key : config.getConfigurationSection("profiles").getKeys(false)) {
                String color1 = config.getString("profiles." + key + ".color1");
                String color2 = config.getString("profiles." + key + ".color2");
                profiles.add(new TimerProfile(key, color1, color2));
            }
        }
    }

    private void saveProfiles() {
        for (TimerProfile profile : profiles) {
            config.set("profiles." + profile.getName() + ".color1", profile.getGradientColor1());
            config.set("profiles." + profile.getName() + ".color2", profile.getGradientColor2());
        }
    }
}