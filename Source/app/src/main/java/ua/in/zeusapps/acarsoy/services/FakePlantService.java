package ua.in.zeusapps.acarsoy.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.models.PlantType;

public class FakePlantService implements IPlantService {

    private final Random _random = new Random();
    private final static double MAX_POWER  = 40d;
    private final static double MAX_WIND = 25d;
    private final static double MAX_TEMPERATURE = 40d;

    @Override
    public Plant getPlant() {
        return new Plant(
                "Random plant #" + _random.nextInt(),
                _random.nextBoolean() ? PlantType.Coal : PlantType.Wind,
                getPower(),
                getWind(),
                getTemperature());
    }

    @Override
    public List<PlantProductivity> getPlantProductivity(Plant plant) {
        List<PlantProductivity> prods = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            prods.add(new PlantProductivity(getPower(), getWind()));
        }

        return prods;
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
}
