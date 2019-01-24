package singpaulee.com.haversinealgorythm;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    MapsAndLocation mapsAndLocation;
    LocationManager locationManager;
    GpsTracker gpsTracker;

    double latUser;
    double longUser;

    static final double LatSaya = -6.988561;
    static final double LongSaya = 110.403785;

    static final double LatVG = -6.9887053;
    static final double LongVG = 110.4047024;
    static final double LatKF = -6.985324;
    static final double LongKF = 110.418763;
    static final double LatSF = -6.9875573;
    static final double LongSF = 110.4047589;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapsAndLocation = new MapsAndLocation(this, locationManager);

        mapsAndLocation.checkLocation();

        getMyLocation();
    }

//    private void getMyLocation() {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
////        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Mendapatkan Lokasi");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        gpsTracker = new GpsTracker(MainActivity.this);
//
//        // TODO Cek Lokasi
//
//        if (gpsTracker.canGetLocation()) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pDialog.dismiss();
//                    latUser = gpsTracker.getLatitude();
//                    longUser = gpsTracker.getLongitude();
//                    Toast.makeText(MainActivity.this, "Get My Location! "+latUser+" "+longUser, Toast.LENGTH_SHORT).show();
//                    Log.d("haversine", "run: "+Haversine.hitungJarak(LatSaya,LongSaya,LatKF,LongKF));
//                    Log.d("haversine", "run: "+Haversine.hitungJarak(LatSaya,LongSaya,LatSF,LongSF));
//                }
//            },2000);
//
////            String address = mapsAndLocation.getAddress(latitudeCollector, longitudeCollector);
////            dpTvLokasiSekarang.setText("" + address);
//        } else {
//            pDialog.dismiss();
//            gpsTracker.showSettingsAlert();
//        }
//    }
private void getMyLocation() {
    gpsTracker = new GpsTracker(MainActivity.this);
    if (gpsTracker.canGetLocation()) {
        latUser = gpsTracker.getLatitude();
        longUser = gpsTracker.getLongitude();
            Toast.makeText(getApplicationContext(), "ini lat " + latUser + " ini long " + longUser, Toast.LENGTH_SHORT).show();
        MapsInitializer.initialize(MainActivity.this);
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    } else {
        gpsTracker.showSettingsAlert();
    }
}
    @Override
    protected void onRestart() {
        super.onRestart();
        mapsAndLocation.checkLocation();
        if (mapsAndLocation.isLocationEnabled()){
            getMyLocation();
        }
    }
}
