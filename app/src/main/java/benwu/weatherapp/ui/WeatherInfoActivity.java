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
import benwu.weatherapp.data.WeatherDataObject;

/**
 * Created by Ben Wu on 11/14/2015.
 */

public class WeatherInfoActivity extends FragmentActivity {

    public static final String TAG = "WeatherInfoActivity";

    public static final String KEY_LOCATION = "LOCATION";

    private RetrieveDataTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        String location = getIntent().getStringExtra(KEY_LOCATION);
        if(location == null)
            displayErrorMessage();
        else {
            mTask = new RetrieveDataTask();
            mTask.execute(location);
        }
        ((TextView)findViewById(R.id.location_field)).setText(location);

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

            weatherData[0] = OpenWeatherHelper.getDataFor(params[0]);
            weatherData[1] = OpenWeatherHelper.getDataFor("Ottawa");
            weatherData[2] = OpenWeatherHelper.getDataFor("Happy Valley Goose Bay");

            for(WeatherDataObject weatherObject : weatherData) {
                if(weatherObject == null) return null;
            }

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

/*public class WeatherInfoActivity extends AppCompatActivity {

    public static final String TAG = "WeatherInfoActivity";

    public static final String KEY_LOCATION = "LOCATION";

    private WeatherDataObject mWeather;

    private RetrieveDataTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_circle);

        String location = getIntent().getStringExtra(KEY_LOCATION);
        if(location == null)
            displayErrorMessage();
        else {
            mTask = new RetrieveDataTask();
            mTask.execute(location);
        }
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

    private void setupUi() {
        ((TextView)findViewById(R.id.locationName)).setText(mWeather.getLocation());
        ((TextView)findViewById(R.id.minTemp)).setText(String.valueOf(mWeather.getMinTemp()));
        ((TextView)findViewById(R.id.maxTemp)).setText(String.valueOf(mWeather.getMaxTemp()));
        ((TextView)findViewById(R.id.curTemp)).setText(String.valueOf(mWeather.getCurTemp()));
        ((TextView)findViewById(R.id.conditions)).setText(mWeather.getDescription());
    }

    private class RetrieveDataTask extends AsyncTask<String, Void, WeatherDataObject> {
        @Override
        protected WeatherDataObject doInBackground(String... params) {
            return OpenWeatherHelper.getDataFor(params[0]);
        }

        @Override
        protected void onPostExecute(WeatherDataObject weatherDataObject) {
            mWeather = weatherDataObject;
            if(mWeather == null)
                displayErrorMessage();
            else {
                setContentView(R.layout.activity_weather_info);
                setupUi();
            }
        }
    }
}*/
