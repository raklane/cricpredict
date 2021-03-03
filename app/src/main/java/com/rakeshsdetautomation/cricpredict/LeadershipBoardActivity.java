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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;
import com.rakeshsdetautomation.cricpredict.users.Participant;
import com.rakeshsdetautomation.prediction_history.PredictionHistory;
import com.rakeshsdetautomation.prediction_history.PredictionHistoryActivity;
import com.rakeshsdetautomation.prediction_history.PredictionHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LeadershipBoardActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadership_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int totalMatches = 10;
        TextView totalMatchesView = (TextView) findViewById(R.id.total_matches_view);
        totalMatchesView.setText("Total Matches: " + BaseClass.totalNumberOfMatches);

        LeadershipBoardAsyncTaskRunner leadershipBoardAsyncTaskRunner = new LeadershipBoardAsyncTaskRunner();
        leadershipBoardAsyncTaskRunner.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
        }
    }

    private class LeadershipBoardAsyncTaskRunner extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LeadershipBoardActivity.this, "Loading", "Loading information");
        }

        @Override
        protected String doInBackground(String... strings) {
            createLeadershipBoard();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }


    private void createLeadershipBoard(){
        //List of participants
        final ArrayList<Participant> participants = new ArrayList<Participant>();

        try {
            JSONArray jsonArray = ((JSONObject) BaseClass.leadershipBoardString).getJSONArray("participantsInBoard");
            for(int i=0; i<jsonArray.length(); i++){
                Participant participant = new Participant();
                participant.setParticipantRank(((JSONObject) jsonArray.get(i)).getInt("rank"));
                participant.setParticipantName(((JSONObject) jsonArray.get(i)).getString("name"));
                participant.setMatchesPlayed(((JSONObject) jsonArray.get(i)).getInt("matchesParticipated"));
                participant.setParticipantScore(((JSONObject) jsonArray.get(i)).getInt("pointsCollected"));
                participant.setTopScoredInMatches(((JSONObject) jsonArray.get(i)).getInt("matchesTopped"));
                participant.setUserId(((JSONObject) jsonArray.get(i)).getString("userId"));
                participants.add(participant);
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }



        LeadershipAdapter leadershipAdapter = new LeadershipAdapter(this, participants);
        ListView listView = (ListView) findViewById(R.id.leadership_list_view);
        listView.setAdapter(leadershipAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Participant participant = participants.get(position);
                String userId = participant.getUserId();

                Intent predictionHistoryActivityIntent = new Intent(LeadershipBoardActivity.this, PredictionHistoryActivity.class);
                predictionHistoryActivityIntent.putExtra("userId", userId);
                startActivity(predictionHistoryActivityIntent);



                /*Intent predictionsScoresIntent = new Intent(LeadershipBoardActivity.this, PredictionScores.class);
                startActivity(predictionsScoresIntent);*/

            }
        });
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
