package benwu.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import benwu.weatherapp.data.OpenWeatherHelper;
import benwu.weatherapp.data.WeatherDataObject;
import benwu.weatherapp.utils.LogUtils;
import benwu.weatherapp.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showWeatherInfo(View view) {
        String location = ((EditText)findViewById(R.id.locationField)).getText().toString();

        if(location.length() == 0) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, WeatherInfoActivity.class);
        intent.putExtra(WeatherInfoActivity.KEY_LOCATION, location);
        startActivity(intent);
    }
}
