package benwu.weatherapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import benwu.weatherapp.data.WeatherDataObject;
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
        WeatherDataObject data;
        try{
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

        mainData = root.getJSONObject("query").getJSONObject("results");

        mainData = mainData.getJSONObject("channel");

        weather.setTime(mainData.getString("lastBuildDate"));
        weather.setLocation(mainData.getJSONObject("location").getString("city"));
        weather.setCurTemp((mainData.getJSONObject("item").getJSONObject("condition").getDouble("temp")-32)*5/9); // convert to c
        weather.setHumidity(mainData.getJSONObject("atmosphere").getString("humidity"));
        weather.setDescription(mainData.getJSONObject("item").getJSONObject("condition").getString("text"));

        return weather;
    }
}
