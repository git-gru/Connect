package com.jarrebnnee.connect.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vi6 on 05-May-17.
 */

public class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
