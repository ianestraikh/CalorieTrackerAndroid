package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private ListView listView;
    private StepsAdapter stepsAdapter;

    private AppDatabase db;

    private ArrayList<Steps> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vSteps = inflater.inflate(R.layout.fragment_steps, container, false);
        listView = vSteps.findViewById(R.id.lv_steps);

        getActivity().setTitle(R.string.fr_title_steps);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Steps steps = (Steps) parent.getItemAtPosition(position);
                showEditSteps(steps, position);
            }
        });

        GetStepsAsyncTask getSteps = new GetStepsAsyncTask();
        getSteps.execute();

        FloatingActionButton fab = (FloatingActionButton) vSteps.findViewById(R.id.fab_steps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddSteps();
            }
        });

        return vSteps;
    }

    // ref: https://stackoverflow.com/questions/10903754/input-text-dialog-android
    public void showAddSteps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Steps");
        builder.setCancelable(false);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(5)
        });
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(input.getText()))
                    return;
                int stepsAmount = Integer.parseInt(input.getText().toString());
                Steps steps = new Steps(stepsAmount, new Date());

                InsertStepsAsyncTask insertSteps = new InsertStepsAsyncTask();
                insertSteps.execute(steps);

                list.add(steps);
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

    public void showEditSteps(final Steps steps, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit steps");
        builder.setCancelable(false);

        final EditText input = new EditText(getActivity());
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
                // without passing new object to list ListView doesn't refresh view
                int newStepsAmount = Integer.parseInt(input.getText().toString());
                list.set(position, new Steps(newStepsAmount, steps.getDate()));
                stepsAdapter.notifyDataSetChanged();

                steps.setStepsAmount(newStepsAmount);
                UpdateStepsAsyncTask updateSteps = new UpdateStepsAsyncTask();
                updateSteps.execute(steps);
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
        protected List<Steps> doInBackground(Void... voids) {
            return db.stepsDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Steps> stepsList) {
            list = (ArrayList) stepsList;
            stepsAdapter = new StepsAdapter(getActivity(), stepsList);
            listView.setAdapter(stepsAdapter);
        }
    }

    private class InsertAllStepsAsyncTask extends AsyncTask<Void, Void, Void> {
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

    private class InsertStepsAsyncTask extends AsyncTask<Steps, Void, Void> {
        @Override
        protected Void doInBackground(Steps... s) {
            db.stepsDao().insert(s[0]);
            return null;
        }
    }

    private class UpdateStepsAsyncTask extends AsyncTask<Steps, Void, Void> {
        @Override
        protected Void doInBackground(Steps... steps) {
            db.stepsDao().update(steps);
            return null;
        }
    }
}
