/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util;

import com.google.common.collect.ImmutableMap;
import com.jelly.mightyminerv2.mixin.client.MinecraftAccessor;
import com.jelly.mightyminerv2.util.AngleUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class KeyBindUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static final KeyBinding[] allKeys = new KeyBinding[]{KeyBindUtil.mc.field_71474_y.field_74312_F, KeyBindUtil.mc.field_71474_y.field_74313_G, KeyBindUtil.mc.field_71474_y.field_74368_y, KeyBindUtil.mc.field_71474_y.field_74351_w, KeyBindUtil.mc.field_71474_y.field_74370_x, KeyBindUtil.mc.field_71474_y.field_74366_z, KeyBindUtil.mc.field_71474_y.field_74314_A, KeyBindUtil.mc.field_71474_y.field_74311_E, KeyBindUtil.mc.field_71474_y.field_151444_V};
    public static final KeyBinding[] allKeys2 = new KeyBinding[]{KeyBindUtil.mc.field_71474_y.field_74368_y, KeyBindUtil.mc.field_71474_y.field_74351_w, KeyBindUtil.mc.field_71474_y.field_74370_x, KeyBindUtil.mc.field_71474_y.field_74366_z, KeyBindUtil.mc.field_71474_y.field_74314_A};
    public static final Map<KeyBinding, Integer> keyBindMap = ImmutableMap.of((Object)KeyBindUtil.mc.field_71474_y.field_74351_w, (Object)0, (Object)KeyBindUtil.mc.field_71474_y.field_74370_x, (Object)90, (Object)KeyBindUtil.mc.field_71474_y.field_74368_y, (Object)180, (Object)KeyBindUtil.mc.field_71474_y.field_74366_z, (Object)-90);

    public static void rightClick() {
        ((MinecraftAccessor)mc).rightClick();
    }

    public static void leftClick() {
        ((MinecraftAccessor)mc).leftClick();
    }

    public static void middleClick() {
        ((MinecraftAccessor)mc).middleClick();
    }

    public static void onTick(KeyBinding key) {
        if (KeyBindUtil.mc.field_71462_r == null) {
            KeyBinding.func_74507_a((int)key.func_151463_i());
        }
    }

    public static int getRightClickDelayTimer() {
        return ((MinecraftAccessor)mc).getRightClickDelayTimer();
    }

    public static void resetRightClickDelayTimer() {
        ((MinecraftAccessor)mc).setRightClickDelayTimer(0);
    }

    public static void setKeyBindState(KeyBinding key, boolean pressed) {
        if (pressed && KeyBindUtil.mc.field_71462_r != null && key != null) {
            KeyBindUtil.realSetKeyBindState(key, false);
            return;
        }
        KeyBindUtil.realSetKeyBindState(key, pressed);
    }

    private static void realSetKeyBindState(KeyBinding key, boolean pressed) {
        if (key == null) {
            return;
        }
        if (pressed) {
            if (!key.func_151470_d()) {
                KeyBinding.func_74507_a((int)key.func_151463_i());
                KeyBinding.func_74510_a((int)key.func_151463_i(), (boolean)true);
            }
        } else if (key.func_151470_d()) {
            KeyBinding.func_74510_a((int)key.func_151463_i(), (boolean)false);
        }
    }

    public static void stopMovement() {
        KeyBindUtil.stopMovement(false);
    }

    public static void stopMovement(boolean ignoreAttack) {
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74351_w, false);
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74368_y, false);
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74366_z, false);
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74370_x, false);
        if (!ignoreAttack) {
            KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74312_F, false);
            KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74313_G, false);
        }
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74311_E, false);
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74314_A, false);
        KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_151444_V, false);
    }

    public static void holdThese(boolean withAttack, KeyBinding ... keyBinding) {
        KeyBindUtil.releaseAllExcept(keyBinding);
        for (KeyBinding key : keyBinding) {
            if (key == null) continue;
            KeyBindUtil.realSetKeyBindState(key, true);
        }
        if (withAttack) {
            KeyBindUtil.realSetKeyBindState(KeyBindUtil.mc.field_71474_y.field_74312_F, true);
        }
    }

    public static void holdThese(KeyBinding ... keyBinding) {
        KeyBindUtil.releaseAllExcept(keyBinding);
        for (KeyBinding key : keyBinding) {
            if (key == null) continue;
            KeyBindUtil.realSetKeyBindState(key, true);
        }
    }

    public static void releaseAllExcept(KeyBinding ... keyBinding) {
        for (KeyBinding key : allKeys) {
            if (key == null || KeyBindUtil.contains(keyBinding, key) || !key.func_151470_d()) continue;
            KeyBindUtil.realSetKeyBindState(key, false);
        }
    }

    public static boolean contains(KeyBinding[] keyBinding, KeyBinding key) {
        for (KeyBinding keyBind : keyBinding) {
            if (keyBind == null || keyBind.func_151463_i() != key.func_151463_i()) continue;
            return true;
        }
        return false;
    }

    public static boolean areAllKeybindsReleased() {
        for (KeyBinding key : allKeys2) {
            if (key == null || !key.func_151470_d()) continue;
            return false;
        }
        return true;
    }

    public static KeyBinding[] getHeldKeybinds() {
        KeyBinding[] keybinds = new KeyBinding[allKeys.length];
        int i = 0;
        for (KeyBinding key : allKeys) {
            if (key == null || !key.func_151470_d()) continue;
            keybinds[i] = key;
            ++i;
        }
        return keybinds;
    }

    public static List<KeyBinding> getNeededKeyPresses(Vec3 orig, Vec3 dest) {
        ArrayList<KeyBinding> keys = new ArrayList<KeyBinding>();
        double[] delta = new double[]{orig.field_72450_a - dest.field_72450_a, orig.field_72449_c - dest.field_72449_c};
        float requiredAngle = (float)(MathHelper.func_181159_b((double)delta[0], (double)(-delta[1])) * 57.29577951308232);
        float angleDifference = AngleUtil.normalizeAngle(requiredAngle - KeyBindUtil.mc.field_71439_g.field_70177_z) * -1.0f;
        keyBindMap.forEach((key, yaw) -> {
            if ((double)Math.abs((float)yaw.intValue() - angleDifference) < 67.5 || Math.abs((double)yaw.intValue() - ((double)angleDifference + 360.0)) < 67.5) {
                keys.add((KeyBinding)key);
            }
        });
        return keys;
    }

    public static List<KeyBinding> getNeededKeyPresses(float neededYaw) {
        ArrayList<KeyBinding> keys = new ArrayList<KeyBinding>();
        float finalNeededYaw = neededYaw = AngleUtil.normalizeAngle(neededYaw - KeyBindUtil.mc.field_71439_g.field_70177_z) * -1.0f;
        keyBindMap.forEach((key, yaw) -> {
            if ((double)Math.abs((float)yaw.intValue() - finalNeededYaw) < 67.5 || Math.abs((double)yaw.intValue() - ((double)finalNeededYaw + 360.0)) < 67.5) {
                keys.add((KeyBinding)key);
            }
        });
        return keys;
    }

    public static List<KeyBinding> getOppositeKeys(List<KeyBinding> kbs) {
        ArrayList<KeyBinding> keys = new ArrayList<KeyBinding>();
        kbs.forEach(key -> {
            switch (key.func_151463_i()) {
                case 17: {
                    keys.add(KeyBindUtil.mc.field_71474_y.field_74368_y);
                    break;
                }
                case 30: {
                    keys.add(KeyBindUtil.mc.field_71474_y.field_74366_z);
                    break;
                }
                case 31: {
                    keys.add(KeyBindUtil.mc.field_71474_y.field_74370_x);
                    break;
                }
                case 32: {
                    keys.add(KeyBindUtil.mc.field_71474_y.field_74351_w);
                }
            }
        });
        return keys;
    }

    public static List<KeyBinding> getKeyPressesToDecelerate(Vec3 orig, Vec3 dest) {
        return KeyBindUtil.getOppositeKeys(KeyBindUtil.getNeededKeyPresses(orig, dest));
    }
}

