package com.github.lunatrius.msh.client.gui;

import com.github.lunatrius.msh.entity.SpawnCondition;
import com.github.lunatrius.msh.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.Collections;
import java.util.Comparator;

public class GuiMonsterSpawnHighlighter extends GuiScreen {
	private final GuiScreen prevGuiScreen;

	private GuiMonsterSpawnHighlighterSlot guiMonsterSpawnHighlighterSlot;

	private GuiButton btnToggle = null;
	private GuiButton btnDone = null;

	private final String strTitle = I18n.format("monsterspawnhighlighter.gui.title");
	private final String strDone = I18n.format("monsterspawnhighlighter.gui.done");
	private final String strDisabled = I18n.format("monsterspawnhighlighter.gui.disabled");
	private final String strEnabled = I18n.format("monsterspawnhighlighter.gui.enabled");
	private final String strGuide = I18n.format("monsterspawnhighlighter.gui.guide");

	public GuiMonsterSpawnHighlighter(GuiScreen guiScreen) {
		this.prevGuiScreen = guiScreen;
	}

	@Override
	public void initGui() {
		int id = 0;

		this.btnToggle = new GuiButton(id++, this.width / 2 - 154, this.height - 26, 150, 20, "N/A");
		this.buttonList.add(this.btnToggle);
		setToggleText();

		this.btnDone = new GuiButton(id++, this.width / 2 + 4, this.height - 26, 150, 20, this.strDone);
		this.buttonList.add(this.btnDone);

		SpawnCondition.populateData();

		Collections.sort(SpawnCondition.SPAWN_CONDITIONS, new Comparator<SpawnCondition>() {
			@Override
			public int compare(SpawnCondition spawnConditionA, SpawnCondition spawnConditionB) {
				return spawnConditionA.entity.func_145748_c_().getFormattedText().compareTo(spawnConditionB.entity.func_145748_c_().getFormattedText());
			}
		});

		this.guiMonsterSpawnHighlighterSlot = new GuiMonsterSpawnHighlighterSlot(this.mc, this);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.enabled) {
			if (guiButton.id == this.btnToggle.id) {
				ConfigurationHandler.renderSpawns = (ConfigurationHandler.renderSpawns + 1) % 3;
				setToggleText();
			} else if (guiButton.id == this.btnDone.id) {
				this.mc.displayGuiScreen(this.prevGuiScreen);
			} else {
				this.guiMonsterSpawnHighlighterSlot.actionPerformed(guiButton);
			}
		}
	}

	private void setToggleText() {
		this.btnToggle.displayString = ((ConfigurationHandler.renderSpawns == 0) ? this.strDisabled : ((ConfigurationHandler.renderSpawns == 1) ? this.strEnabled : this.strGuide));
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		this.guiMonsterSpawnHighlighterSlot.drawScreen(x, y, partialTicks);

		drawCenteredString(this.fontRendererObj, this.strTitle, this.width / 2, 4, 0x00FFFFFF);

		super.drawScreen(x, y, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		ConfigurationHandler.save();
	}
}
