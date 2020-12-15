package com.oakspro.flipshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminOrderConfirmationAdapter extends FirebaseRecyclerAdapter<OrdersModel, AdminOrderConfirmationAdapter.AdminOrderConfirmationHolder> {

    private DatabaseReference databaseReference;
    private String firebase_msg_api="https://fcm.googleapis.com/fcm/send";
    FirebaseMessaging firebaseMessaging;

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
                public void onClick(final View v) {
                    String newstatus="1";
                    HashMap updateHashMap=new HashMap();
                    updateHashMap.put("oStatus", newstatus);

                    databaseReference.child(model.getpOrderkey()).updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                            holder.acceptBtn.setText("ACCEPTED");
                            holder.acceptBtn.setEnabled(false);

                            firebaseMessaging.getInstance().subscribeToTopic("news");
                            sendNotificationToken(model.getProductName(), v);
                        }
                    });
                }
            });
            holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }




    }

    private void sendNotificationToken(String product_name, View view) {
        JSONObject object=new JSONObject();
        try {
            object.put("to", "/topics/"+"news");
            JSONObject notifi=new JSONObject();
            notifi.put("title", "Order Status");
            notifi.put("body", "Your Order has been placed. Product: "+product_name);
            object.put("notification", notifi);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, firebase_msg_api, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header=new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAJliIE9M:APA91bGyv-o50fsebXWGn0EqkyE7Xa5JZI7Y7cigeTaJZnEmBccCZgvSK59hqsDcx2nNccl-ZALMyulbMW2U2PtbL-mSQEpQPigX17t3TlhohBIj1RJFXBYE3G1HUIZqQNqHTVLxLlGD");
                    return header;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(view.getContext());
            requestQueue.add(request);


        }catch ( JSONException e){
            e.printStackTrace();
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
