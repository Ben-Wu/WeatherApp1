package benwu.weatherapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/17/2015.
 */
public class AerisWeatherHelper {

    private static final String TAG = "AerisWeatherHelper";

    private static final String BASE_URL = "http://api.aerisapi.com/observations/%s?client_id=%s&client_secret=%s";

    public static WeatherDataObject getDataFor(String[] key, String... location) {
        String jsonContent;
        String locationString = "";

        for(int i = location.length-1 ; i >= 0 ; i--) {
            locationString += location[i]+",";
        }
        locationString = locationString.substring(0, locationString.length()-1);

        try {
            jsonContent = NetworkHelper.downloadJsonContent(String.format(BASE_URL,
                    locationString, key[0], key[1]));
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

        mainData = root.getJSONObject("response");

        weather.setTime(mainData.getLong("obTimestamp"));
        weather.setLocation(mainData.getJSONObject("place").getString("name"));
        weather.setCurTemp(mainData.getJSONObject("ob").getDouble("tempC"));
        weather.setHumidity(mainData.getJSONObject("ob").getString("humidity"));
        weather.setDescription(mainData.getJSONObject("ob").getString("weather"));

        return weather;
    }
}
