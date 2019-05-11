package com.iest0002.calorietracker.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {

    public static final String TAG = RestClient.class.getName();
    public static final String BASE_URL = "http://118.138.65.130:8080/CalorieTrackerBackend/webresources/";

    public static final String CREATE_USER_METHOD_PATH = "entities.usr/";
    public static final String CREATE_CRED_METHOD_PATH = "entities.credential/";
    public static final String USERNAME_EXISTS_METHOD_PATH = "entities.credential/usernameExists/";
    public static final String FIND_CRED_BY_USERNAME = "entities.credential/findByUsername/";

    /**
     * ref: FIT5046 Week7 tutorial
     */
    public static String post(Object object, String methodPath) {
        //initialise
        URL url;
        HttpURLConnection conn = null;
        StringBuffer result = new StringBuffer();
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create();
            String stringCourseJson = gson.toJson(object);

            url = new URL(BASE_URL + methodPath);
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
            Log.e(TAG, "Exception occurred while posting " + Object.class.getName(), e);
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }

    public static String get(String methodPath, String... params) {
        URL url;
        HttpURLConnection conn = null;
        if (params != null) {
            StringBuffer sb = new StringBuffer(methodPath);
            for (String param: params) {
                sb.append(param);
                sb.append("/");
            }
            methodPath = sb.toString();
        }
        StringBuffer result = new StringBuffer();
        try {
            url = new URL(BASE_URL + methodPath);
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
            Log.e(TAG, "Exception occurred while checking if username exists", e);
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }
}
