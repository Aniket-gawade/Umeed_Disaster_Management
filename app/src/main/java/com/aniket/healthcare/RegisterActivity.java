package com.aniket.healthcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.jar.Attributes;

public class RegisterActivity extends AppCompatActivity {

    EditText emailId, password, mobile, name, emr1, emr2, emr3;
    Button btnSignUp;
    TextView tvSignIn;
    ImageView gsignin;
    FirebaseAuth mFirebaseAuth;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog pd;
    private static final String TAG = "tag";
    private int RC_SIGN_IN = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        mobile= findViewById(R.id.editTextMobile);
        name = findViewById(R.id.editTextName);
        btnSignUp = findViewById(R.id.cirRegisterButton);
        tvSignIn = findViewById(R.id.Al_Reg);
        gsignin = findViewById(R.id.GsignIn);
        emr1 = findViewById(R.id.emr1);
        emr2 = findViewById(R.id.emr2);
        emr3 = findViewById(R.id.emr3);
        pd = new ProgressDialog(this);
        pd.setMessage("Sign in ....");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String mob = mobile.getText().toString();
                String nam = name.getText().toString();
                String em1 = emr1.getText().toString();
                String em2 = emr2.getText().toString();
                String em3 = emr3.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(mob.isEmpty()){
                    mobile.setError("Please enter your Mobile Number");
                    mobile.requestFocus();
                }
                else  if(nam.isEmpty()){
                    name.setError("Please enter your Name");
                    name.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty() && mob.isEmpty() && nam.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty() && mob.isEmpty() && nam.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                DbUser dbUser = new DbUser();
                                User user = new User(user_id,mob,em1,em2,em3,email)   ;

                                //(String userId,String mob,String emr1,String emr2,String emr3,String mail)

                                dbUser.add(user).addOnSuccessListener(suc ->
                                {
                                    Toast.makeText(RegisterActivity.this,user_id,Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(er->
                                {
                                    Toast.makeText(RegisterActivity.this,"Failed to add data",Toast.LENGTH_SHORT).show();
                                });


                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Error Occurred!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("278944410326-rfro3ljl2b2ff2iodhqrgv5khqd4gqk6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        gsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                signIn();
            }
        });
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    private void signIn(){
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

    private void updateUI(FirebaseUser fUser){

        Intent intToHome = new Intent(RegisterActivity.this,DashBoard.class);
        startActivity(intToHome);

    }


    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}