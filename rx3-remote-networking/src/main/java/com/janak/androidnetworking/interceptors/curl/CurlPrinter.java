package com.janak.androidnetworking.interceptors.curl;

import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by Janak Khimsuriya on 01/11/2020.
 */
public class CurlPrinter {

    /**
     * Drawing toolbox
     */
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";

    private static String sTag = "CURL";

    public static void print(@Nullable String tag, String url, String msg) {
        // setting tag if not null
        if (tag != null)
            sTag = tag;

        String logMsg = "\n" + "\n" +
                "URL: " + url +
                "\n" +
                SINGLE_DIVIDER +
                "\n" +
                msg +
                " " +
                " \n" +
                SINGLE_DIVIDER +
                " \n ";
        log(logMsg);
    }

    private static void log(String msg) {
        Log.d(sTag, msg);
    }
}