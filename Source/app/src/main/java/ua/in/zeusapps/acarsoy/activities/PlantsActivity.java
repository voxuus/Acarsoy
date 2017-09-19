package ua.in.zeusapps.acarsoy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.api.PlantResponse;

/**
 * Created by oleg on 17.08.2017.
 */

public class PlantsActivity extends BaseNavActivity {

    private GoogleMap _map;
    private ClusterManager<PlantResponse> _manager;
    private AcarsoyService mAcarsoyService;

    @BindView(R.id.activity_plants_txt_total_power)
    TextView mTxtTotalPower;

    private ConvertUtils mConvertUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plants;
    }

    @Override
    protected int getCheckedNavId() {
        return R.id.main_nav_menu_plants;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initServices();
        initMap();
    }

    private void initServices() {
        mAcarsoyService = new AcarsoyService();
        mConvertUtils = new ConvertUtils(this);
    }

    private void initMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_plants_map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                _map = googleMap;
                _map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                _map.getUiSettings().setZoomControlsEnabled(true);
                _manager = new ClusterManager<>(PlantsActivity.this, googleMap);
                _manager.setRenderer(new PlantRenderer(PlantsActivity.this, googleMap, _manager));
                _manager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PlantResponse>() {
                    @Override
                    public boolean onClusterItemClick(PlantResponse plant) {
                        if (plant.Type.equals(Const.ENERGY_TYPE_COAL)) {
                            return true;
                        }

                        Intent intent = new Intent(PlantsActivity.this, TurbinesActivity.class);
                        intent.putExtra(Const.EXTRA_PLANT_NAME, plant.Name);
                        startActivity(intent);
                        return true;
                    }
                });
                _map.setOnCameraIdleListener(_manager);
                _map.setOnMarkerClickListener(_manager);
                loadPlantsAsync();
            }

        });
    }

    private void loadPlantsAsync() {
        mAcarsoyService.getPlantsAsync(new IAsyncCommand<Object, List<PlantResponse>>() {
            @Override
            public void onComplete(List<PlantResponse> data) {

                if (data == null) {
                    Toast.makeText(PlantsActivity.this, getString(R.string.msg_while_loading_data), Toast.LENGTH_SHORT).show();
                    return;
                }

                _manager.clearItems();
                _manager.addItems(data);
                showTotalPower(data);

                PlantResponse zoomPlant = data.get(0);
                if (zoomPlant == null) {
                    Toast.makeText(PlantsActivity.this, R.string.msg_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                moveCamera(data);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PlantsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public Object getParameters() {
                return null;
            }
        });
    }

    private void moveCamera(List<PlantResponse> data) {
        double lat = 0.0;
        double lng = 0.0;

        for (PlantResponse plant : data) {
            lat += plant.Latitude;
            lng += plant.Longitude;
        }

        _map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                new LatLng(lat / data.size(), lng / data.size()), 5f)));
    }

    private void showTotalPower(List<PlantResponse> data) {

        double res = 0.0;

        for (PlantResponse plant : data) {
            res += plant.Power;
        }

        mTxtTotalPower.setText(mConvertUtils.getPowerMWatt(res));

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.msg_do_you_want_to_exit)
                .setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitApp();
                    }
                })
                .setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }

    @Override
    public void refresh() {
        loadPlantsAsync();
    }

    class MarkerHolder extends GenericHolder<PlantResponse> {

        ConvertUtils mConvertUtils;

        @BindView(R.id.template_plant_marker_image_holder)
        FrameLayout _imageHolder;

        @BindView(R.id.template_plant_marker_image)
        ImageView _image;

        @BindView(R.id.template_plant_marker_temperature)
        TextView _temperature;

        @BindView(R.id.template_plant_marker_name)
        TextView _name;

        @BindView(R.id.template_plant_marker_power)
        TextView _power;

        @BindView(R.id.template_plant_marker_wind)
        TextView _wind;

        MarkerHolder(View itemView) {
            super(itemView);

            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(PlantResponse plant) {
            _imageHolder.setBackgroundColor(mConvertUtils.getIconBackground(plant.Type));
            _image.setBackground(mConvertUtils.getIcon(plant.Type));
            _temperature.setText(mConvertUtils.getTemperature(plant.Temperature));
            _name.setText(plant.Name);
            _power.setText(mConvertUtils.getPowerMWatt(plant.Power));
            _wind.setText(plant.Type.equals(Const.ENERGY_TYPE_WIND) ? mConvertUtils.getWind(plant.Wind) : null);
            _wind.setVisibility(plant.Type.equals(Const.ENERGY_TYPE_WIND) ? View.VISIBLE : View.GONE);
        }
    }

    class PlantRenderer extends DefaultClusterRenderer<PlantResponse> {

        private final IconGenerator _iconGenerator;
        private final MarkerHolder _holder;

        PlantRenderer(Context context, GoogleMap map, ClusterManager<PlantResponse> clusterManager) {
            super(context, map, clusterManager);

            View view = getLayoutInflater().inflate(R.layout.template_marker_plant, null);
            int width = (int) getResources().getDimension(R.dimen.plant_marker_width);
            int height = (int) getResources().getDimension(R.dimen.plant_marker_height);
            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            _iconGenerator = new IconGenerator(context);
            _iconGenerator.setContentView(view);
            _holder = new MarkerHolder(view);
        }

        @Override
        protected void onBeforeClusterItemRendered(PlantResponse plant, MarkerOptions markerOptions) {
            _holder.update(plant);

            Bitmap icon = _iconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(plant.getTitle());
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster<PlantResponse> cluster) {
            return cluster.getSize() > 1;
        }
    }

}
