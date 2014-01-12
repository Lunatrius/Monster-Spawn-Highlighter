package com.github.lunatrius.msh.gui;

import com.github.lunatrius.msh.Config;
import com.github.lunatrius.msh.EntityLivingEntry;
import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.util.StatCollector;

import java.util.Collections;
import java.util.Comparator;

public class GuiMonsterSpawnHighlighter extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	protected final Config config;

	private GuiMonsterSpawnHighlighterSlot guiMonsterSpawnHighlighterSlot;

	private GuiSmallButton btnToggle = null;
	private GuiSmallButton btnDone = null;

	private final String strTitle = StatCollector.translateToLocal("msh.gui.title");
	private final String strDone = StatCollector.translateToLocal("msh.gui.done");
	private final String strDisabled = StatCollector.translateToLocal("msh.gui.disabled");
	private final String strEnabled = StatCollector.translateToLocal("msh.gui.enabled");
	private final String strGuide = StatCollector.translateToLocal("msh.gui.guide");

	public GuiMonsterSpawnHighlighter(GuiScreen guiScreen) {
		this.prevGuiScreen = guiScreen;
		this.config = MonsterSpawnHighlighter.instance.config;
	}

	@Override
	public void initGui() {
		int id = 0;

		this.btnToggle = new GuiSmallButton(id++, this.width / 2 - 154, this.height - 26, "N/A");
		this.buttonList.add(this.btnToggle);
		setToggleText();

		this.btnDone = new GuiSmallButton(id++, this.width / 2 + 4, this.height - 26, this.strDone);
		this.buttonList.add(this.btnDone);

		Collections.sort(MonsterSpawnHighlighter.instance.entityList, new Comparator<EntityLivingEntry>() {
			@Override
			public int compare(EntityLivingEntry entityLivingEntry1, EntityLivingEntry entityLivingEntry2) {
				return entityLivingEntry1.entity.getEntityName().compareTo(entityLivingEntry2.entity.getEntityName());
			}
		});

		this.guiMonsterSpawnHighlighterSlot = new GuiMonsterSpawnHighlighterSlot(this.mc, this);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.enabled) {
			if (guiButton.id == this.btnToggle.id) {
				this.config.renderSpawns = (this.config.renderSpawns + 1) % 3;
				setToggleText();
			} else if (guiButton.id == this.btnDone.id) {
				this.mc.displayGuiScreen(this.prevGuiScreen);
			} else {
				this.guiMonsterSpawnHighlighterSlot.actionPerformed(guiButton);
			}
		}
	}

	private void setToggleText() {
		this.btnToggle.displayString = ((this.config.renderSpawns == 0) ? this.strDisabled : ((this.config.renderSpawns == 1) ? this.strEnabled : this.strGuide));
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		this.guiMonsterSpawnHighlighterSlot.drawScreen(x, y, partialTicks);

		drawCenteredString(this.fontRenderer, this.strTitle, this.width / 2, 4, 0x00FFFFFF);

		super.drawScreen(x, y, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		this.config.save();
	}
}
