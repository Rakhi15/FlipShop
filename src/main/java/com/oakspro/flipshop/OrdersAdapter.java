package com.oakspro.flipshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class OrdersAdapter extends FirebaseRecyclerAdapter<OrdersModel,OrdersAdapter.OrdersViewHolder> {

    public OrdersAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull OrdersModel model) {
        holder.p_name.setText(model.getProductName());

        Picasso.get().load(model.getpImage()).into(holder.p_image);
        String orderstatus_string=model.getoStatus();
        if (orderstatus_string.equals("0")){
            holder.orderstatus.setText("PENDING");
            holder.orderstatus.setTextColor(Color.BLUE);

        }else if (orderstatus_string.equals("1")){
            //accept
            holder.orderstatus.setText("ACCEPTED");
            holder.orderstatus.setTextColor(Color.GREEN);
        }else if(orderstatus_string.equals("2")){
            //reject
            holder.orderstatus.setText("REJECTED");
            holder.orderstatus.setTextColor(Color.RED);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent pIntent=new Intent(Context, ViewOrder.class);
                pIntent.putExtra("pName", model.getcName());
                pIntent.putExtra("uemail", getActivity().getIntent().getStringExtra("uemail").toString());
                Context.startActivity(pIntent);*/
            }
        });
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderslayout, parent, false);
        return new OrdersViewHolder(view);
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder{
        ImageView p_image;
        TextView p_name,orderstatus;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            p_image=(ImageView)itemView.findViewById(R.id.productImg);
            p_name=(TextView)itemView.findViewById(R.id.productName);
            orderstatus=(TextView)itemView.findViewById(R.id.orderstatus);
        }
    }
}
