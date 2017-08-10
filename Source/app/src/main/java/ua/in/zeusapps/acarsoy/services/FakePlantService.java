package ua.in.zeusapps.acarsoy.services;

import java.util.Random;

import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantType;

public class FakePlantService implements IPlantService {

    private final Random _random = new Random();

    @Override
    public Plant getPlant() {
        return new Plant(
                "Random plant #" + _random.nextInt(),
                _random.nextBoolean() ? PlantType.Coal : PlantType.Wind,
                _random.nextDouble() * 40,
                _random.nextDouble() * 25,
                _random.nextDouble() * 40);
    }
}
