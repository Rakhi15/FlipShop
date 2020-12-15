package com.oakspro.flipshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AdminOrderConfirmation extends AppCompatActivity {

    RecyclerView ordersRecycler;
    AdminOrderConfirmationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_confirmation);
        ordersRecycler=(RecyclerView)findViewById(R.id.recyclerView11);
        ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        firebaseOperation();
    }


    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<OrdersModel> options=new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query, OrdersModel.class)
                .build();
        adapter=new AdminOrderConfirmationAdapter(options);
        ordersRecycler.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.notifyDataSetChanged();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }
}
