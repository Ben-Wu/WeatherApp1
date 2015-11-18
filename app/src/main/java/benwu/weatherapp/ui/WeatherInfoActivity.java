package benwu.weatherapp.ui;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import benwu.weatherapp.R;
import benwu.weatherapp.data.AerisWeatherHelper;
import benwu.weatherapp.data.DataManager;
import benwu.weatherapp.data.OpenWeatherHelper;
import benwu.weatherapp.data.WUndergroundHelper;
import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.data.WorldWeatherHelper;
import benwu.weatherapp.data.YahooWeatherHelper;
import benwu.weatherapp.utils.Data;

/**
 * Created by Ben Wu on 11/14/2015.
 */

public class WeatherInfoActivity extends AppCompatActivity {

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

        setupToolbar(location, country);

        if(location == null)
            displayErrorMessage();
        else {
            mTask = new RetrieveDataTask();
            if (country == null || country.isEmpty()) {
                mTask.execute(location);
            } else {
                mTask.execute(country, location);
            }
        }

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

    private void setupToolbar(String location, String country) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.weather_info_toolbar);
        toolbar.setTitle(location);
        if(!country.isEmpty())
            toolbar.setSubtitle(country);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
    }

    private void displayErrorMessage() {
        Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show();
    }

    private class RetrieveDataTask extends AsyncTask<String, Void, WeatherDataObject[]> {
        @Override
        protected WeatherDataObject[] doInBackground(String... params) {
            WeatherDataObject[] weatherData = new WeatherDataObject[5];

            weatherData[0] = OpenWeatherHelper.getDataFor(Data.getOpenweatherkey(), params);
            weatherData[1] = WUndergroundHelper.getDataFor(Data.getWundergroundkey(), params);
            weatherData[2] = YahooWeatherHelper.getDataFor(params);
            weatherData[3] = WorldWeatherHelper.getDataFor(Data.getWorldweatherkey(), params);
            weatherData[4] = AerisWeatherHelper.getDataFor(Data.getAeriskey(), params);

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
