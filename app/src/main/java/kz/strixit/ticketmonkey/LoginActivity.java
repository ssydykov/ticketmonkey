package kz.strixit.ticketmonkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

//    private Button signInButton;
    private EditText loginEditText, pswEditText;
    private ProgressBar progressBar;

    private String token;

    private static final String url = "http://tktmonkey.kz/api/login_reg/api-token-auth/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        pswEditText = (EditText) findViewById(R.id.pswEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // On click listener:
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInButtonOnClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
        final String login = prefs.getString("login", "");
        final String psw = prefs.getString("psw", "");

        if (!login.isEmpty() && !psw.isEmpty()) {

            loginQuery(login, psw);
        }
    }

    private void signInButtonOnClick() {

        final String login = loginEditText.getText().toString();
        final String psw = pswEditText.getText().toString();

        // Request
        loginQuery(login, psw);
    }

    private void loginQuery(final String login, final String psw){

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    // If Success

                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Login Activity", "Response: " + response);

                        // Parsing json object
                        try {

                            JSONObject tokenJsonObject = new JSONObject(response);
                            token = tokenJsonObject.getString("token");
                            Log.e("Login Activity", "Token " + token);

                            ((MyApplication) getApplication()).setToken(token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Save user
                        SharedPreferences.Editor editor = getSharedPreferences("user_pref", MODE_PRIVATE).edit();
                        editor.putString("login", login);
                        editor.putString("psw", psw);
                        editor.apply();

                        // Start new Activity
                        Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    // If Error

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("Login Activity", "Request error " + error.toString());

                        if (error.networkResponse.statusCode == 400) {

                            Log.e("Login activity", "Wrong password");
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                // Post params

                Map<String, String> params = new HashMap<>();
                params.put("username", login);
                params.put("password", psw);

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
}
