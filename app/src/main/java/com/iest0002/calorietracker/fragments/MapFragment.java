package com.iest0002.calorietracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iest0002.calorietracker.R;
import com.iest0002.calorietracker.data.RestClient;

// ref: https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
public class MapFragment extends Fragment {
    View vMap;

    MapView mMapView;
    float lat, lng;
    private GoogleMap googleMap;
    private Button btnShowParks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMap = inflater.inflate(R.layout.fragment_map, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        final String userAddress = sharedPref.getString(getResources().getString(R.string.saved_user_address), "");

        mMapView = vMap.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
            }
        });

        OpenCageAsyncTask openCage = new OpenCageAsyncTask();
        openCage.execute(userAddress);

        btnShowParks = vMap.findViewById(R.id.btn_show_parks);
        btnShowParks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != 0f && lng != 0f) {
                    FoursquareAsyncTask foursquare = new FoursquareAsyncTask();
                    foursquare.execute(String.format("%f,%f", lat, lng));
                }
            }
        });

        return vMap;
    }

    /*
    private class GeocodeAddressAndParksAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String geoCodeResponse = RestClient.googleGet(RestClient.GOOGLE_ADDRESS, strings[0]);

            JsonObject jsonObject = new JsonParser().parse(geoCodeResponse)
                    .getAsJsonObject()
                    .get("candidates")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("geometry")
                    .getAsJsonObject()
                    .get("location")
                    .getAsJsonObject();

            float[] location = new float[]{
                    jsonObject.get("lat").getAsFloat(),
                    jsonObject.get("long").getAsFloat()
            };
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
    */

    private class OpenCageAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return RestClient.openCageGet(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String msg = null;
            if (!TextUtils.isEmpty(s)) {
                JsonArray jsonArray = new JsonParser().parse(s)
                        .getAsJsonObject()
                        .get("results")
                        .getAsJsonArray();
                if (jsonArray.size() != 0) {
                    JsonObject jsonObject = jsonArray
                            .get(0)
                            .getAsJsonObject()
                            .get("geometry")
                            .getAsJsonObject();

                    lat = jsonObject.get("lat").getAsFloat();
                    lng = jsonObject.get("lng").getAsFloat();

                    LatLng homeAddress = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(homeAddress).title("Home"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(homeAddress).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    btnShowParks.setEnabled(true);

                    return;
                } else {
                    msg = getString(R.string.msg_address_error);
                }
            } else {
                msg = getString(R.string.msg_something_went_wrong);
            }
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class FoursquareAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return RestClient.foursquareGet(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            String msg;
            if (!TextUtils.isEmpty(s)) {
                JsonArray jsonArray = new JsonParser().parse(s)
                        .getAsJsonObject()
                        .get("response")
                        .getAsJsonObject()
                        .get("venues")
                        .getAsJsonArray();
                if (jsonArray.size() != 0) {
                    for (JsonElement jsonElement : jsonArray) {
                        JsonObject jsonLocation = jsonElement
                                .getAsJsonObject()
                                .get("location")
                                .getAsJsonObject();

                        float parkLat = jsonLocation.get("lat").getAsFloat();
                        float parkLng = jsonLocation.get("lng").getAsFloat();

                        String name = jsonElement
                                .getAsJsonObject()
                                .get("name")
                                .getAsString();

                        LatLng loc = new LatLng(parkLat, parkLng);
                        googleMap.addMarker(new MarkerOptions().position(loc).title(name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        btnShowParks.setEnabled(false);
                    }
                    return;
                } else {
                    msg = getString(R.string.msg_something_went_wrong);
                }
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
