package ua.in.zeusapps.acarsoy.services;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;

public class FakePlantService implements IPlantService {

    private final Random _random = new Random();
    private final static double MAX_POWER = 40d;
    private final static double MAX_WIND = 25d;
    private final static double MAX_TEMPERATURE = 40d;
    private final static double LONGITUDE_CENTER = 27.706573;
    private final static double LATITUDE_CENTER = 41.422757;
    private final static double LONGITUDE_SKEW = 3;
    private final static double LATITUDE_SKEW = 2;

    @Override
    public PlantResponse getPlant() {
        return new PlantResponse(
                getName(),
                getType(),
                getPosition().getLatitude(),
                getPosition().getLongitude(),
                getPower(),
                getWind(),
                getTemperature());
    }

    @Override
    public List<PlantResponse> getPlantList() {
        List<PlantResponse> plants = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            plants.add(getPlant());
        }

        return plants;
    }

    @Override
    public List<PlantProductivity> getPlantProductivity(PlantResponse plant) {
        List<PlantProductivity> prods = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            prods.add(new PlantProductivity(getPower(), getWind()));
        }

        return prods;
    }

    @Override
    public List<PlantProductivity> getPlantsProductivityByHours() {
        List<PlantProductivity> prods = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            prods.add(new PlantProductivity(getPower(), getWind(), hour, Const.ENERGY_TYPE_COAL));
            prods.add(new PlantProductivity(getPower(), getWind(), hour, Const.ENERGY_TYPE_WIND));
        }

        return prods;
    }

    private String getName() {
        return "T #" + Math.abs(_random.nextInt());
    }

    private String getType() {
        return _random.nextBoolean() ? Const.ENERGY_TYPE_COAL : Const.ENERGY_TYPE_WIND;
    }

    private double getPower() {
        return Math.abs(_random.nextDouble()) * MAX_POWER;
    }

    private double getWind() {
        return Math.abs(_random.nextDouble()) * MAX_WIND;
    }

    private double getTemperature() {
        return _random.nextDouble() * MAX_TEMPERATURE;
    }

    private Position getPosition() {
        double latitude = LATITUDE_CENTER + _random.nextDouble() * LATITUDE_SKEW;
        double longitude = LONGITUDE_CENTER + _random.nextDouble() * LONGITUDE_SKEW;

        return new Position(latitude, longitude);
    }

    public static LatLng getCenter() {
        return new LatLng(
                LATITUDE_CENTER + LATITUDE_SKEW / 2,
                LONGITUDE_CENTER + LONGITUDE_SKEW / 2);
    }

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

}
