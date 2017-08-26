package ua.in.zeusapps.acarsoy.services;

import java.util.List;

import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.api.Plant;

public interface IPlantService {
    Plant getPlant();

    List<Plant> getPlantList();

    List<PlantProductivity> getPlantProductivity(Plant plant);

    List<PlantProductivity> getPlantsProductivityByHours();
}