package be.kuleuven.gt.hikinglog.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.PathDrawer;

public class PathDisplayFragment extends Fragment implements OnMapReadyCallback {
    private String pathName;
    private ArrayList<LatLng> coords;
    private int profileId;
    private GoogleMap gMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_path_display, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentByTag("mapFragProf");
        mapFragment.getMapAsync(this);
        coords = new ArrayList<LatLng>();
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        recoverPath();
    }

    public void recoverPath() {
        BaseActivity mapsScreen = (BaseActivity) getActivity();
        MapState mapState = mapsScreen.returnMapState();
        mapState.recoverMap(profileId, pathName, new VolleyCallback() {
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
                        int j = i;
                        JSONObject object = jsonArray.getJSONObject(i);
                        String string = object.getString("coordinate");
                        string = string.replace('_', '.');
                        String[] parts = string.split(",");
                        coords.add(new LatLng(Double.valueOf(parts[0]), Double.valueOf(parts[1])));
                        if (i > 1) {
                            mapsScreen.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        gMap.addPolyline(PathDrawer.createLine(coords.get(j - 1), coords.get(j)));
                                    } catch (ArrayIndexOutOfBoundsException e){
                                        Log.w("EXCEPTION", "Path was empty", e);
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.w("EXCEPTION", "JSON was empty", e);
                    }
                }
                if (coords.size() != 0){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(coords.get(coords.size() - 1).latitude, coords.get(coords.size() - 1).longitude), 15));
                }
            }
        });
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
    public void setProfileId(int profileId){this.profileId =profileId;}

    public FragmentManager getFragMan() {
        return getActivity().getSupportFragmentManager();
    }
}