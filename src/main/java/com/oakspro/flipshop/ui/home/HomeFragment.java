package com.oakspro.flipshop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oakspro.flipshop.Category;
import com.oakspro.flipshop.HolderCategory;
import com.oakspro.flipshop.HomeActivity;
import com.oakspro.flipshop.LoginActivity;
import com.oakspro.flipshop.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;

    Button logoutBtn;
    FirebaseAuth mAuth;
    TextView message;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //instance

        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Category_img");

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
        recyclerView=root.findViewById(R.id.recyclerView);

        LinearLayoutManager managerCategory=new LinearLayoutManager(getContext());


        managerCategory.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(managerCategory);

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

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Category> options=new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(ref, Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, HolderCategory> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Category, HolderCategory>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HolderCategory holder, int position, @NonNull Category model) {

                holder.setView(getContext(), model.getcName(), model.getcImageUri());
                Log.i("TAG", model.getcName());
            }

            @NonNull
            @Override
            public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_category, parent, false);
                return new HolderCategory(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}