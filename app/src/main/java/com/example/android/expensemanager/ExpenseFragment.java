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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ExpenseFragment extends Fragment {

    private Spinner category;
    private EditText mAmount;
    private EditText mDescription;
    private Button mAdd;
    private String mCategory;
    private SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
    private SimpleDateFormat tf = new SimpleDateFormat("h:mm a");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mFirebaseAuth;
    String username;
    DatabaseReference myRef = database.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add,container,false);
        category = rootView.findViewById(R.id.category_spinner);
        mAmount = rootView.findViewById(R.id.amount_edit);
        mDescription = rootView.findViewById(R.id.description_edit);
        mAdd = rootView.findViewById(R.id.add_button);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        username = user.getUid();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.categories,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                mCategory = category;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Select a category", Toast.LENGTH_SHORT).show();
            }
        });

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
                String date = df.format(Calendar.getInstance().getTime());
                String time = tf.format(Calendar.getInstance().getTime());
                ExpenseItem listitem = new ExpenseItem(mAmount.getText().toString(),mCategory,mDescription.getText().toString(),date,time);
                myRef.child(username).child("Expenses").push().setValue(listitem);
                Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Amount Added", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

}
