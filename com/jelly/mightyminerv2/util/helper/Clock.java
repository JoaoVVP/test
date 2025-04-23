/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.util.helper;

public class Clock {
    private long deltaTime;
    private boolean paused;
    private boolean scheduled;
    private long endTime;
    private long startTime;

    public void schedule(long milliseconds) {
        this.endTime = System.currentTimeMillis() + milliseconds;
        this.deltaTime = milliseconds;
        this.scheduled = true;
        this.paused = false;
    }

    public void schedule(double milliseconds) {
        this.endTime = System.currentTimeMillis() + (long)milliseconds;
        this.deltaTime = (long)milliseconds;
        this.scheduled = true;
        this.paused = false;
    }

    public boolean passed() {
        return System.currentTimeMillis() >= this.endTime;
    }

    public void pause() {
        if (this.scheduled && !this.paused) {
            this.deltaTime = this.endTime - System.currentTimeMillis();
            this.paused = true;
        }
    }

    public void resume() {
        if (this.scheduled && this.paused) {
            this.endTime = System.currentTimeMillis() + this.deltaTime;
            this.paused = false;
        }
    }

    public long getRemainingTime() {
        if (this.paused) {
            return this.deltaTime;
        }
        return Math.max(0L, this.endTime - System.currentTimeMillis());
    }

    public void start(boolean reset) {
        if (!this.scheduled || reset) {
            this.startTime = System.currentTimeMillis();
        } else {
            this.resumeTimer();
        }
        this.scheduled = true;
    }

    public void stop(boolean reset) {
        if (!this.scheduled || reset) {
            this.reset();
        } else {
            this.pauseTimer();
        }
    }

    public long getTimePassed() {
        if (!this.scheduled || this.paused) {
            return this.deltaTime;
        }
        return System.currentTimeMillis() - this.startTime;
    }

    public void pauseTimer() {
        if (this.scheduled && !this.paused) {
            this.deltaTime = System.currentTimeMillis() - this.startTime;
            this.paused = true;
        }
    }

    public void resumeTimer() {
        if (this.scheduled && this.paused) {
            this.startTime = System.currentTimeMillis() - this.deltaTime;
            this.paused = false;
        }
    }

    public void reset() {
        this.scheduled = false;
        this.paused = false;
        this.endTime = 0L;
        this.deltaTime = 0L;
    }

    public boolean isScheduled() {
        return this.scheduled;
    }
}

