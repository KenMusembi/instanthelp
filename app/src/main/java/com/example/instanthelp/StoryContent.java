package com.example.instanthelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoryContent extends AppCompatActivity {


    FirebaseAuth mAuth;

    ListView storiesListView;
    List<String> storiesList;

    DatabaseReference storiesDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_content);

        //get the category name to pass on later
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("category_name");

        Toolbar myToolbar = findViewById(R.id.my_toolbar_stories);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

        storiesListView = findViewById(R.id.storiesListView);
        storiesList = new ArrayList<String>();

        storiesDBRef = FirebaseDatabase.getInstance().getReference("Stories").child(categoryName);

        storiesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storiesList.clear();

                for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){
                    String stories = storyDatasnap.getValue().toString();
                    storiesList.add(stories);
                }

                StoryContentListAdapter adapter = new StoryContentListAdapter(StoryContent.this, storiesList);
                storiesListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton fab = findViewById(R.id.fabAddStory);
        fab.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                showUpdateDialog( );
                // return false;
            }
        });


    }

    private void showUpdateDialog( ){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog, null);
        mDialog.setView(mDialogView);

        //create views references
        EditText etUpdateStory = mDialogView.findViewById(R.id.etUpdateStory);
        Button btnUpdate = mDialogView.findViewById(R.id.btnAdd);

        mDialog.setTitle("Add a Story");

        mDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we will update data in database
                //now get values from view

                String newStory = etUpdateStory.getText().toString();
                updateData( newStory);
                //kill dialog
            }
        });
    }
    //only add category while adding stories
    private void updateData( String story){
        //creating database reference
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Stories").child(story);
        Stories stories = new Stories(story, null);
        DbRef.setValue(stories);
        Toast.makeText(StoryContent.this, "Story Added Succesfully",Toast.LENGTH_SHORT).show();
    }

}