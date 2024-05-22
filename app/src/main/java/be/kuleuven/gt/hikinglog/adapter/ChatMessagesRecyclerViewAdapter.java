package be.kuleuven.gt.hikinglog.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case 1: view = inflater.inflate(R.layout.recycler_view_row_message_yours,parent, false);
            break;
            case 0: view = inflater.inflate(R.layout.recycler_view_row_message_to_you, parent, false);
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (messagesDisplayed.get(position).getSender() == usrId) ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel message = messagesDisplayed.get(position);
        holder.getDate().setText(message.getDate());
        holder.getMessage().setText(message.getText());
    }
    @Override
    public int getItemCount() {
        return messagesDisplayed.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView date;
        ConstraintLayout outsideConstraint, insideConstraint;

        public TextView getMessage() {
            return message;
        }

        public TextView getDate() {
            return date;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.txtMessage);
            date = itemView.findViewById(R.id.txtDate);
            outsideConstraint = itemView.findViewById(R.id.constraintOutside);
            insideConstraint = itemView.findViewById(R.id.constraintInside);
        }
    }
}
