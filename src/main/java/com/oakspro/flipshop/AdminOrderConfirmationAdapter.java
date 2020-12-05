package com.oakspro.flipshop;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminOrderConfirmationAdapter extends FirebaseRecyclerAdapter<OrdersModel, AdminOrderConfirmationAdapter.AdminOrderConfirmationHolder> {

    private DatabaseReference databaseReference;

    public AdminOrderConfirmationAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final AdminOrderConfirmationHolder holder, int position, @NonNull final OrdersModel model) {


        String ostatus=model.getoStatus().toString();
        if (ostatus.equals("0")){

            holder.p_name.setText(model.getProductName());
            holder.customermail.setText(model.getUserID());
            Picasso.get().load(model.getpImage()).into(holder.p_image);

            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newstatus="1";
                    HashMap updateHashMap=new HashMap();
                    updateHashMap.put("oStatus", newstatus);

                    databaseReference.child(model.getpOrderkey()).updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                            holder.acceptBtn.setText("ACCEPTED");
                            holder.acceptBtn.setEnabled(false);
                        }
                    });
                }
            });
            holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }




    }

    @NonNull
    @Override
    public AdminOrderConfirmationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        databaseReference= FirebaseDatabase.getInstance().getReference("Orders");

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_admin_order_confirmation, parent, false);
        return new AdminOrderConfirmationHolder(view);
    }

    public class AdminOrderConfirmationHolder extends RecyclerView.ViewHolder{
        ImageView p_image;
        TextView p_name,customermail;
        Button acceptBtn, rejectBtn, moreBtn;
        public AdminOrderConfirmationHolder(@NonNull View itemView) {
            super(itemView);
            p_image=(ImageView)itemView.findViewById(R.id.productImg);
            p_name=(TextView)itemView.findViewById(R.id.productName);
            customermail=(TextView)itemView.findViewById(R.id.customerMail);
            acceptBtn=(Button)itemView.findViewById(R.id.accept);
            rejectBtn=(Button)itemView.findViewById(R.id.reject);
            moreBtn=(Button)itemView.findViewById(R.id.moredetails);
        }
    }
}
