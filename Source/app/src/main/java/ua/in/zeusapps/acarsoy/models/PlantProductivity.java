package ua.in.zeusapps.acarsoy.models;

public class PlantProductivity {
    private double _power;
    private double _wind;

    public PlantProductivity(double power, double wind) {
        _power = power;
        _wind = wind;
    }

    public PlantProductivity() {

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
}
