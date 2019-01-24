package singpaulee.com.haversinealgorythm.rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import singpaulee.com.haversinealgorythm.model.ApotekModel;
import singpaulee.com.haversinealgorythm.model.ResponseModel;

/**
 * Created by ASUS on 24/05/2018.
 */

public interface ApiClient {

    @GET("getDataDua.php")
//    Call<ArrayList<ApotekModel>> getData();
    Call<ResponseModel> getData();
}
