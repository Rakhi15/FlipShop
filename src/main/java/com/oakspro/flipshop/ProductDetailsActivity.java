package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsActivity extends AppCompatActivity {

    String key;

    DatabaseReference ref;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Query query;

    private String brand, name, mrp, price, description,color, size, category, skey;
    private Integer stock;
    private String cImageUri1, cImageUri2, cImageUri3, cImageUri4;

    TextView pmrp, pprice, pname, pcolor, pbrand, pdesc, psize, stockav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        pmrp=findViewById(R.id.productmrp);
        pprice=findViewById(R.id.productprice);
        pname=findViewById(R.id.productname);
        pcolor=findViewById(R.id.color_txt);
        pbrand=findViewById(R.id.brand_txt);
        pdesc=findViewById(R.id.description_txt);
        psize=findViewById(R.id.size_txt);
        stockav=findViewById(R.id.stock);



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        key=getIntent().getStringExtra("pid").toString();

       // query=FirebaseDatabase.getInstance().getReference("Products").orderByChild("key").equalTo(key);

     //   query.addListenerForSingleValueEvent(valueEventListener);

        ref=FirebaseDatabase.getInstance().getReference("Products").child(key);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brand=snapshot.child("brand").getValue().toString();
                name=snapshot.child("name").getValue().toString();
                mrp=snapshot.child("mrp").getValue().toString();
                price=snapshot.child("price").getValue().toString();
                description=snapshot.child("description").getValue().toString();
                stock= Integer.valueOf(snapshot.child("stock").getValue().toString());
                color=snapshot.child("color").getValue().toString();
                size=snapshot.child("size").getValue().toString();
                skey=snapshot.child("key").getValue().toString();

                cImageUri1=snapshot.child("cImageUri1").getValue().toString();
                cImageUri2=snapshot.child("cImageUri2").getValue().toString();
                cImageUri3=snapshot.child("cImageUri3").getValue().toString();
                cImageUri4=snapshot.child("cImageUri4").getValue().toString();

                ArrangeData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void ArrangeData() {

        pname.setText(name);
        pmrp.setText("MRP. "+mrp);
        pmrp.setPaintFlags(pmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pprice.setText("Rs. "+price);
        pbrand.setText(brand);
        psize.setText(size);
        pcolor.setText(color);
        pdesc.setText(description);

        if (stock>0){
            stockav.setTextColor(Color.GREEN);
            stockav.setText("IN STOCK");
        }else {
            stockav.setTextColor(Color.RED);
            stockav.setText("OUT OF STOCK");
        }
        progressDialog.dismiss();

    }
}