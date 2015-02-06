package com.cikoapps.rigatransport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialog progress = new ProgressDialog(this);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        UserDataBaseHelper userDataBaseHelper = new UserDataBaseHelper(getApplicationContext());
        if (dataBaseHelper.checkForData() && userDataBaseHelper.checkForData()) {

            Intent categoriesIntent = new Intent(this, CategoriesActivity.class);
            startActivity(categoriesIntent);
            progress.cancel();
        }
    }
}
