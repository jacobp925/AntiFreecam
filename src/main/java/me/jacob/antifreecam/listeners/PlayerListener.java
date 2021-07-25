package me.jacob.antifreecam.listeners;

import me.jacob.antifreecam.AntiFreecam;
import me.jacob.antifreecam.objects.BoundingBox;
import me.jacob.antifreecam.objects.FreecamPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
import org.bukkit.material.Stairs;
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
                Material currentMat;
                Location currentLoc;
                for (double d = 0; d <= 5; d += 0.01) {
                    currentMat = (currentBlock = (currentLoc = origin.clone().add(direction.clone().multiply(d)).toLocation(p.getWorld())).getBlock()).getType();
                    if (solidBlock(currentBlock, currentMat, currentLoc)) {
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

    public boolean solidBlock(Block block, Material material, Location location) {
        //NOTE: inefficient and could be improved upon by not calculating a new aabb for the block every single time.. don't have time rn maybe ill do this later lol.
        if (material == Material.AIR || block.isLiquid() || block.getState() instanceof Sign || block.getState().getData() instanceof Stairs) {
            return false;
        }
        BoundingBox aabb = new BoundingBox(block);
        if (location.getX() < aabb.getMin().getX() || location.getX() > aabb.getMax().getX()) {
            return false;
        } else if (location.getY() < aabb.getMin().getY() || location.getY() > aabb.getMax().getY()) {
            return false;
        } else if (location.getZ() < aabb.getMin().getZ() || location.getZ() > aabb.getMax().getZ()) {
            return false;
        }
        return true;
    }
}
