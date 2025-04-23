/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import java.util.List;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateScoreboardEvent
extends Event {
    public final List<String> scoreboard;
    public final long timestamp;

    public UpdateScoreboardEvent(List<String> scoreboard, long timestamp) {
        this.scoreboard = scoreboard;
        this.timestamp = timestamp;
    }
}

