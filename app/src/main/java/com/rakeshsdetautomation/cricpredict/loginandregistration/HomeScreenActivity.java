package com.rakeshsdetautomation.cricpredict.loginandregistration;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakeshsdetautomation.cricpredict.App;
import com.rakeshsdetautomation.cricpredict.BaseActivity;
import com.rakeshsdetautomation.cricpredict.LeadershipBoardActivity;
import com.rakeshsdetautomation.cricpredict.PredictionActivity;
import com.rakeshsdetautomation.cricpredict.R;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.prediction_history.PredictionHistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import xdroid.toaster.Toaster;

public class HomeScreenActivity extends BaseActivity implements View.OnClickListener {

    private NotificationManagerCompat notificationManagerCompat;


    public static final String TAG = "HomeScreenActivity";
    private String name;
    private int rank_int;
    TextView homeScreenTitle;
    TextView rank;
    TextView totalPoints;
    TextView totalMatches;
    TextView totalMatchesPlayed;


    LinearLayout firstMatch;
    LinearLayout secondMatch;
    ImageView firstMatchFirstTeamImage;
    ImageView firstMatchSecondTeamImage;
    ImageView secondMatchFirstTeamImage;
    ImageView secondMatchSecondTeamImage;
    TextView firstMatchFirstTeamName;
    TextView firstMatchSecondTeamName;
    TextView secondMatchFirstTeamName;
    TextView secondMatchSecondTeamName;
    TextView firstMatchPredictionTime;
    TextView secondMatchPredictionTime;
    TextView predictionTimeNotificationView;

    Button lastDayMatchesResults;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private String email;



    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        homeScreenTitle = (TextView) findViewById(R.id.home_screen_title);
        rank = (TextView) findViewById(R.id.participant_text_view_rank);
        totalPoints = (TextView) findViewById(R.id.participant_text_view_total_points);
        totalMatches = (TextView) findViewById(R.id.participant_text_view_total_matches);
        totalMatchesPlayed = (TextView) findViewById(R.id.participant_text_view_total_matches_played);

        firstMatch = (LinearLayout) findViewById(R.id.first_match_linear_layout);
        secondMatch = (LinearLayout) findViewById(R.id.second_match_linear_layout);
        firstMatchFirstTeamImage = (ImageView) findViewById(R.id.first_match_first_team_image);
        firstMatchSecondTeamImage = (ImageView) findViewById(R.id.first_match_second_team_image);
        secondMatchFirstTeamImage = (ImageView) findViewById(R.id.second_match_first_team_image);
        secondMatchSecondTeamImage = (ImageView) findViewById(R.id.second_match_second_team_image);
        firstMatchFirstTeamName = (TextView) findViewById(R.id.first_match_first_team_name_text_view);
        firstMatchSecondTeamName = (TextView) findViewById(R.id.first_match_second_team_name_text_view);
        secondMatchFirstTeamName = (TextView) findViewById(R.id.second_match_first_team_name_text_view);
        secondMatchSecondTeamName = (TextView) findViewById(R.id.second_match_second_team_name_text_view);
        firstMatchPredictionTime = (TextView) findViewById(R.id.first_match_time_remaining_text_view);
        secondMatchPredictionTime = (TextView) findViewById(R.id.second_match_time_remaining_text_view);
        predictionTimeNotificationView = (TextView) findViewById(R.id.prediction_time_notification);

        lastDayMatchesResults = (Button) findViewById(R.id.previous_match_button);

        //set default visibility of first and second layout to invisible
        firstMatch.setVisibility(View.INVISIBLE);
        secondMatch.setVisibility(View.INVISIBLE);

        if(BaseClass.userString == null){
            try {
                String userStringToConvert = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceParticipant + getIntent().getStringExtra("username"));
                if(userStringToConvert.isEmpty()){
                    Toaster.toast("No data exists for email: " + getIntent().getStringExtra("username") + ". Please logout and register!");
                    return;
                }

                BaseClass.userString = BaseClass.convertStringToJson(userStringToConvert);
            } catch (IOException e) {
                Log.e(TAG, "User retrieval error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println(BaseClass.userString);
        //Set welcome message
        //homeScreenTitle.setText("Welcome " + name + "!");


        HomeScreenDataLoadAsyncTaskRunner homeScreenDataLoadAsyncTaskRunner = new HomeScreenDataLoadAsyncTaskRunner();
        homeScreenDataLoadAsyncTaskRunner.execute();

        //On click listener on last matches results for self
        lastDayMatchesResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviousMatchResultsAsyncTaskRunner previousMatchResultsAsyncTaskRunner = new PreviousMatchResultsAsyncTaskRunner();
                previousMatchResultsAsyncTaskRunner.execute();
            }
        });

        onStart();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null){

            reference = FirebaseDatabase.getInstance().getReference("Users");
            userId = user.getUid();

            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);
                    if(userProfile != null){
                        name = userProfile.fullName;
                        email = userProfile.email;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.apply();

                        homeScreenTitle.setText("Welcome " + sharedPreferences.getString("name", null) + "!");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeScreenActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
            return;

        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            name = account.getDisplayName();
            email = account.getEmail();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("email", email);
            editor.commit();
            //homeScreenTitle.setText("Welcome " + name + "!");
            homeScreenTitle.setText("Welcome " + sharedPreferences.getString("name", null) + "!");
            return;
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


    private class HomeScreenDataLoadAsyncTaskRunner extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(HomeScreenActivity.this, "Loading", "Loading information...");
        }

        @Override
        protected String doInBackground(String... strings) {
            getCurrentParticipantRank();
            getMatchStatsInfo();
            try {
                getMatchesString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                inflateTodayMatchesForPrediction();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if(rank_int > 0){
                    rank.setText(Integer.toString(rank_int));
                }else{
                    rank.setText("--");
                }

                totalPoints.setText(Integer.toString(BaseClass.userString.getInt("pointsCollected")));
                totalMatchesPlayed.setText(Integer.toString(BaseClass.userString.getInt("matchesParticipated")));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(HomeScreenActivity.this, "ServerError:Unable to parse user json", Toast.LENGTH_SHORT).show();
                Intent mainScreenIntent = new Intent(HomeScreenActivity.this, MainActivity.class);
                startActivity(mainScreenIntent);
            }

            totalMatches.setText(Integer.toString(BaseClass.totalNumberOfMatches));


            progressDialog.dismiss();
        }
    }

    private void getCurrentParticipantRank(){
        //Get rank form leadership board
        int currentParticipantRank = 0;
        try {
            String leadershipBoardString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceLeadershipBoard);

            JSONObject leadershipBoardJsonObj = new JSONObject(leadershipBoardString);
            BaseClass.leadershipBoardString = leadershipBoardJsonObj;

            JSONArray jsonArray = leadershipBoardJsonObj.getJSONArray("participantsInBoard");

            for(int i=0; i<jsonArray.length(); i++){
                if(((JSONObject) jsonArray.get(i)).getString("userId").equalsIgnoreCase(BaseClass.userString.getString("userId"))){
                    currentParticipantRank = ((JSONObject) jsonArray.get(i)).getInt("rank");
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        rank_int = currentParticipantRank;
    }


    private void getMatchStatsInfo(){
        //Get total number of matches from MatchStats call
        try {
            String matchStatsString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMatchStats);

            JSONObject matchStatsJsonObj = new JSONObject(matchStatsString);
            BaseClass.matchStatsString = matchStatsJsonObj;

            JSONArray jsonArray = matchStatsJsonObj.getJSONArray("matchesStats");

            BaseClass.totalNumberOfMatches = jsonArray.length();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //totalMatches.setText(Integer.toString(BaseClass.totalNumberOfMatches));
    }



    public void onLeadershipBoardButtonClick(View view){
        Intent leadershipBoardIntent = new Intent(HomeScreenActivity.this, LeadershipBoardActivity.class);
        startActivity(leadershipBoardIntent);
    }


    /*
    * Get matches details and set last matches and today matches json
     */
    private void getMatchesString() throws ParseException {
        try {
            String matchesString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMatches);

            JSONObject matchesJsonObj = new JSONObject(matchesString);
            BaseClass.matchesString = matchesJsonObj;


            JSONArray jsonArray = matchesJsonObj.getJSONArray("matches");

//            Instant instant = Instant.now();
//            ZoneId zoneId = ZoneId.of("Asia/Calcutta");
//            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
//            ZonedDateTime matchDateTime;

            Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();


            for(int i=0; i<jsonArray.length(); i++){

                //System.out.println(((JSONObject) jsonArray.get(i)).getInt("matchId"));
                Long matchStartTimeStamp = ((JSONObject) jsonArray.get(i)).getLong("matchStartTimeStamp");

                Date matchStartTime = new Date(matchStartTimeStamp);
                int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));


                if(diff_hours>=-4 && diff_hours<BaseClass.predictionStartHours){
                    if(BaseClass.todayMatchesId.size() == 2){
                        BaseClass.todayMatchesId.set(0, BaseClass.todayMatchesId.get(1));
                        BaseClass.todayMatchesId.set(1, ((JSONObject) jsonArray.get(i)));
                    }else{
                        BaseClass.todayMatchesId.add(((JSONObject) jsonArray.get(i)));
                    }
                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }


    /*
    * Inflate matches to be predicted
     */
    public void inflateTodayMatchesForPrediction() throws JSONException {

        try{

            if(BaseClass.todayMatchesId.size() == 0){
                predictionTimeNotificationView.setText("Prediction starts " + BaseClass.predictionStartHours + " hours before the match!");
            }

            for(int i=0; i<BaseClass.todayMatchesId.size(); i++){

                predictionTimeNotificationView.setText("Prediction ends " + BaseClass.predictionCutOffHours + " hour before the match!");

                String firstTeamName = BaseClass.todayMatchesId.get(i).getString("team1Name");
                String secondTeamName = BaseClass.todayMatchesId.get(i).getString("team2Name");
                String firstTeamImageName = firstTeamName.toLowerCase().trim().replaceAll(" ","_");
                String secondTeamImageName = secondTeamName.toLowerCase().trim().replaceAll(" ","_");

//                Instant instant = Instant.now();
//                ZoneId zoneId = ZoneId.of("Asia/Calcutta");
//                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
//                ZonedDateTime matchDateTime;
//                ZonedDateTime currentZoneMatchDateTime;

                Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();


                Long matchStartTimeStamp = BaseClass.todayMatchesId.get(i).getLong("matchStartTimeStamp");
//                matchDateTime = Instant.ofEpochMilli(matchStartTimeStamp).atZone(zoneId);
//                currentZoneMatchDateTime  = Instant.ofEpochMilli(matchStartTimeStamp).atZone(ZoneId.systemDefault());
                Date matchStartTime = new Date(matchStartTimeStamp);
                int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));

                //final int diff = (int) ChronoUnit.HOURS.between(zonedDateTime, matchDateTime);


                String predictionTimeline = "";
                String currentZonepredictionTimeline = "";
                if(diff_hours>BaseClass.predictionCutOffHours){
                    predictionTimeline = "Prediction ends at ";
                    currentZonepredictionTimeline = "Prediction ends at ";
                }else {
                    predictionTimeline = "Prediction ended at ";
                    currentZonepredictionTimeline = "Prediction ended at ";
                }

                String dateTimePattern = "hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimePattern);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(matchStartTime);
                calendar.add(Calendar.HOUR_OF_DAY, (-1) * BaseClass.predictionCutOffHours);
                Date predictionTime = calendar.getTime();
                currentZonepredictionTimeline = currentZonepredictionTimeline + simpleDateFormat.format(predictionTime);

//                ZonedDateTime predictionTime = matchStartTime.minusHours(BaseClass.predictionCutOffHours);
//                ZonedDateTime currentZonePredictionTime = currentZoneMatchDateTime.minusHours(BaseClass.predictionCutOffHours);

//                predictionTimeline = predictionTimeline + DateTimeFormatter.ofPattern("hh:mm a").format(predictionTime);
//                currentZonepredictionTimeline = currentZonepredictionTimeline + DateTimeFormatter.ofPattern("hh:mm a").format(currentZonePredictionTime);
                final Intent[] notificationIntent = new Intent[1];

                if(i==0){
                    int resId = getResources().getIdentifier(firstTeamImageName, "drawable", getPackageName());
                    firstMatchFirstTeamImage.setImageResource(resId);
                    resId = getResources().getIdentifier(secondTeamImageName, "drawable", getPackageName());
                    firstMatchSecondTeamImage.setImageResource(resId);
                    firstMatchFirstTeamName.setText(firstTeamName);
                    firstMatchSecondTeamName.setText(secondTeamName);
                    firstMatchPredictionTime.setText(currentZonepredictionTimeline);
                    firstMatch.setVisibility(View.VISIBLE);
                    final boolean timePassedForPredictionFirstMatch = (!BaseClass.checkIfPredictionExists(HomeScreenActivity.this,
                            BaseClass.todayMatchesId.get(i).getInt("matchId")))
                            && diff_hours < BaseClass.predictionCutOffHours;
                    final int diff_hoursForInsideClass = diff_hours;
                    firstMatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(timePassedForPredictionFirstMatch){
                                Toast.makeText(HomeScreenActivity.this, "Prediction time is over!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent predictionIntent = new Intent(HomeScreenActivity.this, PredictionActivity.class);
                            try {
                                predictionIntent.putExtra("matchId", BaseClass.todayMatchesId.get(0).getInt("matchId"));
                                predictionIntent.putExtra("team1Name", BaseClass.todayMatchesId.get(0).getString("team1Name"));
                                predictionIntent.putExtra("team2Name", BaseClass.todayMatchesId.get(0).getString("team2Name"));
                                predictionIntent.putExtra("editMode", false);
                                predictionIntent.putExtra("timeoutForPrediction", diff_hoursForInsideClass>=BaseClass.predictionCutOffHours ? false : true );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(predictionIntent);
                        }
                    });
                    if(diff_hours<BaseClass.predictionCutOffHours){
                        firstMatch.setBackgroundColor(Color.GRAY);
                    }
                }

                if(i==1){
                    int resId = getResources().getIdentifier(firstTeamImageName, "drawable", getPackageName());
                    secondMatchFirstTeamImage.setImageResource(resId);
                    resId = getResources().getIdentifier(secondTeamImageName, "drawable", getPackageName());
                    secondMatchSecondTeamImage.setImageResource(resId);
                    secondMatchFirstTeamName.setText(firstTeamName);
                    secondMatchSecondTeamName.setText(secondTeamName);
                    secondMatchPredictionTime.setText(currentZonepredictionTimeline);
                    secondMatch.setVisibility(View.VISIBLE);
                    final boolean timePassedForPredictionFirstMatch = (!BaseClass.checkIfPredictionExists(HomeScreenActivity.this,
                            BaseClass.todayMatchesId.get(i).getInt("matchId")))
                            && diff_hours < BaseClass.predictionCutOffHours;
                    final int diff_hoursForInsideClass = diff_hours;
                    secondMatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent predictionIntent = new Intent(HomeScreenActivity.this, PredictionActivity.class);
                            try {
                                if(timePassedForPredictionFirstMatch){
                                    Toast.makeText(HomeScreenActivity.this, "Prediction time is over!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                predictionIntent.putExtra("matchId", BaseClass.todayMatchesId.get(1).getInt("matchId"));
                                predictionIntent.putExtra("team1Name", BaseClass.todayMatchesId.get(1).getString("team1Name"));
                                predictionIntent.putExtra("team2Name", BaseClass.todayMatchesId.get(1).getString("team2Name"));
                                predictionIntent.putExtra("editMode", false);
                                predictionIntent.putExtra("timeoutForPrediction", diff_hoursForInsideClass>=BaseClass.predictionCutOffHours ? false : true );
                                notificationIntent[0] = predictionIntent;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(predictionIntent);
                        }
                    });
                    if(diff_hours<BaseClass.predictionCutOffHours){
                        secondMatch.setBackgroundColor(Color.GRAY);
                    }
                }

                String predictionReminderNotificationTitle = firstTeamName + " vs " + secondTeamName;
                int resId = getResources().getIdentifier(firstTeamImageName, "drawable", getPackageName());
                Bitmap firstTeamIcon = BitmapFactory.decodeResource(this.getResources(), resId);
                resId = getResources().getIdentifier(secondTeamImageName, "drawable", getPackageName());
                Bitmap secondTeamIcon = BitmapFactory.decodeResource(this.getResources(), resId);

                Bitmap mergedImages = BaseClass.combineTwoBitmapImages(firstTeamIcon, secondTeamIcon);
                //sendPredictionReminderChannelNotification(predictionReminderNotificationTitle, currentZonepredictionTimeline, mergedImages);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    private void previousMatchesResults(){


        //get participant string
        try{
            String userId = BaseClass.userString.getString("userId");

            Intent predictionHistoryActivityIntent = new Intent(HomeScreenActivity.this, PredictionHistoryActivity.class);
            predictionHistoryActivityIntent.putExtra("userId", userId);
            startActivity(predictionHistoryActivityIntent);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(HomeScreenActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
        }

    }


    private class PreviousMatchResultsAsyncTaskRunner extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(HomeScreenActivity.this, "Loading", "Loading previous match info...");
        }

        @Override
        protected String doInBackground(String... strings) {
            previousMatchesResults();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }




    public void sendPredictionReminderChannelNotification(String title, String message, Bitmap mergedImages){

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                mainActivityIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_PREDICTION_ID)
                .setSmallIcon(R.drawable.cricket_notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(mergedImages)
                .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(mergedImages)
                    .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setColor(Color.rgb(0, 204, 51))
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .build();
        notificationManagerCompat.notify(App.prediction_id, notification);

    }

    public void sendLeadershipReminderChannelNotification(String title, String message){

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_LEADERSHIP_ID)
                .setSmallIcon(R.drawable.cricket_notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setColor(Color.rgb(0, 204, 51))
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .build();
        notificationManagerCompat.notify(App.leadership_id, notification);

    }



    /*public void tempOnClickMethod(View view) throws IOException, InterruptedException {

        final MatchStats[] matchStats = new MatchStats[1];
        matchStats[0] = new MatchStats();

        final MatchStatsParse matchStatsParse = new MatchStatsParse(new MatchStatsParse.AsyncResponse() {
            @Override
            public void processFinish(MatchStats output) {
                System.out.println("RRRakesh ---" + output);
                System.out.println();

                matchStats[0] = output;
            }
        });
        matchStatsParse.execute("22396");

    }*/









}
