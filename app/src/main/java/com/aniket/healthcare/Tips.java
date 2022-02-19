package com.aniket.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Tips extends AppCompatActivity {

    String str[]={"Earthquake", "Flood", "Cyclone"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        ListView l= (ListView)findViewById(R.id.listView);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,str);
        l.setAdapter(adapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(Tips.this,"clicked "+i,Toast.LENGTH_SHORT).show();
                if(i==0)
                {
                    Intent intSignUp = new Intent(Tips.this, activity_earthquake.class);
                    startActivity(intSignUp);

                }
                else if(i==1)
                {
                    Intent intSignUp = new Intent(Tips.this, flood.class);
                    startActivity(intSignUp);


                }
                else if(i==2)
                {
                    Intent intSignUp = new Intent(Tips.this, cyclone.class);
                    startActivity(intSignUp);

                }
                else{

                }
            }
        });

    }

    public void function(View v){
//        Intent i=new Intent(Tips.this,userMap.class);

    }
}