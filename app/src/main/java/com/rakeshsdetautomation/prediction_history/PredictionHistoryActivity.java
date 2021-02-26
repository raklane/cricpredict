package com.rakeshsdetautomation.prediction_history;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.LeadershipBoardActivity;
import com.rakeshsdetautomation.cricpredict.R;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PredictionHistoryActivity extends AppCompatActivity {

    String userId;
    ArrayList<PredictionHistory> predictionHistoryArrayList = new ArrayList<PredictionHistory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predictions_scores_history_list);

        userId = getIntent().getStringExtra("userId");

        PredictionHistoryActivityAsyncTaskRunner predictionHistoryActivityAsyncTaskRunner = new PredictionHistoryActivityAsyncTaskRunner();
        predictionHistoryActivityAsyncTaskRunner.execute();
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
            PredictionHistoryAdapter predictionHistoryAdapter = new PredictionHistoryAdapter(PredictionHistoryActivity.this, predictionHistoryArrayList);
            ListView predictionHistoryListView = (ListView) findViewById(R.id.predictions_scores_history_list);
            predictionHistoryListView.setAdapter(predictionHistoryAdapter);
            progressDialog.dismiss();
        }
    }



    private void inflatePredictionHistoryAdapter(String userId){

        try{
            String participantString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceParticipant + userId);

            JSONObject participantJsonObj = new JSONObject(participantString);
            BaseClass.currentParticipantTabbedScores = participantJsonObj;

                    /*JSONArray jsonArray = BaseClass.currentParticipantTabbedScores.getJSONArray("participantPredictions");
                    if(jsonArray.length() == 0){
                        Toast.makeText(LeadershipBoardActivity.this, "User has never predicted!", Toast.LENGTH_SHORT).show();
                        return;
                    }*/


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
}