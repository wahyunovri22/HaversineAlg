package singpaulee.com.haversinealgorythm.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by ASUS on 24/05/2018.
 */

public class ApotekModel implements Comparable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("telepon")
    @Expose
    private String telepon;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    private Double jarak;

    public ApotekModel(String id, String nama, String alamat, String telepon, String latitude, String longitude, String gambar, Double jarak) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gambar = gambar;
        this.jarak = jarak;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Double getJarak() {
        return jarak;
    }

    public void setJarak(Double jarak) {
        this.jarak = jarak;
    }

//    @Override
//    public String toString() {
//        return "[ rollno=" + rollno + ", name=" + studentname + ", age=" + studentage + "]";
//    }

    public static Comparator<ApotekModel> sortJarak = new Comparator<ApotekModel>() {
        @Override
        public int compare(ApotekModel apotekModel, ApotekModel t1) {
            String j1 = String.format("%.2f",apotekModel.getJarak()).replace(",",".");
            String j2 = String.format("%.2f",t1.getJarak()).replace(",",".");
            Double j11 = new Double(Double.valueOf(j1)*1000);
            Double j12 = new Double(Double.valueOf(j2)*1000);
            int hasilJ1 = j11.intValue();
            int hasilJ2 = j11.intValue();
            return hasilJ2-hasilJ1;
        }
    };

    @Override
    public int compareTo(@NonNull Object o) {
        String j1 = String.format("%.2f",((ApotekModel)o).getJarak()).replace(",",".");
        String j2 = String.format("%.2f",this.getJarak()).replace(",",".");
        Double j11 = new Double(Double.valueOf(j1)*1000);
        Double j12 = new Double(Double.valueOf(j2)*1000);
        int hasilJ1 = j11.intValue();
        int hasilJ2 = j12.intValue();
        return hasilJ2-hasilJ1;
    }
}
