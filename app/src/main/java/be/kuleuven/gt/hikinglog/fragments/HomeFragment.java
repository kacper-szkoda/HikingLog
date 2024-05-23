package be.kuleuven.gt.hikinglog.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.dialogs.SavePathDialog;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;

public class HomeFragment extends Fragment {
    MapFragment mapFragment;
    Button startBtn;
    Dialog dialogSave;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("started", 0);
        editor.apply();

        BaseActivity mapsScreen = (BaseActivity) getActivity();

        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragMap);
        startBtn = view.findViewById(R.id.btnStart);
        startBtn.setText(R.string.btnStartValue);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getStarted()) {
                    startBtn.setText(R.string.btnStopValue);
                    startBtn.setEnabled(false);
                    MapState.INSTANCE.startPath(
                            jsonArray -> MapState.INSTANCE.getLatestPathId(new VolleyCallback() {
                                @Override
                                public void onSuccess(String stringResponse) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(stringResponse);
                                        int pathId = jsonArray.getJSONObject(0).getInt("idpaths");
                                        editor.putInt("latestPathId", pathId);
                                        editor.putInt("started", 1);
                                        editor.apply();
                                        mapFragment.onStartBtn();
                                        startBtn.setEnabled(true);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }));
                } else {
                    mapFragment.onStopBtn();
                    dialogSave.show();
                    startBtn.setText(R.string.btnStartValue);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("started", 0);
                    editor.apply();
                }
            }
        });
        dialogSave = new SavePathDialog(requireActivity(), mapFragment);

        return view;
    }

    public boolean getStarted() {
        sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPref.getInt("started", 0) != 0;
    }
}