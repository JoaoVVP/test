/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import java.util.List;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateTablistEvent
extends Event {
    public final List<String> tablist;
    public final long timestamp;

    public UpdateTablistEvent(List<String> tablist, long timestamp) {
        this.tablist = tablist;
        this.timestamp = timestamp;
    }
}

