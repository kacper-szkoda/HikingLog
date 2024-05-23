package be.kuleuven.gt.hikinglog.state;

import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import be.kuleuven.gt.hikinglog.helpers.SQLControl;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;

public enum UserState {
    INSTANCE;
    public static int latestPathId;
    final SQLControl control = SQLControl.INSTANCE;
    private final String QUEUE_URL = "https://studev.groept.be/api/a23PT313/";
    RequestQueue requestQueue;
    Context context;
    SharedPreferences sharedPreferences;

    public void findByUsername(String usrname, VolleyCallback callback) {
        String nameOfService = "findIDForUsername";
        String URL_extension = SQLControl.urlBuilder(nameOfService, usrname);
        control.executeGetRequest(URL_extension, callback);
    }

    public void addNewUser(String usrname, String password, VolleyCallback callback) {
        String nameOfService = "addUser";
        usrname = usrname.trim();
        Map<String, String> params = SQLControl.paramBuilder(asList("password", "usrname"), asList(password, usrname));
        control.executePostRequest(nameOfService, params, callback);
    }

    public void changeUsername(String usrnameNew, VolleyCallback callback) {
        String nameOfService = "changeUsername";
        usrnameNew = usrnameNew.trim();
        String userid = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("userid", "newusername"), asList(userid, usrnameNew));
        control.executePostRequest(nameOfService, params, callback);
    }

    public void sendFriendRequest(int idprofile, VolleyCallback callback) {
        String nameOfService = "sendFriendRequest";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("iduser", "idprofile"), asList(iduser, String.valueOf(idprofile)));
        control.executePostRequest(nameOfService, params, callback);
    }

    public List<String> suggestUsermames(String lettersEntered, VolleyCallback callback) {
        return null;
    }

    public void findPasswordByUsername(String username, VolleyCallback callback) {
        username = username.trim();
        String URL_extension = SQLControl.urlBuilder("findPasswordByUsername", username);
        control.executeGetRequest(URL_extension, callback);
    }

    public void findFriends(VolleyCallback callback) {
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        String URL_extension = SQLControl.urlBuilder("findFriendsAndUsernames", iduser, iduser);
        control.executeGetRequest(URL_extension, callback);
    }

    public void getUsernameForID(int idprofile, VolleyCallback callback) {
        String URL_extension = SQLControl.urlBuilder("getUsernameForID", String.valueOf(idprofile));
        control.executeGetRequest(URL_extension, callback);
    }

    public void getMessagesPerPair(int profileid, VolleyCallback callback) {
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        String URL_extension = SQLControl.urlBuilder("getMessagesPerPair", iduser, iduser, String.valueOf(profileid), String.valueOf(profileid));
        control.executeGetRequest(URL_extension, callback);
    }

    public void sendMessage(String message, int profileId, VolleyCallback callback) {
        String nameOfService = "sendMessage";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateTimeString = formatter.format(new Date());
        Map<String, String> params = SQLControl.paramBuilder(asList("idusersender", "iduserreceiver", "message", "time"), asList(iduser, String.valueOf(profileId), message, dateTimeString));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void addFriend(int profileId, VolleyCallback callback) {
        String nameOfService = "sendFriendRequest";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("iduser", "idprofile"), asList(iduser, String.valueOf(profileId)));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void deleteRequest(int profileId, VolleyCallback callback) {
        String nameOfService = "deleteFriendRequest";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("idusersender", "iduserreceiver"), asList(String.valueOf(profileId), iduser));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void acceptFriend(int profileId, String date, VolleyCallback callback) {
        String nameOfService = "acceptFriend";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        Map<String, String> params = SQLControl.paramBuilder(asList("date", "idusrs", "idusrr"), asList(date, String.valueOf(profileId), iduser));
        SQLControl.INSTANCE.executePostRequest(nameOfService, params, callback);
    }

    public void findFriendsUsernamesLastMessages(VolleyCallback callback) {
        String nameOfService = "findFriendsUsernamesLastMessages";
        String iduser = String.valueOf(context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("usrId", 1));
        String URLExtension = SQLControl.urlBuilder(nameOfService, iduser, iduser);
        SQLControl.INSTANCE.executeGetRequest(URLExtension, callback);
    }

    public void setUp(Context context) {
        this.context = context;
    }
}
