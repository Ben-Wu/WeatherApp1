package benwu.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import benwu.weatherapp.data.OpenWeatherHelper;
import benwu.weatherapp.data.WeatherDataObject;

/**
 * Created by Ben Wu on 11/14/2015.
 */
public class WeatherInfoActivity extends AppCompatActivity {

    public static final String TAG = "WeatherInfoActivity";

    public static final String KEY_LOCATION = "LOCATION";

    private WeatherDataObject mWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String location = getIntent().getStringExtra(KEY_LOCATION);
        if(location == null)
            displayErrorMessage();
        else
            new RetrieveDataTask().execute(location);
    }

    private void displayErrorMessage() {
        Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show();
    }

    private void setupUi() {
        ((TextView)findViewById(R.id.locationName)).setText(mWeather.getLocation());
        ((TextView)findViewById(R.id.minTemp)).setText(""+mWeather.getMinTemp());
        ((TextView)findViewById(R.id.maxTemp)).setText(""+mWeather.getMaxTemp());
        ((TextView)findViewById(R.id.curTemp)).setText(""+mWeather.getCurTemp());
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
}
