package com.example.exercisesapp;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseDataService {

    // base url
    public static final String QUERY_FOR_EX_MUSCLE_BY_NAME = "https://api.api-ninjas.com/v1/exercises?name=";

    // context
    Context context;

    // constructor
    public ExerciseDataService(Context context) {
        this.context = context;
    }

    // volley listener
    public interface ExInfoByNameResponse {
        void onError(String message);

        void onResponse(ArrayList<ExInfoModel> exInfoModels);
    }

    public void getExInfoByName(String exName, ExInfoByNameResponse exInfoByNameResponse) {
        ArrayList<ExInfoModel> exInfoModels = new ArrayList<>();
        String url = QUERY_FOR_EX_MUSCLE_BY_NAME + exName;

        // get the array
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    // loop through the JSON list
                    for (int i = 0; i < response.length(); i++) {

                        // create new exercise model
                        ExInfoModel oneResultEx = new ExInfoModel();

                        // get a JSON object at position i
                        JSONObject oneAPIResult = (JSONObject) response.get(i);

                        // set model attributes to api object attributes
                        oneResultEx.setName(oneAPIResult.getString("name"));
                        oneResultEx.setType(oneAPIResult.getString("type"));
                        oneResultEx.setMuscle(oneAPIResult.getString("muscle"));
                        oneResultEx.setEquipment(oneAPIResult.getString("equipment"));
                        oneResultEx.setDifficulty(oneAPIResult.getString("difficulty"));
                        oneResultEx.setInstructions(oneAPIResult.getString("instructions"));

                        // add to model list
                        exInfoModels.add(oneResultEx);
                    }

                    exInfoByNameResponse.onResponse(exInfoModels);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        )   {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Api-Key", "PMhfR/rJJXo7TRcL3TLvSQ==AZO8ccTVmiOviIpZ");

                return params;
            }
    };

        // Add the request to the RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}

//    // Base url
//    String url = QUERY_FOR_EX_INFO + exName;
//
//    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//        @Override
//        public void onResponse(JSONArray response) {
//
//            String ExName = "";
//            try {
//                JSONObject ExInfo = response.getJSONObject(0);
//                ExName = ExInfo.getString("name");
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//
//            Toast.makeText(context, "Exercise name: " + ExName, Toast.LENGTH_SHORT).show();
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
//        }
//    }
//    )   {
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("X-Api-Key", "PMhfR/rJJXo7TRcL3TLvSQ==AZO8ccTVmiOviIpZ");
//
//            return params;
//        }
//    };
//
//// Add the request to the RequestQueue.
//        MySingleton.getInstance(context).addToRequestQueue(request);

