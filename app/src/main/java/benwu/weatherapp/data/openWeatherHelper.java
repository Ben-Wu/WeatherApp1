package benwu.weatherapp.data;

import android.os.AsyncTask;

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
    private static final String API_KEY = "&appid=e640a73de96605837e6ede1be773ff6c";

    public static WeatherDataObject getDataFor(String... location) {
        String jsonContent;
        String locationString = "";

        for(String s : location) {
            locationString += s+",";
        }
        locationString = locationString.substring(0, locationString.length()-1);

        try {
            jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + locationString + PARAMS + API_KEY);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Download JSON: " + e);
            return null;
        }
        return parse(jsonContent);
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

        weather.setLocation(root.optString("name"));
        weather.setCurTemp(mainData.optDouble("temp"));
        weather.setMinTemp(mainData.optDouble("temp_min"));
        weather.setMaxTemp(mainData.optDouble("temp_max"));
        weather.setHumidity(mainData.optDouble("humidity"));
        weather.setDescription(root.optJSONArray("weather").optJSONObject(0).optString("main"));

        return weather;
    }
}
