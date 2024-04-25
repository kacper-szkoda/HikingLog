package be.kuleuven.gt.hikinglog.mapstate;

import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class PathDrawer {
    public static CircleOptions createCircle(double Lat, double Lng){
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(Lat, Lng))
                .radius(10);
        circleOptions.fillColor(Color.BLACK);
        return circleOptions;
    }

    public static PolylineOptions createLine(double Lat1, double Lng1, double Lat2, double Lng2){
        return new PolylineOptions()
                .add(new LatLng(Lat1, Lng1), new LatLng(Lat2, Lng2));
    }
}
