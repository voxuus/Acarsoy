package ua.in.zeusapps.acarsoy.services;

import java.util.List;

import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;

public interface IPlantService {
    PlantResponse getPlant();

    List<PlantResponse> getPlantList();

    List<PlantProductivity> getPlantProductivity(PlantResponse plant);

    List<PlantProductivity> getPlantsProductivityByHours();
}