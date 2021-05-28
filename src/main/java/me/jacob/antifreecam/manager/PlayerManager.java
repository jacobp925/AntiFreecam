package me.jacob.antifreecam.manager;

import lombok.Getter;
import me.jacob.antifreecam.objects.FreecamPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

public class PlayerManager {

    @Getter
    private ArrayList<Player> staff = new ArrayList<>();

    public void violation(Player p) {
        int violations = FreecamPlayer.getByPlayer(p).addViolation();
        if (violations == 0 || violations % 100 == 0) {
            for (Player staff : getStaff()) {
                if (FreecamPlayer.getByPlayer(staff).isAlerts()) {
                    staff.sendMessage(color("&c[Freecam] &7" + p.getName() + " is suspected of using freecam &b[" + (violations == 0 ? "Activated" : violations / 20 + "s") + "]"));
                    Bukkit.getLogger().log(Level.INFO, p.getName() + " logged for freecam [" + violations / 20 + "s]");
                }
            }
        }
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
