/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.macro.commissionmacro;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.UpdateTablistEvent;
import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.failsafe.FailsafeManager;
import com.jelly.mightyminerv2.feature.FeatureManager;
import com.jelly.mightyminerv2.feature.impl.AutoCommissionClaim;
import com.jelly.mightyminerv2.feature.impl.AutoDrillRefuel;
import com.jelly.mightyminerv2.feature.impl.AutoInventory;
import com.jelly.mightyminerv2.feature.impl.AutoMobKiller;
import com.jelly.mightyminerv2.feature.impl.AutoWarp;
import com.jelly.mightyminerv2.feature.impl.BlockMiner;
import com.jelly.mightyminerv2.feature.impl.RouteNavigator;
import com.jelly.mightyminerv2.handler.GameStateHandler;
import com.jelly.mightyminerv2.handler.GraphHandler;
import com.jelly.mightyminerv2.hud.CommissionHUD;
import com.jelly.mightyminerv2.macro.AbstractMacro;
import com.jelly.mightyminerv2.macro.commissionmacro.helper.Commission;
import com.jelly.mightyminerv2.util.CommissionUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.helper.MineableBlock;
import com.jelly.mightyminerv2.util.helper.location.Location;
import com.jelly.mightyminerv2.util.helper.location.SubLocation;
import com.jelly.mightyminerv2.util.helper.route.Route;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CommissionMacro
extends AbstractMacro {
    private static CommissionMacro instance = new CommissionMacro();
    private int commissionCounter = 0;
    private List<String> necessaryItems = new ArrayList<String>();
    private MainState mainState = MainState.NONE;
    private MacroState macroState = MacroState.STARTING;
    private boolean checkForCommissionChange = false;
    private int miningSpeed = 0;
    private int miningSpeedBoost = 0;
    private int macroRetries = 0;
    private List<Commission> curr = new ArrayList<Commission>();
    private final MineableBlock[] blocksToMine = new MineableBlock[]{MineableBlock.GRAY_MITHRIL, MineableBlock.GREEN_MITHRIL, MineableBlock.BLUE_MITHRIL, MineableBlock.TITANIUM};
    private final int[] mithrilPriority = new int[]{2, 3, 4, 1};
    private final int[] titaniumPriority = new int[]{2, 3, 4, 10};
    private ItemState itemState = ItemState.STARTING;
    private int itemRetries = 0;
    private WarpState warpState = WarpState.STARTING;
    private int warpRetries = 0;

    public static CommissionMacro getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "Commission Macro";
    }

    @Override
    public void onEnable() {
        this.changeMainState(MainState.MACRO);
        String miningTool = MightyMinerConfig.commMiningTool;
        if (miningTool.toLowerCase().contains("drill") || InventoryUtil.getFullName(miningTool).contains("Drill")) {
            int drillFuel = InventoryUtil.getDrillRemainingFuel(miningTool);
            this.note("Using Drill. Drill Fuel: " + drillFuel);
            if (drillFuel == 0) {
                this.note("DrillFuel = 0. MacroState: " + (Object)((Object)this.macroState) + ", Comms: " + this.curr);
                this.changeMacroState(MacroState.PATHING);
                this.curr.add(Commission.REFUEL);
            }
        }
        this.log("CommMacro::onEnable");
    }

    @Override
    public void onDisable() {
        this.mainState = MainState.NONE;
        this.warpState = WarpState.STARTING;
        this.itemState = ItemState.STARTING;
        this.macroState = MacroState.STARTING;
        this.warpRetries = 0;
        this.itemRetries = 0;
        this.macroRetries = 0;
        this.miningSpeedBoost = 0;
        this.miningSpeed = 0;
        this.curr = new ArrayList<Commission>();
        this.necessaryItems = new ArrayList<String>();
        this.checkForCommissionChange = false;
        if (CommissionHUD.getInstance().commHudResetStats) {
            this.commissionCounter = 0;
        }
        this.log("CommMacro::onDisable");
    }

    @Override
    public void onPause() {
        FeatureManager.getInstance().pauseAll();
        this.log("CommMacro::onPause");
    }

    @Override
    public void onResume() {
        FeatureManager.getInstance().resumeAll();
        this.log("CommMacro::onResume");
    }

    @Override
    public List<String> getNecessaryItems() {
        if (this.necessaryItems.isEmpty()) {
            ArrayList<String> items = new ArrayList<String>();
            items.add(MightyMinerConfig.commMiningTool);
            items.add(MightyMinerConfig.commSlayerWeapon);
            if (MightyMinerConfig.commClaimMethod == 2 || MightyMinerConfig.commDrillRefuel && !MightyMinerConfig.commMechaGuiAccessMethod) {
                items.add("Abiphone");
            }
            if (MightyMinerConfig.commClaimMethod == 1) {
                items.add("Royal Pigeon");
            }
            this.necessaryItems = items;
        }
        return this.necessaryItems;
    }

    public int getCompletedCommissions() {
        return this.commissionCounter;
    }

    public List<Commission> getActiveCommissions() {
        return this.curr;
    }

    public void stopActiveFeatures() {
        BlockMiner.getInstance().stop();
        AutoMobKiller.getInstance().stop();
    }

    private void changeMainState(MainState to, int timeToWait) {
        this.mainState = to;
        this.timer.schedule(timeToWait);
    }

    private void changeMainState(MainState to) {
        this.mainState = to;
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (this.isTimerRunning()) {
            return;
        }
        if (this.mainState == MainState.MACRO) {
            if (GameStateHandler.getInstance().getCurrentLocation() != Location.DWARVEN_MINES) {
                this.changeMainState(MainState.WARP, 0);
            } else if (!InventoryUtil.areItemsInInventory(this.getNecessaryItems()) && this.macroState != MacroState.REFUEL_VERIFY && !FailsafeManager.getInstance().isFailsafeActive(AbstractFailsafe.Failsafe.ITEM_CHANGE)) {
                this.error("Items Arent In Inventory And Failsafe Isnt Active");
                this.changeMainState(MainState.NONE);
            } else if (!InventoryUtil.areItemsInHotbar(this.getNecessaryItems()) && this.macroState != MacroState.REFUEL_VERIFY && !FailsafeManager.getInstance().isFailsafeActive(AbstractFailsafe.Failsafe.ITEM_CHANGE)) {
                this.changeMainState(MainState.ITEMS, 0);
            }
        }
        switch (this.mainState) {
            case NONE: {
                this.disable();
                break;
            }
            case WARP: {
                this.handleWarp();
                break;
            }
            case ITEMS: {
                this.handleItems();
                break;
            }
            case MACRO: {
                this.handleMacro();
            }
        }
    }

    @Override
    public void onChat(String message) {
        if (!this.isEnabled() || this.mainState != MainState.MACRO) {
            return;
        }
        if (message.contains("Commission Complete")) {
            this.curr.removeIf(it -> {
                if (message.equalsIgnoreCase(it.getName() + " Commission Complete! Visit the King to claim your rewards!")) {
                    ++this.commissionCounter;
                    this.log("Commission Complete Detected");
                    return true;
                }
                return false;
            });
            if (this.curr.isEmpty()) {
                this.log("No more commissions to complete.");
                this.curr.add(Commission.COMMISSION_CLAIM);
                this.stopActiveFeatures();
                this.changeMacroState(MacroState.PATHING);
            }
        }
        if (message.endsWith("is empty! Refuel it by talking to a Drill Mechanic!")) {
            if (!MightyMinerConfig.commDrillRefuel) {
                this.changeMainState(MainState.NONE);
                this.error("Drill Empty But Not Allowed to Refuel. Stopping");
                return;
            }
            this.curr.clear();
            this.curr.add(Commission.REFUEL);
            this.stopActiveFeatures();
            this.changeMacroState(MacroState.PATHING, 500);
        }
    }

    @Override
    public void onTablistUpdate(UpdateTablistEvent event) {
        if (!this.isEnabled() || this.mainState != MainState.MACRO || !this.checkForCommissionChange) {
            return;
        }
        List<Commission> comms = CommissionUtil.getCurrentCommissionsFromTablist();
        if (comms.size() != this.curr.size()) {
            this.curr = comms;
            this.log("Commission change detected from tablist");
            if (this.curr.contains((Object)Commission.COMMISSION_CLAIM)) {
                this.log("curr contains claim");
                this.stopActiveFeatures();
                this.checkForCommissionChange = false;
                this.changeMacroState(MacroState.STARTING);
            }
        }
    }

    private void changeMacroState(MacroState to, int timeToWait) {
        this.macroState = to;
        this.timer.schedule(timeToWait);
    }

    private void changeMacroState(MacroState to) {
        this.macroState = to;
    }

    public void handleMacro() {
        switch (this.macroState) {
            case STARTING: {
                this.changeMacroState(MacroState.CHECKING_COMMISSION);
                if (this.miningSpeed != 0 || this.miningSpeedBoost != 0) break;
                if (!InventoryUtil.holdItem(MightyMinerConfig.commMiningTool)) {
                    this.error("Something went wrong. Cannot hold mining tool");
                    this.changeMainState(MainState.NONE);
                    return;
                }
                this.changeMacroState(MacroState.CHECKING_STATS, 500);
                break;
            }
            case CHECKING_STATS: {
                AutoInventory.getInstance().retrieveSpeedBoost();
                this.changeMacroState(MacroState.GETTING_STATS);
                break;
            }
            case GETTING_STATS: {
                if (AutoInventory.getInstance().isRunning()) {
                    return;
                }
                if (AutoInventory.getInstance().sbSucceeded()) {
                    int[] sb = AutoInventory.getInstance().getSpeedBoostValues();
                    this.miningSpeed = sb[0];
                    this.miningSpeedBoost = sb[1];
                    this.macroRetries = 0;
                    this.changeMacroState(MacroState.STARTING);
                    this.log("MiningSpeed: " + this.miningSpeed + ", MiningSpeedBoost: " + this.miningSpeedBoost);
                    return;
                }
                switch (AutoInventory.getInstance().getSbError()) {
                    case NONE: {
                        throw new IllegalStateException("AutoInventory#getSbError failed but returned NONE");
                    }
                    case CANNOT_OPEN_INV: {
                        if (++this.macroRetries > 3) {
                            this.changeMainState(MainState.NONE);
                            this.error("Tried 3 times to open inv but failed. Stopping");
                            break;
                        }
                        this.changeMacroState(MacroState.STARTING);
                        this.log("Failed to open inventory. Retrying");
                        break;
                    }
                    case CANNOT_GET_VALUE: {
                        this.changeMainState(MainState.NONE);
                        this.error("Failed To Get Value. Follow Previous Instruction (If Any) or contact the developer.");
                    }
                }
                break;
            }
            case CHECKING_COMMISSION: {
                this.curr = CommissionUtil.getCurrentCommissionsFromTablist();
                this.changeMacroState(MacroState.PATHING);
                if (!this.curr.isEmpty()) {
                    this.macroRetries = 0;
                    return;
                }
                if (++this.macroRetries > 3) {
                    this.error("Tried > 3 times to retrieve current commission but failed. Disabling");
                    this.changeMainState(MainState.NONE);
                    break;
                }
                this.log("Could not find commission. Retrying");
                this.changeMacroState(MacroState.STARTING, 300);
                break;
            }
            case PATHING: {
                this.changeMacroState(MacroState.PATHING_VERIFY);
                Commission first = this.curr.get(0);
                if (first == Commission.COMMISSION_CLAIM && MightyMinerConfig.commClaimMethod != 0 || first == Commission.REFUEL && !MightyMinerConfig.commMechaGuiAccessMethod) break;
                RouteWaypoint end = first.getWaypoint();
                List<RouteWaypoint> nodes = GraphHandler.instance.findPathFrom(this.getName(), PlayerUtil.getBlockStandingOn(), end);
                if (nodes.isEmpty()) {
                    this.error("Could not find a path to target. Stopping. Start: " + PlayerUtil.getBlockStandingOn() + ", End: " + end);
                    this.changeMainState(MainState.NONE);
                    return;
                }
                RouteNavigator.getInstance().start(new Route(nodes));
                break;
            }
            case PATHING_VERIFY: {
                if (RouteNavigator.getInstance().isRunning()) {
                    return;
                }
                if (RouteNavigator.getInstance().succeeded()) {
                    this.changeMacroState(MacroState.TOGGLE_MACRO);
                    this.macroRetries = 0;
                    return;
                }
                if (++this.macroRetries > 3) {
                    this.changeMainState(MainState.NONE);
                    this.error("Could Not Reach Vein Properly. Disabling");
                    return;
                }
                switch (RouteNavigator.getInstance().getNavError()) {
                    case NONE: {
                        this.error("RouteNavigator Failed But NavError is NONE.");
                        this.changeMainState(MainState.NONE);
                        break;
                    }
                    case TIME_FAIL: 
                    case PATHFIND_FAILED: {
                        this.error("Failed to Pathfind. Warping");
                        this.changeMainState(MainState.WARP);
                        this.changeMacroState(MacroState.PATHING);
                    }
                }
                break;
            }
            case TOGGLE_MACRO: {
                String commName = this.curr.get(0).getName();
                if (commName.contains("Claim")) {
                    this.changeMacroState(MacroState.CLAIMING_COMMISSION);
                    break;
                }
                if (commName.contains("Titanium") || commName.contains("Mithril")) {
                    this.changeMacroState(MacroState.START_MINING);
                    break;
                }
                if (commName.contains("Refuel")) {
                    this.changeMacroState(MacroState.REFUEL);
                    break;
                }
                this.changeMacroState(MacroState.ENABLE_MOBKILLER);
                break;
            }
            case START_MINING: {
                BlockMiner.getInstance().start(this.blocksToMine, this.miningSpeed, this.miningSpeedBoost, this.curr.stream().anyMatch(it -> it.getName().contains("Titanium")) || MightyMinerConfig.commMineTitanium ? this.titaniumPriority : this.mithrilPriority, MightyMinerConfig.commMiningTool);
                this.changeMacroState(MacroState.MINING_VERIFY);
                break;
            }
            case MINING_VERIFY: {
                if (BlockMiner.getInstance().isRunning()) {
                    return;
                }
                if (BlockMiner.getInstance().hasSucceeded()) {
                    return;
                }
                if (++this.macroRetries > 3) {
                    this.changeMainState(MainState.NONE);
                    this.error("MithrilMiner Crashed More Than 3 Times");
                    break;
                }
                switch (BlockMiner.getInstance().getMithrilError()) {
                    case NONE: {
                        this.error("MithrilMacro Failed but error is NONE.");
                        this.changeMainState(MainState.NONE);
                        return;
                    }
                    case NOT_ENOUGH_BLOCKS: {
                        this.changeMainState(MainState.WARP);
                        this.changeMacroState(MacroState.STARTING);
                    }
                }
                break;
            }
            case ENABLE_MOBKILLER: {
                Set<String> mobName = CommissionUtil.getMobForCommission(this.curr.get(0));
                if (mobName == null) {
                    this.error("Was Supposed to Start MobKiller but current comm is " + this.curr.get(0).getName());
                    this.changeMainState(MainState.NONE);
                    return;
                }
                AutoMobKiller.getInstance().start(mobName, this.curr.get(0).getName().startsWith("Glacite") ? MightyMinerConfig.commMiningTool : MightyMinerConfig.commSlayerWeapon);
                this.changeMacroState(MacroState.MOBKILLER_VERIFY);
                break;
            }
            case MOBKILLER_VERIFY: {
                if (AutoMobKiller.getInstance().isRunning()) {
                    return;
                }
                if (AutoMobKiller.getInstance().succeeded()) {
                    return;
                }
                if (++this.macroRetries > 3) {
                    this.changeMainState(MainState.NONE);
                    this.error("Tried AutoMobKiller more than 3 times but it didnt work. Disabling");
                    break;
                }
                switch (AutoMobKiller.getInstance().getMkError()) {
                    case NONE: {
                        this.error("AutoMobKiller Failed But MKError is NONE.");
                        this.changeMainState(MainState.NONE);
                        break;
                    }
                    case NO_ENTITIES: {
                        this.log("Restarting");
                        this.changeMainState(MainState.WARP);
                        this.changeMacroState(MacroState.STARTING);
                    }
                }
                break;
            }
            case CLAIMING_COMMISSION: {
                AutoCommissionClaim.getInstance().start();
                this.changeMacroState(MacroState.CLAIM_VERIFY);
                break;
            }
            case CLAIM_VERIFY: {
                if (AutoCommissionClaim.getInstance().isRunning()) {
                    return;
                }
                if (AutoCommissionClaim.getInstance().succeeded()) {
                    List<Commission> nextComm = AutoCommissionClaim.getInstance().getNextComm();
                    if (nextComm == null || nextComm.isEmpty()) {
                        this.error("Couldn't retrieve next comm from NPC GUI. Waiting for Tablist to update.");
                        this.changeMacroState(MacroState.WAITING, 3000);
                        this.checkForCommissionChange = true;
                    } else {
                        this.curr = nextComm;
                        this.changeMacroState(MacroState.PATHING);
                    }
                    return;
                }
                if (++this.macroRetries > 3) {
                    this.error("Tried three time but kept getting timed out. Disabling");
                    this.changeMainState(MainState.NONE);
                    break;
                }
                switch (AutoCommissionClaim.getInstance().claimError()) {
                    case NONE: {
                        this.error("AutoCommissionClaim Failed but ClaimError is NONE.");
                        this.changeMainState(MainState.NONE);
                        break;
                    }
                    case INACCESSIBLE_NPC: {
                        this.error("Inaccessible NPC. Retrying");
                        this.changeMacroState(MacroState.PATHING);
                        break;
                    }
                    case TIMEOUT: {
                        this.log("Retrying claim");
                        this.changeMacroState(MacroState.CLAIMING_COMMISSION);
                    }
                }
                break;
            }
            case REFUEL: {
                AutoDrillRefuel.getInstance().start(MightyMinerConfig.commMiningTool, MightyMinerConfig.commMachineFuel, MightyMinerConfig.commFuelRetrievalMethod, MightyMinerConfig.commMechaGuiAccessMethod);
                this.changeMacroState(MacroState.REFUEL_VERIFY);
                break;
            }
            case REFUEL_VERIFY: {
                if (AutoDrillRefuel.getInstance().isRunning()) break;
                if (AutoDrillRefuel.getInstance().hasSucceeded()) {
                    this.log("Done refilling");
                    this.changeMacroState(MacroState.STARTING);
                    break;
                }
                if (++this.macroRetries > 3) {
                    this.error("Tried thrice but failed to refill. Stopping");
                    this.changeMainState(MainState.NONE);
                    break;
                }
                switch (AutoDrillRefuel.getInstance().getFailReason()) {
                    case NONE: {
                        this.error("AutoDrillRefuel Failed but ClaimError is NONE.");
                        this.changeMainState(MainState.NONE);
                        break;
                    }
                    case INACCESSIBLE_MECHANIC: {
                        this.error("Inaccessible NPC. Retrying");
                        this.changeMainState(MainState.WARP);
                        this.changeMacroState(MacroState.STARTING);
                        break;
                    }
                    case FAILED_REFILL: {
                        this.error("Stopping because failed refill");
                        this.changeMainState(MainState.NONE);
                    }
                }
                break;
            }
            case WAITING: {
                if (!this.hasTimerEnded()) break;
                this.error("Timer ended but couldnt exit WAITING state. Disabling");
                this.changeMainState(MainState.NONE);
            }
        }
    }

    private void changeItemState(ItemState to, int time) {
        this.itemState = to;
        this.timer.schedule(time);
    }

    private void changeItemState(ItemState to) {
        this.itemState = to;
    }

    public void handleItems() {
        switch (this.itemState) {
            case STARTING: {
                this.changeItemState(ItemState.TRIGGERING_AUTO_INV);
                break;
            }
            case TRIGGERING_AUTO_INV: {
                AutoInventory.getInstance().moveItems(this.getNecessaryItems());
                this.changeItemState(ItemState.WAITING_FOR_AUTO_INV);
                break;
            }
            case WAITING_FOR_AUTO_INV: {
                if (AutoInventory.getInstance().isRunning()) {
                    return;
                }
                if (!AutoInventory.getInstance().moveFailed()) {
                    this.log("AutoInventory Completed");
                    this.changeMainState(MainState.MACRO);
                    return;
                }
                if (++this.itemRetries > 3) {
                    this.changeMainState(MainState.NONE);
                    this.error("tried to swap items three times but failed");
                    break;
                }
                this.changeItemState(ItemState.HANDLING_ERRORS);
                this.log("Attempting to handle errors");
                break;
            }
            case HANDLING_ERRORS: {
                switch (AutoInventory.getInstance().getMoveError()) {
                    case NONE: {
                        throw new IllegalStateException("AutoInventory Failed but MoveError is NONE.");
                    }
                    case NOT_ENOUGH_HOTBAR_SPACE: {
                        this.changeMainState(MainState.NONE);
                        this.error("Not enough space in hotbar. This should never happen.");
                    }
                }
            }
        }
    }

    private void changeWarpState(WarpState to, int timeToWait) {
        this.warpState = to;
        this.timer.schedule(timeToWait);
    }

    private void changeWarpState(WarpState to) {
        this.warpState = to;
    }

    public void handleWarp() {
        block0 : switch (this.warpState) {
            case STARTING: {
                this.changeWarpState(WarpState.TRIGGERING_AUTOWARP);
                break;
            }
            case TRIGGERING_AUTOWARP: {
                AutoWarp.getInstance().start(null, SubLocation.THE_FORGE);
                this.changeWarpState(WarpState.WAITING_FOR_AUTOWARP);
                break;
            }
            case WAITING_FOR_AUTOWARP: {
                if (AutoWarp.getInstance().isRunning()) {
                    return;
                }
                this.log("AutoWarp Ended");
                if (AutoWarp.getInstance().hasSucceeded()) {
                    this.log("AutoWarp Completed");
                    this.changeMainState(MainState.MACRO);
                    return;
                }
                if (++this.warpRetries > 3) {
                    this.changeMainState(MainState.NONE);
                    this.error("Tried to warp 3 times but didn't reach destination. Disabling.");
                    break;
                }
                this.log("Something went wrong while warping. Trying to fix!");
                this.changeWarpState(WarpState.HANDLING_ERRORS);
                break;
            }
            case HANDLING_ERRORS: {
                switch (AutoWarp.getInstance().getFailReason()) {
                    case NONE: {
                        throw new IllegalStateException("AutoWarp Failed But FailReason is NONE.");
                    }
                    case FAILED_TO_WARP: {
                        this.log("Retrying AutoWarp");
                        this.changeWarpState(WarpState.STARTING);
                        break block0;
                    }
                    case NO_SCROLL: {
                        this.log("No Warp Scroll. Disabling");
                        this.changeMainState(MainState.NONE);
                    }
                }
            }
        }
    }

    static enum MacroState {
        STARTING,
        CHECKING_STATS,
        GETTING_STATS,
        CHECKING_COMMISSION,
        PATHING,
        PATHING_VERIFY,
        TOGGLE_MACRO,
        START_MINING,
        MINING_VERIFY,
        ENABLE_MOBKILLER,
        MOBKILLER_VERIFY,
        CLAIMING_COMMISSION,
        CLAIM_VERIFY,
        REFUEL,
        REFUEL_VERIFY,
        WAITING;

    }

    static enum ItemState {
        STARTING,
        TRIGGERING_AUTO_INV,
        WAITING_FOR_AUTO_INV,
        HANDLING_ERRORS;

    }

    static enum WarpState {
        STARTING,
        TRIGGERING_AUTOWARP,
        WAITING_FOR_AUTOWARP,
        HANDLING_ERRORS;

    }

    static enum MainState {
        NONE,
        WARP,
        ITEMS,
        MACRO;

    }
}

