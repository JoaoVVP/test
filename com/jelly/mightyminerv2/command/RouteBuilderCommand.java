/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.commands.annotations.Command
 *  cc.polyfrost.oneconfig.utils.commands.annotations.Main
 *  cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 */
package com.jelly.mightyminerv2.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import com.jelly.mightyminerv2.feature.impl.RouteBuilder;
import com.jelly.mightyminerv2.feature.impl.RouteNavigator;
import com.jelly.mightyminerv2.handler.RouteHandler;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.route.Route;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

@Command(value="rb", aliases={"route", "routebuilder", "builder"})
public class RouteBuilderCommand {
    @Main
    public void main() {
        if (!RouteBuilder.getInstance().isRunning()) {
            Logger.sendMessage("Enable RouteBuilder First.");
            return;
        }
        Logger.sendMessage("Use these commands to manage your routes.");
        this.success("   1. /rb list -> List all available routes.");
        this.success("   2. /rb select <route-name> -> Select the specified route name. A new route will be created if none exist.");
        this.success("   3. /rb add -> Add the block player is standing on to selected route.");
        this.success("   4. /rb remove -> Remove the block player is standing on from selected route.");
        this.success("   5. /rb replace <index> -> Replaces Specified Index from the route with block player is standing on.");
        this.success("   6. /rb clear <route-name> -> Deletes the route.");
    }

    @SubCommand
    public void list() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Routes: ");
        RouteHandler.getInstance().getRoutes().forEach((key, val) -> {
            String str = key;
            if (RouteHandler.getInstance().getSelectedRoute().equals(val)) {
                str = str + "*";
            }
            sb.append(str).append(", ");
        });
        Logger.sendMessage(sb.toString());
    }

    @SubCommand
    public void select(String routeName) {
        RouteHandler.getInstance().selectRoute(routeName);
    }

    @SubCommand
    public void add(String name) {
        if (!RouteBuilder.getInstance().isRunning()) {
            return;
        }
        RouteBuilder.getInstance().addToRoute(name.equalsIgnoreCase("aotv") ? TransportMethod.AOTV : TransportMethod.ETHERWARP);
    }

    @SubCommand
    public void remove() {
        if (!RouteBuilder.getInstance().isRunning()) {
            return;
        }
        RouteBuilder.getInstance().removeFromRoute();
    }

    @SubCommand
    public void clear(String routeName) {
        if (!RouteBuilder.getInstance().isRunning()) {
            return;
        }
        RouteHandler.getInstance().clearRoute(routeName);
    }

    @SubCommand
    public void replace(int indexToReplace) {
        if (!RouteBuilder.getInstance().isRunning()) {
            return;
        }
        if (indexToReplace <= 0) {
            return;
        }
        RouteBuilder.getInstance().replaceNode(indexToReplace - 1);
    }

    @SubCommand
    public void follow(int rotation) {
        Route route = RouteHandler.getInstance().getSelectedRoute();
        if (route.isEmpty()) {
            this.success("Route is empty.");
            return;
        }
        RouteNavigator.getInstance().setRotationType(rotation == 0 ? RotationConfiguration.RotationType.CLIENT : RotationConfiguration.RotationType.SERVER);
        RouteNavigator.getInstance().start(route);
    }

    private void success(String message) {
        Minecraft.func_71410_x().field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("\u00a7a" + message));
    }
}

