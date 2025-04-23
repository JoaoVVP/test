/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.macro.commissionmacro.helper;

import com.jelly.mightyminerv2.util.CommissionUtil;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.location.SubLocation;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.Vec3;

public enum Commission {
    MITHRIL_MINER("Mithril Miner", SubLocation.RAMPARTS_QUARRY),
    TITANIUM_MINER("Titanium Miner", SubLocation.RAMPARTS_QUARRY),
    UPPER_MITHRIL("Upper Mines Mithril", SubLocation.UPPER_MINES),
    UPPER_TITANIUM("Upper Mines Titanium", SubLocation.UPPER_MINES),
    ROYAL_MITHRIL("Royal Mines Mithril", SubLocation.ROYAL_MINES),
    ROYAL_TITANIUM("Royal Mines Titanium", SubLocation.ROYAL_MINES),
    LAVA_MITHRIL("Lava Springs Mithril", SubLocation.LAVA_SPRINGS),
    LAVA_TITANIUM("Lava Springs Titanium", SubLocation.LAVA_SPRINGS),
    CLIFFSIDE_MITHRIL("Cliffside Veins Mithril", SubLocation.CLIFFSIDE_VEINS),
    CLIFFSIDE_TITANIUM("Cliffside Veins Titanium", SubLocation.CLIFFSIDE_VEINS),
    RAMPARTS_MITHRIL("Rampart's Quarry Mithril", SubLocation.RAMPARTS_QUARRY),
    RAMPARTS_TITANIUM("Rampart's Quarry Titanium", SubLocation.RAMPARTS_QUARRY),
    GOBLIN_SLAYER("Goblin Slayer", SubLocation.GOBLIN_BURROWS),
    GLACITE_WALKER_SLAYER("Glacite Walker Slayer", SubLocation.GREAT_ICE_WALL),
    COMMISSION_CLAIM("Claim Commission", SubLocation.THE_FORGE),
    REFUEL("Refuel Drill", SubLocation.FORGE_BASIN);

    private static final Map<String, Commission> COMMISSIONS;
    private static final Map<SubLocation, RouteWaypoint[]> VEINS;
    private final String name;
    private final SubLocation location;
    private final int priority;

    private Commission(String name, SubLocation location) {
        this.name = name;
        this.location = location;
        this.priority = name.endsWith("Miner") ? 1 : 0;
    }

    public static Commission getCommission(String name) {
        return COMMISSIONS.get(name);
    }

    public String getName() {
        return this.name;
    }

    public static List<Commission> getBestCommissionFrom(List<Commission> commissions) {
        int size = commissions.size();
        if (size == 0) {
            return Collections.emptyList();
        }
        if (size == 1) {
            return commissions;
        }
        commissions.sort(Comparator.comparing(it -> it.priority));
        SubLocation lastLoc = commissions.get((int)0).location;
        boolean hasMiner = false;
        boolean sameLocation = true;
        for (Commission comm : commissions) {
            if (comm.getName().contains("Slayer")) {
                return new ArrayList<Commission>(Collections.singletonList(commissions.get(0)));
            }
            if (comm.getName().contains("Miner")) {
                hasMiner = true;
            }
            if (lastLoc.equals((Object)comm.location)) continue;
            sameLocation = false;
        }
        if (!sameLocation && !hasMiner) {
            return new ArrayList<Commission>(Collections.singletonList(commissions.get(0)));
        }
        Logger.sendNote("overlap: " + commissions);
        return commissions;
    }

    public RouteWaypoint getWaypoint() {
        if (this.name.equals("Claim Commission")) {
            return this.closestWaypointTo(CommissionUtil.getClosestEmissaryPosition());
        }
        RouteWaypoint[] locs = VEINS.get((Object)this.location);
        if (locs != null && locs.length > 0) {
            return locs[new Random().nextInt(locs.length)];
        }
        throw new IllegalStateException("No route waypoints available for location: " + (Object)((Object)this.location));
    }

    public RouteWaypoint closestWaypointTo(Vec3 pos) {
        RouteWaypoint[] locs = VEINS.get((Object)this.location);
        if (locs != null && locs.length > 0) {
            return Arrays.stream(locs).min(Comparator.comparing(it -> it.toVec3().func_72438_d(pos))).get();
        }
        throw new IllegalStateException("No route waypoints available for location: " + (Object)((Object)this.location));
    }

    static {
        HashMap<String, Commission> commissionsMap = new HashMap<String, Commission>();
        for (Commission comm : Commission.values()) {
            commissionsMap.put(comm.name, comm);
        }
        COMMISSIONS = Collections.unmodifiableMap(commissionsMap);
        EnumMap<SubLocation, RouteWaypoint[]> veinsMap = new EnumMap<SubLocation, RouteWaypoint[]>(SubLocation.class){
            {
                this.put(SubLocation.FORGE_BASIN, new RouteWaypoint[]{new RouteWaypoint(-9, 144, -20, TransportMethod.WALK)});
                this.put(SubLocation.THE_FORGE, new RouteWaypoint[]{new RouteWaypoint(44, 134, 21, TransportMethod.WALK), new RouteWaypoint(58, 197, -11, TransportMethod.WALK), new RouteWaypoint(171, 149, 33, TransportMethod.WALK), new RouteWaypoint(-75, 152, -11, TransportMethod.WALK), new RouteWaypoint(-131, 173, -52, TransportMethod.WALK)});
                this.put(SubLocation.CLIFFSIDE_VEINS, new RouteWaypoint[]{new RouteWaypoint(93, 144, 51, TransportMethod.WALK)});
                this.put(SubLocation.ROYAL_MINES, new RouteWaypoint[]{new RouteWaypoint(115, 153, 83, TransportMethod.WALK)});
                this.put(SubLocation.GREAT_ICE_WALL, new RouteWaypoint[]{new RouteWaypoint(0, 127, 143, TransportMethod.WALK)});
                this.put(SubLocation.GOBLIN_BURROWS, new RouteWaypoint[]{new RouteWaypoint(-56, 134, 153, TransportMethod.WALK)});
                this.put(SubLocation.RAMPARTS_QUARRY, new RouteWaypoint[]{new RouteWaypoint(-41, 138, -13, TransportMethod.WALK), new RouteWaypoint(-58, 146, -18, TransportMethod.WALK)});
                this.put(SubLocation.UPPER_MINES, new RouteWaypoint[]{new RouteWaypoint(-111, 166, -74, TransportMethod.WALK), new RouteWaypoint(-145, 206, -30, TransportMethod.WALK)});
                this.put(SubLocation.TREASURE_HUNTER_CAMP, new RouteWaypoint[]{new RouteWaypoint(-115, 204, -53, TransportMethod.WALK)});
                this.put(SubLocation.LAVA_SPRINGS, new RouteWaypoint[]{new RouteWaypoint(53, 197, -24, TransportMethod.WALK)});
            }
        };
        VEINS = Collections.unmodifiableMap(veinsMap);
    }
}

