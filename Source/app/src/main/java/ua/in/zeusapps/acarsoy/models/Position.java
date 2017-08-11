package ua.in.zeusapps.acarsoy.models;

import com.google.android.gms.maps.model.LatLng;

public class Position {
    private double _longitude;
    private double _latitude;

    public Position(double latitude, double longitude) {
        _longitude = longitude;
        _latitude = latitude;
    }

    public double getLongitude() {
        return _longitude;
    }

    public double getLatitude() {
        return _latitude;
    }

    public LatLng toLatLng(){
        return new LatLng(_latitude, _longitude);
    }
}
