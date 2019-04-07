package com.example.android.expensemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatActivity extends AppCompatActivity {

    private TextView mFood,mTransp,mEntert,mHealth,mApparel,mHousehold,mTotal,mOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent i = getIntent();

        String food = Integer.toString(i.getIntExtra("food",0));
        String transp = Integer.toString(i.getIntExtra("transp",0));
        String entert = Integer.toString(i.getIntExtra("entert",0));
        String health = Integer.toString(i.getIntExtra("health",0));
        String apparel = Integer.toString(i.getIntExtra("apparel",0));
        String house = Integer.toString(i.getIntExtra("house",0));
        String other = Integer.toString(i.getIntExtra("other",0));
        String total = Integer.toString(i.getIntExtra("total",0));

        mFood = findViewById(R.id.food_stat);
        mTransp = findViewById(R.id.transp_stat);
        mEntert = findViewById(R.id.entertainment_stat);
        mHealth = findViewById(R.id.health_stat);
        mApparel = findViewById(R.id.apparel_stat);
        mHousehold = findViewById(R.id.house_stat);
        mOther = findViewById(R.id.other_stat);
        mTotal = findViewById(R.id.total_stat);

        mFood.setText(food);
        mTransp.setText(transp);
        mEntert.setText(entert);
        mHealth.setText(health);
        mApparel.setText(apparel);
        mHousehold.setText(house);
        mOther.setText(other);
        mTotal.setText(total);

    }
}
