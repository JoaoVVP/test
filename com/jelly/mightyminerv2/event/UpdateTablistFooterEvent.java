/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.jelly.mightyminerv2.event;

import java.util.List;
import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateTablistFooterEvent
extends Event {
    public final List<String> footer;

    public UpdateTablistFooterEvent(List<String> footer) {
        this.footer = footer;
    }
}

