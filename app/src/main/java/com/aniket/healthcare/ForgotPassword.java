package com.aniket.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextView tvSignIn;
    Button resetPass;
    EditText emailId;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        tvSignIn = findViewById(R.id.cancelReset);
        resetPass = findViewById(R.id.cirResetButton);

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailId.getText().toString();

                if(isValidEmail(mail)){
                    mFirebaseAuth.sendPasswordResetEmail(mail)
                            .addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(ForgotPassword.this,"Reset Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ForgotPassword.this,"Check Your Email!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
                else{
                    emailId.setError("Please check email id");
                    emailId.requestFocus();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}