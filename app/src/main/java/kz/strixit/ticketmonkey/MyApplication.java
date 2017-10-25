package kz.strixit.ticketmonkey;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by saken2316 on 10/19/17.
 */

public class MyApplication extends Application {

    private String token;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    // Setter getter
    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void startAlarm() {

        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set( Calendar.MINUTE, 00 );
        calendar.set( Calendar.SECOND, 00 );

        // Every 12 hours
        final  int interval = 1000 * 60 * 60 * 12;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
    }
}
