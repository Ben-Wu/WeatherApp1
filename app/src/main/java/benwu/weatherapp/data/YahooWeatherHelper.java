package benwu.weatherapp.data;

import org.json.JSONObject;

import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

/**
 * Created by Ben Wu on 11/15/2015.
 */
public class YahooWeatherHelper {

    private static final String TAG = "YahooWeatherHelper";

    private static final String BASE_URL =
            "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22";
    private static final String URL_END = "%22%29&format=json&diagnostics=true&callback=";

    public static WeatherDataObject getDataFor(String... location) {
        String jsonContent;
        String locationString = "";

        for(String s : location) {
            locationString += s+",";
        }
        locationString = locationString.substring(0, locationString.length()-1);

        try {
            jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + locationString + URL_END);
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
        mainData = root.optJSONObject("query").optJSONObject("results");
        if(mainData == null)
            return null;

        mainData = mainData.optJSONObject("channel");

        weather.setTime(mainData.optString("lastBuildDate"));
        weather.setLocation(mainData.optJSONObject("location").optString("city"));
        weather.setCurTemp((mainData.optJSONObject("item").optJSONObject("condition").optDouble("temp")-32)*5/9); // convert to c
        weather.setHumidity(mainData.optJSONObject("atmosphere").optString("humidity"));
        weather.setDescription(mainData.optJSONObject("item").optJSONObject("condition").optString("text"));

        return weather;
    }
}
