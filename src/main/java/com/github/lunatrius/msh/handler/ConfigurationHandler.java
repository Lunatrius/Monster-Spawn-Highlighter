package com.github.lunatrius.msh.handler;

import com.github.lunatrius.msh.lib.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class ConfigurationHandler {
	public static final String CATEGORY_COLOR = "color";
	public static final String CATEGORY_OTHER = "other";
	public static final String CATEGORY_ENTITIES = "Entities";

	public static final String COLOR_DAY_RED = "colorDayRed";
	public static final String COLOR_DAY_RED_DESC = "Amount of red color (during the day).";
	public static final String COLOR_DAY_GREEN = "colorDayGreen";
	public static final String COLOR_DAY_GREEN_DESC = "Amount of green color (during the day).";
	public static final String COLOR_DAY_BLUE = "colorDayBlue";
	public static final String COLOR_DAY_BLUE_DESC = "Amount of blue color (during the day).";

	public static final String COLOR_NIGHT_RED = "colorNightRed";
	public static final String COLOR_NIGHT_RED_DESC = "Amount of red color (during the night).";
	public static final String COLOR_NIGHT_GREEN = "colorNightGreen";
	public static final String COLOR_NIGHT_GREEN_DESC = "Amount of green color (during the night).";
	public static final String COLOR_NIGHT_BLUE = "colorNightBlue";
	public static final String COLOR_NIGHT_BLUE_DESC = "Amount of blue color (during the night).";

	public static final String COLOR_BOTH_RED = "colorBothRed";
	public static final String COLOR_BOTH_RED_DESC = "Amount of red color (during day and night).";
	public static final String COLOR_BOTH_GREEN = "colorBothGreen";
	public static final String COLOR_BOTH_GREEN_DESC = "Amount of green color (during day and night).";
	public static final String COLOR_BOTH_BLUE = "colorBothBlue";
	public static final String COLOR_BOTH_BLUE_DESC = "Amount of blue color (during day and night).";

	public static final String RENDER_RANGE_XZ = "renderRangeXZ";
	public static final String RENDER_RANGE_XZ_DESC = "Amount of blocks that should be checked in X and Z directions ([2*range+1]^2 total).";
	public static final String RENDER_RANGE_Y_BELLOW = "renderRangeYBellow";
	public static final String RENDER_RANGE_Y_BELLOW_DESC = "Amount of blocks that should be checked bellow the player.";
	public static final String RENDER_RANGE_Y_ABOVE = "renderRangeYAbove";
	public static final String RENDER_RANGE_Y_ABOVE_DESC = "Amount of blocks that should be checked above the player.";

	public static final String UPDATE_RATE = "updateRate";
	public static final String UPDATE_RATE_DESC = "Amount of ticks to wait before refreshing the layout again.";
	public static final String GUIDE_LENGTH = "guideLength";
	public static final String GUIDE_LENGTH_DESC = "Length of the guide line (negative numbers invert the guide line).";

	public static final String ENABLED_ENTITY = "enabled%s";
	public static final String ENABLED_ENTITY_DESC = "Enable spawn rendering of %s.";

	public static final String LANG_PREFIX = Reference.MODID.toLowerCase();

	public static final int COLOR_MIN = 0x00;
	public static final int COLOR_MAX = 0xFF;

	public static Configuration configuration;

	public static int colorDayRed = COLOR_MIN;
	public static int colorDayGreen = COLOR_MAX;
	public static int colorDayBlue = COLOR_MIN;
	public static int colorNightRed = COLOR_MIN;
	public static int colorNightGreen = COLOR_MIN;
	public static int colorNightBlue = COLOR_MAX;
	public static int colorBothRed = COLOR_MAX;
	public static int colorBothGreen = COLOR_MIN;
	public static int colorBothBlue = COLOR_MIN;
	public static int renderRangeXZ = 8;
	public static int renderRangeYBellow = 4;
	public static int renderRangeYAbove = 0;
	public static int updateRate = 4;
	public static float guideLength = 1.5f;
	public static int renderSpawns = 0;

	public static Property propColorDayRed;
	public static Property propColorDayGreen;
	public static Property propColorDayBlue;
	public static Property propColorNightRed;
	public static Property propColorNightGreen;
	public static Property propColorNightBlue;
	public static Property propColorBothRed;
	public static Property propColorBothGreen;
	public static Property propColorBothBlue;
	public static Property propRenderRangeXZ;
	public static Property propRenderRangeYBellow;
	public static Property propRenderRangeYAbove;
	public static Property propUpdateRate;
	public static Property propGuideLength;


	public static void init(File configFile) {
		if (configuration == null) {
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration() {
		propColorDayRed = configuration.get(CATEGORY_COLOR, COLOR_DAY_RED, colorDayRed, COLOR_DAY_RED_DESC, COLOR_MIN, COLOR_MAX);
		propColorDayRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_DAY_RED));
		colorDayRed = propColorDayRed.getInt(colorDayRed);

		propColorDayGreen = configuration.get(CATEGORY_COLOR, COLOR_DAY_GREEN, colorDayGreen, COLOR_DAY_GREEN_DESC, COLOR_MIN, COLOR_MAX);
		propColorDayGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_DAY_GREEN));
		colorDayGreen = propColorDayGreen.getInt(colorDayGreen);

		propColorDayBlue = configuration.get(CATEGORY_COLOR, COLOR_DAY_BLUE, colorDayBlue, COLOR_DAY_BLUE_DESC, COLOR_MIN, COLOR_MAX);
		propColorDayBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_DAY_BLUE));
		colorDayBlue = propColorDayBlue.getInt(colorDayBlue);

		propColorNightRed = configuration.get(CATEGORY_COLOR, COLOR_NIGHT_RED, colorNightRed, COLOR_NIGHT_RED_DESC, COLOR_MIN, COLOR_MAX);
		propColorNightRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_NIGHT_RED));
		colorNightRed = propColorNightRed.getInt(colorNightRed);

		propColorNightGreen = configuration.get(CATEGORY_COLOR, COLOR_NIGHT_GREEN, colorNightGreen, COLOR_NIGHT_GREEN_DESC, COLOR_MIN, COLOR_MAX);
		propColorNightGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_NIGHT_GREEN));
		colorNightGreen = propColorNightGreen.getInt(colorNightGreen);

		propColorNightBlue = configuration.get(CATEGORY_COLOR, COLOR_NIGHT_BLUE, colorNightBlue, COLOR_NIGHT_BLUE_DESC, COLOR_MIN, COLOR_MAX);
		propColorNightBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_NIGHT_BLUE));
		colorNightBlue = propColorNightBlue.getInt(colorNightBlue);

		propColorBothRed = configuration.get(CATEGORY_COLOR, COLOR_BOTH_RED, colorBothRed, COLOR_BOTH_RED_DESC, COLOR_MIN, COLOR_MAX);
		propColorBothRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_BOTH_RED));
		colorBothRed = propColorBothRed.getInt(colorBothRed);

		propColorBothGreen = configuration.get(CATEGORY_COLOR, COLOR_BOTH_GREEN, colorBothGreen, COLOR_BOTH_GREEN_DESC, COLOR_MIN, COLOR_MAX);
		propColorBothGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_BOTH_GREEN));
		colorBothGreen = propColorBothGreen.getInt(colorBothGreen);

		propColorBothBlue = configuration.get(CATEGORY_COLOR, COLOR_BOTH_BLUE, colorBothBlue, COLOR_BOTH_BLUE_DESC, COLOR_MIN, COLOR_MAX);
		propColorBothBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, COLOR_BOTH_BLUE));
		colorBothBlue = propColorBothBlue.getInt(colorBothBlue);

		propRenderRangeXZ = configuration.get(CATEGORY_OTHER, RENDER_RANGE_XZ, renderRangeXZ, RENDER_RANGE_XZ_DESC, 0, 30);
		propRenderRangeXZ.setLanguageKey(String.format("%s.%s", LANG_PREFIX, RENDER_RANGE_XZ));
		renderRangeXZ = propRenderRangeXZ.getInt(renderRangeXZ);

		propRenderRangeYBellow = configuration.get(CATEGORY_OTHER, RENDER_RANGE_Y_BELLOW, renderRangeYBellow, RENDER_RANGE_Y_BELLOW_DESC, 0, 30);
		propRenderRangeYBellow.setLanguageKey(String.format("%s.%s", LANG_PREFIX, RENDER_RANGE_Y_BELLOW));
		renderRangeYBellow = propRenderRangeYBellow.getInt(renderRangeYBellow);

		propRenderRangeYAbove = configuration.get(CATEGORY_OTHER, RENDER_RANGE_Y_ABOVE, renderRangeYAbove, RENDER_RANGE_Y_ABOVE_DESC, 0, 30);
		propRenderRangeYAbove.setLanguageKey(String.format("%s.%s", LANG_PREFIX, RENDER_RANGE_Y_ABOVE));
		renderRangeYAbove = propRenderRangeYAbove.getInt(renderRangeYAbove);

		propUpdateRate = configuration.get(CATEGORY_OTHER, UPDATE_RATE, updateRate, UPDATE_RATE_DESC, 0, 30);
		propUpdateRate.setLanguageKey(String.format("%s.%s", LANG_PREFIX, UPDATE_RATE));
		updateRate = propUpdateRate.getInt(updateRate);

		propGuideLength = configuration.get(CATEGORY_OTHER, GUIDE_LENGTH, guideLength, GUIDE_LENGTH_DESC, -50.0, 50.0);
		propGuideLength.setLanguageKey(String.format("%s.%s", LANG_PREFIX, GUIDE_LENGTH));
		guideLength = (float) propGuideLength.getDouble(guideLength);

		save();
	}

	public static void save() {
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static void glColorDay() {
		GL11.glColor4ub((byte) colorDayRed, (byte) colorDayGreen, (byte) colorDayBlue, (byte) 79);
	}

	public static void glColorNight() {
		GL11.glColor4ub((byte) colorNightRed, (byte) colorNightGreen, (byte) colorNightBlue, (byte) 79);
	}

	public static void glColorBoth() {
		GL11.glColor4ub((byte) colorBothRed, (byte) colorBothGreen, (byte) colorBothBlue, (byte) 79);
	}

	private static Property getEntityProperty(String entityName) {
		return configuration.get(CATEGORY_ENTITIES, String.format(ENABLED_ENTITY, entityName), false, String.format(ENABLED_ENTITY_DESC, entityName));
	}

	public static boolean isEntityEnabled(String entityName) {
		return getEntityProperty(entityName).getBoolean(false);
	}

	public static void setEntityEnabled(String entityName, boolean enabled) {
		Property property = getEntityProperty(entityName);
		property.set(enabled);
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(Reference.MODID)) {
			loadConfiguration();
		}
	}
}
