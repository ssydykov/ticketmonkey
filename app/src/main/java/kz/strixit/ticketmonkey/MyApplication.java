package kz.strixit.ticketmonkey;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by saken2316 on 10/19/17.
 */

public class MyApplication extends Application {

    private Handler handler = new Handler();
    private String url = "http://tktmonkey.kz/api/login_reg/api-token-refresh/";
    private String token;

    private Runnable refreshToken = new Runnable() {
        @Override
        public void run() {
            try {

                Log.e("My Application", "Hello, handler");

                // Request - refresh user token
                RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.this);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            // If Success

                            @Override
                            public void onResponse(String response) {

                                // response
                                Log.d("Response", response);

                                // Parsing json object
                                try {

                                    JSONObject tokenJsonObject = new JSONObject(response);
                                    token = tokenJsonObject.getString("token");
                                    Log.e("My Application", "Token " + token);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            // If Error

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                // error
                                Log.e("My Application", "Request error " + error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        // Post params:

                        Map<String, String> params = new HashMap<>();
                        params.put("token", token);

                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        // Headers params

                        Map<String, String>  params = new HashMap<>();
                        params.put("Content-Type", "application/json");

                        return params;
                    }
                };
                requestQueue.add(postRequest);

            } finally {

                handler.postDelayed(refreshToken, 14 * 60000);
            }
        }
    };

    public void startRepeatingTask() {
        refreshToken.run();
    }
    public void stopRepeatingTask() {
        handler.removeCallbacks(refreshToken);
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
