package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    LinearLayout linearLayoutNew;
    Spinner category_spinner;
    ArrayList<String> category_names=new ArrayList<>();

    DatabaseReference reference;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        linearLayoutNew=findViewById(R.id.linearNew);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        category_spinner=findViewById(R.id.category_spinner);
        //instance
        reference=FirebaseDatabase.getInstance().getReference().child("Category_img");

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, category_names);
        category_spinner.setAdapter(adapter);

        getCategories();


        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    linearLayoutNew.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutNew.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    linearLayoutNew.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void getCategories() {
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category_names.clear();
                category_names.add("Select Category");
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    category_names.add(dataSnapshot.getKey().toString());
                    Log.i("TAG", dataSnapshot.getKey().toString());
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}