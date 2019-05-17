package com.iest0002.calorietracker.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.Steps;

import java.util.List;


public class StepsAdapter extends ArrayAdapter<Steps> {

    public StepsAdapter(Context context, List<Steps> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Steps steps = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.steps_layout, parent, false);
        }

        TextView tvStepsAmount = convertView.findViewById(R.id.tv_steps_amount);
        TextView tvDate = convertView.findViewById(R.id.tv_steps_date);

        tvStepsAmount.setText(Integer.toString(steps.getStepsAmount()));
        tvDate.setText(steps.getDate());

        return convertView;
    }
}