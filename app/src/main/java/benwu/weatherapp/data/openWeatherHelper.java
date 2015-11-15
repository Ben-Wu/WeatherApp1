package benwu.weatherapp.data;

import android.util.Log;

import org.json.JSONObject;

import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class OpenWeatherHelper {

    private static final String TAG = "OpenWeatherHelper";

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String PARAMS = "&units=metric";

    public static WeatherDataObject getDataFor(String key, String... location) {
        String jsonContent;
        String locationString = "";

        for(String s : location) {
            locationString += s+",";
        }
        locationString = locationString.substring(0, locationString.length()-1);

        try {
            jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + locationString + PARAMS + key);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Download JSON: " + e);
            return null;
        }
        WeatherDataObject data = parse(jsonContent);
        LogUtils.LOGI(TAG, data == null ? "No data" : data.toString());
        return data;
    }

    private static WeatherDataObject parse(String content) {
        WeatherDataObject weather = new WeatherDataObject();
        JSONObject root;
        JSONObject mainData;

        try {
            root = new JSONObject(content);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Parse: " + e);
            return null;
        }
        mainData = root.optJSONObject("main");

        if(mainData == null)
            return null;

        weather.setTime(root.optLong("dt"));
        weather.setLocation(root.optString("name"));
        weather.setCurTemp(mainData.optDouble("temp"));
        weather.setHumidity(mainData.optString("humidity"));
        weather.setDescription(root.optJSONArray("weather").optJSONObject(0).optString("main"));

        return weather;
    }
}
