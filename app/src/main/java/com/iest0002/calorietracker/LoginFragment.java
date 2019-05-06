package com.iest0002.calorietracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.iest0002.calorietracker.entities.User;

public class LoginFragment extends Fragment {

    private View vLogin;
    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private Button bSignup;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.login);

        vLogin = inflater.inflate(R.layout.fragment_login, container, false);

        etPassword = vLogin.findViewById(R.id.et_password);
        etUsername = vLogin.findViewById(R.id.et_username);

        bLogin = vLogin.findViewById(R.id.btn_login);
        bSignup = vLogin.findViewById(R.id.btn_signup);
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((WelcomeActivity) getActivity()).validateInput(vLogin)) {
                    LaunchSignupAsyncTask launchSignup = new LaunchSignupAsyncTask();
                    launchSignup.execute(etUsername.getText().toString());
                }
            }
        });
        return vLogin;
    }

    private class LaunchSignupAsyncTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            return RestClient.getPlainText(RestClient.USERNAME_EXISTS_METHOD_PATH, params[0]);
        }
        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
            if (!TextUtils.isEmpty(response) && Integer.parseInt(response) == 0) {
                Fragment signupFragment = new SignupFragment();
                Bundle creds = new Bundle();
                creds.putString("username", etUsername.getText().toString());
                creds.putString("password", etPassword.getText().toString());
                signupFragment.setArguments(creds);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_welcome, signupFragment)
                        .addToBackStack(null)
                        .commit();
            } else if (!TextUtils.isEmpty(response) && Integer.parseInt(response) != 0) {
                new AlertDialog.Builder(getContext())
                        .setMessage("This username isn't available")
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Check your internet connection")
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }
}
