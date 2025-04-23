/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.macro.mithrilmacro;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.FeatureManager;
import com.jelly.mightyminerv2.feature.impl.AutoInventory;
import com.jelly.mightyminerv2.feature.impl.BlockMiner;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.macro.AbstractMacro;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.MineableBlock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

public class MithrilMacro
extends AbstractMacro {
    private static MithrilMacro instance = new MithrilMacro();
    private static final Vec3 WARP_MINES_BLOCK = new Vec3(-49.0, 200.0, -122.0);
    private static final Vec3 STATION_MASTER_LOCATION = new Vec3(39.0, 200.0, -87.0);
    private static final Vec3 LOWER_MINES_LOCATION = new Vec3(84.0, 113.0, 132.0);
    private State currentState = State.NONE;
    private NavigationState navigationState = NavigationState.CHECK_POSITION;
    private List<String> necessaryItems = new ArrayList<String>();
    private int miningSpeed = 0;
    private int miningSpeedBoost = 0;
    private MineableBlock[] blocksToMine = new MineableBlock[0];
    private int macroRetries = 0;
    private boolean isMining = false;
    private final RotationHandler rotationHandler = RotationHandler.getInstance();

    public static MithrilMacro getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "Mithril Macro";
    }

    @Override
    public List<String> getNecessaryItems() {
        if (this.necessaryItems.isEmpty()) {
            this.necessaryItems.add(MightyMinerConfig.mithrilMiningTool);
            this.log("Necessary items initialized: " + this.necessaryItems);
        }
        return this.necessaryItems;
    }

    @Override
    public void onEnable() {
        this.log("Enabling Mithril Macro");
        this.resetVariables();
        this.currentState = State.INITIALIZATION;
    }

    @Override
    public void onDisable() {
        this.log("Disabling Mithril Macro");
        if (this.isMining) {
            BlockMiner.getInstance().stop();
            this.isMining = false;
        }
        this.resetVariables();
    }

    @Override
    public void onPause() {
        FeatureManager.getInstance().pauseAll();
        this.log("Mithril Macro paused");
    }

    @Override
    public void onResume() {
        FeatureManager.getInstance().resumeAll();
        this.log("Mithril Macro resumed");
    }

    private void resetVariables() {
        this.navigationState = NavigationState.CHECK_POSITION;
        this.currentState = State.NONE;
        this.macroRetries = 0;
        this.miningSpeed = 0;
        this.miningSpeedBoost = 0;
        this.necessaryItems.clear();
        this.isMining = false;
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.timer.isScheduled() && !this.timer.passed()) {
            return;
        }
        this.log("Current state: " + (Object)((Object)this.currentState));
        switch (this.currentState) {
            case INITIALIZATION: {
                this.handleInitializationState();
                break;
            }
            case CHECKING_STATS: {
                this.handleCheckingStatsState();
                break;
            }
            case GETTING_STATS: {
                this.handleGettingStatsState();
                break;
            }
            case MINING: {
                this.handleMiningState();
                break;
            }
            case PATHFINDING: {
                this.handlePathfindingState();
                break;
            }
            case WAITING_ON_PATHFINDING: {
                this.handleWaitingOnPathfindingState();
                break;
            }
            case END: {
                this.handleEndState();
            }
        }
    }

    private void handleInitializationState() {
        this.log("Handling initialization state");
        this.setBlocksToMineBasedOnOreType();
        BlockMiner.getInstance().wait_threshold = 500;
        if (this.miningSpeed == 0 && this.miningSpeedBoost == 0) {
            if (!InventoryUtil.holdItem(MightyMinerConfig.mithrilMiningTool)) {
                this.disable("Cannot hold mining tool");
                return;
            }
            this.changeState(State.CHECKING_STATS);
        } else {
            this.changeState(State.MINING);
        }
    }

    private void handleCheckingStatsState() {
        this.log("Checking mining stats");
        AutoInventory.getInstance().retrieveSpeedBoost();
        this.changeState(State.GETTING_STATS);
    }

    private void handleGettingStatsState() {
        if (AutoInventory.getInstance().isRunning()) {
            return;
        }
        if (AutoInventory.getInstance().sbSucceeded()) {
            int[] sb = AutoInventory.getInstance().getSpeedBoostValues();
            this.miningSpeed = sb[0];
            this.miningSpeedBoost = sb[1];
            this.macroRetries = 0;
            this.log("Retrieved stats - Speed: " + this.miningSpeed + ", Boost: " + this.miningSpeedBoost);
            this.changeState(State.MINING);
        } else {
            this.handleSpeedBoostError();
        }
    }

    private void handleMiningState() {
        if (!this.checkValidLocation()) {
            this.changeState(State.INITIALIZATION);
            return;
        }
        if (BlockMiner.getInstance().getMithrilError() == BlockMiner.BlockMinerError.NOT_ENOUGH_BLOCKS) {
            this.changeState(State.PATHFINDING);
            return;
        }
        if (!this.isMining) {
            this.startMining();
        }
    }

    private void handlePathfindingState() {
        if (!Pathfinder.getInstance().isRunning()) {
            this.movePosition();
            this.changeState(State.WAITING_ON_PATHFINDING);
        }
    }

    private void handleWaitingOnPathfindingState() {
        if (!Pathfinder.getInstance().isRunning()) {
            this.changeState(State.MINING);
        }
    }

    private void handleEndState() {
        this.macroRetries = 0;
        this.miningSpeed = 0;
        this.miningSpeedBoost = 0;
        this.necessaryItems.clear();
        this.isMining = false;
        this.changeState(State.NONE);
    }

    private void startMining() {
        BlockMiner.getInstance().start(this.blocksToMine, this.miningSpeed, this.miningSpeedBoost, this.determinePriority(), MightyMinerConfig.mithrilMiningTool);
        this.isMining = true;
        this.log("Started mining with speed: " + this.miningSpeed + ", boost: " + this.miningSpeedBoost);
        if (BlockMiner.getInstance().getMithrilError() == BlockMiner.BlockMinerError.NOT_ENOUGH_BLOCKS) {
            this.log("Not enough blocks to mine. Stopping macro.");
            this.changeState(State.NONE);
        }
    }

    private void handleSpeedBoostError() {
        this.log("Handling speed boost error");
        switch (AutoInventory.getInstance().getSbError()) {
            case NONE: {
                throw new IllegalStateException("AutoInventory#getSbError failed but returned NONE");
            }
            case CANNOT_OPEN_INV: {
                if (++this.macroRetries > 3) {
                    this.disable("Failed to open inventory after 3 attempts");
                    break;
                }
                this.changeState(State.INITIALIZATION);
                break;
            }
            case CANNOT_GET_VALUE: {
                this.disable("Failed to get value. Contact the developer.");
            }
        }
    }

    private void setBlocksToMineBasedOnOreType() {
        this.log("Setting blocks to mine based on ore type: " + MightyMinerConfig.oreType);
        switch (MightyMinerConfig.oreType) {
            case 0: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.DIAMOND};
                break;
            }
            case 1: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.EMERALD};
                break;
            }
            case 2: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.REDSTONE};
                break;
            }
            case 3: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.LAPIS};
                break;
            }
            case 4: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.GOLD};
                break;
            }
            case 5: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.IRON};
                break;
            }
            case 6: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.COAL};
                break;
            }
            case 7: {
                this.blocksToMine = new MineableBlock[]{MineableBlock.GRAY_MITHRIL, MineableBlock.GREEN_MITHRIL, MineableBlock.BLUE_MITHRIL, MineableBlock.TITANIUM};
                break;
            }
            default: {
                this.blocksToMine = new MineableBlock[0];
                this.log("Invalid ore type selected");
            }
        }
        this.log("Blocks to mine: " + Arrays.toString((Object[])this.blocksToMine));
    }

    private int[] determinePriority() {
        this.log("Determining mining priorities");
        if (MightyMinerConfig.oreType == 7) {
            return new int[]{MightyMinerConfig.grayMithrilPriority, MightyMinerConfig.greenMithrilPriority, MightyMinerConfig.blueMithrilPriority, MightyMinerConfig.titaniumPriority};
        }
        return new int[]{1, 1, 1, 1};
    }

    @Nullable
    private Entity getMinecart() {
        return this.mc.field_71441_e.func_72910_y().stream().filter(entity -> entity instanceof EntityMinecart).min(Comparator.comparingDouble(entity -> entity.func_174831_c(this.mc.field_71439_g.func_180425_c()))).orElse(null);
    }

    @Nullable
    private Entity getStationMaster() {
        return this.mc.field_71441_e.func_72910_y().stream().filter(this::isStationMaster).min(Comparator.comparingDouble(entity -> entity.func_174831_c(this.mc.field_71439_g.func_180425_c()))).orElse(null);
    }

    private boolean isStationMaster(Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        EntityOtherPlayerMP player = (EntityOtherPlayerMP)entity;
        return player.func_146103_bH().getProperties().containsKey((Object)"textures") && player.func_146103_bH().getProperties().get((Object)"textures").stream().anyMatch(p -> p.getValue().contains("ewogICJ0aW1lc3RhbXAiIDogMTYwODMxMzM3MzE3MywKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVmMzFjMWMyMTVhNTdlOTM3ZmQ3NWFiMzU3ODJmODVlYzI0MmExYjFmOTUwYTI2YTQyYmI1ZTBhYTVjYmVkYSIKICAgIH0KICB9Cn0="));
    }

    private void movePosition() {
        this.log("Moving to next mining position");
    }

    private boolean checkValidLocation() {
        return true;
    }

    private void changeState(State newState) {
        this.log("Changing state from " + (Object)((Object)this.currentState) + " to " + (Object)((Object)newState));
        this.currentState = newState;
    }

    private void disable(String reason) {
        Logger.sendError("[Mithril Macro] " + reason);
        this.onDisable();
    }

    private static enum NavigationState {
        CHECK_POSITION,
        WARP_TO_MINES,
        WAIT_FOR_WARP,
        GO_TO_STATION_MASTER,
        WAIT_FOR_PATHFINDER,
        LOOK_AT_STATION_MASTER,
        OPEN_STATION_MASTER,
        CLICK_LOWER_MINES,
        CLOSE_MASTER_GUI,
        WAIT_FOR_CART,
        LOOK_AT_CART,
        RIGHT_CLICK_CART,
        WAIT_FOR_TELEPORT,
        AOTV_TO_POSITION;

    }

    private static enum State {
        NONE,
        INITIALIZATION,
        CHECKING_STATS,
        GETTING_STATS,
        MINING,
        PATHFINDING,
        WAITING_ON_PATHFINDING,
        END;

    }
}

