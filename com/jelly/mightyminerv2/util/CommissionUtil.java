/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 *  kotlin.Pair
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.StringUtils
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.macro.commissionmacro.helper.Commission;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.EntityUtil;
import com.jelly.mightyminerv2.util.InventoryUtil;
import com.jelly.mightyminerv2.util.TablistUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;

public class CommissionUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static final Map<Commission, Set<String>> slayerMob = ImmutableMap.of((Object)((Object)Commission.GOBLIN_SLAYER), (Object)ImmutableSet.of((Object)"Goblin", (Object)"Knifethrower", (Object)"Fireslinger"), (Object)((Object)Commission.GLACITE_WALKER_SLAYER), (Object)ImmutableSet.of((Object)"Glacite Walker"));
    public static final List<Pair<String, Vec3>> emissaries = Arrays.asList(new Pair((Object)"Ceanna", (Object)new Vec3(42.5, 134.5, 22.5)), new Pair((Object)"Carlton", (Object)new Vec3(-72.5, 153.0, -10.5)), new Pair((Object)"Wilson", (Object)new Vec3(171.5, 150.0, 31.5)), new Pair((Object)"Lilith", (Object)new Vec3(58.5, 198.0, -8.5)), new Pair((Object)"Fraiser", (Object)new Vec3(-132.5, 174.0, -50.5)));

    public static Set<String> getMobForCommission(Commission commission) {
        return slayerMob.get((Object)commission);
    }

    public static Optional<EntityPlayer> getEmissary(Vec3 pos) {
        return CommissionUtil.mc.field_71441_e.field_73010_i.stream().filter(entity -> entity.field_70165_t == pos.field_72450_a && entity.field_70163_u == pos.field_72448_b && entity.field_70161_v == pos.field_72449_c && !entity.func_70005_c_().contains("Sentry") && EntityUtil.isNpc((Entity)entity)).findFirst();
    }

    public static Optional<EntityPlayer> getClosestEmissary() {
        Vec3 pos = emissaries.stream().min(Comparator.comparing(it -> CommissionUtil.mc.field_71439_g.func_174791_d().func_72436_e((Vec3)it.getSecond()))).map(Pair::getSecond).orElse(null);
        if (pos == null) {
            return Optional.empty();
        }
        return CommissionUtil.mc.field_71441_e.field_73010_i.stream().filter(entity -> entity.field_70165_t == pos.field_72450_a && entity.field_70163_u == pos.field_72448_b && entity.field_70161_v == pos.field_72449_c && !entity.func_70005_c_().contains("Sentry") && EntityUtil.isNpc((Entity)entity)).findFirst();
    }

    public static Vec3 getClosestEmissaryPosition() {
        return (Vec3)emissaries.stream().min(Comparator.comparing(it -> CommissionUtil.mc.field_71439_g.func_174791_d().func_72436_e((Vec3)it.getSecond()))).get().getSecond();
    }

    public static List<Commission> getCurrentCommissionsFromTablist() {
        ArrayList<Commission> comms = new ArrayList<Commission>();
        boolean foundCommission = false;
        for (String text : TablistUtil.getCachedTablist()) {
            if (!foundCommission) {
                if (!text.equalsIgnoreCase("Commissions:")) continue;
                foundCommission = true;
                continue;
            }
            if (text.contains("DONE")) {
                return Arrays.asList(Commission.COMMISSION_CLAIM);
            }
            Commission comm = Commission.getCommission(text.split(": ")[0].trim());
            if (comm != null) {
                comms.add(comm);
            }
            if (!text.isEmpty()) continue;
            break;
        }
        return Commission.getBestCommissionFrom(comms);
    }

    public static int getClaimableCommissionSlot() {
        if (!(CommissionUtil.mc.field_71439_g.field_71070_bA instanceof ContainerChest)) {
            return -1;
        }
        ContainerChest chest = (ContainerChest)CommissionUtil.mc.field_71439_g.field_71070_bA;
        for (int i = 0; i < chest.func_85151_d().func_70302_i_(); ++i) {
            ItemStack stack = chest.func_85151_d().func_70301_a(i);
            if (stack == null || stack.func_77973_b() == null) continue;
            for (String lore : InventoryUtil.getItemLore(stack)) {
                if (!lore.equalsIgnoreCase("completed")) continue;
                return i;
            }
        }
        return -1;
    }

    public static List<Commission> getCommissionFromContainer(ContainerChest container) {
        IInventory lowerChest = container.func_85151_d();
        ArrayList<Commission> comms = new ArrayList<Commission>();
        block0: for (int i = 0; i < lowerChest.func_70302_i_(); ++i) {
            ItemStack stack = lowerChest.func_70301_a(i);
            if (stack == null || stack.func_77973_b() == null || !stack.func_82837_s() || !StringUtils.func_76338_a((String)stack.func_82833_r()).startsWith("Commission")) continue;
            List<String> loreList = InventoryUtil.getItemLore(stack);
            for (int j = 0; j < loreList.size(); ++j) {
                Commission comm;
                if (!loreList.get(j).isEmpty()) continue;
                if ((comm = Commission.getCommission(loreList.get(++j))) == null) continue block0;
                if (comm == Commission.COMMISSION_CLAIM) {
                    return Arrays.asList(comm);
                }
                comms.add(comm);
                continue block0;
            }
        }
        return Commission.getBestCommissionFrom(comms);
    }

    public static List<EntityLiving> getMobList(String mobName, Set<EntityLiving> mobsToIgnore) {
        ArrayList<EntityLiving> mobs = new ArrayList<EntityLiving>();
        for (Entity mob2 : CommissionUtil.mc.field_71441_e.field_72996_f) {
            if (!(mob2 instanceof EntityLiving) || !mob2.func_70005_c_().trim().equals(mobName) || !mob2.func_70089_S() || mobsToIgnore.contains(mob2)) continue;
            mobs.add((EntityLiving)mob2);
        }
        Vec3 playerPos = CommissionUtil.mc.field_71439_g.func_174791_d();
        float normalizedYaw = AngleUtil.normalizeAngle(CommissionUtil.mc.field_71439_g.field_70177_z);
        mobs.sort(Comparator.comparingDouble(mob -> {
            Vec3 mobPos = mob.func_174791_d();
            double distanceCost = Math.hypot(playerPos.field_72450_a - mobPos.field_72450_a, playerPos.field_72449_c - mobPos.field_72449_c) + Math.abs(mobPos.field_72448_b - playerPos.field_72448_b) * 2.0;
            double angleCost = Math.abs(AngleUtil.normalizeAngle(normalizedYaw - AngleUtil.getRotation((Entity)mob).yaw));
            return distanceCost * (double)MightyMinerConfig.devMKillDist + angleCost * (double)MightyMinerConfig.devMKillRot;
        }));
        return mobs;
    }

    public static List<Pair<EntityPlayer, Pair<Double, Double>>> getMobListDebug(String mobName, Set<EntityPlayer> mobsToIgnore) {
        ArrayList<Pair<EntityPlayer, Pair<Double, Double>>> mobs = new ArrayList<Pair<EntityPlayer, Pair<Double, Double>>>();
        Vec3 playerPos = CommissionUtil.mc.field_71439_g.func_174791_d();
        float normalizedYaw = AngleUtil.normalizeAngle(CommissionUtil.mc.field_71439_g.field_70177_z);
        for (EntityPlayer mob : CommissionUtil.mc.field_71441_e.field_73010_i) {
            if (!mob.func_70005_c_().trim().equals(mobName) || !mob.func_70089_S() || mobsToIgnore.contains(mob)) continue;
            Vec3 mobPos = mob.func_174791_d();
            double distanceCost = Math.hypot(playerPos.field_72450_a - mobPos.field_72450_a, playerPos.field_72449_c - mobPos.field_72449_c) + Math.abs(mobPos.field_72448_b - playerPos.field_72448_b) * 2.0;
            double angleCost = Math.abs(AngleUtil.normalizeAngle(normalizedYaw - AngleUtil.getRotation((Entity)mob).yaw));
            mobs.add((Pair<EntityPlayer, Pair<Double, Double>>)new Pair((Object)mob, (Object)new Pair((Object)(distanceCost * 0.6), (Object)(angleCost * 0.1))));
        }
        mobs.sort(Comparator.comparing(a -> {
            Pair b = (Pair)a.getSecond();
            return (Double)b.getFirst() + (Double)b.getSecond();
        }));
        return mobs;
    }
}

