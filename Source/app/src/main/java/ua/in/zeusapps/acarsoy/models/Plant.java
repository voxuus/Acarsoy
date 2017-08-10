package ua.in.zeusapps.acarsoy.models;

public class Plant {
    private String _name;
    private PlantType _type;
    private double _power;
    private double _wind;
    private double _temperature;

    public Plant(){

    }

    public Plant(String name, PlantType type, double power, double wind, double temperature) {
        _name = name;
        _type = type;
        _power = power;
        _wind = wind;
        _temperature = temperature;
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
