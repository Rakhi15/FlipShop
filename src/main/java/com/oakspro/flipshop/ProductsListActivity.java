package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductsListActivity extends AppCompatActivity {

    private String pName;
    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Query query;
    String uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        recyclerView=findViewById(R.id.recyclerView23);

        StaggeredGridLayoutManager sm=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sm);

        pName=getIntent().getStringExtra("pName").toString();
        uemail=getIntent().getStringExtra("uemail").toString();

        query=FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").equalTo(pName);

        getProductsList();

    }
    private void getProductsList() {

        progressDialog.dismiss();
        FirebaseRecyclerOptions<ProductsModel> options=new FirebaseRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class)
                .build();

        FirebaseRecyclerAdapter<ProductsModel, ProductHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ProductsModel, ProductHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final ProductsModel model) {

                holder.setView(ProductsListActivity.this, model.getName(), model.getcImageUri1(), model.getMrp(), model.price);
                Log.i("TAG", model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Toast.makeText(getApplicationContext(),model.getKey(),Toast.LENGTH_LONG).show();
                      Intent intent_product=new Intent(ProductsListActivity.this, ProductDetailsActivity.class);
                        intent_product.putExtra("pid", model.getKey().toString());
                        intent_product.putExtra("uemail", uemail);
                        startActivity(intent_product);





                    }
                });
            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_list_item, parent, false);
                return new ProductHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

}