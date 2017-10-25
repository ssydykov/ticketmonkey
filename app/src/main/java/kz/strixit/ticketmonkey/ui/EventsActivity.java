package kz.strixit.ticketmonkey.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.strixit.ticketmonkey.AlarmReceiver;
import kz.strixit.ticketmonkey.Constants;
import kz.strixit.ticketmonkey.adapter.EventsAdapter;
import kz.strixit.ticketmonkey.module.EventsModule;
import kz.strixit.ticketmonkey.MyApplication;
import kz.strixit.ticketmonkey.R;

public class EventsActivity extends AppCompatActivity {

    private String qrCodeMessage;
    private ListView listView;

    private List<EventsModule> events;

    private static final String TAG = "Main Activity";
    private String[] myStringArray = {"Item 1", "Item 2", "Item 3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myStringArray);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        ((MyApplication) getApplication()).setPendingIntent(PendingIntent.getBroadcast(this, 0, alarmIntent, 0));
        ((MyApplication) getApplication()).startAlarm();

        Intent intent = getIntent();
        qrCodeMessage = intent.getStringExtra("qr_code_message");

        getEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(EventsActivity.this, qrCodeMessage, Toast.LENGTH_SHORT).show();
    }

    private void getEvents() {

        // Request
        RequestQueue requestQueue = Volley.newRequestQueue(EventsActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Constants.GET_EVENTS_URL,
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

    private void displayEvents(final List<EventsModule> events) {

        EventsAdapter eventsAdapter = new EventsAdapter(events, EventsActivity.this);
        listView.setAdapter(eventsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String eventId = Integer.toString(events.get(i).getId());
                Log.e(TAG, eventId);

                Intent intent = new Intent(EventsActivity.this, CameraActivity.class);
                intent.putExtra("event_id", eventId);
                startActivityForResult(intent, 0);
            }
        });
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
