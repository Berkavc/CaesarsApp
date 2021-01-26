package com.example.sezar.logger;

import com.zc.logger.LogManager;

public class LogUtil {
    private static volatile boolean writeLogs = true;

    public static void writeLogs(boolean writeLogs) {
        LogUtil.writeLogs = writeLogs;
    }

    private static boolean showLogs(String tag, String msg) {
        return writeLogs;
    }

    public static void v(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (showLogs(tag, msg)) {
            LogManager.getInstance().e(tag, msg);
        }
    }
}
