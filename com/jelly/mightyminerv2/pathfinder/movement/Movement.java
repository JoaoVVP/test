/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.pathfinder.movement.IMovement;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u000b\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0013\u001a\u00020\tH\u0016R\u001a\u0010\b\u001a\u00020\tX\u0096\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0014\u0010\u0006\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000f\u00a8\u0006\u0014"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Movement;", "Lcom/jelly/mightyminerv2/pathfinder/movement/IMovement;", "mm", "Lcom/jelly/mightyminerv2/MightyMiner;", "source", "Lnet/minecraft/util/BlockPos;", "dest", "(Lcom/jelly/mightyminerv2/MightyMiner;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/BlockPos;)V", "costs", "", "getCosts", "()D", "setCosts", "(D)V", "getDest", "()Lnet/minecraft/util/BlockPos;", "getMm", "()Lcom/jelly/mightyminerv2/MightyMiner;", "getSource", "getCost", "MightyMinerV2"})
public abstract class Movement
implements IMovement {
    @NotNull
    private final MightyMiner mm;
    @NotNull
    private final BlockPos source;
    @NotNull
    private final BlockPos dest;
    private double costs;

    public Movement(@NotNull MightyMiner mm, @NotNull BlockPos source, @NotNull BlockPos dest) {
        Intrinsics.checkNotNullParameter((Object)mm, (String)"mm");
        Intrinsics.checkNotNullParameter((Object)source, (String)"source");
        Intrinsics.checkNotNullParameter((Object)dest, (String)"dest");
        this.mm = mm;
        this.source = source;
        this.dest = dest;
        this.costs = 1000000.0;
    }

    @Override
    @NotNull
    public MightyMiner getMm() {
        return this.mm;
    }

    @Override
    @NotNull
    public BlockPos getSource() {
        return this.source;
    }

    @Override
    @NotNull
    public BlockPos getDest() {
        return this.dest;
    }

    @Override
    public double getCosts() {
        return this.costs;
    }

    public void setCosts(double d) {
        this.costs = d;
    }

    @Override
    public double getCost() {
        return this.getCosts();
    }
}

