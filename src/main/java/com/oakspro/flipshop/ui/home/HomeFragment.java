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

import com.google.firebase.auth.FirebaseAuth;
import com.oakspro.flipshop.HomeActivity;
import com.oakspro.flipshop.LoginActivity;
import com.oakspro.flipshop.R;

public class HomeFragment extends Fragment {

    Button logoutBtn;
    FirebaseAuth mAuth;
    TextView message;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);



        logoutBtn=root.findViewById(R.id.logoutLink);
        message=root.findViewById(R.id.message);
/*
        String name=getIntent().getStringExtra("uname").toString();
        String email=getIntent().getStringExtra("uemail").toString();
*/
      //  message.setText("Welcome \n Name: "+name+" Email: "+email);



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return root;
    }
}