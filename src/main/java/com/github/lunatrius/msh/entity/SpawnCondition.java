package com.github.lunatrius.msh.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public abstract class SpawnCondition {
	public final static int LIGHT_DAY = 0;
	public final static int LIGHT_NIGHT = 16;

	public enum SpawnType {
		NONE,
		DAY,
		NIGHT,
		BOTH;

		public SpawnType or(SpawnType rho) {
			SpawnType[] values = values();
			return values[(this.ordinal() | rho.ordinal()) % values.length];
		}

		public SpawnType and(SpawnType rho) {
			SpawnType[] values = values();
			return values[(this.ordinal() & rho.ordinal()) % values.length];
		}
	}

	public final String name;
	public final Entity entity;
	public boolean enabled;

	public SpawnCondition(String name, EntityLiving entity, boolean enabled) {
		this.name = name;
		this.entity = entity;
		this.enabled = enabled;
	}

	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		return hasNoCollisions(world) ? SpawnType.BOTH : SpawnType.NONE;
	}

	protected boolean hasNoCollisions(World world) {
		return world.checkNoEntityCollision(this.entity.boundingBox) && world.getCollidingBoundingBoxes(this.entity, this.entity.boundingBox).isEmpty() && !world.isAnyLiquid(this.entity.boundingBox);
	}

	protected int getBlockLightLevel(World world, int x, int y, int z, int kst) {
		return world.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockLightValue(x & 0xF, y, z & 0xF, kst);
	}
}
