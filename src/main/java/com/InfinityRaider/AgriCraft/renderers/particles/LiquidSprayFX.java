package com.InfinityRaider.AgriCraft.renderers.particles;

//heavily inspired by the OpenBlocks sprinkler

import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class LiquidSprayFX extends EntityFX {
    public LiquidSprayFX(World world, double x, double y, double z, float scale, float gravity, Vec3 vector) {
        super(world, x, y, z, 0 , 0, 0);
        this.lastTickPosX = this.prevPosX = this.posX = x;
        this.lastTickPosY = this.prevPosY = this.posY = y;
        this.lastTickPosZ = this.prevPosZ = this.posZ = z;
        setParticleTextureIndex(FluidRegistry.WATER.getSpriteNumber());
        this.particleGravity = gravity;
        this.particleScale = scale;
        this.particleMaxAge = 15;
        this.setSize(0.2f, 0.2f);
        this.noClip = false;
        this.motionX = vector.xCoord;
        this.motionY = vector.yCoord;
        this.motionZ = vector.zCoord;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}
