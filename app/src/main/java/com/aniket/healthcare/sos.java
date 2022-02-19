package com.aniket.healthcare;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class sos extends AppCompatActivity {

    String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView callNo1,callNo2,callNo3;

    EditText message;
    Button send_btn,callBtn1,callBtn2,callBtn3;

    String contactNo1,contactNo2,contactNo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //messaging
        message = findViewById(R.id.message);
        send_btn = findViewById(R.id.send_btn);

        //calling
        callBtn1 = findViewById(R.id.callBtn1);
        callBtn2 = findViewById(R.id.callBtn2);
        callBtn3 = findViewById(R.id.callBtn3);

        callNo1 = findViewById(R.id.callNo1);
        callNo2 = findViewById(R.id.callNo2);
        callNo3 = findViewById(R.id.callNo3);


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(sos.this
                        ,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }
                else{
                    ActivityCompat.requestPermissions(sos.this,
                            new String[]{Manifest.permission.SEND_SMS},100);

                }
            }
        });


        callBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallButton1();
            }
        });

        callBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallButton2();
            }
        });

        callBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallButton3();
            }
        });

        Toast.makeText(sos.this,UserId,Toast.LENGTH_SHORT).show();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("User").orderByChild("userId").equalTo(UserId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    contactNo1 = data.child("emr1").getValue().toString();
                    contactNo2 = data.child("emr2").getValue().toString();
                    contactNo3 = data.child("emr3").getValue().toString();

                    callNo1.setText(contactNo1);
                    callNo2.setText(contactNo2);
                    callNo3.setText(contactNo3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(sos.this,"Failed to add data",Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void sendSMS(){
        //String phoneNo = phone.getText().toString().trim();

        //Get arrayList of phone numbers of user from DB

        String mssg = message.getText().toString().trim();

        SmsManager smsManager = SmsManager.getDefault();

        if(!contactNo1.equals("") && !mssg.equals("")){

            smsManager.sendTextMessage(contactNo1,null,mssg,null,null);

            Toast.makeText(getApplicationContext(),"SOS Message 1 sent successfully",Toast.LENGTH_LONG).show();
        }
        if(!contactNo2.equals("") && !mssg.equals("")){

            smsManager.sendTextMessage(contactNo2,null,mssg,null,null);

            Toast.makeText(getApplicationContext(),"SOS Message 2 sent successfully",Toast.LENGTH_LONG).show();
        }
        if(!contactNo3.equals("") && !mssg.equals("")){

            smsManager.sendTextMessage(contactNo3,null,mssg,null,null);

            Toast.makeText(getApplicationContext(),"SOS Message 3 sent successfully",Toast.LENGTH_LONG).show();
        }


        else{
            Toast.makeText(getApplicationContext(),"Failed to send message",Toast.LENGTH_LONG).show();
        }


    }

    private void CallButton1(){

        if(ContextCompat.checkSelfPermission(sos.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(sos.this,new String[] {Manifest.permission.CALL_PHONE},101);
        }
        else{

            String dial =  "tel:" + contactNo1;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }

    }

    private void CallButton2(){

        if(ContextCompat.checkSelfPermission(sos.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(sos.this,new String[] {Manifest.permission.CALL_PHONE},102);
        }
        else{
            String dial =  "tel:" + contactNo2;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }

    }

    private void CallButton3(){

        if(ContextCompat.checkSelfPermission(sos.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(sos.this,new String[] {Manifest.permission.CALL_PHONE},103);
        }
        else{
            String dial =  "tel:" + contactNo3;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }

        if(requestCode == 101 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            CallButton1();
        }

        if(requestCode == 102 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            CallButton2();
        }

        if(requestCode == 103 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            CallButton3();
        }

        else{
            //Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}