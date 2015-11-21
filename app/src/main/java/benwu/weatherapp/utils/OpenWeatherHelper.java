package benwu.weatherapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import benwu.weatherapp.data.WeatherDataObject;
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
        WeatherDataObject data;
        try {
            data = parse(jsonContent);
        } catch(JSONException e) {
            data = null;
        }
        LogUtils.LOGI(TAG, data == null ? "No data" : data.toString());
        return data;
    }

    private static WeatherDataObject parse(String content) throws JSONException {
        WeatherDataObject weather = new WeatherDataObject();
        JSONObject root;
        JSONObject mainData;

        root = new JSONObject(content);

        mainData = root.getJSONObject("main");

        weather.setTime(root.getLong("dt"));
        weather.setLocation(root.getString("name"));
        weather.setCurTemp(mainData.getDouble("temp"));
        weather.setHumidity(mainData.getString("humidity"));
        weather.setDescription(root.getJSONArray("weather").getJSONObject(0).getString("main"));

        return weather;
    }
}
