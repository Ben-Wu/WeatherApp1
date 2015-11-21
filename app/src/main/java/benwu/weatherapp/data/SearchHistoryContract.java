package benwu.weatherapp.data;

import android.provider.BaseColumns;

/**
 * Created by Ben Wu on 11/21/2015.
 */
public class SearchHistoryContract {

    public SearchHistoryContract() {}

    public static class SearchHistoryColumns implements BaseColumns {
        public static final String TABLE_NAME = "SearchHistory";
        public static final String COLUMN_CITY = "CityColumn";
        public static final String COLUMN_COUNTRY = "CountryColumn";
        public static final String COLUMN_DATE = "DateColumn";

        public static final String[] PROJ = {_ID, COLUMN_CITY, COLUMN_COUNTRY, COLUMN_DATE};

        public static final String FOR_COUNTRY = COLUMN_COUNTRY + " =?";
        public static final String FOR_CITY = COLUMN_CITY + " =?";
        public static final String FOR_CITY_AND_COUNTRY = COLUMN_CITY + " =? AND " + COLUMN_COUNTRY + " =?";
    }

}
