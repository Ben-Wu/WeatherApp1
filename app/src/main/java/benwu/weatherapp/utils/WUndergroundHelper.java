package benwu.weatherapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/15/2015.
 */
public class WUndergroundHelper {

    private static final String TAG = "WUndergroundHelper";

    private static final String BASE_URL = "http://api.wunderground.com/api/";
    private static final String PARAMS = "/conditions";
    private static final String DATA_FORMAT = ".json";

    private static String mKey;

    public static WeatherDataObject getDataFor(String key, String... location) {
        String jsonContent;
        String locationString = "/q/";

        for(String s : location) {
            locationString += s+"/";
        }
        locationString = locationString.substring(0, locationString.length()-1);

        mKey = key;

        try {
            jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + mKey + PARAMS + locationString + DATA_FORMAT);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Download JSON: " + e);
            return null;
        }
        WeatherDataObject data;
        try {
            data = router(jsonContent);
        } catch(JSONException e) {
            data = null;
        }
        LogUtils.LOGI(TAG, data == null ? "No data" : data.toString());
        return data;
    }

    private static WeatherDataObject router(String content) throws JSONException {
        JSONObject root;

        root = new JSONObject(content);

        if(root.has("current_observation")) {
            return parse(content);
        } else if(root.getJSONObject("response").has("error")) {
            return null;
        }
        String locationUrl = root.getJSONObject("response").getJSONArray("results").getJSONObject(0).getString("l");
        String jsonContent;
        try {
            jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + mKey + PARAMS + locationUrl + DATA_FORMAT);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Download JSON: " + e);
            return null;
        }
        return parse(jsonContent);
    }

    private static WeatherDataObject parse(String content) throws JSONException {
        WeatherDataObject weather = new WeatherDataObject();
        JSONObject root;
        JSONObject mainData;

        root = new JSONObject(content);

        mainData = root.getJSONObject("current_observation");

        if(mainData == null)
            return null;

        weather.setLocation(mainData.getJSONObject("display_location").getString("full"));
        weather.setCurTemp(mainData.getDouble("temp_c"));
        weather.setTime(Long.parseLong(mainData.getString("observation_epoch")));
        weather.setHumidity(mainData.getString("relative_humidity").replaceAll("%", ""));
        weather.setDescription(mainData.getString("weather"));

        return weather;
    }
}
