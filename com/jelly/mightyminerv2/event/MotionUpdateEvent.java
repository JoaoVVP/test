/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MotionUpdateEvent
extends Event {
    public float yaw;
    public float pitch;

    public MotionUpdateEvent(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}

