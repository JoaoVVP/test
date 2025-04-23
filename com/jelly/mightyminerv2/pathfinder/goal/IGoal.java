/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package com.jelly.mightyminerv2.pathfinder.goal;

import kotlin.Metadata;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H&J \u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H&\u00a8\u0006\n"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/goal/IGoal;", "", "heuristic", "", "x", "", "y", "z", "isAtGoal", "", "MightyMinerV2"})
public interface IGoal {
    public boolean isAtGoal(int var1, int var2, int var3);

    public double heuristic(int var1, int var2, int var3);
}

