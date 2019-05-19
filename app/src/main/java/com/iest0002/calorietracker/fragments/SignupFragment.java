package com.iest0002.calorietracker.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.WelcomeActivity;
import com.iest0002.calorietracker.data.Credential;
import com.iest0002.calorietracker.data.RestClient;
import com.iest0002.calorietracker.data.User;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignupFragment extends Fragment {

    private View vSignup;
    private Button bSubmit;
    private EditText etFname, etLname, etEmail, etDob, etHeigh, etWeigh,
            etAddress, etPostcode, etStepsPerMile;
    private RadioGroup rgGender;
    private Spinner spnLevelOfActivity;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.signup);

        vSignup = inflater.inflate(R.layout.fragment_signup, container, false);

        etFname = vSignup.findViewById(R.id.et_fname);
        etLname = vSignup.findViewById(R.id.et_lname);
        etEmail = vSignup.findViewById(R.id.et_email);

        etDob = vSignup.findViewById(R.id.et_dob);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        etHeigh = vSignup.findViewById(R.id.et_height);
        etWeigh = vSignup.findViewById(R.id.et_weight);

        rgGender = vSignup.findViewById(R.id.rg_gender);

        etAddress = vSignup.findViewById(R.id.et_address);
        etPostcode = vSignup.findViewById(R.id.et_postcode);

        spnLevelOfActivity = vSignup.findViewById(R.id.spn_level_of_activity);
        ArrayAdapter<CharSequence> levelOfActivityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.level_of_activity_array, android.R.layout.simple_spinner_item);
        levelOfActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevelOfActivity.setAdapter(levelOfActivityAdapter);

        etStepsPerMile = vSignup.findViewById(R.id.et_steps_per_mile);

        bSubmit = vSignup.findViewById(R.id.btn_signup);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((WelcomeActivity) getActivity()).validateInput(vSignup)) {
                    PostAsyncTask postAsyncTask = new PostAsyncTask();
                    postAsyncTask.execute(
                            etFname.getText().toString(),
                            etLname.getText().toString(),
                            etEmail.getText().toString(),
                            etDob.getText().toString(),
                            etHeigh.getText().toString(),
                            etWeigh.getText().toString(),
                            ((RadioButton) vSignup.findViewById(rgGender.getCheckedRadioButtonId()))
                                    .getText().toString(),
                            etAddress.getText().toString(),
                            etPostcode.getText().toString(),
                            spnLevelOfActivity.getSelectedItem().toString(),
                            etStepsPerMile.getText().toString()
                    );
                }
            }
        });

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
            etDob.setText(String.format(Locale.ENGLISH, "%d/%d/%d", day, month + 1, year));
            etDob.setError(null);
        }
    }

    private class PostAsyncTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            String emailResponse = RestClient.myDbGet(RestClient.FIND_USER_BY_EMAIL, params[2]);
            if (new JsonParser().parse(emailResponse).getAsJsonArray().size() != 0) {
                return -1;
            }

            User user = new User(
                    params[0],
                    params[1],
                    params[2],
                    params[3],
                    Double.parseDouble(params[4]),
                    Double.parseDouble(params[5]),
                    params[6].charAt(0),
                    params[7],
                    params[8],
                    Integer.parseInt(params[9]),
                    Integer.parseInt(params[10])
            );
            String responseMessage = RestClient.post(RestClient.USER_METHOD_PATH, user);
            User returnedUser;
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            if (!TextUtils.isEmpty(responseMessage)) {
                returnedUser = gson.fromJson(responseMessage, User.class);
            } else {
                return 0;
            }

            // hash password
            String password = getArguments().getString("password");
            String hashPassword = BCrypt.withDefaults().hashToString(6, password.toCharArray());

            Credential cred = new Credential(
                    getArguments().getString("username"),
                    hashPassword,
                    new Date(),
                    returnedUser
            );
            responseMessage = RestClient.post(RestClient.CRED_METHOD_PATH, cred);
            if (!TextUtils.isEmpty(responseMessage))
                return 1;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer isUserCreated) {
            progressDialog.cancel();
            if (isUserCreated == 1) {
                new AlertDialog.Builder(getContext())
                        //TODO put this string inside resource file
                        .setMessage("Your account has been created!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getFragmentManager().popBackStack();
                            }
                        }).show();
            } else if (isUserCreated == 0){
                Toast.makeText(getContext(), R.string.msg_something_went_wrong, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), R.string.msg_email_not_available, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
