/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementResult;
import kotlin.Metadata;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H&J\b\u0010\u0016\u001a\u00020\u0003H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\t\u00a8\u0006\u0017"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/IMovement;", "", "costs", "", "getCosts", "()D", "dest", "Lnet/minecraft/util/BlockPos;", "getDest", "()Lnet/minecraft/util/BlockPos;", "mm", "Lcom/jelly/mightyminerv2/MightyMiner;", "getMm", "()Lcom/jelly/mightyminerv2/MightyMiner;", "source", "getSource", "calculateCost", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "getCost", "MightyMinerV2"})
public interface IMovement {
    @NotNull
    public MightyMiner getMm();

    @NotNull
    public BlockPos getSource();

    @NotNull
    public BlockPos getDest();

    public double getCosts();

    public double getCost();

    public void calculateCost(@NotNull CalculationContext var1, @NotNull MovementResult var2);
}

