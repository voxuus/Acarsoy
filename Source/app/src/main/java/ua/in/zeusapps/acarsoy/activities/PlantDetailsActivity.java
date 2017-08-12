package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.GenericAdapter;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.models.PlantProductivity;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;

public class PlantDetailsActivity extends AppCompatActivity {

    private final IPlantService _plantService = new FakePlantService();
    private final DecimalFormat _df = new DecimalFormat("0.0");

    @BindView(R.id.activity_plant_details_image_holder)
    FrameLayout _imageHolder;
    @BindView(R.id.activity_plant_details_image)
    ImageView _imageView;
    @BindView(R.id.activity_plant_details_temperature)
    TextView _temperatureTextView;
    @BindView(R.id.activity_plant_details_name)
    TextView _nameTextView;
    @BindView(R.id.activity_plant_details_recyclerView)
    RecyclerView _recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        ButterKnife.bind(this);

        Plant plant = _plantService.getPlant();
        List<PlantProductivity> productivities = _plantService.getPlantProductivity(plant);
        showData(plant, productivities);
    }

    private void showData(Plant plant, List<PlantProductivity> productivities) {
        int imageRes = 0;
        int backgroundRes = 0;
        switch (plant.getType()){
            case Coal:
                imageRes = R.drawable.coal;
                backgroundRes = R.color.colorCoal;
                break;
            case Wind:
                imageRes = R.drawable.wind;
                backgroundRes = R.color.colorWind;
        }

        _imageView.setBackground(getDrawable(imageRes));
        int color = ContextCompat.getColor(this, backgroundRes);
        _imageHolder.setBackgroundColor(color);

        String temperature = String.format(
                getString(R.string.temperature),
                _df.format(plant.getTemperature()));
        _temperatureTextView.setText(temperature);
        _nameTextView.setText(plant.getName());

        Adapter adapter = new Adapter(productivities);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class Holder extends GenericHolder<PlantProductivity> {
        @BindView(R.id.template_plant_productivity_details_power)
        TextView powerTextView;
        @BindView(R.id.template_plant_productivity_details_wind)
        TextView windTextView;

        public Holder(View itemView) {
            super(itemView);
        }

        public void update(PlantProductivity productivity) {
            powerTextView.setText(
                    String.format(
                            getString(R.string.power_format),
                            _df.format(productivity.getPower())));
            windTextView.setText(
                    String.format(
                            getString(R.string.wind_format),
                            _df.format(productivity.getWind())));
        }
    }

    private class Adapter extends GenericAdapter<PlantProductivity, Holder> {
        protected Adapter(List<PlantProductivity> plantProductivities) {
            super(plantProductivities);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_plant_productivity_details, parent, false);
            return new Holder(view);
        }
    }
}
