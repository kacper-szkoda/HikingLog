package be.kuleuven.gt.hikinglog.helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SQLControl {
    INSTANCE;
    RequestQueue requestQueue;
    Context context;
    private static final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void executeGetRequest( String urlExtension, VolleyCallback callback) {
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + urlExtension,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                (CharSequence) error,
                                Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(queueRequest);
    }

    public void executePostRequest(Map<String, String> params, VolleyCallback callback) {
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.POST,
                QUEUE_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                (CharSequence) error,
                                Toast.LENGTH_LONG).show();
                    }
                }
                ){
            @Override
            protected Map<String, String> getParams(){
                return params;
            }
        };
        requestQueue.add(queueRequest);
    }
    public static String urlBuilder( String... args) {
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

    public void setUp(Context context){
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }
}
