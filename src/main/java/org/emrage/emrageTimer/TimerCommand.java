package org.emrage.emrageTimer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimerCommand implements CommandExecutor, TabCompleter {

    private final Timer timer;
    private final ProfileManager profileManager;

    public TimerCommand(Timer timer, ProfileManager profileManager) {
        this.timer = timer;
        this.profileManager = profileManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You must be an OP to use this command.");
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /timer <pause|resume|set|gradient|show|stopOnDeath|stopOnEmpty|saveProfile|loadProfile|direction>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "pause":
                timer.pauseTimer();
                sender.sendMessage(ChatColor.GREEN + "Timer paused.");
                break;
            case "resume":
                timer.resumeTimer();
                sender.sendMessage(ChatColor.GREEN + "Timer resumed.");
                break;
            case "set":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer set <seconds>");
                    return true;
                }
                int seconds;
                try {
                    seconds = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number.");
                    return true;
                }
                timer.setTimer(seconds);
                sender.sendMessage(ChatColor.GREEN + "Timer set to " + seconds + " seconds.");
                break;
            case "gradient":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer gradient <color1> <color2>");
                    return true;
                }
                String color1 = args[1];
                String color2 = args[2];
                timer.setGradientColors(color1, color2);
                sender.sendMessage(ChatColor.GREEN + "Gradient colors set to " + color1 + " and " + color2 + ".");
                break;
            case "show":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer show <true|false>");
                    return true;
                }
                boolean show = Boolean.parseBoolean(args[1]);
                timer.setShowTimer(show);
                sender.sendMessage(ChatColor.GREEN + "Timer visibility set to " + show + ".");
                break;
            case "stopondeath":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer stopOnDeath <true|false>");
                    return true;
                }
                boolean stopOnDeath = Boolean.parseBoolean(args[1]);
                timer.setStopOnDeath(stopOnDeath);
                sender.sendMessage(ChatColor.GREEN + "Timer stop on death set to " + stopOnDeath + ".");
                break;
            case "stoponempty":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer stopOnEmpty <true|false>");
                    return true;
                }
                boolean stopOnEmpty = Boolean.parseBoolean(args[1]);
                timer.setStopOnEmpty(stopOnEmpty);
                sender.sendMessage(ChatColor.GREEN + "Timer stop on empty set to " + stopOnEmpty + ".");
                break;
            case "saveprofile":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer saveProfile <name>");
                    return true;
                }
                String profileName = args[1];
                TimerProfile profile = new TimerProfile(profileName, timer.getGradientColor1(), timer.getGradientColor2());
                profileManager.addProfile(profile);
                sender.sendMessage(ChatColor.GREEN + "Profile " + profileName + " saved.");
                break;
            case "loadprofile":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer loadProfile <name>");
                    return true;
                }
                profileName = args[1];
                profile = profileManager.getProfile(profileName);
                if (profile == null) {
                    sender.sendMessage(ChatColor.RED + "Profile " + profileName + " not found.");
                    return true;
                }
                timer.setGradientColors(profile.getGradientColor1(), profile.getGradientColor2());
                sender.sendMessage(ChatColor.GREEN + "Profile " + profileName + " loaded.");
                break;
            case "direction":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /timer direction <forwards|backwards>");
                    return true;
                }
                boolean countForwards = args[1].equalsIgnoreCase("forwards");
                timer.setCountForwards(countForwards);
                sender.sendMessage(ChatColor.GREEN + "Timer direction set to " + (countForwards ? "forwards" : "backwards") + ".");
                break;
            default:
                sender.sendMessage(ChatColor.YELLOW + "Usage: /timer <pause|resume|set|gradient|show|stopOnDeath|stopOnEmpty|saveProfile|loadProfile|direction>");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("pause");
            completions.add("resume");
            completions.add("set");
            completions.add("gradient");
            completions.add("show");
            completions.add("stopOnDeath");
            completions.add("stopOnEmpty");
            completions.add("saveProfile");
            completions.add("loadProfile");
            completions.add("direction");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("loadProfile")) {
            for (TimerProfile profile : profileManager.getProfiles()) {
                completions.add(profile.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("direction")) {
            completions.add("forwards");
            completions.add("backwards");
        }
        return completions;
    }
}