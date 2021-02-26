package com.rakeshsdetautomation.cricpredict;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PredictionActivity extends AppCompatActivity {

    Spinner batsman1_spinner_view;
    Spinner batsman2_spinner_view;
    Spinner bowler1_spinner_view;
    Spinner bowler2_spinner_view;
    Spinner manofthematch1_spinner_view;
    Spinner manofthematch2_spinner_view;
    Spinner match_winner_spinner_view;
    Spinner toss_winner_spinner_view;
    TextView prediction_screen_title_view;
    Button perdiction_submit_button_view;
    TextView prediction_error_text_view;

    int matchId;
    String team1Name;
    String team2Name;
    boolean editMode = false;
    boolean timeoutForPrediction = false;

    ArrayAdapter<String> adapterPlayers1;
    ArrayAdapter<String> adapterPlayers2;
    ArrayAdapter<String> teamAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchId = getIntent().getIntExtra("matchId", 11111);
        team1Name = getIntent().getStringExtra("team1Name");
        team2Name = getIntent().getStringExtra("team2Name");
        editMode = getIntent().getBooleanExtra("editMode", false);
        timeoutForPrediction = getIntent().getBooleanExtra("timeoutForPrediction", false);

        if(BaseClass.checkIfPredictionExists(PredictionActivity.this, matchId) && editMode == false){
            Toast.makeText(PredictionActivity.this, "You've already Predicted!", Toast.LENGTH_SHORT).show();
            Intent successfulPredictionIntent = new Intent(PredictionActivity.this, SuccessfulPredictionActivity.class);
            successfulPredictionIntent.putExtra("matchId", matchId);
            successfulPredictionIntent.putExtra("team1Name", team1Name);
            successfulPredictionIntent.putExtra("team2Name", team2Name);
            successfulPredictionIntent.putExtra("timeoutForPrediction", timeoutForPrediction);
            startActivity(successfulPredictionIntent);
        }


        setContentView(R.layout.activity_prediction);

        prediction_screen_title_view = (TextView) findViewById(R.id.prediction_screen_title);
        batsman1_spinner_view = (Spinner) findViewById(R.id.batsman_1_spinner);
        batsman2_spinner_view = (Spinner) findViewById(R.id.batsman_2_spinner);
        bowler1_spinner_view = (Spinner) findViewById(R.id.bowler_1_spinner);
        bowler2_spinner_view = (Spinner) findViewById(R.id.bowler_2_spinner);
        manofthematch1_spinner_view = (Spinner) findViewById(R.id.manofthematch_1_spinner);
        manofthematch2_spinner_view = (Spinner) findViewById(R.id.manofthematch_2_spinner);
        match_winner_spinner_view = (Spinner) findViewById(R.id.match_winning_team_spinner);
        toss_winner_spinner_view = (Spinner) findViewById(R.id.toss_winning_team_spinner);
        perdiction_submit_button_view = (Button) findViewById(R.id.prediction_submit_button);
        prediction_error_text_view = (TextView) findViewById(R.id.prediction_error);



        perdiction_submit_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPrediction();
            }
        });

        //fill_dropdowns();

        FillDropdownDataAyncTaskRunner fillDropdownDataAyncTaskRunner = new FillDropdownDataAyncTaskRunner();
        fillDropdownDataAyncTaskRunner.execute();

    }


    private class FillDropdownDataAyncTaskRunner extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PredictionActivity.this, "Loading", "Loading players");
        }

        @Override
        protected String doInBackground(String... strings) {

            fill_dropdowns();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            prediction_screen_title_view.setText(team1Name + " vs " + team2Name);

            batsman1_spinner_view.setAdapter(adapterPlayers1);
            bowler1_spinner_view.setAdapter(adapterPlayers1);
            manofthematch1_spinner_view.setAdapter(adapterPlayers1);

            batsman2_spinner_view.setAdapter(adapterPlayers2);
            bowler2_spinner_view.setAdapter(adapterPlayers2);
            manofthematch2_spinner_view.setAdapter(adapterPlayers2);

            match_winner_spinner_view.setAdapter(teamAdapter);
            toss_winner_spinner_view.setAdapter(teamAdapter);

            if(editMode == true){
                int spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("batsmen").split(",")[0]);
                batsman1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("batsmen").split(",")[1]);
                batsman2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("bowlers").split(",")[0]);
                bowler1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("bowlers").split(",")[1]);
                bowler2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("menOfTheMatch").split(",")[0]);
                manofthematch1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("menOfTheMatch").split(",")[1]);
                manofthematch2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = teamAdapter.getPosition(getIntent().getStringExtra("matchWinner"));
                match_winner_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = teamAdapter.getPosition(getIntent().getStringExtra("tossWinner"));
                toss_winner_spinner_view.setSelection(spinnerPosition);

            }

            progressDialog.dismiss();
        }
    }



    public void fill_dropdowns() {

        try{

            //prediction_screen_title_view.setText(team1Name + " vs " + team2Name);

            String resourceTeamsTeam1 = BaseClass.resourceTeam + team1Name;
            String resourceTeamsTeam2 = BaseClass.resourceTeam + team2Name;
            String team2String = "";

            //team 1
            String team1String = BaseClass.getCall(BaseClass.serviceUrl + resourceTeamsTeam1);
            JSONObject teamJsonObj = teamJsonObj = new JSONObject(team1String);
            JSONArray jsonArray = teamJsonObj.getJSONArray("players");

            List<String> playerList = new ArrayList<String>();
            for(int i=0; i<jsonArray.length(); i++){
                playerList.add(jsonArray.getString(i));
            }
            String[] playerStringArray = new String[playerList.size()];
            playerStringArray = playerList.toArray(playerStringArray);

            adapterPlayers1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerStringArray);
            /*batsman1_spinner_view.setAdapter(adapterPlayers1);
            bowler1_spinner_view.setAdapter(adapterPlayers1);
            manofthematch1_spinner_view.setAdapter(adapterPlayers1);*/

            //team 2
            team2String = BaseClass.getCall(BaseClass.serviceUrl + resourceTeamsTeam2);
            teamJsonObj = null;
            teamJsonObj = new JSONObject(team2String);
            jsonArray = null;
            jsonArray = teamJsonObj.getJSONArray("players");

            playerList = new ArrayList<String>();
            for(int i=0; i<jsonArray.length(); i++){
                playerList.add(jsonArray.getString(i));
            }
            playerStringArray = new String[playerList.size()];
            playerStringArray = playerList.toArray(playerStringArray);

            adapterPlayers2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerStringArray);
            /*batsman2_spinner_view.setAdapter(adapterPlayers2);
            bowler2_spinner_view.setAdapter(adapterPlayers2);
            manofthematch2_spinner_view.setAdapter(adapterPlayers2);*/

            String[] teamNameArray = {team1Name, team2Name};
            teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamNameArray);
            /*match_winner_spinner_view.setAdapter(teamAdapter);
            toss_winner_spinner_view.setAdapter(teamAdapter);*/

            /*if(editMode == true){
                int spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("batsmen").split(",")[0]);
                batsman1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("batsmen").split(",")[1]);
                batsman2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("bowlers").split(",")[0]);
                bowler1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("bowlers").split(",")[1]);
                bowler2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers1.getPosition(getIntent().getStringExtra("menOfTheMatch").split(",")[0]);
                manofthematch1_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = adapterPlayers2.getPosition(getIntent().getStringExtra("menOfTheMatch").split(",")[1]);
                manofthematch2_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = teamAdapter.getPosition(getIntent().getStringExtra("matchWinner"));
                match_winner_spinner_view.setSelection(spinnerPosition);
                spinnerPosition = teamAdapter.getPosition(getIntent().getStringExtra("tossWinner"));
                toss_winner_spinner_view.setSelection(spinnerPosition);

            }*/

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Server Error!", Toast.LENGTH_SHORT).show();
        }

    }





    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void submitPrediction(){

        try{

            //Check if prediction time is over
            if(BaseClass.checkIfPredictionTimeIsOver(matchId)){
                Toast.makeText(this, "Prediction time is over!", Toast.LENGTH_SHORT).show();
                Intent homeScreenIntent = new Intent(PredictionActivity.this, HomeScreenActivity.class);
                startActivity(homeScreenIntent);
                return;
            }

            String batsman1String = batsman1_spinner_view.getSelectedItem().toString().trim();
            String batsman2String = batsman2_spinner_view.getSelectedItem().toString().trim();
            String bowler1String = bowler1_spinner_view.getSelectedItem().toString().trim();
            String bowler2String = bowler2_spinner_view.getSelectedItem().toString().trim();
            String manOfTheMatch1String = manofthematch1_spinner_view.getSelectedItem().toString().trim();
            String manOfTheMatch2String = manofthematch2_spinner_view.getSelectedItem().toString().trim();
            String tossWinnerString = toss_winner_spinner_view.getSelectedItem().toString().trim();
            String matchWinnerString = match_winner_spinner_view.getSelectedItem().toString().trim();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("batsmen", batsman1String + "," + batsman2String);
            jsonObject.put("bowlers", bowler1String + "," + bowler2String);
            jsonObject.put("matchId", matchId);
            jsonObject.put("matchWinner", matchWinnerString);
            jsonObject.put("tossWinner", tossWinnerString);
            jsonObject.put("menOfTheMatch", manOfTheMatch1String + "," + manOfTheMatch2String);

            String resourcePredictionUpdate = "";
            editMode = getIntent().getBooleanExtra("editMode", false);
            if(editMode == false){
                resourcePredictionUpdate = BaseClass.resourceParticipantPredictionUpdate + BaseClass.userString.getString("userId");
            }else {
                resourcePredictionUpdate = BaseClass.resourceParticipantEditPrediction + BaseClass.userString.getString("userId");
            }

            String userString = null;
            userString = BaseClass.putCall(BaseClass.serviceUrl + resourcePredictionUpdate,
                    jsonObject.toString());
            if(BaseClass.serviceResponseCode == 200){
                BaseClass.userString = BaseClass.convertStringToJson(userString);
                if(BaseClass.userString == null){
                    Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent successfulPredictionIntent = new Intent(PredictionActivity.this, SuccessfulPredictionActivity.class);
                successfulPredictionIntent.putExtra("matchId", matchId);
                successfulPredictionIntent.putExtra("team1Name", team1Name);
                successfulPredictionIntent.putExtra("team2Name", team2Name);
                startActivity(successfulPredictionIntent);
            }else if(BaseClass.serviceResponseCode == 404 || BaseClass.serviceResponseCode == 400){
                prediction_error_text_view.setText(new JSONObject(userString).getString("error"));
            }
            else{
                prediction_error_text_view.setText("Unknown Server Error!");
            }
        }catch(Exception e){
            prediction_error_text_view.setText("Unknown Server Error!");
            e.printStackTrace();
        }


    }


}