package com.example.android.expensemanager;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddActivity extends AppCompatActivity {

    private TextView mIncome;
    private TextView mExpense;
    private TextView mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        mIncome = findViewById(R.id.income_tool1);
        mExpense = findViewById(R.id.expenses_tool1);
        mTotal = findViewById(R.id.total_tool1);

        Intent i = getIntent();

        String income = Integer.toString(i.getIntExtra("income",0));
        String expense = Integer.toString(i.getIntExtra("expenses",0));
        String total = Integer.toString(i.getIntExtra("total",0));

        mIncome.setText(income);
        mExpense.setText(expense);
        mTotal.setText(total);

        ViewPager pager = findViewById(R.id.viewpager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.add_tabs);
        tab.setupWithViewPager(pager);

        View root = tab.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorAccent));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }
}
