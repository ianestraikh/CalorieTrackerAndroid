package com.iest0002.calorietracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iest0002.calorietracker.R;

public class MapFragment extends Fragment {
    View vMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMap = inflater.inflate(R.layout.fragment_map, container, false);
        return vMap;
    }
}