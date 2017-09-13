package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericAdapter;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;
import ua.in.zeusapps.acarsoy.services.api.TurbineRequest;
import ua.in.zeusapps.acarsoy.services.api.TurbineResponse;

public class TurbineDetailsActivity extends BaseNavActivity {

    private PlantResponse.Turbine mTurbine;

    private ConvertUtils mConvertUtils;

    private AcarsoyService mAcarsoyService;

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

    @BindView(R.id.activity_turbine_details_wind_direction)
    ImageView mImgWindDirection;

    @BindView(R.id.activity_turbine_details_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_turbine_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTurbine = new Gson().fromJson(getIntent().getStringExtra(Const.EXTRA_TURBINE_JSON), PlantResponse.Turbine.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initServices();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        mAcarsoyService.getTurbineDetailsAsync(new IAsyncCommand<TurbineRequest, TurbineResponse>() {
            @Override
            public void onComplete(final TurbineResponse data) {

                mRecyclerView.setAdapter(new Adapter(new ArrayList<Pair<String, String>>() {
                    {
                        add(new Pair<>("Power", data.Turbine.Power.toString()));
                        add(new Pair<>("Wind Speed", data.Turbine.WindSpeed.toString()));
                        add(new Pair<>("Temperature", data.Turbine.Temperature.toString()));
                        add(new Pair<>("Energy Total", data.Turbine.EnergyTotal.toString()));
                        add(new Pair<>("Consumption Total", data.Turbine.ConsumptionTotal.toString()));
                        add(new Pair<>("Reactive Power", data.Turbine.ReactivePower.toString()));
                        add(new Pair<>("Current U", data.Turbine.CurrentU.toString()));
                        add(new Pair<>("Current V", data.Turbine.CurrentV.toString()));
                        add(new Pair<>("Current W", data.Turbine.CurrentW.toString()));
                        add(new Pair<>("Voltage U", data.Turbine.VoltageU.toString()));
                        add(new Pair<>("Voltage V", data.Turbine.VoltageV.toString()));
                        add(new Pair<>("Voltage W", data.Turbine.VoltageW.toString()));
                        add(new Pair<>("Wind Direction", data.Turbine.WindDirection.toString()));
                        add(new Pair<>("Frequency", data.Turbine.Frequency.toString()));
                        add(new Pair<>("Voltage L1", data.Turbine.VoltageL1.toString()));
                        add(new Pair<>("Voltage L2", data.Turbine.VoltageL2.toString()));
                        add(new Pair<>("Voltage L3", data.Turbine.VoltageL3.toString()));
                    }
                }));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TurbineDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public TurbineRequest getParameters() {
                return new TurbineRequest(mTurbine.PlantName, mTurbine.Name);
            }
        });

        _imageView.setBackground(getDrawable(imageRes));
        _imageHolder.setBackgroundColor(ContextCompat.getColor(TurbineDetailsActivity.this, backgroundRes));

        _temperatureTextView.setText(mConvertUtils.getTemperature(mTurbine.Temperature));
        _temperatureTextView.setBackgroundColor(ContextCompat.getColor(TurbineDetailsActivity.this, R.color.colorTemperature));

        mTxtTurbineName.setText(mTurbine.Name);
        mTxtPlantName.setText(mTurbine.PlantName);
        mImgWindDirection.setRotation((float) mTurbine.WindDirection);
    }

    @Override
    protected int getCheckedNavId() {
        return 0;
    }

    private void initServices() {
        mConvertUtils = new ConvertUtils(this);
        mAcarsoyService = new AcarsoyService();
    }

    public class Adapter extends GenericAdapter<Pair<String, String>, Holder> {

        protected Adapter(List<Pair<String, String>> items) {
            super(items);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(getLayoutInflater().inflate(R.layout.template_turbine_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            super.onBindViewHolder(holder, position);
        }
    }

    public class Holder extends GenericHolder<Pair<String, String>> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_turbine_details_txt_key)
        TextView mTextViewValue;

        @BindView(R.id.template_turbine_details_txt_value)
        TextView mTextViewUnits;

        public Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(final Pair<String, String> item) {

            mTextViewValue.setText(String.valueOf(item.first));
            mTextViewUnits.setText(String.valueOf(item.second));
        }
    }
}
