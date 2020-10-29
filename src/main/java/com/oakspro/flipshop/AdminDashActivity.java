package com.oakspro.flipshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
    }

    public void openAdminCatAdd(View view) {

        Intent intent=new Intent(AdminDashActivity.this, AdminCategoryAdd.class);
        startActivity(intent);
    }
}