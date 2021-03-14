package com.rakeshsdetautomation.cricpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;

import org.json.JSONObject;

public class SuccessfulPredictionActivity extends BaseActivity implements View.OnClickListener {

    TextView screen_title;
    TextView batsman1_text_view;
    TextView batsman2_text_view;
    TextView bowler1_text_view;
    TextView bowler2_text_view;
    TextView mom1_text_view;
    TextView mom2_text_view;
    TextView match_winner_text_view;
    TextView toss_winner_text_view;
    Button edit_prediction_button;

    int matchId;

    String batsman1;
    String batsman2;
    String bowler1;
    String bowler2;
    String mom1;
    String mom2;
    String matchWinner;
    String tossWinner;

    String team1Name;
    String team2Name;
    boolean timeoutForPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_prediction);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        batsman1_text_view = (TextView) findViewById(R.id.successful_prediction_batsman_1);
        batsman2_text_view = (TextView) findViewById(R.id.successful_prediction_batsman_2);
        bowler1_text_view = (TextView) findViewById(R.id.successful_prediction_bowler_1);
        bowler2_text_view = (TextView) findViewById(R.id.successful_prediction_bowler_2);
        mom1_text_view = (TextView) findViewById(R.id.successful_prediction_mom_1);
        mom2_text_view = (TextView) findViewById(R.id.successful_prediction_mom_2);
        match_winner_text_view = (TextView) findViewById(R.id.successful_prediction_match_winner);
        toss_winner_text_view = (TextView) findViewById(R.id.successful_prediction_toss_winner);
        screen_title = (TextView) findViewById(R.id.successful_prediction_screen_title);
        edit_prediction_button = (Button) findViewById(R.id.successful_prediction_edit_prediction_button);

        team1Name = getIntent().getStringExtra("team1Name");
        team2Name = getIntent().getStringExtra("team2Name");
        timeoutForPrediction = getIntent().getBooleanExtra("timeoutForPrediction", false);
        screen_title.setText(team1Name + " vs " + team2Name);

        matchId = getIntent().getIntExtra("matchId", 11111);

        //fillUpPredictionForMatchId();

        edit_prediction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPredition();
            }
        });

        FillUpSuccessfulPredictionAsyncTask fillUpSuccessfulPredictionAsyncTask = new FillUpSuccessfulPredictionAsyncTask();
        fillUpSuccessfulPredictionAsyncTask.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
        }
    }

    private void editPredition(){
        try{
            if(timeoutForPrediction == true){
                Toast.makeText(this, "Prediction time is over!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent predictionIntent = new Intent(SuccessfulPredictionActivity.this, PredictionActivity.class);
            predictionIntent.putExtra("matchId", matchId);
            predictionIntent.putExtra("team1Name", team1Name);
            predictionIntent.putExtra("team2Name", team2Name);
            predictionIntent.putExtra("editMode", true);

            //For prefilling the selected options in case of editing
            String predictionResourceUrl = BaseClass.resourceParticipantPrediction +
                    BaseClass.userString.getString("userId") + "/" + matchId;

            String predictionString = BaseClass.getCall(BaseClass.serviceUrl + predictionResourceUrl);

            JSONObject predictionJsonObj = null;
            predictionJsonObj = new JSONObject(predictionString);
            predictionIntent.putExtra("batsmen", predictionJsonObj.getString("batsmen"));
            predictionIntent.putExtra("bowlers", predictionJsonObj.getString("bowlers"));
            predictionIntent.putExtra("menOfTheMatch", predictionJsonObj.getString("menOfTheMatch"));
            predictionIntent.putExtra("matchWinner", predictionJsonObj.getString("matchWinner"));
            predictionIntent.putExtra("tossWinner", predictionJsonObj.getString("tossWinner"));


            startActivity(predictionIntent);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Server Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private class FillUpSuccessfulPredictionAsyncTask extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SuccessfulPredictionActivity.this, "Loading", "Loading your prediction");
        }

        @Override
        protected String doInBackground(String... strings) {

            fillUpPredictionForMatchId();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            batsman1_text_view.setText(batsman1);
            batsman2_text_view.setText(batsman2);
            bowler1_text_view.setText(bowler1);
            bowler2_text_view.setText(bowler2);
            mom1_text_view.setText(mom1);
            mom2_text_view.setText(mom2);
            match_winner_text_view.setText(matchWinner);
            toss_winner_text_view.setText(tossWinner);
            progressDialog.dismiss();
        }
    }


    private void fillUpPredictionForMatchId(){

        try{
            String predictionString = "";
            String predictionResourceUrl = "";

            predictionResourceUrl = BaseClass.resourceParticipantPrediction +
                    BaseClass.userString.getString("userId") + "/" + matchId;

            predictionString = BaseClass.getCall(BaseClass.serviceUrl + predictionResourceUrl);

            JSONObject predictionJsonObj = null;
            predictionJsonObj = new JSONObject(predictionString);

            String batsmen = predictionJsonObj.getString("batsmen");
            batsman1 = batsmen.split(",")[0].trim();
            batsman2 = batsmen.split(",")[1].trim();
            String bowlers = predictionJsonObj.getString("bowlers");
            bowler1 = bowlers.split(",")[0].trim();
            bowler2 = bowlers.split(",")[1].trim();
            String menOfTheMatch = predictionJsonObj.getString("menOfTheMatch");
            mom1 = menOfTheMatch.split(",")[0].trim();
            mom2 = menOfTheMatch.split(",")[1].trim();
            matchWinner = predictionJsonObj.getString("matchWinner");
            tossWinner =  predictionJsonObj.getString("tossWinner");


        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Server Error!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeScreenIntent = new Intent(SuccessfulPredictionActivity.this, HomeScreenActivity.class);
        homeScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityIfNeeded(homeScreenIntent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cricpredict_menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_link:
                logout();
                break;
            case R.id.milestones:
                milestones();
                break;
            case R.id.settings:
                //write code for settings
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}