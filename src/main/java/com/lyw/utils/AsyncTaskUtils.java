package com.lyw.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AsyncTaskUtils {

    public static void run(TimerTask r) {
        run(r, 0, TimeUnit.SECONDS);
    }

    public static void run(TimerTask r, long delay, TimeUnit timeUnit) {
        new Timer().schedule(r, timeUnit.toMillis(delay));
    }

    public static void schedule(TimerTask r, long gap, TimeUnit timeUnit) {
        schedule(r, 0, gap, timeUnit);
    }

    public static void schedule(TimerTask r, long delay, long gap, TimeUnit timeUnit) {
        new Timer().schedule(r, timeUnit.toMillis(delay), timeUnit.toMillis(gap));
    }

}
