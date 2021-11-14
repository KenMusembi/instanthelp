package com.example.instanthelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        //set itemLong listener on listview item

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Questions questions = questionsList.get(position);
                showUpdateDialog( questions.getQuestion());
                return false;
            }
        });




    }
    private void showUpdateDialog( String question){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog, null);
        mDialog.setView(mDialogView);

        //create views references
        EditText etUpdateQuestion = mDialogView.findViewById(R.id.etUpdateQuestion);
        Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);

        mDialog.setTitle("Updating " + question + " question");

        mDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we will update data in database
                //now get values from view

                String newQuestion = etUpdateQuestion.getText().toString();
                updateData( newQuestion);
                Toast.makeText(RetrieveDataActivity.this, "Question Updated",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void updateData( String question){
        //creating database reference
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Questions").child(question);
        Questions questions = new Questions(  question);
        DbRef.setValue(questions);
    }
}
