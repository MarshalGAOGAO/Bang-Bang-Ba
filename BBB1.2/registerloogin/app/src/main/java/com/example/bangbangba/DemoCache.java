package com.example.bangbangba;

import android.content.Context;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by jezhee on 2/20/15.
 */
public class DemoCache {
    private static  String token;

    private static Context context;

    private static String account;

    private static String accid;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static void setToken(String Token) {
        token = Token;
    }

    public static String getToken() {
        return token;
    }


    public static String getAccount() {
        return account;
    }

    private static boolean mainTaskLaunching;

    public static void setAccount(String account) {
        DemoCache.account = account;
    }

    public static void setAccid(String accid) {
        DemoCache.accid = accid;
    }

    public static String getAccid() {
        return accid;
    }




    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        DemoCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context.getApplicationContext();
    }

    public static void setMainTaskLaunching(boolean mainTaskLaunching) {
        DemoCache.mainTaskLaunching = mainTaskLaunching;
    }

    public static boolean isMainTaskLaunching() {
        return mainTaskLaunching;
    }
}
