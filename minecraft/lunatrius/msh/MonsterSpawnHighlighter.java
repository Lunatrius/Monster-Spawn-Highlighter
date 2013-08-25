package lunatrius.msh;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import lunatrius.msh.util.Config;
import lunatrius.msh.util.Vector4i;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.*;
import java.util.Map.Entry;

@Mod(modid = "MonsterSpawnHighlighter")
public class MonsterSpawnHighlighter {
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private World world = null;
	private final KeyBinding toggleKey = new KeyBinding("msh.toggle", Keyboard.KEY_L);
	private final Settings settings = Settings.instance();
	private final Frustrum frustrum = new Frustrum();
	private final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	private int ticks = -1;

	@Instance("MonsterSpawnHighlighter")
	public static MonsterSpawnHighlighter instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = this.settings.config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		this.settings.colorDayRed = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorDayRed", (int) (this.settings.colorDayRed * 255), 0, 255, "Amount of red color (during the day).") / 255.0f;
		this.settings.colorDayGreen = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorDayGreen", (int) (this.settings.colorDayGreen * 255), 0, 255, "Amount of green color (during the day).") / 255.0f;
		this.settings.colorDayBlue = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorDayBlue", (int) (this.settings.colorDayBlue * 255), 0, 255, "Amount of blue color (during the day).") / 255.0f;
		this.settings.colorNightRed = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorNightRed", (int) (this.settings.colorNightRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.settings.colorNightGreen = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorNightGreen", (int) (this.settings.colorNightGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.settings.colorNightBlue = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorNightBlue", (int) (this.settings.colorNightBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.settings.colorBothRed = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorBothRed", (int) (this.settings.colorBothRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.settings.colorBothGreen = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorBothGreen", (int) (this.settings.colorBothGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.settings.colorBothBlue = Config.getInt(config, Configuration.CATEGORY_GENERAL, "colorBothBlue", (int) (this.settings.colorBothBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.settings.renderRangeXZ = Config.getInt(config, Configuration.CATEGORY_GENERAL, "renderRangeXZ", this.settings.renderRangeXZ, 1, 32, "Amount of blocks that should be checked in X and Z directions ([2*range+1]^2 total).");
		this.settings.renderRangeYBellow = Config.getInt(config, Configuration.CATEGORY_GENERAL, "renderRangeYBellow", this.settings.renderRangeYBellow, 1, 32, "Amount of blocks that should be checked bellow the player.");
		this.settings.renderRangeYAbove = Config.getInt(config, Configuration.CATEGORY_GENERAL, "renderRangeYAbove", this.settings.renderRangeYAbove, 1, 32, "Amount of blocks that should be checked above the player.");
		this.settings.updateRate = Config.getInt(config, Configuration.CATEGORY_GENERAL, "updateRate", this.settings.updateRate, 1, 30, "Amount of ticks to wait before refreshing again.");
		this.settings.guideLength = (float) Config.getDouble(config, Configuration.CATEGORY_GENERAL, "guideLength", this.settings.guideLength, -50.0f, 50.0f, "Length of the guide line (negative numbers invert the guide line).");
		for (int i = 0; i < this.settings.entityLiving.length; i++) {
			this.settings.entityLiving[i].enabled = Config.getBoolean(config, Configuration.CATEGORY_GENERAL, "enabled" + this.settings.entityLiving[i].name, this.settings.entityLiving[i].enabled, "Enable spawn rendering of " + this.settings.entityLiving[i].name + ".");
		}
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		try {
			MinecraftForge.EVENT_BUS.register(new Render(this.minecraft));

			KeyBindingRegistry.registerKeyBinding(new KeyBindingHandler(new KeyBinding[] { this.toggleKey }, new boolean[] {
					false
			}));

			LanguageRegistry.instance().addStringLocalization("msh.toggle", "Toggle Monster Spawns");

			TickRegistry.registerTickHandler(new Ticker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		try {
			this.settings.seed = event.getServer().worldServers[0].getSeed();
		} catch (Exception e) {
			this.settings.seed = 0;
		}
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		this.settings.seed = 0;
	}

	public void keyboardEvent(KeyBinding keyBinding, boolean down) {
		if (down && this.minecraft.currentScreen == null && keyBinding == this.toggleKey) {
			this.minecraft.displayGuiScreen(new GuiMonsterSpawnHighlighter());
		}
	}

	public boolean onTick(TickType tickType, boolean start) {
		if (start) {
			return true;
		}

		if (--this.ticks < 0) {
			this.ticks = this.settings.updateRate;

			if (this.minecraft != null && this.minecraft.theWorld != null && this.settings.renderBlocks != 0) {
				this.settings.spawnList.clear();

				this.frustrum.setPosition(this.settings.playerPosition.x, this.settings.playerPosition.y, this.settings.playerPosition.z);

				this.world = this.minecraft.theWorld;

				int lowX, lowY, lowZ, highX, highY, highZ, x, y, z, type;

				lowX = (int) (Math.floor(this.settings.playerPosition.x) - this.settings.renderRangeXZ);
				highX = (int) (Math.floor(this.settings.playerPosition.x) + this.settings.renderRangeXZ);
				lowY = (int) (Math.floor(this.settings.playerPosition.y) - this.settings.renderRangeYBellow);
				highY = (int) (Math.floor(this.settings.playerPosition.y) + this.settings.renderRangeYAbove);
				lowZ = (int) (Math.floor(this.settings.playerPosition.z) - this.settings.renderRangeXZ);
				highZ = (int) (Math.floor(this.settings.playerPosition.z) + this.settings.renderRangeXZ);

				for (y = lowY; y <= highY; y++) {
					for (x = lowX; x <= highX; x++) {
						for (z = lowZ; z <= highZ; z++) {
							if (!this.frustrum.isBoundingBoxInFrustum(this.boundingBox.setBounds(x, y, z, x + 1, y + 1, z + 1))) {
								continue;
							}

							setEntityLivingLocation(x, y, z);

							if ((type = getCanSpawnHere(x, y, z)) > 0) {
								this.settings.spawnList.add(new Vector4i(x, y, z, type));
							}
						}
					}
				}
			}
		}

		return true;
	}

	private void setEntityLivingLocation(int x, int y, int z) {
		for (int i = 0; i < this.settings.entityLiving.length; i++) {
			this.settings.entityLiving[i].entity.setLocationAndAngles(x + 0.5f, y, z + 0.5f, 0.0f, 0.0f);
		}
	}

	@SuppressWarnings("null")
	private int getCanSpawnHere(int x, int y, int z) {
		int blockID = this.world.getBlockId(x, y - 1, z);
		if (blockID == 0) {
			return 0x00;
		}

		Block block = Block.blocksList[blockID];
		if (block == null || block != null && block.blockMaterial.isLiquid()) {
			return 0x00;
		}

		BiomeGenBase biome = this.world.getBiomeGenForCoords(x, z);

		Map<Class, EnumCreatureType> entityCreatureTypeMapping = null;
		if (!this.settings.biomeCreatureSpawnMapping.containsKey(biome.biomeID)) {
			entityCreatureTypeMapping = new HashMap<Class, EnumCreatureType>();

			for (EnumCreatureType creatureType : EnumCreatureType.values()) {
				List<SpawnListEntry> spawnableList = biome.getSpawnableList(creatureType);
				if (spawnableList != null) {
					for (SpawnListEntry entry : spawnableList) {
						entityCreatureTypeMapping.put(entry.entityClass, creatureType);
					}
				}
			}

			this.settings.biomeCreatureSpawnMapping.put(biome.biomeID, entityCreatureTypeMapping);
		}

		entityCreatureTypeMapping = this.settings.biomeCreatureSpawnMapping.get(biome.biomeID);
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

			for (i = 0; i < this.settings.entityLiving.length; i++) {
				entity = this.settings.entityLiving[i].entity;

				if (this.settings.entityLiving[i].enabled && key.isInstance(entity)) {
					if (!key.equals(EntityOcelot.class) && !SpawnerAnimals.canCreatureTypeSpawnAtLocation(value, this.world, x, y, z)) {
						continue;
					}

					if (key.equals(EntitySquid.class) && y > 45 && y < 63) {
						return 0x03;
					}

					if (!this.world.isAnyLiquid(entity.boundingBox)) {
						if (this.world.getCollidingBlockBounds(entity.boundingBox).isEmpty()) {
							if (key.equals(EntitySlime.class) && (y < 40 && isSlimeChunk(x >> 4, z >> 4) || y > 50 && y < 70 && biome.biomeID == BiomeGenBase.swampland.biomeID && getBlockLightLevel(x, y, z, 16) < 8)) {
								return 0x03;
							}

							if ((key.equals(EntityPigZombie.class) || key.equals(EntityGhast.class) || key.equals(EntityMagmaCube.class)) && this.world.difficultySetting > 0) {
								return 0x03;
							}

							if (key.equals(EntityOcelot.class) && y >= 64 && (blockID == Block.grass.blockID || block.isLeaves(this.world, x, y - 1, z))) {
								return 0x03;
							}

							if ((key.equals(EntityCreeper.class) || key.equals(EntityZombie.class) || key.equals(EntitySkeleton.class) || key.equals(EntitySpider.class) || key.equals(EntityEnderman.class)) && getBlockLightLevel(x, y, z, 16) < 8 && this.world.difficultySetting > 0) {
								spawnType |= 0x02;
							}

							Calendar calendar = this.world.getCurrentDate();
							if (key.equals(EntityBat.class) && entity.boundingBox.minY < 63 && getBlockLightLevel(x, y, z, 16) < ((calendar.get(Calendar.MONTH) + 1 != 10 || calendar.get(Calendar.DATE) < 20) && (calendar.get(Calendar.MONTH) + 1 != 11 || calendar.get(Calendar.DATE) > 3) ? 5 : 8)) {
								spawnType |= 0x02;
							}

							if ((key.equals(EntityChicken.class) || key.equals(EntityCow.class) || key.equals(EntityMooshroom.class) || key.equals(EntityPig.class) || key.equals(EntityWolf.class)) && blockID == Block.grass.blockID && getBlockLightLevel(x, y, z, 0) > 8) {
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
		return (this.settings.seed != 0) && ((new Random(this.settings.seed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911)).nextInt(10) == 0);
	}
}
