package com.oakspro.flipshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button logoutBtn;
    FirebaseAuth mAuth;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutBtn=findViewById(R.id.logoutLink);
        message=findViewById(R.id.message);

        String name=getIntent().getStringExtra("uname").toString();
        String email=getIntent().getStringExtra("uemail").toString();

        message.setText("Welcome \n Name: "+name+" Email: "+email);



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}