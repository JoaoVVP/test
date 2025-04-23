/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateEntityEvent
extends Event {
    public final EntityLivingBase entity;
    public final byte updateType;
    public long newHash;

    public UpdateEntityEvent(EntityLivingBase entity) {
        this.entity = entity;
        this.updateType = 0;
    }

    public UpdateEntityEvent(EntityLivingBase entity, byte updateType) {
        this.entity = entity;
        this.updateType = updateType;
    }

    public UpdateEntityEvent(EntityLivingBase entity, long newHash) {
        this.entity = entity;
        this.updateType = (byte)2;
        this.newHash = newHash;
    }
}

