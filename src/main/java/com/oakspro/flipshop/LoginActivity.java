package com.oakspro.flipshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 101;
    Button googleBtn, loginBtn;
    EditText username, password;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference docRef;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set id

        googleBtn=findViewById(R.id.googlelink);
        loginBtn=findViewById(R.id.login);
        username=findViewById(R.id.usernameEd);
        password=findViewById(R.id.passwordEd);

        //instance
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname=username.getText().toString();
                final String upass=password.getText().toString();

                if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(upass)){

                    docRef=firebaseFirestore.collection("users").document(uname);

                    if (!(docRef ==null)){
                        docRef.addSnapshotListener(LoginActivity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String acct_password=value.getString("Password");
                                if (upass.equals(acct_password)){
                                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent.putExtra("uname", value.getString("Name"));
                                    intent.putExtra("uemail", value.getString("Email"));
                                    startActivity(intent);

                                }else {
                                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Please enter Valid Email", Toast.LENGTH_SHORT).show();
                    }





                }
                else {
                    Toast.makeText(LoginActivity.this, "Either username or password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signIn();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "error: "+e.toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("uname", user.getDisplayName());
        intent.putExtra("uemail", user.getEmail());
        startActivity(intent);
    }

    public void OpenSignup(View view) {

        Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}