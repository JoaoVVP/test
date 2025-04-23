/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.io;

import java.io.IOException;

public interface MeasurableStream {
    public long length() throws IOException;

    public long position() throws IOException;
}

