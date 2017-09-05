package ua.in.zeusapps.acarsoy.activities;

import android.content.Intent;
import android.os.Bundle;
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

public class TurbinesListActivity extends BaseNavActivity {

    private final AcarsoyService mAcarsoyService = new AcarsoyService();

    @BindView(R.id.activity_plant_list_recycler_view)
    RecyclerView _recyclerView;

    private String mPlantName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_turbines_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);

        initRecyclerView();
        loadDataAsync();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected int getCheckedNavId() {
        return R.id.main_nav_menu_turbines_list;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadDataAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<PlantResponse>>() {
            @Override
            public void onComplete(List<PlantResponse> data) {

                if (data == null) {
                    Toast.makeText(TurbinesListActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                PlantResponse curPlant = null;

                for (PlantResponse plant : data) {
                    if (plant.Name.equals(mPlantName)) {
                        curPlant = plant;
                        break;
                    }
                }

                List<PlantResponse.Turbine> turbines = new ArrayList<>();

                if (curPlant == null) {
                    for (PlantResponse plant : data) {
                        if (plant.Turbines != null) {
                            turbines.addAll(plant.Turbines);
                        }
                    }
                } else {
                    if (curPlant.Turbines != null) {
                        turbines.addAll(curPlant.Turbines);
                    }
                }

                if (turbines.size() == 0) {
                    Toast.makeText(TurbinesListActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                _recyclerView.setAdapter(new Adapter(turbines));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TurbinesListActivity.this, error, Toast.LENGTH_SHORT).show();
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

    class Holder extends GenericHolder<PlantResponse.Turbine> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_plant_item_icon_holder)
        FrameLayout _iconHolder;

        @BindView(R.id.template_plant_item_icon)
        ImageView _icon;

        @BindView(R.id.template_plant_card_txt_plant)
        TextView mTxtViewPlantName;

        @BindView(R.id.template_plant_card_txt_turbine)
        TextView mTxtViewTurbineName;

        @BindView(R.id.template_plant_item_power)
        TextView _powerTextView;

        @BindView(R.id.template_plant_item_wind)
        TextView _windTextView;

        @BindView(R.id.template_plant_item_temperature)
        TextView _temperatureTextView;

        @BindView(R.id.template_plant_card_item_card)
        View _mainLayout;

        Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(final PlantResponse.Turbine turbine) {
            _icon.setBackground(mConvertUtils.getIcon(turbine.Type));
            _iconHolder.setBackgroundColor(mConvertUtils.getIconBackground(turbine.Type));
            mTxtViewPlantName.setText(turbine.PlantName);
            mTxtViewTurbineName.setText(turbine.Name);
            _powerTextView.setText(mConvertUtils.getPowerKWatt(turbine.Power));
            _windTextView.setText(mConvertUtils.getWind(turbine.WindSpeed));
            _temperatureTextView.setText(mConvertUtils.getTemperature(turbine.Temperature));

            _mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TurbinesListActivity.this, TurbineDetailsActivity.class);
                    intent.putExtra(Const.EXTRA_TURBINE_JSON, new Gson().toJson(turbine));
                    startActivity(intent);
                }
            });
        }
    }

    class Adapter extends GenericAdapter<PlantResponse.Turbine, Holder> {

        protected Adapter(List<PlantResponse.Turbine> plants) {
            super(plants);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.template_turbine_card_item, parent, false);

            return new Holder(view);
        }
    }
}
