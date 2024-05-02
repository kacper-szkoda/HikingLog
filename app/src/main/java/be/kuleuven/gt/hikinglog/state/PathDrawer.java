package be.kuleuven.gt.hikinglog.state;

import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class PathDrawer {
    public static CircleOptions createCircle(double Lat, double Lng) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(Lat, Lng))
                .radius(10);
        circleOptions.fillColor(Color.BLACK);
        return circleOptions;
    }

    public static PolylineOptions createLine(LatLng latLng1, LatLng latLng2) {
        return new PolylineOptions()
                .add(latLng1, latLng2);
    }
}
