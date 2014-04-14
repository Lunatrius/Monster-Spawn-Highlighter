package com.github.lunatrius.msh.entity;

import net.minecraft.entity.EntityLiving;

public class SpawnConditionCreature extends SpawnCondition {
	public SpawnConditionCreature(String name, EntityLiving entity, boolean enabled) {
		super(name, entity, enabled);
	}
}
