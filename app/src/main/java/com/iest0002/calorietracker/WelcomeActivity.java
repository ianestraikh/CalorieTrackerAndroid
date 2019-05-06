package com.iest0002.calorietracker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_welcome, new LoginFragment())
                .commit();
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
