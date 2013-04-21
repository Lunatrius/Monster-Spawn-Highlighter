package lunatrius.msh;

import cpw.mods.fml.relauncher.ReflectionHelper;
import lunatrius.msh.util.Vector3f;
import lunatrius.msh.util.Vector4i;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraftforge.common.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
	private static final Settings instance = new Settings();

	public Configuration config = null;

	public float colorDayRed = 0.0f;
	public float colorDayGreen = 1.0f;
	public float colorDayBlue = 0.0f;
	public float colorNightRed = 0.0f;
	public float colorNightGreen = 0.0f;
	public float colorNightBlue = 1.0f;
	public float colorBothRed = 1.0f;
	public float colorBothGreen = 0.0f;
	public float colorBothBlue = 0.0f;
	public int renderRange = 8;
	public int updateRate = 4;
	public float guideLength = 1.5f;
	public int renderBlocks = 0;
	public long seed = 0;

	public Vector3f playerPosition = new Vector3f();
	public List<Vector4i> spawnList = new ArrayList<Vector4i>();
	public Map<Integer, Map<Class, EnumCreatureType>> biomeCreatureSpawnMapping = new HashMap<Integer, Map<Class, EnumCreatureType>>();

	public EntityLivingEntry creeper = new EntityLivingEntry("Creeper", new EntityCreeper(null), true);
	public EntityLivingEntry zombie = new EntityLivingEntry("Zombie", new EntityZombie(null), true);
	public EntityLivingEntry skeleton = new EntityLivingEntry("Skeleton", new EntitySkeleton(null), true);
	public EntityLivingEntry spider = new EntityLivingEntry("Spider", new EntitySpider(null), true);
	public EntityLivingEntry enderman = new EntityLivingEntry("Enderman", new EntityEnderman(null), true);
	public EntityLivingEntry slime = new EntityLivingEntry("Slime", new EntitySlime(null), true);
	public EntityLivingEntry pigZombie = new EntityLivingEntry("PigZombie", new EntityPigZombie(null), true);
	public EntityLivingEntry ghast = new EntityLivingEntry("Ghast", new EntityGhast(null), true);
	public EntityLivingEntry lavaSlime = new EntityLivingEntry("LavaSlime", new EntityMagmaCube(null), true);
	public EntityLivingEntry chicken = new EntityLivingEntry("Chicken", new EntityChicken(null), true);
	public EntityLivingEntry cow = new EntityLivingEntry("Cow", new EntityCow(null), true);
	public EntityLivingEntry mushroomCow = new EntityLivingEntry("MushroomCow", new EntityMooshroom(null), true);
	public EntityLivingEntry pig = new EntityLivingEntry("Pig", new EntityPig(null), true);
	public EntityLivingEntry sheep = new EntityLivingEntry("Sheep", new EntitySheep(null), true);
	public EntityLivingEntry squid = new EntityLivingEntry("Squid", new EntitySquid(null), true);
	public EntityLivingEntry wolf = new EntityLivingEntry("Wolf", new EntityWolf(null), true);
	public EntityLivingEntry ozelot = new EntityLivingEntry("Ozelot", new EntityOcelot(null), true);
	public EntityLivingEntry bat = new EntityLivingEntry("Bat", new EntityBat(null), true);

	public EntityLivingEntry[] entityLiving = new EntityLivingEntry[] {
			this.creeper,
			this.zombie,
			this.skeleton,
			this.spider,
			this.enderman,
			this.slime,
			this.pigZombie,
			this.ghast,
			this.lavaSlime,
			this.chicken,
			this.cow,
			this.mushroomCow,
			this.pig,
			this.sheep,
			this.squid,
			this.wolf,
			this.ozelot,
			this.bat
	};

	private Settings() {
		for (EntityLivingEntry entityLivingEntry : this.entityLiving) {
			if (entityLivingEntry.entity instanceof EntitySlime) {
				// ((EntitySlime) entityLivingEntry.entity).setSlimeSize(1);
				try {
					Method method = ReflectionHelper.findMethod(EntitySlime.class, (EntitySlime) entityLivingEntry.entity, new String[] {
							"func_70799_a",
							"a",
							"setSlimeSize"
					}, new Class[] {int.class});
					method.invoke(entityLivingEntry.entity, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Settings instance() {
		return instance;
	}
}
