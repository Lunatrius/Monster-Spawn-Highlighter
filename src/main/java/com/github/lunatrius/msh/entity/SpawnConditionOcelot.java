package com.github.lunatrius.msh.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class SpawnConditionOcelot extends SpawnConditionAnimal {
	public SpawnConditionOcelot(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}

	@Override
	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		if (hasNoCollisions(world)) {
			if (y >= 63) {
				Block block = world.getBlock(x, y - 1, z);
				if (block == Blocks.grass || block.isLeaves(world, x, y - 1, z)) {
					return SpawnType.BOTH;
				}
			}
		}

		return SpawnType.NONE;
	}
}
