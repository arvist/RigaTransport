package com.cikoapps.rigatransport;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.cikoapps.rigatransport/databases/";

    private static String DB_NAME = "transport";

    private static String LOG_DB = "DATABASE.TRANSPORT";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public boolean checkForData() {
        boolean isDataPresent = checkDataBase();
        if (isDataPresent) {
            openDataBase();
            Cursor cursor = myDataBase.rawQuery("SELECT * FROM routes", null);

            if (cursor.getCount() < 100) {
                try {
                    createDataBase();
                    Log.d(LOG_DB, "Data missing from database");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            Log.d(LOG_DB, "Database valid");
        } else {
            try {
                Log.d(LOG_DB, "Database do not exist");
                createDataBase();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        //close();
        return true;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        Log.w("DATABASE", dbExist + " ");
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            ConstructNewFileFromResources();
        }
    }

    public void ConstructNewFileFromResources() {
        Log.w(LOG_DB, "Reading database");
        int ResourceList[] = new int[]{
                R.raw.transport1,
                R.raw.transport2,
                R.raw.transport3,
                R.raw.transport4,
                R.raw.transport5,
                R.raw.transport6,
                R.raw.transport7,
                R.raw.transport8,
                R.raw.transport9,
                R.raw.transport10,
                R.raw.transport11
        };
        try {
            FileOutputStream Fos = new FileOutputStream(DB_PATH + DB_NAME);
            for (int FileId : ResourceList) {
                InputStream inputFile = myContext.getResources().openRawResource(FileId);
                int TotalLength = 0;
                try {
                    TotalLength = inputFile.available();
                } catch (IOException e) {
                    Toast.makeText(myContext, "Error Reading File", Toast.LENGTH_SHORT).show();
                }
                // Reading and writing the file Method 1 :
                byte[] buffer = new byte[TotalLength];
                int len = 0;
                try {
                    len = inputFile.read(buffer);
                } catch (IOException e) {
                    Toast.makeText(myContext, "Error Reading File", Toast.LENGTH_SHORT).show();
                }
                Fos.write(buffer);
                inputFile.close();
            }
            Fos.close();
        } catch (IOException e) {
            Toast.makeText(myContext, "IO Error Reading/writing File", Toast.LENGTH_SHORT).show();
        }
        Log.d(LOG_DB, "Finished reading database");
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    protected boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        String[] databaseList = myContext.databaseList();
        boolean dbExists = false;
        for (String database : databaseList) {
            if (database.equalsIgnoreCase("transport")) dbExists = true;
        }
        if (dbExists) return true;
        else return false;
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getTransportListQuery(int id) {
        /*
            id = 1 Bus
            id = 2 Tram
            id = 3 Trolley
         */
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select * from routes where type = " + id + " and reverse = 'false';", null);
        //close();
        return cursor;

    }


    public Cursor getAllRouteStopsPositionsByRouteId(int route_id) {
        /*
            route_id - id of Route from Routes table
            transport_type = 1 - Bus, 2 - Tram, 3 - Trolley
        */
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("Select stop.lat, stop.lng, stop.name, stop._id from stop where stop.routeId=" + route_id + " order by _id ASC;", null);
        //close();
        return cursor;
    }

    public int getRouteIntByTransportTypeAndNum(int transport_type, int num) {
        openDataBase();
        int _id = -1;
        Cursor cursor = myDataBase.rawQuery("Select routes._id from Routes where routes.type = " + transport_type
                + " and routes.number = " + num + " order by routes._id ASC limit 1;", null);
        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        return _id;
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}
