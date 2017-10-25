package kz.strixit.ticketmonkey.ui;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import kz.strixit.ticketmonkey.Constants;
import kz.strixit.ticketmonkey.MyApplication;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraActivity extends Activity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Camera Activity";
    private ZXingScannerView mScannerView;

    private String token;
    private String eventId;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        Intent intent = getIntent();
        eventId = intent.getStringExtra("event_id");

        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    // Camera qr result:

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results
        Log.v(TAG, rawResult.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v(TAG, rawResult.getBarcodeFormat().toString());
        token = rawResult.getText();

        checkQrRequest();
    }

    private void checkQrRequest() {

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(CameraActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.CHECK_QR_URL,
                new Response.Listener<String>()
                {
                    // If Success

                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.d(TAG, "Response " + response);
                        startNewActivity(response);
                    }
                },
                new Response.ErrorListener()
                {
                    // If Error

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        Log.e(TAG, "Request error " + error.toString());

                        startNewActivity("Invalid qr code");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                // Post params:

                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("id", eventId);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                // Headers params

                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "JWT " + ((MyApplication) getApplication()).getToken());
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        requestQueue.add(postRequest);
    }
    private void startNewActivity(String message){

        Intent intent = new Intent(CameraActivity.this, EventsActivity.class);
        intent.putExtra("qr_code_message", message);
        startActivity(intent);
    }
}
