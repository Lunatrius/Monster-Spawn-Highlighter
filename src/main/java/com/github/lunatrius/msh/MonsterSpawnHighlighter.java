package com.github.lunatrius.msh;

import com.github.lunatrius.core.util.vector.Vector3f;
import com.github.lunatrius.core.util.vector.Vector4i;
import com.github.lunatrius.msh.client.Events;
import com.github.lunatrius.msh.client.gui.TextureInformation;
import com.github.lunatrius.msh.client.renderer.Renderer;
import com.github.lunatrius.msh.config.Config;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

@Mod(modid = "MonsterSpawnHighlighter")
public class MonsterSpawnHighlighter {
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private World world = null;
	public final KeyBinding toggleKey = new KeyBinding("key.msh.toggle", Keyboard.KEY_L, "key.category.msh");
	private final Frustrum frustrum = new Frustrum();
	private final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	private Map<Integer, Map<Class, EnumCreatureType>> biomeCreatureSpawnMapping = new HashMap<Integer, Map<Class, EnumCreatureType>>();
	public final List<Vector4i> spawnList = new ArrayList<Vector4i>();
	public Vector3f playerPosition = new Vector3f();
	private int ticks = -1;

	public Config config = null;
	public List<String> entityBlacklist = new ArrayList<String>();
	public List<EntityLivingEntry> entityList = new ArrayList<EntityLivingEntry>();
	public Map<Class<? extends EntityLiving>, TextureInformation> entityIcons = new HashMap<Class<? extends EntityLiving>, TextureInformation>();

	public boolean hasSeed = false;
	public long seed = 0;

	@Instance("MonsterSpawnHighlighter")
	public static MonsterSpawnHighlighter instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.config = new Config(event.getSuggestedConfigurationFile());
		this.config.save();

		// vanilla
		this.entityBlacklist.add("Blaze");
		this.entityBlacklist.add("CaveSpider");
		this.entityBlacklist.add("EnderDragon");
		this.entityBlacklist.add("Giant");
		this.entityBlacklist.add("Mob");
		this.entityBlacklist.add("Monster");
		this.entityBlacklist.add("SnowMan");
		this.entityBlacklist.add("Villager");
		this.entityBlacklist.add("VillagerGolem");
		this.entityBlacklist.add("WitherBoss");
		this.entityBlacklist.add("Silverfish");
		this.entityBlacklist.add("Witch");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		try {
			MinecraftForge.EVENT_BUS.register(new Renderer(this.minecraft));

			ClientRegistry.registerKeyBinding(this.toggleKey);

			FMLCommonHandler.instance().bus().register(new Events());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Set<String> entityNames = EntityList.stringToClassMapping.keySet();
		for (String entityName : entityNames) {
			if (this.entityBlacklist.contains(entityName)) {
				continue;
			}

			Entity entity = EntityList.createEntityByName(entityName, null);
			if (entity instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving) entity;
				boolean enabled = this.config.isEntityEnabled(entityName);
				EntityLivingEntry entityLivingEntry = new EntityLivingEntry(entityName, entityLiving, enabled);
				this.entityList.add(entityLivingEntry);

				if (entityLivingEntry.entity instanceof EntitySlime) {
					try {
						Method method = ReflectionHelper.findMethod(EntitySlime.class, (EntitySlime) entityLivingEntry.entity, new String[] {
								"func_70799_a", "a", "setSlimeSize"
						}, new Class[] { int.class });
						method.invoke(entityLivingEntry.entity, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				try {
					TextureInformation textureInformation = new TextureInformation(entityLiving);
					this.entityIcons.put(entityLiving.getClass(), textureInformation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		try {
			this.hasSeed = true;
			this.seed = event.getServer().worldServerForDimension(0).getSeed();
		} catch (Exception e) {
			this.hasSeed = false;
		}
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		this.hasSeed = false;
	}

	public boolean onTick() {
		if (--this.ticks < 0) {
			this.ticks = this.config.updateRate;

			if (this.minecraft != null && this.minecraft.theWorld != null && this.config.renderSpawns != 0) {
				this.spawnList.clear();

				this.frustrum.setPosition(this.playerPosition.x, this.playerPosition.y, this.playerPosition.z);

				this.world = this.minecraft.theWorld;

				int lowX, lowY, lowZ, highX, highY, highZ, x, y, z, type;

				lowX = (int) (Math.floor(this.playerPosition.x) - this.config.renderRangeXZ);
				highX = (int) (Math.floor(this.playerPosition.x) + this.config.renderRangeXZ);
				lowY = (int) (Math.floor(this.playerPosition.y) - this.config.renderRangeYBellow);
				highY = (int) (Math.floor(this.playerPosition.y) + this.config.renderRangeYAbove);
				lowZ = (int) (Math.floor(this.playerPosition.z) - this.config.renderRangeXZ);
				highZ = (int) (Math.floor(this.playerPosition.z) + this.config.renderRangeXZ);

				for (y = lowY; y <= highY; y++) {
					for (x = lowX; x <= highX; x++) {
						for (z = lowZ; z <= highZ; z++) {
							if (!this.frustrum.isBoundingBoxInFrustum(this.boundingBox.setBounds(x, y, z, x + 1, y + 1, z + 1))) {
								continue;
							}

							setEntityLivingLocation(x, y, z);

							if ((type = getCanSpawnHere(x, y, z)) > 0) {
								this.spawnList.add(new Vector4i(x, y, z, type));
							}
						}
					}
				}
			} else {
				this.world = null;
			}
		}

		return true;
	}

	private void setEntityLivingLocation(int x, int y, int z) {
		for (EntityLivingEntry entityLivingEntry : this.entityList) {
			entityLivingEntry.entity.setLocationAndAngles(x + 0.5f, y, z + 0.5f, 0.0f, 0.0f);
		}
	}

	@SuppressWarnings("null")
	private int getCanSpawnHere(int x, int y, int z) {
		Block block = this.world.func_147439_a(x, y - 1, z);
		if (block == null || block == Blocks.air || block.func_149688_o().isLiquid()) {
			return 0x00;
		}

		BiomeGenBase biome = this.world.getBiomeGenForCoords(x, z);

		Map<Class, EnumCreatureType> entityCreatureTypeMapping = null;
		if (!this.biomeCreatureSpawnMapping.containsKey(biome.biomeID)) {
			entityCreatureTypeMapping = new HashMap<Class, EnumCreatureType>();

			for (EnumCreatureType creatureType : EnumCreatureType.values()) {
				List<BiomeGenBase.SpawnListEntry> spawnableList = biome.getSpawnableList(creatureType);
				if (spawnableList != null) {
					for (BiomeGenBase.SpawnListEntry entry : spawnableList) {
						entityCreatureTypeMapping.put(entry.entityClass, creatureType);
					}
				}
			}

			this.biomeCreatureSpawnMapping.put(biome.biomeID, entityCreatureTypeMapping);
		}

		entityCreatureTypeMapping = this.biomeCreatureSpawnMapping.get(biome.biomeID);
		if (entityCreatureTypeMapping == null) {
			return 0x00;
		}

		Class key;
		EnumCreatureType value;
		int spawnType = 0, i;
		EntityLiving entity = null;

		for (Entry<Class, EnumCreatureType> entry : entityCreatureTypeMapping.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();

			for (EntityLivingEntry entityLivingEntry : this.entityList) {
				entity = entityLivingEntry.entity;

				if (entityLivingEntry.enabled && key.isInstance(entity)) {
					if (!key.equals(EntityOcelot.class) && !SpawnerAnimals.canCreatureTypeSpawnAtLocation(value, this.world, x, y, z)) {
						continue;
					}

					if (key.equals(EntitySquid.class) && y > 45 && y < 63) {
						return 0x03;
					}

					if (!this.world.isAnyLiquid(entity.boundingBox)) {
						if (this.world.func_147461_a(entity.boundingBox).isEmpty()) {
							if (key.equals(EntitySlime.class) && (y < 40 && isSlimeChunk(x >> 4, z >> 4) || y > 50 && y < 70 && biome.biomeID == BiomeGenBase.swampland.biomeID && getBlockLightLevel(x, y, z, 16) < 8)) {
								return 0x03;
							}

							if ((key.equals(EntityPigZombie.class) || key.equals(EntityGhast.class) || key.equals(EntityMagmaCube.class)) && this.world.difficultySetting != EnumDifficulty.PEACEFUL) {
								return 0x03;
							}

							if (key.equals(EntityOcelot.class) && y >= 64 && (block == Blocks.grass || block.isLeaves(this.world, x, y - 1, z))) {
								return 0x03;
							}

							if ((key.equals(EntityCreeper.class) || key.equals(EntityZombie.class) || key.equals(EntitySkeleton.class) || key.equals(EntitySpider.class) || key.equals(EntityEnderman.class)) && getBlockLightLevel(x, y, z, 16) < 8 && this.world.difficultySetting != EnumDifficulty.PEACEFUL) {
								spawnType |= 0x02;
							}

							Calendar calendar = this.world.getCurrentDate();
							if (key.equals(EntityBat.class) && entity.boundingBox.minY < 63 && getBlockLightLevel(x, y, z, 16) < ((calendar.get(Calendar.MONTH) + 1 != 10 || calendar.get(Calendar.DATE) < 20) && (calendar.get(Calendar.MONTH) + 1 != 11 || calendar.get(Calendar.DATE) > 3) ? 5 : 8)) {
								spawnType |= 0x02;
							}

							if ((key.equals(EntityChicken.class) || key.equals(EntityCow.class) || key.equals(EntityMooshroom.class) || key.equals(EntityPig.class) || key.equals(EntityWolf.class)) && block == Blocks.grass && getBlockLightLevel(x, y, z, 0) > 8) {
								spawnType |= 0x01;
							}
						}
					}
				}
			}
		}

		return spawnType;
	}

	private int getBlockLightLevel(int x, int y, int z, int kst) {
		return this.world.getChunkFromChunkCoords(x >> 4, z >> 4).getBlockLightValue(x & 0xF, y, z & 0xF, kst);
	}

	private boolean isSlimeChunk(int x, int z) {
		return this.hasSeed && ((new Random(this.seed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911)).nextInt(10) == 0);
	}
}
