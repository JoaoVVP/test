/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent
extends Event {
    public final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    @Cancelable
    public static class Received
    extends PacketEvent {
        public Received(Packet<?> packet) {
            super(packet);
        }
    }

    @Cancelable
    public static class Sent
    extends PacketEvent {
        public Sent(Packet<?> packet) {
            super(packet);
        }
    }
}

