package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    LinearLayout linearLayoutNew;
    Spinner category_spinner;
    ArrayList<String> category_names=new ArrayList<>();

    DatabaseReference reference;
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;

    //
    private StorageReference storageReference;
    // private DatabaseReference databaseReference;

    private Uri imageUri1,imageUri2,imageUri3,imageUri4;
    String imguri1,imguri2,imguri3,imguri4;

    DatabaseReference dr;
    EditText brand,name,mrp,price,color,stock,size,description;
    Button picThumb,photo2,photo3,photo4, uploadBtn;
    ImageView img1;
    String downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        linearLayoutNew=findViewById(R.id.linearNew);


        //
        brand=findViewById(R.id.brand);
        name=findViewById(R.id.name);
        mrp=findViewById(R.id.mrp);
        price=findViewById(R.id.price);
        color=findViewById(R.id.color);
        stock=findViewById(R.id.stock);
        size=findViewById(R.id.size);
        description=findViewById(R.id.description_ed);
        uploadBtn=findViewById(R.id.uploadBtn);

        picThumb=findViewById(R.id.pick_pic);
        photo2=findViewById(R.id.pick_pic_pht1);
        photo3=findViewById(R.id.pick_pic_pht2);
        photo4=findViewById(R.id.pick_pic_pht3);
        img1=findViewById(R.id.selected_pic);

        //




        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        category_spinner=findViewById(R.id.category_spinner);
        //instance
        reference=FirebaseDatabase.getInstance().getReference().child("Category_img");

        storageReference= FirebaseStorage.getInstance().getReference("Products_img");
        dr= FirebaseDatabase.getInstance().getReference("Products");


        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, category_names);
        category_spinner.setAdapter(adapter);

        getCategories();


        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    linearLayoutNew.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutNew.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    linearLayoutNew.setVisibility(View.INVISIBLE);
            }
        });




        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sbrand=brand.getText().toString();
                String sname=name.getText().toString();
                String smrp=mrp.getText().toString();
                String sprice=price.getText().toString();
                String sstock=stock.getText().toString();
                String scolor=color.getText().toString();
                String ssize=size.getText().toString();
                String sdescription=description.getText().toString();
                String scategory=category_spinner.getSelectedItem().toString();


                if (!TextUtils.isEmpty(sbrand) && !TextUtils.isEmpty(sname) && !TextUtils.isEmpty(smrp) && !TextUtils.isEmpty(sprice) && !TextUtils.isEmpty(sstock) && !TextUtils.isEmpty(scolor) &&
                        !TextUtils.isEmpty(ssize) && !TextUtils.isEmpty(sdescription) && imageUri1 !=null && imageUri2 !=null && imageUri3 !=null && imageUri4!=null){

                    uploadProduct(scategory, sname, sbrand, smrp, sprice, sstock, ssize, scolor, sdescription, imageUri1, imageUri2, imageUri3, imageUri4);

                }else {
                    Toast.makeText(AddProductActivity.this, "All Details Required", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void uploadProduct(final String scategory, final String sname, final String sbrand, final String smrp, final String sprice, final String sstock, final String ssize, final String scolor, final String sdescription, Uri imageUri1, final Uri imageUri2, final Uri imageUri3, final Uri imageUri4) {


        progressDialog.show();
        if (imageUri1 !=null && imageUri2!=null && imageUri3!=null && imageUri4!=null){
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+fileExt(imageUri1));
            final StorageReference filereference2=storageReference.child(System.currentTimeMillis()+"."+fileExt(imageUri2));
            final StorageReference filereference3=storageReference.child(System.currentTimeMillis()+"."+fileExt(imageUri3));
            final StorageReference filereference4=storageReference.child(System.currentTimeMillis()+"."+fileExt(imageUri4));



            //new code

            final UploadTask uploadTask1=filereference.putFile(imageUri1);
            uploadTask1.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                              @Override
                                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                  Toast.makeText(AddProductActivity.this, "Product-1 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                                  Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                      @Override
                                                                      public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                          if (!task.isSuccessful()) {
                                                                              throw task.getException();
                                                                          }

                                                                          downloadUrl1 = filereference.getDownloadUrl().toString();
                                                                          return filereference.getDownloadUrl();
                                                                      }
                                                                  }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                      @Override
                                                                      public void onComplete(@NonNull Task<Uri> task) {
                                                                          if (task.isSuccessful()) {
                                                                              downloadUrl1 = task.getResult().toString();

                                                                              //image 2
                                                                              final UploadTask uploadTask2=filereference2.putFile(imageUri2);
                                                                              uploadTask2.addOnFailureListener(new OnFailureListener() {
                                                                                  @Override
                                                                                  public void onFailure(@NonNull Exception e) {
                                                                                      String message = e.toString();
                                                                                      Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                                      progressDialog.dismiss();
                                                                                  }
                                                                              }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                  @Override
                                                                                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                      Toast.makeText(AddProductActivity.this, "Product-2 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                                                      Task<Uri> urlTask = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                          @Override
                                                                                          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                              if (!task.isSuccessful()) {
                                                                                                  throw task.getException();
                                                                                              }

                                                                                              downloadUrl2 = filereference2.getDownloadUrl().toString();
                                                                                              return filereference2.getDownloadUrl();
                                                                                          }
                                                                                      }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                          @Override
                                                                                          public void onComplete(@NonNull Task<Uri> task) {
                                                                                              if (task.isSuccessful()) {
                                                                                                  downloadUrl2 = task.getResult().toString();

                                                                                                  //image 3

                                                                                                  final UploadTask uploadTask3=filereference3.putFile(imageUri3);
                                                                                                  uploadTask3.addOnFailureListener(new OnFailureListener() {
                                                                                                      @Override
                                                                                                      public void onFailure(@NonNull Exception e) {
                                                                                                          String message = e.toString();
                                                                                                          Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                                                          progressDialog.dismiss();
                                                                                                      }
                                                                                                  }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                      @Override
                                                                                                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                          Toast.makeText(AddProductActivity.this, "Product-3 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                                                                          Task<Uri> urlTask = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                                              @Override
                                                                                                              public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                                                  if (!task.isSuccessful()) {
                                                                                                                      throw task.getException();
                                                                                                                  }

                                                                                                                  downloadUrl3 = filereference3.getDownloadUrl().toString();
                                                                                                                  return filereference3.getDownloadUrl();
                                                                                                              }
                                                                                                          }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                                              @Override
                                                                                                              public void onComplete(@NonNull Task<Uri> task) {
                                                                                                                  if (task.isSuccessful()) {
                                                                                                                      downloadUrl3 = task.getResult().toString();

                                                                                                                      //image 4

                                                                                                                      final UploadTask uploadTask4=filereference4.putFile(imageUri4);
                                                                                                                      uploadTask4.addOnFailureListener(new OnFailureListener() {
                                                                                                                          @Override
                                                                                                                          public void onFailure(@NonNull Exception e) {
                                                                                                                              String message = e.toString();
                                                                                                                              Toast.makeText(AddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                                                                              progressDialog.dismiss();
                                                                                                                          }
                                                                                                                      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                          @Override
                                                                                                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                              Toast.makeText(AddProductActivity.this, "Product-4 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                                                                                              Task<Uri> urlTask = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                                                                  @Override
                                                                                                                                  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                                                                      if (!task.isSuccessful()) {
                                                                                                                                          throw task.getException();
                                                                                                                                      }

                                                                                                                                      downloadUrl4 = filereference4.getDownloadUrl().toString();
                                                                                                                                      return filereference4.getDownloadUrl();
                                                                                                                                  }
                                                                                                                              }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                                                                  @Override
                                                                                                                                  public void onComplete(@NonNull Task<Uri> task) {
                                                                                                                                      if (task.isSuccessful()) {
                                                                                                                                          downloadUrl4 = task.getResult().toString();

                                                                                                                                          Toast.makeText(AddProductActivity.this, "All Images Successfully...", Toast.LENGTH_SHORT).show();

                                                                                                                                          ProductData(scategory, sbrand,sname, smrp,sprice, ssize, sstock, scolor, sdescription, downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4);
                                                                                                                                      }
                                                                                                                                  }
                                                                                                                              });


                                                                                                                          }
                                                                                                                      });
                                                                                                                  }
                                                                                                              }
                                                                                                          });


                                                                                                      }
                                                                                                  });
                                                                                              }
                                                                                          }
                                                                                      });


                                                                                  }
                                                                              });
                                                                          }
                                                                      }
                                                                  });


                                                              }
                                                          });



            /*
            //old code
            filereference.putFile(imageUri1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imguri1=taskSnapshot.getUploadSessionUri().toString();
                            // String uploadId=databaseReference.push().getKey();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
            filereference2.putFile(imageUri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imguri2=taskSnapshot.getUploadSessionUri().toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
            filereference3.putFile(imageUri3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imguri3=taskSnapshot.getUploadSessionUri().toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
            filereference4.putFile(imageUri4)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imguri4=taskSnapshot.getUploadSessionUri().toString();

                            ProductData(scategory, sbrand,sname, smrp,sprice, ssize, sstock, scolor, sdescription, imguri1, imguri2, imguri3, imguri4);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

            */
            //code end old
        }
        else{
            Toast.makeText(AddProductActivity.this, "No Picture Selected", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void ProductData(String scategory, String sbrand, String sname, String smrp, String sprice, String ssize, String sstock, String scolor, String sdescription, String imguri1, String imguri2, String imguri3, String imguri4) {
        String uploadId=dr.push().getKey();

       ProductUpload upload=new ProductUpload(uploadId, scategory, sbrand, sname, smrp, sprice, sdescription, sstock, scolor, ssize, imguri1, imguri2, imguri3, imguri4);

        //String uploadId=sname;
        dr.child(uploadId).setValue(upload);
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "All Done", Toast.LENGTH_SHORT).show();

    }


    public void getPics(final View view){

        Dexter.withActivity(AddProductActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), code(view));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }


                }).check();
    }
    public  int code(View v){
        int requestcode=0;
        switch (v.getId()) {
            case R.id.pick_pic:
                requestcode = 1;
                break;
            case R.id.pick_pic_pht1:
                requestcode = 2;
                break;
            case R.id.pick_pic_pht2:
                requestcode = 3;
                break;

            case R.id.pick_pic_pht3:
                requestcode = 4;
                break;
        }
        return requestcode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1&& resultCode==RESULT_OK && data!=null){

            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                img1.setImageBitmap(bitmap);
                imageUri1=data.getData();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        else if (requestCode==2&& resultCode==RESULT_OK && data!=null){

            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                BitmapDrawable newBit=new BitmapDrawable(getResources(), bitmap);
                photo2.setBackground(newBit);
                imageUri2=data.getData();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        else if (requestCode==3&& resultCode==RESULT_OK && data!=null){

            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                BitmapDrawable newBit=new BitmapDrawable(getResources(), bitmap);
                photo3.setBackground(newBit);
                imageUri3=data.getData();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        else if(requestCode==4&& resultCode==RESULT_OK && data!=null){

            Uri filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                BitmapDrawable newBit=new BitmapDrawable(getResources(), bitmap);
                photo4.setBackground(newBit);
                imageUri4=data.getData();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String fileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void getCategories() {
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category_names.clear();
                category_names.add("Select Category");
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    category_names.add(dataSnapshot.getKey().toString());
                    Log.i("TAG", dataSnapshot.getKey().toString());
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}