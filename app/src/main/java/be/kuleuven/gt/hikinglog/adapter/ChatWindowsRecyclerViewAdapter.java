package be.kuleuven.gt.hikinglog.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.PathModel;
import java.util.ArrayList;
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
        int profileId = friends.get(position).getIdprofile();
        String username = friends.get(position).getUsername();
        holder.setProfileId(profileId);
        holder.setUsername(username);
        holder.btnViewChat.setText(username);
        holder.btnViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.changeChat();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnViewChat;
        String username;
        int profileId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewChat = itemView.findViewById(R.id.btnViewChat);

        }

        public void changeChat() {
            //TODO write method
            this.getAdapterPosition();
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public void setProfileId(int profileId){this.profileId = profileId;}
        public Button returnButton(){
            return btnViewChat;
        }
    }


}