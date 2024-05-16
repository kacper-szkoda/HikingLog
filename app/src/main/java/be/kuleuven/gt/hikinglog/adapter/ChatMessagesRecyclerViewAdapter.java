package be.kuleuven.gt.hikinglog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.state.FriendModel;

import be.kuleuven.gt.hikinglog.R;

public class ChatMessagesRecyclerViewAdapter extends RecyclerView.Adapter<ChatMessagesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    FriendModel friend;
    ArrayList<String> messagesDisplayed;
    int userId;

    public ChatMessagesRecyclerViewAdapter(Context context, FriendModel friend) {
        this.context = context;
        this.friend = friend;
    }

    @NonNull
    @Override
    public ChatMessagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_message, parent, false);
        return new ChatMessagesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessagesRecyclerViewAdapter.MyViewHolder holder, int position) {
        int profileId = friend.getIdprofile();
        String username = friend.getUsername();
        holder.setProfileId(profileId);
        holder.setUsername(username);
//        holder.btnSend.setText(username);
//        holder.returnButtonSend().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.changeChat();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return messagesDisplayed.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        Button btnSend;
//        TextInputEditText txtInput;
        String username;
        int profileId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //btnSend = itemView.findViewById(R.id.btnViewChat);
            //txtInput = itemView.findViewById(R.id.txtMessageInputField)
        }

        public void changeChat() {
            //TODO write method
            this.getAdapterPosition();
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public void setProfileId(int profileId){this.profileId = profileId;}
//        public Button returnButtonSend(){
//            return btnSend;
//        }
    }
}
