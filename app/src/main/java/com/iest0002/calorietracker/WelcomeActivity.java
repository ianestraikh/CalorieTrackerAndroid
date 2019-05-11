package com.iest0002.calorietracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.iest0002.calorietracker.data.AppDatabase;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        CheckDatabaseAsyncTask checkDatabase = new CheckDatabaseAsyncTask();
        checkDatabase.execute();

    }

    public boolean validateInput(View v) {
        boolean isNotEmpty = true;
        for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
            View childView = ((ViewGroup) v).getChildAt(i);
            if (childView instanceof EditText && TextUtils.isEmpty(((EditText) childView).getText())) {
                ((EditText) childView).setError(getString(R.string.validation_empty));
                isNotEmpty = false;
            }
        }
        return isNotEmpty;
    }

    public AppDatabase getDb() {
        return db;
    }

    private class CheckDatabaseAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... s) {
            return db.userDao().count() == 0;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(WelcomeActivity.this, null, "Loading...", true);
        }

        @Override
        protected void onPostExecute(Boolean isDbEmpty) {
            progressDialog.cancel();
            if (isDbEmpty) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_welcome, new LoginFragment())
                        .commit();
            } else {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
