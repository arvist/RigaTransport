package com.cikoapps.rigatransport;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creation date 1/29/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formating
 */
class UserDataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.cikoapps.rigatransport/databases/";

    private static String DB_NAME = "user_data";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /*
        Constructor
     */
    public UserDataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /*
        checks for data, if not present then tables favorite_routes and favorite stops
        are created
     */
    public boolean checkForData() {
        boolean isDataPresent = checkDataBase();
        if (isDataPresent) {
            return true;
        } else {
            createDataBase();
        }
        return true;
    }

    /*
        Creates database with two tables favorite_routes and favorite_stops where to store
        data about users favorite routes and stops
     */
    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            //noinspection AccessStaticViaInstance
            myDataBase = myContext.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

            String createFavoriteRoutes = "CREATE TABLE `favorite_routes` (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`route_id`\tINTEGER,\n" +
                    "\tPRIMARY KEY(_id)\n" +
                    ");";
            myDataBase.execSQL(createFavoriteRoutes);
            myDataBase.execSQL("CREATE TABLE `favorite_stops` (\n" +
                    "\t`_id`\tINTEGER,\n" +
                    "\t`stop_id`\tINTEGER,\n" +
                    "\tPRIMARY KEY(_id)\n" +
                    ");");
        }
        close();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /*
        Checks whether database where to store user favorite data is present
     */
    boolean checkDataBase() {
        String[] databaseList = myContext.databaseList();
        boolean dbExists = false;
        for (String database : databaseList) {
            if (database.equalsIgnoreCase(DB_NAME)) dbExists = true;
        }
        return dbExists;
    }

    /*
        Open database, it has to be closed manually
     */
    void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = myContext.openOrCreateDatabase(myPath, SQLiteDatabase.OPEN_READONLY, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /*
        Adds route to favorites using route id
     */
    public void addFavoriteRoute(int routeId) {
        openDataBase();
        myDataBase.execSQL("insert into favorite_routes(route_id) values (" + routeId + ");");
        close();
    }

    /*
        Adds stop to favorites using stop id
     */
    public void addFavoriteStop(int stopId) {
        openDataBase();
        myDataBase.execSQL("insert into favorite_stops (stop_id)  values (" + stopId + ");");
        close();
    }

    /*
        Tests whether route is favorited by user or not. Returns true|false
     */
    public boolean isFavoriteRoute(int routeId) {
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_routes where route_id = " + routeId + ";", null);
        boolean isFavorite = cursor.moveToFirst();
        close();
        return isFavorite;
    }

    public boolean isFavoriteStop(int stopId) {
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_stops where stop_id = " + stopId + ";", null);
        boolean isFavorite = cursor.moveToFirst();
        close();
        return isFavorite;
    }


    /*
        Removes route from favorites using route id
     */
    public void removeFavoriteRoute(int route_id) {
        openDataBase();
        myDataBase.execSQL("Delete from favorite_routes where route_id = " + route_id + ";");
        close();
    }

    public void removeFavoriteStop(int stop_id) {
        openDataBase();
        myDataBase.execSQL("Delete from favorite_stops where stop_id = " + stop_id + ";");
        close();
    }

    /*
        Returns array of int variables that represent route id's which user has favorited
     */
    public int[] getRouteFavoriteIds() {
        int[] returnArray;
        int position = 0;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_routes", null);
        returnArray = new int[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("route_id"));
                returnArray[position] = _id;
                position++;
            } while (cursor.moveToNext());
        }
        close();
        return returnArray;
    }

    public int[] getStopFavoriteIds() {
        int[] returnArray;
        int position = 0;
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from favorite_stops", null);
        returnArray = new int[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex("stop_id"));
                returnArray[position] = _id;
                position++;
            } while (cursor.moveToNext());
        }
        close();
        return returnArray;
    }
}
