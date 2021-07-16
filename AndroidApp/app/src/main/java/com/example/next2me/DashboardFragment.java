package com.example.next2me;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.next2me.utils.Utilities;
import com.example.next2me.viewmodel.CardItem;
import com.example.next2me.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnItemListener{

    private RecyclerView recyclerView;
    private List<CardItem> cardItems;
    private CardAdapter adapter;

    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(adapter);

        model = new ViewModelProvider((ViewModelStoreOwner)getActivity()).get(ListViewModel.class);
        model.getItems(((HomeActivity)getActivity()).getCurrentUserPos()).observe((LifecycleOwner) getActivity(), new Observer<List<CardItem>>() {
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