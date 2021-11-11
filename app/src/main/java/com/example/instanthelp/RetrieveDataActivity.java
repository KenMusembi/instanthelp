package com.example.instanthelp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class RetrieveDataActivity extends AppCompatActivity {

    ListView myListView;
    List<Questions> questionsList;

    DatabaseReference questionsDBRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        myListView = findViewById(R.id.myListView);
        questionsList = new ArrayList<>();

        questionsDBRef = FirebaseDatabase.getInstance().getReference("Questions");

        questionsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionsList.clear();



                for(DataSnapshot questionDatasnap : dataSnapshot.getChildren()){
                    Questions questions = questionDatasnap.getValue(Questions.class);
                    questionsList.add(questions);
                }

                ListAdapter adapter = new ListAdapter(RetrieveDataActivity.this, questionsList);
                myListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
