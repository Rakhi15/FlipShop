package com.oakspro.flipshop;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ProductHolder extends RecyclerView.ViewHolder {

    View view;
    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }

    public void setView(Context context, String pname, String pimgurl, String pmrp, String pprice){
        TextView pName=view.findViewById(R.id.name_product);
        ImageView pImage=view.findViewById(R.id.image_product);
        TextView pMrp=view.findViewById(R.id.productmrpprice);
        TextView price=view.findViewById(R.id.productprice);

        pName.setText(pname);
        Picasso.get().load(pimgurl.toString()).into(pImage);
        pMrp.setText(pmrp);
        pMrp.setPaintFlags(pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        price.setText(pprice);

    }

}
