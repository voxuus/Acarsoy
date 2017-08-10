package ua.in.zeusapps.acarsoy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantType;

public class PlantDetailsActivity extends AppCompatActivity {

    @BindView(R.id.activity_plant_details_image)
    ImageView _imageView;
    @BindView(R.id.activity_plant_details_temperature)
    TextView _temperatureTextView;
    @BindView(R.id.activity_plant_details_name)
    TextView _nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        ButterKnife.bind(this);

        Plant plant = randomPlant();
        showData(plant);
    }

    private Plant randomPlant(){
        Random random = new Random();

        return new Plant(
                "Random plant #" + random.nextInt(),
                random.nextBoolean() ? PlantType.Coal : PlantType.Wind,
                random.nextDouble() * 40,
                random.nextDouble() * 25,
                random.nextDouble() * 40);
    }

    private void showData(Plant plant) {
        int imageRes = 0;
        switch (plant.getType()){
            case Coal:
                imageRes = R.drawable.coal;
                break;
            case Wind:
                imageRes = R.drawable.wind;
        }

        _imageView.setBackground(getDrawable(imageRes));

        DecimalFormat df = new DecimalFormat("#.0");
        String temperature = df.format(plant.getTemperature()) + "°C";
        _temperatureTextView.setText(temperature);
        _nameTextView.setText(plant.getName());
    }
}
