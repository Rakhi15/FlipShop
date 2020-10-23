package com.oakspro.flipshop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.oakspro.flipshop.HomeActivity;
import com.oakspro.flipshop.LoginActivity;
import com.oakspro.flipshop.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Button logoutBtn;
    FirebaseAuth mAuth;
    TextView message;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);



        logoutBtn=root.findViewById(R.id.logoutLink);
        message=root.findViewById(R.id.message);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
/*
        String name=getIntent().getStringExtra("uname").toString();
        String email=getIntent().getStringExtra("uemail").toString();
*/
      //  message.setText("Welcome \n Name: "+name+" Email: "+email);

        //offer slider

        ImageSlider slider=root.findViewById(R.id.imageSlider);

        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("https://www.coupondunia.in/blog/wp-content/uploads/2017/08/Amazon-and-FK-Sale_1200-x-628-2-1050x550.jpg"));
        slideModels.add(new SlideModel("https://static.digit.in/default/9fc7020964bccbda38991ecf2779ce8e075f12a6.jpeg"));
        slideModels.add(new SlideModel("https://www.jagranimages.com/images/08_10_2018-flipkar_amazon_18512112_132414573.jpg"));
        slideModels.add(new SlideModel("https://trak.in/wp-content/uploads/2019/12/Flipkart-end-of-year-sale-top-deals-1024x550.jpg"));
        slideModels.add(new SlideModel("https://trak.in/wp-content/uploads/2019/10/second-flipkart-big-diwali-sale-1280x720.png"));
        slideModels.add(new SlideModel("https://images.indianexpress.com/2020/10/Untitled-design-2020-10-15T171350.830.jpg"));
        slider.setImageList(slideModels, true);


        return root;
    }
}