package com.example.android.expensemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {

    private TextView mFood,mTransp,mEntert,mHealth,mApparel,mHousehold,mTotal,mOther;
    PieChart mPiechart;
    private float f,t,e,h,a,g,o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        f=0;t=0;e=0;h=0;a=0;g=0;o=0;

        Intent i = getIntent();

        String food = Integer.toString(i.getIntExtra("food",0));
        String transp = Integer.toString(i.getIntExtra("transp",0));
        String entert = Integer.toString(i.getIntExtra("entert",0));
        String health = Integer.toString(i.getIntExtra("health",0));
        String apparel = Integer.toString(i.getIntExtra("apparel",0));
        String house = Integer.toString(i.getIntExtra("house",0));
        String other = Integer.toString(i.getIntExtra("other",0));
        String total = Integer.toString(i.getIntExtra("total",0));

        f = i.getIntExtra("food",0);
        t = i.getIntExtra("transp",0);
        e = i.getIntExtra("entert",0);
        h = i.getIntExtra("health",0);
        a = i.getIntExtra("apparel",0);
        g = i.getIntExtra("house",0);
        o = i.getIntExtra("other",0);

        mFood = findViewById(R.id.food_stat);
        mTransp = findViewById(R.id.transp_stat);
        mEntert = findViewById(R.id.entertainment_stat);
        mHealth = findViewById(R.id.health_stat);
        mApparel = findViewById(R.id.apparel_stat);
        mHousehold = findViewById(R.id.house_stat);
        mOther = findViewById(R.id.other_stat);
        mTotal = findViewById(R.id.total_stat);
        mPiechart = findViewById(R.id.pie_chart);

        mPiechart.setRotationEnabled(true);
        mPiechart.setHoleRadius(50);
        mPiechart.setCenterText("Money Spent on");
        mPiechart.setCenterTextSize(10);

        mFood.setText(food);
        mTransp.setText(transp);
        mEntert.setText(entert);
        mHealth.setText(health);
        mApparel.setText(apparel);
        mHousehold.setText(house);
        mOther.setText(other);
        mTotal.setText(total);

        ArrayList<PieEntry> entry = new ArrayList<>();

        if(f>0){
        entry.add(new PieEntry(f,"Food"));}
        if(t>0){
        entry.add(new PieEntry(t,"Transportation"));}
        if(e>0){
        entry.add(new PieEntry(e,"Entertainment"));}
        if(h>0){
        entry.add(new PieEntry(h,"Health"));}
        if(a>0){
        entry.add(new PieEntry(a,"Apparel"));}
        if(g>0){
        entry.add(new PieEntry(g,"Household"));}
        if(o>0){
        entry.add(new PieEntry(o,"Other"));}

        PieDataSet set = new PieDataSet(entry, "");
        PieData data = new PieData(set);
        set.setSliceSpace(2);
        set.setValueTextSize(0);

        set.setColors(new int[]{R.color.expense,R.color.Green,R.color.Blue,R.color.purple,R.color.brown,R.color.orange,R.color.yellow});
        mPiechart.setData(data);
        mPiechart.invalidate();

        Legend legend = mPiechart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

    }


}
