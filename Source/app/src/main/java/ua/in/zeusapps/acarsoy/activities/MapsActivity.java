package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import ua.in.zeusapps.acarsoy.R;
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
}
