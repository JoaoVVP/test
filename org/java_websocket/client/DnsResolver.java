/*
 * Decompiled with CFR 0.152.
 */
package org.java_websocket.client;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public interface DnsResolver {
    public InetAddress resolve(URI var1) throws UnknownHostException;
}

