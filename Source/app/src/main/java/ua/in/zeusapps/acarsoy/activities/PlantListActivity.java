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
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericAdapter;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.Plant;

public class PlantListActivity extends AppCompatActivity {

    //TODO remove fake data
    //private final IPlantService _plantService = new FakePlantService();

    private final AcarsoyService mAcarsoyService = new AcarsoyService();

    @BindView(R.id.activity_plant_list_recycler_view)
    RecyclerView _recyclerView;

    private String mPlantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);

        ButterKnife.bind(this);
        initRecyclerView();
        loadDataAsync();
    }

    private void loadDataAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<Plant>>() {
            @Override
            public void onComplete(List<Plant> data) {

                if (data == null) {
                    Toast.makeText(PlantListActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                Plant curPlant = null;

                for (Plant plant : data) {
                    if (plant.Name.equals(mPlantName)) {
                        curPlant = plant;
                        break;
                    }
                }

                if (curPlant == null) {
                    Toast.makeText(PlantListActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                _recyclerView.setAdapter(new Adapter(curPlant.Turbines));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PlantListActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Object getParameters() {
                return null;
            }
        });
    }

    private void initRecyclerView() {
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class Holder extends GenericHolder<Plant.Turbine> {

        ConvertUtils mConvertUtils;

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

        Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(Plant.Turbine plant) {
            _icon.setBackground(mConvertUtils.getIcon(plant.Type));
            _iconHolder.setBackgroundColor(mConvertUtils.getIconBackground(plant.Type));
            _nameTextView.setText(plant.Name);
            _powerTextView.setText(mConvertUtils.getPower(plant.Power));
            _windTextView.setText(mConvertUtils.getWind(plant.WindSpeed));
            _temperatureTextView.setText(mConvertUtils.getTemperature(plant.Temperature));
        }
    }

    class Adapter extends GenericAdapter<Plant.Turbine, Holder> {

        protected Adapter(List<Plant.Turbine> plants) {
            super(plants);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_plant_card_item, parent, false);

            return new Holder(view);
        }
    }
}
