package com.iest0002.calorietracker.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.Consumption;
import com.iest0002.calorietracker.data.Food;
import com.iest0002.calorietracker.data.GoogleSearchResult;
import com.iest0002.calorietracker.data.RestClient;
import com.iest0002.calorietracker.data.User;

import java.util.ArrayList;
import java.util.Date;

public class DietFragment extends Fragment {
    private ProgressDialog progressDialog;
    private Spinner spnCategory, spnFood;
    private ImageView ivFood;

    private TextView tvCalAmount, tvFat, tvServUnit, tvServ, tvFoodDesc;
    private FrameLayout flFoodDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vDiet = inflater.inflate(R.layout.fragment_diet, container, false);

        tvCalAmount = vDiet.findViewById(R.id.tv_cal_amount);
        tvFat = vDiet.findViewById(R.id.tv_fat);
        tvServUnit = vDiet.findViewById(R.id.tv_serv_unit);
        tvServ = vDiet.findViewById(R.id.tv_serv);
        tvFoodDesc = vDiet.findViewById(R.id.tv_food_desc);
        flFoodDesc = vDiet.findViewById(R.id.fl_food_desc);

        ivFood = vDiet.findViewById(R.id.iv_food);

        Button btnConsumed = vDiet.findViewById(R.id.btn_food_consumed);
        btnConsumed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostConsumedFoodAsyncTask postConsumedFood = new PostConsumedFoodAsyncTask();
                postConsumedFood.execute();
            }
        });

        spnCategory = vDiet.findViewById(R.id.spn_food_category);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                GetFoodsAsyncTask getFood = new GetFoodsAsyncTask();
                getFood.execute(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnFood = vDiet.findViewById(R.id.spn_food);
        spnFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Food food = (Food) parent.getItemAtPosition(position);
                tvCalAmount.setText(Double.toString(food.getCalorieAmount()));
                tvFat.setText(Double.toString(food.getFat()));
                tvServUnit.setText(food.getServingUnit());
                tvServ.setText(Double.toString(food.getServingAmount()));

                GoogleSearchAsyncTask googleSearchAsyncTask = new GoogleSearchAsyncTask();
                googleSearchAsyncTask.execute(food.getFoodName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fabDiet = vDiet.findViewById(R.id.fab_diet);
        fabDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment addFoodDialogFragment = AddFoodDialogFragment.newInstance();
                addFoodDialogFragment.show(ft, "dialog");
            }
        });

        GetCategoriesAsyncTask getCategories = new GetCategoriesAsyncTask();
        getCategories.execute();
        return vDiet;
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        GetCategoriesAsyncTask getCategoriesAsyncTask = new GetCategoriesAsyncTask();
        getCategoriesAsyncTask.execute();
    }*/

    private class GetCategoriesAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return RestClient.myDbGet(RestClient.GET_FOOD_CATEGORIES);
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
            if (!TextUtils.isEmpty(response)) {
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("category").getAsJsonArray();
                ArrayList<String> list = new ArrayList<>();
                for (JsonElement json : jsonArray) {
                    list.add(json.getAsString());
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategory.setAdapter(categoryAdapter);
            } else {
                new AlertDialog.Builder(getContext())
                        .setCancelable(false)
                        .setMessage("Check your internet connection")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment homeFragment = new HomeFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame, homeFragment)
                                        .commit();
                                ((NavigationView) getActivity().findViewById(R.id.nav_view))
                                        .setCheckedItem(R.id.nav_home);
                            }
                        })
                        .show();
            }
        }
    }

    private class GetFoodsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(getActivity(), null, "Loading...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            return RestClient.myDbGet(RestClient.FIND_FOOD_BY_CATEGORY, params[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            //progressDialog.cancel();
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new GsonBuilder().create();
                Food[] foods = gson.fromJson(response, Food[].class);
                ArrayAdapter<Food> foodAdapter = new ArrayAdapter<Food>(getActivity(),
                        android.R.layout.simple_spinner_item, foods);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnFood.setAdapter(foodAdapter);
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Check your internet connection")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }

    private class GoogleSearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.googleSearchGet(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                GoogleSearchResult googleSearchResult = gson.fromJson(response, GoogleSearchResult.class);

                String snippet = null;
                String imgUrl = null;
                try {
                    snippet = googleSearchResult.getItems().get(0).getSnippet();
                    imgUrl = googleSearchResult.getItems().get(0).getPagemap().getCseImage().get(0).getSrc();
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Exception occurred while parsing google search result: " + e.getMessage());
                }

                if (snippet != null && imgUrl != null) {
                    Glide.with(getContext())
                            .load(imgUrl)
                            .into(ivFood);
                    snippet = snippet.replace("\r\n", " ").replace("\n", " ");
                    snippet = snippet.replace(",", "");
                    if (snippet.contains("."))
                        snippet = snippet.split("\\.")[0];
                    snippet += ".";
                    tvFoodDesc.setText(snippet);
                    flFoodDesc.setVisibility(View.VISIBLE);
                } else {
                    ivFood.setImageResource(R.drawable.img_noimage);
                    flFoodDesc.setVisibility(View.INVISIBLE);
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Check your internet connection")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }

    private class PostConsumedFoodAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
            );
            int userIdDefault = getResources().getInteger(R.integer.saved_default_user_id);
            int userId = sharedPref.getInt(getResources().getString(R.string.saved_user_id_key), userIdDefault);
            Food food = (Food) spnFood.getSelectedItem();

            String userJson = RestClient.myDbGet(RestClient.USER_METHOD_PATH, Integer.toString(userId));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            User user = gson.fromJson(userJson, User.class);

            Consumption cons = new Consumption(new Date(), 1, food, user);
            return RestClient.post(RestClient.CONSUMPTION_METHOD_PATH, cons);
        }

        @Override
        protected void onPostExecute(String response) {
            if (!TextUtils.isEmpty(response)) {
                Toast.makeText(getContext(), "Food has been added to Consumption table", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
