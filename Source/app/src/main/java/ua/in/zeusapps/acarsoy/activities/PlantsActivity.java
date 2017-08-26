package ua.in.zeusapps.acarsoy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

/**
 * Created by oleg on 17.08.2017.
 */

public class PlantsActivity extends FragmentActivity {

    @BindView(R.id.activity_plants_recycler_view)
    RecyclerView mRecyclerView;

    private AcarsoyService mAcarsoyService = new AcarsoyService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        ButterKnife.bind(this);

        initRecyclerView();
        loadDataAsync();
    }

    private void loadDataAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand() {
            @Override
            public void onComplete(Object data) {
                List<Plant> plants = (List<Plant>) data;
                mRecyclerView.setAdapter(new Adapter(plants));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PlantsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Adapter(new ArrayList<Plant>()));
    }

    public class Adapter extends GenericAdapter<Plant, Holder> {

        protected Adapter(List<Plant> plants) {
            super(plants);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(getLayoutInflater().inflate(R.layout.template_plant_item, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            super.onBindViewHolder(holder, position);
        }
    }

    public class Holder extends GenericHolder<Plant> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_plant_marker_name)
        TextView mTextViewName;

        @BindView(R.id.template_plant_marker_power)
        TextView mTextViewPower;

        @BindView(R.id.template_plant_marker_temperature)
        TextView mTextViewTemperature;

        @BindView(R.id.template_plant_marker_wind)
        TextView mTextViewWind;

        @BindView(R.id.template_plant_marker_image_holder)
        FrameLayout mImageHolder;

        @BindView(R.id.template_plant_marker_image)
        ImageView mImage;

        public Holder(View itemView) {
            super(itemView);
            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(final Plant plant) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO use array string in resources
                    final CharSequence[] items = {"Map", "List", "Details"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlantsActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    Intent intent = new Intent(PlantsActivity.this, MapsActivity.class);
                                    intent.putExtra(Const.EXTRA_PLANT_NAME, plant.Name);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(PlantsActivity.this, PlantListActivity.class);
                                    intent.putExtra(Const.EXTRA_PLANT_NAME, plant.Name);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(PlantsActivity.this, PlantDetailsActivity.class);
                                    intent.putExtra(Const.EXTRA_PLANT_NAME, plant.Name);
                                    startActivity(intent);
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            mImageHolder.setBackgroundColor(mConvertUtils.getIconBackground(plant.Type));
            mImage.setBackground(mConvertUtils.getIcon(plant.Type));
            mTextViewName.setText(plant.Name);
            mTextViewPower.setText(mConvertUtils.getPower(plant.Power));
            mTextViewTemperature.setText(mConvertUtils.getTemperature(plant.Temperature));
            mTextViewWind.setText(mConvertUtils.getWind(plant.Wind));
        }
    }
}
