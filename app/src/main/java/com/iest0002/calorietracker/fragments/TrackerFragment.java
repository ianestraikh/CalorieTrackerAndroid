package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.data.RestClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerFragment extends Fragment {

    View vTracker;

    TextView tvGoal, tvSteps, tvCalConsumed, tvCalBurned;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vTracker = inflater.inflate(R.layout.fragment_tracker, container, false);

        tvGoal = vTracker.findViewById(R.id.tv_tracker_goal);
        tvSteps = vTracker.findViewById(R.id.tv_tracker_steps);
        tvCalConsumed = vTracker.findViewById(R.id.tv_tracker_consumed);
        tvCalBurned = vTracker.findViewById(R.id.tv_tracker_burned);

        SharedPreferences sharedPrefCalGoal = getActivity().getPreferences(Context.MODE_PRIVATE);
        int calorieGoalDefault = getResources().getInteger(R.integer.saved_calorie_goal_default_value);
        int calorieGoalKey = sharedPrefCalGoal.getInt(getResources().getString(R.string.saved_calorie_goal_key), calorieGoalDefault);
        tvGoal.setText(String.valueOf(calorieGoalKey));

        GetStepsAsyncTask getSteps = new GetStepsAsyncTask();
        getSteps.execute();

        GetCalConsumedAsyncTask getCalConsumed = new GetCalConsumedAsyncTask();
        getCalConsumed.execute(1);

        return vTracker;
    }


    private class GetStepsAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "AppDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
            return db.stepsDao().sumStepsAmount();
        }

        @Override
        protected void onPostExecute(Integer steps) {
            tvSteps.setText(Integer.toString(steps));
        }
    }

    private class GetCalConsumedAsyncTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... ints) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(new Date());
            date = "2019-03-16";
            return RestClient.myDbGet(RestClient.CALC_CAL_CONSUMED, Integer.toString(ints[0]), date);
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                String calConsumed = jsonObject.get("caloriesConsumed").getAsString();
                tvCalConsumed.setText(calConsumed);
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Something went wrong")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }

        }
    }
}
