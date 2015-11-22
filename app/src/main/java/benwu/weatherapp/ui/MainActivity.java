package benwu.weatherapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import benwu.weatherapp.R;
import benwu.weatherapp.data.SearchHistoryContract;
import benwu.weatherapp.data.SearchHistoryDbHelper;
import benwu.weatherapp.utils.DateUtils;
import static benwu.weatherapp.utils.LogUtils.LOGI;
import benwu.weatherapp.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SearchHistoryDbHelper mDbHelper;
    CursorAdapter mAdapter;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new SearchHistoryDbHelper(this);

        // Setting up list view
        String[] fromColumns = SearchHistoryContract.SearchHistoryColumns.PROJ;
        int[] toViews = {R.id.idList, R.id.locationList, R.id.countryList, R.id.dateList};

        ListView historyList = (ListView) findViewById(R.id.searchHistory);
        historyList.setOnItemClickListener(listClickListener);
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_search_history, null, fromColumns, toViews);

        historyList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // query should happen off main thread for best performance
        mCursor = mDbHelper.query();
        mAdapter.swapCursor(mCursor);

        LOGI(TAG, "Cursor: " + cursorToString(mCursor));
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

        mDbHelper.insert(location, country, DateUtils.getCurrentTime());

        startInfoActivity(location, country);
    }

    private AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String location = ((TextView) view.findViewById(R.id.locationList)).getText().toString();
            String country = ((TextView) view.findViewById(R.id.countryList)).getText().toString();

            startInfoActivity(location, country);
        }
    };

    private void startInfoActivity(String location, String country) {
        Intent intent = new Intent(this, WeatherInfoActivity.class);
        intent.putExtra(WeatherInfoActivity.KEY_LOCATION, location);
        intent.putExtra(WeatherInfoActivity.KEY_COUNTRY, country);

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
