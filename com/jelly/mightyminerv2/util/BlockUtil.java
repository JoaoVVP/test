/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package com.jelly.mightyminerv2.util;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.pathfinder.helper.BlockStateAccessor;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementHelper;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RaytracingUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.heap.MinHeap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import kotlin.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static final Map<EnumFacing, float[]> BLOCK_SIDES = new HashMap<EnumFacing, float[]>(){
        {
            this.put(EnumFacing.DOWN, new float[]{0.5f, 0.01f, 0.5f});
            this.put(EnumFacing.UP, new float[]{0.5f, 0.99f, 0.5f});
            this.put(EnumFacing.WEST, new float[]{0.01f, 0.5f, 0.5f});
            this.put(EnumFacing.EAST, new float[]{0.99f, 0.5f, 0.5f});
            this.put(EnumFacing.NORTH, new float[]{0.5f, 0.5f, 0.01f});
            this.put(EnumFacing.SOUTH, new float[]{0.5f, 0.5f, 0.99f});
            this.put(null, new float[]{0.5f, 0.5f, 0.5f});
        }
    };

    public static BlockPos getBlockLookingAt() {
        return BlockUtil.mc.field_71476_x.func_178782_a();
    }

    public static List<BlockPos> getWalkableBlocksAround(BlockPos playerPos) {
        ArrayList<BlockPos> walkableBlocks = new ArrayList<BlockPos>();
        BlockStateAccessor bsa = new BlockStateAccessor((World)BlockUtil.mc.field_71441_e);
        int yOffset = MovementHelper.INSTANCE.isBottomSlab(bsa.get(playerPos.func_177958_n(), playerPos.func_177956_o(), playerPos.func_177952_p())) ? -1 : 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = yOffset; j <= 0; ++j) {
                for (int k = -1; k <= 1; ++k) {
                    int z;
                    int y;
                    int x = playerPos.func_177958_n() + i;
                    if (!MovementHelper.INSTANCE.canStandOn(bsa, x, y = playerPos.func_177956_o() + j, z = playerPos.func_177952_p() + k, bsa.get(x, y, z)) || !MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 1, z, bsa.get(x, y + 1, z)) || !MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 2, z, bsa.get(x, y + 2, z))) continue;
                    walkableBlocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return walkableBlocks;
    }

    public static List<BlockPos> getBestMineableBlocksAround(Map<Integer, Integer> stateIds, int[] priority, BlockPos blockToIgnore, int miningSpeed) {
        MinHeap<BlockPos> blocks = new MinHeap<BlockPos>(500);
        HashSet<Long> visitedPositions = new HashSet<Long>();
        List<BlockPos> walkableBlocks = BlockUtil.getWalkableBlocksAround(PlayerUtil.getBlockStandingOn());
        if (blockToIgnore != null) {
            visitedPositions.add(BlockUtil.longHash(blockToIgnore.func_177958_n(), blockToIgnore.func_177956_o(), blockToIgnore.func_177952_p()));
        }
        for (BlockPos blockPos : walkableBlocks) {
            Vec3 blockCenter = new Vec3((Vec3i)blockPos).func_72441_c(0.5, (double)BlockUtil.mc.field_71439_g.eyeHeight, 0.5);
            for (int x = -5; x < 6; ++x) {
                for (int z = -5; z < 6; ++z) {
                    for (int y = -3; y < 5; ++y) {
                        int stateID;
                        Integer index;
                        BlockPos pos = blockPos.func_177982_a(x, y + 2, z);
                        long hash = BlockUtil.longHash(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
                        if (visitedPositions.contains(hash) || (index = stateIds.get(stateID = Block.func_176210_f((IBlockState)BlockUtil.mc.field_71441_e.func_180495_p(pos)))) == null) continue;
                        if (blockCenter.func_72438_d(new Vec3((Vec3i)pos)) > 4.0 || !BlockUtil.hasVisibleSide(pos)) continue;
                        double hardness = BlockUtil.getBlockStrength(stateID);
                        float angleChange = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(pos)).lengthSqrt();
                        visitedPositions.add(hash);
                        blocks.add(pos, (hardness * 1500.0 / (double)miningSpeed + (double)(angleChange * (float)MightyMinerConfig.devMithRot)) / (double)priority[index]);
                    }
                }
            }
        }
        return blocks.getBlocks();
    }

    public static long longHash(int x, int y, int z) {
        long hash = 3241L;
        hash = 3457689L * hash + (long)x;
        hash = 8734625L * hash + (long)y;
        hash = 2873465L * hash + (long)z;
        return hash;
    }

    public static List<BlockPos> getBestMineableBlocks(Map<Integer, Integer> stateIds, int[] priority, BlockPos blockToIgnore, int miningSpeed) {
        MinHeap<BlockPos> blocks = new MinHeap<BlockPos>(500);
        BlockPos blockPos = PlayerUtil.getBlockStandingOn().func_177982_a(0, 2, 0);
        Vec3 posEyes = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
        for (int x = -5; x < 6; ++x) {
            for (int z = -5; z < 6; ++z) {
                for (int y = -3; y < 5; ++y) {
                    BlockPos pos = blockPos.func_177982_a(x, y, z);
                    int stateID = Block.func_176210_f((IBlockState)BlockUtil.mc.field_71441_e.func_180495_p(pos));
                    Integer index = stateIds.get(stateID);
                    if (index == null) continue;
                    if (posEyes.func_72438_d(new Vec3((Vec3i)pos)) > 4.0 || !BlockUtil.hasVisibleSide(pos)) continue;
                    double hardness = BlockUtil.getBlockStrength(stateID);
                    float angleChange = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(pos)).lengthSqrt();
                    blocks.add(pos, (hardness * 1500.0 / (double)miningSpeed + (double)(angleChange * (float)MightyMinerConfig.devMithRot)) / (double)priority[index]);
                }
            }
        }
        return blocks.getBlocks();
    }

    public static List<Pair<BlockPos, List<Float>>> getBestMineableBlocksAroundDebug(Map<Integer, Integer> stateIds, int[] priority, BlockPos blockToIgnore, int miningSpeed) {
        ArrayList<Pair<BlockPos, List<Float>>> debugs = new ArrayList<Pair<BlockPos, List<Float>>>();
        HashSet<Long> visitedPositions = new HashSet<Long>();
        List<BlockPos> walkableBlocks = BlockUtil.getWalkableBlocksAround(PlayerUtil.getBlockStandingOn());
        if (blockToIgnore != null) {
            visitedPositions.add(BlockUtil.longHash(blockToIgnore.func_177958_n(), blockToIgnore.func_177956_o(), blockToIgnore.func_177952_p()));
        }
        for (BlockPos blockPos : walkableBlocks) {
            Vec3 blockCenter = new Vec3((Vec3i)blockPos).func_72441_c(0.5, (double)BlockUtil.mc.field_71439_g.eyeHeight, 0.5);
            for (int x = -3; x < 4; ++x) {
                for (int z = -3; z < 4; ++z) {
                    for (int y = -3; y < 4; ++y) {
                        int stateID;
                        Integer index;
                        BlockPos pos = blockPos.func_177982_a(x, y + 2, z);
                        long hash = BlockUtil.longHash(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
                        if (visitedPositions.contains(hash) || (index = stateIds.get(stateID = Block.func_176210_f((IBlockState)BlockUtil.mc.field_71441_e.func_180495_p(pos)))) == null || blockCenter.func_72438_d(new Vec3((Vec3i)pos)) > 4.0 || !BlockUtil.hasVisibleSide(pos)) continue;
                        double hardness = BlockUtil.getBlockStrength(stateID);
                        Angle ang = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(pos));
                        float angleChange = ang.lengthSqrt();
                        visitedPositions.add(hash);
                        debugs.add((Pair<BlockPos, List<Float>>)new Pair((Object)pos, Arrays.asList(Float.valueOf((float)(hardness * 1500.0 * (double)priority[index]) / (float)miningSpeed), Float.valueOf(angleChange * (float)MightyMinerConfig.devMithRot / (float)priority[index]), Float.valueOf(ang.yaw), Float.valueOf(ang.pitch))));
                    }
                }
            }
        }
        debugs.sort(Comparator.comparing(it -> Float.valueOf(((Float)((List)it.getSecond()).get(0)).floatValue() + ((Float)((List)it.getSecond()).get(1)).floatValue())));
        return debugs;
    }

    public static List<Pair<BlockPos, List<Float>>> getBestMithrilBlocksDebug(Map<Integer, Integer> stateIds, int[] priority, int speed) {
        BlockPos playerHeadPos = PlayerUtil.getBlockStandingOn().func_177982_a(0, 2, 0);
        Vec3 posEyes = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
        ArrayList<Pair<BlockPos, List<Float>>> debugs = new ArrayList<Pair<BlockPos, List<Float>>>();
        for (int x = -4; x < 5; ++x) {
            for (int z = -4; z < 5; ++z) {
                for (int y = -3; y < 5; ++y) {
                    BlockPos pos = playerHeadPos.func_177982_a(x, y, z);
                    IBlockState state = BlockUtil.mc.field_71441_e.func_180495_p(pos);
                    int stateID = Block.func_176210_f((IBlockState)state);
                    Integer index = stateIds.get(stateID);
                    if (index == null || posEyes.func_72438_d(new Vec3((Vec3i)pos)) > 4.0 || !BlockUtil.hasVisibleSide(pos)) continue;
                    double hardness = BlockUtil.getBlockStrength(stateID);
                    float angleChange = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(pos)).getValue();
                    debugs.add((Pair<BlockPos, List<Float>>)new Pair((Object)pos, Arrays.asList(Float.valueOf((float)(hardness * 1500.0 * (double)priority[index] / (double)speed)), Float.valueOf(angleChange * 3.0f / (float)priority[index]))));
                }
            }
        }
        debugs.sort(Comparator.comparing(it -> ((List)it.getSecond()).stream().reduce(Float.valueOf(0.0f), Float::sum)));
        return debugs;
    }

    public static int getBlockStrength(int stateID) {
        switch (stateID) {
            case 22: 
            case 41: 
            case 42: 
            case 57: 
            case 133: 
            case 152: 
            case 173: {
                return 600;
            }
            case 19: {
                return 500;
            }
            case 1: {
                return 50;
            }
            case 16385: {
                return 2000;
            }
            case 28707: {
                return 500;
            }
            case 12323: {
                return 1500;
            }
            case 37023: {
                return 500;
            }
            case 168: 
            case 4264: 
            case 8360: {
                return 800;
            }
            case 95: 
            case 160: 
            case 16479: 
            case 16544: {
                return 3800;
            }
            case 4191: 
            case 4256: 
            case 12383: 
            case 12448: 
            case 20575: 
            case 20640: 
            case 41055: 
            case 41120: {
                return 3000;
            }
            case 8287: 
            case 8352: {
                return 4800;
            }
            case 45151: 
            case 45216: 
            case 49247: 
            case 49312: 
            case 53343: 
            case 53408: 
            case 61535: 
            case 61600: {
                return 5200;
            }
            case 57439: 
            case 57504: {
                return 2300;
            }
        }
        return 5000;
    }

    public static int getMiningTime(int stateId, int miningSpeed) {
        return (int)Math.ceil((float)(BlockUtil.getBlockStrength(stateId) * 30) / (float)miningSpeed) + MightyMinerConfig.mithrilMinerTickGlideOffset;
    }

    public static Vec3 getSidePos(BlockPos block, EnumFacing face) {
        float[] offset = BLOCK_SIDES.get(face);
        return new Vec3((double)((float)block.func_177958_n() + offset[0]), (double)((float)block.func_177956_o() + offset[1]), (double)((float)block.func_177952_p() + offset[2]));
    }

    public static boolean canSeeSide(BlockPos block, EnumFacing side) {
        return RaytracingUtil.canSeePoint(BlockUtil.getSidePos(block, side));
    }

    public static boolean canSeeSide(Vec3 from, BlockPos block, EnumFacing side) {
        return RaytracingUtil.canSeePoint(from, BlockUtil.getSidePos(block, side));
    }

    public static List<EnumFacing> getAllVisibleSides(BlockPos block) {
        ArrayList<EnumFacing> sides = new ArrayList<EnumFacing>();
        for (EnumFacing face : BLOCK_SIDES.keySet()) {
            if (face != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(face), face) || !BlockUtil.canSeeSide(block, face)) continue;
            sides.add(face);
        }
        return sides;
    }

    public static List<EnumFacing> getAllVisibleSides(Vec3 from, BlockPos block) {
        ArrayList<EnumFacing> sides = new ArrayList<EnumFacing>();
        for (EnumFacing face : BLOCK_SIDES.keySet()) {
            if (face != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(face), face) || !BlockUtil.canSeeSide(from, block, face)) continue;
            sides.add(face);
        }
        return sides;
    }

    public static Vec3 getClosestVisibleSidePos(BlockPos block) {
        EnumFacing face = null;
        if (BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            Vec3 eyePos = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
            double dist = Double.MAX_VALUE;
            for (EnumFacing side : BLOCK_SIDES.keySet()) {
                if (side != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side)) continue;
                double distanceToThisSide = eyePos.func_72438_d(BlockUtil.getSidePos(block, side));
                if (!BlockUtil.canSeeSide(block, side) || !(distanceToThisSide < dist) || side == null && face != null) continue;
                dist = distanceToThisSide;
                face = side;
            }
        }
        float[] offset = BLOCK_SIDES.get(face);
        return new Vec3((double)((float)block.func_177958_n() + offset[0]), (double)((float)block.func_177956_o() + offset[1]), (double)((float)block.func_177952_p() + offset[2]));
    }

    public static Vec3 getClosestVisibleSidePos(Vec3 from, BlockPos block) {
        EnumFacing face = null;
        if (BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            double dist = Double.MAX_VALUE;
            for (EnumFacing side : BLOCK_SIDES.keySet()) {
                if (side != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side)) continue;
                double distanceToThisSide = from.func_72438_d(BlockUtil.getSidePos(block, side));
                if (!BlockUtil.canSeeSide(from, block, side) || !(distanceToThisSide < dist) || side == null && face != null) continue;
                dist = distanceToThisSide;
                face = side;
            }
        }
        float[] offset = BLOCK_SIDES.get(face);
        return new Vec3((double)((float)block.func_177958_n() + offset[0]), (double)((float)block.func_177956_o() + offset[1]), (double)((float)block.func_177952_p() + offset[2]));
    }

    public static EnumFacing getClosestVisibleSide(BlockPos block) {
        if (!BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            return null;
        }
        Vec3 eyePos = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
        double dist = Double.MAX_VALUE;
        EnumFacing face = null;
        for (EnumFacing side : BLOCK_SIDES.keySet()) {
            if (side != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side)) continue;
            double distanceToThisSide = eyePos.func_72438_d(BlockUtil.getSidePos(block, side));
            if (!BlockUtil.canSeeSide(block, side) || !(distanceToThisSide < dist) || side == null && face != null) continue;
            dist = distanceToThisSide;
            face = side;
        }
        return face;
    }

    public static EnumFacing getClosestVisibleSide(Vec3 from, BlockPos block) {
        if (!BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            return null;
        }
        double dist = Double.MAX_VALUE;
        EnumFacing face = null;
        for (EnumFacing side : BLOCK_SIDES.keySet()) {
            if (side != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side)) continue;
            double distanceToThisSide = from.func_72438_d(BlockUtil.getSidePos(block, side));
            if (!BlockUtil.canSeeSide(from, block, side) || !(distanceToThisSide < dist) || side == null && face != null) continue;
            dist = distanceToThisSide;
            face = side;
        }
        return face;
    }

    public static boolean hasVisibleSide(BlockPos block) {
        if (!BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            return false;
        }
        for (EnumFacing side : EnumFacing.values()) {
            if (side != null && !BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side) || !BlockUtil.canSeeSide(block, side)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasVisibleSide(Vec3 from, BlockPos block) {
        if (!BlockUtil.mc.field_71441_e.func_175665_u(block)) {
            return false;
        }
        for (EnumFacing side : EnumFacing.values()) {
            if (!BlockUtil.mc.field_71441_e.func_180495_p(block).func_177230_c().func_176225_a((IBlockAccess)BlockUtil.mc.field_71441_e, block.func_177972_a(side), side) || !BlockUtil.canSeeSide(from, block, side)) continue;
            return true;
        }
        return false;
    }

    public static List<Vec3> bestPointsOnBestSide(BlockPos block) {
        return BlockUtil.pointsOnBlockSide(block, BlockUtil.getClosestVisibleSide(block)).stream().filter(RaytracingUtil::canSeePoint).sorted(Comparator.comparingDouble(i -> AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(i)).getValue())).collect(Collectors.toList());
    }

    public static List<Vec3> bestPointsOnBestSide(Vec3 from, BlockPos block) {
        return BlockUtil.pointsOnBlockSide(block, BlockUtil.getClosestVisibleSide(from, block)).stream().filter(it -> RaytracingUtil.canSeePoint(from, it)).sorted(Comparator.comparingDouble(i -> AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(from, i)).getValue())).collect(Collectors.toList());
    }

    public static List<Vec3> bestPointsOnVisibleSides(BlockPos block) {
        return BlockUtil.pointsOnVisibleSides(block).stream().filter(RaytracingUtil::canSeePoint).sorted(Comparator.comparingDouble(arg_0 -> ((Vec3)BlockUtil.mc.field_71439_g.func_174824_e(1.0f)).func_72438_d(arg_0))).collect(Collectors.toList());
    }

    public static List<Vec3> bestPointsOnVisibleSides(Vec3 from, BlockPos block) {
        return BlockUtil.pointsOnVisibleSides(block).stream().filter(it -> RaytracingUtil.canSeePoint(from, it)).sorted(Comparator.comparingDouble(arg_0 -> ((Vec3)from).func_72438_d(arg_0))).collect(Collectors.toList());
    }

    private static List<Vec3> pointsOnVisibleSides(BlockPos block) {
        ArrayList<Vec3> points = new ArrayList<Vec3>();
        for (EnumFacing side : BlockUtil.getAllVisibleSides(block)) {
            points.addAll(BlockUtil.pointsOnBlockSide(block, side));
        }
        return points;
    }

    private static List<Vec3> pointsOnVisibleSides(Vec3 from, BlockPos block) {
        ArrayList<Vec3> points = new ArrayList<Vec3>();
        for (EnumFacing side : BlockUtil.getAllVisibleSides(from, block)) {
            points.addAll(BlockUtil.pointsOnBlockSide(block, side));
        }
        return points;
    }

    private static List<Vec3> pointsOnBlockSide(BlockPos block, EnumFacing side) {
        HashSet<Vec3> points = new HashSet<Vec3>();
        if (side != null) {
            float[] it = BLOCK_SIDES.get(side);
            for (int i = 0; i < 20; ++i) {
                Vec3 point;
                float x = it[0];
                float y = it[1];
                float z = it[2];
                if (x == 0.5f) {
                    x = BlockUtil.randomVal();
                }
                if (y == 0.5f) {
                    y = BlockUtil.randomVal();
                }
                if (z == 0.5f) {
                    z = BlockUtil.randomVal();
                }
                if (points.contains(point = new Vec3((Vec3i)block).func_72441_c((double)x, (double)y, (double)z))) continue;
                points.add(point);
            }
        } else {
            for (float[] bside : BLOCK_SIDES.values()) {
                for (int i = 0; i < 20; ++i) {
                    Vec3 point;
                    float x = bside[0];
                    float y = bside[1];
                    float z = bside[2];
                    if (x == 0.5f) {
                        x = BlockUtil.randomVal();
                    }
                    if (y == 0.5f) {
                        y = BlockUtil.randomVal();
                    }
                    if (z == 0.5f) {
                        z = BlockUtil.randomVal();
                    }
                    if (points.contains(point = new Vec3((Vec3i)block).func_72441_c((double)x, (double)y, (double)z))) continue;
                    points.add(point);
                }
            }
        }
        return new ArrayList<Vec3>(points);
    }

    private static float randomVal() {
        return (float)(new Random().nextInt(6) + 2) / 10.0f;
    }

    public static boolean canWalkBetween(CalculationContext ctx, BlockPos start2, BlockPos end) {
        int ey = end.func_177956_o();
        int ex = end.func_177958_n();
        int ez = end.func_177952_p();
        IBlockState endState = ctx.get(ex, ey, ez);
        if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), ex, ey, ez, endState)) {
            return false;
        }
        if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), ex, ey + 1, ez, ctx.get(ex, ey + 1, ez))) {
            return false;
        }
        if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), ex, ey + 2, ez, ctx.get(ex, ey + 2, ez))) {
            return false;
        }
        return !com.jelly.mightyminerv2.pathfinder.util.BlockUtil.INSTANCE.bresenham(ctx, start2, end);
    }

    public static boolean canWalkBetween(CalculationContext ctx, Vec3 start2, Vec3 end) {
        int ey = MathHelper.func_76128_c((double)end.field_72448_b);
        int ex = MathHelper.func_76128_c((double)end.field_72450_a);
        int ez = MathHelper.func_76128_c((double)end.field_72449_c);
        IBlockState endState = ctx.get(ex, ey, ez);
        if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), ex, ey, ez, endState)) {
            return false;
        }
        if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), ex, ey + 1, ez, ctx.get(ex, ey + 1, ez))) {
            return false;
        }
        if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), ex, ey + 2, ez, ctx.get(ex, ey + 2, ez))) {
            return false;
        }
        return !com.jelly.mightyminerv2.pathfinder.util.BlockUtil.INSTANCE.bresenham(ctx, start2, end);
    }

    public static boolean canStandOn(BlockPos pos) {
        int z;
        int y;
        BlockStateAccessor bsa = new BlockStateAccessor((World)BlockUtil.mc.field_71441_e);
        int x = pos.func_177958_n();
        return MovementHelper.INSTANCE.canStandOn(bsa, x, y = pos.func_177956_o(), z = pos.func_177952_p(), bsa.get(x, y, z)) && MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 1, z, bsa.get(x, y + 1, z)) && MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 2, z, bsa.get(x, y + 2, z));
    }
}

