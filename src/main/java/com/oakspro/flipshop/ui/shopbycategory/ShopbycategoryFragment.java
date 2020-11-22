package com.oakspro.flipshop.ui.shopbycategory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oakspro.flipshop.Category;
import com.oakspro.flipshop.HolderCategory;
import com.oakspro.flipshop.ProductsListActivity;
import com.oakspro.flipshop.R;

public class ShopbycategoryFragment extends Fragment {

    TextView message;
    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopbycategory, container, false);


        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Category_img");
        recyclerView=root.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager sm=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sm);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Category> options=new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(ref, Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, HolderCategory> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Category, HolderCategory>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HolderCategory holder, int position, @NonNull final Category model) {

                holder.setView(getContext(), model.getcName(), model.getcImageUri());
                Log.i("TAG", model.getcName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(getActivity(),model.getcName(),Toast.LENGTH_LONG).show();

                        Intent pIntent=new Intent(getContext(), ProductsListActivity.class);
                        pIntent.putExtra("pName", model.getcName());
                        pIntent.putExtra("uemail", getActivity().getIntent().getStringExtra("uemail").toString());
                        startActivity(pIntent);
                    }
                });
            }

            @NonNull
            @Override
            public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_item, parent, false);
                return new HolderCategory(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

}
