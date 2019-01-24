package singpaulee.com.haversinealgorythm.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import singpaulee.com.haversinealgorythm.GpsTracker;
import singpaulee.com.haversinealgorythm.R;
import singpaulee.com.haversinealgorythm.model.ApotekModel;
import singpaulee.com.haversinealgorythm.model.ResponseModel;
import singpaulee.com.haversinealgorythm.rest.ApiClient;
import singpaulee.com.haversinealgorythm.rest.Config;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    private MapView mapV;
    GpsTracker gpsTracker;
    private double Lat, Long;

    List<ApotekModel> apotekModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        mapV = (MapView) findViewById(R.id.maps);
        mapV.onCreate(savedInstanceState);
        mapV.onResume();

        Awal();
    }

    public void Awal(){
        gpsTracker = new GpsTracker(MapsActivity.this);
        if (gpsTracker.canGetLocation()) {
            Lat = gpsTracker.getLatitude();
            Long = gpsTracker.getLongitude();


        } else {
            gpsTracker.showSettingsAlert();
        }

        mapV.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                MapsInitializer.initialize(MapsActivity.this);

                final LatLng user = new LatLng(Lat, Long);
                mMap = googleMap;

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                //                mMap.addCircle(new CircleOptions()
//                .center(user).radius(Config.RADIOUS_TO_LEVEL));
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // klo di hilangin jadi biasa aja
                mMap.setMyLocationEnabled(true);

                final ProgressDialog loading = ProgressDialog.show(MapsActivity.this, "Loading", "Mencari Lokasi...", false, false);

                ApiClient apiServiceServer  = Config.getRetrofit().create(ApiClient.class);
                Call<ResponseModel> call = apiServiceServer.getData();
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        loading.dismiss();
                        apotekModels = response.body().getApotek();
                        for (int i = 0; i <apotekModels.size(); i++) {
                            String nama = apotekModels.get(i).getNama();
                            String alamat = apotekModels.get(i).getAlamat();
                            Float lat = Float.valueOf(apotekModels.get(i).getLatitude());
                            Float lng = Float.valueOf(apotekModels.get(i).getLongitude());

                            LatLng posisi = new LatLng(lat, lng);

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);

                            googleMap.addMarker(new MarkerOptions()
                                    .position(posisi)
                                    .title(nama)
                                    .snippet(alamat)
                                    .icon(icon));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(MapsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
