package com.iest0002.calorietracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

public class HomeFragment extends Fragment {
    View vHome;
    ImageView imgFitness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_home, container, false);

        imgFitness = vHome.findViewById(R.id.img_fitness);

        return vHome;
    }

    @Override
    public void onStart() {
        super.onStart();
        final int[] imgFitnessResources = {R.drawable.img_fitness_1, R.drawable.img_fitness_2,
                R.drawable.img_fitness_3, R.drawable.img_fitness_4, R.drawable.img_fitness_5,
                R.drawable.img_fitness_6, R.drawable.img_fitness_7, R.drawable.img_fitness_8};
        final int rnd = new Random().nextInt(7);
        imgFitness.setImageResource(imgFitnessResources[rnd]);
    }
}
