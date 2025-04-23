/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.feature;

import com.jelly.mightyminerv2.failsafe.AbstractFailsafe;
import com.jelly.mightyminerv2.feature.AbstractFeature;
import com.jelly.mightyminerv2.feature.impl.AutoChestUnlocker;
import com.jelly.mightyminerv2.feature.impl.AutoCommissionClaim;
import com.jelly.mightyminerv2.feature.impl.AutoDrillRefuel;
import com.jelly.mightyminerv2.feature.impl.AutoInventory;
import com.jelly.mightyminerv2.feature.impl.AutoMobKiller;
import com.jelly.mightyminerv2.feature.impl.AutoWarp;
import com.jelly.mightyminerv2.feature.impl.BlockMiner;
import com.jelly.mightyminerv2.feature.impl.MobTracker;
import com.jelly.mightyminerv2.feature.impl.MouseUngrab;
import com.jelly.mightyminerv2.feature.impl.Nuker;
import com.jelly.mightyminerv2.feature.impl.Pathfinder;
import com.jelly.mightyminerv2.feature.impl.RouteBuilder;
import com.jelly.mightyminerv2.feature.impl.RouteNavigator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FeatureManager {
    private static FeatureManager instance;
    public final Set<AbstractFeature> allFeatures = new LinkedHashSet<AbstractFeature>();

    public static FeatureManager getInstance() {
        if (instance == null) {
            instance = new FeatureManager();
        }
        return instance;
    }

    public FeatureManager() {
        this.allFeatures.addAll(Arrays.asList(AutoCommissionClaim.getInstance(), AutoInventory.getInstance(), AutoMobKiller.getInstance(), AutoWarp.getInstance(), BlockMiner.getInstance(), MouseUngrab.getInstance(), Pathfinder.getInstance(), RouteBuilder.getInstance(), RouteNavigator.getInstance(), MobTracker.getInstance(), AutoDrillRefuel.getInstance(), AutoChestUnlocker.instance, Nuker.getInstance()));
    }

    public void enableAll() {
        this.allFeatures.forEach(it -> {
            if (it.shouldStartAtLaunch()) {
                it.start();
            }
        });
    }

    public void disableAll() {
        this.allFeatures.forEach(it -> {
            if (it.isRunning()) {
                it.stop();
            }
        });
    }

    public void pauseAll() {
        this.allFeatures.forEach(it -> {
            if (it.isRunning()) {
                it.pause();
            }
        });
    }

    public void resumeAll() {
        this.allFeatures.forEach(it -> {
            if (it.isRunning()) {
                it.resume();
            }
        });
    }

    public boolean shouldNotCheckForFailsafe() {
        return this.allFeatures.stream().filter(AbstractFeature::isRunning).anyMatch(AbstractFeature::shouldNotCheckForFailsafe);
    }

    public Set<AbstractFailsafe.Failsafe> getFailsafesToIgnore() {
        HashSet<AbstractFailsafe.Failsafe> failsafes = new HashSet<AbstractFailsafe.Failsafe>();
        this.allFeatures.forEach(it -> {
            if (it.isRunning()) {
                failsafes.addAll(it.getFailsafesToIgnore());
            }
        });
        return failsafes;
    }
}

