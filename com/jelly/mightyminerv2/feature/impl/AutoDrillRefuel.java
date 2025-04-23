/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.util.MathHelper
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.util.EntityUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.KeyBindUtil;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoDrillRefuel
extends AbstractFeature {
    private static final AutoDrillRefuel instance = new AutoDrillRefuel();
    private String drillToRefill = "";
    private int fuelIndex = -1;
    private boolean getFuelFromSack = false;
    private boolean useNpc = false;
    private Entity npc = null;
    private State state = State.STARTING;
    private Error failReason = Error.NONE;
    private int requiredFuelAmount = 0;
    private boolean movedSuccessfully = false;
    private final String[] fuelList = new String[]{"Enchanted Poppy", "Goblin Egg", "Green Goblin Egg", "Yellow Goblin Egg", "Red Goblin Egg", "Blue Goblin Egg", "Volta", "Oil Barrel"};
    private final Map<String, Integer> fuelAmount = new HashMap<String, Integer>(){
        {
            this.put("Enchanted Poppy", 1000);
            this.put("Goblin Egg", 3000);
            this.put("Green Goblin Egg", 3000);
            this.put("Yellow Goblin Egg", 3000);
            this.put("Red Goblin Egg", 3000);
            this.put("Blue Goblin Egg", 3000);
            this.put("Volta", 3000);
            this.put("Oil Barrel", 3000);
        }
    };

    public static AutoDrillRefuel getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "AutoDrillRefuel";
    }

    @Override
    public void resetStatesAfterStop() {
        this.state = State.STARTING;
        this.failsafesToIgnore.remove((Object)AbstractFailsafe.Failsafe.ITEM_CHANGE);
    }

    public void start(String drillName, int fuelIndex, boolean getFuelFromSack, boolean useNpc) {
        if (InventoryUtil.getSlotIdOfItemInContainer(drillName) == -1) {
            this.error("Can't refill because no drill - dumbass");
            this.stop(Error.FAILED_REFILL);
            return;
        }
        this.failReason = Error.NONE;
        this.drillToRefill = drillName;
        this.fuelIndex = fuelIndex;
        this.getFuelFromSack = getFuelFromSack;
        this.useNpc = useNpc;
        this.enabled = true;
        this.log("Started refuel for " + this.drillToRefill + " with " + this.fuelList[fuelIndex]);
    }

    @Override
    public void stop() {
        this.enabled = false;
        this.drillToRefill = "";
        this.fuelIndex = -1;
        this.getFuelFromSack = false;
        this.useNpc = false;
        this.npc = null;
        this.requiredFuelAmount = 0;
        this.movedSuccessfully = false;
        this.resetStatesAfterStop();
    }

    public void stop(Error failReason) {
        this.note("Stopped because " + (Object)((Object)failReason));
        this.failReason = failReason;
        this.stop();
    }

    public void swapState(State to, int time) {
        this.state = to;
        if (time == 0) {
            this.timer.reset();
        } else {
            this.timer.schedule(time);
        }
    }

    public boolean hasSucceeded() {
        return this.failReason == Error.NONE;
    }

    public Error getFailReason() {
        return this.failReason;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        switch (this.state) {
            case STARTING: {
                this.swapState(State.LOCATING_ITEMS, 0);
                break;
            }
            case LOCATING_ITEMS: {
                int capacity = InventoryUtil.getDrillFuelCapacity(this.drillToRefill);
                if (capacity == -1) {
                    this.error("Cannot find drill fuel capacity. stopping");
                    this.stop(Error.FAILED_REFILL);
                    return;
                }
                String chosenFuel = this.fuelList[this.fuelIndex];
                this.requiredFuelAmount = MathHelper.func_76123_f((float)((float)capacity / (float)this.fuelAmount.get(chosenFuel).intValue()));
                if (InventoryUtil.getAmountOfItemInInventory(chosenFuel) >= this.requiredFuelAmount) {
                    this.swapState(State.ROTATING, 0);
                    break;
                }
                this.swapState(State.FETCHING_ITEMS, 0);
                break;
            }
            case FETCHING_ITEMS: {
                int time = 2000;
                if (this.getFuelFromSack) {
                    this.mc.field_71439_g.func_71165_d("/gfs " + this.fuelList[this.fuelIndex] + " " + this.requiredFuelAmount);
                } else {
                    time = 0;
                }
                this.swapState(State.VERIFYING_ITEM_FETCH, time);
                break;
            }
            case VERIFYING_ITEM_FETCH: {
                if (this.getFuelFromSack) {
                    if (this.hasTimerEnded()) {
                        this.error("Couldnt Get Fuel From Sack in time");
                        this.stop(Error.FAILED_REFILL);
                        break;
                    }
                    if (!this.movedSuccessfully) break;
                    this.swapState(State.ROTATING, 0);
                    break;
                }
                this.swapState(State.ROTATING, 0);
                break;
            }
            case ROTATING: {
                if (this.useNpc) {
                    this.npc = this.mc.field_71441_e.field_73010_i.stream().filter(entity -> entity.field_70165_t == -6.5 && entity.field_70163_u == 145.0 && entity.field_70161_v == -18.5 && !entity.func_70005_c_().contains("Sentry") && EntityUtil.isNpc((Entity)entity)).findFirst().orElse(null);
                    if (this.npc == null || this.mc.field_71439_g.func_70068_e(this.npc) > 9.0) {
                        this.stop(Error.INACCESSIBLE_MECHANIC);
                        this.error("Cannot find NPC");
                        break;
                    }
                    RotationHandler.getInstance().easeTo(new RotationConfiguration(new Target(this.npc), 300L, null));
                }
                this.swapState(State.OPENING_MECHANICS_GUI, 0);
                break;
            }
            case OPENING_MECHANICS_GUI: {
                int time = 2000;
                if (this.useNpc) {
                    Optional<Entity> entityLookingAt = EntityUtil.getEntityLookingAt();
                    if (RotationHandler.getInstance().isEnabled() && !entityLookingAt.isPresent()) {
                        return;
                    }
                    if (entityLookingAt.isPresent() && entityLookingAt.get().equals((Object)this.npc)) {
                        KeyBindUtil.leftClick();
                    } else {
                        this.mc.field_71442_b.func_78768_b((EntityPlayer)this.mc.field_71439_g, this.npc);
                    }
                } else {
                    time = 0;
                }
                this.failsafesToIgnore.add(AbstractFailsafe.Failsafe.ITEM_CHANGE);
                this.swapState(State.GUI_VERIFY, time);
                break;
            }
            case GUI_VERIFY: {
                if (this.useNpc && this.hasTimerEnded()) {
                    this.stop(Error.INACCESSIBLE_MECHANIC);
                    break;
                }
                if (!InventoryUtil.getInventoryName().contains("Drill Anvil") || !InventoryUtil.isInventoryLoaded()) break;
                this.log("Opened Anvil GUI");
                this.swapState(State.PUTTING_ITEMS, 500);
                break;
            }
            case PUTTING_ITEMS: {
                if (this.isTimerRunning()) break;
                this.log("IN PUTTING ITEMS");
                int lowerChestSize = ((ContainerChest)this.mc.field_71439_g.field_71070_bA).func_85151_d().func_70302_i_();
                int slotToClick = InventoryUtil.getSlotIdOfItemInContainer(this.drillToRefill);
                if (slotToClick != -1 && slotToClick >= lowerChestSize || (slotToClick = InventoryUtil.getSlotIdOfItemInContainer(this.fuelList[this.fuelIndex])) != -1 && slotToClick >= lowerChestSize) {
                    InventoryUtil.clickContainerSlot(slotToClick, InventoryUtil.ClickType.LEFT, InventoryUtil.ClickMode.QUICK_MOVE);
                    this.log("Put item");
                    this.swapState(State.PUTTING_ITEMS, 500);
                    break;
                }
                this.swapState(State.RETRIEVING_DRILL, 500);
                break;
            }
            case RETRIEVING_DRILL: {
                if (this.isTimerRunning()) break;
                this.log("retrieving drill");
                int drillSlot = InventoryUtil.getSlotIdOfItemInContainer(this.drillToRefill);
                this.log("drill slot: " + drillSlot);
                if (drillSlot == -1) {
                    this.error("No drill in inventory for some reason - should never happen but if it does then :thumbsupcat:");
                    this.stop(Error.FAILED_REFILL);
                    break;
                }
                int lowerChestSize = ((ContainerChest)this.mc.field_71439_g.field_71070_bA).func_85151_d().func_70302_i_();
                this.log("lowerChestSize: " + lowerChestSize);
                if (drillSlot < lowerChestSize) {
                    this.log("drillslot < lowerchestsize. retrieving");
                    InventoryUtil.clickContainerSlot(InventoryUtil.getSlotIdOfItemInContainer("Drill Anvil"), InventoryUtil.ClickType.LEFT, InventoryUtil.ClickMode.PICKUP);
                    this.swapState(State.RETRIEVING_DRILL, 500);
                    break;
                }
                this.log("ending");
                this.swapState(State.WAITING, 300);
                break;
            }
            case WAITING: {
                if (this.isTimerRunning()) {
                    return;
                }
                InventoryUtil.closeScreen();
                this.swapState(State.ENDING, 300);
                break;
            }
            case ENDING: {
                if (this.isTimerRunning()) {
                    return;
                }
                this.stop();
                this.log("Succeeded or failed");
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!this.enabled || !this.getFuelFromSack || event.type != 0) {
            return;
        }
        String message = event.message.func_150260_c();
        if (message.equals("Moved " + this.requiredFuelAmount + " " + this.fuelList[this.fuelIndex] + " from your Sacks to your inventory.")) {
            this.movedSuccessfully = true;
        }
    }

    public static enum Error {
        NONE,
        INACCESSIBLE_MECHANIC,
        FAILED_REFILL;

    }

    static enum State {
        STARTING,
        LOCATING_ITEMS,
        FETCHING_ITEMS,
        VERIFYING_ITEM_FETCH,
        ROTATING,
        OPENING_MECHANICS_GUI,
        GUI_VERIFY,
        PUTTING_ITEMS,
        RETRIEVING_DRILL,
        WAITING,
        ENDING;

    }
}

