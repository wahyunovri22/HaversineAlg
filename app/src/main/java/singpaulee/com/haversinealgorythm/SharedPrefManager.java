package singpaulee.com.haversinealgorythm;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.PublicKey;

/**
 * Created by ASUS on 24/05/2018.
 */

public class SharedPrefManager {
    public static final String SP = "haversine";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sp = context.getSharedPreferences(SP,Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void savePrefString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public void savePrefBoolean(String key, Boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }


//    public void savePrefDouble(String key, Float value){
//        editor.putFloat(key,value);
//        editor.commit();
//    }

    public void clearAll(){
        editor.clear();
        editor.commit();
    }

    public String getLATITUDE() {
        return sp.getString(LATITUDE,"");
    }

    public String getLONGITUDE() {
        return sp.getString(LONGITUDE,"");
    }
}
