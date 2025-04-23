/*
 * Decompiled with CFR 0.152.
 */
package org.java_websocket.exceptions;

import java.io.IOException;
import org.java_websocket.WebSocket;

public class WrappedIOException
extends Exception {
    private final transient WebSocket connection;
    private final IOException ioException;

    public WrappedIOException(WebSocket connection, IOException ioException) {
        this.connection = connection;
        this.ioException = ioException;
    }

    public WebSocket getConnection() {
        return this.connection;
    }

    public IOException getIOException() {
        return this.ioException;
    }
}

