package me.jacob.antifreecam;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import me.jacob.antifreecam.commands.AntiFreecamCommand;
import me.jacob.antifreecam.listeners.PlayerListener;
import me.jacob.antifreecam.manager.PlayerManager;
import me.jacob.antifreecam.objects.FreecamPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiFreecam extends JavaPlugin {

    public static AntiFreecam plugin;
    @Getter
    private PlayerManager manager;

    public void onEnable() {
        plugin = this;
        getServer().getConsoleSender().sendMessage("AntiFreecam has been enabled");
        manager = new PlayerManager();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("antifreecam.admin")) {
                getManager().getStaff().add(p);
            }
            new FreecamPlayer(p);
        }
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("antifreecam").setExecutor(new AntiFreecamCommand());
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.ABILITIES) {
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                if (!p.getAllowFlight()) {
                    getManager().violation(p);
                }
            }
        });
    }

    public void onDisable() {
        plugin = null;
        getServer().getConsoleSender().sendMessage("AntiFreecam has been disabled");
    }
}