package me.jacob.antifreecam.listeners;

import me.jacob.antifreecam.AntiFreecam;
import me.jacob.antifreecam.objects.FreecamPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private AntiFreecam plugin;

    public PlayerListener(AntiFreecam instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        new FreecamPlayer(p);
        if (p.hasPermission("antifreecam.admin")) {
            plugin.getManager().getStaff().add(p);
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        FreecamPlayer.remove(p.getUniqueId());
        if (p.hasPermission("antifreecam.admin")) {
            plugin.getManager().getStaff().remove(p);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.isCancelled()) {
            Block b = event.getClickedBlock();
            if ((event.getItem() != null && event.getItem().getType() == Material.MONSTER_EGG) || b.getState() instanceof InventoryHolder) {
                Player p = event.getPlayer();
                Vector origin = p.getEyeLocation().toVector();
                Vector direction = p.getEyeLocation().getDirection();
                Block rayTraced = null;
                Block currentBlock;
                for (double d = 0; d <= 5; d += 0.01) {
                    //p.getWorld().playEffect(origin.clone().add(direction.clone().multiply(d)).toLocation(p.getWorld()), Effect.COLOURED_DUST, 0);
                    if ((currentBlock = origin.clone().add(direction.clone().multiply(d)).toLocation(p.getWorld()).getBlock()).getType() != Material.AIR && !currentBlock.isLiquid() && !(currentBlock.getState() instanceof Sign)) {
                        rayTraced = currentBlock;
                        break;
                    }
                }
                if (rayTraced == null || !b.equals(rayTraced)) {
                    event.setCancelled(true);
                    for (Player staff : plugin.getManager().getStaff()) {
                        if (FreecamPlayer.getByPlayer(staff).isAlerts()) {
                            staff.sendMessage(color("&c[Freecam] &7" + p.getName() + " attempted an illegal interaction"));
                        }
                    }
                }
            }
        }
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
