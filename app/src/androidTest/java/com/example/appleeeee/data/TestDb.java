package com.example.appleeeee.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testLocationTable() {
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createNorthPoleLocationValues();
        contentValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testCityName);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);

        db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, contentValues);

        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME, null, null, null, null, null, null
        );
        assertTrue("No records", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error", cursor, contentValues);
        assertFalse("Error, more than one record returned", cursor.moveToNext());

        cursor.close();
        db.close();
    }

    public void testWeatherTable() {
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = insertLocation();

        ContentValues values = TestUtilities.createWeatherValues(rowId);
        db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME, null, null, null, null, null, null
        );
        assertTrue("No records", cursor.moveToFirst());
        TestUtilities.validateCursor("Invalidate cursor", cursor, values);
        cursor.close();
        db.close();
    }


    public long insertLocation() {
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = TestUtilities.createNorthPoleLocationValues();
        contentValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, 99705);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, "North Pole");
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, 64.7488);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, -147.353);

        return db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, contentValues);
    }
}
