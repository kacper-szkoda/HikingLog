package be.kuleuven.gt.hikinglog.state;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.helpers.ChatHeadClickedListener;

public class FriendModel {
    ArrayList<String> messagesDisplayed;
    int idprofile;
    String username;
    String dateAccepted;
    String lastMessage;
    boolean accepted;
    boolean sender;
    ChatHeadClickedListener listener;
    public FriendModel(int idprofile, String dateAccepted, String username, boolean sender) {
        this.idprofile = idprofile;
        this.username = username;
        this.dateAccepted = dateAccepted;
        this.sender = sender;
        if (dateAccepted.equals("null")){
            accepted = false;
        }
        else {
            accepted = true;
        }
    }
    public int getIdprofile() {
        return idprofile;
    }
    public String getUsername() {
        return username;
    }
    public String getDateAccepted() {
        return dateAccepted;
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public void setIdprofile(int idprofile) {
        this.idprofile = idprofile;
    }
    public void setDateAccepted(String dateAccepted) {
        this.dateAccepted = dateAccepted;
    }
    public boolean getAccepted(){
        return accepted;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public void setListener(ChatHeadClickedListener listener){
        this.listener = listener;
    }
    public void enterChat(){
            listener.chatClicked(idprofile, username, accepted, sender);
    }
    public static ArrayList<FriendModel> getFriendsFromJSON(String json, int usrId) {
        ArrayList<FriendModel> friends = new ArrayList<FriendModel>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int idFriend = jsonObject.getInt("iduserReceiver");
                String usernameFriend = jsonObject.getString("r_username");
                boolean sender = false;
                if (idFriend == usrId) {
                    sender = true;
                    idFriend = jsonObject.getInt("iduserSender");
                    usernameFriend = jsonObject.getString("s_username");
                }
                String date = jsonObject.getString("date");
                FriendModel friend = (new FriendModel(idFriend, date, usernameFriend, sender));
                friends.add(friend);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return friends;
    }
}
