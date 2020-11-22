package com.oakspro.flipshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class CartAdapter extends FirebaseRecyclerAdapter<AddCartModel, CartAdapter.cartViewHolder> {


    public CartAdapter(FirebaseRecyclerOptions<AddCartModel> options){
        super(options);
    } 
    
    @Override
    protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull AddCartModel model) {
        
        holder.p_name.setText(model.getProductName());
        holder.p_price.setText(model.getpPrice());

        holder.uploadId=model.getpCartkey();
        Picasso.get().load(model.getpImage()).into(holder.p_image);
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem, parent, false);
        return new cartViewHolder(view);
    }

    public class cartViewHolder extends RecyclerView.ViewHolder{

        ImageView p_image;
        TextView p_name, p_price;
        Button deleteBtn;
        String uploadId;
        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            
            p_image=(ImageView)itemView.findViewById(R.id.productImg);
            p_name=(TextView)itemView.findViewById(R.id.productName);
            p_price=(TextView)itemView.findViewById(R.id.productPrice);
            deleteBtn=(Button)itemView.findViewById(R.id.deleteBtn);
            
            
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    // delete data from firebase code

                    String newdeleteId=uploadId;
                    
                }
            });
        }
    }

}
