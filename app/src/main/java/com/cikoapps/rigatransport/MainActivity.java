package com.cikoapps.rigatransport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase transportDB = null;
    String insertCategories = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
        setContentView(R.layout.activity_main);
        //createDB();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        dataBaseHelper.checkForData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android

   /* public void createDB() {
        try{
            DataBaseCreator.CreateFromRawDbFiles(getApplicationContext());
            *//*transportDB = this.openOrCreateDatabase("Transport", MODE_PRIVATE, null);
            //transportDB.execSQL("DROP TABLE categories;");
            transportDB.execSQL("CREATE TABLE IF NOT EXISTS categories " +
                    "(id integer primary key, title text);");
            File database = getApplicationContext().getDatabasePath("Transport");
            if(database.exists()){
                Cursor cursor = transportDB.rawQuery("SELECT * FROM categories", null);
                Log.w("TEST",cursor.getCount()+"   <-------------");
                if(cursor != null && (cursor.getCount() > 4)) {
                        //Toast.makeText(this,"Using db from memory", Toast.LENGTH_SHORT).show();
                }
                else {
                    new DownloadDatabase().execute();
                }
            }
            else {
                Toast.makeText(this, "Database failure", Toast.LENGTH_LONG).show();
            }
            Intent categoriesIntent = new Intent(this, CategoriesActivity.class);
            startActivity(categoriesIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }*//*
    } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w("GOT PAST","GOT PAST");
    }*/

    /*
        Private class to download categories of public transportation
     */
    private class DownloadDatabase  extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Cursor cursor = transportDB.rawQuery("SELECT * FROM categories", null);
            int idColumn = cursor.getColumnIndex("id");
            int titleColumn = cursor.getColumnIndex("title");
            cursor.moveToFirst();
            String allCategories = "";
            if(cursor != null && (cursor.getCount() > 0)){
                do{
                    String id = cursor.getString(idColumn);
                    String title = cursor.getString(titleColumn);
                    allCategories += id + " - " + title + "\n";
                } while(cursor.moveToNext());
                Toast.makeText(getApplicationContext(), allCategories, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"No Results To Show",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            DefaultHttpClient jsonDownloadClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost("http://cikoapps.com/test.txt");
            httpPost.setHeader("Content-type", "text/plain");
            InputStream inputStream = null;
            try{
                HttpResponse httpResponse = jsonDownloadClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line     = bufferedReader.readLine()) != null) {
                    transportDB.execSQL(line);
                    sb.append(line);
                }
                insertCategories = sb.toString();
               Log.w("DOWNLOAD FINISHED", insertCategories);
            }
            catch (ClientProtocolException e) {
                e.printStackTrace(); }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
