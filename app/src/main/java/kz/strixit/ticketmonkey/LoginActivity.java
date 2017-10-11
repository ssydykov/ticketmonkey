package kz.strixit.ticketmonkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button signInButton;
    private EditText loginEditText, pswEditText;

    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String url = "http://tktmonkey.kz/api/login_reg/api-token-auth/";
    private static final String jsonBody = "{'username':'saken','password':'saken'}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (Button) findViewById(R.id.signInButton);
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

        Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void getJson() throws IOException {

        String json = authUser(url, jsonBody);
    }

    private String authUser(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
