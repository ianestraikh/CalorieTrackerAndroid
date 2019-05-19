package com.iest0002.calorietracker;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.data.Food;
import com.iest0002.calorietracker.data.Report;
import com.iest0002.calorietracker.data.RestClient;
import com.iest0002.calorietracker.data.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduledIntentService extends IntentService {
    static int counter = 0;

    public ScheduledIntentService() {
        super("ScheduledIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
        int userId = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), userIdDefault);

        int goalDefault = getResources().getInteger(R.integer.saved_default_cal_goal);
        int goal = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), goalDefault);

        Log.i(ScheduledIntentService.class.getName(), String.format("userId: %d, goal: %d", userId, goal));

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        int steps = db.stepsDao().sumStepsAmount();

        Log.i(ScheduledIntentService.class.getName(), String.format("steps: %d", steps));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        String calConsumedResp = RestClient.myDbGet(RestClient.CALC_CAL_CONSUMED, Integer.toString(userId), date);
        double calConsumed = .0;
        if (!TextUtils.isEmpty(calConsumedResp)) {
            JsonObject jsonObject = new JsonParser().parse(calConsumedResp).getAsJsonObject();
            calConsumed = jsonObject.get("caloriesConsumed").getAsDouble();
        }

        String calBurnedResp = RestClient.myDbGet(RestClient.CALC_CAL_BURNED, Integer.toString(userId),
                Integer.toString(steps));

        double calBurned = .0;
        if (!TextUtils.isEmpty(calBurnedResp)) {
            JsonObject jsonObject = new JsonParser().parse(calBurnedResp).getAsJsonObject();
            calBurned = jsonObject.get("caloriesBurned").getAsDouble();
        }

        String userResponse = RestClient.myDbGet(RestClient.USER_METHOD_PATH, Integer.toString(userId));

        User user = null;
        if (userResponse != null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            user = gson.fromJson(userResponse, User.class);
        }

        Report report = new Report(new Date(), calConsumed, calBurned, steps, goal, user);
        RestClient.post(RestClient.REPORT_METHOD_PATH, report);

        db.stepsDao().deleteAll();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getResources().getString(R.string.saved_user_id_key));
        editor.apply();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}