package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iest0002.calorietracker.MainActivity;
import com.iest0002.calorietracker.R;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPrefCalGoal;
    private View vHome;
    private TextView tvHelloUser, tvCalGoal;
    private Button btnCalGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);

        tvHelloUser = vHome.findViewById(R.id.tv_hello_user);
        tvCalGoal = vHome.findViewById(R.id.tv_goal);
        btnCalGoal = vHome.findViewById(R.id.btn_add_cal_goal);
        btnCalGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditGoalDialog();
            }
        });

        GetNameAsyncTask getName = new GetNameAsyncTask();
        getName.execute();

        sharedPrefCalGoal = getActivity().getPreferences(Context.MODE_PRIVATE);
        int calorieGoalDefault = getResources().getInteger(R.integer.saved_calorie_goal_default_value);
        int calorieGoalKey = sharedPrefCalGoal.getInt(getResources().getString(R.string.saved_calorie_goal_key), calorieGoalDefault);
        tvCalGoal.setText(String.valueOf(calorieGoalKey));

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
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_calorie_goal_key), Integer.parseInt(inputNumber));
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

    private class GetNameAsyncTask extends AsyncTask<Void, Void, String> {

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
