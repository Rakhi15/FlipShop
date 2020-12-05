package com.oakspro.flipshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class WishlistAdapter extends FirebaseRecyclerAdapter<WishlistModel,WishlistAdapter.WishlistViewHolder> {


    public WishlistAdapter(@NonNull FirebaseRecyclerOptions<WishlistModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishlistViewHolder holder, int position, @NonNull WishlistModel model) {
        holder.p_name.setText(model.getProductName());
        holder.p_price.setText(model.getpPrice());

        holder.uploadId=model.getpCartkey();
        Picasso.get().load(model.getpImage()).into(holder.p_image);
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlistitem, parent, false);
        return new WishlistViewHolder(view);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder{
        ImageView p_image;
        TextView p_name, p_price,rating;
        Button addtocart;

        String uploadId;
        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            p_image=(ImageView)itemView.findViewById(R.id.productImg);
            p_name=(TextView)itemView.findViewById(R.id.productName);
            p_price=(TextView)itemView.findViewById(R.id.productPrice);
            addtocart=(Button)itemView.findViewById(R.id.wishcart);
            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // delete data from firebase code

                    String newdeleteId=uploadId;

                }
            });
        }
    }
}
