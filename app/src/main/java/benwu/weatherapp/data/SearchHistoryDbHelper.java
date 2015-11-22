package benwu.weatherapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static benwu.weatherapp.data.SearchHistoryContract.SearchHistoryColumns;

/**
 * Created by Ben Wu on 11/21/2015.
 */
public class SearchHistoryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SearchHistory.db";

    private static int sCount = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchHistoryColumns.TABLE_NAME + " (" +
                    SearchHistoryColumns._ID + " INTEGER PRIMARY KEY," +
                    SearchHistoryColumns.COLUMN_CITY + " TEXT," +
                    SearchHistoryColumns.COLUMN_COUNTRY + " TEXT," +
                    SearchHistoryColumns.COLUMN_DATE + " TEXT" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchHistoryColumns.TABLE_NAME;

    public SearchHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insert(String city, String country, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SearchHistoryColumns._ID, sCount++);
        values.put(SearchHistoryColumns.COLUMN_CITY, city);
        values.put(SearchHistoryColumns.COLUMN_COUNTRY, country);
        values.put(SearchHistoryColumns.COLUMN_DATE, date);

        return db.insert(SearchHistoryColumns.TABLE_NAME, "null", values);
    }

    public int clearAll() {
        SQLiteDatabase db = getReadableDatabase();
        sCount = 1;
        return db.delete(SearchHistoryColumns.TABLE_NAME, null, null);
    }

    // returns all rows in the db
    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = SearchHistoryColumns.PROJ;

        Cursor data = db.query(
                SearchHistoryColumns.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return data;
    }
}
