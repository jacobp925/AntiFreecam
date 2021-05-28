package me.jacob.antifreecam.commands;

import com.google.common.base.Joiner;
import me.jacob.antifreecam.objects.FreecamPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AntiFreecamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("antifreecam")) {
            Player p = (Player) sender;
            if (p.hasPermission("antifreecam.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("alerts")) {
                        FreecamPlayer freecamPlayer = FreecamPlayer.getByPlayer(p);
                        p.sendMessage(color("&c[Freecam]&7 Alerts toggled " + (freecamPlayer.toggleAlerts() ? "&aON" : "&cOFF")));
                        return true;
                    } else if (args[0].equalsIgnoreCase("list")) {
                        long time = System.currentTimeMillis();
                        List<String> players = new ArrayList<>();
                        for (FreecamPlayer freecamPlayer : FreecamPlayer.getFreecamPlayers().values()) {
                            if (time - freecamPlayer.getLastViolation() < 1000) {
                                players.add(freecamPlayer.getName());
                            }
                        }
                        p.sendMessage(color("&f&lPlayers In Freecam:"));
                        p.sendMessage(format(players, net.md_5.bungee.api.ChatColor.RESET + ", "));
                        return true;
                    } else {
                        helpMessage(p);
                        return true;
                    }
                } else {
                    helpMessage(p);
                    return true;
                }
            } else {
                p.sendMessage(color("&cNo Permission"));
                return true;
            }
        }
        return false;
    }

    private static String format(Iterable<?> objects, String separators) {
        return Joiner.on(separators).join(objects);
    }

    private void helpMessage(Player p) {
        p.sendMessage(color("&7&l&m------------------------"));
        p.sendMessage(color("&eManage AntiFreecam"));
        p.sendMessage("");
        p.sendMessage(color("&7/antifreecam&a&l alerts"));
        p.sendMessage(color("&7/antifreecam&c&l list"));
        p.sendMessage(color("&7&l&m------------------------"));
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
