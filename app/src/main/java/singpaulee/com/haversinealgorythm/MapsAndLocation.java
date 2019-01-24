package singpaulee.com.haversinealgorythm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ASUS on 23/05/2018.
 */

public class MapsAndLocation {
    Context context;
    LocationManager locationManager;

    public MapsAndLocation(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
    }

    public boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("GPS pada ponsel mati.\nHidupkan untuk menggunakan aplikasi ini ")
                .setPositiveButton("Hidupkan Gps", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                    }
                });
        dialog.show();
    }

    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
//        LatLng latLng = null;
        String lat = "51.1488671";
        String longt = "-133.7530371";

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location;
            location = address.get(0);
//            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            lat = "" + location.getLatitude();
            longt = "" + location.getLongitude();

        } catch (IOException e) {
            Log.d("LangLong", "getLocationFromAddress: " + e);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            lat = "51.1488671";
            longt = "-133.7530371";
        }

        return lat + ";" + longt;
    }

    public String getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            String fullAddress = address + " " + city + " " + country + " " + postalCode + " " + knownName;
            return fullAddress;
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }
}
