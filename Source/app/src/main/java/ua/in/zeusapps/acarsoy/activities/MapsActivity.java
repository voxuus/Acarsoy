package ua.in.zeusapps.acarsoy.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.ConvertUtils;
import ua.in.zeusapps.acarsoy.common.GenericHolder;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;
import ua.in.zeusapps.acarsoy.services.api.Plant;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //TODO remove fake date service
    private final IPlantService _plantService = new FakePlantService();

    private GoogleMap _map;
    private ClusterManager<Plant.Turbine> _manager;

    private AcarsoyService mAcarsoyService = new AcarsoyService();
    private String mPlantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mPlantName = getIntent().getStringExtra(Const.EXTRA_PLANT_NAME);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        _map = googleMap;
        _map.getUiSettings().setZoomControlsEnabled(true);
        _manager = new ClusterManager<>(this, _map);
        _manager.setRenderer(new PlantRenderer(this, _map, _manager));
        _manager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Plant.Turbine>() {
            @Override
            public boolean onClusterItemClick(Plant.Turbine plant) {
                Toast.makeText(MapsActivity.this, plant.Name, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        _map.setOnCameraIdleListener(_manager);
        _map.setOnMarkerClickListener(_manager);
        addItemsContinueWithMoveCamera();
    }

    private void moveCamera(double lat, double lng) {
        _map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                new LatLng(lat, lng), 10f)));
    }

    private void addItemsContinueWithMoveCamera() {

        mAcarsoyService.getPlantsAsync(new IAsyncCommand() {
            @Override
            public void onComplete(Object data) {

                List<Plant> plants;

                try {
                    plants = (List<Plant>) data;
                } catch (Exception ex) {
                    Toast.makeText(MapsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (plants == null) {
                    Toast.makeText(MapsActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                Plant curPlant = null;

                for (Plant plant : plants) {
                    if (plant.Name.equals(mPlantName)) {
                        curPlant = plant;
                        break;
                    }
                }

                if (curPlant == null) {
                    Toast.makeText(MapsActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                _manager.addItems(curPlant.Turbines);

                Plant.Turbine zoomTurbine = curPlant.Turbines.get(0);
                if (zoomTurbine == null) {
                    Toast.makeText(MapsActivity.this, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show();
                    return;
                }

                moveCamera(zoomTurbine.Latitude, zoomTurbine.Longitude);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        //TODO remove fake data
     /*   List<Plant> plants = _plantService.getPlantList();
        _manager.addItemsContinueWithMoveCamera(plants);*/
    }

    class MarkerHolder extends GenericHolder<Plant.Turbine> {

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

        public MarkerHolder(View itemView) {
            super(itemView);

            mConvertUtils = new ConvertUtils(getContext());
        }

        @Override
        public void update(Plant.Turbine turbine) {
            _imageHolder.setBackgroundColor(mConvertUtils.getIconBackground(turbine.Type));
            _image.setBackground(mConvertUtils.getIcon(turbine.Type));
            _temperature.setText(mConvertUtils.getTemperature(turbine.Temperature));
            _name.setText(turbine.Name);
            _power.setText(mConvertUtils.getPower(turbine.Power));
            _wind.setText(mConvertUtils.getWind(turbine.WindSpeed));
        }
    }

    class PlantRenderer extends DefaultClusterRenderer<Plant.Turbine> {
        private final IconGenerator _iconGenerator;
        private final MarkerHolder _holder;

        public PlantRenderer(Context context, GoogleMap map, ClusterManager<Plant.Turbine> clusterManager) {
            super(context, map, clusterManager);

            _iconGenerator = new IconGenerator(context);
            View view = getLayoutInflater().inflate(R.layout.template_plant_item, null);
            int width = (int) getResources().getDimension(R.dimen.plant_marker_width);
            int height = (int) getResources().getDimension(R.dimen.plant_marker_height);
            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            _iconGenerator.setContentView(view);
            _holder = new MarkerHolder(view);
        }

        @Override
        protected void onBeforeClusterItemRendered(Plant.Turbine turbine, MarkerOptions markerOptions) {
            _holder.update(turbine);

            Bitmap icon = _iconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(turbine.getTitle());
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster<Plant.Turbine> cluster) {
            return cluster.getSize() > 1;
        }
    }
}
