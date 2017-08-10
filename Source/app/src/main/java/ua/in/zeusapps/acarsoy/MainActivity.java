package ua.in.zeusapps.acarsoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.activities.LoginActivity;
import ua.in.zeusapps.acarsoy.activities.PlantDetailsActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_main_login_screen)
    public void showLogin(){
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    @OnClick(R.id.activity_main_plant_details)
    public void showPlantDetails() {
        Intent intent = new Intent(this, PlantDetailsActivity.class);

        startActivity(intent);
    }
}
