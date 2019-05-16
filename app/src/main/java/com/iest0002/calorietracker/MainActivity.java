package com.iest0002.calorietracker;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.fragments.DietFragment;
import com.iest0002.calorietracker.fragments.HomeFragment;
import com.iest0002.calorietracker.fragments.MapFragment;
import com.iest0002.calorietracker.fragments.ReportFragment;
import com.iest0002.calorietracker.fragments.StepsFragment;
import com.iest0002.calorietracker.fragments.TrackerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase db;
    private TextView tvNavHeaderTitle;
    private TextView tvNavHeaderSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        View headerView =  navigationView.getHeaderView(0);

        tvNavHeaderTitle = headerView.findViewById(R.id.tv_nav_header_title);
        tvNavHeaderSubtitle = headerView.findViewById(R.id.tv_nav_header_subtitle);
        SetNavTitleAsyncTask setNavTitle = new SetNavTitleAsyncTask();
        setNavTitle.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;
        if (id == R.id.nav_home) {
            // Handle the camera action
            nextFragment = new HomeFragment();
        } else if (id == R.id.nav_diet) {
            nextFragment = new DietFragment();
        } else if (id == R.id.nav_report) {
            nextFragment = new ReportFragment();
        } else if (id == R.id.nav_map) {
            nextFragment = new MapFragment();
        } else if (id == R.id.nav_steps) {
            nextFragment = new StepsFragment();
        } else if (id == R.id.nav_tracker) {
            nextFragment = new TrackerFragment();
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DeleteUserAsyncTask deleteUser = new DeleteUserAsyncTask();
                            deleteUser.execute();
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return false;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public AppDatabase getDb() {
        return db;
    }

    private class DeleteUserAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            db.userDao().deleteAll();
            return null;
        }
    }

    private class SetNavTitleAsyncTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            String fname = db.userDao().getFirstName().get(0);
            String lname = db.userDao().getLastName().get(0);
            String fullName = String.format("%s %s", fname, lname);
            String email = db.userDao().getEmail().get(0);
            return new String[]{fullName, email};
        }

        @Override
        protected void onPostExecute(String... s) {
            tvNavHeaderTitle.setText(s[0]);
            tvNavHeaderSubtitle.setText(s[1]);
        }
    }
}
