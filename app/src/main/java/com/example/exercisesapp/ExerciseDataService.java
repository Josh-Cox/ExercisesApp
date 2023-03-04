package com.example.exercisesapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExerciseDataService {

    // -------------------- attributes -------------------- //

    // base url and API key
    public static final String QUERY_FOR_EX_MUSCLE_BY_NAME = "https://api.api-ninjas.com/v1/exercises?name=";
    public static final String API_KEY = "PMhfR/rJJXo7TRcL3TLvSQ==AZO8ccTVmiOviIpZ";

    // context
    Context context;

    /**
     * constructor
     * @param context context
     */
    public ExerciseDataService(Context context) {
        this.context = context;
    }

    /**
     * Volley listener
     */
    public interface ExInfoByNameResponse {
        void onError(String message);

        void onResponse(ArrayList<ExInfoModel> exInfoModels);
    }

    /**
     * gets a list of up to 10 exercise objects as a result of a name search
     * @param exName exercise name
     * @param exInfoByNameResponse exercise info from the given name
     */
    public void getExInfoByName(String exName, String[] filters, ExInfoByNameResponse exInfoByNameResponse) {
        // array of exercise objects
        ArrayList<ExInfoModel> exInfoModels = new ArrayList<>();

        // url for API request
        String url = QUERY_FOR_EX_MUSCLE_BY_NAME + exName + "&difficulty=" + filters[0]
                + "&muscle=" + filters[1] + "&type=" + filters[2];

        // get the array
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            /**
             * on API service response, collect and format the data
             * @param response response of the api request
             */
            @Override
            public void onResponse(JSONArray response) {

                try {
                    // loop through the JSON list
                    for (int i = 0; i < response.length(); i++) {

                        // create new exercise model
                        ExInfoModel oneResultEx = new ExInfoModel();

                        // get a JSON object at position i
                        JSONObject oneAPIResult = (JSONObject) response.get(i);

                        // set model attributes to API object attributes
                        oneResultEx.setName(oneAPIResult.getString("name"));
                        oneResultEx.setType(oneAPIResult.getString("type"));
                        oneResultEx.setMuscle(oneAPIResult.getString("muscle"));
                        oneResultEx.setEquipment(oneAPIResult.getString("equipment"));
                        oneResultEx.setDifficulty(oneAPIResult.getString("difficulty"));
                        oneResultEx.setInstructions(oneAPIResult.getString("instructions"));

                        // add to model list
                        exInfoModels.add(oneResultEx);
                    }

                    // call onResponse with list of returned exercise models
                    exInfoByNameResponse.onResponse(exInfoModels);

                    // catch and throw JSON exception
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            /**
             * catch and return Volley error
             * @param volleyError error on api request
             */
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError || volleyError instanceof AuthFailureError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError) {
                    message = "Cannot connect to Internet";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again later";
                }  else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again later";
                }

                Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        }
        )   {
            /**
             * add API key as a header in the API request
             * @return header with api key
             * @throws AuthFailureError authentication failure
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-Api-Key", API_KEY);

                return params;
            }
    };
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }
}
