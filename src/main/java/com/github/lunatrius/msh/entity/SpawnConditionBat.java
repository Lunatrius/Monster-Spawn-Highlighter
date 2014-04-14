package com.github.lunatrius.msh.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Calendar;

public class SpawnConditionBat extends SpawnCondition {
	public SpawnConditionBat(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}

	@Override
	public SpawnType canSpawnAt(World world, int x, int y, int z) {
		y = MathHelper.floor_double(this.entity.boundingBox.minY);

		if (y < 63) {
			int light = getBlockLightLevel(world, x, y, z, LIGHT_NIGHT);
			int requiredLight = 4;
			Calendar calendar = world.getCurrentDate();

			if ((calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) >= 20) || (calendar.get(Calendar.MONTH) + 1 == 11 && calendar.get(Calendar.DATE) <= 3)) {
				requiredLight = 7;
			}

			return light > (requiredLight - 1) ? SpawnType.NONE : super.canSpawnAt(world, x, y, z);
		}

		return SpawnType.NONE;
	}
}
