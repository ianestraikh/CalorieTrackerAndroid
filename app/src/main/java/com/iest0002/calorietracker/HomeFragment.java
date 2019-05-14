package com.iest0002.calorietracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iest0002.calorietracker.data.AppDatabase;

import java.util.Random;

public class HomeFragment extends Fragment {
    View vHome;
    TextView tvHelloUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);

        tvHelloUser = vHome.findViewById(R.id.tv_hello_user);

        GetNameAsyncTask getName = new GetNameAsyncTask();
        getName.execute();

        return vHome;
    }

    private class GetNameAsyncTask extends AsyncTask <Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return ((MainActivity) getActivity()).getDb().userDao().getFirstName().get(0);
        }

        @Override
        protected void onPostExecute(String s) {
            // ref: https://stackoverflow.com/questions/46129389/how-to-concat-two-strings-in-settext-in-android
            tvHelloUser.setText(getResources().getString(R.string.hello_user, s));
        }


    }

}
