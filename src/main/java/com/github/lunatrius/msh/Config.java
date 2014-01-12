package com.github.lunatrius.msh;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class Config extends Configuration {
	private float colorDayRed = 0.0f;
	private float colorDayGreen = 1.0f;
	private float colorDayBlue = 0.0f;
	private float colorNightRed = 0.0f;
	private float colorNightGreen = 0.0f;
	private float colorNightBlue = 1.0f;
	private float colorBothRed = 1.0f;
	private float colorBothGreen = 0.0f;
	private float colorBothBlue = 0.0f;
	protected int renderRangeXZ = 8;
	protected int renderRangeYBellow = 4;
	protected int renderRangeYAbove = 0;
	protected int updateRate = 4;
	public float guideLength = 1.5f;
	public int renderSpawns = 0;

	public Config(File file) {
		super(file);
	}

	@Override
	public void load() {
		super.load();
		String categoryColor = "color";
		String categoryOther = "other";
		this.colorDayRed = getInt(categoryColor, "colorDayRed", (int) (this.colorDayRed * 255), 0, 255, "Amount of red color (during the day).") / 255.0f;
		this.colorDayGreen = getInt(categoryColor, "colorDayGreen", (int) (this.colorDayGreen * 255), 0, 255, "Amount of green color (during the day).") / 255.0f;
		this.colorDayBlue = getInt(categoryColor, "colorDayBlue", (int) (this.colorDayBlue * 255), 0, 255, "Amount of blue color (during the day).") / 255.0f;
		this.colorNightRed = getInt(categoryColor, "colorNightRed", (int) (this.colorNightRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.colorNightGreen = getInt(categoryColor, "colorNightGreen", (int) (this.colorNightGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.colorNightBlue = getInt(categoryColor, "colorNightBlue", (int) (this.colorNightBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.colorBothRed = getInt(categoryColor, "colorBothRed", (int) (this.colorBothRed * 255), 0, 255, "Amount of red color (during the night).") / 255.0f;
		this.colorBothGreen = getInt(categoryColor, "colorBothGreen", (int) (this.colorBothGreen * 255), 0, 255, "Amount of green color (during the night).") / 255.0f;
		this.colorBothBlue = getInt(categoryColor, "colorBothBlue", (int) (this.colorBothBlue * 255), 0, 255, "Amount of blue color (during the night).") / 255.0f;
		this.renderRangeXZ = getInt(categoryOther, "renderRangeXZ", this.renderRangeXZ, 1, 32, "Amount of blocks that should be checked in X and Z directions ([2*range+1]^2 total).");
		this.renderRangeYBellow = getInt(categoryOther, "renderRangeYBellow", this.renderRangeYBellow, 1, 32, "Amount of blocks that should be checked bellow the player.");
		this.renderRangeYAbove = getInt(categoryOther, "renderRangeYAbove", this.renderRangeYAbove, 1, 32, "Amount of blocks that should be checked above the player.");
		this.updateRate = getInt(categoryOther, "updateRate", this.updateRate, 1, 30, "Amount of ticks to wait before refreshing again.");
		this.guideLength = (float) getDouble(categoryOther, "guideLength", this.guideLength, -50.0f, 50.0f, "Length of the guide line (negative numbers invert the guide line).");
	}

	private int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		Property property = get(category, key, Integer.toString(defaultValue));
		property.comment = String.format("%1$s [range: %2$s ~ %3$s, default: %4$s]", comment, minValue, maxValue, defaultValue);
		int value = property.getInt(defaultValue);
		property.set(value < minValue ? minValue : (value > maxValue ? maxValue : value));
		return property.getInt(defaultValue);
	}

	private double getDouble(String category, String key, double defaultValue, double minValue, double maxValue, String comment) {
		Property property = get(category, key, defaultValue);
		property.comment = String.format("%1$s [range: %2$s ~ %3$s, default: %4$s]", comment, minValue, maxValue, defaultValue);
		double value = property.getDouble(defaultValue);
		property.set(value < minValue ? minValue : (value > maxValue ? maxValue : value));
		return property.getDouble(defaultValue);
	}

	public Property getBooleanProperty(String category, String key, boolean defaultValue, String comment) {
		Property property = get(category, key, defaultValue);
		property.comment = String.format("%1$s [default: %2$s]", comment, defaultValue);
		return property;
	}

	public void glColorDay() {
		GL11.glColor4f(this.colorDayRed, this.colorDayGreen, this.colorDayBlue, 0.3f);
	}

	public void glColorNight() {
		GL11.glColor4f(this.colorNightRed, this.colorNightGreen, this.colorNightBlue, 0.3f);
	}

	public void glColorBoth() {
		GL11.glColor4f(this.colorBothRed, this.colorBothGreen, this.colorBothBlue, 0.3f);
	}

	private Property getEntityProperty(String entityName) {
		return getBooleanProperty("Entities", String.format("enabled%s", entityName), true, String.format("Enable spawn rendering of %s.", entityName));
	}

	public boolean isEntityEnabled(String entityName) {
		return getEntityProperty(entityName).getBoolean(true);
	}

	public void setEntityEnabled(String entityName, boolean enabled) {
		Property property = getEntityProperty(entityName);
		property.set(enabled);
	}
}
