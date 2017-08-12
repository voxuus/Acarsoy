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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.PlantHolder;
import ua.in.zeusapps.acarsoy.models.Plant;
import ua.in.zeusapps.acarsoy.services.FakePlantService;
import ua.in.zeusapps.acarsoy.services.IPlantService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final IPlantService _plantService = new FakePlantService();
    private GoogleMap _map;
    private ClusterManager<Plant> _manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        _map.setOnCameraIdleListener(_manager);
        _map.setOnMarkerClickListener(_manager);

        moveCamera();
        addItems();
    }

    private void moveCamera(){
        _map.moveCamera(
                CameraUpdateFactory
                        .newCameraPosition(
                                CameraPosition.fromLatLngZoom(
                                        FakePlantService.getCenter(),
                                        7f
                                )
                        )
        );
    }

    private void addItems(){
        List<Plant> plants = _plantService.getPlantList();
        _manager.addItems(plants);
    }

    class MarkerHolder extends PlantHolder {
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
        }

        @Override
        public void update(Plant plant) {
            _imageHolder.setBackgroundColor(getIconBackground(plant));
            _image.setBackground(getIcon(plant));
            _temperature.setText(getTemperature(plant));
            _name.setText(plant.getName());
            _power.setText(getPower(plant));
            _wind.setText(getWind(plant));
        }
    }

    class PlantRenderer extends DefaultClusterRenderer<Plant> {
        private final IconGenerator _iconGenerator;
        private final MarkerHolder _holder;

        public PlantRenderer(Context context, GoogleMap map, ClusterManager<Plant> clusterManager) {
            super(context, map, clusterManager);

            _iconGenerator = new IconGenerator(context);
            View view = getLayoutInflater().inflate(R.layout.template_plant_marker, null);
            int width = (int) getResources().getDimension(R.dimen.plant_marker_width);
            int height = (int) getResources().getDimension(R.dimen.plant_marker_height);
            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            _iconGenerator.setContentView(view);
            _holder = new MarkerHolder(view);
        }

        @Override
        protected void onBeforeClusterItemRendered(Plant plant, MarkerOptions markerOptions) {
            _holder.update(plant);

            Bitmap icon = _iconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(plant.getName());
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster<Plant> cluster) {
            return cluster.getSize() > 1;
        }
    }
}
