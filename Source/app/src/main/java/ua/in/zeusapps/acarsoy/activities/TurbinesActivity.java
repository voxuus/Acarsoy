package ua.in.zeusapps.acarsoy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;

public class TurbinesActivity extends BaseNavActivity {

    private GoogleMap _map;
    private ClusterManager<PlantResponse.Turbine> _manager;

    private AcarsoyService mAcarsoyService;
    private ConvertUtils mConvertUtils;

    private String mPlantName;

    @BindView(R.id.activity_turbines_txt_total_power)
    TextView mTxtTotalPower;

    @BindView(R.id.activity_turbines_progress)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_turbines;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);
        initServices();
        initMap();
    }

    private void initServices() {
        mAcarsoyService = new AcarsoyService();
        mConvertUtils = new ConvertUtils(this);
    }

    @Override
    protected int getCheckedNavId() {
        return R.id.main_nav_menu_turbines;
    }

    private void initMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_turbines_map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                _map = googleMap;
                _map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                _map.getUiSettings().setZoomControlsEnabled(true);
                _manager = new ClusterManager<>(TurbinesActivity.this, googleMap);
                _manager.setRenderer(new TurbineRenderer(TurbinesActivity.this, googleMap, _manager));
                _manager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PlantResponse.Turbine>() {
                    @Override
                    public boolean onClusterItemClick(PlantResponse.Turbine turbine) {
                        Intent intent = new Intent(TurbinesActivity.this, TurbineDetailsActivity.class);
                        intent.putExtra(Const.EXTRA_TURBINE_JSON, new Gson().toJson(turbine));
                        startActivity(intent);
                        return true;
                    }
                });
                _map.setOnCameraIdleListener(_manager);
                _map.setOnMarkerClickListener(_manager);
                loadTurbinesAsync();
            }
        });
    }

    private void moveCamera(List<PlantResponse.Turbine> data) {
        double lat = 0.0;
        double lng = 0.0;

        for (PlantResponse.Turbine turbine : data) {
            lat += turbine.Latitude;
            lng += turbine.Longitude;
        }

        _map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                new LatLng(lat / data.size(), lng / data.size()), 14f)));
    }

    private void loadTurbinesAsync() {

        mTxtTotalPower.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<PlantResponse>>() {
            @Override
            public void onComplete(List<PlantResponse> data) {

                if (data == null) {
                    Toast.makeText(TurbinesActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();

                    mTxtTotalPower.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(TurbinesActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();

                    mTxtTotalPower.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                _manager.clearItems();
                _manager.addItems(turbines);
                showTotalPower(turbines);

                moveCamera(turbines);

                mTxtTotalPower.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TurbinesActivity.this, error, Toast.LENGTH_SHORT).show();

                mTxtTotalPower.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public Object getParameters() {
                return null;
            }
        });
    }

    private void showTotalPower(List<PlantResponse.Turbine> turbines) {

        double res = 0.0;
        for (PlantResponse.Turbine turbine : turbines) {
            res += turbine.Power;
        }

        mTxtTotalPower.setText(mConvertUtils.getPowerKWatt(res));
    }

    @OnClick(R.id.activity_turbines_layout_show_list)
    public void onClickShowList() {
        Intent intent = new Intent(this, TurbinesListActivity.class);
        intent.putExtra(Const.EXTRA_PLANT_NAME, mPlantName);
        startActivity(intent);
    }

    @OnClick(R.id.activity_turbines_analysis)
    public void OnClickAnalysis() {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra(Const.EXTRA_PLANT_NAME, mPlantName);
        startActivity(intent);
    }

    @Override
    public void refresh() {
        loadTurbinesAsync();
    }

    class MarkerHolder extends GenericHolder<PlantResponse.Turbine> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_marker_turbine_layout)
        View _layout;

        @BindView(R.id.template_plant_marker_image)
        ImageView _image;

        @BindView(R.id.template_plant_marker_name)
        TextView _name;

        @BindView(R.id.template_plant_marker_power)
        TextView _power;

        MarkerHolder(View itemView) {
            super(itemView);

            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(PlantResponse.Turbine turbine) {
            _layout.setBackground(turbine.ErrorCode.equals(0)
                    ? getResources().getDrawable(R.drawable.rounded_corner_green)
                    : getResources().getDrawable(R.drawable.rounded_corner_red)
            );
            _name.setText(turbine.Name);
            _power.setText(mConvertUtils.getPowerKWatt(turbine.Power));
        }
    }

    private class TurbineRenderer extends DefaultClusterRenderer<PlantResponse.Turbine> {

        private final IconGenerator _iconGenerator;
        private final MarkerHolder _holder;

        private TurbineRenderer(Context context, GoogleMap map, ClusterManager<PlantResponse.Turbine> clusterManager) {
            super(context, map, clusterManager);

            View view = getLayoutInflater().inflate(R.layout.template_marker_turbine, null);
            int width = (int) getResources().getDimension(R.dimen.plant_marker_width);
            int height = (int) getResources().getDimension(R.dimen.plant_marker_height);
            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            _iconGenerator = new IconGenerator(context);
            _iconGenerator.setBackground(null);
            _iconGenerator.setContentView(view);
            _holder = new MarkerHolder(view);
        }

        @Override
        protected void onBeforeClusterItemRendered(PlantResponse.Turbine turbine, MarkerOptions markerOptions) {
            _holder.update(turbine);

            Bitmap icon = _iconGenerator.makeIcon();
            icon.setHasAlpha(true);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster<PlantResponse.Turbine> cluster) {
            return cluster.getSize() > 1;
        }
    }
}
