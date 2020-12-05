package com.oakspro.flipshop.ui.wishlist;

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
import com.oakspro.flipshop.R;
import com.oakspro.flipshop.WishlistAdapter;
import com.oakspro.flipshop.WishlistModel;

public class WishlistFragment extends Fragment {
    RecyclerView wishlistRecycler;
    WishlistAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);
        wishlistRecycler=(RecyclerView)root.findViewById(R.id.recyclerView11);

        wishlistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseOperation();
        return root;
    }

    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("Wishlist");

        FirebaseRecyclerOptions<WishlistModel> options=new FirebaseRecyclerOptions.Builder<WishlistModel>()
                .setQuery(query, WishlistModel.class)
                .build();
        adapter=new WishlistAdapter(options);
        wishlistRecycler.setAdapter(adapter);
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
