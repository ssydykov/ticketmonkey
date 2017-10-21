package kz.strixit.ticketmonkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String qrCode;
    private ListView listView;

    private List<EventsModule> events;

    private static final String TAG = "Main Activity";
    private static final String url = "http://tktmonkey.kz/api/events/org/all/";
    private String[] myStringArray = {"Item 1", "Item 2", "Item 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myStringArray);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        qrCode = intent.getStringExtra("qr_code");

        getEvents();
    }

    private void getEvents() {

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    // If Success

                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.d(TAG, "Response " + response);

                        events = parseJson(response);

                        for (EventsModule event: events) {

                            Log.d(TAG, "Id: " + event.getId());
                            Log.d(TAG, "Title: " + event.getTitle());
                            Log.d(TAG, "Description: " + event.getDescription());
                            Log.d(TAG, "Price: " + event.getMin_price());
                            Log.d(TAG, "Img: " + event.getPortrait_img());
                        }

                        displayEvents(events);
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

    private void displayEvents(List<EventsModule> events) {

        EventsAdapter eventsAdapter = new EventsAdapter(events, MainActivity.this);
        listView.setAdapter(eventsAdapter);
    }

    private List<EventsModule> parseJson(String response){

        JsonElement jsonElement = new JsonParser().parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("events");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<EventsModule>>(){}.getType();

        List<EventsModule> eventsModules = gson.fromJson(jsonArray, listType);

        return gson.fromJson(jsonArray, listType);
    }
}
