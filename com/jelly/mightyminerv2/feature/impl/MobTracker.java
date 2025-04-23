/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.command.OsamaTestCommandNobodyTouchPleaseLoveYou;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.event.UpdateEntityEvent;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.util.EntityUtil;
import com.jelly.mightyminerv2.util.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobTracker
extends AbstractFeature {
    private static MobTracker instance = new MobTracker();
    List<Vec3> l = new ArrayList<Vec3>();
    private final Object2ObjectMap<String, ObjectSet<EntityLivingBase>> mobs = new Object2ObjectOpenHashMap<String, ObjectSet<EntityLivingBase>>();

    public static MobTracker getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "MobTracker";
    }

    public Set<EntityLivingBase> getEntity(String name) {
        Set list = (Set)this.mobs.get(name);
        if (list == null) {
            return new HashSet<EntityLivingBase>();
        }
        return new HashSet<EntityLivingBase>(list);
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        if (OsamaTestCommandNobodyTouchPleaseLoveYou.getInstance().allowed) {
            this.mobs.forEach((k, v) -> v.forEach(it -> {
                RenderUtil.outlineBox(it.func_174813_aQ(), new Color(255, 255, 255, 200));
                RenderUtil.drawText(k, it.field_70165_t, it.field_70163_u + (double)it.field_70131_O, it.field_70161_v, 1.0f);
            }));
        }
    }

    @Override
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.mobs.clear();
    }

    @SubscribeEvent
    public void onEntitySpawn(UpdateEntityEvent event) {
        byte updateType = event.updateType;
        EntityLivingBase entity = event.entity;
        String name = "";
        if (entity instanceof EntityArmorStand) {
            if (updateType != 0) {
                return;
            }
            name = EntityUtil.getEntityNameFromArmorStand(entity.func_95999_t());
            if (name.isEmpty()) {
                return;
            }
            this.l.add(entity.func_174791_d());
            List ents = this.mc.field_71441_e.func_175674_a((Entity)entity, entity.func_174813_aQ().func_72314_b(0.0, 3.0, 0.0), it -> it instanceof EntityLivingBase);
            if (ents.isEmpty()) {
                return;
            }
            this.mobs.computeIfAbsent(name, k -> new ObjectOpenHashSet()).add((EntityLivingBase)ents.stream().min(Comparator.comparing(arg_0 -> ((EntityLivingBase)entity).func_70068_e(arg_0))).get());
        } else if (updateType == 1) {
            this.mobs.values().forEach(it -> it.remove(entity));
        }
    }

    private long pack(int x, int z) {
        return (long)x << 32 | (long)z & 0xFFFFFFFFL;
    }
}

