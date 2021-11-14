package com.example.instanthelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Button btnForgotPassword;
    TextView btnForgotPassword;
    TextInputEditText etForgotPasswordEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnForgotPassword = findViewById(R.id.btnForgotPassword);


        btnForgotPassword.setOnClickListener(view -> {

                etForgotPasswordEmail = findViewById(R.id.etForgotPasswordEmail);
                String email = etForgotPasswordEmail.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please input your email address", Toast.LENGTH_SHORT).show();
                } else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Password Reset Link successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                }

        });

    }
}