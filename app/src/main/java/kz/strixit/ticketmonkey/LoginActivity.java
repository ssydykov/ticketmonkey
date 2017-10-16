package kz.strixit.ticketmonkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class LoginActivity extends AppCompatActivity {

//    private Button signInButton;
    private EditText loginEditText, pswEditText;

    private String token;

    private static final String url = "http://tktmonkey.kz/api/login_reg/api-token-auth/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        pswEditText = (EditText) findViewById(R.id.pswEditText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInButtonOnClick();
            }
        });
    }

    private void signInButtonOnClick() {

        final String login = loginEditText.getText().toString();
        final String psw = pswEditText.getText().toString();

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {

                            JSONObject tokenJsonObject = new JSONObject(response);
                            token = tokenJsonObject.getString("token");
                            Log.d("token", token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        Log.e("Error.Response", error.toString());
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("username", login);
                params.put("password", psw);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
