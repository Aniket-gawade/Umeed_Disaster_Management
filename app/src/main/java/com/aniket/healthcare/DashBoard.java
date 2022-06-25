package com.aniket.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aniket.healthcare.Login.LoginActivity;
import com.aniket.healthcare.Map.MapsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {

    //This changes is available at update_1 branch

    Button signout;
    TextView camera, doctor;
    CardView sos, map, aid, tips;
    FirebaseAuth mFirebaseAuth;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        signout = findViewById(R.id.cirLogOutButton);
        map = findViewById(R.id.map1);
        sos = findViewById(R.id.sos);
        aid = findViewById(R.id.first_aid);
        tips = findViewById(R.id.safety_tips);


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("278944410326-rfro3ljl2b2ff2iodhqrgv5khqd4gqk6.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(DashBoard.this,gso);
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashBoard.this, LoginActivity.class));
                finish();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashBoard.this, MapsActivity.class));
                finish();
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, sos.class));
            }
        });

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, Tips.class));
            }
        });

        aid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, aid_fragment.class));
            }
        });

    }
}