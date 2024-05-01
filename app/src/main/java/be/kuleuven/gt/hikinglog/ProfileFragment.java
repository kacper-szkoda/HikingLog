package be.kuleuven.gt.hikinglog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.mapstate.MapState;
import be.kuleuven.gt.hikinglog.mapstate.PathModel;
import be.kuleuven.gt.hikinglog.mapstate.VolleyCallback;

public class ProfileFragment extends Fragment {
    ArrayList<PathModel> usrPaths;
    private MapState mapState;
    private int profileId = 1;
    private maps_screen mapsScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        usrPaths = new ArrayList<PathModel>();
        mapsScreen = (maps_screen) getActivity();
        mapState = mapsScreen.returnMapState();
        setUpPathModels();

        return view;
    }

    public void setUpPathModels(){
        mapState.getPathsPerUser(profileId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++){
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