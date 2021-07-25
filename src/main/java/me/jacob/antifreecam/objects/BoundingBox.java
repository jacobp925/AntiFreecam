package me.jacob.antifreecam.objects;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.util.Vector;

public class BoundingBox {

    @Getter
    private Vector max;
    @Getter
    private Vector min;

    public BoundingBox(Block block) {
        IBlockData blockData = ((CraftWorld) block.getWorld()).getHandle().getType(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        net.minecraft.server.v1_8_R3.Block blockNative = blockData.getBlock();
        blockNative.updateShape(((CraftWorld) block.getWorld()).getHandle(), new BlockPosition(block.getX(), block.getY(), block.getZ()));
        min = new Vector((double) block.getX() + blockNative.B(), (double) block.getY() + blockNative.D(), (double) block.getZ() + blockNative.F());
        max = new Vector((double) block.getX() + blockNative.C(), (double) block.getY() + blockNative.E(), (double) block.getZ() + blockNative.G());
    }
}