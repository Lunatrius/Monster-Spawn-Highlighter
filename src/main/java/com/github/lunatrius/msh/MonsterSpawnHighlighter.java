package com.github.lunatrius.msh;

import com.github.lunatrius.core.version.VersionChecker;
import com.github.lunatrius.msh.entity.SpawnCondition;
import com.github.lunatrius.msh.handler.ConfigurationHandler;
import com.github.lunatrius.msh.lib.Reference;
import com.github.lunatrius.msh.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, guiFactory = Reference.GUI_FACTORY)
public class MonsterSpawnHighlighter {
	@Instance(Reference.MODID)
	public static MonsterSpawnHighlighter instance;

	@SidedProxy(serverSide = Reference.PROXY_COMMON, clientSide = Reference.PROXY_CLIENT)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		VersionChecker.registerMod(event.getModMetadata(), Reference.FORGE);

		Reference.logger = event.getModLog();

		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		proxy.setConfigEntryClasses();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerEvents();
		proxy.registerKeybindings();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		SpawnCondition.populateData();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		try {
			Reference.hasSeed = true;
			Reference.seed = event.getServer().worldServerForDimension(0).getSeed();
		} catch (Exception e) {
			Reference.hasSeed = false;
		}
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		Reference.hasSeed = false;
	}
}
