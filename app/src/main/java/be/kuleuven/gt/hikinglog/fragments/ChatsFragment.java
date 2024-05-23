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

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.adapter.ChatWindowsRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.ChatHeadClickedListener;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.FriendModel;
import be.kuleuven.gt.hikinglog.state.UserState;

public class ChatsFragment extends Fragment implements ChatHeadClickedListener {
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
        this.setUpChatHeads();
        return view;
    }

    public void setUpChatHeads() {
        UserState.INSTANCE.findFriendsUsernamesLastMessages(new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                friends = FriendModel.getFriendsFromJSON(stringResponse, usrId);
                setListeners();
                try {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RecyclerView recyclerView = requireView().findViewById(R.id.recyclerFriends);
                                ChatWindowsRecyclerViewAdapter adapter = new ChatWindowsRecyclerViewAdapter(getContext(), friends);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            } catch (IllegalStateException e) {
                                Log.w("ERROR", "exception", e);
                            }
                        }
                    });
                } catch (IllegalStateException e) {
                    Log.w("ERROR", "exception", e);
                }
            }
        });
    }

    @Override
    public void chatClicked(int idprofile, String username, boolean accepted, boolean sender) {
        ((BaseActivity) getActivity()).changeToChat(idprofile, username, accepted, sender);
    }

    public void setListeners() {
        friends.forEach(friendModel -> friendModel.setListener(this));
    }
}