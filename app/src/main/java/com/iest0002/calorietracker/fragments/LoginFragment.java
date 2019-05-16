package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.MainActivity;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.WelcomeActivity;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.data.Credential;
import com.iest0002.calorietracker.data.RestClient;

import at.favre.lib.crypto.bcrypt.BCrypt;

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
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((WelcomeActivity) getActivity()).validateInput(vLogin)) {
                    LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                    loginAsyncTask.execute(
                            etUsername.getText().toString(),
                            etPassword.getText().toString()
                    );
                }
            }
        });
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

    private class LaunchSignupAsyncTask extends AsyncTask<String, Void, Integer> {
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            String response = RestClient.myDbGet(RestClient.USERNAME_EXISTS_METHOD_PATH, params[0]);
            JsonObject jsonObject;
            if (!TextUtils.isEmpty(response)) {
                jsonObject = new JsonParser().parse(response).getAsJsonObject();
            } else {
                return -1;
            }
            return jsonObject.get("usernameExists").getAsInt();
        }

        @Override
        protected void onPostExecute(Integer usernameCount) {
            String alertMessage;
            progressDialog.cancel();
            if (usernameCount == 0) {
                Fragment signupFragment = new SignupFragment();
                Bundle cred = new Bundle();
                cred.putString("username", etUsername.getText().toString());
                cred.putString("password", etPassword.getText().toString());
                signupFragment.setArguments(cred);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_welcome, signupFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            } else if (usernameCount >0) {
                alertMessage = "This username isn't available";
            } else {
                alertMessage = "Try again";
            }
            new AlertDialog.Builder(getContext())
                    .setMessage(alertMessage)
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String response = RestClient.myDbGet(RestClient.FIND_CRED_BY_USERNAME, params[0]);
            Credential cred;
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            if (!TextUtils.isEmpty(response)) {
                cred = gson.fromJson(response, Credential.class);
            } else {
                return false;
            }

            String hashPassword = cred.getPasswordHash();
            BCrypt.Result bCryptResult = BCrypt.verifyer().verify(params[1].toCharArray(), hashPassword);
            if (!bCryptResult.verified) {
                return false;
            }

            AppDatabase db = ((WelcomeActivity) getActivity()).getDb();
            db.userDao().insert(cred.getUserId());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isUserInserted) {
            progressDialog.cancel();
            if (isUserInserted) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Try again")
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }
}
