/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.config.annotations.Switch
 *  cc.polyfrost.oneconfig.config.core.OneColor
 *  cc.polyfrost.oneconfig.hud.TextHud
 */
package com.jelly.mightyminerv2.hud;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.TextHud;
import com.jelly.mightyminerv2.macro.commissionmacro.CommissionMacro;
import java.util.List;

public class CommissionHUD
extends TextHud {
    private static transient CommissionHUD instance = new CommissionHUD();
    @Switch(name="Reset Stats When Disabled")
    public boolean commHudResetStats = false;
    private final transient CommissionMacro macro = CommissionMacro.getInstance();

    public static CommissionHUD getInstance() {
        return instance;
    }

    public CommissionHUD() {
        super(true, 1.0f, 10.0f, 1.0f, true, true, 1.0f, 5.0f, 5.0f, new OneColor(0, 0, 0, 150), false, 2.0f, new OneColor(0, 0, 0, 127));
    }

    protected void getLines(List<String> lines, boolean example) {
        long uptime = this.macro.uptime.getTimePassed() / 1000L;
        int totalComms = this.macro.getCompletedCommissions();
        int commsPerHour = 0;
        if (uptime > 0L) {
            commsPerHour = (int)((float)totalComms / (float)uptime * 3600.0f);
        }
        lines.add("\u00a76\u00a7lCommission Macro Stats");
        lines.add("\u00a77Runtime: \u00a7f" + this.formatElapsedTime(uptime));
        lines.add("\u00a77Total Commissions: \u00a7f" + totalComms);
        lines.add("\u00a77HOTM Exp Gained: \u00a7f" + totalComms * 400);
        lines.add("\u00a77Commissions per Hour: \u00a7f" + commsPerHour);
        lines.add("\u00a77HOTM Exp/H: \u00a7f" + commsPerHour * 400);
        if (this.macro.isEnabled()) {
            lines.add("\u00a77Active Commissions:");
            this.macro.getActiveCommissions().forEach(it -> lines.add("  \u00a7f" + it.getName()));
        }
    }

    private String formatElapsedTime(long elapsedTimeSeconds) {
        long seconds = elapsedTimeSeconds % 60L;
        long minutes = elapsedTimeSeconds / 60L % 60L;
        long hours = elapsedTimeSeconds / 3600L;
        return String.format("%02d:%02d:%02d%s", hours, minutes, seconds, !this.macro.isEnabled() ? " (Paused)" : "");
    }
}

