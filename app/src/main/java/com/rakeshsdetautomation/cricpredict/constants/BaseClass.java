package com.rakeshsdetautomation.cricpredict.constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;
import com.rakeshsdetautomation.cricpredict.loginandregistration.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaseClass {

    public static String serviceUrl = "http://ec2-13-59-31-251.us-east-2.compute.amazonaws.com:8080/cricpredictapi/services/";
    public static String resourceLogin = "v1/participants/login";
    public static String resourceLeadershipBoard = "v1/pointscalculator/leadershipboard";
    public static String resourceMatchStats = "v1/matchstats";
    public static String resourceMatches = "v1/matches";
    public static String resourceMatchById = "v1/matches/";
    public static String resourceRegistration = "v1/participants/create";
    public static String resourceTeam = "v1/teams/";
    public static String resourceParticipantPredictionUpdate = "v1/participants/";
    public static String resourceParticipantEditPrediction ="v1/participants/editPrediction/";
    public static String resourceParticipantPrediction = "v1/participants/participantprediction/";
    public static String resourceParticipant = "v1/participants/";
    public static String resourceMilestones = "v1/pointscalculator/milestonesboard";

    public static GoogleSignInClient mGoogleSignInClient;

    public static int serviceResponseCode;
    public static int totalNumberOfMatches;
    public static List<JSONObject> lastMathesId = new ArrayList<JSONObject>();
    public static List<JSONObject> todayMatchesId = new ArrayList<JSONObject>();


    public static JSONObject userString;
    public static JSONObject leadershipBoardString;
    public static JSONObject matchStatsString;
    public static JSONObject matchesString;

    public static int currentParticipantRank = 0;
    public static JSONObject currentParticipantTabbedScores;
    public static JSONArray currentParticipantMatchesScores;
    public static List<String> matchesCalculatedUntilNow = new ArrayList<String>();
    public static List<String> timestampMatchesCalculatedUntilNow = new ArrayList<String>();
    public static List<String> teamNamesMatchesCalculatedUntilNow = new ArrayList<String>();


    public static int predictionCutOffHours = 1;
    public static int predictionStartHours = 18;
    public static String registeredUserId = "";

    //For prediction notification
    public static List<String> predictionCutOff_notification;
    public static List<String> team1Name_notification;
    public static List<String> team2Name_notification;
    public static String rank_notification;
    public static String pointsCollected_notification;
    public static String userId_notification;

    //shared preference name for user
    public static final String mySharedPrefName = "userSharedPref";

    public static void clearAllValues(){
        BaseClass.userString = null;
        BaseClass.leadershipBoardString = null;
        BaseClass.matchesString = null;
        BaseClass.matchStatsString = null;
        BaseClass.totalNumberOfMatches = 0;
        BaseClass.lastMathesId.clear();
        BaseClass.todayMatchesId.clear();
        BaseClass.currentParticipantTabbedScores = null;
        BaseClass.currentParticipantMatchesScores = null;
        BaseClass.matchesCalculatedUntilNow = null;
        BaseClass.timestampMatchesCalculatedUntilNow = null;
        BaseClass.teamNamesMatchesCalculatedUntilNow = null;

    }


    public static JSONObject convertStringToJson(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    public static String postCall(String url, String json) throws IOException {
        BaseClass.serviceResponseCode = 0;
        try{
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                BaseClass.serviceResponseCode = response.code();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
                return "Server call error";
            }
        }catch(Exception e){
            return "Server error";
        }
    }


    public static String putCall(String url, String json) throws IOException {
        BaseClass.serviceResponseCode = 0;
        try{
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                BaseClass.serviceResponseCode = response.code();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
                return "Server call error";
            }
        }catch(Exception e){
            return "Server error";
        }
    }


    public static  String getCall(String url) throws IOException {
        BaseClass.serviceResponseCode = 0;
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                BaseClass.serviceResponseCode = response.code();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
                return "Server call error";
            }
        }catch(Exception e){
            return "Server error";
        }
    }


    public static boolean checkIfPredictionExists(Context context, int matchId){

        try{
            String predictionString = "";
            String predictionResourceUrl = "";

            predictionResourceUrl = BaseClass.resourceParticipantPrediction +
                    BaseClass.userString.getString("userId") + "/" + matchId;

            predictionString = BaseClass.getCall(BaseClass.serviceUrl + predictionResourceUrl);

            JSONObject predictionJsonObj = null;
            predictionJsonObj = new JSONObject(predictionString);

            if(BaseClass.serviceResponseCode == 200 && predictionJsonObj.has("matchId")){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Server Error!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkIfPredictionTimeIsOver(int matchId){

        try{

//            Instant instant = Instant.now();
//            ZoneId zoneId = ZoneId.of("Asia/Calcutta");
//            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

            Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

            String currentMatchString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMatchById + matchId);
            JSONObject currentMatchJsonObj = new JSONObject(currentMatchString);
            Long matchStartTimeStamp = currentMatchJsonObj.getLong("matchStartTimeStamp");
//            ZonedDateTime matchDateTime = Instant.ofEpochMilli(matchStartTimeStamp).atZone(zoneId);
//            int diff = (int) ChronoUnit.HOURS.between(zonedDateTime, matchDateTime);
            Date matchStartTime = new Date(matchStartTimeStamp);
            int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));

            if(diff_hours < BaseClass.predictionCutOffHours){
                return true;
            }
            return false;

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }


    public static Bitmap combineTwoBitmapImages(Bitmap c, Bitmap s){

        Bitmap cs = null;
        int height, width = 0;
        width = c.getWidth() + s.getWidth();
        height = c.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, Resources.getSystem().getDisplayMetrics().widthPixels/2, 0f, null);

        return cs;

    }


    /*
     * Get matches details and set last matches and today matches json
     */
    public static void getMatchesStringBaseClass() throws ParseException {
        try {

            String dateTimePattern = "hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimePattern);


            BaseClass.predictionCutOff_notification = new ArrayList<String>();
            BaseClass.team1Name_notification = new ArrayList<String>();
            BaseClass.team2Name_notification = new ArrayList<String>();

            String matchesString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMatches);
            JSONObject matchesJsonObj = new JSONObject(matchesString);
            JSONArray matchesJsonObjJSONArrayArray = matchesJsonObj.getJSONArray("matches");

            Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

            for(int i=0; i<matchesJsonObjJSONArrayArray.length(); i++){

                Long matchStartTimeStamp = ((JSONObject) matchesJsonObjJSONArrayArray.get(i)).getLong("matchStartTimeStamp");

                Date matchStartTime = new Date(matchStartTimeStamp);
                int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));


                if(diff_hours>=-4 && diff_hours<BaseClass.predictionStartHours){

                    String firstTeamName = ((JSONObject) matchesJsonObjJSONArrayArray.get(i)).getString("team1Name");
                    String secondTeamName = ((JSONObject) matchesJsonObjJSONArrayArray.get(i)).getString("team2Name");


                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(matchStartTime);
                    calendar.add(Calendar.HOUR_OF_DAY, (-1) * BaseClass.predictionCutOffHours);
                    Date predictionTime = calendar.getTime();
                    String currentZonepredictionTimeline = "Prediction ends at " + simpleDateFormat.format(predictionTime);

                    if(predictionCutOff_notification.size() == 2){
                        BaseClass.team1Name_notification.remove(0);
                        BaseClass.team2Name_notification.remove(0);
                        BaseClass.predictionCutOff_notification.remove(0);
                    }
                    BaseClass.team1Name_notification.add(firstTeamName);
                    BaseClass.team2Name_notification.add(secondTeamName);
                    BaseClass.predictionCutOff_notification.add(currentZonepredictionTimeline);

                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public static void getCurrentParticipantRankBaseClass(){



        int currentParticipantRank = 0;
        int totalPointsCollected = 0;
        try {
            String leadershipBoardString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceLeadershipBoard);

            JSONObject leadershipBoardJsonObj = new JSONObject(leadershipBoardString);

            JSONArray jsonArray = leadershipBoardJsonObj.getJSONArray("participantsInBoard");

            for(int i=0; i<jsonArray.length(); i++){
                if(((JSONObject) jsonArray.get(i)).getString("userId").equalsIgnoreCase(BaseClass.userId_notification)){
                    currentParticipantRank = ((JSONObject) jsonArray.get(i)).getInt("rank");
                    totalPointsCollected = ((JSONObject) jsonArray.get(i)).getInt("pointsCollected");
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if(currentParticipantRank > 0){
            BaseClass.rank_notification = "Your rank is " + currentParticipantRank;
        }else{
            BaseClass.rank_notification = "Your rank is " + "--";
        }
        BaseClass.pointsCollected_notification = "You have collected " + totalPointsCollected;

    }

}
