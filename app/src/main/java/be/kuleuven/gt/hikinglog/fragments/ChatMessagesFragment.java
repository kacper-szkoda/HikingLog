package be.kuleuven.gt.hikinglog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.adapter.ChatMessagesRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.LastMessageVisibleListener;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MessageModel;
import be.kuleuven.gt.hikinglog.state.UserState;


public class ChatMessagesFragment extends Fragment implements LastMessageVisibleListener {
    int profileId;
    String username;
    ArrayList<MessageModel> messages;
    int usrId;
    TextView txtInput;
    TextView btnSend;
    Timer myTimer;
    boolean canRefresh;

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
        btnSend = view.findViewById(R.id.txtSend);
        txtInput = view.findViewById(R.id.txtInputMessage);
        canRefresh = true;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(view);
            }
        });
        this.refreshChat(view);
        this.schedulePolling(view);
        return view;
    }

    public void setUpMessages(View view) {
        ChatMessagesFragment father = this;
        try {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerMessages);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    ChatMessagesRecyclerViewAdapter adapter = new ChatMessagesRecyclerViewAdapter(getContext(), messages, father);
                    recyclerView.setAdapter(adapter);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);

//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//                                int currentLastVisible = layoutManager.findLastVisibleItemPosition();
//                                if (currentLastVisible == -1){
//                                    canRefresh = true;
//                                }
//                                else canRefresh = currentLastVisible == messages.size() - 1;
//                            }
//                        });
                }
            });
        } catch (IllegalStateException ignored) {
        }
    }

    public void sendMessage(View view) {
        if (txtInput.getText().toString().isEmpty()) {
            Toast.makeText(getActivity().getBaseContext(), "Do not send and empty message", Toast.LENGTH_SHORT).show();
        } else {
            UserState.INSTANCE.sendMessage(txtInput.getText().toString(), profileId, new VolleyCallback() {
                @Override
                public void onSuccess(String stringResponse) {
                    txtInput.setText("");
                    refreshChat(view);
                }
            });
        }
    }

    public void refreshChat(View view) {
        ArrayList<MessageModel> messagesNew = new ArrayList<>();
        UserState.INSTANCE.getMessagesPerPair(profileId, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
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

    public void schedulePolling(View view) {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (canRefresh) {
                    refreshChat(view);
                }
            }
        }, 3000, 3000);
    }

    @Override
    public void scrolledPast(boolean scrolledPast) {
        this.canRefresh = !scrolledPast;
    }
}
