package benwu.weatherapp.data;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class DataManager {

    private static DataManager sDataManager;

    private WeatherDataObject[] mWeatherData = new WeatherDataObject[3];

    public static DataManager getInstance() {
        if(sDataManager == null) {
            sDataManager = new DataManager();
        }

        return sDataManager;
    }

    public void setWeatherData(WeatherDataObject[] weatherData) {
        this.mWeatherData = weatherData;
    }

    public WeatherDataObject[] getWeatherData() {
        return mWeatherData;
    }
}
