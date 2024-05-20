package be.kuleuven.gt.hikinglog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.adapter.ChatMessagesRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.adapter.ChatWindowsRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.MessageModel;
import be.kuleuven.gt.hikinglog.state.UserState;


public class ChatMessagesFragment extends Fragment {
    int profileId;
    String username;
    ArrayList<MessageModel> messages;
    int usrId;
    TextView txtInput;
    Button btnSend;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        usrId = sharedPreferences.getInt("usrId", 1);
        messages = (ArrayList<MessageModel>) getArguments().getSerializable("messages");
        username = getArguments().getString("username");
        profileId = getArguments().getInt("profileId");
        TextView txtFriendName = view.findViewById(R.id.txtChatUser);
        txtFriendName.setText(username);
        btnSend = view.findViewById(R.id.btnSend);
        txtInput = view.findViewById(R.id.txtInputMessage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(view);
            }
        });
        this.setUpMessages(view);
        return view;
    }

    public void setUpMessages(View view){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerMessages);
                ChatMessagesRecyclerViewAdapter adapter = new ChatMessagesRecyclerViewAdapter(getContext(), messages);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);
            }
        });
    }
    public void sendMessage(View view){
        UserState.INSTANCE.sendMessage(txtInput.getText().toString(), profileId, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                txtInput.setText("");
                refreshChat(profileId, view);
            }
        });
    }
    public void refreshChat(int idprofile,  View view){
        ArrayList<MessageModel> messagesNew = new ArrayList<>();
        UserState.INSTANCE.getMessagesPerPair(idprofile, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String text = jsonObject.getString("message");
                        String date = jsonObject.getString("time");
                        int idsender = jsonObject.getInt("idusersender");
                        int idreceiver = jsonObject.getInt("iduserreceiver");
                        MessageModel message = new MessageModel(text, date, idsender, idreceiver);
                        messagesNew.add(message);
                    }
                    messages = messagesNew;
                    setUpMessages(view);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
