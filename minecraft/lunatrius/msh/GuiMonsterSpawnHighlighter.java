package lunatrius.msh;

import lunatrius.msh.util.Config;
import lunatrius.msh.util.GuiButtonToggle;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class GuiMonsterSpawnHighlighter extends GuiScreen {
	private final Settings settings = Settings.instance();
	private final StringTranslate strTranslate = StringTranslate.getInstance();

	private final GuiButtonToggle[] btnToggleMonsters = new GuiButtonToggle[this.settings.entityLiving.length];

	private GuiButton btnEnabled = null;
	private GuiButton btnDone = null;

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		int id = 0;

		int baseX = this.width / 2 - 140;
		int baseY = this.height / 2 - (int) Math.ceil(this.btnToggleMonsters.length / 3.0 + 1) * 25 / 2 - 5;

		for (int i = 0; i < this.btnToggleMonsters.length; i++) {
			this.btnToggleMonsters[i] = new GuiButtonToggle(id++, baseX + (i % 3) * 95, baseY + (i / 3) * 25, 90, 20, this.strTranslate.translateKey("entity." + this.settings.entityLiving[i].name + ".name"));
			this.buttonList.add(this.btnToggleMonsters[i]);
			this.btnToggleMonsters[i].enabled = this.settings.entityLiving[i].enabled;
		}

		this.btnEnabled = new GuiButton(id++, baseX, baseY + (this.btnToggleMonsters.length / 3) * 25 + 10, 90, 20, this.settings.renderBlocks == 0 ? "Disabled" : (this.settings.renderBlocks == 1 ? "Squares" : "Guides"));
		this.buttonList.add(this.btnEnabled);

		this.btnDone = new GuiButton(id++, baseX + 190, baseY + (this.btnToggleMonsters.length / 3) * 25 + 10, 90, 20, "Done");
		this.buttonList.add(this.btnDone);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		for (int i = 0; i < this.btnToggleMonsters.length; i++) {
			if (guiButton.id == this.btnToggleMonsters[i].id) {
				this.settings.entityLiving[i].enabled = !this.settings.entityLiving[i].enabled;
				this.btnToggleMonsters[i].enabled = this.settings.entityLiving[i].enabled;

				Property prop = Config.get(this.settings.config, Configuration.CATEGORY_GENERAL, "enabled" + this.settings.entityLiving[i].name, false, "Enable spawn rendering of " + this.settings.entityLiving[i].name + ".");
				prop.set(this.settings.entityLiving[i].enabled);
			}
		}

		if (guiButton.id == this.btnEnabled.id) {
			this.settings.renderBlocks = (this.settings.renderBlocks + 1) % 3;
			this.btnEnabled.displayString = this.settings.renderBlocks == 0 ? "Disabled" : (this.settings.renderBlocks == 1 ? "Squares" : "Guides");
		} else if (guiButton.id == this.btnDone.id) {
			this.mc.displayGuiScreen(null);
			this.settings.config.save();
		}
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();

		super.drawScreen(var1, var2, var3);
	}

	@Override
	public void onGuiClosed() {
		this.settings.config.save();
	}
}
