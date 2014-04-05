package com.github.lunatrius.msh.client.gui;

import com.github.lunatrius.msh.EntityLivingEntry;
import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import com.github.lunatrius.msh.config.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.Collections;
import java.util.Comparator;

public class GuiMonsterSpawnHighlighter extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	protected final Config config;

	private GuiMonsterSpawnHighlighterSlot guiMonsterSpawnHighlighterSlot;

	private GuiButton btnToggle = null;
	private GuiButton btnDone = null;

	private final String strTitle = I18n.format("msh.gui.title");
	private final String strDone = I18n.format("msh.gui.done");
	private final String strDisabled = I18n.format("msh.gui.disabled");
	private final String strEnabled = I18n.format("msh.gui.enabled");
	private final String strGuide = I18n.format("msh.gui.guide");

	public GuiMonsterSpawnHighlighter(GuiScreen guiScreen) {
		this.prevGuiScreen = guiScreen;
		this.config = MonsterSpawnHighlighter.instance.config;
	}

	@Override
	public void initGui() {
		int id = 0;

		this.btnToggle = new GuiButton(id++, this.width / 2 - 154, this.height - 26, 150, 20, "N/A");
		this.buttonList.add(this.btnToggle);
		setToggleText();

		this.btnDone = new GuiButton(id++, this.width / 2 + 4, this.height - 26, 150, 20, this.strDone);
		this.buttonList.add(this.btnDone);

		Collections.sort(MonsterSpawnHighlighter.instance.entityList, new Comparator<EntityLivingEntry>() {
			@Override
			public int compare(EntityLivingEntry entityLivingEntry1, EntityLivingEntry entityLivingEntry2) {
				return entityLivingEntry1.entity.func_145748_c_().getFormattedText().compareTo(entityLivingEntry2.entity.func_145748_c_().getFormattedText());
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

		drawCenteredString(this.fontRendererObj, this.strTitle, this.width / 2, 4, 0x00FFFFFF);

		super.drawScreen(x, y, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		this.config.save();
	}
}
