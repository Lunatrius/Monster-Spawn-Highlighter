package com.github.lunatrius.msh.client.gui;

import com.github.lunatrius.msh.EntityLivingEntry;
import com.github.lunatrius.msh.MonsterSpawnHighlighter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiMonsterSpawnHighlighterSlot extends GuiSlot {
	private final GuiMonsterSpawnHighlighter guiMonsterSpawnHighlighter;
	private final TextureManager renderEngine;
	private final FontRenderer fontRenderer;
	private final List<EntityLivingEntry> entityList;

	public GuiMonsterSpawnHighlighterSlot(Minecraft minecraft, GuiMonsterSpawnHighlighter guiMonsterSpawnHighlighter) {
		super(minecraft, guiMonsterSpawnHighlighter.field_146294_l, guiMonsterSpawnHighlighter.field_146295_m, 16, guiMonsterSpawnHighlighter.field_146295_m - 30, 24);
		this.guiMonsterSpawnHighlighter = guiMonsterSpawnHighlighter;
		this.renderEngine = minecraft.renderEngine;
		this.fontRenderer = minecraft.fontRenderer;
		this.entityList = MonsterSpawnHighlighter.instance.entityList;
	}

	@Override
	protected int func_148127_b() {
		return this.entityList.size();
	}

	@Override
	protected void func_148144_a(int index, boolean isDoubleClick, int a, int b) {
		if (index < 0 || index >= this.entityList.size()) {
			return;
		}

		EntityLivingEntry entityLivingEntry = this.entityList.get(index);
		entityLivingEntry.enabled = !entityLivingEntry.enabled;

		this.guiMonsterSpawnHighlighter.config.setEntityEnabled(entityLivingEntry.name, entityLivingEntry.enabled);
	}

	@Override
	protected boolean func_148131_a(int index) {
		return !(index < 0 || index >= this.entityList.size()) && this.entityList.get(index).enabled;

	}

	@Override
	protected void func_148123_a() {
	}

	@Override
	protected void drawContainerBackground(Tessellator tessellator) {
	}

	@Override
	protected void func_148126_a(int index, int x, int y, int par4, Tessellator tessellator, int a, int b) {
		if (index < 0 || index >= this.entityList.size()) {
			return;
		}

		drawEntity(x, y, this.entityList.get(index).entity);
		this.guiMonsterSpawnHighlighter.drawString(this.fontRenderer, this.entityList.get(index).entity.func_145748_c_().func_150254_d(), x + 24, y + 6, 0x00FFFFFF);
	}

	private void drawEntity(int x, int y, EntityLiving entityLiving) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Tessellator tess = Tessellator.instance;

		this.renderEngine.bindTexture(Gui.statIcons);
		drawEntitySlot(tess, x, y);

		TextureInformation ti = MonsterSpawnHighlighter.instance.entityIcons.get(entityLiving.getClass());
		if (ti != null) {
			this.renderEngine.bindTexture(ti.resourceLocation);
			drawTextureParts(tess, x, y, ti);

			if (ti.resourceSpecial != null) {
				this.renderEngine.bindTexture(ti.resourceSpecial);
				drawTextureParts(tess, x, y, ti);
			}
		}
	}

	private void drawEntitySlot(Tessellator tess, int x, int y) {
		tess.startDrawingQuads();
		tess.addVertexWithUV(x + 1, y + 19, 0, 0 * 0.0078125, 18 * 0.0078125);
		tess.addVertexWithUV(x + 19, y + 19, 0, 18 * 0.0078125, 18 * 0.0078125);
		tess.addVertexWithUV(x + 19, y + 1, 0, 18 * 0.0078125, 0 * 0.0078125);
		tess.addVertexWithUV(x + 1, y + 1, 0, 0 * 0.0078125, 0 * 0.0078125);
		tess.draw();
	}

	private void drawTextureParts(Tessellator tess, int x, int y, TextureInformation ti) {
		tess.startDrawingQuads();
		for (TextureInformation.TexturePart tp : ti.textureParts) {
			tess.addVertexWithUV(x + 2 + tp.x, y + 2 + tp.y + tp.height, 0, tp.srcX / ti.width, (tp.srcY + tp.srcHeight) / ti.height);
			tess.addVertexWithUV(x + 2 + tp.x + tp.width, y + 2 + tp.y + tp.height, 0, (tp.srcX + tp.srcWidth) / ti.width, (tp.srcY + tp.srcHeight) / ti.height);
			tess.addVertexWithUV(x + 2 + tp.x + tp.width, y + 2 + tp.y, 0, (tp.srcX + tp.srcWidth) / ti.width, tp.srcY / ti.height);
			tess.addVertexWithUV(x + 2 + tp.x, y + 2 + tp.y, 0, tp.srcX / ti.width, tp.srcY / ti.height);
		}
		tess.draw();
	}
}