package com.github.lunatrius.msh.client.gui;

import com.github.lunatrius.msh.EntityLivingEntry;
import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import com.github.lunatrius.msh.config.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

import java.util.Collections;
import java.util.Comparator;

public class GuiMonsterSpawnHighlighter extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	protected final Config config;

	private GuiMonsterSpawnHighlighterSlot guiMonsterSpawnHighlighterSlot;

	private GuiButton btnToggle = null;
	private GuiButton btnDone = null;

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

		this.btnToggle = new GuiButton(id++, this.field_146294_l / 2 - 154, this.field_146295_m - 26, 150, 20, "N/A");
		this.field_146292_n.add(this.btnToggle);
		setToggleText();

		this.btnDone = new GuiButton(id++, this.field_146294_l / 2 + 4, this.field_146295_m - 26, 150, 20, this.strDone);
		this.field_146292_n.add(this.btnDone);

		Collections.sort(MonsterSpawnHighlighter.instance.entityList, new Comparator<EntityLivingEntry>() {
			@Override
			public int compare(EntityLivingEntry entityLivingEntry1, EntityLivingEntry entityLivingEntry2) {
				return entityLivingEntry1.entity.func_145748_c_().func_150254_d().compareTo(entityLivingEntry2.entity.func_145748_c_().func_150254_d());
			}
		});

		this.guiMonsterSpawnHighlighterSlot = new GuiMonsterSpawnHighlighterSlot(this.field_146297_k, this);
	}

	@Override
	protected void func_146284_a(GuiButton guiButton) {
		if (guiButton.field_146124_l) {
			if (guiButton.field_146127_k == this.btnToggle.field_146127_k) {
				this.config.renderSpawns = (this.config.renderSpawns + 1) % 3;
				setToggleText();
			} else if (guiButton.field_146127_k == this.btnDone.field_146127_k) {
				this.field_146297_k.func_147108_a(this.prevGuiScreen);
			} else {
				this.guiMonsterSpawnHighlighterSlot.func_148147_a(guiButton);
			}
		}
	}

	private void setToggleText() {
		this.btnToggle.field_146126_j = ((this.config.renderSpawns == 0) ? this.strDisabled : ((this.config.renderSpawns == 1) ? this.strEnabled : this.strGuide));
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		this.guiMonsterSpawnHighlighterSlot.func_148128_a(x, y, partialTicks);

		drawCenteredString(this.field_146289_q, this.strTitle, this.field_146294_l / 2, 4, 0x00FFFFFF);

		super.drawScreen(x, y, partialTicks);
	}

	@Override
	public void func_146281_b() {
		this.config.save();
	}
}
