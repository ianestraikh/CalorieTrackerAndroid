package com.iest0002.calorietracker.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        final Button btnEdit = convertView.findViewById(R.id.btn_edit_steps);
        btnEdit.setTag(position);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) btnEdit.getTag();
                Steps steps = getItem(position);
                showEditSteps(steps);
                v.invalidate();
            }
        });
        Button btnDelete = convertView.findViewById(R.id.btn_delete_steps);
        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) btnEdit.getTag();
                Steps steps = getItem(position);
                remove(steps);
            }
        });

        return convertView;
    }

    // ref: https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void showEditSteps(final Steps steps) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit steps");
        builder.setCancelable(false);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(Integer.toString(steps.getStepsAmount()));
        input.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(5)
        });
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(input.getText()))
                    return;
                steps.setStepsAmount(Integer.parseInt(input.getText().toString()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}