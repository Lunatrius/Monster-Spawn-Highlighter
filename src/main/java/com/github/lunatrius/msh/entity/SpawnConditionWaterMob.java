package com.github.lunatrius.msh.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class SpawnConditionWaterMob extends SpawnConditionCreature {
	public SpawnConditionWaterMob(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}

	@Override
	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		return world.checkNoEntityCollision(this.entity.boundingBox) ? SpawnType.BOTH : SpawnType.NONE;
	}
}
