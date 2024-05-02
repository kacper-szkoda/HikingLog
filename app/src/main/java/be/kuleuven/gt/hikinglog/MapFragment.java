package be.kuleuven.gt.hikinglog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import be.kuleuven.gt.hikinglog.mapstate.MapState;
import be.kuleuven.gt.hikinglog.mapstate.PathDrawer;
import be.kuleuven.gt.hikinglog.mapstate.VolleyCallback;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = maps_screen.class.getSimpleName();
    private final LatLng defaultLocation = new LatLng(50.8749, 4.7078);
    private Location lastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap gMap;
    private ArrayList<LatLng> coords;
    private FragmentActivity parent;
    private Timer myTimer;
    private MapState mapState;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            View view = inflater.inflate(R.layout.fragment_map, container, false);
            SupportMapFragment mapFragment=(SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.google_map_dialog);
            mapFragment.getMapAsync(this);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
            coords = new ArrayList<LatLng>();
            parent = this.getActivity();
            setStarted(false);
            HomeFragment homeFragment = (HomeFragment) getParentFragment();
            mapState = homeFragment.getMapState();
            return view;

        }catch (Exception e)
        {
            Log.e(TAG, "onCreateView", e);
            throw e;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                if (locationPermissionGranted) {
                    Task<Location> locationResult = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);
                    locationResult.addOnCompleteListener(this.getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation != null) {
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                }
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                gMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                            if (getStarted()){
                            coords.add(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
                            saveCoords();
                            if (coords.size() > 1){
                                parent.runOnUiThread(RecordPath);
                            }
                            }
                        }
                    });
                }
            }
        }catch(SecurityException e){
                Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private Runnable RecordPath = new Runnable() {
        public void run() {
            Polyline polyline = gMap.addPolyline(PathDrawer.createLine(coords.get(coords.size()-2), coords.get(coords.size()-1)));
            //This method runs in the same thread as the UI.
            //Do something to the UI thread here
        }
    };

    private void PathMethod(){
        getDeviceLocation();
    }

    public void onStartBtn(){
        setStarted(true);
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                PathMethod();
            }
        }, 5, 10000);
    }

    public void onStopBtn(){
        setStarted(false);
        myTimer.cancel();
    }

    public void saveCoords(){
        mapState.postMap(coords.get(coords.size() - 1), new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {

            }
        });
    }
    public void savePath(String pathname){
        mapState.savePath(pathname, new VolleyCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {

            }
        });
        mapState.renamePath(pathname);
    }

    public boolean getStarted(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPref.getInt("started", 0) != 0;
    }

    public void setStarted(boolean toSet){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (toSet) {
            editor.putInt("started", 1);
        } else {
            editor.putInt("started", 0);
        }
        editor.apply();
    }
}