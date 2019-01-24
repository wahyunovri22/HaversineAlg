package singpaulee.com.haversinealgorythm.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import singpaulee.com.haversinealgorythm.GpsTracker;
import singpaulee.com.haversinealgorythm.MainActivity;
import singpaulee.com.haversinealgorythm.R;
import singpaulee.com.haversinealgorythm.SharedPrefManager;
import singpaulee.com.haversinealgorythm.activity.MainHomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    GpsTracker gpsTracker;

    double latUser;
    double longUser;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getMyLocation();
        return view;
    }

//    private void getMyLocation() {
//        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
////        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Mendapatkan Lokasi");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        gpsTracker = new GpsTracker(getActivity());
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
//                    Toast.makeText(getActivity(), "Get My Location! "+latUser+" "+longUser, Toast.LENGTH_SHORT).show();
//                    SharedPrefManager prefManager = new SharedPrefManager(getActivity());
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
        gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            latUser = gpsTracker.getLatitude();
            longUser = gpsTracker.getLongitude();
            Toast.makeText(getActivity(), "ini lat " + latUser + " ini long " + longUser, Toast.LENGTH_SHORT).show();
            MapsInitializer.initialize(getActivity());
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

}
