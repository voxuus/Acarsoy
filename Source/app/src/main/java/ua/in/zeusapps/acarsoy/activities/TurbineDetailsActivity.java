package ua.in.zeusapps.acarsoy.activities;

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

        mTxtViewPower.setText(mConvertUtils.getPowerKWatt(mTurbine.Power));
        mTxtViewWind.setText(mConvertUtils.getWind(mTurbine.WindSpeed));
        mImgWindDirection.setRotation((float) mTurbine.WindDirection);
    }

    @Override
    protected int getCheckedNavId() {
        return 0;
    }

    private void initServices() {
        mConvertUtils = new ConvertUtils(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
