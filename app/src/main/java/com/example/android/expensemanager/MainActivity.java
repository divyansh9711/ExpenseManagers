package com.example.android.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
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
import java.util.Arrays;
import java.util.Collections;

import DbSchema.UserStructure;

public class MainActivity extends AppCompatActivity implements ListItemAdapter.OnClickListener {

    private RecyclerView mMessageListView;
    FloatingActionButton plus;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private  ListItemAdapter listItemAdapter;
    private ChildEventListener mChildEventListener;
    private ProgressBar mProgressbar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthState;
    public static final int RC_SIGN_IN=1;
    private String mUsername;
    private TextView mIncome;
    private TextView mExpense;
    private TextView mTotal;
    private ArrayList<ExpenseItem> eItems;
    int expense,income,total,food,transp,entert,health,apparel,household,other,stotal;
    private Button mSplit;
    private TextView mBlank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        eItems = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        mUsername = "Anonymous";
        plus = findViewById(R.id.add);
        mProgressbar = findViewById(R.id.progress_bar);
        mIncome = findViewById(R.id.income_tool);
        mExpense = findViewById(R.id.expenses_tool);
        mSplit = findViewById(R.id.split);
        mTotal = findViewById(R.id.total_tool);
        mBlank=findViewById(R.id.blank_text);
        setExpense();
        listItemAdapter = new ListItemAdapter(MainActivity.this,eItems,this);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo == null ){
            mProgressbar.setVisibility(View.GONE);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        mMessageListView = findViewById(R.id.list);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAmount = new Intent(getApplicationContext(),AddActivity.class);
                addAmount.putExtra("income",income);
                addAmount.putExtra("expenses",expense);
                addAmount.putExtra("total",total);
                startActivity(addAmount);
            }
        });

        mSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SplitActivity.class);
                startActivity(i);
            }
        });

        mAuthState = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user =firebaseAuth.getCurrentUser();

                if(user != null){
                    String photo = user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString();
                    myRef.child("expenses").child("users").child(LoginActivity.convertEmail(user.getEmail())).setValue(new UserStructure(user.getDisplayName(), user.getEmail(), photo));
                    onSignin(user.getUid());
                }
                else{
                    onSignout();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }
    public static String convertEmail(String email){
        return email.replace(".", ",");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        expense = 0; income = 0;
        food=0;transp=0;entert=0;health=0;apparel=0;household=0;total=0;other=0;
        mFirebaseAuth.addAuthStateListener(mAuthState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthState != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthState);
            detachReadlistener();
        }
    }

    private void onSignin(String username){
        mUsername = username;
        attachReadlistener();
    }

    private void onSignout(){
        mUsername = "Anonymous";
        detachReadlistener();
    }

    private void attachReadlistener(){


        myRef.child(mUsername).child("Expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eItems.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ExpenseItem items = snapshot.getValue(ExpenseItem.class);

                    items.setId(snapshot.getKey());
                    eItems.add(items);
                    setExpense();
                    if(items.getCategory().equals("Food")){food = food + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Transportation")){transp = transp + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Entertainment")){entert = entert + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Health")){health = health + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Apparel")){apparel = apparel + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Household")){household = household + Integer.parseInt(items.getAmount());}
                    else if(items.getCategory().equals("Other")){ other = other + Integer.parseInt(items.getAmount());}
                    stotal = food+transp+entert+health+apparel+household+other;


                    //mListAdapter.add(items);
                }
                listItemAdapter.addAll(eItems);
                mMessageListView.setLayoutManager(new LinearLayoutManager(MainActivity.this,1,false));
                mMessageListView.setAdapter(listItemAdapter);
                mProgressbar.setVisibility(View.GONE);

                if(listItemAdapter.getItemCount()==0){
                    mBlank.setVisibility(View.VISIBLE);
                }
                else {
                    mBlank.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setExpense(){
        food=0;transp=0;entert=0;health=0;apparel=0;household=0;total=0;other=0;
        income = 0;
        total = 0;
        expense = 0;
        for(ExpenseItem items : eItems){
            if(items.getCategory().equals("Amount Added")){
                income = income + Integer.parseInt(items.getAmount());
            }else{expense = expense + Integer.parseInt(items.getAmount());}
            total = income - expense;
            if(items.getCategory().equals("Food")){food = food + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Transportation")){transp = transp + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Entertainment")){entert = entert + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Health")){health = health + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Apparel")){apparel = apparel + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Household")){household = household + Integer.parseInt(items.getAmount());}
            else if(items.getCategory().equals("Other")){ other = other + Integer.parseInt(items.getAmount());}

        }
        stotal = food+transp+entert+health+apparel+household+other;

        mExpense.setText(Integer.toString(expense));
        mIncome.setText(Integer.toString(income));
        mTotal.setText(Integer.toString(total));

    }

    private void detachReadlistener(){
        if(mChildEventListener != null) {
            myRef.child(mUsername).child("Expenses").removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.stats:
                Intent i = new Intent(getApplicationContext(),StatActivity.class);
                i.putExtra("food",food);
                i.putExtra("transp",transp);
                i.putExtra("entert",entert);
                i.putExtra("health",health);
                i.putExtra("apparel",apparel);
                i.putExtra("house",household);
                i.putExtra("other",other);
                i.putExtra("total",stotal);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OnClick(int position) {
        if(position >= 0) {
            myRef.child(mUsername).child("Expenses").child(eItems.get(position).getId()).removeValue();
//
            eItems.remove(eItems.get(position));

            setExpense();
            listItemAdapter.addAll(eItems);
        }
    }
}
