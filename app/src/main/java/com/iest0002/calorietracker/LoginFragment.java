package com.iest0002.calorietracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

public class LoginFragment extends Fragment {

    private View vLogin;
    private Button bLogin;
    private Button bSignup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.login);

        vLogin = inflater.inflate(R.layout.fragment_login, container, false);
        bLogin = vLogin.findViewById(R.id.btn_login);
        bSignup = vLogin.findViewById(R.id.btn_signup);
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_welcome, new SignupFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return vLogin;
    }
}
