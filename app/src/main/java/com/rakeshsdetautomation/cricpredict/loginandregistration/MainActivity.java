package com.rakeshsdetautomation.cricpredict.loginandregistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.annotations.SerializedName;
import com.rakeshsdetautomation.cricpredict.R;
import com.rakeshsdetautomation.cricpredict.RegisterActivity;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;

    private EditText user;
    private EditText pass;
    private TextView registerButton;
    private Button loginButton;
    private com.google.android.gms.common.SignInButton googleSignInButton;
    private View banner;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        BaseClass.mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        registerButton = findViewById(R.id.login_page_registration_button);
        loginButton = (Button) findViewById(R.id.login_button);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        banner = findViewById(R.id.banner);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        forgotPassword = (TextView) findViewById(R.id.login_page_forgot_password);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
        banner.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //olg login method
        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTaskRunner loginAsyncTaskRunner = new LoginAsyncTaskRunner();
                loginAsyncTaskRunner.execute();
            }
        });*/

        clearFields();
        fillUpRegisteredUser();

    }

    @Override
    public void onClick(View v) {
        String email;
        switch (v.getId()){
            case R.id.login_button:
                userLogin();
                break;
            case R.id.login_page_registration_button:
                //register
                email = user.getText().toString();
                startActivity(new Intent(this, RegisterActivity.class).putExtra("userIdForRegistration", email));
                break;
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.login_page_forgot_password:
                email = user.getText().toString();
                startActivity(new Intent(this, ForgotPassword.class).putExtra("email", email));
                break;
        }
    }

    private void userLogin() {

        final String email = user.getText().toString().trim();
        String password = pass.getText().toString();

        if(email.isEmpty()){
            user.setError("Email cannot be empty");
            user.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            user.setError("Email is incorrect");
            return;
        }else if(password.isEmpty()){
            pass.setError("Password cannot be empty");
            pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
                                homeScreenIntent.putExtra("username", email);
                                startActivity(homeScreenIntent);
                            }else{
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                            }


                        }else{
                            Toast.makeText(MainActivity.this, "Failed to login. Check your credentials", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void googleSignIn() {
        Intent signInIntent = BaseClass.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    //old login logic
    /*private class LoginAsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Logging in...");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                onUserLoginButtonClick();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }


    public void onUserLoginButtonClick() throws JSONException, IOException {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (user.isFocused() || pass.isFocused())
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String username_text = user.getText().toString();
        String password_text = pass.getText().toString();

        if(username_text.isEmpty()){
            user.setError("Email cannot be empty");
            user.requestFocus();
            return;
        }else if(password_text.isEmpty()){
            pass.setError("Password cannot be empty");
            pass.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(password_text).matches()){
            user.setError("Email is incorrect");
            user.requestFocus();
            return;
        }

        BaseClass.serviceResponseCode = 0;
        String userString = loginPost(BaseClass.serviceUrl + BaseClass.resourceLogin,
                new JSONObject().put("userId", username_text).put("password", password_text).toString());
        System.out.println(userString);
        if (BaseClass.serviceResponseCode == 202) {

            //Store userid permanently
            SharedPreferences sp = getSharedPreferences(BaseClass.mySharedPrefName, MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("userId",username_text);
            edit.putBoolean("loggedIn", true);
            edit.apply();

            BaseClass.userString = BaseClass.convertStringToJson(userString);
            BaseClass.userId_notification = username_text;
            if (BaseClass.userString == null) {
                Toast.makeText(this, "Server Error!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
            homeScreenIntent.putExtra("username", username_text);
            startActivity(homeScreenIntent);
        } else {
            Toast.makeText(this, "Server Error!", Toast.LENGTH_LONG).show();
        }


        //getToken();

    }


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    

    String loginPost(String url, String json) throws IOException {
        try {
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                BaseClass.serviceResponseCode = response.code();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Login failed due to server error.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }*/


    private void fillUpRegisteredUser() {
        user.setText(BaseClass.registeredUserId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearFields();
        fillUpRegisteredUser();
    }

    private void clearFields() {
        user.clearFocus();
        pass.clearFocus();
        user.setText("");
        pass.setText("");
        BaseClass.clearAllValues();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
            homeScreenIntent.putExtra("username", account.getEmail());
            startActivity(homeScreenIntent);
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            if(user.isEmailVerified()){
                Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
                homeScreenIntent.putExtra("username", user.getEmail());
                startActivity(homeScreenIntent);
                return;
            }else{
                Intent homeScreenIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(homeScreenIntent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
            homeScreenIntent.putExtra("username", account.getEmail());
            startActivity(homeScreenIntent);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult: failed code=" +  e.getStatusCode());
            //Pending: Update UI for error in sign in
        }
    }
}

