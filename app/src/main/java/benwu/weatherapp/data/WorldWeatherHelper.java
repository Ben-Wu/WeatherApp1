package benwu.weatherapp.data;

import org.json.JSONObject;

import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/15/2015.
 */
public class WorldWeatherHelper {

    private static final String TAG = "YahooWeatherHelper";

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
        if(root.optJSONObject("data").has("error"))
            return null;

        mainData = root.optJSONObject("data").optJSONArray("current_condition").optJSONObject(0);

        weather.setTime(mainData.optString("observation_time") + " GMT");
        weather.setLocation(root.optJSONObject("data").optJSONArray("request").optJSONObject(0).optString("query"));
        weather.setCurTemp(mainData.optDouble("temp_C"));
        weather.setHumidity(mainData.optString("humidity"));
        weather.setDescription(mainData.optJSONArray("weatherDesc").optJSONObject(0).optString("value"));

        return weather;
    }
}
