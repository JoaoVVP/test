/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.Pair
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.SourceDebugExtension
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.calculate.path;

import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.pathfinder.calculate.path.PathExecutor;
import com.jelly.mightyminerv2.pathfinder.util.RefKt;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.StrafeUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.Clock;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\u0004J\u0010\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0007J\u0010\u00100\u001a\u00020-2\u0006\u0010.\u001a\u000201H\u0007J\u000e\u00102\u001a\u00020-2\u0006\u0010\u0019\u001a\u00020\u001fJ\u0006\u00103\u001a\u00020\u0004J\u000e\u00103\u001a\u00020-2\u0006\u0010\u0019\u001a\u00020\u001fJ\u0006\u00104\u001a\u00020-J\u0006\u0010\"\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bRi\u0010\f\u001aZ\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e0\u00100\rj,\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e0\u0010`\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001a\u0010\"\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0006\"\u0004\b$\u0010\bR\u001a\u0010%\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0016\"\u0004\b'\u0010\u0018R\u0011\u0010(\u001a\u00020)\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+\u00a8\u00065"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/path/PathExecutor;", "", "()V", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "failed", "getFailed", "setFailed", "hashedPath", "Ljava/util/LinkedHashMap;", "Lkotlin/Pair;", "", "", "Lkotlin/collections/LinkedHashMap;", "getHashedPath", "()Ljava/util/LinkedHashMap;", "lastIndex", "getLastIndex", "()I", "setLastIndex", "(I)V", "path", "Lnet/minecraft/util/BlockPos;", "getPath", "()Ljava/util/List;", "pathQueue", "Ljava/util/LinkedList;", "Lcom/jelly/mightyminerv2/pathfinder/calculate/Path;", "getPathQueue", "()Ljava/util/LinkedList;", "succeeded", "getSucceeded", "setSucceeded", "targetIndex", "getTargetIndex", "setTargetIndex", "timer", "Lcom/jelly/mightyminerv2/util/helper/Clock;", "getTimer", "()Lcom/jelly/mightyminerv2/util/helper/Clock;", "onRender", "", "event", "Lnet/minecraftforge/client/event/RenderWorldLastEvent;", "onTick", "Lnet/minecraftforge/fml/common/gameevent/TickEvent$ClientTickEvent;", "queuePath", "start", "stop", "MightyMinerV2"})
@SourceDebugExtension(value={"SMAP\nPathExecutor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PathExecutor.kt\ncom/jelly/mightyminerv2/pathfinder/calculate/path/PathExecutor\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,205:1\n766#2:206\n857#2,2:207\n1963#2,14:209\n1855#2,2:223\n*S KotlinDebug\n*F\n+ 1 PathExecutor.kt\ncom/jelly/mightyminerv2/pathfinder/calculate/path/PathExecutor\n*L\n135#1:206\n135#1:207,2\n136#1:209,14\n202#1:223,2\n*E\n"})
public final class PathExecutor {
    @NotNull
    public static final PathExecutor INSTANCE = new PathExecutor();
    private static boolean enabled;
    @NotNull
    private static final LinkedList<Path> pathQueue;
    @NotNull
    private static final List<BlockPos> path;
    @NotNull
    private static final LinkedHashMap<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> hashedPath;
    private static int targetIndex;
    private static int lastIndex;
    @NotNull
    private static final Clock timer;
    private static boolean failed;
    private static boolean succeeded;

    private PathExecutor() {
    }

    public final boolean getEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean bl) {
        enabled = bl;
    }

    @NotNull
    public final LinkedList<Path> getPathQueue() {
        return pathQueue;
    }

    @NotNull
    public final List<BlockPos> getPath() {
        return path;
    }

    @NotNull
    public final LinkedHashMap<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> getHashedPath() {
        return hashedPath;
    }

    public final int getTargetIndex() {
        return targetIndex;
    }

    public final void setTargetIndex(int n) {
        targetIndex = n;
    }

    public final int getLastIndex() {
        return lastIndex;
    }

    public final void setLastIndex(int n) {
        lastIndex = n;
    }

    @NotNull
    public final Clock getTimer() {
        return timer;
    }

    public final boolean getFailed() {
        return failed;
    }

    public final void setFailed(boolean bl) {
        failed = bl;
    }

    public final boolean getSucceeded() {
        return succeeded;
    }

    public final void setSucceeded(boolean bl) {
        succeeded = bl;
    }

    public final void queuePath(@NotNull Path path) {
        Intrinsics.checkNotNullParameter((Object)path, (String)"path");
        if (path.getPath().isEmpty()) {
            Logger.sendMessage("Path is empty");
            return;
        }
        pathQueue.offer(path);
    }

    public final boolean start() {
        if (pathQueue.isEmpty()) {
            Logger.sendMessage("Path queue is empty. Not starting");
            failed = !enabled;
            succeeded = enabled;
            return false;
        }
        failed = false;
        succeeded = false;
        path.clear();
        hashedPath.clear();
        targetIndex = 0;
        lastIndex = -1;
        timer.reset();
        RotationHandler.getInstance().stop();
        List<BlockPos> smoothPath = pathQueue.poll().getSmoothedPath();
        path.addAll((Collection<BlockPos>)smoothPath);
        int n = smoothPath.size();
        for (int i = 0; i < n; ++i) {
            BlockPos block = smoothPath.get(i);
            hashedPath.computeIfAbsent((Pair<Integer, Integer>)new Pair((Object)block.func_177958_n(), (Object)block.func_177952_p()), arg_0 -> PathExecutor.start$lambda$0(start.1.INSTANCE, arg_0)).add(new Pair((Object)block.func_177956_o(), (Object)i));
        }
        enabled = true;
        return true;
    }

    public final void start(@NotNull Path path) {
        Intrinsics.checkNotNullParameter((Object)path, (String)"path");
        if (path.getPath().isEmpty()) {
            Logger.sendMessage("Path is empty");
            failed = true;
            return;
        }
        failed = false;
        succeeded = false;
        PathExecutor.path.clear();
        hashedPath.clear();
        List<BlockPos> smoothPath = path.getSmoothedPath();
        PathExecutor.path.addAll((Collection<BlockPos>)smoothPath);
        int n = smoothPath.size();
        for (int i = 0; i < n; ++i) {
            BlockPos block = smoothPath.get(i);
            hashedPath.computeIfAbsent((Pair<Integer, Integer>)new Pair((Object)block.func_177958_n(), (Object)block.func_177952_p()), arg_0 -> PathExecutor.start$lambda$1(start.2.INSTANCE, arg_0)).add(new Pair((Object)block.func_177956_o(), (Object)i));
        }
        enabled = true;
        Logger.sendMessage("Started PathExecutor");
    }

    public final void stop() {
        enabled = false;
        path.clear();
        hashedPath.clear();
        pathQueue.clear();
        targetIndex = 0;
        lastIndex = -1;
        timer.reset();
        StrafeUtil.enabled = false;
        KeyBindUtil.releaseAllExcept(new KeyBinding[0]);
        RotationHandler.getInstance().stop();
        Logger.sendMessage("Stopped PathExecutor");
    }

    /*
     * Unable to fully structure code
     */
    @SubscribeEvent
    public final void onTick(@NotNull TickEvent.ClientTickEvent event) {
        Intrinsics.checkNotNullParameter((Object)event, (String)"event");
        if (RefKt.getPlayer() == null || RefKt.getWorld() == null || !PathExecutor.enabled || RefKt.getMc().field_71462_r != null) {
            return;
        }
        if (RefKt.getPlayer().field_70159_w == 0.0 && RefKt.getPlayer().field_70179_y == 0.0) {
            if (PathExecutor.timer.isScheduled()) {
                if (PathExecutor.timer.passed()) {
                    PathExecutor.failed = true;
                    Logger.sendMessage("Stopped Moving for too long. Disabling");
                    this.stop();
                    return;
                }
            } else {
                PathExecutor.timer.schedule(1000L);
            }
        } else {
            PathExecutor.timer.reset();
        }
        if ((var3_2 = PathExecutor.hashedPath.get(new Pair((Object)MathHelper.func_76128_c((double)RefKt.getPlayer().field_70165_t), (Object)MathHelper.func_76128_c((double)RefKt.getPlayer().field_70161_v)))) == null) ** GOTO lbl-1000
        var5_4 = var3_2;
        $i$f$filter = false;
        var7_9 = $this$filter$iv;
        destination$iv$iv = new ArrayList<E>();
        $i$f$filterTo = false;
        for (T element$iv$iv : $this$filterTo$iv$iv) {
            it = (Pair)element$iv$iv;
            $i$a$-filter-PathExecutor$onTick$currentIndex$1 = false;
            if (!((double)((Number)it.getFirst()).intValue() < RefKt.getPlayer().field_70163_u)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        $i$f$filter = (List)destination$iv$iv;
        $i$f$maxByOrNull = false;
        iterator$iv = $this$maxByOrNull$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v0 = null;
        } else {
            maxElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v0 = maxElem$iv;
            } else {
                it = (Pair)maxElem$iv;
                $i$a$-maxByOrNull-PathExecutor$onTick$currentIndex$2 = false;
                maxValue$iv = ((Number)it.getFirst()).intValue();
                do {
                    e$iv = iterator$iv.next();
                    it = (Pair)e$iv;
                    $i$a$-maxByOrNull-PathExecutor$onTick$currentIndex$2 = false;
                    v$iv = ((Number)it.getFirst()).intValue();
                    if (maxValue$iv >= v$iv) continue;
                    maxElem$iv = e$iv;
                    maxValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v0 = maxElem$iv;
            }
        }
        $this$filter$iv = v0;
        if ($this$filter$iv != null) {
            v1 = (Integer)$this$filter$iv.getSecond();
        } else lbl-1000:
        // 2 sources

        {
            v1 = currentIndex = null;
        }
        if (currentIndex != null) {
            var3_3 = PathExecutor.lastIndex;
            if (currentIndex != var3_3) {
                Logger.sendLog("Standing On Node " + currentIndex);
                PathExecutor.lastIndex = currentIndex;
                PathExecutor.targetIndex = currentIndex + 1;
                RotationHandler.getInstance().stop();
                Logger.sendLog("Position Updated. LastPos: " + PathExecutor.lastIndex + ", CurrentPos: " + PathExecutor.targetIndex + ", PathSize: " + PathExecutor.path.size());
                if (PathExecutor.targetIndex == PathExecutor.path.size()) {
                    Logger.sendMessage("Path Traversed. Disabling");
                    PathExecutor.succeeded = true;
                    PathExecutor.failed = false;
                    if (!this.start()) {
                        this.stop();
                    }
                    return;
                }
            }
        }
        target = PathExecutor.path.get(PathExecutor.targetIndex);
        yaw = AngleUtil.get360RotationYaw(AngleUtil.getRotation((Vec3)RefKt.getPlayer().func_174791_d().func_72441_c((double)RefKt.getPlayer().field_70159_w, (double)0.0, (double)RefKt.getPlayer().field_70179_y), (Vec3)new Vec3((Vec3i)((Vec3i)target)).func_72441_c((double)0.5, (double)0.0, (double)0.5)).yaw);
        yawDiff = Math.abs(AngleUtil.get360RotationYaw() - yaw);
        if (yawDiff > 10.0f && !RotationHandler.getInstance().isEnabled()) {
            Logger.sendLog("Started Rotation. YawDiff: " + yawDiff);
            config = new RotationConfiguration(new Angle(yaw, 20.0f), 300L, null);
            config.easeFunction(RotationConfiguration.Ease.EASE_OUT_QUAD);
            RotationHandler.getInstance().easeTo(config);
        }
        StrafeUtil.enabled = true;
        StrafeUtil.yaw = yaw;
        shouldJump = RefKt.getPlayer().field_70122_E != false && Math.hypot(RefKt.getPlayer().field_70165_t - ((double)target.func_177958_n() + 0.5), RefKt.getPlayer().field_70161_v - ((double)target.func_177952_p() + 0.5)) <= 1.0 && (double)target.func_177956_o() >= RefKt.getPlayer().field_70163_u && Math.abs((double)target.func_177956_o() - RefKt.getPlayer().field_70163_u) < 0.1;
        KeyBindUtil.setKeyBindState(RefKt.getGameSettings().field_74351_w, true);
        KeyBindUtil.setKeyBindState(RefKt.getGameSettings().field_151444_V, yawDiff < 40.0f && shouldJump == false && RefKt.getPlayer().field_70122_E != false);
        if (shouldJump) {
            Logger.sendMessage("Jumping");
            RefKt.getPlayer().func_70664_aZ();
        }
    }

    public final boolean failed() {
        return !enabled && failed && !succeeded;
    }

    public final boolean succeeded() {
        return !enabled && !failed && succeeded;
    }

    @SubscribeEvent
    public final void onRender(@NotNull RenderWorldLastEvent event) {
        Intrinsics.checkNotNullParameter((Object)event, (String)"event");
        if (!enabled) {
            return;
        }
        Iterable $this$forEach$iv = path;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            BlockPos it = (BlockPos)element$iv;
            boolean bl = false;
            RenderUtil.drawBlock(it, new Color(0, 255, 255, 50));
        }
        RenderUtil.drawBlock(path.get(targetIndex), new Color(255, 0, 0, 100));
    }

    private static final List start$lambda$0(Function1 $tmp0, Object p0) {
        Intrinsics.checkNotNullParameter((Object)$tmp0, (String)"$tmp0");
        return (List)$tmp0.invoke(p0);
    }

    private static final List start$lambda$1(Function1 $tmp0, Object p0) {
        Intrinsics.checkNotNullParameter((Object)$tmp0, (String)"$tmp0");
        return (List)$tmp0.invoke(p0);
    }

    static {
        pathQueue = new LinkedList();
        path = new ArrayList();
        hashedPath = new LinkedHashMap();
        lastIndex = -1;
        timer = new Clock();
    }
}

