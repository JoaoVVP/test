/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.play.server.S1DPacketEntityEffect
 *  net.minecraft.potion.Potion
 */
package com.jelly.mightyminerv2.failsafe.impl;

import com.google.common.collect.ImmutableSet;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.macro.MacroManager;
import java.util.Set;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.Potion;

public class BadEffectFailsafe
extends AbstractFailsafe {
    private static final BadEffectFailsafe instance = new BadEffectFailsafe();
    private final Set<Integer> BAD_EFFECTS;

    public BadEffectFailsafe() {
        this.BAD_EFFECTS = ImmutableSet.of((Object)Potion.field_76436_u.field_76415_H, (Object)Potion.field_82731_v.field_76415_H, (Object)Potion.field_76437_t.field_76415_H, (Object)Potion.field_76440_q.field_76415_H, (Object)Potion.field_76438_s.field_76415_H, (Object)Potion.field_76421_d.field_76415_H, (Object[])new Integer[]{Potion.field_76419_f.field_76415_H});
    }

    public static BadEffectFailsafe getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "BadEffectFailsafe";
    }

    @Override
    public AbstractFailsafe.Failsafe getFailsafeType() {
        return AbstractFailsafe.Failsafe.BAD_EFFECTS;
    }

    @Override
    public int getPriority() {
        return 7;
    }

    @Override
    public boolean onPacketReceive(PacketEvent.Received event) {
        if (!(event.packet instanceof S1DPacketEntityEffect)) {
            return false;
        }
        S1DPacketEntityEffect packet = (S1DPacketEntityEffect)event.packet;
        return this.mc.field_71441_e.func_73045_a(packet.func_149426_d()) instanceof EntityPlayerSP && this.BAD_EFFECTS.contains(packet.func_149427_e());
    }

    @Override
    public boolean react() {
        MacroManager.getInstance().disable();
        this.warn("Bad effect detected! Disabling macro.");
        return true;
    }
}

