package com.example.instanthelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    FirebaseAuth mAuth;

    EditText etAskQuestion;
    Button buttonSubmitQuestion;
    Button buttonBrowseQuestion;

    DatabaseReference questionDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        etAskQuestion = findViewById(R.id.etAskQuestion);
        buttonSubmitQuestion = findViewById(R.id.buttonSubmitQuestion);
        buttonBrowseQuestion = findViewById(R.id.buttonBrowseQuestion);

        questionDBRef = FirebaseDatabase.getInstance().getReference().child("Questions");

        buttonSubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertQuestionData();
            }
        });

        buttonBrowseQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RetrieveDataActivity.class));
                // setContentView(R.layout.activity_retrieve_data);
                //open the data retreiving activity using Intents
            }
        });

    }

    private void insertQuestionData() {
        String question = etAskQuestion.getText().toString();
        String answer = "Answer";

        Questions questions = new Questions(question, answer);

        questionDBRef.push().setValue(questions);
        Toast.makeText(MainActivity.this, "Question Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}