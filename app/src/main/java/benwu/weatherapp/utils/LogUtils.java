package benwu.weatherapp.utils;

import android.util.Log;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class LogUtils {
    private static final boolean DEBUG = true;

    public static void LOGV(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void LOGD(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void LOGI(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void LOGW(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void LOGE(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void LOGA(String tag, String msg) {
        Log.wtf(tag, msg);
    }
}
