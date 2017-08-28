package ua.in.zeusapps.acarsoy.models;

public class PlantProductivity {
    private double _power;
    private double _wind;

    private int _hour;
    private String _plantType;

    public PlantProductivity(double power, double wind) {
        _power = power;
        _wind = wind;
    }

    public PlantProductivity(double power, double wind, int hour, String plantType) {
        _power = power;
        _wind = wind;
        _hour = hour;
        _plantType = plantType;
    }

    public PlantProductivity() {

    }

    public int getHour() {
        return _hour;
    }

    public String getPlantType() {
        return _plantType;
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
