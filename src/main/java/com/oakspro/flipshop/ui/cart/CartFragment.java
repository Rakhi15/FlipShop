package com.oakspro.flipshop.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.oakspro.flipshop.AddCartModel;
import com.oakspro.flipshop.CartAdapter;
import com.oakspro.flipshop.R;

public class CartFragment extends Fragment {


    Button buyNow;
    RecyclerView cartRecycler;
    CartAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecycler=(RecyclerView)root.findViewById(R.id.recyclerView11);
        buyNow=(Button)root.findViewById(R.id.buyBtn);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseOperation();
        return root;
    }

    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("CartAll");

        FirebaseRecyclerOptions<AddCartModel> options=new FirebaseRecyclerOptions.Builder<AddCartModel>()
                .setQuery(query, AddCartModel.class)
                .build();
        adapter=new CartAdapter(options);
        cartRecycler.setAdapter(adapter);

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