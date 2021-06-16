package com.ezc.favoritelist.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezc.favoritelist.CoffeeAdapter;
import com.ezc.favoritelist.CoffeeItem;
import com.ezc.favoritelist.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<CoffeeItem> coffeeItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CoffeeAdapter(coffeeItems, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        coffeeItems.add(new CoffeeItem(R.drawable.beach, "Beach", "0","0"));
        coffeeItems.add(new CoffeeItem(R.drawable.food1, "food1", "1","0"));
        coffeeItems.add(new CoffeeItem(R.drawable.food2, "food2", "2","0"));
        coffeeItems.add(new CoffeeItem(R.drawable.food3, "food3", "3","0"));
        coffeeItems.add(new CoffeeItem(R.drawable.food4, "food4", "4","0"));
        coffeeItems.add(new CoffeeItem(R.drawable.maxresdefault, "Mugh M.", "5","0"));

        return root;
    }

}