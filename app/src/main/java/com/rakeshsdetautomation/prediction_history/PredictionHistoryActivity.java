package com.rakeshsdetautomation.prediction_history;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.rakeshsdetautomation.cricpredict.BaseActivity;
import com.rakeshsdetautomation.cricpredict.R;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class PredictionHistoryActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PredictionHistoryActiv";
    String userId;
    private Spinner matchSpinner;
    private ListView predictionHistoryListView;
    private PredictionHistoryAdapter predictionHistoryAdapter;
    ArrayList<PredictionHistory> predictionHistoryArrayList = new ArrayList<PredictionHistory>();

    Set<String> teamNamesForSpinner = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predictions_scores_history_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matchSpinner = (Spinner) findViewById(R.id.prediction_history_spinner) ;
        predictionHistoryListView = (ListView) findViewById(R.id.predictions_scores_history_list);

        userId = getIntent().getStringExtra("userId");

        PredictionHistoryActivityAsyncTaskRunner predictionHistoryActivityAsyncTaskRunner = new PredictionHistoryActivityAsyncTaskRunner();
        predictionHistoryActivityAsyncTaskRunner.execute();

    }

    private void initializeSpinner() {

        String[] teamNamesArrayForSpinner = teamNamesForSpinner.toArray(new String[teamNamesForSpinner.size()]);
        matchSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ArrayUtils.concat(new String[]{"All Matches"}, teamNamesArrayForSpinner)));

        matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTeamName = matchSpinner.getItemAtPosition(position).toString();
                getSelectedMatchListItem(selectedTeamName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSelectedMatchListItem(String selectedTeamName) {

        if(selectedTeamName == "All Matches"){
            predictionHistoryListView.setAdapter(new PredictionHistoryAdapter(PredictionHistoryActivity.this, predictionHistoryArrayList));
        }else{
            ArrayList<PredictionHistory> teamPredictionHistoryArrayList = new ArrayList<PredictionHistory>();
            for(PredictionHistory predictionHistory : predictionHistoryArrayList){
                if(predictionHistory.getMatch_name().contains(selectedTeamName)){
                    teamPredictionHistoryArrayList.add(predictionHistory);
                }
            }
            predictionHistoryListView.setAdapter(new PredictionHistoryAdapter(PredictionHistoryActivity.this, teamPredictionHistoryArrayList));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
        }
    }



    private class PredictionHistoryActivityAsyncTaskRunner extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PredictionHistoryActivity.this, "Loading", "Loading previous predictions...");
        }

        @Override
        protected String doInBackground(String... strings) {
            inflatePredictionHistoryAdapter(userId);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            predictionHistoryAdapter = new PredictionHistoryAdapter(PredictionHistoryActivity.this, predictionHistoryArrayList);
            predictionHistoryListView.setAdapter(predictionHistoryAdapter);
            initializeSpinner();
            progressDialog.dismiss();
        }
    }



    private void inflatePredictionHistoryAdapter(String userId){

        try{
            String participantString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceParticipant + userId);

            JSONObject participantJsonObj = new JSONObject(participantString);
            BaseClass.currentParticipantTabbedScores = participantJsonObj;


            Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

            JSONArray jsonArray = BaseClass.currentParticipantTabbedScores.getJSONArray("participantPredictions");

            if(jsonArray.length() == 0){
                Toast.makeText(PredictionHistoryActivity.this, "User has never predicted!", Toast.LENGTH_SHORT).show();
                return;
            }

            BaseClass.currentParticipantMatchesScores = jsonArray;

            String dateTimePattern = "dd/MM/yyyy - hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimePattern);



            for(int i=jsonArray.length()-1; i>=0; i--){
                String matchId = ((JSONObject) jsonArray.get(i)).getString("matchId");

                //Get match timestamp
                String currentMatchString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMatchById + matchId);
                JSONObject currentMatchJsonObj = new JSONObject(currentMatchString);
                Long currentMatchTimestamp = currentMatchJsonObj.getLong("matchStartTimeStamp");

                Date matchStartTime = new Date(currentMatchTimestamp);
                int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));

                //Show prediction if the cutoff time has crossed
                if(diff_hours < BaseClass.predictionCutOffHours){

                    PredictionHistory predictionHistory = new PredictionHistory();
                    predictionHistory.setMatch_name(currentMatchJsonObj.getString("team1Name") + " vs " +
                            currentMatchJsonObj.getString("team2Name"));
                    predictionHistory.setMatch_date(simpleDateFormat.format(matchStartTime));
                    teamNamesForSpinner.add(currentMatchJsonObj.getString("team1Name"));
                    teamNamesForSpinner.add(currentMatchJsonObj.getString("team2Name"));


                    String batsman1 = ((JSONObject) jsonArray.get(i)).getString("batsmen").split(",")[0];
                    String batsman2 = ((JSONObject) jsonArray.get(i)).getString("batsmen").split(",")[1];
                    String bowler1 = ((JSONObject) jsonArray.get(i)).getString("bowlers").split(",")[0];
                    String bowler2 = ((JSONObject) jsonArray.get(i)).getString("bowlers").split(",")[1];
                    String mom1 = ((JSONObject) jsonArray.get(i)).getString("menOfTheMatch").split(",")[0];
                    String mom2 = ((JSONObject) jsonArray.get(i)).getString("menOfTheMatch").split(",")[1];
                    String matchWinner = ((JSONObject) jsonArray.get(i)).getString("matchWinner");
                    String tossWinner = ((JSONObject) jsonArray.get(i)).getString("tossWinner");
                    int batsman1Points = ((JSONObject) jsonArray.get(i)).getInt("batsman1Points");
                    int batsman2Points = ((JSONObject) jsonArray.get(i)).getInt("batsman2Points");
                    int bowler1Points = ((JSONObject) jsonArray.get(i)).getInt("bowler1Points");
                    int bowler2Points = ((JSONObject) jsonArray.get(i)).getInt("bowler2Points");
                    int mom1Points = ((JSONObject) jsonArray.get(i)).getInt("manOfTheMatch1Points");
                    int mom2Points = ((JSONObject) jsonArray.get(i)).getInt("manOfTheMatch2Points");
                    int matchWinnerPoints = ((JSONObject) jsonArray.get(i)).getInt("matchWinnerPoints");
                    int tossWinnerPoints = ((JSONObject) jsonArray.get(i)).getInt("tossWinnerPoints");
                    int pointsCollectedForMatch = ((JSONObject) jsonArray.get(i)).getInt("pointsCollectedForMatch");
                    boolean toppedForMatch = ((JSONObject) jsonArray.get(i)).getBoolean("toppedForMatchFlag");

                    predictionHistory.setBatsman1(batsman1);
                    predictionHistory.setBatsman2(batsman2);
                    predictionHistory.setBowler1(bowler1);
                    predictionHistory.setBowler2(bowler2);
                    predictionHistory.setMom1(mom1);
                    predictionHistory.setMom2(mom2);
                    predictionHistory.setMatchWinner(matchWinner);
                    predictionHistory.setTossWinner(tossWinner);
                    predictionHistory.setMatch_topper(toppedForMatch);
                    predictionHistory.setBatsman1_points(batsman1Points);
                    predictionHistory.setBatsman2_points(batsman2Points);
                    predictionHistory.setBowler1_points(bowler1Points);
                    predictionHistory.setBowler2_points(bowler2Points);
                    predictionHistory.setMom1_points(mom1Points);
                    predictionHistory.setMom2_points(mom2Points);
                    predictionHistory.setMatch_winner_points(matchWinnerPoints);
                    predictionHistory.setToss_wineer_points(tossWinnerPoints);
                    predictionHistory.setTotal_Points(pointsCollectedForMatch);

                    predictionHistoryArrayList.add(predictionHistory);

                }else{
                    break;
                }


            }


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(PredictionHistoryActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
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
                //write code for milestones
                break;
            case R.id.settings:
                //write code for settings
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}