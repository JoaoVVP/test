/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.BlockMiner;
import com.jelly.mightyminerv2.util.InventoryUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Pair;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoInventory
extends AbstractFeature {
    private static AutoInventory instance;
    private Task mainTask = Task.NONE;
    private int[] speedBoostValues = new int[2];
    private SB sbState = SB.STARTING;
    private SBError sbError = SBError.NONE;
    private MoveState moveState = MoveState.STARTING;
    private MoveError moveError = MoveError.NONE;
    private Queue<String> elementsToSwap = new LinkedList<String>();
    private Queue<Integer> availableSlots = new LinkedList<Integer>();

    public static AutoInventory getInstance() {
        if (instance == null) {
            instance = new AutoInventory();
        }
        return instance;
    }

    public AutoInventory() {
        this.failsafesToIgnore = new ArrayList<AbstractFailsafe.Failsafe>(Arrays.asList(AbstractFailsafe.Failsafe.ITEM_CHANGE));
    }

    @Override
    public String getName() {
        return "AutoInventory";
    }

    @Override
    public void resetStatesAfterStop() {
        this.mainTask = Task.NONE;
        this.sbState = SB.STARTING;
        this.moveState = MoveState.STARTING;
    }

    @Override
    @SubscribeEvent
    protected void onTick(TickEvent.ClientTickEvent event) {
        if (!this.enabled) {
            return;
        }
        switch (this.mainTask) {
            case NONE: {
                this.stop();
                break;
            }
            case GET_SPEED_BOOST: {
                this.handleGetSpeedBoost();
                break;
            }
            case MOVE_ITEMS_TO_HOTBAR: {
                this.handleMoveItems();
            }
        }
    }

    public void retrieveSpeedBoost() {
        this.mainTask = Task.GET_SPEED_BOOST;
        this.sbError = SBError.NONE;
        this.speedBoostValues = new int[2];
        this.enabled = true;
        this.start();
    }

    public int[] getSpeedBoostValues() {
        return this.speedBoostValues;
    }

    public boolean sbSucceeded() {
        return !this.enabled && this.sbError == SBError.NONE;
    }

    public SBError getSbError() {
        return this.sbError;
    }

    private void swapSbState(SB state, int time) {
        this.sbState = state;
        this.timer.schedule(time);
    }

    private void handleGetSpeedBoost() {
        switch (this.sbState) {
            case STARTING: {
                this.swapSbState(SB.OPEN_MENU, MightyMinerConfig.getRandomGuiWaitDelay());
                break;
            }
            case OPEN_MENU: {
                this.mc.field_71439_g.func_71165_d("/sbmenu");
                this.swapSbState(SB.GET_SPEED, 2000);
                break;
            }
            case GET_SPEED: {
                if (this.hasTimerEnded()) {
                    this.stop();
                    this.sbError = SBError.CANNOT_GET_VALUE;
                    this.error("Could Not Get Speed In Time.");
                    break;
                }
                if (!(this.mc.field_71462_r instanceof GuiChest) || !InventoryUtil.getInventoryName().equals("SkyBlock Menu") || !InventoryUtil.isInventoryLoaded()) break;
                List<String> loreList = InventoryUtil.getItemLoreFromOpenContainer("Your SkyBlock Profile");
                for (String lore : loreList) {
                    if (!lore.contains("Mining Speed")) continue;
                    try {
                        String[] splitValues = lore.replace(",", "").split(" ");
                        this.speedBoostValues[0] = (int)Float.parseFloat(splitValues[splitValues.length - 1]);
                        this.swapSbState(SB.OPEN_HOTM_MENU, MightyMinerConfig.getRandomGuiWaitDelay());
                        return;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        this.log("Couldn't parse value properly.");
                    }
                }
                this.stop();
                this.sbError = SBError.CANNOT_GET_VALUE;
                this.error("Could not get mining speed from profile.");
                break;
            }
            case OPEN_HOTM_MENU: {
                if (this.isTimerRunning()) break;
                if (InventoryUtil.getInventoryName().equals("SkyBlock Menu")) {
                    InventoryUtil.clickContainerSlot(InventoryUtil.getSlotIdOfItemInContainer("Heart of the Mountain"), InventoryUtil.ClickType.LEFT, InventoryUtil.ClickMode.PICKUP);
                } else {
                    this.log("Menu Name Is NOT SkyBlock Menu");
                    this.mc.field_71439_g.func_71165_d("/hotm");
                }
                this.swapSbState(SB.GET_SPEED_BOOST, 2000);
                break;
            }
            case GET_SPEED_BOOST: {
                if (this.hasTimerEnded()) {
                    this.stop();
                    this.sbError = SBError.CANNOT_OPEN_INV;
                    this.error("Could Not Open HOTM Inventory in Time.");
                    break;
                }
                if (!(this.mc.field_71439_g.field_71070_bA instanceof ContainerChest) || !InventoryUtil.getInventoryName().equals("Heart of the Mountain") || !InventoryUtil.isInventoryLoaded()) break;
                int speedBoostSlot = InventoryUtil.getSlotIdOfItemInContainer("Mining Speed Boost");
                String speedBoostLore = String.join((CharSequence)" ", InventoryUtil.getLoreOfItemInContainer(speedBoostSlot));
                Matcher matcher = Pattern.compile("\\+(\\d+)%").matcher(speedBoostLore);
                if (matcher.find()) {
                    this.speedBoostValues[1] = Integer.parseInt(matcher.group(1));
                    if (BlockMiner.getBoostState() == BlockMiner.BoostState.ACTIVE) {
                        this.speedBoostValues[0] = (int)((double)this.speedBoostValues[0] / ((double)this.speedBoostValues[1] / 100.0));
                    }
                    this.swapSbState(SB.END, 500);
                    break;
                }
                this.stop();
                this.sbError = SBError.CANNOT_GET_VALUE;
                this.error("Could Not Get Speed Boost Value.");
                break;
            }
            case END: {
                if (this.isTimerRunning()) break;
                InventoryUtil.closeScreen();
                this.stop();
            }
        }
    }

    public void moveItems(Collection<String> items) {
        if (items.isEmpty()) {
            return;
        }
        this.availableSlots.clear();
        this.elementsToSwap.clear();
        Pair<List<Integer>, List<String>> itemsToMove = InventoryUtil.getAvailableHotbarSlots(items);
        this.availableSlots.addAll((Collection)itemsToMove.getFirst());
        this.elementsToSwap.addAll((Collection)itemsToMove.getSecond());
        if (this.elementsToSwap.isEmpty()) {
            this.log("No Elements to move");
            return;
        }
        if (this.availableSlots.size() < this.elementsToSwap.size()) {
            this.error("Not enough slots to move items to. Disabling");
            this.moveError = MoveError.NOT_ENOUGH_HOTBAR_SPACE;
            return;
        }
        this.mainTask = Task.MOVE_ITEMS_TO_HOTBAR;
        this.moveError = MoveError.NONE;
        this.enabled = true;
        this.start();
        this.log("Started moving items into hotbar");
    }

    private void changeMoveState(MoveState to, int time) {
        this.moveState = to;
        this.timer.schedule(time);
    }

    public boolean moveFailed() {
        return !this.enabled && this.moveError != MoveError.NONE;
    }

    public MoveError getMoveError() {
        return this.moveError;
    }

    private void handleMoveItems() {
        if (this.timer.isScheduled() && !this.timer.passed()) {
            return;
        }
        switch (this.moveState) {
            case STARTING: {
                InventoryUtil.openInventory();
                this.changeMoveState(MoveState.SWAP_SLOTS, MightyMinerConfig.getRandomGuiWaitDelay());
                this.log("Opened Inventory");
                break;
            }
            case SWAP_SLOTS: {
                if (!this.elementsToSwap.isEmpty() && !this.availableSlots.isEmpty()) {
                    InventoryUtil.swapSlots(InventoryUtil.getSlotIdOfItemInContainer(this.elementsToSwap.poll()), this.availableSlots.poll());
                    this.changeMoveState(MoveState.SWAP_SLOTS, MightyMinerConfig.getRandomGuiWaitDelay());
                } else {
                    this.changeMoveState(MoveState.FINISH, 0);
                }
                this.log("Swapped Item");
                break;
            }
            case FINISH: {
                InventoryUtil.closeScreen();
                if (!this.elementsToSwap.isEmpty()) {
                    this.moveError = MoveError.NOT_ENOUGH_HOTBAR_SPACE;
                }
                this.stop();
                this.log("Closed");
            }
        }
    }

    public static enum MoveError {
        NONE,
        NOT_ENOUGH_HOTBAR_SPACE;

    }

    static enum MoveState {
        STARTING,
        SWAP_SLOTS,
        FINISH;

    }

    public static enum SBError {
        NONE,
        CANNOT_OPEN_INV,
        CANNOT_GET_VALUE;

    }

    static enum SB {
        STARTING,
        OPEN_MENU,
        GET_SPEED,
        OPEN_HOTM_MENU,
        GET_SPEED_BOOST,
        END;

    }

    static enum Task {
        NONE,
        GET_SPEED_BOOST,
        MOVE_ITEMS_TO_HOTBAR;

    }
}

