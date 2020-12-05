package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    String key;

    ViewPager productView;
    DatabaseReference ref;
    FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Query query;
    Button addCart;

    static ArrayList<String> imagesall=new ArrayList<String>();

    private String brand, name, mrp, price, description,color, size, category, skey;
    private Integer stock;
    private String cImageUri1, cImageUri2, cImageUri3, cImageUri4;
    private String uemail;

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
        addCart=findViewById(R.id.cart);

       productView=findViewById(R.id.viewPager);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        key=getIntent().getStringExtra("pid").toString();
        uemail=getIntent().getStringExtra("uemail").toString();

        imagesall.clear();

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

                imagesall.add(cImageUri2);
                imagesall.add(cImageUri3);
                imagesall.add(cImageUri4);

                ArrangeData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                addCartProgram();
            }
        });


    }

    private void addCartProgram() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CartAll");
        String uploadId=databaseReference.push().getKey();

        AddCartModel add=new AddCartModel(uemail, skey, name, cImageUri1, price, uploadId);
        databaseReference.child(uploadId).setValue(add);
        Toast.makeText(this, ""+name+" added to cart", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();

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

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        productView.setAdapter(viewPagerAdapter);


        progressDialog.dismiss();

    }

    public void wishlist(View view) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Wishlist");
        String uploadId=databaseReference.push().getKey();

        WishlistModel add=new WishlistModel(uemail, skey, name, cImageUri1, price, uploadId);
        databaseReference.child(uploadId).setValue(add);
        //wishlist.setBackgroundColor(Color.parseColor("#FF0000"));
        Toast.makeText(this, ""+name+" added to wishlist", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();

    }

    public void buynow(View view) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Orders");
        String uploadId=databaseReference.push().getKey();
        String ostatus="0";
        OrdersModel add=new OrdersModel(uemail, skey, name, cImageUri1, price, uploadId, ostatus);
        databaseReference.child(uploadId).setValue(add);
        Intent i=new Intent(this,AdminOrderConfirmation.class);
        i.putExtra("productname",name);
        i.putExtra("productimage",cImageUri1);


        Toast.makeText(this, ""+name+" added to Orders", Toast.LENGTH_SHORT).show();
    }


}