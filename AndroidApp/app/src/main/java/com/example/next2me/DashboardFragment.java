package com.example.next2me;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.example.next2me.viewmodel.CardItem;
import com.example.next2me.viewmodel.ListViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class DashboardFragment extends Fragment implements OnItemListener{

    private RecyclerView recyclerView;
    private List<CardItem> cardItems;
    private CardAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
    private LatLng currentUserPos = new LatLng(44.0575500,12.5652800);

    private ListViewModel model;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        cardItems = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), cardItems, this);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onCreateView(inflater, container, savedInstanceState);
            }
        });

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                currentUserPos = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("pos", "status changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("pos", "provider enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("pos", "provider disabled");
            }
        };

        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        model = new ViewModelProvider((ViewModelStoreOwner)getActivity()).get(ListViewModel.class);
        model.getItems(currentUserPos).observe((LifecycleOwner) getActivity(), new Observer<List<CardItem>>() {
            @Override
            public void onChanged(List<CardItem> cardItems) {
                Log.d("items", cardItems.toString());
                adapter.setData(cardItems);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            model.select(adapter.getItem(position));
            Utilities.insertFragment(appCompatActivity, new DetailsFragment(),
                    DetailsFragment.class.getSimpleName());
        }
    }
}