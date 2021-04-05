package com.rakeshsdetautomation.cricpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.MainActivity;
import com.rakeshsdetautomation.cricpredict.loginandregistration.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import android.util.Patterns;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    EditText emailEditView;
    EditText passwordEditView;
    EditText nameEditView;
    Button registerButton;
    Button backToLoginButton;
    String userIdForRegistration;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private View banner;
    final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userIdForRegistration = getIntent().getStringExtra("userIdForRegistration");
        emailEditView = (EditText) findViewById(R.id.register_email_edit_text_view);
        passwordEditView = (EditText) findViewById(R.id.register_password_edit_text_view);
        nameEditView = (EditText) findViewById(R.id.register_name_edit_text_view);
        registerButton = (Button) findViewById(R.id.registration_button);
        backToLoginButton = (Button) findViewById(R.id.registration_back_to_login_button);
        progressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        banner = findViewById(R.id.banner);

        emailEditView.setText(userIdForRegistration);

        registerButton.setOnClickListener(this);
        backToLoginButton.setOnClickListener(this);
        banner.setOnClickListener(this);

    }


    private void register()  {


        final String nameText = nameEditView.getText().toString().trim();
        final String emailText = emailEditView.getText().toString().trim();
        String passwordText = passwordEditView.getText().toString();

        if(nameText.isEmpty()){
            nameEditView.setError("Enter the name");
            nameEditView.requestFocus();
            return;
        }else if(emailText.isEmpty()){
            emailEditView.setError("Enter the email");
            emailEditView.requestFocus();
            return;
        }else if(passwordText.isEmpty()){
            passwordEditView.setError("Enter the password");
            passwordEditView.requestFocus();
            return;
        }else if(passwordText.length() < 6){
            passwordEditView.setError("Password should be of at least 6 characters");
            passwordEditView.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            emailEditView.setError("Email is not correct");
            emailEditView.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(nameText, emailText);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                String registrationUrl = BaseClass.serviceUrl + BaseClass.resourceRegistration;
                                                JSONObject registrationJson = new JSONObject();
                                                try {
                                                    registrationJson.put("userId", emailText);
                                                    registrationJson.put("name", nameText);
                                                    registrationJson.put("password", "");
                                                    String participantString = BaseClass.postCall(registrationUrl, registrationJson.toString());
                                                    System.out.println(participantString);
                                                } catch (JSONException | IOException e) {
                                                    e.printStackTrace();
                                                }

                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Failed to registered USER. Try again", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else {
                            try{
                                throw task.getException();
                            }catch (Exception e){
                                if(e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(RegisterActivity.this, "Email already exists. Try forgot password.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    return;
                                }
                            }

                            Toast.makeText(RegisterActivity.this, "Failed to registered. Try again", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);}
                    }
                });


        //old registration logic
        /*String registrationUrl = BaseClass.serviceUrl + BaseClass.resourceRegistration;
        JSONObject registrationJson = new JSONObject();
        registrationJson.put("userId", emailText);
        registrationJson.put("name", nameText);
        registrationJson.put("password", passwordText);

        String participantString = BaseClass.postCall(registrationUrl, registrationJson.toString());
        System.out.println(participantString);
        if(BaseClass.serviceResponseCode == 201){
            clearFields();
            //registrationSuccessTextView.setText("Registration Successful! Please login!");
            BaseClass.registeredUserId = emailText;
        }else if(BaseClass.serviceResponseCode == 409){
            //registrationFormErrorTextView.setText("Email already exists! Try to login!");
        }else{
            //registrationFormErrorTextView.setText("Server Error!");
        }*/

    }

    private void clearFields(){
        nameEditView.setText("");
        passwordEditView.setText("");
        emailEditView.setText("");
        nameEditView.clearFocus();
        passwordEditView.clearFocus();
        emailEditView.clearFocus();
        nameEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        passwordEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        emailEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);


    }

    private void setUserIdFromLogin(){
        emailEditView.setText(userIdForRegistration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //clearFields();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registration_button:
                register();
                break;
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registration_back_to_login_button:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}