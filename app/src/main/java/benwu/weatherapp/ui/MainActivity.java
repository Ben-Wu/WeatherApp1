package benwu.weatherapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import benwu.weatherapp.R;
import benwu.weatherapp.data.SearchHistoryDbHelper;
import benwu.weatherapp.utils.DateUtils;
import benwu.weatherapp.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SearchHistoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new SearchHistoryDbHelper(this);
        Log.i(TAG, cursorToString(mDbHelper.query()));
    }

    public void showWeatherInfo(View view) {
        String location = ((EditText)findViewById(R.id.locationField)).getText().toString();
        String country = ((EditText)findViewById(R.id.countryField)).getText().toString();

        if(!NetworkHelper.isConnected(this)) {
            Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if(location.length() == 0) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, WeatherInfoActivity.class);
        intent.putExtra(WeatherInfoActivity.KEY_LOCATION, location);
        intent.putExtra(WeatherInfoActivity.KEY_COUNTRY, country);

        mDbHelper.insert(location, country, DateUtils.getCurrentTime());

        startActivity(intent);
    }

    private String cursorToString(Cursor c) {
        if(c.getCount() == 0)
            return "EMPTY";
        c.moveToFirst();
        StringBuilder stringBuilder = new StringBuilder("");

        do {
            for (int i = 0; i < c.getColumnCount(); i++) {
                stringBuilder.append(c.getString(i));
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
        } while(c.moveToNext());
        return stringBuilder.toString();
    }
}
