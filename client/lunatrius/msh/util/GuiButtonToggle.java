package lunatrius.msh.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonToggle extends GuiButton {
	public GuiButtonToggle(int var1, int var2, int var3, int var4, int var5, String var6) {
		super(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public boolean mousePressed(Minecraft var1, int var2, int var3) {
		return this.drawButton && var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
	}
}
