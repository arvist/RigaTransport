package com.cikoapps.rigatransport;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Creation date 1/19/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.cikoapps.rigatransport/databases/";

    private static String DB_NAME = "transport";

    private SQLiteDatabase myDataBase;

    private final Context myContext;


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
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } else try {
            createDataBase();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        close();
        return true;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            ConstructNewFileFromResources();
        }
    }

    void ConstructNewFileFromResources() {
        int ResourceList[] = new int[]{
                R.raw.transport1,
                R.raw.transport2,
                R.raw.transport3,
                R.raw.transport4,
                R.raw.transport5,
                R.raw.transport6,
                R.raw.transport7,
                R.raw.transport8

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
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    boolean checkDataBase() {
        String[] databaseList = myContext.databaseList();
        boolean dbExists = false;
        for (String database : databaseList) {
            Log.e("TRANSPORT ", database);
            if (database.equalsIgnoreCase("transport")) dbExists = true;

        }
        Log.e("DATABASE", dbExists + " ");
        return dbExists;
    }

    void openDataBase() throws SQLException {
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
        return myDataBase.rawQuery("Select * from routes where type = " + id + " and reverse = 'false';", null);

    }

    public Cursor getAllRouteStopsByRouteId(int route_id) {
        openDataBase();
        return myDataBase.rawQuery("select * from stop where stop.route_id = " + route_id + " order by stop._id", null);
    }

    public Cursor getAllRouteStopsPositionsByRouteId(int route_id) {
        /*
            route_id - id of Route from Routes table
            transport_type = 1 - Bus, 2 - Tram, 3 - Trolley
        */
        openDataBase();
        return myDataBase.rawQuery("Select stop.lat, stop.lng, stop.name, stop._id from stop where stop.route_id=" + route_id + " order by _id ASC;", null);
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
        close();
        return _id;
    }

    public int getRouteIntByTransportTypeAndNumRev(int transport_type, int num) {
        openDataBase();
        int _id = -1;
        Cursor cursor = myDataBase.rawQuery("Select routes._id from Routes where routes.type = " + transport_type
                + " and routes.number = " + num + " order by routes._id DESC limit 1;", null);
        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        close();
        return _id;
    }


    public int getStopIdByTransportTypeAndNum(int transport_type, int num) {
        openDataBase();
        int _id = -1;
        Cursor cursor = myDataBase.rawQuery("Select stop._id from stop where stop.type = " + transport_type
                + " and routes.number = " + num + " order by routes._id ASC limit 1;", null);
        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndex("_id"));
            } while (cursor.moveToNext());
        }
        close();
        return _id;
    }


    public Cursor getFavoriteRoutes(int[] ids) {
        openDataBase();
        StringBuilder sb = new StringBuilder();
        String query = "Select * from routes where ";
        sb.append(query);
        for (int id : ids) {
            sb.append("routes._id = ").append(id).append(" or ");
        }
        sb.trimToSize();
        String temp = (sb.substring(0, sb.length() - 3)) + ";";
        sb.append(";");
        return myDataBase.rawQuery(temp, null);

    }

    public Cursor getFavoriteStops(int[] ids) {
        openDataBase();
        StringBuilder sb = new StringBuilder();
        String query = "Select * from stop where ";
        sb.append(query);
        for (int id : ids) {
            sb.append("stop._id = ").append(id).append(" or ");
        }
        sb.trimToSize();
        String temp = (sb.substring(0, sb.length() - 3)) + ";";
        sb.append(";");
        return myDataBase.rawQuery(temp, null);
    }

    public Cursor getWeekDayTimesByStopId(int id) {
        openDataBase();
        String query = "select * from timesWeekDays where stop_id = " + id + ";";
        return myDataBase.rawQuery(query, null);
    }

    public Cursor getWeekEndTimesByStopId(int id) {
        openDataBase();
        String query = "select * from timesWeekEnds where stop_id = " + id + ";";
        return myDataBase.rawQuery(query, null);

    }


    public Cursor getRouteStops(int id) {
        openDataBase();
        String query = " select stop._id, stop.name from stop where route_id = " + id + ";";
        return myDataBase.rawQuery(query, null);
    }

    public Cursor getRouteStopsForward(int route_id, int stop_id) {
        openDataBase();
        String query = " select stop._id, stop.name from stop where route_id = " + route_id + "; "; //and _id > "+stop_id+";";
        return myDataBase.rawQuery(query, null);
    }

    public int getRouteIdByStopId(int stopId) {
        openDataBase();
        String query = "select * from stop where stop._id = " + stopId + "";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int routeId = -1;

        if (cursor.moveToFirst()) {
            do {
                routeId = cursor.getInt(cursor.getColumnIndex("route_id"));
            } while (cursor.moveToNext());
        }
        close();
        return routeId;
    }

    public Cursor getWeekEndStandartTimeByStopAndPosition(int routeId) {
        openDataBase();
        String query = "select  timesWeekEnds.time, timesWeekEnds.stop_id , stop.name as \"stop_name\"\n" +
                "from timesWeekEnds\n" +
                "join stop on  timesWeekEnds.stop_id = stop._id \n" +
                "join routes on  routes._id = stop.route_id \n" +
                "where routes._id = " + routeId + " and  timesWeekEnds.standartTime like \"true\"";

        return myDataBase.rawQuery(query, null);

    }

    public Cursor getWeekEndNonStandartTimeByStopAndPosition(int routeId) {
        openDataBase();
        String query = "select  timesWeekEnds.time, timesWeekEnds.stop_id , stop.name as \"stop_name\"\n" +
                "from timesWeekEnds\n" +
                "join stop on  timesWeekEnds.stop_id = stop._id \n" +
                "join routes on  routes._id = stop.route_id \n" +
                "where routes._id = " + routeId + " and  timesWeekEnds.standartTime like \"false\" ";
        return myDataBase.rawQuery(query, null);
    }

    public Cursor getWeekDayNonStandartTimeByStopAndPosition(int routeId) {
        openDataBase();
        String query = "select  timesWeekDays.time, timesWeekDays.stop_id , stop.name as \"stop_name\"\n" +
                "from timesWeekDays\n" +
                "join stop on  timesWeekDays.stop_id = stop._id \n" +
                "join routes on  routes._id = stop.route_id \n" +
                "where routes._id = " + routeId + " and  timesWeekDays.standartTime like \"false\" ";
        return myDataBase.rawQuery(query, null);
    }

    public Cursor getWeekDayStandartTimeByStopAndPosition(int routeId) {
        openDataBase();
        String query = "select  timesWeekDays.time, timesWeekDays.stop_id , stop.name as \"stop_name\"\n" +
                "from timesWeekDays\n" +
                "join stop on  timesWeekDays.stop_id = stop._id \n" +
                "join routes on  routes._id = stop.route_id \n" +
                "where routes._id = " + routeId + " and  timesWeekDays.standartTime like \"true\" ";
        return myDataBase.rawQuery(query, null);
    }

    public int getStandartTimeNumWeekDay(int timeId, int stopId) {

        openDataBase();
        String query = "select * from timesWeekDays where stop_id = " + stopId + " and standartTime like \"true\" and _id < " + timeId + "";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int count = cursor.getCount();
        close();
        return count;

    }

    public int getStandartTimeNumWeekend(int timeId, int stopId) {
        openDataBase();
        String query = "select * from timesWeekEnds where stop_id = " + stopId + " and standartTime like \"true\" and _id < " + timeId + "";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int count = cursor.getCount();
        close();
        return count;
    }

    public int getNonStandartTimeNumWeekDay(int timeId, int stopId) {
        openDataBase();
        String query = "select * from timesWeekDays where stop_id = " + stopId + " and standartTime like \"false\" and _id < " + timeId + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int count = cursor.getCount();
        close();
        return count;
    }

    public int getNonStandartTimeNumWeekEnd(int timeId, int stopId) {
        openDataBase();
        String query = "select * from timesWeekEnds where stop_id = " + stopId + " and standartTime like \"false\" and _id < " + timeId + "";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int count = cursor.getCount();
        close();
        return count;
    }

    public int[] getRouteIntAndTypeByRouteId(int rotueId) {
        openDataBase();
        String query = "select * from routes where _id = " + rotueId + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);
        int type = -1;
        int number = -1;
        if (cursor.moveToFirst()) {
            do {
                type = cursor.getInt(cursor.getColumnIndex("type"));
                number = cursor.getInt(cursor.getColumnIndex("number"));
            } while (cursor.moveToNext());
        }
        close();
        int[] rotueInfo = {type, number};
        return rotueInfo;
    }


}
