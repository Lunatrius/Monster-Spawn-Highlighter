package com.github.lunatrius.msh;

import net.minecraft.entity.EntityLiving;

public class EntityLivingEntry {
	public final String name;
	public final EntityLiving entity;
	public boolean enabled;

	public EntityLivingEntry(String name, EntityLiving entity, boolean enabled) {
		this.name = name;
		this.entity = entity;
		this.enabled = enabled;
	}
}
