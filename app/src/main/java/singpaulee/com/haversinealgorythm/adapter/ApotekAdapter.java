package singpaulee.com.haversinealgorythm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import singpaulee.com.haversinealgorythm.Haversine;
import singpaulee.com.haversinealgorythm.R;
import singpaulee.com.haversinealgorythm.SharedPrefManager;
import singpaulee.com.haversinealgorythm.model.ApotekModel;

/**
 * Created by ASUS on 24/05/2018.
 */

public class ApotekAdapter extends RecyclerView.Adapter<ApotekAdapter.ViewHolder>
implements Filterable{
    Context context;
    ArrayList<ApotekModel> list;
    ArrayList<ApotekModel> filteredList;
    SharedPrefManager prefManager;

    public ApotekAdapter(Context context, ArrayList<ApotekModel> list) {
        this.context = context;
        this.list = list;
        filteredList = list;
        prefManager = new SharedPrefManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso
                .with(context)
                .load("https://seputarsemarang.000webhostapp.com/api/apotek/image/"+filteredList.get(position).getGambar())
                .resize(64,56)
                .into(holder.imageView);

        holder.tvNama.setText(""+filteredList.get(position).getNama());
        holder.tvAlamat.setText(""+filteredList.get(position).getAlamat());
        holder.tvTelp.setText(""+filteredList.get(position).getTelepon());

        double latUser = Double.valueOf(prefManager.getLATITUDE());
        double longUser = Double.valueOf(prefManager.getLONGITUDE());
        double latApotek = Double.valueOf(filteredList.get(position).getLatitude());
        double longApotek = Double.valueOf(filteredList.get(position).getLongitude());
        String jarak = String.valueOf(Haversine.hitungJarak(latUser,longUser,latApotek,longApotek)).substring(0,4);
        holder.tvJarak.setText(""+String.format("%.2f",list.get(position).getJarak())+" km");
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    ArrayList<ApotekModel> list2 = new ArrayList<>();

                    for (ApotekModel model : list) {
                        if (model.getNama().toLowerCase().contains(charSequence) ||
                                model.getAlamat().toLowerCase().contains(charSequence)) {
                            list2.add(model);
                        }
                    }
                    filteredList = list2;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<ApotekModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNama;
        TextView tvAlamat;
        TextView tvJarak;
        TextView tvTelp;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_image);
            tvNama= itemView.findViewById(R.id.list_nama);
            tvAlamat= itemView.findViewById(R.id.list_alamat);
            tvTelp= itemView.findViewById(R.id.list_telp);
            tvJarak= itemView.findViewById(R.id.list_jarak);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "http://maps.google.com/maps?saddr="+prefManager.getLATITUDE()+","+prefManager.getLONGITUDE()+"&daddr="+""+list.get(getAdapterPosition()).getLatitude()+","+list.get(getAdapterPosition()).getLongitude();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    context.startActivity(intent);
                }
            });
        }
    }
}
