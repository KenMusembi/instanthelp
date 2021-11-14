package com.example.instanthelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    FirebaseAuth mAuth;

    EditText etAskQuestion;
    Button buttonSubmitQuestion;
    Button buttonBrowseQuestion;
    TextView textViewReply;

    List<TextMessage> conversation;
    String userUID="123456";//on production app its come from user uid
    SmartReplyGenerator smartReplyGenerator;
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
        textViewReply = findViewById(R.id.tvReply);

        conversation = new ArrayList<>();
        smartReplyGenerator = SmartReply.getClient();



        questionDBRef = FirebaseDatabase.getInstance().getReference().child("Questions");

        buttonSubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View v) {
                //insertQuestionData();
                String message = etAskQuestion.getText().toString().trim();
                conversation.add(TextMessage.createForRemoteUser(message, System.currentTimeMillis(), userUID));
                smartReplyGenerator.suggestReplies(conversation).addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(@NonNull SmartReplySuggestionResult smartReplySuggestionResult) {
                        if(smartReplySuggestionResult.getStatus()==SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE){
                            //the conversations language is not supported
                            //the result does not contain any suggestions
                            textViewReply.setText("No Reply");
                        }else if(smartReplySuggestionResult.getStatus()==SmartReplySuggestionResult.STATUS_SUCCESS){
                            String reply ="";
                            for(SmartReplySuggestion suggestion:smartReplySuggestionResult.getSuggestions()){
                                reply= reply + suggestion.getText()+"\n";
                                textViewReply.setText(reply);
                            }
                        } else {
                            textViewReply.setText("Sorry we do not have an answer to that yet.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textViewReply.setText("Error :"+e.getMessage());
                    }
                });


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
        Questions questions = new Questions( question);

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