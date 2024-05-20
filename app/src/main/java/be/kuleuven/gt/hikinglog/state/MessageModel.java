package be.kuleuven.gt.hikinglog.state;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String text;
    private String date;
    private int sender;
    private int receiver;
    public MessageModel(String text, String date, int sender, int receiver) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public int getReceiver() {
        return receiver;
    }

    public String getDate() {
        return date;
    }

    public int getSender() {
        return sender;
    }
}
