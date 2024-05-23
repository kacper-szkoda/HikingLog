package be.kuleuven.gt.hikinglog.state;

import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;

public enum MapState {
    INSTANCE;
    public static int latestPathId;
    public static int idusr;
    final SQLControl control = SQLControl.INSTANCE;
    private final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    RequestQueue requestQueue;
    Context context;
    SharedPreferences sharedPreferences;

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

    public void renameFromName(String newName, String oldName, int idusr, VolleyCallback callback) {
        String nameOfService = "renameFromName";
        Map<String, String> params = SQLControl.paramBuilder(asList("name", "pathname", "iduser"), asList(newName, oldName, String.valueOf(idusr)));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void getLatestPathId(VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("getLatestPathId", String.valueOf(idusr));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void renamePathEntries(String pathname, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("savePath", pathname, String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void renamePath(String pathname, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("renamePath", pathname, String.valueOf(latestPathId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void deletePath(VolleyCallback callback) {
        int userId = sharedPreferences.getInt("usrId", 1);
        String URL_Fin = SQLControl.urlBuilder("deletePath", String.valueOf(latestPathId), String.valueOf(userId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void deletePathEntries(VolleyCallback callback) {
        idusr = sharedPreferences.getInt("usrId", 1);
        String URL_Fin = SQLControl.urlBuilder("deleteEntries", String.valueOf(latestPathId), String.valueOf(idusr));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void getPathsPerUser(int usrId, VolleyCallback callback) {
        String URL_Fin = SQLControl.urlBuilder("getPathsPerUser", String.valueOf(usrId));

        control.executeGetRequest(URL_Fin, callback);
    }

    public void deleteEntirePathFromDialog(String pathname, VolleyCallback callback) {
        String nameOfService = "deleteEntries";
        idusr = sharedPreferences.getInt("usrId", 1);
        Map<String, String> params = SQLControl.paramBuilder(asList("pathname", "iduser"), asList(pathname, String.valueOf(idusr)));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
        params = SQLControl.paramBuilder(asList("pathname", "iduser"), asList(pathname, String.valueOf(idusr)));
        nameOfService = "deleteFromName";
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void setUpMapState(Context context) {
        control.setUp(context);
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        idusr = sharedPreferences.getInt("usrId", 1);
    }
}
