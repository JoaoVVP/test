/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.impl.MobTracker;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.TablistUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class EntityUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean isNpc(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        return !TablistUtil.getTabListPlayersSkyblock().contains(entity.func_70005_c_());
    }

    public static BlockPos getBlockStandingOn(Entity entity) {
        return new BlockPos(entity.field_70165_t, Math.ceil(entity.field_70163_u - 0.25) - 1.0, entity.field_70161_v);
    }

    public static Optional<Entity> getEntityLookingAt() {
        return Optional.ofNullable(EntityUtil.mc.field_71476_x.field_72308_g);
    }

    public static boolean isStandDead(String name) {
        return EntityUtil.getHealthFromStandName(name) == 0;
    }

    public static String getEntityNameFromArmorStand(String armorstandName) {
        char[] carr = armorstandName.toCharArray();
        if (carr.length == 0 || carr[carr.length - 1] != '\u2764') {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean foundSpace = false;
        boolean foundDigit = false;
        int charCounter = 0;
        for (int i = carr.length - 1; i >= 0; --i) {
            char curr = carr[i];
            if (!foundDigit) {
                foundDigit = Character.isDigit(curr);
                continue;
            }
            if (!foundSpace) {
                foundSpace = curr == ' ';
                continue;
            }
            if (curr == '\u00a7') {
                if ((charCounter = (int)((byte)(charCounter + 1))) != 2) continue;
                builder.deleteCharAt(builder.length() - 1);
                break;
            }
            if (charCounter != 1) continue;
            builder.append(curr);
        }
        return builder.reverse().toString();
    }

    public static int getHealthFromStandName(String name) {
        int health = 0;
        try {
            String[] arr = name.split(" ");
            health = Integer.parseInt(arr[arr.length - 1].split("/")[0].replace(",", ""));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return health;
    }

    public static List<EntityLivingBase> getEntities(Set<String> entityNames, Set<EntityLivingBase> entitiesToIgnore) {
        ArrayList entities = new ArrayList();
        entityNames.forEach(it -> {
            Set<EntityLivingBase> set = MobTracker.getInstance().getEntity((String)it);
            entitiesToIgnore.forEach(set::remove);
            entities.addAll(set);
        });
        Vec3 playerPos = EntityUtil.mc.field_71439_g.func_174791_d();
        float normalizedYaw = AngleUtil.normalizeAngle(EntityUtil.mc.field_71439_g.field_70177_z);
        return entities.stream().filter(EntityLivingBase::func_70089_S).sorted(Comparator.comparingDouble(ent -> {
            Vec3 entPos = ent.func_174791_d();
            double distanceCost = playerPos.func_72438_d(entPos);
            double angleCost = Math.abs(AngleUtil.getNeededYawChange(normalizedYaw, AngleUtil.getRotationYaw(entPos)));
            return distanceCost * (double)((float)MightyMinerConfig.devMKillDist / 100.0f) + angleCost * (double)((float)MightyMinerConfig.devMKillRot / 100.0f);
        })).collect(Collectors.toList());
    }

    private static long pack(int x, int z) {
        return (long)x << 32 | (long)z & 0xFFFFFFFFL;
    }
}

