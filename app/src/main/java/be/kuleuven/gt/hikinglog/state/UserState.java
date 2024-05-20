package be.kuleuven.gt.hikinglog.state;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public void changeUsername(String usrnameNew, VolleyCallback callback){
        String nameOfService = "changeUsername";
        String userid = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("userid", "newusername"), asList(userid,usrnameNew));
        control.executePostRequest(nameOfService, params, callback);
    }
    public void sendFriendRequest(int idprofile, VolleyCallback callback){
        String nameOfService = "sendFriendRequest";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("iduser", "idprofile"), asList(String.valueOf(iduser), String.valueOf(idprofile)));
        control.executePostRequest(nameOfService, params, callback);
    }
    public List<String> suggestUsermames(String lettersEntered, VolleyCallback callback){
        return null;
    }
    public void findPasswordByUsername(String username, VolleyCallback callback){
        String URL_extension = SQLControl.urlBuilder("findPasswordByUsername", username);
        control.executeGetRequest(URL_extension, callback);
    }
    public void findFriends(VolleyCallback callback){
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        String URL_extension = SQLControl.urlBuilder("findFriendsAndUsernames", String.valueOf(iduser), iduser);
        control.executeGetRequest(URL_extension, callback);
    }
    public void getUsernameForID(int idprofile, VolleyCallback callback){
        String URL_extension = SQLControl.urlBuilder("getUsernameForID", String.valueOf(idprofile));
        control.executeGetRequest(URL_extension, callback);
    }
    public void getMessagesPerPair(int profileid, VolleyCallback callback) {
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        String URL_extension = SQLControl.urlBuilder("getMessagesPerPair", iduser, iduser, String.valueOf(profileid), String.valueOf(profileid) );
        control.executeGetRequest(URL_extension, callback);
    }
    public void sendMessage(String message, int profileId, VolleyCallback callback){
        String nameOfService = "sendMessage";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateTimeString = formatter.format(new Date());
        Map<String, String> params = SQLControl.paramBuilder(asList("idusersender", "iduserreceiver", "message", "time"), asList(String.valueOf(iduser), String.valueOf(profileId), message, dateTimeString));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void setUp(Context context){
        this.context = context;
    }


}
