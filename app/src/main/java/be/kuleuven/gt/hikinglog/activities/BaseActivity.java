package be.kuleuven.gt.hikinglog.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.databinding.ActivityBaseBinding;
import be.kuleuven.gt.hikinglog.fragments.AcceptFragment;
import be.kuleuven.gt.hikinglog.fragments.ChatMessagesFragment;
import be.kuleuven.gt.hikinglog.fragments.ChatsFragment;
import be.kuleuven.gt.hikinglog.fragments.HomeFragment;
import be.kuleuven.gt.hikinglog.fragments.ProfileFragment;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.MessageModel;
import be.kuleuven.gt.hikinglog.state.UserState;

public class BaseActivity extends AppCompatActivity {
    ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HikingLog);
        EdgeToEdge.enable(this);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MapState.INSTANCE.setUpMapState(getBaseContext());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home)
                replaceFragment(new HomeFragment());
            else if (item.getItemId() == R.id.profile) {
                goToProfile();
            } else if (item.getItemId() == R.id.chat)
                replaceFragment(new ChatsFragment());
            return true;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    public void goToProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("profileId", sharedPreferences.getInt("usrId", 1));
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        //TODO replace fragment in on success for finding id by username, put that into bundle to be read
        UserState.INSTANCE.findByUsername(query, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                try {
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int profileId = jsonObject.getInt("iduser");
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle args = new Bundle();
                        args.putInt("profileId", profileId);
                        args.putString("username", query);
                        fragment.setArguments(args);
                        replaceFragment(fragment);
                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "No such user found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public int getUsrId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("usrId", 1);
    }

    public void changeToChat(int idprofile, String username, boolean friends, boolean sender) {
        if (friends) {
            changeToAcceptedChat(idprofile, username, false);
        } else {
            changeToAcceptScreen(idprofile, username, sender);
        }
    }

    public void changeToAcceptedChat(int idprofile, String username, boolean fromAccept) {
        ArrayList<MessageModel> messages = new ArrayList<>();
        UserState.INSTANCE.getMessagesPerPair(idprofile, new VolleyCallback() {
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
                        messages.add(message);
                    }
                    Bundle args = new Bundle();
                    args.putSerializable("messages", messages);
                    args.putString("username", username);
                    args.putInt("profileId", idprofile);
                    ChatMessagesFragment fragment = new ChatMessagesFragment();
                    fragment.setArguments(args);
                    if (fromAccept) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);
                        transaction.replace(R.id.fragContainer, fragment);
                        transaction.commit();
                    } else {
                        replaceFragment(fragment);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void changeToAcceptScreen(int profileid, String username, boolean sender) {
        AcceptFragment fragment = new AcceptFragment();
        Bundle args = new Bundle();
        int senderInt = sender ? 1 : 0;
        args.putInt("sender", senderInt);
        args.putInt("profileId", profileid);
        args.putString("username", username);
        fragment.setArguments(args);
        replaceFragment(fragment);
    }

    public void requestDeclined() {
        ChatsFragment fragment = new ChatsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentContainerView fcv = findViewById(R.id.fragContainer);
        super.onBackPressed();
        if (fcv.getFragment().getClass().equals(HomeFragment.class)) {
            binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);
        } else if (fcv.getFragment().getClass().equals(ChatsFragment.class)) {
            binding.bottomNavigationView.getMenu().getItem(2).setChecked(true);
        } else if (fcv.getFragment().getClass().equals(ProfileFragment.class)) {
            binding.bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }

    }
}