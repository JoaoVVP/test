/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 */
package com.jelly.mightyminerv2.util.helper.route;

import com.google.gson.annotations.Expose;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class Route {
    @Expose
    private final List<RouteWaypoint> waypoints = new ArrayList<RouteWaypoint>();

    public Route() {
    }

    public Route(List<RouteWaypoint> routes) {
        this.waypoints.addAll(routes);
    }

    public void insert(RouteWaypoint waypoint) {
        this.insert(this.waypoints.size(), waypoint);
    }

    public void insert(int index, RouteWaypoint waypoint) {
        this.waypoints.add(index, waypoint);
    }

    public void remove(RouteWaypoint waypoint) {
        int index = this.waypoints.indexOf(waypoint);
        if (index == -1) {
            return;
        }
        this.remove(index);
    }

    public void remove(int index) {
        this.waypoints.remove(index);
    }

    public RouteWaypoint get(int index) {
        return this.waypoints.get((index + this.waypoints.size()) % this.waypoints.size());
    }

    public Optional<RouteWaypoint> getClosest(BlockPos pos) {
        return this.waypoints.stream().min(Comparator.comparingDouble(wp -> wp.toVec3().func_72436_e(new Vec3((Vec3i)pos))));
    }

    public int indexOf(RouteWaypoint waypoint) {
        return this.waypoints.indexOf(waypoint);
    }

    public void replace(int index, RouteWaypoint waypoint) {
        this.waypoints.set(index, waypoint);
    }

    public boolean isEnd(int index) {
        return index + 1 == this.waypoints.size();
    }

    public boolean isEmpty() {
        return this.waypoints.isEmpty();
    }

    public void drawRoute() {
        for (int i = 0; i < this.waypoints.size(); ++i) {
            RouteWaypoint currWaypoint = this.get(i);
            RenderUtil.drawBlock(currWaypoint.toBlockPos(), MightyMinerConfig.routeBuilderNodeColor.toJavaColor());
            RenderUtil.drawText(String.valueOf(i + 1), (double)currWaypoint.getX() + 0.5, currWaypoint.getY() + 1, (double)currWaypoint.getZ() + 0.5, 1.0f);
            if (this.waypoints.size() == 1) continue;
            RouteWaypoint prevWaypoint = this.get(i - 1);
            RenderUtil.drawLine(prevWaypoint.toVec3().func_72441_c(0.5, 0.5, 0.5), currWaypoint.toVec3().func_72441_c(0.5, 0.5, 0.5), MightyMinerConfig.routeBuilderTracerColor.toJavaColor());
        }
    }

    public int size() {
        return this.waypoints.size();
    }
}

