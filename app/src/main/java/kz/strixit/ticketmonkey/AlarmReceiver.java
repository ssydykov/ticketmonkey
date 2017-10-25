package kz.strixit.ticketmonkey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by saken2316 on 10/25/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private String token;
    private final String TAG = "Alarm Receiver";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "Work");

        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("user_pref", MODE_PRIVATE);
        token = prefs.getString("token", "");

        getToken(context);
    }

    private void getToken(Context context) {

        // Request - refresh user token
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.REFRESH_TOKEN_URL,
                new Response.Listener<String>()
                {
                    // If Success

                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.d(TAG, "Responce: " + response);

                        // Parsing json object
                        try {

                            JSONObject tokenJsonObject = new JSONObject(response);
                            token = tokenJsonObject.getString("token");
                            Log.e(TAG, "Token " + token);

                            updateToken();

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
                        Log.e(TAG, "Request error " + error.toString());
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
    }

    private void updateToken() {

        SharedPreferences.Editor editor = context.getSharedPreferences("user_pref", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }
}
