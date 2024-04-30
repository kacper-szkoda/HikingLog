package be.kuleuven.gt.hikinglog.mapstate;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;

public class MapState {
    Context context;
    SQLControl control;
    private final String QUEUE_URL =  "https://studev.groept.be/api/a23PT313/";

    public MapState(Context context) {
        this.context = context;
        control = new SQLControl(context);
    }

    public static int latestPathId;
    public static void postMap(LatLng coords){

    }

    public void recoverMap(){

    }

    public void startPath(int idusr, VolleyCallback callback){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "initPath/" + idusr ,
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
                                "Unable to communicate with the server",
                                Toast.LENGTH_LONG).show();
                    }
                });
        control.getRequestQueue().add(queueRequest);
    }

    public void getLatestPathId(int idusr, final VolleyCallback callback){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "getLatestPathId/" + idusr,
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
                                "Unable to communicate with the server",
                                Toast.LENGTH_LONG).show();
                    }
                });
        control.getRequestQueue().add(queueRequest);
    }
}
