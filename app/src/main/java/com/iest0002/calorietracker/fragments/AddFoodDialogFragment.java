package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.Food;
import com.iest0002.calorietracker.data.NdbFood;
import com.iest0002.calorietracker.data.RestClient;

import java.util.ArrayList;

// ref: https://developer.android.com/reference/android/app/DialogFragment
public class AddFoodDialogFragment extends DialogFragment {

    private ProgressDialog progressDialog;

    private EditText etEnterFood;
    private Button btnAddFood;
    private Spinner spnFoodId;

    static AddFoodDialogFragment newInstance() {
        AddFoodDialogFragment f = new AddFoodDialogFragment();
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.add_food_dialog_title);

        View v = inflater.inflate(R.layout.fragment_add_food, container, false);

        etEnterFood = v.findViewById(R.id.et_enter_food);
        etEnterFood.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    FindFoodIdAsyncTask findFoodIdAsyncTask = new FindFoodIdAsyncTask();
                    findFoodIdAsyncTask.execute(etEnterFood.getText().toString());
                    return true;
                }
                return false;
            }
        });

        btnAddFood = v.findViewById(R.id.btn_add_food);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PostFoodAsyncTask postFoodAsyncTask = new PostFoodAsyncTask();
                postFoodAsyncTask.execute(spnFoodId.getSelectedItem());
            }
        });

        spnFoodId = v.findViewById(R.id.spn_food_id);

        return v;
    }

    private class FindFoodIdAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.ndbGet(RestClient.NDB_FOOD_ID, strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                try {
                    NdbFood ndbFood = gson.fromJson(response, NdbFood.class);
                    ArrayList<NdbFood.Item> items = ndbFood.getList().getItem();
                    ArrayAdapter<NdbFood.Item> itemAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, items);
                    itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnFoodId.setAdapter(itemAdapter);
                    spnFoodId.setVisibility(View.VISIBLE);
                    btnAddFood.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.e(AddFoodDialogFragment.class.getName(), "Exception occurred while GSON parsing");
                    new AlertDialog.Builder(getContext())
                            .setMessage("Try again")
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Check your internet connection")
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }

    private class PostFoodAsyncTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            String foodName = ((NdbFood.Item) objects[0]).getName();
            String foodGroup = ((NdbFood.Item) objects[0]).getGroup();
            String foodId = ((NdbFood.Item) objects[0]).getNdbno();
            String response = RestClient.ndbGet(RestClient.NDB_FOOD, foodId);
            if (!TextUtils.isEmpty(response)) {
                NdbFood.FoodItem ndbFood;
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                try {
                    ndbFood = gson.fromJson(response, NdbFood.class).getFoods().get(0).getFood();
                } catch (Exception e) {
                    Log.e(AddFoodDialogFragment.class.getName(), "Exception occurred while GSON parsing");
                    return false;
                }
                NdbFood.Nutrient energy = null;
                NdbFood.Nutrient fat = null;
                for (NdbFood.Nutrient nut : ndbFood.getNutrients()) {
                    if (nut.getNutrientId() == 208)
                        energy = nut;
                    else if (nut.getNutrientId() == 204)
                        fat = nut;
                }
                if (energy == null || fat == null)
                    return false;
                Food food = new Food(
                        foodName,
                        foodGroup,
                        energy.getMeasures().get(0).getValue(),
                        energy.getMeasures().get(0).getLabel(),
                        energy.getMeasures().get(0).getQty(),
                        fat.getMeasures().get(0).getValue()
                );
                String postResponse = RestClient.post(RestClient.FOOD_METHOD_PATH, food);
                return !TextUtils.isEmpty(postResponse);
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            progressDialog.cancel();
            if (isSuccessful) {
                new AlertDialog.Builder(getContext())
                        .setMessage("The food has been added")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dismiss();
                            }
                        }).show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Something went wrong")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }
}