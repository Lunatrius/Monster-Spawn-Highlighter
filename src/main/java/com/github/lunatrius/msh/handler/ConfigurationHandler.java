package com.github.lunatrius.msh.handler;

import com.github.lunatrius.msh.lib.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class ConfigurationHandler {
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
		propColorDayRed = configuration.get("color", "colorDayRed", colorDayRed, "Amount of red color (during the day).", COLOR_MIN, COLOR_MAX);
		propColorDayRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorDayRed"));
		colorDayRed = propColorDayRed.getInt(colorDayRed);

		propColorDayGreen = configuration.get("color", "colorDayGreen", colorDayGreen, "Amount of green color (during the day).", COLOR_MIN, COLOR_MAX);
		propColorDayGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorDayGreen"));
		colorDayGreen = propColorDayGreen.getInt(colorDayGreen);

		propColorDayBlue = configuration.get("color", "colorDayBlue", colorDayBlue, "Amount of blue color (during the day).", COLOR_MIN, COLOR_MAX);
		propColorDayBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorDayBlue"));
		colorDayBlue = propColorDayBlue.getInt(colorDayBlue);

		propColorNightRed = configuration.get("color", "colorNightRed", colorNightRed, "Amount of red color (during the night).", COLOR_MIN, COLOR_MAX);
		propColorNightRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorNightRed"));
		colorNightRed = propColorNightRed.getInt(colorNightRed);

		propColorNightGreen = configuration.get("color", "colorNightGreen", colorNightGreen, "Amount of green color (during the night).", COLOR_MIN, COLOR_MAX);
		propColorNightGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorNightGreen"));
		colorNightGreen = propColorNightGreen.getInt(colorNightGreen);

		propColorNightBlue = configuration.get("color", "colorNightBlue", colorNightBlue, "Amount of blue color (during the night).", COLOR_MIN, COLOR_MAX);
		propColorNightBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorNightBlue"));
		colorNightBlue = propColorNightBlue.getInt(colorNightBlue);

		propColorBothRed = configuration.get("color", "colorBothRed", colorBothRed, "Amount of red color (during day and night).", COLOR_MIN, COLOR_MAX);
		propColorBothRed.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorBothRed"));
		colorBothRed = propColorBothRed.getInt(colorBothRed);

		propColorBothGreen = configuration.get("color", "colorBothGreen", colorBothGreen, "Amount of green color (during day and night).", COLOR_MIN, COLOR_MAX);
		propColorBothGreen.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorBothGreen"));
		colorBothGreen = propColorBothGreen.getInt(colorBothGreen);

		propColorBothBlue = configuration.get("color", "colorBothBlue", colorBothBlue, "Amount of blue color (during day and night).", COLOR_MIN, COLOR_MAX);
		propColorBothBlue.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "colorBothBlue"));
		colorBothBlue = propColorBothBlue.getInt(colorBothBlue);

		propRenderRangeXZ = configuration.get("other", "renderRangeXZ", renderRangeXZ, "Amount of blocks that should be checked in X and Z directions ([2*range+1]^2 total).", 0, 30);
		propRenderRangeXZ.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "renderRangeXZ"));
		renderRangeXZ = propRenderRangeXZ.getInt(renderRangeXZ);

		propRenderRangeYBellow = configuration.get("other", "renderRangeYBellow", renderRangeYBellow, "Amount of blocks that should be checked bellow the player.", 0, 30);
		propRenderRangeYBellow.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "renderRangeYBellow"));
		renderRangeYBellow = propRenderRangeYBellow.getInt(renderRangeYBellow);

		propRenderRangeYAbove = configuration.get("other", "renderRangeYAbove", renderRangeYAbove, "Amount of blocks that should be checked above the player.", 0, 30);
		propRenderRangeYAbove.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "renderRangeYAbove"));
		renderRangeYAbove = propRenderRangeYAbove.getInt(renderRangeYAbove);

		propUpdateRate = configuration.get("other", "updateRate", updateRate, "Amount of ticks to wait before refreshing again.", 0, 30);
		propUpdateRate.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "updateRate"));
		updateRate = propUpdateRate.getInt(updateRate);

		propGuideLength = configuration.get("other", "guideLength", guideLength, "Length of the guide line (negative numbers invert the guide line).", -50.0, 50.0);
		propGuideLength.setLanguageKey(String.format("%s.%s", LANG_PREFIX, "guideLength"));
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
		return configuration.get("Entities", String.format("enabled%s", entityName), false, String.format("Enable spawn rendering of %s.", entityName));
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
