/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Multithreading
 *  com.google.gson.annotations.Expose
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent
 */
package com.jelly.mightyminerv2.handler;

import cc.polyfrost.oneconfig.utils.Multithreading;
import com.google.gson.annotations.Expose;
import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.macro.AbstractMacro;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.helper.graph.Graph;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import java.awt.Color;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class GraphHandler {
    public static final GraphHandler instance = new GraphHandler();
    @Expose
    public final Map<String, Graph<RouteWaypoint>> graphs = new HashMap<String, Graph<RouteWaypoint>>();
    private String activeGraphKey = "default";
    private boolean editing = false;
    private boolean dirty = false;
    private RouteWaypoint lastPos = null;

    public Graph<RouteWaypoint> getActiveGraph() {
        return this.graphs.computeIfAbsent(this.activeGraphKey, k -> new Graph());
    }

    public void switchGraph(AbstractMacro macro) {
        this.activeGraphKey = macro.getName();
        this.getActiveGraph();
        Logger.sendMessage("Switched to graph: " + macro.getName());
    }

    public void toggleEdit(String graphName) {
        if (!this.graphs.containsKey(graphName)) {
            return;
        }
        this.activeGraphKey = graphName;
        if (this.editing) {
            this.stop();
        } else {
            this.start();
        }
        Logger.sendMessage(this.editing ? "Editing " + graphName : "Stopped Editing " + graphName);
    }

    public void toggleEdit() {
        if (this.editing) {
            this.stop();
        } else {
            this.start();
        }
        Logger.sendMessage(this.editing ? "Editing " + this.activeGraphKey : "Stopped Editing " + this.activeGraphKey);
    }

    public void start() {
        this.editing = true;
        Multithreading.schedule(this::save, (long)0L, (TimeUnit)TimeUnit.MILLISECONDS);
    }

    public void stop() {
        this.editing = false;
        this.dirty = false;
    }

    public List<RouteWaypoint> findPath(BlockPos start2, RouteWaypoint end) {
        Graph<RouteWaypoint> graph = this.getActiveGraph();
        RouteWaypoint startWp = graph.map.keySet().stream().min(Comparator.comparing(wp -> start2.func_177951_i((Vec3i)wp.toBlockPos()))).orElse(new RouteWaypoint(start2, TransportMethod.WALK));
        if (!graph.map.containsKey(end)) {
            return Collections.emptyList();
        }
        return this.findPath(startWp, end);
    }

    public List<RouteWaypoint> findPath(RouteWaypoint first, RouteWaypoint second) {
        Graph<RouteWaypoint> graph = this.getActiveGraph();
        List<RouteWaypoint> route = graph.findPath(first, second);
        if (route.size() < 2 || PlayerUtil.getBlockStandingOn().func_177951_i((Vec3i)route.get(0).toBlockPos()) >= route.get(0).toBlockPos().func_177951_i((Vec3i)route.get(1).toBlockPos())) {
            return route;
        }
        route.remove(0);
        return route;
    }

    public List<RouteWaypoint> findPathFrom(String graphName, BlockPos start2, RouteWaypoint end) {
        Graph<RouteWaypoint> graph = this.graphs.get(graphName);
        if (graph == null) {
            return Collections.emptyList();
        }
        RouteWaypoint startWp = graph.map.keySet().stream().min(Comparator.comparing(wp -> start2.func_177951_i((Vec3i)wp.toBlockPos()))).orElse(new RouteWaypoint(start2, TransportMethod.WALK));
        if (!graph.map.containsKey(end)) {
            return Collections.emptyList();
        }
        return this.findPathFrom(graphName, startWp, end);
    }

    public List<RouteWaypoint> findPathFrom(String graphName, RouteWaypoint first, RouteWaypoint second) {
        Graph<RouteWaypoint> graph = this.graphs.get(graphName);
        if (graph == null) {
            return Collections.emptyList();
        }
        List<RouteWaypoint> route = graph.findPath(first, second);
        if (route.size() < 2 || PlayerUtil.getBlockStandingOn().func_177951_i((Vec3i)route.get(0).toBlockPos()) >= route.get(0).toBlockPos().func_177951_i((Vec3i)route.get(1).toBlockPos())) {
            return route;
        }
        route.remove(0);
        return route;
    }

    public synchronized void save() {
        while (this.editing) {
            if (!this.dirty) continue;
            Graph<RouteWaypoint> graph = this.graphs.get(this.activeGraphKey);
            try (BufferedWriter writer = Files.newBufferedWriter(MightyMiner.routesDirectory.resolve(this.activeGraphKey + ".json"), StandardCharsets.UTF_8, new OpenOption[0]);){
                writer.write(MightyMiner.gson.toJson(graph));
                Logger.sendLog("Saved graph: " + this.activeGraphKey);
            }
            catch (Exception e) {
                Logger.sendLog("Failed to save graph: " + this.activeGraphKey);
                e.printStackTrace();
            }
            this.dirty = false;
        }
    }

    @SubscribeEvent
    public void onInput(InputEvent event) {
        if (!this.editing) {
            return;
        }
        RouteWaypoint currentWaypoint = new RouteWaypoint(PlayerUtil.getBlockStandingOn(), TransportMethod.WALK);
        Graph<RouteWaypoint> graph = this.getActiveGraph();
        if (MightyMinerConfig.routeBuilderSelect.isActive()) {
            this.lastPos = currentWaypoint;
            Logger.sendMessage("Changed Parent");
        }
        if (MightyMinerConfig.routeBuilderUnidi.isActive() || MightyMinerConfig.routeBuilderBidi.isActive()) {
            if (this.lastPos != null) {
                graph.add(this.lastPos, currentWaypoint, MightyMinerConfig.routeBuilderBidi.isActive());
                Logger.sendMessage("Added " + (MightyMinerConfig.routeBuilderBidi.isActive() ? "Bidirectional" : "Unidirectional"));
            } else {
                graph.add(currentWaypoint);
                Logger.sendMessage("Added Single Waypoint");
            }
            this.lastPos = currentWaypoint;
            this.dirty = true;
        }
        if (MightyMinerConfig.routeBuilderMove.isActive() && this.lastPos != null) {
            graph.update(this.lastPos, currentWaypoint);
            this.lastPos = currentWaypoint;
            this.dirty = true;
            Logger.sendMessage("Updated");
        }
        if (MightyMinerConfig.routeBuilderDelete.isActive() && this.lastPos != null) {
            graph.remove(this.lastPos);
            this.lastPos = null;
            this.dirty = true;
            Logger.sendMessage("Removed");
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!this.editing) {
            return;
        }
        Graph<RouteWaypoint> graph = this.getActiveGraph();
        for (Map.Entry entry : graph.map.entrySet()) {
            RenderUtil.drawBlock(((RouteWaypoint)entry.getKey()).toBlockPos(), new Color(101, 10, 142, 186));
            for (RouteWaypoint edge : entry.getValue()) {
                RenderUtil.drawLine(((RouteWaypoint)entry.getKey()).toVec3().func_72441_c(0.5, 0.5, 0.5), edge.toVec3().func_72441_c(0.5, 0.5, 0.5), new Color(194, 12, 164, 179));
            }
        }
        if (this.lastPos != null) {
            RenderUtil.drawBlock(new BlockPos(this.lastPos.toVec3()), new Color(255, 0, 0, 150));
        }
    }
}

