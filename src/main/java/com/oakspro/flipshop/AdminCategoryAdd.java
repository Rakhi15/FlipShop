package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdminCategoryAdd extends AppCompatActivity {


    Button pick_img, uploadbtn;
    ImageView loadedimg;
    EditText category_name_ed;
    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_add);
        //set ids
        pick_img=findViewById(R.id.pick_pic);
        uploadbtn=findViewById(R.id.upload);
        loadedimg=findViewById(R.id.selected_pic);
        category_name_ed=findViewById(R.id.cat_name);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Data Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        storageReference= FirebaseStorage.getInstance().getReference("Category_img");
        databaseReference= FirebaseDatabase.getInstance().getReference("Category_img");


            pick_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dexter.withActivity(AdminCategoryAdd.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }
            });


            uploadbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(category_name_ed.getText().toString())){
                        categoryUpload();
                    }

                }
            });

    }

    private void categoryUpload() {

        progressDialog.show();
        if (imageUri !=null){
            StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+fileExt(imageUri));

            filereference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(AdminCategoryAdd.this, "Category Updated", Toast.LENGTH_SHORT).show();
                            CategoryUpload upload=new CategoryUpload(category_name_ed.getText().toString().trim(), taskSnapshot.getUploadSessionUri().toString());
                           // String uploadId=databaseReference.push().getKey();
                            String uploadId=category_name_ed.getText().toString();
                            databaseReference.child(uploadId).setValue(upload);
                            progressDialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminCategoryAdd.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
        else {
            Toast.makeText(AdminCategoryAdd.this, "No Picture Selected", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK && data!=null){

            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                loadedimg.setImageBitmap(bitmap);
                imageUri=data.getData();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String fileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

}