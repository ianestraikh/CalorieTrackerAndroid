package com.iest0002.calorietracker.fragments;

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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.data.RestClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerFragment extends Fragment {

    private View vTracker;

    private TextView tvGoal, tvSteps, tvCalConsumed, tvCalBurned;

    private AppDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vTracker = inflater.inflate(R.layout.fragment_tracker, container, false);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        tvGoal = vTracker.findViewById(R.id.tv_tracker_goal);
        tvSteps = vTracker.findViewById(R.id.tv_tracker_steps);
        tvCalConsumed = vTracker.findViewById(R.id.tv_tracker_consumed);
        tvCalBurned = vTracker.findViewById(R.id.tv_tracker_burned);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int calorieGoalDefault = getResources().getInteger(R.integer.saved_default_cal_goal);
        int calorieGoalKey = sharedPref.getInt(getResources().getString(R.string.saved_cal_goal_key), calorieGoalDefault);
        int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
        int userId = sharedPref.getInt(getString(R.string.saved_user_id_key), userIdDefault);

        tvGoal.setText(String.valueOf(calorieGoalKey));

        GetStepsAsyncTask getSteps = new GetStepsAsyncTask();
        getSteps.execute();

        GetCalConsumedAsyncTask getCalConsumed = new GetCalConsumedAsyncTask();
        getCalConsumed.execute(userId);

        GetCalBurnedAsyncTask getCalBurned = new GetCalBurnedAsyncTask();
        getCalBurned.execute(userId);

        return vTracker;
    }


    private class GetStepsAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
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
            return RestClient.myDbGet(RestClient.CALC_CAL_CONSUMED, Integer.toString(ints[0]), date);
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                String calConsumed = jsonObject.get("caloriesConsumed").getAsString();
                tvCalConsumed.setText(calConsumed);
            } else {
                Toast.makeText(getContext(), R.string.msg_check_connection, Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private class GetCalBurnedAsyncTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... ints) {
            int stepsAmount = db.stepsDao().sumStepsAmount();
            return RestClient.myDbGet(RestClient.CALC_CAL_BURNED, Integer.toString(ints[0]),
                    Integer.toString(stepsAmount));
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                String calBurned = jsonObject.get("caloriesBurned").getAsString();
                tvCalBurned.setText(calBurned);
            } else {
                Toast.makeText(getContext(), getString(R.string.msg_check_connection), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
