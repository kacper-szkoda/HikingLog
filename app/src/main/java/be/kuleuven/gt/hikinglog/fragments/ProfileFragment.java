package be.kuleuven.gt.hikinglog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.adapter.PathRecyclerViewAdapter;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.PathModel;

public class ProfileFragment extends Fragment {
    ArrayList<PathModel> usrPaths;
    private MapState mapState;
    private int profileId;
    private BaseActivity mapsScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        profileId = sharedPreferences.getInt("usrId", 1);
        usrPaths = new ArrayList<PathModel>();
        mapsScreen = (BaseActivity) getActivity();
        mapState = mapsScreen.returnMapState();
        setUpPathModels();

        return view;
    }

    public void setUpPathModels() {
        mapState.getPathsPerUser(profileId, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(stringResponse);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("pathname");
                        usrPaths.add(new PathModel(name));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                RecyclerView recyclerView = mapsScreen.findViewById(R.id.recyclerPaths);
                PathRecyclerViewAdapter adapter = new PathRecyclerViewAdapter(mapsScreen, usrPaths);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mapsScreen));
            }
        });
    }
}