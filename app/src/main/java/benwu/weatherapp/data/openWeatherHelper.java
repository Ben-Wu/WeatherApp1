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

    private static WeatherDataObject sWeatherDataObject;

    public static WeatherDataObject getDataFor(String location) {
        sWeatherDataObject= null;

        new ParserTask().execute(location);

        while(sWeatherDataObject == null);

        return sWeatherDataObject;
    }

    private static WeatherDataObject parse(String content) {
        WeatherDataObject weather = new WeatherDataObject();
        JSONObject root;
        JSONObject mainData;

        try {
            root = new JSONObject(content);
        } catch(Exception e) {
            LogUtils.LOGE(TAG, "Parse: " + e);
            return weather;
        }
        mainData = root.optJSONObject("main");

        weather.setLocation(root.optString("name"));
        weather.setCurTemp(mainData.optDouble("temp"));
        weather.setMinTemp(mainData.optDouble("temp_min"));
        weather.setMaxTemp(mainData.optDouble("temp_max"));
        weather.setHumidity(mainData.optDouble("humidity"));

        return weather;
    }

    private static class ParserTask extends AsyncTask<String, Void, WeatherDataObject> {

        protected WeatherDataObject doInBackground(String... params) {
            String jsonContent;

            try {
                jsonContent = NetworkHelper.downloadJsonContent(BASE_URL + params[0] + PARAMS + API_KEY);
            } catch(Exception e) {
                LogUtils.LOGE(TAG, "Download JSON: " + e);
                return new WeatherDataObject();
            }
            WeatherDataObject ob = parse(jsonContent);
            LogUtils.LOGD(TAG, ob.toString());
            sWeatherDataObject = ob;
            return ob;
        }
    }
}
