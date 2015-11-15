package benwu.weatherapp.data;

import org.json.JSONObject;

import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/15/2015.
 */
public class WUndergroundHelper {

    private static final String TAG = "WUndergroundHelper";

    private static final String BASE_URL = "http://api.wunderground.com/api/";
    private static final String PARAMS = "/conditions/";
    private static final String DATA_FORMAT = ".json";

    private static String mKey;

    public static WeatherDataObject getDataFor(String key, String... location) {
        String jsonContent;
        String locationString = "q/";

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
        WeatherDataObject data = router(jsonContent);
        LogUtils.LOGI(TAG, data == null ? "No data" : data.toString());
        return data;
    }

    private static WeatherDataObject router(String content) {
        JSONObject root;

        try {
            root = new JSONObject(content);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Parse: " + e);
            return null;
        }

        if(root.has("current_observation")) {
            return parse(content);
        } else if(root.optJSONObject("response").has("error")) {
            return null;
        }
        String locationUrl = root.optJSONObject("response").optJSONArray("results").optJSONObject(0).optString("l");
        return parse(BASE_URL + mKey + PARAMS + locationUrl + DATA_FORMAT);
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
        mainData = root.optJSONObject("current_observation");

        if(mainData == null)
            return null;

        weather.setLocation(mainData.optJSONObject("display_location").optString("full"));
        weather.setCurTemp(mainData.optDouble("temp_c"));
        weather.setTime(Long.parseLong(mainData.optString("observation_epoch")));
        weather.setHumidity(mainData.optString("relative_humidity").replaceAll("%", ""));
        weather.setDescription(mainData.optString("weather"));

        return weather;
    }
}
