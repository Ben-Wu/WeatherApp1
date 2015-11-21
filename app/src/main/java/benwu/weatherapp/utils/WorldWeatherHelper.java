package benwu.weatherapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/15/2015.
 */
public class WorldWeatherHelper {

    private static final String TAG = "WorldWeatherHelper";

    private static final String BASE_URL =
            "http://api.worldweatheronline.com/free/v2/weather.ashx?q=";
    private static final String PARAMS = "&format=json&num_of_days=1&key=";

    public static WeatherDataObject getDataFor(String key, String... location) {
        String jsonContent;
        String locationString = "";

        for(int i = location.length-1 ; i >= 0 ; i--) {
            locationString += location[i]+",";
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

        mainData = root.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);

        weather.setTime(mainData.getString("observation_time") + " GMT");
        weather.setLocation(root.getJSONObject("data").getJSONArray("request").getJSONObject(0).getString("query"));
        weather.setCurTemp(mainData.getDouble("temp_C"));
        weather.setHumidity(mainData.getString("humidity"));
        weather.setDescription(mainData.getJSONArray("weatherDesc").getJSONObject(0).getString("value"));

        return weather;
    }
}
