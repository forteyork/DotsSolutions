package nuttapon.dots.co.th.dotssolutions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latADouble = 0, lngADouble = 0;
    private double endLatADouble, endLngADouble;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        SetUp Location
        setUpLocation();


        // Create Fragment
        createFragment();

//        Create Toolbar
        createToolbar();
    } // main method

    @Override
    protected void onResume() {
        super.onResume();

        findLatLng();

    }

    private void findLatLng() {

//        For Network
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

//        For GPS
        Location gpsLocation = myFindLocation(locationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        Log.d("6SepV1", "Lat ==>" + latADouble);
        Log.d("6SepV1", "Lng ==>" + lngADouble);

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String providerString) {


        Location location = null;
        if (locationManager.isProviderEnabled(providerString)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(providerString, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(providerString);
        }

        return location;

    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void setUpLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void createFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarMap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(getString(R.string.choose_point));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_action_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity.this, ServiceActivity.class);
                intent.putExtra("Lat", endLatADouble);
                intent.putExtra("Lng", endLngADouble);
                setResult(50, intent);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Create Center Map
        if (latADouble != 0) {

            LatLng centerLatLng = new LatLng(latADouble, lngADouble);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16));
            createMarker(centerLatLng);
        } else {
            findLatLng();
        }

//        Map Controller
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                createMarker(latLng);

            }
        });

        // onMap
    } // Main Class



    private void createMarker(LatLng latLng) {

        endLatADouble = latLng.latitude;
        endLngADouble = latLng.longitude;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);


        mMap.addMarker(markerOptions);
    }
}
