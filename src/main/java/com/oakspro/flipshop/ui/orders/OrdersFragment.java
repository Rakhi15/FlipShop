package com.oakspro.flipshop.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.oakspro.flipshop.OrdersAdapter;
import com.oakspro.flipshop.OrdersModel;
import com.oakspro.flipshop.R;

public class OrdersFragment extends Fragment {



    RecyclerView ordersRecycler;
    OrdersAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersRecycler=(RecyclerView)root.findViewById(R.id.recyclerView11);

        ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseOperation();
        return root;
    }
    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<OrdersModel> options=new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query, OrdersModel.class)
                .build();
        adapter=new OrdersAdapter(options);
        ordersRecycler.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
