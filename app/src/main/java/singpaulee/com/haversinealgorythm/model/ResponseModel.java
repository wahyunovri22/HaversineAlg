package singpaulee.com.haversinealgorythm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ASUS on 24/05/2018.
 */

public class ResponseModel {
    @SerializedName("kode")
    @Expose
    private Integer kode;
    @SerializedName("apotek")
    @Expose
    private ArrayList<ApotekModel> apotek = null;

    public Integer getKode() {
        return kode;
    }

    public void setKode(Integer kode) {
        this.kode = kode;
    }

    public ArrayList<ApotekModel> getApotek() {
        return apotek;
    }

    public void setApotek(ArrayList<ApotekModel> apotek) {
        this.apotek = apotek;
    }
}
