package be.kuleuven.gt.hikinglog.state;

import android.text.format.DateUtils;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class MessageModel implements Serializable {
    private String text;
    private String date;
    private int sender;
    private int receiver;
    public MessageModel(String text, String date, int sender, int receiver) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        if (today.equals((date.substring(0,10)))){
            this.date = date.substring(10,16);
        }
        else {
            this.date = date.substring(0, 16);
        }
        this.text = text;
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
