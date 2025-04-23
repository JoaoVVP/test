/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MouseHelper
 *  org.lwjgl.input.Mouse
 */
package com.jelly.mightyminerv2.feature.impl;

import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class MouseUngrab
extends AbstractFeature {
    private static volatile MouseUngrab instance;
    private MouseHelper oldMouseHelper;
    private boolean mouseUngrabbed = false;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static MouseUngrab getInstance() {
        if (instance != null) return instance;
        Class<MouseUngrab> clazz = MouseUngrab.class;
        synchronized (MouseUngrab.class) {
            if (instance != null) return instance;
            instance = new MouseUngrab();
            // ** MonitorExit[var0] (shouldn't be in output)
            return instance;
        }
    }

    public void ungrabMouse() {
        if (this.mouseUngrabbed || !Mouse.isGrabbed()) {
            return;
        }
        this.oldMouseHelper = this.mc.field_71417_B;
        this.mc.field_71417_B.func_74373_b();
        this.mc.field_71417_B = new MouseHelper(){

            public void func_74374_c() {
            }

            public void func_74372_a() {
            }

            public void func_74373_b() {
            }
        };
        this.mouseUngrabbed = true;
        this.log("Mouse ungrabbed successfully.");
    }

    public void regrabMouse() {
        if (!this.mouseUngrabbed) {
            return;
        }
        if (this.oldMouseHelper != null) {
            this.mc.field_71417_B = this.oldMouseHelper;
            this.oldMouseHelper = null;
        }
        if (this.mc.field_71462_r == null) {
            this.mc.field_71417_B.func_74372_a();
        }
        this.mouseUngrabbed = false;
        this.log("Mouse regrabbed successfully.");
    }

    @Override
    public String getName() {
        return "Ungrab Mouse";
    }

    @Override
    public boolean isEnabled() {
        return MightyMinerConfig.ungrabMouse;
    }

    @Override
    public boolean shouldStartAtLaunch() {
        return this.isEnabled();
    }

    @Override
    public void start() {
        this.log("MouseUngrab::start");
        try {
            this.ungrabMouse();
        }
        catch (Exception e) {
            this.log("Failed to ungrab mouse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        this.log("MouseUngrab::stop");
        try {
            this.regrabMouse();
        }
        catch (Exception e) {
            this.log("Failed to regrab mouse: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

