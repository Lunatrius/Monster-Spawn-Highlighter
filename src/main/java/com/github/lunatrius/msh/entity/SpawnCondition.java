package com.github.lunatrius.msh.entity;

import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import com.github.lunatrius.msh.handler.ConfigurationHandler;
import com.github.lunatrius.msh.reference.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public final EntityLiving entity;
	public boolean enabled;

	public static final List<SpawnCondition> SPAWN_CONDITIONS = new ArrayList<SpawnCondition>();
	public static final Map<Class, SpawnCondition> CLASS_SPAWN_CONDITION_MAP = new HashMap<Class, SpawnCondition>();
	private static final Map<String, Class> STRING_CLASS_HASH_MAP = new HashMap<String, Class>();

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

	public static void populateData() {
		CLASS_SPAWN_CONDITION_MAP.clear();

		for (Map.Entry<String, Class> entry : STRING_CLASS_HASH_MAP.entrySet()) {
			String entityName = entry.getKey();
			Class spawnConditionClass = entry.getValue();
			Reference.logger.info(String.format("Setting up %s...", entityName));
			try {
				Entity entity = EntityList.createEntityByName(entityName, null);
				boolean enabled = ConfigurationHandler.isEntityEnabled(entityName);
				Constructor constructor = spawnConditionClass.getConstructor(String.class, EntityLiving.class, boolean.class);
				SpawnCondition spawnCondition = (SpawnCondition) constructor.newInstance(entityName, entity, enabled);

				adjustEntity(entity);
				MonsterSpawnHighlighter.proxy.constructTextureInformation((EntityLiving) entity);

				CLASS_SPAWN_CONDITION_MAP.put(entity.getClass(), spawnCondition);
			} catch (Exception e) {
				Reference.logger.error(String.format("Failed to set up %s!", entityName), e);
			}
		}

		SPAWN_CONDITIONS.clear();
		for (Map.Entry<Class, SpawnCondition> entry : CLASS_SPAWN_CONDITION_MAP.entrySet()) {
			SPAWN_CONDITIONS.add(entry.getValue());
		}
	}

	private static void adjustEntity(Entity entity) {
		if (entity instanceof EntitySlime) {
			((EntitySlime) entity).setSlimeSize(1);
		}
	}

	static {
		// vanilla - animals
		STRING_CLASS_HASH_MAP.put("Chicken", SpawnConditionAnimal.class);
		STRING_CLASS_HASH_MAP.put("Cow", SpawnConditionAnimal.class);
		STRING_CLASS_HASH_MAP.put("MushroomCow", SpawnConditionAnimal.class);
		STRING_CLASS_HASH_MAP.put("Ozelot", SpawnConditionOcelot.class);
		STRING_CLASS_HASH_MAP.put("Pig", SpawnConditionAnimal.class);
		STRING_CLASS_HASH_MAP.put("Sheep", SpawnConditionAnimal.class);
		STRING_CLASS_HASH_MAP.put("Wolf", SpawnConditionAnimal.class);

		// vanilla - ambient monsters
		STRING_CLASS_HASH_MAP.put("Bat", SpawnConditionBat.class);

		// vanilla - water monsters
		STRING_CLASS_HASH_MAP.put("Squid", SpawnConditionSquid.class);

		// vanilla - monsters
		STRING_CLASS_HASH_MAP.put("Creeper", SpawnConditionMob.class);
		STRING_CLASS_HASH_MAP.put("Enderman", SpawnConditionMob.class);
		STRING_CLASS_HASH_MAP.put("Skeleton", SpawnConditionMob.class);
		STRING_CLASS_HASH_MAP.put("Slime", SpawnConditionSlime.class);
		STRING_CLASS_HASH_MAP.put("Spider", SpawnConditionMob.class);
		STRING_CLASS_HASH_MAP.put("Witch", SpawnConditionMob.class);
		STRING_CLASS_HASH_MAP.put("Zombie", SpawnConditionMob.class);

		// vanilla - nether monsters
		STRING_CLASS_HASH_MAP.put("Ghast", SpawnConditionGhast.class);
		STRING_CLASS_HASH_MAP.put("LavaSlime", SpawnConditionMagmaCube.class);
		STRING_CLASS_HASH_MAP.put("PigZombie", SpawnConditionPigZombie.class);
	}
}
