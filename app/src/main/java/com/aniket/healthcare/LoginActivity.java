package com.aniket.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp, forgotPass, drLogin;
    ImageView gsign, fbSign;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog pd;
    GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.cirLoginButton);
        tvSignUp = findViewById(R.id.New_User);
        forgotPass = findViewById(R.id.forgot);
        gsign = findViewById(R.id.GsignIn);
        pd = new ProgressDialog(this);
        pd.setMessage("Sign in ....");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if( mFirebaseUser != null ){
////                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
////                    password.requestFocus();
////                    emailId.requestFocus();
//                    Intent i = new Intent(LoginActivity.this, DashBoard.class);
//                    startActivity(i);
//                    finish();
//                }
//                else{
////                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
//                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                    pd.dismiss();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                    pd.dismiss();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else  if(isValidEmail(email)){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                            else{
                                password.getText().clear();
                                emailId.getText().clear();
                                Intent intToHome = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intToHome);
                                finish();
                                pd.dismiss();
                            }
                        }
                    });
                }
                else{
                    emailId.setError("Please Check Your Email Id");
                    emailId.requestFocus();
                    pd.dismiss();

                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ResetPass = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(ResetPass);
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("278944410326-rfro3ljl2b2ff2iodhqrgv5khqd4gqk6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                signIn();
            }
        });



    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result back to the Facebook SDK
//        CallbackManager.onActivityResult(requestCode, resultCode, data);
//    }


    
    


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                pd.dismiss();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                pd.dismiss();
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                            pd.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                            pd.dismiss();
                        }
                    }
                });
    }

//    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
//        //check if the account is null
//        if (acct != null) {
//            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//            mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
////                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                        updateUI(user);
//                        pd.dismiss();
//                    } else {
////                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                        updateUI(null);
//                        pd.dismiss();
//                    }
//                }
//            });
//        }
//        else{
//            Toast.makeText(LoginActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void updateUI(FirebaseUser fUser){

        if(fUser != null) {
            Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intToHome);
            finish();
        }

    }


//    Intent x = new Intent(this, RegisterActivity.class);

//    public void Clicked(View View){
//        Intent i = new Intent(this, DashBoard.class);
//        startActivity(i);
//    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}