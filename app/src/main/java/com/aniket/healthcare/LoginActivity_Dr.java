package com.aniket.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity_Dr extends AppCompatActivity {

    TextView userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dr);

        userLogin = findViewById(R.id.Login_User);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity_Dr.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
            }
        });
    }
}