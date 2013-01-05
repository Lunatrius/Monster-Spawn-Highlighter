package lunatrius.msh;

import net.minecraft.entity.EntityLiving;

public class EntityLivingEntry {
	public String name = "";
	public EntityLiving entity = null;
	public boolean enabled = false;

	public EntityLivingEntry(String name, EntityLiving entity, boolean enabled) {
		this.name = name;
		this.entity = entity;
		this.enabled = enabled;
	}
}
