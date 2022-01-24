package com.example.alpha.FirebaseActions;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CheckIfNicknameExists {


    public interface CheckIfNicknameExitsListener{
        void onPostExecuteConcluded(boolean result);
    }
private CheckIfNicknameExitsListener mListener;

    final public void setListener(CheckIfNicknameExitsListener listener){
        mListener=listener;
    }
    public void sendRequest(Context context, final String request) {

        String requestUrl = "https://us-central1-helpme-45.cloudfunctions.net/checkIfNicknameExists";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
           if (response.equals("true")){
                mListener.onPostExecuteConcluded(true);
                }else{
                mListener.onPostExecuteConcluded(false);
           }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("nickName", request);

                return postMap;
            }
        };
//make the request to your server as indicated in your request url
        Volley.newRequestQueue(context).add(stringRequest);

    }
}
