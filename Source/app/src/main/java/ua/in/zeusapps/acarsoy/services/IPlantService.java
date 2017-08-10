package ua.in.zeusapps.acarsoy.services;

import java.util.List;

import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;

public interface IPlantService {
    Plant getPlant();

    List<PlantProductivity> getPlantProductivity(Plant plant);
}