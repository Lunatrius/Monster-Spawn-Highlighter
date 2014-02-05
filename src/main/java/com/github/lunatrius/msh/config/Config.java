package com.github.lunatrius.msh.config;

import com.github.lunatrius.core.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class Config extends Configuration {
	public static final int COLOR_MIN = 0x00;
	public static final int COLOR_MAX = 0xFF;

	private final Property propColorDayRed;
	private final Property propColorDayGreen;
	private final Property propColorDayBlue;
	private final Property propColorNightRed;
	private final Property propColorNightGreen;
	private final Property propColorNightBlue;
	private final Property propColorBothRed;
	private final Property propColorBothGreen;
	private final Property propColorBothBlue;
	private final Property propRenderRangeXZ;
	private final Property propRenderRangeYBellow;
	private final Property propRenderRangeYAbove;
	private final Property propUpdateRate;
	private final Property propGuideLength;

	public int colorDayRed = COLOR_MIN;
	public int colorDayGreen = COLOR_MAX;
	public int colorDayBlue = COLOR_MIN;
	public int colorNightRed = COLOR_MIN;
	public int colorNightGreen = COLOR_MIN;
	public int colorNightBlue = COLOR_MAX;
	public int colorBothRed = COLOR_MAX;
	public int colorBothGreen = COLOR_MIN;
	public int colorBothBlue = COLOR_MIN;
	public int renderRangeXZ = 8;
	public int renderRangeYBellow = 4;
	public int renderRangeYAbove = 0;
	public int updateRate = 4;
	public float guideLength = 1.5f;
	public int renderSpawns = 0;

	public Config(File file) {
		super(file);

		String categoryColor = "color";
		String categoryOther = "other";

		this.propColorDayRed = get(categoryColor, "colorDayRed", this.colorDayRed, COLOR_MIN, COLOR_MAX, "Amount of red color (during the day).");
		this.propColorDayGreen = get(categoryColor, "colorDayGreen", this.colorDayGreen, COLOR_MIN, COLOR_MAX, "Amount of green color (during the day).");
		this.propColorDayBlue = get(categoryColor, "colorDayBlue", this.colorDayBlue, COLOR_MIN, COLOR_MAX, "Amount of blue color (during the day).");
		this.propColorNightRed = get(categoryColor, "colorNightRed", this.colorNightRed, COLOR_MIN, COLOR_MAX, "Amount of red color (during the night).");
		this.propColorNightGreen = get(categoryColor, "colorNightGreen", this.colorNightGreen, COLOR_MIN, COLOR_MAX, "Amount of green color (during the night).");
		this.propColorNightBlue = get(categoryColor, "colorNightBlue", this.colorNightBlue, COLOR_MIN, COLOR_MAX, "Amount of blue color (during the night).");
		this.propColorBothRed = get(categoryColor, "colorBothRed", this.colorBothRed, COLOR_MIN, COLOR_MAX, "Amount of red color (during the night).");
		this.propColorBothGreen = get(categoryColor, "colorBothGreen", this.colorBothGreen, COLOR_MIN, COLOR_MAX, "Amount of green color (during the night).");
		this.propColorBothBlue = get(categoryColor, "colorBothBlue", this.colorBothBlue, COLOR_MIN, COLOR_MAX, "Amount of blue color (during the night).");
		this.propRenderRangeXZ = get(categoryOther, "renderRangeXZ", this.renderRangeXZ, 1, 32, "Amount of blocks that should be checked in X and Z directions ([2*range+1]^2 total).");
		this.propRenderRangeYBellow = get(categoryOther, "renderRangeYBellow", this.renderRangeYBellow, 1, 32, "Amount of blocks that should be checked bellow the player.");
		this.propRenderRangeYAbove = get(categoryOther, "renderRangeYAbove", this.renderRangeYAbove, 1, 32, "Amount of blocks that should be checked above the player.");
		this.propUpdateRate = get(categoryOther, "updateRate", this.updateRate, 1, 30, "Amount of ticks to wait before refreshing again.");
		this.propGuideLength = get(categoryOther, "guideLength", this.guideLength, -50.0f, 50.0f, "Length of the guide line (negative numbers invert the guide line).");

		this.colorDayRed = this.propColorDayRed.getInt(this.colorDayRed);
		this.colorDayGreen = this.propColorDayGreen.getInt(this.colorDayGreen);
		this.colorDayBlue = this.propColorDayBlue.getInt(this.colorDayBlue);
		this.colorNightRed = this.propColorNightRed.getInt(this.colorNightRed);
		this.colorNightGreen = this.propColorNightGreen.getInt(this.colorNightGreen);
		this.colorNightBlue = this.propColorNightBlue.getInt(this.colorNightBlue);
		this.colorBothRed = this.propColorBothRed.getInt(this.colorBothRed);
		this.colorBothGreen = this.propColorBothGreen.getInt(this.colorBothGreen);
		this.colorBothBlue = this.propColorBothBlue.getInt(this.colorBothBlue);
		this.renderRangeXZ = this.propRenderRangeXZ.getInt(this.renderRangeXZ);
		this.renderRangeYBellow = this.propRenderRangeYBellow.getInt(this.renderRangeYBellow);
		this.renderRangeYAbove = this.propRenderRangeYAbove.getInt(this.renderRangeYAbove);
		this.updateRate = this.propUpdateRate.getInt(this.updateRate);
		this.guideLength = (float) this.propGuideLength.getDouble(this.guideLength);
	}

	public void glColorDay() {
		GL11.glColor4ub((byte) this.colorDayRed, (byte) this.colorDayGreen, (byte) this.colorDayBlue, (byte) 79);
	}

	public void glColorNight() {
		GL11.glColor4ub((byte) this.colorNightRed, (byte) this.colorNightGreen, (byte) this.colorNightBlue, (byte) 79);
	}

	public void glColorBoth() {
		GL11.glColor4ub((byte) this.colorBothRed, (byte) this.colorBothGreen, (byte) this.colorBothBlue, (byte) 79);
	}

	private Property getEntityProperty(String entityName) {
		return get("Entities", String.format("enabled%s", entityName), false, String.format("Enable spawn rendering of %s.", entityName));
	}

	public boolean isEntityEnabled(String entityName) {
		return getEntityProperty(entityName).getBoolean(false);
	}

	public void setEntityEnabled(String entityName, boolean enabled) {
		Property property = getEntityProperty(entityName);
		property.set(enabled);
	}
}
