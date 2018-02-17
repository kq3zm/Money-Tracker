package edu.virginia.cs4720.final_project_final_sudowoodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView = null;
    Toolbar toolbar = null;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set fragment initially
        OverviewFragment overviewFragment = new OverviewFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, overviewFragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Log.d(TAG, "dbhelper");
        //Get the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "db");
        String selectQuery = "SELECT * FROM " + ListFragment.DataTable.TABLE_NAME;
        Log.d(TAG, "select");
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, "cursor");


        final StringBuilder emailText = new StringBuilder();
        //Iterate through the database (to check the database)
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emailText.append(cursor.getString(0) + " (" + cursor.getString(1) + "): $"+ cursor.getString(2)+ " on " +cursor.getString(4)+"\n");
            cursor.moveToNext();
        }
        email = emailText.toString();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        // find MenuItem you want to change
        MenuItem nav_camara = menu.findItem(R.id.nav_camera);
        // set new title to the MenuItem
        nav_camara.setTitle("Overview");

        MenuItem nav_gallery = menu.findItem(R.id.nav_gallery);
        nav_gallery.setTitle("List");

        MenuItem nav_slideshow = menu.findItem(R.id.nav_slideshow);
        nav_slideshow.setVisible(false);

        MenuItem nav_manage = menu.findItem(R.id.nav_manage);
        nav_manage.setVisible(false);

        MenuItem nav_share = menu.findItem(R.id.nav_share);
        nav_share.setVisible(false);

        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = this.getIntent();
        if (intent != null) {
            try {
                String srcData = intent.getExtras().getString("Source");
                if ((srcData.equals("AddItemActivity Success")) || (srcData.equals("EditItemActivity Success"))) {
                    ListFragment listFragment = new ListFragment();
                    fragmentTransaction.replace(R.id.fragment_container, listFragment);
                    fragmentTransaction.commit();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,AddItem.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //Intent intent = new Intent(MainActivity.this,ListActivity.class);
            //startActivity(intent);
            OverviewFragment overviewFragment = new OverviewFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, overviewFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_gallery) {
            ListFragment listFragment = new ListFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, listFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Money Tracker Summary");
            intent.putExtra(Intent.EXTRA_TEXT, email);
            intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Necessary for accessing SQLite table in this activity.
    public static class DataTable implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
    }

}
