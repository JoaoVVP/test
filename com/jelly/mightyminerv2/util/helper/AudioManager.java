/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.Multithreading
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.SoundCategory
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.jelly.mightyminerv2.util.helper;

import cc.polyfrost.oneconfig.utils.Multithreading;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.helper.Clock;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AudioManager {
    private static volatile AudioManager instance;
    private final Minecraft mc = Minecraft.func_71410_x();
    private boolean minecraftSoundEnabled = false;
    private final Clock delayBetweenPings = new Clock();
    private int numSounds = 15;
    private float soundBeforeChange = 0.0f;
    private static Clip clip;

    private AudioManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static AudioManager getInstance() {
        if (instance != null) return instance;
        Class<AudioManager> clazz = AudioManager.class;
        synchronized (AudioManager.class) {
            if (instance != null) return instance;
            instance = new AudioManager();
            // ** MonitorExit[var0] (shouldn't be in output)
            return instance;
        }
    }

    public void resetSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        this.minecraftSoundEnabled = false;
        if (MightyMinerConfig.maxOutMinecraftSounds) {
            this.mc.field_71474_y.func_151439_a(SoundCategory.MASTER, this.soundBeforeChange);
        }
    }

    public void playSound() {
        if (!MightyMinerConfig.failsafeSoundType) {
            if (this.minecraftSoundEnabled) {
                return;
            }
            this.startMinecraftSound();
        } else {
            this.playCustomSound();
        }
    }

    private void startMinecraftSound() {
        this.numSounds = 15;
        this.minecraftSoundEnabled = true;
        if (MightyMinerConfig.maxOutMinecraftSounds) {
            this.mc.field_71474_y.func_151439_a(SoundCategory.MASTER, 1.0f);
        }
    }

    private void playCustomSound() {
        Multithreading.schedule(() -> {
            try {
                AudioInputStream inputStream = this.getAudioStreamForSelectedSound();
                if (inputStream == null) {
                    Logger.sendError("[Audio Manager] Failed to load sound file!");
                    return;
                }
                clip = AudioSystem.getClip();
                clip.open(inputStream);
                this.setSoundVolume(MightyMinerConfig.failsafeSoundVolume);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
            catch (Exception e) {
                Logger.sendError("[Audio Manager] Error playing sound: " + e.getMessage());
            }
        }, (long)0L, (TimeUnit)TimeUnit.MILLISECONDS);
    }

    private AudioInputStream getAudioStreamForSelectedSound() throws Exception {
        switch (MightyMinerConfig.failsafeSoundSelected) {
            case 0: {
                File audioFile = new File(this.mc.field_71412_D.getAbsolutePath() + "/farmhelper_sound.wav");
                if (!audioFile.exists() || !audioFile.isFile()) break;
                return AudioSystem.getAudioInputStream(audioFile);
            }
            case 1: {
                return AudioSystem.getAudioInputStream(this.getClass().getResource("/farmhelper/sounds/staff_check_voice_notification.wav"));
            }
            case 2: {
                return AudioSystem.getAudioInputStream(this.getClass().getResource("/farmhelper/sounds/metal_pipe.wav"));
            }
            case 3: {
                return AudioSystem.getAudioInputStream(this.getClass().getResource("/farmhelper/sounds/AAAAAAAAAA.wav"));
            }
            case 4: {
                return AudioSystem.getAudioInputStream(this.getClass().getResource("/farmhelper/sounds/loud_buzz.wav"));
            }
            default: {
                Logger.sendError("[Audio Manager] Invalid sound selection!");
            }
        }
        return null;
    }

    private void setSoundVolume(float volumePercentage) {
        FloatControl volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float)(Math.log(volumePercentage / 100.0f) / Math.log(10.0) * 20.0);
        volume.setValue(dB);
    }

    public boolean isSoundPlaying() {
        return clip != null && clip.isRunning() || this.minecraftSoundEnabled;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        if (MightyMinerConfig.failsafeSoundType || !this.minecraftSoundEnabled) {
            return;
        }
        this.handleMinecraftSoundTick();
    }

    private void handleMinecraftSoundTick() {
        if (this.delayBetweenPings.isScheduled() && !this.delayBetweenPings.passed()) {
            return;
        }
        if (this.numSounds <= 0) {
            this.resetSound();
            return;
        }
        String soundEvent = MightyMinerConfig.failsafeMcSoundSelected == 0 ? "random.orb" : "random.anvil_land";
        this.mc.field_71441_e.func_72980_b(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u, this.mc.field_71439_g.field_70161_v, soundEvent, 10.0f, 1.0f, false);
        this.delayBetweenPings.schedule(100L);
        --this.numSounds;
    }

    public boolean isMinecraftSoundEnabled() {
        return this.minecraftSoundEnabled;
    }

    public void setMinecraftSoundEnabled(boolean minecraftSoundEnabled) {
        this.minecraftSoundEnabled = minecraftSoundEnabled;
    }

    public void setSoundBeforeChange(float soundBeforeChange) {
        this.soundBeforeChange = soundBeforeChange;
    }
}

