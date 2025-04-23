/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util.helper;

import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Target {
    private Vec3 vec;
    private Entity entity;
    private BlockPos blockPos;
    private Angle angle;
    private float additionalY = (float)(1.0 + Math.random()) * 0.75f;

    public Target(Vec3 vec) {
        this.vec = vec;
    }

    public Target(Entity entity) {
        this.entity = entity;
    }

    public Target(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public Target(Angle angle) {
        this.angle = angle;
    }

    public Angle getTargetAngle() {
        if (this.blockPos != null) {
            return AngleUtil.getRotation(this.blockPos);
        }
        if (this.vec != null) {
            return AngleUtil.getRotation(this.vec);
        }
        if (this.entity != null) {
            return AngleUtil.getRotation(this.entity.func_174791_d().func_72441_c(0.0, (double)this.additionalY, 0.0));
        }
        return this.angle;
    }

    public String toString() {
        return "Vec3: " + this.vec + ", Ent: " + (this.entity != null ? Integer.valueOf(this.entity.func_145782_y()) : "null") + ", Pos: " + this.blockPos + ", Angle: " + this.angle;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Angle getAngle() {
        return this.angle;
    }

    public Target additionalY(float additionalY) {
        this.additionalY = additionalY;
        return this;
    }

    public float additionalY() {
        return this.additionalY;
    }
}

