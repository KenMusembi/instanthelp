package com.example.instanthelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private ImageButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";

    //private RequestQueue mRequestQueue;

    private ArrayList<ChatsModel> chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.showContextMenu(1, 1);
        myToolbar.getMenu();

        myToolbar.inflateMenu(R.menu.main_menu);

        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();

        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);

        chatsModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });

    }
    private void getResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=161555&key=Bgc0Zn95r2hNj9md&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MSGModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MSGModel>() {
            @Override
            public void onResponse(Call<MSGModel> call, Response<MSGModel> response) {
                if(response.isSuccessful()){
                    MSGModel model = response.body();
                    chatsModelArrayList.add(new ChatsModel(model.getCnt(), BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MSGModel> call, Throwable t) {
                chatsModelArrayList.add(new ChatsModel(t.getMessage() , BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_stories:
                // User chose the "Settings" item, show the app settings UI...
                //setContentView(R.layout.questions_asked);
                startActivity(new Intent(MainActivity.this, RetrieveDataActivity.class));
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //setContentView(R.layout.activity_login);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }




}