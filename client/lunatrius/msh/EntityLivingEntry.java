package lunatrius.msh;

import net.minecraft.src.EntityLiving;

public class EntityLivingEntry {
	public String name = "";
	public String nameObf = "";
	public EntityLiving entity = null;
	public boolean enabled = false;

	public EntityLivingEntry(String name, EntityLiving entity, boolean enabled) {
		this.name = name;
		this.nameObf = entity.getClass().getSimpleName();
		this.entity = entity;
		this.enabled = enabled;
	}
}
