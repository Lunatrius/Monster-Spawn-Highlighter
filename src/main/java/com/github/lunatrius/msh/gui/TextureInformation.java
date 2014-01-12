package com.github.lunatrius.msh.gui;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TextureInformation {
	public class TexturePart {
		public final double x;
		public final double y;
		public final double width;
		public final double height;

		public final double srcX;
		public final double srcY;
		public final double srcWidth;
		public final double srcHeight;

		public TexturePart(double srcX, double srcY, double srcWidth, double srcHeight) {
			this(0, 0, 16, 16, srcX, srcY, srcWidth, srcHeight);
		}

		public TexturePart(double x, double y, double width, double height, double srcX, double srcY, double srcWidth, double srcHeight) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.srcX = srcX;
			this.srcY = srcY;
			this.srcWidth = srcWidth;
			this.srcHeight = srcHeight;
		}
	}

	public final ResourceLocation resourceLocation;
	public final ResourceLocation resourceSpecial;
	public final int width;
	public final int height;
	public final List<TexturePart> textureParts = new ArrayList<TexturePart>();

	public TextureInformation(EntityLiving entityLiving) throws NullPointerException, IOException, InvocationTargetException, IllegalAccessException {
		Class<? extends EntityLiving> clazz = entityLiving.getClass();
		RenderLiving renderLiving = (RenderLiving) RenderManager.instance.getEntityClassRenderObject(clazz);

		Method method = ReflectionHelper.findMethod(net.minecraft.client.renderer.entity.Render.class, renderLiving, new String[] {
				"func_110775_a", "a", "getEntityTexture"
		}, new Class[] { Entity.class });
		this.resourceLocation = (ResourceLocation) method.invoke(renderLiving, entityLiving);

		if (clazz.equals(EntityEnderman.class)) {
			this.resourceSpecial = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
		} else {
			this.resourceSpecial = null;
		}

		if (clazz.equals(EntityBat.class)) {
			this.textureParts.add(new TexturePart(6, 6, 6, 6));
		} else if (clazz.equals(EntityChicken.class)) {
			this.textureParts.add(new TexturePart(2, 3, 6, 6));
			this.textureParts.add(new TexturePart(2, 6, 12, 6, 16, 2, 4, 2));
		} else if (clazz.equals(EntityCow.class)) {
			this.textureParts.add(new TexturePart(6, 6, 8, 8));
		} else if (clazz.equals(EntityCreeper.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
		} else if (clazz.equals(EntityEnderman.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
			this.textureParts.add(new TexturePart(8, 24, 8, 8));
		} else if (clazz.equals(EntityGhast.class)) {
			this.textureParts.add(new TexturePart(16, 16, 16, 16));
		} else if (clazz.equals(EntityMagmaCube.class)) {
			// base
			this.textureParts.add(new TexturePart(32, 16, 8, 8));
			// eyes
			this.textureParts.add(new TexturePart(0, 6, 16, 2, 32, 27, 8, 1));
		} else if (clazz.equals(EntityMooshroom.class)) {
			this.textureParts.add(new TexturePart(6, 6, 8, 8));
		} else if (clazz.equals(EntityOcelot.class)) {
			this.textureParts.add(new TexturePart(5, 5, 5, 4));
			this.textureParts.add(new TexturePart(3, 8, 10, 8, 2, 26, 3, 2));
		} else if (clazz.equals(EntityPig.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
			this.textureParts.add(new TexturePart(4, 8, 8, 6, 17, 17, 4, 3));
		} else if (clazz.equals(EntityPigZombie.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
			this.textureParts.add(new TexturePart(40, 8, 8, 8));
		} else if (clazz.equals(EntitySheep.class)) {
			this.textureParts.add(new TexturePart(8, 8, 6, 6));
		} else if (clazz.equals(EntitySkeleton.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
		} else if (clazz.equals(EntitySlime.class)) {
			// core
			this.textureParts.add(new TexturePart(2, 2, 12, 12, 6, 22, 6, 6));
			// left eye
			this.textureParts.add(new TexturePart(2, 4, 4, 4, 34, 2, 2, 2));
			// right eye
			this.textureParts.add(new TexturePart(10, 4, 4, 4, 34, 6, 2, 2));
			// mouth
			this.textureParts.add(new TexturePart(8, 10, 2, 2, 33, 9, 1, 1));
			// shell
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
		} else if (clazz.equals(EntitySpider.class)) {
			this.textureParts.add(new TexturePart(40, 12, 8, 8));
		} else if (clazz.equals(EntitySquid.class)) {
			this.textureParts.add(new TexturePart(12, 12, 12, 16));
		} else if (clazz.equals(EntityWolf.class)) {
			// base
			this.textureParts.add(new TexturePart(4, 4, 6, 6));
			// nose
			this.textureParts.add(new TexturePart(4, 8, 8, 8, 4, 14, 3, 3));
		} else if (clazz.equals(EntityZombie.class)) {
			this.textureParts.add(new TexturePart(8, 8, 8, 8));
		}

		InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(this.resourceLocation).getInputStream();
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		this.width = bufferedImage.getWidth();
		this.height = bufferedImage.getHeight();
	}
}
