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
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;

public class PlantListActivity extends AppCompatActivity {

    private final DecimalFormat _df = new DecimalFormat("0.0");
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

    class Holder extends RecyclerView.ViewHolder {
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

            ButterKnife.bind(this, itemView);
        }

        void update(Plant plant){
            int iconRes = 0;
            int colorRes = 0;
            switch (plant.getType()){
                case Coal:
                    iconRes = R.drawable.coal;
                    colorRes = R.color.colorCoal;
                    break;
                case Wind:
                    iconRes = R.drawable.wind;
                    colorRes = R.color.colorWind;
                    break;
            }

            _icon.setBackground(ContextCompat.getDrawable(PlantListActivity.this, iconRes));
            _iconHolder.setBackgroundColor(ContextCompat.getColor(PlantListActivity.this, colorRes));
            _nameTextView.setText(plant.getName());
            _powerTextView.setText(String.format(
                    getString(R.string.mega_watts),
                    _df.format(plant.getPower())
            ));

            _windTextView.setText(String.format(
                    getString(R.string.meters_per_second),
                    _df.format(plant.getWind())
            ));
            _temperatureTextView.setText(String.format(
                    getString(R.string.temperature),
                    _df.format(plant.getTemperature())
            ));
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        private final List<Plant> _items;

        private Adapter(List<Plant> items) {
            _items = items;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_plant_item, parent, false);

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.update(_items.get(position));
        }

        @Override
        public int getItemCount() {
            return _items.size();
        }
    }
}
