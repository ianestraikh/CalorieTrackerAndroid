package com.iest0002.calorietracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.Food;
import com.iest0002.calorietracker.data.RestClient;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    View vReport;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        pieChart = vReport.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
        int userId = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), userIdDefault);

        GetReportByDateAsyncTask getReportByDate = new GetReportByDateAsyncTask();
        getReportByDate.execute("1", "2019-03-24");

        return vReport;
    }

    // ref: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/PieChartActivity.java
    private void setData(float... floats) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < floats.length ; i++) {
            entries.add(new PieEntry(floats[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        /*
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        */
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private class GetReportByDateAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.myDbGet(RestClient.GET_REPORT_BY_DATE, strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                float calConsumed = jsonObject.get("caloriesConsumed").getAsFloat();
                float calBurned = jsonObject.get("caloriesBurned").getAsFloat();
                float calRemaining = jsonObject.get("remainingCalories").getAsFloat();

                setData(calConsumed, calBurned, calRemaining);
            } else {
                Toast.makeText(getContext(), getString(R.string.msg_check_connection), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
