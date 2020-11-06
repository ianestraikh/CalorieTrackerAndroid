package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.ScheduledIntentService;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPref;
    private View vHome;
    private TextView tvHelloUser, tvCalGoal;
    private Button btnCalGoal, btnSendReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle(R.string.ft_title_home);

        tvHelloUser = vHome.findViewById(R.id.tv_hello_user);
        tvCalGoal = vHome.findViewById(R.id.tv_goal);
        btnCalGoal = vHome.findViewById(R.id.btn_add_cal_goal);
        btnCalGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditGoalDialog();
            }
        });

        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        final int calorieGoalDefault = getResources().getInteger(R.integer.saved_default_cal_goal);
        int calorieGoalKey = sharedPref.getInt(getResources().getString(R.string.saved_cal_goal_key), calorieGoalDefault);
        String fname = sharedPref.getString(getResources().getString(R.string.saved_user_fname_key), "%fname");
        tvCalGoal.setText(String.valueOf(calorieGoalKey));
        tvHelloUser.setText(getResources().getString(R.string.hello_user, fname));

        btnSendReport = vHome.findViewById(R.id.btn_send_report);
        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCalGoal.setText(Integer.toString(calorieGoalDefault));
                Intent intent = new Intent(getActivity(), ScheduledIntentService.class);
                getActivity().startService(intent);
            }
        });

        return vHome;
    }

    // ref: https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void showEditGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Daily Goal");
        builder.setCancelable(false);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("kcal");
        input.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(5)
        });
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputNumber = input.getText().toString();
                if (TextUtils.isEmpty(inputNumber)) {
                    return;
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_cal_goal_key), Integer.parseInt(inputNumber));
                editor.apply();
                tvCalGoal.setText(inputNumber);
                editor.apply();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
