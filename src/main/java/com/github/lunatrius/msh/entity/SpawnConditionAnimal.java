package com.github.lunatrius.msh.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class SpawnConditionAnimal extends SpawnConditionCreature {
	public SpawnConditionAnimal(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}

	@Override
	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z) == Blocks.grass && getBlockLightLevel(world, x, y, z, LIGHT_DAY) > 8 ? SpawnType.DAY : SpawnType.NONE;
	}
}
