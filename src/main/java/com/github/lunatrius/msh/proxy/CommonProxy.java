package com.github.lunatrius.msh.proxy;

import net.minecraft.entity.EntityLiving;

public abstract class CommonProxy {
    public abstract void setConfigEntryClasses();

    public abstract void registerKeybindings();

    public abstract void registerEvents();

    public abstract void constructTextureInformation(EntityLiving entityLiving);
}
