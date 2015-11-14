package benwu.weatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class NetworkHelper {

    public final static String TAG = "NetworkHelper";

    private static OkHttpClient sClient;

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String downloadJsonContent(String urlString) throws IOException {
        LogUtils.LOGI(TAG, "Downloading JSON from: " + urlString);
        if(sClient == null) {
            sClient = new OkHttpClient();
            sClient.setConnectTimeout(5, TimeUnit.SECONDS);
            sClient.setReadTimeout(5, TimeUnit.SECONDS);
        }

        Request request = new Request.Builder()
                .url(urlString)
                .build();

        Response response = sClient.newCall(request).execute();
        return response.body().string();
    }
}
