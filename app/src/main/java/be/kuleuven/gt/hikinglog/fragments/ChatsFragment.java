package be.kuleuven.gt.hikinglog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Objects;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.adapter.ChatWindowsRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.adapter.PathRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.LastFriendAdded;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.UserState;

public class ChatsFragment extends Fragment implements LastFriendAdded {
    ArrayList<FriendModel> friends;
    int usrId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        usrId = sharedPreferences.getInt("usrId", 1);
        friends = new ArrayList<FriendModel>();
        this.setUpChatHeads(this);
        return view;
    }

    public void setUpChatHeads(LastFriendAdded listener) {
        UserState.INSTANCE.findFriends(new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                friends = getFriends(stringResponse);
                friends.forEach(friendModel -> friendModel.addListener(listener));
                friends.forEach(FriendModel::setUsername);
            }
        });
    }

    public ArrayList<FriendModel> getFriends(String json) {
        ArrayList<FriendModel> friends = new ArrayList<FriendModel>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int idfriend = jsonObject.getInt("iduserReceiver");
                if (idfriend == usrId) {
                    idfriend = jsonObject.getInt("iduserSender");
                }
                String date = jsonObject.getString("date");
                FriendModel friend = (new FriendModel(idfriend, date));
                friends.add(friend);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return friends;
    }



    @Override
    public void NameFilled() {
        Log.w("myApp", String.valueOf(getActivity()));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (friends.stream()
                        .map(FriendModel::getUsername)
                        .noneMatch(Objects::isNull))
                {
                    RecyclerView recyclerView = getView().findViewById(R.id.recyclerFriends);
                    ChatWindowsRecyclerViewAdapter adapter = new ChatWindowsRecyclerViewAdapter(getContext(), friends);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });
    }
}