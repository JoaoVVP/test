/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.enums.EnumEntries
 *  kotlin.enums.EnumEntriesKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementResult;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementAscend;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementDescend;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementDiagonal;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementTraverse;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0011\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!\u00a8\u0006\""}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "", "offsetX", "", "offsetZ", "(Ljava/lang/String;III)V", "getOffsetX", "()I", "getOffsetZ", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "TRAVERSE_NORTH", "TRAVERSE_SOUTH", "TRAVERSE_EAST", "TRAVERSE_WEST", "ASCEND_NORTH", "ASCEND_SOUTH", "ASCEND_EAST", "ASCEND_WEST", "DESCEND_NORTH", "DESCEND_SOUTH", "DESCEND_EAST", "DESCEND_WEST", "DIAGONAL_NORTHEAST", "DIAGONAL_NORTHWEST", "DIAGONAL_SOUTHEAST", "DIAGONAL_SOUTHWEST", "MightyMinerV2"})
public abstract class Moves
extends Enum<Moves> {
    private final int offsetX;
    private final int offsetZ;
    public static final /* enum */ Moves TRAVERSE_NORTH = new TRAVERSE_NORTH("TRAVERSE_NORTH", 0);
    public static final /* enum */ Moves TRAVERSE_SOUTH = new TRAVERSE_SOUTH("TRAVERSE_SOUTH", 1);
    public static final /* enum */ Moves TRAVERSE_EAST = new TRAVERSE_EAST("TRAVERSE_EAST", 2);
    public static final /* enum */ Moves TRAVERSE_WEST = new TRAVERSE_WEST("TRAVERSE_WEST", 3);
    public static final /* enum */ Moves ASCEND_NORTH = new ASCEND_NORTH("ASCEND_NORTH", 4);
    public static final /* enum */ Moves ASCEND_SOUTH = new ASCEND_SOUTH("ASCEND_SOUTH", 5);
    public static final /* enum */ Moves ASCEND_EAST = new ASCEND_EAST("ASCEND_EAST", 6);
    public static final /* enum */ Moves ASCEND_WEST = new ASCEND_WEST("ASCEND_WEST", 7);
    public static final /* enum */ Moves DESCEND_NORTH = new DESCEND_NORTH("DESCEND_NORTH", 8);
    public static final /* enum */ Moves DESCEND_SOUTH = new DESCEND_SOUTH("DESCEND_SOUTH", 9);
    public static final /* enum */ Moves DESCEND_EAST = new DESCEND_EAST("DESCEND_EAST", 10);
    public static final /* enum */ Moves DESCEND_WEST = new DESCEND_WEST("DESCEND_WEST", 11);
    public static final /* enum */ Moves DIAGONAL_NORTHEAST = new DIAGONAL_NORTHEAST("DIAGONAL_NORTHEAST", 12);
    public static final /* enum */ Moves DIAGONAL_NORTHWEST = new DIAGONAL_NORTHWEST("DIAGONAL_NORTHWEST", 13);
    public static final /* enum */ Moves DIAGONAL_SOUTHEAST = new DIAGONAL_SOUTHEAST("DIAGONAL_SOUTHEAST", 14);
    public static final /* enum */ Moves DIAGONAL_SOUTHWEST = new DIAGONAL_SOUTHWEST("DIAGONAL_SOUTHWEST", 15);
    private static final /* synthetic */ Moves[] $VALUES;
    private static final /* synthetic */ EnumEntries $ENTRIES;

    private Moves(int offsetX, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
    }

    public final int getOffsetX() {
        return this.offsetX;
    }

    public final int getOffsetZ() {
        return this.offsetZ;
    }

    public abstract void calculate(@NotNull CalculationContext var1, int var2, int var3, int var4, @NotNull MovementResult var5);

    public static Moves[] values() {
        return (Moves[])$VALUES.clone();
    }

    public static Moves valueOf(String value) {
        return Enum.valueOf(Moves.class, value);
    }

    @NotNull
    public static EnumEntries<Moves> getEntries() {
        return $ENTRIES;
    }

    public /* synthetic */ Moves(String $enum$name, int $enum$ordinal, int offsetX, int offsetZ, DefaultConstructorMarker $constructor_marker) {
        this(offsetX, offsetZ);
    }

    static {
        $VALUES = movesArray = new Moves[]{Moves.TRAVERSE_NORTH, Moves.TRAVERSE_SOUTH, Moves.TRAVERSE_EAST, Moves.TRAVERSE_WEST, Moves.ASCEND_NORTH, Moves.ASCEND_SOUTH, Moves.ASCEND_EAST, Moves.ASCEND_WEST, Moves.DESCEND_NORTH, Moves.DESCEND_SOUTH, Moves.DESCEND_EAST, Moves.DESCEND_WEST, Moves.DIAGONAL_NORTHEAST, Moves.DIAGONAL_NORTHWEST, Moves.DIAGONAL_SOUTHEAST, Moves.DIAGONAL_SOUTHWEST};
        $ENTRIES = EnumEntriesKt.enumEntries((Enum[])$VALUES);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$ASCEND_EAST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class ASCEND_EAST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        ASCEND_EAST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementAscend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$ASCEND_NORTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class ASCEND_NORTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        ASCEND_NORTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementAscend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$ASCEND_SOUTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class ASCEND_SOUTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        ASCEND_SOUTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementAscend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$ASCEND_WEST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class ASCEND_WEST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        ASCEND_WEST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementAscend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DESCEND_EAST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DESCEND_EAST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DESCEND_EAST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDescend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DESCEND_NORTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DESCEND_NORTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DESCEND_NORTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDescend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DESCEND_SOUTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DESCEND_SOUTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DESCEND_SOUTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDescend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DESCEND_WEST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DESCEND_WEST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DESCEND_WEST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDescend.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DIAGONAL_NORTHEAST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DIAGONAL_NORTHEAST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DIAGONAL_NORTHEAST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDiagonal.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DIAGONAL_NORTHWEST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DIAGONAL_NORTHWEST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DIAGONAL_NORTHWEST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDiagonal.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DIAGONAL_SOUTHEAST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DIAGONAL_SOUTHEAST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DIAGONAL_SOUTHEAST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDiagonal.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$DIAGONAL_SOUTHWEST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class DIAGONAL_SOUTHWEST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        DIAGONAL_SOUTHWEST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementDiagonal.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$TRAVERSE_EAST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class TRAVERSE_EAST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        TRAVERSE_EAST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementTraverse.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$TRAVERSE_NORTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class TRAVERSE_NORTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        TRAVERSE_NORTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementTraverse.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$TRAVERSE_SOUTH;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class TRAVERSE_SOUTH
    extends Moves {
        /*
         * WARNING - void declaration
         */
        TRAVERSE_SOUTH() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementTraverse.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016\u00a8\u0006\f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/Moves$TRAVERSE_WEST;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Moves;", "calculate", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "parentX", "", "parentY", "parentZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "MightyMinerV2"})
    static final class TRAVERSE_WEST
    extends Moves {
        /*
         * WARNING - void declaration
         */
        TRAVERSE_WEST() {
            void var1_1;
        }

        @Override
        public void calculate(@NotNull CalculationContext ctx, int parentX, int parentY, int parentZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            MovementTraverse.Companion.calculateCost(ctx, parentX, parentY, parentZ, parentX + this.getOffsetX(), parentZ + this.getOffsetZ(), res);
        }
    }
}

