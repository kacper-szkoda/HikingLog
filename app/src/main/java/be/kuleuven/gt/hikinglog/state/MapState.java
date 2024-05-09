package be.kuleuven.gt.hikinglog.state;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;

public enum MapState {
    INSTANCE;
    final SQLControl control = SQLControl.INSTANCE;
    RequestQueue requestQueue;
    private final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    Context context;
    SharedPreferences sharedPreferences;
    public static int latestPathId;
    public static int idusr;


    public void postMap(LatLng coords, VolleyCallback callback) {
        latestPathId = sharedPreferences.getInt("latestPathId", 1);
        idusr = sharedPreferences.getInt("usrId", 1);
        String coordsFormat = (coords.latitude + "," + coords.longitude).replace('.', '_');
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateTimeString = formatter.format(new Date());

        String URL_Fin = SQLControl.urlBuilder("addCoord", String.valueOf(idusr), coordsFormat,
                String.valueOf(latestPathId), dateTimeString);

        control.executeGetRequest(URL_Fin, callback);
    }

    public void recoverMap(int idusr, String pathname, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("getEntriesForPath", String.valueOf(idusr), pathname);
        control.executeGetRequest(URL_Fin, callback);
    }

    public void startPath(VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("initPath", String.valueOf(idusr));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void getLatestPathId(VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("getLatestPathId", String.valueOf(idusr));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void savePath(String pathname, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("savePath", pathname, String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void renamePath(String pathname, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("renamePath", pathname, String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void deletePath(VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("deletePath", String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void deletePathEntries(VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("deleteEntries", String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void getPathsPerUser(int usrId, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("getPathsPerUser", String.valueOf(usrId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void setUpMapState(Context context){
        control.setUp(context);
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        idusr = sharedPreferences.getInt("usrId", 1);
    }
}
