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
        ConstraintLayout constraintOutside = holder.getOutsideConstraint();
        ConstraintLayout constraintInside = holder.getInsideConstraint();
        ConstraintSet constraintSetOutside = new ConstraintSet();
        constraintSetOutside.clone(constraintOutside);
        ConstraintSet constraintSetInside = new ConstraintSet();
        constraintSetInside.clone(constraintInside);
        if (message.getSender() == usrId){
            constraintSetOutside.connect(R.id.constraintInside, ConstraintSet.RIGHT,
                    R.id.constraintOutside, ConstraintSet.RIGHT,8);
            constraintSetInside.connect(R.id.txtDate, ConstraintSet.RIGHT,
                    R.id.constraintInside, ConstraintSet.RIGHT, 8);
            constraintSetInside.connect(R.id.cardViewMessage, ConstraintSet.RIGHT,
                    R.id.constraintInside, ConstraintSet.RIGHT, 8);
        }
        else {
            constraintSetOutside.connect(R.id.constraintInside, ConstraintSet.LEFT,
                    R.id.constraintOutside, ConstraintSet.LEFT,8);
            constraintSetInside.connect(R.id.txtDate, ConstraintSet.LEFT,
                    R.id.constraintInside, ConstraintSet.LEFT, 8);
            constraintSetInside.connect(R.id.cardViewMessage, ConstraintSet.LEFT,
                    R.id.constraintInside, ConstraintSet.LEFT, 8);
        }
        constraintSetOutside.applyTo(constraintOutside);
        constraintSetInside.applyTo(constraintInside);
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

        public ConstraintLayout getOutsideConstraint() {
            return outsideConstraint;
        }

        public ConstraintLayout getInsideConstraint() {
            return insideConstraint;
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
