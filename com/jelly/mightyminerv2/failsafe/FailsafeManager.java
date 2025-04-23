/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 */
package com.jelly.mightyminerv2.failsafe;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.BlockChangeEvent;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.BadEffectFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.BedrockCheckFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.DisconnectFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.ItemChangeFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.KnockbackFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.RotationFailsafe;
import com.jelly.mightyminerv2.failsafe.impl.TeleportFailsafe;
import com.jelly.mightyminerv2.feature.FeatureManager;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.StrafeUtil;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class FailsafeManager {
    private static FailsafeManager instance;
    private final Minecraft mc = Minecraft.func_71410_x();
    private Clock timer = new Clock();
    public final List<AbstractFailsafe> failsafes = new ArrayList<AbstractFailsafe>();
    public Optional<AbstractFailsafe> triggeredFailsafe = Optional.empty();
    public final Queue<AbstractFailsafe> emergencyQueue = new PriorityQueue<AbstractFailsafe>(Comparator.comparing(AbstractFailsafe::getPriority));
    public Set<AbstractFailsafe.Failsafe> failsafesToIgnore = new HashSet<AbstractFailsafe.Failsafe>();

    public static FailsafeManager getInstance() {
        if (instance == null) {
            instance = new FailsafeManager();
        }
        return instance;
    }

    public FailsafeManager() {
        this.failsafes.addAll(Arrays.asList(BadEffectFailsafe.getInstance(), DisconnectFailsafe.getInstance(), ItemChangeFailsafe.getInstance(), KnockbackFailsafe.getInstance(), TeleportFailsafe.getInstance(), RotationFailsafe.getInstance(), BedrockCheckFailsafe.getInstance()));
    }

    public void stopFailsafes() {
        this.triggeredFailsafe = Optional.empty();
        this.emergencyQueue.clear();
    }

    public boolean shouldNotCheckForFailsafe() {
        return !MacroManager.getInstance().isRunning() || this.triggeredFailsafe.isPresent();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        ArrayList<AbstractFailsafe> failsafeCopy = new ArrayList<AbstractFailsafe>(this.failsafes);
        this.failsafesToIgnore.clear();
        this.failsafesToIgnore.addAll(FeatureManager.getInstance().getFailsafesToIgnore());
        for (AbstractFailsafe failsafe : failsafeCopy) {
            if (this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) || !failsafe.onTick(event)) continue;
            this.emergencyQueue.add(failsafe);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onBlockChange(BlockChangeEvent event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        this.failsafes.forEach(failsafe -> {
            if (!this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) && failsafe.onBlockChange(event)) {
                this.emergencyQueue.add((AbstractFailsafe)failsafe);
            }
        });
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Received event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        this.failsafes.forEach(failsafe -> {
            if (!this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) && failsafe.onPacketReceive(event)) {
                this.emergencyQueue.add((AbstractFailsafe)failsafe);
            }
        });
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        this.failsafes.forEach(failsafe -> {
            if (!this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) && failsafe.onChat(event)) {
                this.emergencyQueue.add((AbstractFailsafe)failsafe);
            }
        });
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onWorldUnload(WorldEvent.Unload event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        this.failsafes.forEach(failsafe -> {
            if (!this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) && failsafe.onWorldUnload(event)) {
                this.emergencyQueue.add((AbstractFailsafe)failsafe);
            }
        });
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        this.failsafes.forEach(failsafe -> {
            if (!this.failsafesToIgnore.contains((Object)failsafe.getFailsafeType()) && failsafe.onDisconnect(event)) {
                this.emergencyQueue.add((AbstractFailsafe)failsafe);
            }
        });
    }

    public void removeFailsafeFromQueue(AbstractFailsafe failsafe) {
        boolean removed = this.emergencyQueue.remove(failsafe);
        if (removed) {
            System.out.println("Successfully removed failsafe: " + (Object)((Object)failsafe.getFailsafeType()));
        } else {
            System.out.println("Failsafe not found in the queue: " + (Object)((Object)failsafe.getFailsafeType()));
        }
    }

    @SubscribeEvent
    public void onTickChooseEmergency(TickEvent.ClientTickEvent event) {
        if (this.shouldNotCheckForFailsafe()) {
            return;
        }
        if (this.triggeredFailsafe.isPresent()) {
            return;
        }
        if (this.emergencyQueue.isEmpty()) {
            return;
        }
        StrafeUtil.forceStop = true;
        if (!this.timer.isScheduled()) {
            this.timer.schedule(MightyMinerConfig.failsafeToggleDelay);
        } else if (this.timer.passed()) {
            this.triggeredFailsafe = Optional.ofNullable(this.emergencyQueue.peek());
            this.emergencyQueue.clear();
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onTickReact(TickEvent.ClientTickEvent event) {
        if (!this.triggeredFailsafe.isPresent()) {
            return;
        }
        if (this.triggeredFailsafe.get().react()) {
            StrafeUtil.forceStop = false;
            this.triggeredFailsafe = Optional.empty();
            this.emergencyQueue.clear();
            this.failsafes.forEach(AbstractFailsafe::resetStates);
        }
    }

    public boolean isFailsafeActive(AbstractFailsafe.Failsafe failsafe) {
        System.out.println("failsafequeue: " + this.emergencyQueue);
        return this.emergencyQueue.stream().anyMatch(it -> it.getFailsafeType().equals((Object)failsafe));
    }
}

