/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package com.jelly.mightyminerv2.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Minecraft.class})
public interface MinecraftAccessor {
    @Accessor(value="timer")
    public Timer getTimer();

    @Accessor(value="leftClickCounter")
    public void setLeftClickCounter(int var1);

    @Accessor(value="rightClickDelayTimer")
    public int getRightClickDelayTimer();

    @Accessor(value="rightClickDelayTimer")
    public void setRightClickDelayTimer(int var1);

    @Invoker(value="clickMouse")
    public void leftClick();

    @Invoker(value="rightClickMouse")
    public void rightClick();

    @Invoker(value="middleClickMouse")
    public void middleClick();
}

