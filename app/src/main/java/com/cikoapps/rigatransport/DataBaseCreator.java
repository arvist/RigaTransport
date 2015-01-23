package com.cikoapps.rigatransport;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by arvis.taurenis on 1/23/2015.
 */
public class DataBaseCreator {
    final static String DbName = "transport";
   /* public static void CreateFromRawDbFiles(Context context)
    {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        // Creation of an empty database if needed, with SQL Helper :
        //CreateMinimum();
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // check for emptyness ( try a request on the database ) :

        if ( dataBaseHelper.checkDataBase())
            return;
        // if empty, overwrite the file from ressources :
        // Get file dir :
        String DBFileName = context.getDatabasePath(DbName).toString();
        ConstructNewFileFromressources(DBFileName, context);
    }*/


}
