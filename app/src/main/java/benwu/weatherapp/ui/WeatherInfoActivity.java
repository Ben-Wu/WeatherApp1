package benwu.weatherapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import benwu.weatherapp.R;
import benwu.weatherapp.data.DataManager;
import benwu.weatherapp.data.OpenWeatherHelper;
import benwu.weatherapp.data.WUndergroundHelper;
import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.data.YahooWeatherHelper;
import benwu.weatherapp.utils.Data;

/**
 * Created by Ben Wu on 11/14/2015.
 */

public class WeatherInfoActivity extends FragmentActivity {

    public static final String TAG = "WeatherInfoActivity";

    public static final String KEY_LOCATION = "LOCATION";
    public static final String KEY_COUNTRY = "COUNTRY";

    private RetrieveDataTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        String location = getIntent().getStringExtra(KEY_LOCATION);
        String country = getIntent().getStringExtra(KEY_COUNTRY);

        if(location == null)
            displayErrorMessage();
        else {
            mTask = new RetrieveDataTask();
            mTask.execute(country, location);
        }
        ((TextView)findViewById(R.id.location_field)).setText(location + "\n" + country);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        LoadingFragment fragment = new LoadingFragment();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mTask != null)
            mTask.cancel(true);
    }

    private void displayErrorMessage() {
        Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show();
    }

    private class RetrieveDataTask extends AsyncTask<String, Void, WeatherDataObject[]> {
        @Override
        protected WeatherDataObject[] doInBackground(String... params) {
            WeatherDataObject[] weatherData = new WeatherDataObject[3];

            weatherData[0] = OpenWeatherHelper.getDataFor(Data.getOpenweatherkey(), params[0], params[1]);
            weatherData[1] = WUndergroundHelper.getDataFor(Data.getWundergroundkey(), params[0], params[1]);
            weatherData[2] = YahooWeatherHelper.getDataFor(params[0], params[1]);

            return weatherData;
        }

        @Override
        protected void onPostExecute(WeatherDataObject[] weatherDataResults) {
            if(weatherDataResults == null)
                displayErrorMessage();
            else {
                DataManager.getInstance().setWeatherData(weatherDataResults);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                WeatherInfoFragment fragment = new WeatherInfoFragment();
                transaction.replace(R.id.content_fragment, fragment);
                transaction.commit();
            }
        }
    }
}
