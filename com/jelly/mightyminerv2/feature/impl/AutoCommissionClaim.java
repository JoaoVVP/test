/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.macro.commissionmacro.helper.Commission;
import com.jelly.mightyminerv2.util.CommissionUtil;
import com.jelly.mightyminerv2.util.EntityUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoCommissionClaim
extends AbstractFeature {
    private static AutoCommissionClaim instance;
    private State state = State.STARTING;
    private ClaimError claimError = ClaimError.NONE;
    private Optional<EntityPlayer> emissary = Optional.empty();
    private List<Commission> nextComm = new ArrayList<Commission>();
    private int retry = 0;

    public static AutoCommissionClaim getInstance() {
        if (instance == null) {
            instance = new AutoCommissionClaim();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "AutoCommissionClaim";
    }

    @Override
    public void start() {
        this.enabled = true;
        this.nextComm = null;
        this.claimError = ClaimError.NONE;
    }

    @Override
    public void stop() {
        if (!this.enabled) {
            return;
        }
        this.enabled = false;
        this.emissary = Optional.empty();
        this.timer.reset();
        this.resetStatesAfterStop();
        this.send("AutoCommissionClaim Stopped");
    }

    @Override
    public void resetStatesAfterStop() {
        this.state = State.STARTING;
        this.retry = 0;
    }

    @Override
    public boolean shouldNotCheckForFailsafe() {
        return true;
    }

    public void stop(ClaimError error) {
        this.claimError = error;
        this.stop();
    }

    public boolean succeeded() {
        return !this.enabled && this.claimError == ClaimError.NONE;
    }

    public ClaimError claimError() {
        return this.claimError;
    }

    public List<Commission> getNextComm() {
        return this.nextComm;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        if (this.retry > 3) {
            this.log("Tried too many times but failed. stopping");
            this.stop(ClaimError.INACCESSIBLE_NPC);
            return;
        }
        switch (this.state) {
            case STARTING: {
                int time = 400;
                switch (MightyMinerConfig.commClaimMethod) {
                    case 0: {
                        time = 0;
                        break;
                    }
                    case 1: {
                        if (InventoryUtil.holdItem("Royal Pigeon")) break;
                        this.stop(ClaimError.NO_ITEMS);
                        break;
                    }
                    case 2: {
                        if (InventoryUtil.holdItem("Abiphone")) break;
                        this.stop(ClaimError.NO_ITEMS);
                    }
                }
                this.swapState(State.ROTATING, time);
                break;
            }
            case ROTATING: {
                if (this.isTimerRunning()) {
                    return;
                }
                if (MightyMinerConfig.commClaimMethod == 0) {
                    this.emissary = CommissionUtil.getClosestEmissary();
                    if (!this.emissary.isPresent()) {
                        this.stop(ClaimError.INACCESSIBLE_NPC);
                        this.error("Cannot Find Emissary. Stopping");
                        break;
                    }
                    if (this.mc.field_71439_g.func_70068_e((Entity)this.emissary.get()) > 16.0) {
                        this.stop(ClaimError.INACCESSIBLE_NPC);
                        this.error("Emissary is too far away.");
                        break;
                    }
                    RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target((Entity)this.emissary.get()), 500L, RotationConfiguration.RotationType.CLIENT, null));
                }
                this.swapState(State.OPENING, 2000);
                break;
            }
            case OPENING: {
                if (this.hasTimerEnded()) {
                    this.stop(ClaimError.TIMEOUT);
                    this.error("Could not finish rotation in time.");
                    break;
                }
                Optional<Entity> entityLookingAt = EntityUtil.getEntityLookingAt();
                int time = 5000;
                switch (MightyMinerConfig.commClaimMethod) {
                    case 0: {
                        if (RotationHandler.getInstance().isEnabled() || !entityLookingAt.isPresent()) {
                            return;
                        }
                        if (entityLookingAt.equals(this.emissary)) {
                            KeyBindUtil.leftClick();
                            break;
                        }
                        this.mc.field_71442_b.func_78768_b((EntityPlayer)this.mc.field_71439_g, (Entity)this.emissary.get());
                        break;
                    }
                    case 1: {
                        KeyBindUtil.rightClick();
                    }
                    case 2: {
                        time = 0;
                    }
                }
                this.log("Scheduler timer for : " + time);
                this.swapState(State.VERIFYING_GUI, time);
                break;
            }
            case VERIFYING_GUI: {
                if (this.hasTimerEnded()) {
                    this.stop(ClaimError.INACCESSIBLE_NPC);
                    this.error("Opened a Different Inventory Named: " + InventoryUtil.getInventoryName());
                    break;
                }
                switch (MightyMinerConfig.commClaimMethod) {
                    case 0: 
                    case 1: {
                        if (this.mc.field_71439_g.field_71070_bA instanceof ContainerChest) {
                            if (!InventoryUtil.getInventoryName().contains("Commissions")) break;
                            this.swapState(State.CLAIMING, 500);
                            break;
                        } else {
                            break;
                        }
                    }
                }
                break;
            }
            case CLAIMING: {
                State nextState;
                if (this.isTimerRunning()) break;
                int slotToClick = CommissionUtil.getClaimableCommissionSlot();
                if (slotToClick != -1) {
                    InventoryUtil.clickContainerSlot(slotToClick, InventoryUtil.ClickType.LEFT, InventoryUtil.ClickMode.PICKUP);
                    nextState = State.CLAIMING;
                } else {
                    this.log("No Commission To Claim");
                    nextState = State.NEXT_COMM;
                }
                this.swapState(nextState, MightyMinerConfig.getRandomGuiWaitDelay());
                break;
            }
            case NEXT_COMM: {
                if (this.isTimerRunning()) break;
                if (this.mc.field_71439_g.field_71070_bA instanceof ContainerChest) {
                    this.nextComm = CommissionUtil.getCommissionFromContainer((ContainerChest)this.mc.field_71439_g.field_71070_bA);
                }
                this.swapState(State.ENDING, 0);
                break;
            }
            case ENDING: {
                if (this.isTimerRunning()) {
                    return;
                }
                InventoryUtil.closeScreen();
                this.stop();
            }
        }
    }

    @SubscribeEvent
    protected void onChat(ClientChatReceivedEvent event) {
        if (!this.enabled || this.state != State.CLAIMING || event.type != 0) {
            return;
        }
        String mess = event.message.func_150260_c();
        if (mess.startsWith("This ability is on cooldown for ")) {
            ++this.retry;
            this.log("Pigeon Cooldown Detected, Waiting for 5 Seconds");
            this.swapState(State.OPENING, 5000);
        }
    }

    private void swapState(State state, int time) {
        this.state = state;
        if (time == 0) {
            this.timer.reset();
        } else {
            this.timer.schedule(time);
        }
    }

    public static enum ClaimError {
        NONE,
        INACCESSIBLE_NPC,
        NO_ITEMS,
        TIMEOUT;

    }

    static enum State {
        STARTING,
        ROTATING,
        OPENING,
        VERIFYING_GUI,
        CLAIMING,
        NEXT_COMM,
        ENDING;

    }
}

