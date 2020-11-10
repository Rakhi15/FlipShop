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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AdminCategoryAdd extends AppCompatActivity {


    Button pick_img, uploadbtn;
    ImageView loadedimg;
    EditText category_name_ed;
    private Uri imageUri;
    String imageurl, newimg, downloadUrl;
    private StorageReference storageReference, storeref;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
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

        storageReference= FirebaseStorage.getInstance().getReference().child("Category_img");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Category_img");


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

            imageurl=System.currentTimeMillis()+"."+fileExt(imageUri);
            final StorageReference filereference=storageReference.child(imageurl);

            final UploadTask uploadTask=filereference.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AdminCategoryAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminCategoryAdd.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadUrl = filereference.getDownloadUrl().toString();
                            return filereference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                downloadUrl = task.getResult().toString();

                                Toast.makeText(AdminCategoryAdd.this, "got the Category image Url Successfully...", Toast.LENGTH_SHORT).show();

                                sendData();
                            }
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(AdminCategoryAdd.this, "No Picture Selected", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void sendData() {

       // storeref=storage.getReferenceFromUrl("gs://flipshop-48732.appspot.com/Category_img").child(imageurl);

                    CategoryUpload upload=new CategoryUpload(category_name_ed.getText().toString().trim(), downloadUrl);
                    // String uploadId=databaseReference.push().getKey();
                    String uploadId=category_name_ed.getText().toString();
                    databaseReference.child(uploadId).setValue(upload);
                    progressDialog.dismiss();



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