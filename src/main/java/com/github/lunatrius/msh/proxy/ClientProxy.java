package com.github.lunatrius.msh.proxy;

import com.github.lunatrius.msh.client.Events;
import com.github.lunatrius.msh.client.gui.TextureInformation;
import com.github.lunatrius.msh.client.renderer.Renderer;
import com.github.lunatrius.msh.handler.ConfigurationHandler;
import com.github.lunatrius.msh.lib.Reference;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends CommonProxy {
	public static final Map<Class<? extends EntityLiving>, TextureInformation> ENTITY_ICONS = new HashMap<Class<? extends EntityLiving>, TextureInformation>();

	@Override
	public void setConfigEntryClasses() {
		ConfigurationHandler.propColorDayRed.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorDayGreen.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorDayBlue.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorNightRed.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorNightGreen.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorNightBlue.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorBothRed.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorBothGreen.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propColorBothBlue.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propRenderRangeXZ.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propRenderRangeYBellow.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propRenderRangeYAbove.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propUpdateRate.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propGuideLength.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
	}

	@Override
	public void registerKeybindings() {
		ClientRegistry.registerKeyBinding(Events.TOGGLE_KEY);
	}

	@Override
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new Renderer(Minecraft.getMinecraft()));

		FMLCommonHandler.instance().bus().register(new Events());
	}

	@Override
	public void constructTextureInformation(EntityLiving entityLiving) {
		try {
			TextureInformation textureInformation = new TextureInformation(entityLiving);
			ENTITY_ICONS.put(entityLiving.getClass(), textureInformation);
		} catch (Exception e) {
			Reference.logger.debug("Failed to construct TextureInformation!", e);
		}
	}
}
