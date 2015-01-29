package com.cikoapps.rigatransport;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by arvis.taurenis on 1/29/2015.
 */
public class UserDataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.cikoapps.rigatransport/databases/";

    private static String DB_NAME = "user_data";

    private static String LOG_DB = "DATABASE.USER_DATA";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public UserDataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public boolean checkForData() {
        boolean isDataPresent = checkDataBase();
        if (isDataPresent) {
            return true;
        } else {

            Log.d(LOG_DB, "Database do not exist");
            createDataBase();
        }
        //close();
        return true;
    }

    private void createDataBase() {

        boolean dbExist = checkDataBase();
        Log.w("DATABASE", dbExist + " ");

        //By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
        if (!dbExist) {
            this.getReadableDatabase();
            myDataBase = myContext.openOrCreateDatabase(DB_NAME, myContext.MODE_PRIVATE, null);

            String createFavoriteRoutes = "CREATE TABLE `favorite_routes` (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`route_id`\tINTEGER,\n" +
                    "\tPRIMARY KEY(_id)\n" +
                    ");";
            myDataBase.execSQL(createFavoriteRoutes);
            Log.w(LOG_DB, createFavoriteRoutes);
            myDataBase.execSQL("CREATE TABLE `favorite_stops` (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`stop_id`\tINTEGER,\n" +
                    "\tPRIMARY KEY(_id)\n" +
                    ");");
        }

    }

    protected boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        String[] databaseList = myContext.databaseList();
        boolean dbExists = false;
        for (String database : databaseList) {
            if (database.equalsIgnoreCase(DB_NAME)) dbExists = true;
        }
        if (dbExists) return true;
        else return false;
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = myContext.openOrCreateDatabase(myPath, SQLiteDatabase.OPEN_READONLY, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFavoriteRoute(int routeId) {
        openDataBase();
        Log.w("INSERTED ROUTE", routeId + " ");
        myDataBase.execSQL("insert into favorite_routes(route_id) values (" + routeId + ");");
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_routes;", null);
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("route_id"));
                Log.w(LOG_DB, " _ID ->" + _id);
            } while (cursor.moveToNext());
        }
    }

    public void addFavoriteStop(int stopId) {
        openDataBase();
        myDataBase.execSQL("insert into favorite_stops (stop_id)  values (" + stopId + ");");

        Cursor cursor = myDataBase.rawQuery("Select * from favorite_stops;", null);
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("route_Id"));
                Log.w(LOG_DB, "ALL_ID ->" + _id);
            } while (cursor.moveToNext());
        }

    }

    public boolean isFavoriteRoute(int routeId) {
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_routes where route_id = " + routeId + ";", null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void removeFavoriteRoute(int route_id) {
        openDataBase();
        myDataBase.execSQL("Delete from favorite_routes where route_id = " + route_id + ";");

    }
    public int[] getRouteFavoriteIds(){
        openDataBase();
        int[] returnArray = null;
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_routes", null);
        returnArray = new int[cursor.getCount()];
        int position = 0;
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("route_id"));
                Log.w(LOG_DB, "ALL_ID ->" + _id);
                returnArray[position] = _id;
                position++;
            } while (cursor.moveToNext());
        }
        return returnArray;
    }
}
