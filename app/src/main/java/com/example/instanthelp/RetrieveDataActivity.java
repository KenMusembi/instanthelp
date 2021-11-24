package com.example.instanthelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class RetrieveDataActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ListView myListView;
    List<Questions> questionsList;

    DatabaseReference questionsDBRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_stories);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stories_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // User chose the "Settings" item, show the app settings UI...
                //setContentView(R.layout.questions_asked);
                startActivity(new Intent(RetrieveDataActivity.this, MainActivity.class));
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //setContentView(R.layout.activity_login);
                startActivity(new Intent(RetrieveDataActivity.this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null){
//            startActivity(new Intent(RetrieveDataActivity.this, LoginActivity.class));
//        }
//    }
}
