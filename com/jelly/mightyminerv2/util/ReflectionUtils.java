/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.jelly.mightyminerv2.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import net.minecraft.client.Minecraft;

public class ReflectionUtils {
    public static boolean invoke(Object object, String methodName) {
        try {
            Method method = object.getClass().getDeclaredMethod(methodName, new Class[0]);
            method.setAccessible(true);
            method.invoke(object, new Object[0]);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static Object field(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static boolean hasPackageInstalled(String name) {
        Package[] packages;
        for (Package pack : packages = Package.getPackages()) {
            if (!pack.getName().contains(name)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasModFile(String name) {
        Path modsDir = Minecraft.func_71410_x().field_71412_D.toPath().resolve("mods");
        String[] modFiles = modsDir.toFile().list();
        return modFiles != null && Arrays.stream(modFiles).anyMatch(modFile -> modFile.toLowerCase().contains(name.toLowerCase()) && !modFile.toLowerCase().endsWith(".disabled"));
    }
}

