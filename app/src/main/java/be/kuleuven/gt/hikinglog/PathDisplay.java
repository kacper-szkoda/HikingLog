package be.kuleuven.gt.hikinglog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.hikinglog.mapstate.MapState;
import be.kuleuven.gt.hikinglog.mapstate.PathDrawer;
import be.kuleuven.gt.hikinglog.mapstate.VolleyCallback;

public class PathDisplay extends Fragment implements OnMapReadyCallback {
    private String pathName;
    private ArrayList<LatLng> coords;

    private GoogleMap gMap;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.fragment_path_display, container, false);
            SupportMapFragment mapFragment=(SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.google_map_dialog);
            mapFragment.getMapAsync(this);
            coords = new ArrayList<LatLng>();
            return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        recoverPath();
    }

    public void recoverPath(){
        maps_screen mapsScreen = (maps_screen) getActivity();
        MapState mapState = mapsScreen.returnMapState();
        mapState.recoverMap(mapsScreen.getUsrId(), pathName, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++){
                    try {
                        int j = i;
                        JSONObject object = jsonArray.getJSONObject(i);
                        String string = object.getString("coordinate");
                        string = string.replace('_', '.');
                        String[] parts = string.split(",");
                        coords.add(new LatLng(Double.valueOf(parts[0]), Double.valueOf(parts[1])));
                        if (i > 1){
                            mapsScreen.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        gMap.addPolyline(PathDrawer.createLine(coords.get(j - 1), coords.get(j)));
                            }
                        });
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(coords.get(coords.size()-1).latitude, coords.get(coords.size()-1).longitude), 15));
            }
        });
    }

    public void setPathName(String pathName){
        this.pathName = pathName;
    }
}