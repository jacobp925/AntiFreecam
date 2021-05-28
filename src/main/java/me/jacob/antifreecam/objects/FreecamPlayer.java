package me.jacob.antifreecam.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FreecamPlayer {

    @Getter
    public static HashMap<UUID, FreecamPlayer> freecamPlayers = new HashMap<>();

    @Getter
    private UUID UUID;
    @Getter
    private String name;
    @Getter
    private int violations;
    @Getter
    private long lastViolation;
    @Getter
    @Setter
    private boolean alerts;

    public FreecamPlayer(Player p) {
        this.UUID = p.getUniqueId();
        this.name = p.getName();
        this.violations = 0;
        this.lastViolation = System.currentTimeMillis();
        freecamPlayers.put(this.UUID, this);
        this.alerts = true;
    }

    public int addViolation() {
        if (System.currentTimeMillis() - this.lastViolation > 1000) this.violations = 0; //Reset violation counter
        this.lastViolation = System.currentTimeMillis();
        return this.violations++;
    }

    public boolean toggleAlerts() {
        this.alerts = !this.alerts;
        return this.alerts;
    }

    public static void remove(UUID uuid) {
        freecamPlayers.remove(uuid);
    }

    public static FreecamPlayer getByPlayer(Player p) {
        return freecamPlayers.get(p.getUniqueId());
    }
}
