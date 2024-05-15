package be.kuleuven.gt.hikinglog.state;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import be.kuleuven.gt.hikinglog.helpers.LastFriendAdded;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;

public class FriendModel {
    boolean lastFriend;
    boolean friendListReady;

    public void setLastFriend(boolean lastFriend) {
        this.lastFriend = lastFriend;
    }

    int idprofile;
    String username;
    String dateAccepted;
    String lastMessage;
    LastFriendAdded nameFilledListener;
    public FriendModel(int idprofile, String dateAccepted) {
        this.idprofile = idprofile;
        this.dateAccepted = dateAccepted;
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

    public void setUsername() {
        this.getUsernameFromId(idprofile);
    }

    public void setDateAccepted(String dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public void addListener(LastFriendAdded listener){
        nameFilledListener = listener;
    }
    public void getUsernameFromId(int idprofile){
        UserState.INSTANCE.getUsernameForID(idprofile, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONObject jsonObject = new JSONArray(stringResponse).getJSONObject(0);
                    username = jsonObject.getString("username");
                    if (lastFriend) {
                        nameFilledListener.NameFilled();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
