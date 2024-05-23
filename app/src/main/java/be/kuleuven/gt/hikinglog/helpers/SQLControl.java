package be.kuleuven.gt.hikinglog.helpers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SQLControl {
    INSTANCE;
    private static final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    RequestQueue requestQueue;
    Context context;

    public static String urlBuilder(String... args) {
        String build = "";
        for (String arg : args) {
            build += arg;
            build += "/";
        }
        return build.substring(0, build.length() - 1);
    }

    public static Map<String, String> paramBuilder(List<String> keys, List<String> values) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            params.put(keys.get(i), values.get(i));
        }
        return params;
    }
    public void executeGetRequest(String urlExtension, VolleyCallback callback) {
        StringRequest queueRequest = new StringRequest(
                Request.Method.GET,
                QUEUE_URL + urlExtension,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        callback.onSuccess(stringResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                "Network error" + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(queueRequest);
    }

    public void executePostRequest(String nameOfService, Map<String, String> params, VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                QUEUE_URL + nameOfService,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        callback.onSuccess(stringResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                "Network error" + error,
                                Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setUp(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }
}
