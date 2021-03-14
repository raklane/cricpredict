package com.rakeshsdetautomation.cricpredict;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;
import com.rakeshsdetautomation.cricpredict.loginandregistration.MainActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String KEY = "key";
    public static SharedPreferences sharedPreferences;



    protected void logout() {

        getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit().clear().commit();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mAuth.signOut();
            Intent mainScreenIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreenIntent);
            return;
        }

        BaseClass.mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent mainScreenIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainScreenIntent);
            }
        });
    }

    protected void milestones() {

        Intent milestonesIntent = new Intent(getApplicationContext(), MilestonesBoard.class);
        startActivity(milestonesIntent);

    }


}
