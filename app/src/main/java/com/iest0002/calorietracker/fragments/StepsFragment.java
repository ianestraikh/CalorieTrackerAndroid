package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.iest0002.calorietracker.MainActivity;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.AppDatabase;
import com.iest0002.calorietracker.data.Steps;
import com.iest0002.calorietracker.views.StepsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepsFragment extends Fragment {

    private View vSteps;

    private ListView listView;
    private StepsAdapter stepsAdapter;

    private AppDatabase db;

    private ArrayList<Steps> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSteps = inflater.inflate(R.layout.fragment_steps, container, false);
        listView = vSteps.findViewById(R.id.lv_steps);

        GetStepsAsyncTask getSteps = new GetStepsAsyncTask();
        getSteps.execute();

        FloatingActionButton fab = (FloatingActionButton) vSteps.findViewById(R.id.fab_steps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddSteps();
            }
        });

        db = ((MainActivity) getActivity()).getDb();

        return vSteps;
    }

    @Override
    public void onPause() {
        super.onPause();
        SaveStepsListToDbAsyncTask saveStepsListToDb = new SaveStepsListToDbAsyncTask();
        saveStepsListToDb.execute();
    }

    // ref: https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void showAddSteps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Steps");
        builder.setCancelable(false);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(input.getText()))
                    return;
                Steps steps = new Steps(Integer.parseInt(input.getText().toString()), new Date());
                list.add(steps);
                stepsAdapter.notifyDataSetChanged();
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

    private class GetStepsAsyncTask extends AsyncTask<Void, Void, List<Steps>> {

        @Override
        protected void onPostExecute(List<Steps> stepsList) {
            stepsAdapter = new StepsAdapter(getContext(), stepsList);
            listView.setAdapter(stepsAdapter);
        }

        @Override
        protected List<Steps> doInBackground(Void... voids) {
            return db.stepsDao().getAll();
        }
    }

    private class SaveStepsListToDbAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... v) {
            db.stepsDao().deleteAll();
            if (list != null) {
                for (Steps steps : list) {
                    db.stepsDao().insert(steps);
                }
            }
            return null;
        }
    }


}
