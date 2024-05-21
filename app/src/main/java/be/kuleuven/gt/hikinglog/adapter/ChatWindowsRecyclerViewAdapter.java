package be.kuleuven.gt.hikinglog.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.PathModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.fragments.PathDisplayFragment;

public class ChatWindowsRecyclerViewAdapter extends RecyclerView.Adapter<ChatWindowsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<FriendModel> friends;
    int userId;

    public ChatWindowsRecyclerViewAdapter(Context context, ArrayList<FriendModel> friends) {
        this.context = context;
        this.friends = friends;
    }

    @NonNull
    @Override
    public ChatWindowsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_row_chat_head, parent, false);
        return new ChatWindowsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatWindowsRecyclerViewAdapter.MyViewHolder holder, int position) {
        String date = friends.get(position).getDateLastMessage();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        String newDate = "";
        if (!date.equals("null")) {
            if (today.equals((date.substring(0,10)))){
                newDate = date.substring(10,16);
            }
            else {
                newDate = date.substring(0, 16);
            }
        }
        int profileId = friends.get(position).getIdprofile();
        String username = friends.get(position).getUsername();
        holder.setProfileId(profileId);
        holder.setUsername(username);
        holder.returnTextViewFriend().setText(username);
        holder.setAccepted(friends.get(position).getAccepted());
        holder.setContext(context);
        holder.setColor();
        holder.setFriend(friends.get(position));
        holder.setLastMessage(friends.get(position).getLastMessage(), newDate);
        holder.returnCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getFriend().enterChat();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewChat;
        String username;
        int profileId;
        boolean accepted;
        TextView txtFriendUsername, txtHeadMessage, txtHeadTime;
        Context context;
        FriendModel friend;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewChat = itemView.findViewById(R.id.cardViewChat);
            txtFriendUsername = itemView.findViewById(R.id.txtFriendUsername);
            txtHeadTime = itemView.findViewById(R.id.txtHeadTime);
            txtHeadMessage = itemView.findViewById(R.id.txtHeadMessage);
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public void setProfileId(int profileId){this.profileId = profileId;}
        public void setAccepted(boolean accepted){
            this.accepted = accepted;
        }
        public void setColor(){
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (accepted) {
                        cardViewChat.setCardBackgroundColor(0xFFF1EAB2);
                        txtFriendUsername.setTextColor(0xFFFFFFFF);
                    }
                    else {
                        cardViewChat.setCardBackgroundColor(0xFFA99995);
                        txtFriendUsername.setTextColor(0xFFFFFFFF);
                    }
                }
            });
        }
        public CardView returnCard(){
            return cardViewChat;
        }
        public TextView returnTextViewFriend(){
            return txtFriendUsername;
        }
        public void setContext(Context context){
            this.context = context;
        }
        public void setFriend (FriendModel friend){this.friend = friend;}
        public FriendModel getFriend(){return friend;}

        public void setLastMessage(String message, String time) {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (accepted) {
                        txtHeadMessage.setText(message);
                        txtHeadTime.setText(time);
                    }
                    else {
                        txtHeadMessage.setText("Awaiting acceptance");
                        txtHeadTime.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}