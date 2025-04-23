/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.util.helper;

public class Angle {
    public float yaw;
    public float pitch;

    public Angle(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setRotation(Angle rotation) {
        this.yaw = rotation.getYaw();
        this.pitch = rotation.getPitch();
    }

    public float getValue() {
        return Math.abs(this.yaw) + Math.abs(this.pitch);
    }

    public float lengthSqrt() {
        return (float)Math.sqrt(this.yaw * this.yaw + this.pitch * this.pitch);
    }

    public String toString() {
        return "Rotation{yaw=" + this.yaw + ", pitch=" + this.pitch + "}";
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

