package com.iest0002.calorietracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iest0002.calorietracker.R;

public class TrackerFragment extends Fragment {

    View vTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vTracker = inflater.inflate(R.layout.fragment_tracker, container, false);

        return vTracker;
    }
}