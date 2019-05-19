package com.iest0002.calorietracker.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class RestClient {

    public static final String TAG = RestClient.class.getName();

    public static final String MY_DB_URL = String.format("http://%s/CalorieTrackerBackend/webresources/", Constants.MY_DB_ADDRESS);

    public static final String USER_METHOD_PATH = "entities.usr/";
    public static final String CRED_METHOD_PATH = "entities.credential/";
    public static final String FOOD_METHOD_PATH = "entities.food/";
    public static final String CONSUMPTION_METHOD_PATH = "entities.consumption/";
    public static final String USERNAME_EXISTS = "entities.credential/usernameExists/";
    public static final String FIND_CRED_BY_USERNAME = "entities.credential/findByUsername/";
    public static final String GET_FOOD_CATEGORIES = "entities.food/getFoodCategories/";
    public static final String FIND_FOOD_BY_CATEGORY = "entities.food/findByFoodCategory/";
    public static final String CALC_CAL_CONSUMED = "entities.consumption/calculateTotalCaloriesConsumed/";
    public static final String CALC_CAL_BURNED = "entities.usr/calculateCaloriesBurned/";
    public static final String GET_REPORT_BY_DATE = "entities.report/getReportByUserIdAndDate/";
    public static final String GET_REPORT_PER_DAY = "entities.report/getReportPerDay/";
    public static final String FIND_USER_BY_EMAIL = "entities.usr/findByUserEmail/";
    public static final String REPORT_METHOD_PATH = "entities.report/";

    public static final String NDB_FOOD = String.format("https://api.nal.usda.gov/ndb/V2/reports?ndbno=%%s&type=b&format=json&api_key=%s", Constants.NDB_KEY);
    public static final String NDB_FOOD_ID = String.format("https://api.nal.usda.gov/ndb/search/?format=json&q=%%s&sort=n&max=25&offset=0&api_key=%s&ds=Standard+Reference", Constants.NDB_KEY);

    public static final String GOOGLE_SEARCH = String.format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%%s", Constants.GOOGLE_SEARCH_KEY, Constants.GOOGLE_SEARCH_CX);

    public static final String OPENCAGE = String.format("https://api.opencagedata.com/geocode/v1/json?q=%%s&key=%s", Constants.OPENCAGE_KEY);

    /**
     * ref: FIT5046 Week7 tutorial
     */
    public static String post(String methodPath, Object object) {
        //initialise
        HttpURLConnection conn = null;
        StringBuffer result = new StringBuffer();
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            String stringCourseJson = gson.toJson(object);

            URL url = new URL(MY_DB_URL + methodPath);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST");
            //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringCourseJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringCourseJson);
            out.close();
            Log.i(TAG, stringCourseJson);
            Log.i(TAG, Integer.toString(conn.getResponseCode()));

            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                result.append(inStream.nextLine());
            }
            Log.i(TAG, result.toString());
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred while executing POST of " + Object.class.getName(), e);
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }

    public static String myDbGet(String methodPath, String... params) {
        if (params != null) {
            StringBuffer sb = new StringBuffer(methodPath);
            for (String param: params) {
                param = URLEncoder.encode(param);
                param = param.replace("+", "%20");
                sb.append(param);
                sb.append("/");
            }
            methodPath = sb.toString();
        }
        return get(MY_DB_URL + methodPath);
    }

    public static String ndbGet(String methodPath, String param) {
        param = URLEncoder.encode(param);
        String spec = String.format(methodPath, param);
        return get(spec);
    }

    public static String openCageGet(String address) {
        address = URLEncoder.encode(address);
        String spec = String.format(OPENCAGE, address);
        return get(spec);
    }

    public static String googleGet(String path, String query) {
        query = URLEncoder.encode(query);
        String spec = String.format(path, query);
        return get(spec);
    }

    public static String foursquareGet(String query) {
        query = URLEncoder.encode(query);
        String path = "https://api.foursquare.com/v2/venues/search?" +
                "client_id=%s" +
                "&client_secret=%s" +
                "&ll=%s&radius=5000&v=20190519&categoryId=4bf58dd8d48988d163941735";
        String spec = String.format(path, Constants.FOURSQUARE_CLIENT_ID, Constants.FOURSQUARE_CLIENT_SECRET, query);
        return get(spec);
    }

    private static String get(String spec) {
        HttpURLConnection conn = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(spec);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                result.append(inStream.nextLine());
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred while executing GET", e);
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }
}
