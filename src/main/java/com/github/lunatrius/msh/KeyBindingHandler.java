package com.github.lunatrius.msh;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.settings.KeyBinding;

import java.util.EnumSet;

public class KeyBindingHandler extends KeyBindingRegistry.KeyHandler {
	public KeyBindingHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "KB";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding keyBinding, boolean tickEnd, boolean isRepeat) {
		key(keyBinding, true);
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding keyBinding, boolean tickEnd) {
		key(keyBinding, false);
	}

	private void key(KeyBinding keyBinding, boolean down) {
		MonsterSpawnHighlighter.instance.keyboardEvent(keyBinding, down);
	}
}
