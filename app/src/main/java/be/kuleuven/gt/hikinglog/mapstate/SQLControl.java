package be.kuleuven.gt.hikinglog.mapstate;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class SQLControl {
    Context context;
    RequestQueue requestQueue;
    private final String QUEUE_URL =  "https://studev.groept.be/api/a23PT313/";
    public SQLControl(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void insert(VolleyCallback callback,String urlExtension){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        callback.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                "Unable to communicate with the server",
                                Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(queueRequest);
    }



}
