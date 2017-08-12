package ua.in.zeusapps.acarsoy.services;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.models.PlantType;
import ua.in.zeusapps.acarsoy.models.Position;

public class FakePlantService implements IPlantService {

    private final Random _random = new Random();
    private final static double MAX_POWER  = 40d;
    private final static double MAX_WIND = 25d;
    private final static double MAX_TEMPERATURE = 40d;
    private final static double LONGITUDE_CENTER = 27.706573;
    private final static double LATITUDE_CENTER = 41.422757;
    private final static double LONGITUDE_SKEW = 3;
    private final static double LATITUDE_SKEW = 2;
    @Override
    public Plant getPlant() {
        return new Plant(
                getName(),
                getType(),
                getPower(),
                getWind(),
                getTemperature(),
                getPosition());
    }

    @Override
    public List<Plant> getPlantList() {
        List<Plant> plants = new ArrayList<>();

        for (int i = 0; i < 20; i++){
            plants.add(getPlant());
        }

        return plants;
    }

    @Override
    public List<PlantProductivity> getPlantProductivity(Plant plant) {
        List<PlantProductivity> prods = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            prods.add(new PlantProductivity(getPower(), getWind()));
        }

        return prods;
    }

    private String getName(){
        return "T #" + Math.abs(_random.nextInt());
    }

    private PlantType getType(){
        return _random.nextBoolean() ? PlantType.Coal : PlantType.Wind;
    }

    private double getPower(){
        return Math.abs(_random.nextDouble()) * MAX_POWER;
    }

    private double getWind(){
        return Math.abs(_random.nextDouble()) * MAX_WIND;
    }

    private double getTemperature(){
        return _random.nextDouble() * MAX_TEMPERATURE;
    }

    private Position getPosition(){
        double latitude = LATITUDE_CENTER + _random.nextDouble() * LATITUDE_SKEW;
        double longitude = LONGITUDE_CENTER + _random.nextDouble() * LONGITUDE_SKEW;

        return new Position(latitude, longitude);
    }

    public static LatLng getCenter(){
        return new LatLng(
                LATITUDE_CENTER + LATITUDE_SKEW / 2,
                LONGITUDE_CENTER + LONGITUDE_SKEW / 2);
    }
}
