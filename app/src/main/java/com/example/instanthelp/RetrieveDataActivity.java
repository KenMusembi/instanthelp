package com.example.instanthelp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RetrieveDataActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ListView categoryListView;
    List<String> storiesList;

    DatabaseReference storiesDBRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        Toolbar myToolbar = findViewById(R.id.my_toolbar_stories);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

        categoryListView = findViewById(R.id.categoryListView);

        storiesList = new ArrayList<String>();

        storiesDBRef = FirebaseDatabase.getInstance().getReference("Stories");

        storiesDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storiesList.clear();
                for(DataSnapshot storyDatasnap : dataSnapshot.getChildren()){
                    String stories = storyDatasnap.getKey().toString();
                    storiesList.add(stories);
                }
                ListAdapter adapter = new ListAdapter(RetrieveDataActivity.this, storiesList);
                categoryListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set itemLong listener on listview item

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = storiesList.get(position);
                Intent intent = new Intent(RetrieveDataActivity.this, StoryContent.class);
                intent.putExtra("category_name", categoryName);
                startActivity( intent);
                return false;
            }
        });
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
                //setContentView(R.layout.categories_listed);
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
}
