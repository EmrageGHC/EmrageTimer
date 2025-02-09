package org.emrage.emrageTimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class Timer {

    private final JavaPlugin plugin;
    private final NamespacedKey key = new NamespacedKey("spectator", "exclude");
    private int timerSeconds;
    public boolean isPaused;
    private boolean showTimer;
    private boolean stopOnDeath;
    private boolean stopOnEmpty;
    private boolean countForwards;
    private double gradientOffset = 0.0;
    private String gradientColor1;
    private String gradientColor2;
    private String idleMessage;
    private String timerMessage;

    public Timer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.timerSeconds = plugin.getConfig().getInt("timer-seconds", 0);
        this.isPaused = true;
        this.showTimer = plugin.getConfig().getBoolean("show-timer", true);
        this.stopOnDeath = plugin.getConfig().getBoolean("stop-on-death", false);
        this.stopOnEmpty = plugin.getConfig().getBoolean("stop-on-empty", false);
        this.countForwards = plugin.getConfig().getString("timer-direction", "forwards").equalsIgnoreCase("forwards");
        loadConfigValues();
        startTasks();
        Bukkit.getPluginManager().registerEvents(new TimerEventListener(this), plugin);
    }

    public void startTimer(int seconds) {
        timerSeconds = seconds;
        isPaused = false;
    }

    public void pauseTimer() {
        isPaused = true;
    }

    public void resumeTimer() {
        isPaused = false;
    }

    public void setTimer(int seconds) {
        timerSeconds = seconds;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void saveTimerSeconds() {
        plugin.getConfig().set("timer-seconds", timerSeconds);
        plugin.saveConfig();
    }

    public void setGradientColors(String color1, String color2) {
        this.gradientColor1 = color1;
        this.gradientColor2 = color2;
        saveConfigValues();
    }

    public void setShowTimer(boolean show) {
        this.showTimer = show;
        plugin.getConfig().set("show-timer", show);
        plugin.saveConfig();
    }

    public void setStopOnDeath(boolean stop) {
        this.stopOnDeath = stop;
        plugin.getConfig().set("stop-on-death", stop);
        plugin.saveConfig();
    }

    public void setStopOnEmpty(boolean stop) {
        this.stopOnEmpty = stop;
        plugin.getConfig().set("stop-on-empty", stop);
        plugin.saveConfig();
    }

    public boolean isStopOnEmpty() {
        return stopOnEmpty;
    }

    public boolean isStopOnDeath() {
        return stopOnDeath;
    }

    public String getGradientColor1() {
        return gradientColor1;
    }

    public String getGradientColor2() {
        return gradientColor2;
    }

    public void setCountForwards(boolean countForwards) {
        this.countForwards = countForwards;
        plugin.getConfig().set("timer-direction", countForwards ? "forwards" : "backwards");
        plugin.saveConfig();
    }

    public boolean isCountForwards() {
        return countForwards;
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void saveConfigValues() {
        plugin.getConfig().set("gradient-color1", gradientColor1);
        plugin.getConfig().set("gradient-color2", gradientColor2);
        plugin.getConfig().set("idle-message", idleMessage);
        plugin.getConfig().set("timer-message", timerMessage);
        plugin.saveConfig();
    }

    private void loadConfigValues() {
        gradientColor1 = plugin.getConfig().getString("gradient-color1", "#707CF7");
        gradientColor2 = plugin.getConfig().getString("gradient-color2", "#F658CF");
        idleMessage = plugin.getConfig().getString("idle-message", "<gold><italic>Idle</italic></gold>");
        timerMessage = plugin.getConfig().getString("timer-message", "<gradient:%color1%:%color2%:%offset%><b>%time%</b></gradient>");
    }

    private void startTasks() {
        // Timer task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    if (countForwards) {
                        timerSeconds++;
                    } else {
                        timerSeconds--;
                    }
                }
                updateActionBar();
            }
        }.runTaskTimer(plugin, 0, 20);

        // Gradient animation task
        new BukkitRunnable() {
            @Override
            public void run() {
                gradientOffset += 0.05;
                if (gradientOffset > 1) {
                    gradientOffset -= 2;
                }
                updateActionBar();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void updateActionBar() {
        if (!showTimer) {
            return;
        }

        MiniMessage miniMessage = MiniMessage.miniMessage();
        if (isPaused) {
            Component idleText = miniMessage.deserialize(idleMessage);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                    player.sendActionBar(idleText);
                }
            }
        } else {
            int days = (int) TimeUnit.SECONDS.toDays(timerSeconds);
            int hours = (int) (TimeUnit.SECONDS.toHours(timerSeconds) % 24);
            int minutes = (int) (TimeUnit.SECONDS.toMinutes(timerSeconds) % 60);
            int seconds = (int) (timerSeconds % 60);

            StringBuilder timeString = new StringBuilder();
            if (days > 0) {
                timeString.append(days).append("d, ");
            }
            if (hours > 0 || days > 0) {
                timeString.append(hours).append("h ");
            }
            if (minutes > 0 || hours > 0 || days > 0) {
                timeString.append(minutes).append("m ");
            }
            if (seconds > 0 || (days == 0 && hours == 0 && minutes == 0)) {
                timeString.append(seconds).append("s");
            }

            String formattedMessage = timerMessage
                    .replace("%color1%", gradientColor1)
                    .replace("%color2%", gradientColor2)
                    .replace("%offset%", String.valueOf(gradientOffset))
                    .replace("%time%", timeString.toString().trim());

            Component timerText = miniMessage.deserialize(formattedMessage);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                    player.sendActionBar(timerText);
                }
            }
        }
    }
}