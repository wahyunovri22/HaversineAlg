package singpaulee.com.haversinealgorythm.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import singpaulee.com.haversinealgorythm.Haversine;
import singpaulee.com.haversinealgorythm.R;
import singpaulee.com.haversinealgorythm.SharedPrefManager;
import singpaulee.com.haversinealgorythm.adapter.ApotekAdapter;
import singpaulee.com.haversinealgorythm.model.ApotekModel;
import singpaulee.com.haversinealgorythm.model.ResponseModel;
import singpaulee.com.haversinealgorythm.rest.ApiClient;
import singpaulee.com.haversinealgorythm.rest.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    ArrayList<ApotekModel> list;
    ApotekAdapter adapter;
    SharedPrefManager prefManager;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);

        prefManager = new SharedPrefManager(getActivity());
        list = new ArrayList<>();
        getData();

        adapter = new ApotekAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(s.toLowerCase());
                return false;
            }
        });

        return view;
    }

    private void getData() {
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Loading ...");
        sweetAlertDialog.show();

        ApiClient apiClient = Config.getRetrofit().create(ApiClient.class);
        Call<ResponseModel> dataApotek = apiClient.getData();
        dataApotek.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                sweetAlertDialog.dismiss();
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getApotek().size(); i++) {
                        double latUser = Double.valueOf(prefManager.getLATITUDE());
                        double longUser = Double.valueOf(prefManager.getLONGITUDE());
                        double latApotek = Double.valueOf(response.body().getApotek().get(i).getLatitude());
                        double longApotek = Double.valueOf(response.body().getApotek().get(i).getLongitude());
                        double hasilJarak1 = Haversine.hitungJarak(latUser, longUser, latApotek, longApotek);

                        ApotekModel apotekModel = new ApotekModel(
                                response.body().getApotek().get(i).getId(),
                                response.body().getApotek().get(i).getNama(),
                                response.body().getApotek().get(i).getAlamat(),
                                response.body().getApotek().get(i).getTelepon(),
                                response.body().getApotek().get(i).getLatitude(),
                                response.body().getApotek().get(i).getLongitude(),
                                response.body().getApotek().get(i).getGambar(),
                                hasilJarak1
                        );
                        list.add(apotekModel);
                    }

                    Collections.sort(list);

                    adapter = new ApotekAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "" + list.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                sweetAlertDialog.dismiss();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                SweetAlertDialog error = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                error.setTitleText("Periksa Koneksi Anda");
                error.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        getData();
                    }
                });
                error.show();
            }
        });
    }


    private void initView(View view) {
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}
