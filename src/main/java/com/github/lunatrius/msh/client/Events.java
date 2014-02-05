package com.github.lunatrius.msh.client;

import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import com.github.lunatrius.msh.client.gui.GuiMonsterSpawnHighlighter;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

import static cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import static cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Events {
	private final Minecraft minecraft = Minecraft.getMinecraft();

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			MonsterSpawnHighlighter.instance.onTick();
		}
	}

	@SubscribeEvent
	public void keyInput(KeyInputEvent event) {
		if (MonsterSpawnHighlighter.instance.toggleKey.func_151468_f()) {
			if (this.minecraft.currentScreen == null) {
				this.minecraft.func_147108_a(new GuiMonsterSpawnHighlighter(null));
			}
		}
	}
}
