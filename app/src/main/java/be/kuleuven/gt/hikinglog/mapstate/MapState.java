package be.kuleuven.gt.hikinglog.mapstate;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MapState {
    Context context;
    SQLControl control;
    private final String QUEUE_URL =  "https://studev.groept.be/api/a23PT313/";

    public MapState(Context context) {
        this.context = context;
        control = new SQLControl(context);
    }

    public static int latestPathId;
    public void postMap(LatLng coords, VolleyCallback callback){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        latestPathId = sharedPreferences.getInt("latestPathId", 1);
        int idusr = sharedPreferences.getInt("usrId", 1);
        String coordsFormat = (String.valueOf(coords.latitude + "," + coords.longitude)).replace('.', '_');
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateTimeString = formatter.format(date);

        String madeURL = QUEUE_URL + "addCoord/" + idusr + "/" +coordsFormat + "/" + latestPathId + "/" + dateTimeString;

        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "addCoord/" + idusr + "/" +coordsFormat + "/" + latestPathId + "/" + dateTimeString,
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

    public void savePath(String pathname, VolleyCallback callback){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "savePath/" + pathname + "/" +latestPathId ,
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

    public void renamePath(String pathname){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "renamePath/" + pathname + "/" +latestPathId ,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
    public void deletePath(VolleyCallback callback){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "deletePath/" + latestPathId ,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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

    public void deletePathEntries(){
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                QUEUE_URL + "deleteEntries/" + latestPathId ,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
