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


public class PlantDetailsActivity extends AppCompatActivity {

    //TODO delete fake service
    //private final IPlantService _plantService = new FakePlantService();
    //private final DecimalFormat _df = new DecimalFormat("0.0");
    private String mPlantName;

    private AcarsoyService mAcarsoyService = new AcarsoyService();

    private ConvertUtils mConvertUtils = new ConvertUtils(PlantDetailsActivity.this);

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
        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);

        ButterKnife.bind(this);

        initRecyclerView();
        loadDataAsync();
    }

    private void initRecyclerView() {
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadDataAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<Plant>>() {
            @Override
            public void onComplete(List<Plant> data) {

                if (data == null) {
                    Toast.makeText(PlantDetailsActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PlantDetailsActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                int imageRes = 0;
                int backgroundRes = 0;
                switch (curPlant.Type) {
                    case Const.ENERGY_TYPE_COAL:
                        imageRes = R.drawable.coal;
                        backgroundRes = R.color.colorCoal;
                        break;
                    case Const.ENERGY_TYPE_WIND:
                        imageRes = R.drawable.wind;
                        backgroundRes = R.color.colorWind;
                }

                _imageView.setBackground(getDrawable(imageRes));
                int color = ContextCompat.getColor(PlantDetailsActivity.this, backgroundRes);
                _imageHolder.setBackgroundColor(color);

                _temperatureTextView.setText(mConvertUtils.getTemperature(curPlant.Temperature));
                _nameTextView.setText(curPlant.Name);

                _recyclerView.setAdapter(new Adapter(curPlant.Turbines));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PlantDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Object getParameters() {
                return null;
            }
        });
    }

   /* private void showData(Plant plant, List<PlantProductivity> productivities) {
        int imageRes = 0;
        int backgroundRes = 0;
        switch (plant.Type) {
            case Const.ENERGY_TYPE_COAL:
                imageRes = R.drawable.coal;
                backgroundRes = R.color.colorCoal;
                break;
            case Const.ENERGY_TYPE_WIND:
                imageRes = R.drawable.wind;
                backgroundRes = R.color.colorWind;
        }

        _imageView.setBackground(getDrawable(imageRes));
        int color = ContextCompat.getColor(this, backgroundRes);
        _imageHolder.setBackgroundColor(color);

        String temperature = String.format(
                getString(R.string.temperature),
                _df.format(plant.Temperature));
        _temperatureTextView.setText(temperature);
        _nameTextView.setText(plant.Name);

        Adapter adapter = new Adapter(productivities);
        _recyclerView.setAdapter(adapter);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }*/

   /* class Holder extends GenericHolder<PlantProductivity> {
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
    }*/


    class Holder extends GenericHolder<Plant.Turbine> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_plant_productivity_details_power)
        TextView mTextViewPower;

        @BindView(R.id.template_plant_productivity_details_wind)
        TextView mTextViewWind;

        public Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(Plant.Turbine plant) {
            mTextViewPower.setText(mConvertUtils.getPower(plant.Power));
            mTextViewWind.setText(mConvertUtils.getWind(plant.WindSpeed));
        }
    }

    class Adapter extends GenericAdapter<Plant.Turbine, Holder> {

        Adapter(List<Plant.Turbine> plants) {
            super(plants);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_plant_productivity_details, parent, false);

            return new Holder(view);
        }
    }
}
