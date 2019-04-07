package com.example.android.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class IncomeFragment extends Fragment {

    private EditText mAmount;
    private EditText mDescription;
    private Button mAdd;
    private String amount;
    private String description;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mFirebaseAuth;
    String username;
    private SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
    private SimpleDateFormat tf = new SimpleDateFormat("h:mm a");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add_income,container,false);

        FirebaseApp.initializeApp(getActivity());

        mAmount = rootView.findViewById(R.id.amount_edit_income);
        mDescription = rootView.findViewById(R.id.description_edit_income);
        mAdd = rootView.findViewById(R.id.add_button_income);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        username = user.getUid();

        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() > 0) {
                    mAdd.setEnabled(true);
                } else {
                    mAdd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(mAmount.length() == 0){
            mAdd.setEnabled(false);
        }

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = mAmount.getText().toString();
                description = mDescription.getText().toString();
                String date = df.format(Calendar.getInstance().getTime());
                String time = tf.format(Calendar.getInstance().getTime());

                ExpenseItem listitem = new ExpenseItem(amount,description,date,time);
                myRef.child(username).child("Expenses").push().setValue(listitem);
                Toast.makeText(getActivity(), "Amount Added", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }


    }

