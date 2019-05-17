package com.iest0002.calorietracker;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.fragments.LoginFragment;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
        int userId = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), userIdDefault);
        if (userId != userIdDefault) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_welcome, new LoginFragment())
                    .commit();
        }

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
}
