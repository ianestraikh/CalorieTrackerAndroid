package com.iest0002.calorietracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DietFragment extends Fragment {
    View vDiet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vDiet = inflater.inflate(R.layout.fragment_diet, container, false);
        return vDiet;
    }
}
