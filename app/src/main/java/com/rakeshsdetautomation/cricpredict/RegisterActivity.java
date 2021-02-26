package com.rakeshsdetautomation.cricpredict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditView;
    EditText passwordEditView;
    EditText nameEditView;
    Button registerButton;
    Button backToLoginButton;
    TextView registrationFormErrorTextView;
    TextView registrationSuccessTextView;
    String userIdForRegistration;
    String registeredUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userIdForRegistration = getIntent().getStringExtra("userIdForRegistration");
        emailEditView = (EditText) findViewById(R.id.register_email_edit_text_view);
        passwordEditView = (EditText) findViewById(R.id.register_password_edit_text_view);
        nameEditView = (EditText) findViewById(R.id.register_name_edit_text_view);
        registerButton = (Button) findViewById(R.id.registration_button);
        backToLoginButton = (Button) findViewById(R.id.registration_back_to_login_button);
        registrationFormErrorTextView = (TextView) findViewById(R.id.registration_form_error);
        registrationSuccessTextView = (TextView) findViewById(R.id.registration_success_text_view);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginScreenIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(loginScreenIntent);
            }
        });

        clearFields();
        setUserIdFromLogin();

    }


    private void register() throws JSONException, IOException {


        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(emailEditView.isFocused() || passwordEditView.isFocused() || nameEditView.isFocused())
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        String nameText = nameEditView.getText().toString();
        String emailText = emailEditView.getText().toString();
        String passwordText = passwordEditView.getText().toString();

        nameEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        emailEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        passwordEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        if(nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()){
            registrationFormErrorTextView.setText("Credentials cannot be empty!");
            if(nameText.isEmpty())
                nameEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            else if(emailText.isEmpty())
                emailEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            else
                passwordEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            return;
        }

        if((!emailText.contains("@")) || (!emailText.contains(".")) || (emailText.indexOf("@")>emailText.lastIndexOf("."))){
            registrationFormErrorTextView.setText("Email is not correct.");
            emailEditView.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            return;
        }

        String registrationUrl = BaseClass.serviceUrl + BaseClass.resourceRegistration;
        JSONObject registrationJson = new JSONObject();
        registrationJson.put("userId", emailText);
        registrationJson.put("name", nameText);
        registrationJson.put("password", passwordText);

        String participantString = BaseClass.postCall(registrationUrl, registrationJson.toString());
        System.out.println(participantString);
        if(BaseClass.serviceResponseCode == 201){
            clearFields();
            registrationSuccessTextView.setText("Registration Successful! Please login!");
            BaseClass.registeredUserId = emailText;
        }else if(BaseClass.serviceResponseCode == 409){
            registrationFormErrorTextView.setText("Email already exists! Try to login!");
        }else{
            registrationFormErrorTextView.setText("Server Error!");
        }

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
        registrationFormErrorTextView.setText("");
        registrationSuccessTextView.setText("");


    }

    private void setUserIdFromLogin(){
        emailEditView.setText(userIdForRegistration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //clearFields();
    }
}