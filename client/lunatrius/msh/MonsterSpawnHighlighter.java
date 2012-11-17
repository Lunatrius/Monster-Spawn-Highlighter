package lunatrius.msh;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import lunatrius.msh.util.Config;
import lunatrius.msh.util.Vector4i;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.SpawnerAnimals;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(modid = "MonsterSpawnHighlighter")
public class MonsterSpawnHighlighter {
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private World world = null;
	private final KeyBinding toggleKey = new KeyBinding("msh.toggle", Keyboard.KEY_L);
	private final Settings settings = Settings.instance();
	private int ticks = -1;

	@Instance("MonsterSpawnHighlighter")
	public static MonsterSpawnHighlighter instance;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = this.settings.config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		this.settings.colorDayRed = Config.getInt(config, "colorDayRed", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorDayRed * 255), 0, 255, "Amount of red color (during the day).") / 255.0f;
		this.settings.colorDayGreen = Config.getInt(config, "colorDayGreen", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorDayGreen * 255), 0, 255, "Amount of green color (during the day).") / 255.0f;
		this.settings.colorDayBlue = Config.getInt(config, "colorDayBlue", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorDayBlue * 255), 0, 255, "Amount of blue color (during the day).") / 255.0f;
		this.settings.colorNightRed = Config.getInt(config, "colorNightRed", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorNightRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.settings.colorNightGreen = Config.getInt(config, "colorNightGreen", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorNightGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.settings.colorNightBlue = Config.getInt(config, "colorNightBlue", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorNightBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.settings.colorBothRed = Config.getInt(config, "colorBothRed", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorBothRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.settings.colorBothGreen = Config.getInt(config, "colorBothGreen", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorBothGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.settings.colorBothBlue = Config.getInt(config, "colorBothBlue", Configuration.CATEGORY_GENERAL, (int) (this.settings.colorBothBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.settings.renderRange = Config.getInt(config, "renderRange", Configuration.CATEGORY_GENERAL, this.settings.renderRange, 1, 50, "Amount of blocks that should be checked in each direction ([2*range+1]^3 total)");
		this.settings.updateRate = Config.getInt(config, "updateRate", Configuration.CATEGORY_GENERAL, this.settings.updateRate, 1, 30, "Amount of ticks to wait before refreshing again.");
		this.settings.guideLength = Config.getFloat(config, "guideLength", Configuration.CATEGORY_GENERAL, this.settings.guideLength, -50.0f, 50.0f, "Length of the guide line (negative numbers invert the guide line).");
		for (int i = 0; i < this.settings.entityLiving.length; i++) {
			this.settings.entityLiving[i].enabled = Config.getBoolean(config, "enabled" + this.settings.entityLiving[i].name, Configuration.CATEGORY_GENERAL, this.settings.entityLiving[i].enabled, "Enable spawn rendering of " + this.settings.entityLiving[i].name + ".");
		}
		config.save();
	}

	@Init
	public void init(FMLInitializationEvent event) {
		try {
			MinecraftForge.EVENT_BUS.register(new Render(this.minecraft));

			KeyBindingRegistry.registerKeyBinding(new KeyBindingHandler(new KeyBinding[] {
				this.toggleKey
			}, new boolean[] {
				false
			}));

			LanguageRegistry.instance().addStringLocalization("msh.toggle", "Toggle Monster Spawns");

			TickRegistry.registerTickHandler(new Ticker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@ServerStarting
	public void serverStarting(FMLServerStartingEvent event) {
		try {
			this.settings.seed = event.getServer().worldServers[0].getSeed();
		} catch (Exception e) {
			this.settings.seed = 0;
		}
	}

	@ServerStopping
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

			this.settings.spawnList.clear();
			if (this.minecraft != null && this.minecraft.currentScreen == null && this.settings.renderBlocks != 0) {
				this.world = this.minecraft.theWorld;

				int lowX, lowY, lowZ, highX, highY, highZ, x, y, z, type;

				lowX = (int) (Math.floor(this.settings.playerPosition.x) - this.settings.renderRange);
				highX = (int) (Math.floor(this.settings.playerPosition.x) + this.settings.renderRange);
				lowY = (int) (Math.floor(this.settings.playerPosition.y) - this.settings.renderRange);
				highY = (int) (Math.floor(this.settings.playerPosition.y) + this.settings.renderRange);
				lowZ = (int) (Math.floor(this.settings.playerPosition.z) - this.settings.renderRange);
				highZ = (int) (Math.floor(this.settings.playerPosition.z) + this.settings.renderRange);

				for (y = lowY; y <= highY; y++) {
					for (x = lowX; x <= highX; x++) {
						for (z = lowZ; z <= highZ; z++) {
							setEntityLivingLocation(x, y, z);

							type = 0;
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

		Map<String, EnumCreatureType> entityCreatureTypeMapping = null;
		if (!this.settings.biomeCreatureSpawnMapping.containsKey(biome.biomeID)) {
			entityCreatureTypeMapping = new HashMap<String, EnumCreatureType>();

			for (EnumCreatureType creatureType : EnumCreatureType.values()) {
				List<SpawnListEntry> spawnableList = biome.getSpawnableList(creatureType);
				if (spawnableList != null) {
					for (SpawnListEntry entry : spawnableList) {
						entityCreatureTypeMapping.put(entry.entityClass.getSimpleName(), creatureType);
					}
				}
			}

			this.settings.biomeCreatureSpawnMapping.put(biome.biomeID, entityCreatureTypeMapping);
		}

		entityCreatureTypeMapping = this.settings.biomeCreatureSpawnMapping.get(biome.biomeID);
		if (entityCreatureTypeMapping == null) {
			return 0x00;
		}

		String key;
		EnumCreatureType value;
		int spawnType = 0, i;
		EntityLiving entity = null;

		for (Entry<String, EnumCreatureType> entry : entityCreatureTypeMapping.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();

			for (i = 0; i < this.settings.entityLiving.length; i++) {
				entity = this.settings.entityLiving[i].entity;

				if (this.settings.entityLiving[i].enabled && key.equals(entity.getClass().getSimpleName())) {
					if (!SpawnerAnimals.canCreatureTypeSpawnAtLocation(value, this.world, x, y, z) && !key.equals(this.settings.ozelot.nameObf)) {
						continue;
					}

					if (!this.world.isAnyLiquid(entity.boundingBox)) {
						if (this.world.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty()) {
							if ((key.equals(this.settings.creeper.nameObf) || key.equals(this.settings.zombie.nameObf) || key.equals(this.settings.skeleton.nameObf) || key.equals(this.settings.spider.nameObf) || key.equals(this.settings.enderman.nameObf)) && getBlockLightLevel(x, y, z, 16) < 8) {
								spawnType |= 0x02;
							}

							if (key.equals(this.settings.bat.nameObf) && entity.boundingBox.minY < 63 && getBlockLightLevel(x, y, z, 16) <= 7) {
								spawnType |= 0x02;
							}

							if (key.equals(this.settings.slime.nameObf) && (isSlimeChunk(x >> 4, z >> 4) && y < 40 || biome.biomeID == BiomeGenBase.swampland.biomeID)) {
								return 0x03;
							}

							if (key.equals(this.settings.pigZombie.nameObf) || key.equals(this.settings.ghast.nameObf) || key.equals(this.settings.lavaSlime.nameObf)) {
								return 0x03;
							}

							if ((key.equals(this.settings.chicken.nameObf) || key.equals(this.settings.cow.nameObf) || key.equals(this.settings.mushroomCow.nameObf) || key.equals(this.settings.pig.nameObf) || key.equals(this.settings.sheep.nameObf) || key.equals(this.settings.wolf.nameObf)) && blockID == Block.grass.blockID && getBlockLightLevel(x, y, z, 0) > 8) {
								spawnType |= 0x01;
							}

							if (key.equals(this.settings.ozelot.nameObf) && y >= 64 && (blockID == Block.grass.blockID || blockID == Block.leaves.blockID)) {
								return 0x03;
							}
						}
					}

					if (key.equals(this.settings.squid.nameObf) && y > 45 && y < 63) {
						return 0x03;
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
		return this.settings.seed != 0 ? (new Random(this.settings.seed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911)).nextInt(10) == 0 : false;
	}
}
