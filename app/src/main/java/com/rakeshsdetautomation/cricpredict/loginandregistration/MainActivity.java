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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;

    HashMap<String, String> credentials = new HashMap<String, String>();
    EditText user;
    EditText pass;
    TextView error;
    Button registerButton;
    String registeredUserId;

    String userIdForRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        BaseClass.mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.google_sign_in_button:
                        googleSignIn();
                        break;
                }
            }
        });


        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.credentials_error);
        registerButton = (Button) findViewById(R.id.login_page_registration_button);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTaskRunner loginAsyncTaskRunner = new LoginAsyncTaskRunner();
                loginAsyncTaskRunner.execute();
                /*final Login login;
                try {
                    login = new Login(MainActivity.this);
                    login.execute("http://ec2-18-191-155-71.us-east-2.compute.amazonaws.com:8080/cricpredictapi/services/v1/participants/login",
                            new JSONObject().put("userId","rakesh.xyzz@gmail.com").put("password","abc12345").toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationScreenInent = new Intent(MainActivity.this, RegisterActivity.class);
                userIdForRegistration = user.getText().toString();
                registrationScreenInent.putExtra("userIdForRegistration", userIdForRegistration);
                startActivity(registrationScreenInent);
            }
        });

        clearFields();
        fillUpRegisteredUser();

    }

    private void googleSignIn() {
        Intent signInIntent = BaseClass.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private class LoginAsyncTaskRunner extends AsyncTask<String, String, String> {

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
        /*EditText user = (EditText) findViewById(R.id.username);
        EditText pass = (EditText) findViewById(R.id.password);
        TextView error = (TextView) findViewById(R.id.credentials_error);*/

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (user.isFocused() || pass.isFocused())
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String username_text = user.getText().toString();
        String password_text = pass.getText().toString();

        if (username_text.isEmpty() || password_text.isEmpty()) {
            error.setText("Credentials cannot be empty!");
            if (username_text.isEmpty())
                user.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
            else
                pass.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
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
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                error.setText("Server Error!");
                return;
            }
            Intent homeScreenIntent = new Intent(MainActivity.this, HomeScreenActivity.class);
            homeScreenIntent.putExtra("username", username_text);
            startActivity(homeScreenIntent);
        } else {
            error.setText(new JSONObject(userString).getString("error"));
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
    }


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
        error.setText("");
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

   /* private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        System.out.println("debug---" + msg);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference fcmDatabaseRef = ref.child("FCM_Device_Tokens").push();

                        FCM_Device_Tokens obj = new FCM_Device_Tokens();
                        obj.setToken(token);
                        fcmDatabaseRef.setValue(obj);

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }



}

@IgnoreExtraProperties
class FCM_Device_Tokens {

    @SerializedName("token")
    private String token;

    public FCM_Device_Tokens() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}*/
