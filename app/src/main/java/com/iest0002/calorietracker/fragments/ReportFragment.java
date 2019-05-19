package com.iest0002.calorietracker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.RestClient;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    View vReport;
    PieChart pieChart;
    BarChart barChart;
    EditText etPieDate, etBarDate1, etBarDate2;
    Button btnPieChart, btnBarChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        getActivity().setTitle(R.string.fr_title_report);

        etPieDate = vReport.findViewById(R.id.et_pie_date);
        etBarDate1 = vReport.findViewById(R.id.et_bar_date1);
        etBarDate2 = vReport.findViewById(R.id.et_bar_date2);

        btnPieChart = vReport.findViewById(R.id.btn_pie_chart);
        btnBarChart = vReport.findViewById(R.id.btn_bar_chart);

        pieChart = vReport.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        barChart = vReport.findViewById(R.id.bar_chart);
        barChart.getDescription().setEnabled(false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
        final int userId = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), userIdDefault);

        etPieDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", v.getId());
                DialogFragment newFragment = new ReportFragment.DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        etBarDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", v.getId());
                DialogFragment newFragment = new ReportFragment.DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        etBarDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", v.getId());
                DialogFragment newFragment = new ReportFragment.DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPieDate.getText())) {
                    etPieDate.setError(getString(R.string.validation_empty));
                    return;
                }
                GetReportByDateAsyncTask getReportByDate = new GetReportByDateAsyncTask();
                getReportByDate.execute(Integer.toString(userId), etPieDate.getText().toString());
            }
        });

        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etBarDate1.getText())) {
                    etBarDate1.setError(getString(R.string.validation_empty));
                }
                if (TextUtils.isEmpty(etBarDate2.getText())) {
                    etBarDate2.setError(getString(R.string.validation_empty));
                }
                GetReportPerDayAsyncTask getReportPerDay = new GetReportPerDayAsyncTask();
                getReportPerDay.execute(Integer.toString(userId), etBarDate1.getText().toString(),
                        etBarDate2.getText().toString());
            }
        });

        return vReport;
    }

    // ref: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/PieChartActivity.java
    private void setPieChartData(float... floats) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(floats[0], "Calories Consumed"));
        entries.add(new PieEntry(floats[1], "Calories Burned"));
        entries.add(new PieEntry(floats[2], "Remaining Calories"));

        PieDataSet dataSet = new PieDataSet(entries, null);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);

        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private void setBarChartData(List<Float> dates, List<Float> calConsumed, List<Float> calBurned) {
        float dayInMillisec = 1000f * 60f * 60f * 24f;

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(dayInMillisec);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM");
                    return dateFormat.format(new Date(((Float) value).longValue()));
                } catch (Exception e) {
                    return ((Float) value).toString();
                }
            }
        });

        ArrayList<BarEntry> eCalConsumed = new ArrayList<>();
        ArrayList<BarEntry> eCalBurned = new ArrayList<>();
        for (int i = 0; i < calConsumed.size(); i++) {
            eCalConsumed.add(new BarEntry(dates.get(i), calConsumed.get(i)));
            eCalBurned.add(new BarEntry(dates.get(i), calBurned.get(i)));
        }

        BarDataSet set1 = new BarDataSet(eCalConsumed, "Calories Consumed");
        set1.setColor(Color.BLUE);
        set1.setHighLightAlpha(0);
        BarDataSet set2 = new BarDataSet(eCalBurned, "Calories Burned");
        set2.setColor(Color.RED);
        set2.setHighLightAlpha(0);

        BarData data = new BarData(set1, set2);
        barChart.setData(data);
        barChart.getBarData().setBarWidth(dayInMillisec * 0.8f);
        barChart.getXAxis().setAxisMinimum(dates.get(0) - dayInMillisec);
        barChart.getXAxis().setAxisMaximum(dates.get(dates.size() - 1) + dayInMillisec);
        barChart.invalidate();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        int editTextId;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            editTextId = getArguments().getInt("id");

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText et = getActivity().findViewById(editTextId);
            et.setText(String.format(Locale.ENGLISH, "%d-%d-%d", year, month + 1, day));
            et.setError(null);
        }
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

                setPieChartData(calConsumed, calBurned, calRemaining);
            }
        }
    }

    private class GetReportPerDayAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.myDbGet(RestClient.GET_REPORT_PER_DAY, strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                List<Float> dates = new ArrayList<>();
                List<Float> calConsumed = new ArrayList<>();
                List<Float> calBurned = new ArrayList<>();

                JsonArray jsonArray = new JsonParser().parse(s).getAsJsonArray();
                if (jsonArray.size() == 0) {
                    return;
                }
                for (JsonElement json : jsonArray) {
                    dates.add(json.getAsJsonObject().get("reportDate").getAsFloat());
                    calConsumed.add(json.getAsJsonObject().get("caloriesConsumed").getAsFloat());
                    calBurned.add(json.getAsJsonObject().get("caloriesBurned").getAsFloat());
                }

                setBarChartData(dates, calConsumed, calBurned);
            }
        }
    }
}
