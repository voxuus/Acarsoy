package ua.in.zeusapps.acarsoy.activities;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericAdapter;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.Plant;


public class TurbineDetailsActivity extends BaseNavActivity {

    private Plant.Turbine mTurbine;

    private AcarsoyService mAcarsoyService;
    private ConvertUtils mConvertUtils;

    @BindView(R.id.activity_plant_details_image_holder)
    FrameLayout _imageHolder;

    @BindView(R.id.activity_plant_details_image)
    ImageView _imageView;

    @BindView(R.id.activity_plant_details_temperature)
    TextView _temperatureTextView;

    @BindView(R.id.activity_turbine_details_txt_turbine_name)
    TextView mTxtTurbineName;

    @BindView(R.id.activity_turbine_details_txt_plant_name)
    TextView mTxtPlantName;

    @BindView(R.id.activity_turbine_details_power)
    TextView mTxtViewPower;

    @BindView(R.id.activity_turbine_details_wind)
    TextView mTxtViewWind;

    @BindView(R.id.activity_turbine_details_wind_direction)
    ImageView mImgWindDirection;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_turbine_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTurbine = new Gson().fromJson(getIntent().getStringExtra(Const.EXTRA_TURBINE_JSON), Plant.Turbine.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initServices();
        showData();
    }

    private void showData() {
        int imageRes = 0;
        int backgroundRes = 0;
        switch (mTurbine.Type) {
            case Const.ENERGY_TYPE_COAL:
                imageRes = R.drawable.coal;
                backgroundRes = R.color.colorCoal;
                break;
            case Const.ENERGY_TYPE_WIND:
                imageRes = R.drawable.wind;
                backgroundRes = R.color.colorWind;
        }

        _imageView.setBackground(getDrawable(imageRes));
        _imageHolder.setBackgroundColor(ContextCompat.getColor(TurbineDetailsActivity.this, backgroundRes));

        _temperatureTextView.setText(mConvertUtils.getTemperature(mTurbine.Temperature));
        _temperatureTextView.setBackgroundColor(ContextCompat.getColor(TurbineDetailsActivity.this, R.color.colorTemperature));

        mTxtTurbineName.setText(mTurbine.Name);
        mTxtPlantName.setText(mTurbine.PlantName);

        mTxtViewPower.setText(mConvertUtils.getPowerMWatt(mTurbine.Power));
        mTxtViewWind.setText(mConvertUtils.getWind(mTurbine.WindSpeed));

        RotateAnimation rotate = new RotateAnimation(0, (float) mTurbine.WindDirection, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(350);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        mImgWindDirection.startAnimation(rotate);
    }

    @Override
    protected int getCheckedNavId() {
        return 0;
    }

    private void initServices() {
        mAcarsoyService = new AcarsoyService();
        mConvertUtils = new ConvertUtils(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

  /*  private void loadDataAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<Plant>>() {
            @Override
            public void onComplete(List<Plant> data) {

                if (data == null) {
                    Toast.makeText(TurbineDetailsActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TurbineDetailsActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();
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
                int color = ContextCompat.getColor(TurbineDetailsActivity.this, backgroundRes);
                _imageHolder.setBackgroundColor(color);

                _temperatureTextView.setText(mConvertUtils.getTemperature(curPlant.Temperature));
                _nameTextView.setText(curPlant.Name);

                _recyclerView.setAdapter(new Adapter(curPlant.Turbines));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TurbineDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Object getParameters() {
                return null;
            }
        });
    }*/

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
        mTxtViewPlantName.setText(plant.Name);

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
                            _df.format(productivity.getPowerMWatt())));
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

        @BindView(R.id.activity_turbine_details_power)
        TextView mTextViewPower;

        @BindView(R.id.template_plant_productivity_details_wind)
        TextView mTextViewWind;

        public Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(Plant.Turbine plant) {
            mTextViewPower.setText(mConvertUtils.getPowerMWatt(plant.Power));
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
