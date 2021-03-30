package com.rakeshsdetautomation.cricpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PredictionActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PredictionActivity";
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

    LinearLayout banner;
    ImageView batsman1_icon;
    ImageView batsman2_icon;
    ImageView bowler1_icon;
    ImageView bowler2_icon;
    ImageView mom1_icon;
    ImageView mom2_icon;
    ImageView winner_icon;
    ImageView toss_icon;

    int matchId;
    String team1Name;
    String team2Name;
    boolean editMode = false;
    boolean timeoutForPrediction = false;

    ArrayAdapter<String> adapterPlayers1;
    ArrayAdapter<String> adapterPlayers2;
    ArrayAdapter<String> teamAdapter;

    String batsman_rules = "1 Run : 2 Points" + "\n" +
            "1 Four : 4 Points (Bonus)" + "\n" +
            "1 Six : 6 Points (Bonus)" + "\n" +
            "Half-Century : 50 Points (Bonus)" + "\n" +
            "Century : 120 Points (Bonus)";
    String bowler_rules = "1 Wicket : 40 Points" + "\n" +
            "2 Wicket-haul : 30 Points (Bonus)" + "\n" +
            "3 Wicket-haul : 50 Points (Bonus)" + "\n" +
            "4 Wicket-haul : 80 Points (Bonus)" + "\n" +
            "5 Wicket-haul : 120 Points (Bonus)" + "\n" +
            "Maiden Over : 40 Points";
    String mom_rules = "100 Points (If a selected player from either teams wins MoM)";
    String winner_rules = "50 Points";
    String toss_rules = "30 Points";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        banner = (LinearLayout) findViewById(R.id.banner);
        batsman1_icon = (ImageView) findViewById(R.id.prediction_batsman_1_icon);
        batsman2_icon = (ImageView) findViewById(R.id.prediction_batsman_2_icon);
        bowler1_icon = (ImageView) findViewById(R.id.prediction_bowler_1_icon);
        bowler2_icon = (ImageView) findViewById(R.id.prediction_bowler_2_icon);
        mom1_icon = (ImageView) findViewById(R.id.prediction_mom_1_icon);
        mom2_icon = (ImageView) findViewById(R.id.prediction_mom_2_icon);
        winner_icon = (ImageView) findViewById(R.id.prediction_winner_icon);
        toss_icon = (ImageView) findViewById(R.id.prediction_toss_icon);

        banner.setOnClickListener(this);
        batsman1_icon.setOnClickListener(this);
        batsman2_icon.setOnClickListener(this);
        bowler1_icon.setOnClickListener(this);
        bowler2_icon.setOnClickListener(this);
        mom1_icon.setOnClickListener(this);
        mom2_icon.setOnClickListener(this);
        winner_icon.setOnClickListener(this);
        toss_icon.setOnClickListener(this);

        perdiction_submit_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPrediction();
            }
        });

        FillDropdownDataAyncTaskRunner fillDropdownDataAyncTaskRunner = new FillDropdownDataAyncTaskRunner();
        fillDropdownDataAyncTaskRunner.execute();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
            case R.id.prediction_batsman_1_icon:
                showAlertDialog("Team 1 Batsman", batsman_rules);
                break;
            case R.id.prediction_batsman_2_icon:
                showAlertDialog("Team 2 Batsman", batsman_rules);
                break;
            case R.id.prediction_bowler_1_icon:
                showAlertDialog("Team 1 Bowler", bowler_rules);
                break;
            case R.id.prediction_bowler_2_icon:
                showAlertDialog("Team 2 Bowler", bowler_rules);
                break;
            case R.id.prediction_mom_1_icon:
                showAlertDialog("Team 1 Man of the match", mom_rules);
                break;
            case R.id.prediction_mom_2_icon:
                showAlertDialog("Team 2 Man of the match", mom_rules);
                break;
            case R.id.prediction_winner_icon:
                showAlertDialog("Winning team", winner_rules);
                break;
            case R.id.prediction_toss_icon:
                showAlertDialog("Toss Winner", toss_rules);
                break;
        }
    }

    public void showAlertDialog(String category, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(PredictionActivity.this);
        builder.setTitle(category);
        builder.setMessage(message);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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