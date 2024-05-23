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

import java.text.SimpleDateFormat;
import java.util.Date;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.UserState;

public class AcceptFragment extends Fragment {
    String username;
    int profileId, usrId;
    int sender;
    TextView txtAcceptMessage;
    Button btnDecline, btnAccept;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_request, container, false);

        txtAcceptMessage = view.findViewById(R.id.txtRequestMessage);
        btnAccept = view.findViewById(R.id.btnAccept);
        btnDecline = view.findViewById(R.id.btnDecline);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        usrId = sharedPreferences.getInt("usrId", 1);

        Bundle args = getArguments();
        sender = args.getInt("sender");
        profileId = args.getInt("profileId");
        username = args.getString("username");

        if (sender == 1) {
            setUpReceiver(this);
        } else {
            setUpSender();
        }
        return view;
    }

    public void setUpReceiver(AcceptFragment fragment) {
        txtAcceptMessage.setText(R.string.txtRequestAccept);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.INSTANCE.deleteRequest(profileId, new VolleyCallback() {
                    @Override
                    public void onSuccess(String stringResponse) {
                        ((BaseActivity) fragment.requireActivity()).requestDeclined();
                    }
                });
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                String dateTimeString = formatter.format(new Date());
                UserState.INSTANCE.acceptFriend(profileId, dateTimeString, new VolleyCallback() {
                    @Override
                    public void onSuccess(String stringResponse) {
                        ((BaseActivity) fragment.requireActivity()).changeToAcceptedChat(profileId, username, true);
                    }
                });
            }
        });
    }

    public void setUpSender() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnAccept.setEnabled(false);
                btnDecline.setEnabled(false);
                txtAcceptMessage.setText(R.string.txtRequestWait);
                btnAccept.setVisibility(View.INVISIBLE);
                btnDecline.setVisibility(View.INVISIBLE);
            }
        });
    }
}
