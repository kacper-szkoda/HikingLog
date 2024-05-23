package be.kuleuven.gt.hikinglog.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.state.FriendModel;

public class ChatWindowsRecyclerViewAdapter extends RecyclerView.Adapter<ChatWindowsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<FriendModel> friends;

    public ChatWindowsRecyclerViewAdapter(Context context, ArrayList<FriendModel> friends) {
        this.context = context;
        this.friends = friends;
    }

    @NonNull
    @Override
    public ChatWindowsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_row_chat_head, parent,false);
        return new ChatWindowsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatWindowsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.setContext(context);
        setUpMessagePreview(holder, position);

        int profileId = friends.get(position).getIdprofile();
        String username = friends.get(position).getUsername();
        holder.setProfileId(profileId);
        holder.setUsername(username);
        holder.returnTextViewFriend().setText(username);
        holder.setAccepted(friends.get(position).getAccepted());
        holder.setColor();
        holder.setFriend(friends.get(position));
        holder.returnCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getFriend().enterChat();
            }
        });
    }

    private void setUpMessagePreview(@NonNull MyViewHolder holder, int position) {
        String date = friends.get(position).getDateLastMessage();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        String newDate = "";
        if (!date.equals("null")) {
            if (today.equals((date.substring(0, 10)))) {
                newDate = date.substring(10, 16);
            } else {
                newDate = date.substring(0, 16);
            }
        }
        String lastMessage = friends.get(position).getLastMessage();
        if (lastMessage.equals("null")) {
            holder.setLastMessage("", newDate);
        } else {
            holder.setLastMessage(friends.get(position).getLastMessage(), newDate);
        }
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

        public void setProfileId(int profileId) {
            this.profileId = profileId;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public void setColor() {
            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (accepted) {
                        cardViewChat.setCardBackgroundColor(0xFFF1EAB2);
                        txtFriendUsername.setTextColor(0xFFFFFFFF);
                    } else {
                        cardViewChat.setCardBackgroundColor(0xFFA99995);
                        txtFriendUsername.setTextColor(0xFFFFFFFF);
                    }
                }
            });
        }

        public CardView returnCard() {
            return cardViewChat;
        }

        public TextView returnTextViewFriend() {
            return txtFriendUsername;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public FriendModel getFriend() {
            return friend;
        }

        public void setFriend(FriendModel friend) {
            this.friend = friend;
        }

        public void setLastMessage(String message, String time) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (accepted) {
                        txtHeadMessage.setText(message);
                        txtHeadTime.setText(time);
                    } else {
                        txtHeadMessage.setText("Awaiting acceptance");
                        txtHeadTime.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}