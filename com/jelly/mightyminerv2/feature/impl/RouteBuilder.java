/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Multithreading
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import cc.polyfrost.oneconfig.utils.Multithreading;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.handler.RouteHandler;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class RouteBuilder
extends AbstractFeature {
    private static RouteBuilder instance;

    public static RouteBuilder getInstance() {
        if (instance == null) {
            instance = new RouteBuilder();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "RouteBuilder";
    }

    public void toggle() {
        if (!this.enabled) {
            this.start();
        } else {
            this.stop();
        }
    }

    @Override
    public void start() {
        this.enabled = true;
        Multithreading.schedule(RouteHandler.getInstance()::saveData, (long)0L, (TimeUnit)TimeUnit.MILLISECONDS);
        this.send("Enabling RouteBuilder.");
    }

    @Override
    public void stop() {
        this.enabled = false;
        this.send("Disabling RouteBuilder.");
    }

    @Override
    @SubscribeEvent
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        if (!this.enabled) {
            return;
        }
        if (MightyMinerConfig.routeBuilderAotvAddKeybind.isActive()) {
            this.addToRoute(TransportMethod.AOTV);
            Logger.sendMessage("Added Aotv");
        }
        if (MightyMinerConfig.routeBuilderEtherwarpAddKeybind.isActive()) {
            this.addToRoute(TransportMethod.ETHERWARP);
            Logger.sendMessage("Added Etherwarp");
        }
        if (MightyMinerConfig.routeBuilderRemoveKeybind.isActive()) {
            this.removeFromRoute();
        }
    }

    public void addToRoute(TransportMethod method) {
        RouteHandler.getInstance().addToCurrentRoute(PlayerUtil.getBlockStandingOn(), method);
    }

    public void removeFromRoute() {
        RouteHandler.getInstance().removeFromCurrentRoute(PlayerUtil.getBlockStandingOn());
    }

    public void replaceNode(int index) {
        RouteHandler.getInstance().replaceInCurrentRoute(index, new RouteWaypoint(PlayerUtil.getBlockStandingOn(), TransportMethod.ETHERWARP));
    }
}

