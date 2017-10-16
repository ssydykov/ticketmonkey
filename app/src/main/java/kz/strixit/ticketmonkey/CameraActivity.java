package kz.strixit.ticketmonkey;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraActivity extends Activity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "Main Activity";
    private ZXingScannerView mScannerView;

    private static final String url = "http://tktmonkey.kz/api/events/approve-ticket/";
    private String tokenJwt, token;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        Intent intent = getIntent();
        tokenJwt = intent.getStringExtra("token");
        Log.e("Intent token JWT", tokenJwt);

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

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(CameraActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    // If Success

                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.d("Response", response);
                        startNewActivity(response);
                    }
                },
                new Response.ErrorListener()
                {
                    // If Error

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        Log.e("Error.Response", error.toString());
                        startNewActivity(error.toString());
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
                params.put("Authorization", "JWT " + tokenJwt);
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        requestQueue.add(postRequest);

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    private void startNewActivity(String message){

        Intent intent = new Intent(CameraActivity.this, MainActivity.class);
        intent.putExtra("qr_code", message);
        startActivity(intent);
    }
}
