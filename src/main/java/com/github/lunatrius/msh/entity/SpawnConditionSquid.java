package com.github.lunatrius.msh.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class SpawnConditionSquid extends SpawnConditionWaterMob {
	public SpawnConditionSquid(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}

	@Override
	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		return y > 45 && y < 63 ? SpawnType.BOTH.and(super.canSpawnAt(world, x, y, z)) : SpawnType.NONE;
	}
}
