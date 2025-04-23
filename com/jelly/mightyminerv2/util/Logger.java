/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Notifications
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.StringUtils
 */
package com.jelly.mightyminerv2.util;

import cc.polyfrost.oneconfig.utils.Notifications;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

public abstract class Logger {
    protected static final Minecraft mc = Minecraft.func_71410_x();
    private static final Map<String, String> lastMessages = new HashMap<String, String>();

    public abstract String getName();

    public static void addMessage(String text) {
        if (Logger.mc.field_71439_g == null || Logger.mc.field_71441_e == null) {
            System.out.println("MightyMiner " + StringUtils.func_76338_a((String)text));
        } else {
            Logger.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(text));
        }
    }

    public static void sendMessage(String message) {
        Logger.addMessage(Logger.formatPrefix("\u00a7bMighty Miner", message));
    }

    public static void sendWarning(String message) {
        Logger.addMessage("\u00a7c\u00a7l[WARNING] \u00a78\u00bb \u00a7e" + message);
    }

    public static void sendError(String message) {
        Logger.addMessage("\u00a7l\u00a74\u00a7kZ\u00a7r\u00a7l\u00a74[Mighty Miner]\u00a7kH\u00a7r \u00a78\u00bb \u00a7c" + message);
    }

    public static void sendNote(String message) {
        Logger.sendMessage(message);
    }

    public static void sendLog(String message) {
        if (Logger.isDuplicate("debug", message)) {
            return;
        }
        if (MightyMinerConfig.debugMode && Logger.mc.field_71439_g != null) {
            Logger.addMessage("\u00a7l\u00a72[Mighty Miner] \u00a78\u00bb \u00a77" + message);
        } else {
            System.out.println("[Mighty Miner] " + message);
        }
    }

    public static void sendNotification(String title, String message, Long duration) {
        if (Logger.isDuplicate("notification", message)) {
            return;
        }
        Notifications.INSTANCE.send(title, message, (float)duration.longValue());
    }

    private static boolean isDuplicate(String type, String message) {
        if (lastMessages.containsKey(type) && lastMessages.get(type).equals(message)) {
            return true;
        }
        lastMessages.put(type, message);
        return false;
    }

    private static String formatPrefix(String prefix, String message) {
        return EnumChatFormatting.RED + "[" + EnumChatFormatting.BLUE + prefix + EnumChatFormatting.RED + "] \u00a78\u00bb \u00a7e" + message;
    }

    protected void log(String message) {
        Logger.sendLog(this.formatMessage(message));
    }

    protected void send(String message) {
        Logger.sendMessage(this.formatMessage(message));
    }

    protected void error(String message) {
        Logger.sendError(this.formatMessage(message));
    }

    protected void warn(String message) {
        Logger.sendWarning(this.formatMessage(message));
    }

    protected void note(String message) {
        Logger.sendNote(this.formatMessage(message));
    }

    protected String formatMessage(String message) {
        return "[" + this.getName() + "] " + message;
    }
}

