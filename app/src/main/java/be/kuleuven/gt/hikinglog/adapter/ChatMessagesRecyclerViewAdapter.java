package be.kuleuven.gt.hikinglog.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.state.FriendModel;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.state.MessageModel;

public class ChatMessagesRecyclerViewAdapter extends RecyclerView.Adapter<ChatMessagesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<MessageModel> messagesDisplayed;
    int usrId;

    public ChatMessagesRecyclerViewAdapter(Context context, ArrayList<MessageModel> messages) {
        this.context = context;
        this.messagesDisplayed = messages;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        usrId = sharedPreferences.getInt("usrId", 1);
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
        MessageModel message = messagesDisplayed.get(position);
        holder.getDate().setText(message.getDate());
        holder.getMessage().setText(message.getText());
        ConstraintLayout constraint = holder.getMessageConstraint();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraint);
        if (message.getSender() == usrId){
            constraintSet.connect(R.id.layoutMessage, ConstraintSet.RIGHT,R.id.layoutMessContainer,
                    ConstraintSet.RIGHT,0);
        }
        else {
            constraintSet.connect(R.id.layoutMessage, ConstraintSet.LEFT,R.id.layoutMessContainer,
                    ConstraintSet.LEFT,0);
        }
        constraintSet.applyTo(constraint);
    }

    @Override
    public int getItemCount() {
        return messagesDisplayed.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView date;
        ConstraintLayout messageConstraint;

        public TextView getMessage() {
            return message;
        }

        public TextView getDate() {
            return date;
        }

        public ConstraintLayout getMessageConstraint() {
            return messageConstraint;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.txtMessage);
            date = itemView.findViewById(R.id.txtDate);
            messageConstraint = itemView.findViewById(R.id.layoutMessContainer);
        }

        public void sendMessage() {
            //TODO write method
            this.getAdapterPosition();
        }

    }

}
