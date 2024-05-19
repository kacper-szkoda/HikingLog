package be.kuleuven.gt.hikinglog.state;

import java.util.ArrayList;

public class FriendModel {
    ArrayList<String> messagesDisplayed;
    int idprofile;
    String username;
    String dateAccepted;
    String lastMessage;
    boolean accepted;
    public FriendModel(int idprofile, String dateAccepted, String username) {
        this.idprofile = idprofile;
        this.username = username;
        this.dateAccepted = dateAccepted;
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
}
