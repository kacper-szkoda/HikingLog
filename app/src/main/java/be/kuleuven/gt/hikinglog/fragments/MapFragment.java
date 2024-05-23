package be.kuleuven.gt.hikinglog.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import be.kuleuven.gt.hikinglog.R;
import be.kuleuven.gt.hikinglog.activities.BaseActivity;
import be.kuleuven.gt.hikinglog.helpers.VolleyCallback;
import be.kuleuven.gt.hikinglog.state.MapState;
import be.kuleuven.gt.hikinglog.state.PathDrawer;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(50.8749, 4.7078);
    SharedPreferences sharedPreferences;
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private GoogleMap gMap;
    private ArrayList<LatLng> coords;
    private final Runnable RecordPath = new Runnable() {
        public void run() {
            Polyline polyline = gMap.addPolyline(PathDrawer.createLine(coords.get(coords.size() - 2),
                    coords.get(coords.size() - 1)));
        }
    };
    private FragmentActivity parent;
    private Timer myTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentByTag("mapFragment");
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        coords = new ArrayList<LatLng>();
        parent = this.getActivity();
        setStarted(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getLocationPermission() {
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
        try {
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
                        if (getStarted()) {
                            coords.add(new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude()));
                            saveCoords();
                            if (coords.size() > 1) {
                                parent.runOnUiThread(RecordPath);
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    public void onStartBtn() {
        setStarted(true);
        getDeviceLocation();
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getDeviceLocation();
            }
        }, 10000, 10000);
    }

    public void onStopBtn() {
        setStarted(false);
        myTimer.cancel();
    }

    public void saveCoords() {
            MapState.INSTANCE.postMap(coords.get(coords.size() - 1), new VolleyCallback() {
                @Override
                public void onSuccess(String stringResponse) {

                }
            });
    }

    public void savePath(String pathname) {
        MapState.INSTANCE.renamePath(pathname, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {

            }
        });
        MapState.INSTANCE.renamePathEntries(pathname, new VolleyCallback() {
            @Override
            public void onSuccess(String stringResponse) {

            }
        });
    }

    public boolean getStarted() {
        return sharedPreferences.getInt("started", 0) != 0;
    }

    public void setStarted(boolean toSet) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (toSet) {
            editor.putInt("started", 1);
        } else {
            editor.putInt("started", 0);
        }
        editor.apply();
    }
}