/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util.helper.route;

import com.google.gson.annotations.Expose;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class RouteWaypoint {
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int z;
    @Expose
    private TransportMethod transportMethod;

    public RouteWaypoint() {
    }

    public RouteWaypoint(int x, int y, int z, TransportMethod transportMethod) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.transportMethod = transportMethod;
    }

    public RouteWaypoint(BlockPos pos, TransportMethod transportMethod) {
        this(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), transportMethod);
    }

    public Vec3 toVec3() {
        return new Vec3((double)this.x, (double)this.y, (double)this.z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z + "," + this.transportMethod.name();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public TransportMethod getTransportMethod() {
        return this.transportMethod;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setTransportMethod(TransportMethod transportMethod) {
        this.transportMethod = transportMethod;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RouteWaypoint)) {
            return false;
        }
        RouteWaypoint other = (RouteWaypoint)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        return this.getZ() == other.getZ();
    }

    protected boolean canEqual(Object other) {
        return other instanceof RouteWaypoint;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getZ();
        return result;
    }
}

