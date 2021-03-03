package com.rakeshsdetautomation.cricpredict.loginandregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rakeshsdetautomation.cricpredict.BaseActivity;
import com.rakeshsdetautomation.cricpredict.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {

    private EditText emailEdit;
    private Button resetPassword;
    private Button backToLoginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        String email = getIntent().getStringExtra("email");

        emailEdit = (EditText) findViewById(R.id.forgot_password_email);
        resetPassword = (Button) findViewById(R.id.forgot_password_reset_button);
        backToLoginButton = (Button) findViewById(R.id.forgot_password_backtologin_button);
        progressBar = (ProgressBar) findViewById(R.id.forgot_password_progress_bar);

        emailEdit.setText(email);

        resetPassword.setOnClickListener(this);
        backToLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password_reset_button:
                resetPassword();
                break;
            case R.id.forgot_password_backtologin_button:
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                break;
        }
    }

    private void resetPassword() {

        String email = emailEdit.getText().toString().trim();
        if(email.isEmpty()){
            emailEdit.setError("Email cannot be empty");
            emailEdit.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Email is not correct");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ForgotPassword.this, "Try again! Something went wrong", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}