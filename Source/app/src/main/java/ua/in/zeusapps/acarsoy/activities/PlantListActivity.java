package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
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
import ua.in.zeusapps.acarsoy.common.PlantHolder;
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;

public class PlantListActivity extends AppCompatActivity {
    private final IPlantService _plantService = new FakePlantService();

    @BindView(R.id.activity_plant_list_recyclerView)
    RecyclerView _recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        ButterKnife.bind(this);

        List<Plant> plants = _plantService.getPlantList();

        Adapter adapter = new Adapter(plants);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class Holder extends PlantHolder {
        private DecimalFormat _df = new DecimalFormat("0.0");
        @BindView(R.id.template_plant_item_icon_holder)
        FrameLayout _iconHolder;
        @BindView(R.id.template_plant_item_icon)
        ImageView _icon;
        @BindView(R.id.template_plant_item_name)
        TextView _nameTextView;
        @BindView(R.id.template_plant_item_power)
        TextView _powerTextView;
        @BindView(R.id.template_plant_item_wind)
        TextView _windTextView;
        @BindView(R.id.template_plant_item_temperature)
        TextView _temperatureTextView;

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(Plant plant){
            _icon.setBackground(getIcon(plant));
            _iconHolder.setBackgroundColor(getIconBackground(plant));
            _nameTextView.setText(plant.getName());
            _powerTextView.setText(getPower(plant));
            _windTextView.setText(getWind(plant));
            _temperatureTextView.setText(getTemperature(plant));
        }
    }

    private class Adapter extends GenericAdapter<Plant, Holder> {


        protected Adapter(List<Plant> plants) {
            super(plants);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_plant_item, parent, false);

            return new Holder(view);
        }
    }
}
