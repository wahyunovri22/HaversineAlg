package singpaulee.com.haversinealgorythm.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import singpaulee.com.haversinealgorythm.GpsTracker;
import singpaulee.com.haversinealgorythm.Haversine;
import singpaulee.com.haversinealgorythm.MainActivity;
import singpaulee.com.haversinealgorythm.MapsAndLocation;
import singpaulee.com.haversinealgorythm.R;
import singpaulee.com.haversinealgorythm.SharedPrefManager;
import singpaulee.com.haversinealgorythm.fragment.DaftarApotekFragment;
import singpaulee.com.haversinealgorythm.fragment.HomeFragment;
import singpaulee.com.haversinealgorythm.fragment.SearchFragment;
import singpaulee.com.haversinealgorythm.fragment.TentangFragment;

public class MainHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MapsAndLocation mapsAndLocation;
    LocationManager locationManager;
    GpsTracker gpsTracker;

    double latUser;
    double longUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapsAndLocation = new MapsAndLocation(this, locationManager);

        mapsAndLocation.checkLocation();

        permissionLokasi();
        getMyLocation();



        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment())
                .commit();
        getSupportActionBar().setTitle("Home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
            getSupportActionBar().setTitle("Home");
        } else if (id == R.id.nav_search) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new SearchFragment())
                    .commit();
            getSupportActionBar().setTitle("Cari Apotek");
        } else if (id == R.id.nav_maps){
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pharmacy) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new DaftarApotekFragment())
                    .commit();
            getSupportActionBar().setTitle("Daftar Apotek");
        } else if (id == R.id.nav_about) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new TentangFragment())
                    .commit();
            getSupportActionBar().setTitle("About");
        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void getMyLocation() {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
////        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Mendapatkan Lokasi");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        gpsTracker = new GpsTracker(MainHomeActivity.this);
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
//                    Toast.makeText(MainHomeActivity.this, "Get My Location! "+latUser+" "+longUser, Toast.LENGTH_SHORT).show();
//                    SharedPrefManager prefManager = new SharedPrefManager(MainHomeActivity.this);
//                    prefManager.savePrefString(SharedPrefManager.LATITUDE,String.valueOf(latUser));
//                    prefManager.savePrefString(SharedPrefManager.LONGITUDE,String.valueOf(longUser));
////                    Log.d("haversine", "run: "+ Haversine.hitungJarak(LatSaya,LongSaya,LatKF,LongKF));
////                    Log.d("haversine", "run: "+Haversine.hitungJarak(LatSaya,LongSaya,LatSF,LongSF));
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
        gpsTracker = new GpsTracker(MainHomeActivity.this);
        if (gpsTracker.canGetLocation()) {
            latUser = gpsTracker.getLatitude();
            longUser = gpsTracker.getLongitude();
            Toast.makeText(MainHomeActivity.this, "Get My Location! "+latUser+" "+longUser, Toast.LENGTH_SHORT).show();
            SharedPrefManager prefManager = new SharedPrefManager(MainHomeActivity.this);
            prefManager.savePrefString(SharedPrefManager.LATITUDE,String.valueOf(latUser));
            prefManager.savePrefString(SharedPrefManager.LONGITUDE,String.valueOf(longUser));
//            Toast.makeText(getApplicationContext(), "ini lat " + Lat + " ini long " + Long, Toast.LENGTH_SHORT).show();
            MapsInitializer.initialize(MainHomeActivity.this);
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void permissionLokasi() {
        if (ContextCompat.checkSelfPermission(MainHomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainHomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainHomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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
