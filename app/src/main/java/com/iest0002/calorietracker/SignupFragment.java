package com.iest0002.calorietracker;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Locale;

public class SignupFragment extends Fragment {

    private View vSignup;
    private Button bSubmit;
    private EditText etDob;
    private Spinner spnGender;
    private Spinner spnLevelOfActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.signup);

        vSignup = inflater.inflate(R.layout.fragment_signup, container, false);

        etDob = vSignup.findViewById(R.id.et_dob);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        spnGender = vSignup.findViewById(R.id.spn_gender);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(genderAdapter);

        spnLevelOfActivity = vSignup.findViewById(R.id.spn_level_of_activity);
        ArrayAdapter<CharSequence> levelOfActivityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.level_of_activity_array, android.R.layout.simple_spinner_item);
        levelOfActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevelOfActivity.setAdapter(levelOfActivityAdapter);

        bSubmit = vSignup.findViewById(R.id.btn_signup);


        return vSignup;
    }

    /**
     * ref: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText etDob = getActivity().findViewById(R.id.et_dob);
            etDob.setText(String.format(Locale.ENGLISH,"%d/%d/%d", day, month, year));
        }
    }
}
