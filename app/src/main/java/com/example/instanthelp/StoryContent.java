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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

    DatabaseReference storiesDBRef, categoriesDBRef;
    ArrayList<String> categoryList = new ArrayList<>();

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

                showUpdateDialog( );
                // return false;
            }
        });


    }

    private void showUpdateDialog( ){
        AlertDialog mDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.add_story_dialog, null);
        mDialog.setView(mDialogView);

        //create views references
        EditText etUpdateStory = mDialogView.findViewById(R.id.etUpdateStory);
        Spinner categorySpinner = mDialogView.findViewById(R.id.categorySpinner);
        Button btnUpdate = mDialogView.findViewById(R.id.btnAdd);

        mDialog.setTitle("Add a Story");


        categoriesDBRef = FirebaseDatabase.getInstance().getReference("Stories");

        categoriesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();

                for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){

                    String categories = storyDatasnap.getKey().toString();
                    categoryList.add(categories);
                }

                //call the spinner method

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(StoryContent.this, android.R.layout.simple_spinner_item, categoryList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we will update data in database
                //now get values from view

                String newStory = etUpdateStory.getText().toString();
                String newCategory = categorySpinner.getSelectedItem().toString();

               //save stories
                DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Stories").child(newCategory);
                Stories stories = new Stories("story", newStory);
                DbRef.setValue(stories);
                Toast.makeText(StoryContent.this, "Story Added Successfully",Toast.LENGTH_SHORT).show();
                //kill dialog
                mDialog.dismiss();
            }
        });
    }

}