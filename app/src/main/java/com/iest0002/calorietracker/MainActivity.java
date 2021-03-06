package com.iest0002.calorietracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.fragments.DietFragment;
import com.iest0002.calorietracker.fragments.HomeFragment;
import com.iest0002.calorietracker.fragments.MapFragment;
import com.iest0002.calorietracker.fragments.ReportFragment;
import com.iest0002.calorietracker.fragments.StepsFragment;
import com.iest0002.calorietracker.fragments.TrackerFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlarmManager alarmMgr;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;

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

        View headerView = navigationView.getHeaderView(0);

        TextView tvNavHeaderTitle = headerView.findViewById(R.id.tv_nav_header_title);
        TextView tvNavHeaderSubtitle = headerView.findViewById(R.id.tv_nav_header_subtitle);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        String fname = sharedPref.getString(getResources().getString(R.string.saved_user_fname_key), "%fname%");
        String lname = sharedPref.getString(getResources().getString(R.string.saved_user_lname_key), "%lname");
        String email = sharedPref.getString(getResources().getString(R.string.saved_email_key), "%email%");

        tvNavHeaderTitle.setText(String.format("%s %s", fname, lname));
        tvNavHeaderSubtitle.setText(email);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();

        // Set the alarm to start at approximately 11:55 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 30);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmIntent = new Intent(this, ScheduledIntentService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
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
                            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                                    getString(R.string.preference_file_key),
                                    Context.MODE_PRIVATE
                            );
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.clear();
                            editor.apply();

                            ClearDbAsyncTask clearDb = new ClearDbAsyncTask();
                            clearDb.execute();

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

    private class ClearDbAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
            db.clearAllTables();
            return null;
        }
    }
}
