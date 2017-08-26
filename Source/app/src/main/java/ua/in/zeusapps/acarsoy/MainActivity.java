package ua.in.zeusapps.acarsoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.activities.ChartActivity;
import ua.in.zeusapps.acarsoy.activities.LoginActivity;
import ua.in.zeusapps.acarsoy.activities.MapsActivity;
import ua.in.zeusapps.acarsoy.activities.PlantDetailsActivity;
import ua.in.zeusapps.acarsoy.activities.PlantListActivity;
import ua.in.zeusapps.acarsoy.activities.PlantsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_main_login_screen)
    public void showLogin(){
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.activity_main_plant_details)
    public void showPlantDetails() {
        startActivity(PlantDetailsActivity.class);
    }

    @OnClick(R.id.activity_main_plant_list)
    public void showPlantsList(){
        startActivity(PlantListActivity.class);
    }

    @OnClick(R.id.activity_main_plant_maps)
    public void showMaps(){
        startActivity(MapsActivity.class);
    }

    @OnClick(R.id.activity_main_plant_chart)
    public void showChart(){
        startActivity(ChartActivity.class);
    }


    @OnClick(R.id.activity_plant_types)
    public void showPlantTypes(){
        startActivity(PlantsActivity.class);
    }

    private void startActivity(Class clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
