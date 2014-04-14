package com.github.lunatrius.msh.client;

import com.github.lunatrius.msh.CommonProxy;
import com.github.lunatrius.msh.client.gui.TextureInformation;
import com.github.lunatrius.msh.client.renderer.Renderer;
import com.github.lunatrius.msh.lib.Reference;
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
