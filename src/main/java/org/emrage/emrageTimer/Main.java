package org.emrage.emrageTimer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class Main extends JavaPlugin implements Listener {

    private Timer timer;
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        profileManager = new ProfileManager(getConfig());
        timer = new Timer(this);
        TimerCommand timerCommand = new TimerCommand(timer, profileManager);

        PluginCommand command = this.getCommand("timer");
        if (command != null) {
            command.setExecutor(timerCommand);
            command.setTabCompleter(timerCommand);
        }
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new TimerEventListener(timer), this);
        pm.registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if (timer != null) {
            timer.saveTimerSeconds();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (timer.isStopOnDeath()) {
            timer.pauseTimer();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (timer.isStopOnEmpty() && Bukkit.getOnlinePlayers().size() == 1) {
            timer.pauseTimer();
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (timer.isPaused() && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (timer.isPaused() && event.getTarget() instanceof Player) {
            event.setCancelled(true);
        }
    }
}