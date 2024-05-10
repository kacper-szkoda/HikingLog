package be.kuleuven.gt.hikinglog.state;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

import java.util.Map;

import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import static java.util.Arrays.asList;

public enum UserState {
    INSTANCE;
    final SQLControl control = SQLControl.INSTANCE;
    RequestQueue requestQueue;
    private final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    Context context;
    SharedPreferences sharedPreferences;
    public static int latestPathId;
    public static int idusr;

    public void findByUsername(String usrname, VolleyCallback callback){
        String nameOfService = "findIDForUsername";
        Map<String, String> params = SQLControl.paramBuilder(asList("usrname"), asList(usrname));
        control.executePostRequest(nameOfService, params, callback);
    }
    public void addNewUser(String usrname, String password, VolleyCallback callback){
        String nameOfService = "addUser";
        Map<String, String> params = SQLControl.paramBuilder(asList("password", "usrname"), asList(password, usrname));
        control.executePostRequest(nameOfService, params, callback);
    }
    public void changeUsername(String usrnameNew, String password, VolleyCallback callback){

    }
    public void sendFriendRequest(String usernameFriend, VolleyCallback callback){

    }
    public void suggestUsermames(String lettersEntered, VolleyCallback callback){

    }
    public void findPasswordByUsername(String username, VolleyCallback callback){
        String URL_extension = SQLControl.urlBuilder("findPasswordByUsername", username);
        control.executeGetRequest(URL_extension, callback);
    }
}
