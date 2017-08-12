package ua.in.zeusapps.acarsoy.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Plant
    implements ClusterItem {
    private String _name;
    private PlantType _type;
    private double _power;
    private double _wind;
    private double _temperature;
    private Position _position;

    public Plant(){

    }

    public Plant(String name, PlantType type, double power, double wind, double temperature) {
        this(name, type, power, wind, temperature, null);
    }

    public Plant(String name, PlantType type, double power, double wind, double temperature, Position position) {
        _name = name;
        _type = type;
        _power = power;
        _wind = wind;
        _temperature = temperature;
        _position = position;
    }

    public LatLng getPosition() {
        return _position.toLatLng();
    }

    @Override
    public String getTitle() {
        return _name;
    }

    @Override
    public String getSnippet() {
        return _name;
    }

    public void setPosition(Position position) {
        _position = position;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public PlantType getType() {
        return _type;
    }

    public void setType(PlantType type) {
        _type = type;
    }

    public double getPower() {
        return _power;
    }

    public void setPower(double power) {
        _power = power;
    }

    public double getWind() {
        return _wind;
    }

    public void setWind(double wind) {
        _wind = wind;
    }

    public double getTemperature() {
        return _temperature;
    }

    public void setTemperature(double temperature) {
        _temperature = temperature;
    }
}
