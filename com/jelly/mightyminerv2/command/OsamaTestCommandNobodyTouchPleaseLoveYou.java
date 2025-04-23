/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Multithreading
 *  cc.polyfrost.oneconfig.utils.commands.annotations.Command
 *  cc.polyfrost.oneconfig.utils.commands.annotations.Main
 *  cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
 *  kotlin.Pair
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.jelly.mightyminerv2.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.event.PacketEvent;
import com.jelly.mightyminerv2.feature.impl.AutoChestUnlocker;
import com.jelly.mightyminerv2.feature.impl.AutoInventory;
import com.jelly.mightyminerv2.feature.impl.AutoMobKiller;
import com.jelly.mightyminerv2.feature.impl.BlockMiner;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.feature.impl.RouteNavigator;
import com.jelly.mightyminerv2.handler.GraphHandler;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.handler.RouteHandler;
import com.jelly.mightyminerv2.macro.commissionmacro.CommissionMacro;
import com.jelly.mightyminerv2.pathfinder.calculate.Path;
import com.jelly.mightyminerv2.pathfinder.calculate.PathNode;
import com.jelly.mightyminerv2.pathfinder.calculate.path.AStarPathFinder;
import com.jelly.mightyminerv2.pathfinder.goal.Goal;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementResult;
import com.jelly.mightyminerv2.pathfinder.movement.Moves;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementAscend;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementDescend;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementDiagonal;
import com.jelly.mightyminerv2.pathfinder.movement.movements.MovementTraverse;
import com.jelly.mightyminerv2.pathfinder.util.BlockUtil;
import com.jelly.mightyminerv2.util.CommissionUtil;
import com.jelly.mightyminerv2.util.PlayerUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import com.jelly.mightyminerv2.util.helper.Angle;
import com.jelly.mightyminerv2.util.helper.Clock;
import com.jelly.mightyminerv2.util.helper.MineableBlock;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import com.jelly.mightyminerv2.util.helper.route.Route;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import com.jelly.mightyminerv2.util.helper.route.TransportMethod;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(value="set")
public class OsamaTestCommandNobodyTouchPleaseLoveYou {
    private static final Logger log = LoggerFactory.getLogger(OsamaTestCommandNobodyTouchPleaseLoveYou.class);
    private static OsamaTestCommandNobodyTouchPleaseLoveYou instance = new OsamaTestCommandNobodyTouchPleaseLoveYou();
    private final Minecraft mc = Minecraft.func_71410_x();
    RouteWaypoint first;
    RouteWaypoint second;
    Entity entTodraw = null;
    BlockPos block = null;
    List<BlockPos> blockToDraw = new CopyOnWriteArrayList<BlockPos>();
    List<Vec3> points = new ArrayList<Vec3>();
    int tick = 0;
    Path path;
    AStarPathFinder pathfinder;
    PathNode curr;
    List<Pair<EntityPlayer, Pair<Double, Double>>> mobs = new ArrayList<Pair<EntityPlayer, Pair<Double, Double>>>();
    List<Pair<String, Entity>> ents = new ArrayList<Pair<String, Entity>>();
    List<Pair<BlockPos, List<Float>>> vals = new ArrayList<Pair<BlockPos, List<Float>>>();
    public boolean allowed = false;
    boolean allowedd = false;
    Clock timer = new Clock();
    MineableBlock[] blocks = new MineableBlock[]{MineableBlock.DIAMOND, MineableBlock.EMERALD, MineableBlock.GOLD, MineableBlock.COAL, MineableBlock.IRON, MineableBlock.REDSTONE, MineableBlock.LAPIS};
    List<Pair<BlockPos, List<Float>>> btd = new ArrayList<Pair<BlockPos, List<Float>>>();
    boolean test = false;
    List<EntityLivingBase> b = new ArrayList<EntityLivingBase>();

    private String mightyMinerV2$cleanSB(String scoreboard) {
        char[] arr = scoreboard.toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            char c = arr[i];
            if (c >= ' ' && c < '\u007f') {
                cleaned.append(c);
            }
            if (c != '\u00a7') continue;
            ++i;
        }
        return cleaned.toString();
    }

    @Main
    public void main() {
        this.allowed = !this.allowed;
    }

    @SubCommand
    public void t() {
        this.block = PlayerUtil.getBlockStandingOn();
        System.out.println(Block.func_176210_f((IBlockState)this.mc.field_71441_e.func_180495_p(PlayerUtil.getBlockStandingOn())));
    }

    @SubCommand
    public void p(int a) {
        MineableBlock[] block = new MineableBlock[]{this.blocks[a]};
        BlockMiner.getInstance().start(block, 2600, 250, new int[]{1}, "Titanium Drill");
    }

    public String getEntityNameFromArmorStand(String armorstandName) {
        char[] carr = armorstandName.toCharArray();
        if (carr[carr.length - 1] != '\u2764') {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean foundSpace = false;
        int charCounter = 0;
        for (int i = carr.length - 1; i >= 0; --i) {
            char curr = carr[i];
            if (!foundSpace) {
                if (curr != ' ') continue;
                foundSpace = true;
                continue;
            }
            if (curr == '\u00a7') {
                charCounter = (byte)(charCounter + 1);
            }
            if (charCounter == 2) {
                builder.deleteCharAt(builder.length() - 1);
                break;
            }
            builder.append(curr);
        }
        return builder.reverse().toString();
    }

    @SubCommand
    public void calc() {
        BlockPos pos = new BlockPos(this.mc.field_71439_g.field_70165_t, Math.ceil(this.mc.field_71439_g.field_70163_u) - 1.0, this.mc.field_71439_g.field_70161_v);
        MovementResult res = new MovementResult();
        double walkSpeed = this.mc.field_71439_g.func_70689_ay();
        CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
        for (Moves move : Moves.getEntries()) {
            res.reset();
            move.calculate(ctx, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), res);
            double cost = res.getCost();
            if (cost >= 1000000.0) continue;
            com.jelly.mightyminerv2.util.Logger.sendMessage("Name: " + move.name() + ", Movement to: " + res.getDest() + ", Cost: " + cost);
            this.blockToDraw.add(res.getDest());
        }
    }

    private boolean canStandOn(BlockPos pos) {
        return this.mc.field_71441_e.func_175665_u(pos) && this.mc.field_71441_e.func_175623_d(pos.func_177982_a(0, 1, 0)) && this.mc.field_71441_e.func_175623_d(pos.func_177982_a(0, 2, 0));
    }

    @SubCommand
    public void f() {
        BlockPos playerPos = PlayerUtil.getBlockStandingOn();
        this.first = new RouteWaypoint(playerPos, TransportMethod.WALK);
    }

    @SubCommand
    public void s() {
        BlockPos playerPos = PlayerUtil.getBlockStandingOn();
        this.second = new RouteWaypoint(playerPos, TransportMethod.WALK);
    }

    @SubCommand
    public void graph() {
        GraphHandler.instance.toggleEdit(CommissionMacro.getInstance().getName());
    }

    @SubCommand
    public void findg() {
        com.jelly.mightyminerv2.util.Logger.sendNote("sec: " + this.second);
        List<RouteWaypoint> path = GraphHandler.instance.findPath(PlayerUtil.getBlockStandingOn(), this.second);
        Route route = new Route();
        path.forEach(k -> route.insert((RouteWaypoint)k));
        this.blockToDraw.clear();
        path.forEach(i -> this.blockToDraw.add(new BlockPos(i.toVec3())));
        RouteNavigator.getInstance().start(route);
    }

    @SubCommand
    public void k() {
        AutoMobKiller.getInstance().start(Arrays.asList(MightyMinerConfig.devMKillerMob.split(";")), MightyMinerConfig.devMKillerWeapon);
    }

    @SubCommand
    public void stop() {
        RouteNavigator.getInstance().stop();
        AutoMobKiller.getInstance().stop();
        Pathfinder.getInstance().stop();
        RotationHandler.getInstance().stop();
        AutoChestUnlocker.instance.stop();
    }

    @SubCommand
    public void b() {
        this.block = PlayerUtil.getBlockStandingOn();
    }

    @SubCommand
    public void between() {
        if (this.block != null) {
            this.blockToDraw.clear();
            boolean bs = BlockUtil.INSTANCE.bresenham(new CalculationContext(), PlayerUtil.getBlockStandingOn(), this.block);
            com.jelly.mightyminerv2.util.Logger.sendMessage("Walkable: " + bs);
        }
    }

    @SubCommand
    public void rot() {
        RotationConfiguration conf = new RotationConfiguration(new Angle(0.0f, 0.0f), 400L, null);
        RotationHandler.getInstance().easeTo(conf);
    }

    @SubCommand
    public void claim() {
        Multithreading.schedule(() -> com.jelly.mightyminerv2.util.Logger.sendNote("Comm: " + CommissionUtil.getCommissionFromContainer((ContainerChest)this.mc.field_71439_g.field_71070_bA)), (long)800L, (TimeUnit)TimeUnit.MILLISECONDS);
    }

    @SubCommand
    public void move() {
        AutoInventory.getInstance().moveItems(Arrays.asList("Pickonimbus 2000", "Aspect of the Void"));
    }

    @SubCommand
    public void clear() {
        this.b.clear();
        this.vals.clear();
        this.blockToDraw.clear();
        this.entTodraw = null;
        this.block = null;
        this.path = null;
        this.first = null;
        this.second = null;
        this.pathfinder = null;
        this.curr = null;
        this.btd.clear();
        this.ents.clear();
    }

    @SubCommand
    public void aotv() {
        if (RouteHandler.getInstance().getSelectedRoute().isEmpty()) {
            com.jelly.mightyminerv2.util.Logger.sendMessage("Selected Route is empty.");
            return;
        }
        RouteNavigator.getInstance().queueRoute(RouteHandler.getInstance().getSelectedRoute());
        RouteNavigator.getInstance().goTo(36);
    }

    @SubCommand
    public void c() {
        BlockPos pp = PlayerUtil.getBlockStandingOn();
        com.jelly.mightyminerv2.util.Logger.sendMessage("Curr: " + this.curr);
    }

    @SubCommand
    public void go(int go) {
        Multithreading.schedule(() -> {
            double walkSpeed = this.mc.field_71439_g.func_70689_ay();
            CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
            BlockPos first = PlayerUtil.getBlockStandingOn();
            BlockPos second = this.block;
            AStarPathFinder finder = new AStarPathFinder(first.func_177958_n(), first.func_177956_o(), first.func_177952_p(), new Goal(second.func_177958_n(), second.func_177956_o(), second.func_177952_p(), ctx), ctx);
            Path path = finder.calculatePath();
            if (path == null) {
                com.jelly.mightyminerv2.util.Logger.sendMessage("No path found");
            } else {
                com.jelly.mightyminerv2.util.Logger.sendMessage("path found");
                this.blockToDraw.clear();
                if (go == 0) {
                    this.blockToDraw.addAll(path.getSmoothedPath());
                } else {
                    this.blockToDraw.addAll(path.getPath());
                }
            }
        }, (long)0L, (TimeUnit)TimeUnit.MILLISECONDS);
    }

    public void onPack(PacketEvent.Received event) {
        if (!this.allowed) {
            return;
        }
        if (this.timer.isScheduled() && this.timer.passed()) {
            this.timer.reset();
            this.test = false;
            this.allowed = false;
        }
        if (event.packet instanceof S2FPacketSetSlot) {
            if (!this.timer.isScheduled()) {
                this.timer.schedule(5000L);
                this.test = true;
            }
            S2FPacketSetSlot pack = (S2FPacketSetSlot)event.packet;
            com.jelly.mightyminerv2.util.Logger.sendLog("Real: " + pack.func_149173_d() + ", +36: " + (pack.func_149173_d() + 36) + ", Item: " + (pack.func_149174_e() != null ? Integer.valueOf(pack.func_149174_e().field_77994_a) : "null"));
        }
        if (!this.test) {
            return;
        }
        com.jelly.mightyminerv2.util.Logger.sendLog(event.packet.toString());
    }

    void draw(Pair<BlockPos, List<Float>> it, Color color) {
        RenderUtil.drawBlock((BlockPos)it.getFirst(), color);
        RenderUtil.drawText("Hard: " + (int)(((Float)((List)it.getSecond()).get(0)).floatValue() * 1000.0f) / 1000 + ", Ang: " + (int)(((Float)((List)it.getSecond()).get(1)).floatValue() * 1000.0f) / 1000 + ", y: " + ((List)it.getSecond()).get(2) + ", p: " + ((List)it.getSecond()).get(3), (double)((BlockPos)it.getFirst()).func_177958_n() + 0.5, (double)((BlockPos)it.getFirst()).func_177956_o() + 1.2, (double)((BlockPos)it.getFirst()).func_177952_p() + 0.5, 0.4f);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!this.vals.isEmpty()) {
            this.draw(this.vals.get(0), new Color(255, 0, 0, 100));
            for (int i = 1; i < this.vals.size(); ++i) {
                this.draw(this.vals.get(i), new Color(197, 19, 203, 157));
            }
        }
        if (!this.blockToDraw.isEmpty()) {
            this.blockToDraw.forEach(b -> RenderUtil.drawBlock(b, new Color(255, 0, 0, 200)));
        }
        if (this.block != null) {
            RenderUtil.drawBlock(this.block, new Color(255, 0, 0, 50));
        }
        if (this.second != null) {
            RenderUtil.drawBlock(new BlockPos(this.second.toVec3()), new Color(0, 0, 0, 200));
        }
        if (!this.b.isEmpty()) {
            RenderUtil.drawBox(this.b.get(0).func_174813_aQ(), new Color(255, 255, 255, 200));
            this.b.forEach(it -> RenderUtil.outlineBox(it.func_174813_aQ(), new Color(255, 255, 255, 200)));
        }
    }

    @SubCommand
    public void trav() {
        double walkSpeed = this.mc.field_71439_g.func_70689_ay();
        CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
        BlockPos pp = PlayerUtil.getBlockStandingOn();
        EnumFacing d = this.mc.field_71439_g.func_174811_aO();
        MovementResult res = new MovementResult();
        MovementTraverse trav = new MovementTraverse(MightyMiner.instance, pp, pp.func_177982_a(d.func_82601_c(), d.func_96559_d(), d.func_82599_e()));
        trav.calculateCost(ctx, res);
        com.jelly.mightyminerv2.util.Logger.sendMessage("Movement cost: " + res.getCost());
        this.block = res.getDest();
        this.blockToDraw.clear();
        this.blockToDraw.add(pp);
    }

    @SubCommand
    public void asc() {
        CalculationContext ctx = new CalculationContext();
        BlockPos pp = PlayerUtil.getBlockStandingOn();
        EnumFacing d = this.mc.field_71439_g.func_174811_aO();
        MovementResult res = new MovementResult();
        MovementAscend trav = new MovementAscend(MightyMiner.instance, pp, pp.func_177982_a(d.func_82601_c(), d.func_96559_d() + 1, d.func_82599_e()));
        trav.calculateCost(ctx, res);
        com.jelly.mightyminerv2.util.Logger.sendMessage("Movement cost: " + res.getCost());
        this.block = res.getDest();
        this.blockToDraw.clear();
        this.blockToDraw.add(pp);
    }

    @SubCommand
    public void desc() {
        double walkSpeed = this.mc.field_71439_g.func_70689_ay();
        CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
        BlockPos pp = PlayerUtil.getBlockStandingOn();
        EnumFacing d = this.mc.field_71439_g.func_174811_aO();
        MovementResult res = new MovementResult();
        MovementDescend trav = new MovementDescend(MightyMiner.instance, pp, pp.func_177982_a(d.func_82601_c(), d.func_96559_d() - 1, d.func_82599_e()));
        trav.calculateCost(ctx, res);
        com.jelly.mightyminerv2.util.Logger.sendMessage("Movement cost: " + res.getCost());
        this.block = res.getDest();
        this.blockToDraw.clear();
        this.blockToDraw.add(pp);
    }

    @SubCommand
    public void diag() {
        double walkSpeed = this.mc.field_71439_g.func_70689_ay();
        CalculationContext ctx = new CalculationContext(walkSpeed * 1.3, walkSpeed, walkSpeed * 0.3);
        BlockPos pp = PlayerUtil.getBlockStandingOn();
        MovementResult res = new MovementResult();
        MovementDiagonal diag = new MovementDiagonal(MightyMiner.instance, pp, pp.func_177982_a(1, 0, 1));
        diag.calculateCost(ctx, res);
        com.jelly.mightyminerv2.util.Logger.sendMessage("Movement cost: " + res.getCost());
        this.block = res.getDest();
        this.blockToDraw.clear();
        this.blockToDraw.add(pp);
    }

    public static OsamaTestCommandNobodyTouchPleaseLoveYou getInstance() {
        return instance;
    }
}

