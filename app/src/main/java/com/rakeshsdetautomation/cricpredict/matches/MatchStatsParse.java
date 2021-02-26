package com.rakeshsdetautomation.cricpredict.matches;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;
import com.rakeshsdetautomation.cricpredict.constants.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MatchStatsParse extends AsyncTask<String, String, String> {

    public static String scoreboardString;
    ProgressDialog progressDialog;

    /*public interface AsyncResponse{
        void processFinish(MatchStats output);
    }

    public AsyncResponse delegate = null;*/

    public MatchStatsParse(HomeScreenActivity homeScreenActivity) throws ClientProtocolException, IOException {
        progressDialog = new ProgressDialog(homeScreenActivity);
    }

    /*public MatchStatsParse(AsyncResponse delegate) throws ClientProtocolException, IOException {
        this.delegate = delegate;
    }*/


    @Override
    protected void onPreExecute(){
        progressDialog.setMessage("Loading matches details.");
        progressDialog.setTitle("Loading...");
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... strings) {

        String argMatchId = strings[0];

        String url = "https://www.cricbuzz.com/api/html/cricket-scorecard/"+  argMatchId;

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(out);
                scoreboardString = out.toString();
                out.close();
            }else{
                httpResponse.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

            System.out.println(scoreboardString);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("doinbackground");
        return scoreboardString;


    }


    @Override
    protected  void onPostExecute(String result){

        System.out.println("onPostExecute");

        Constants.asyncTaskSyncCount = 0;

        super.onPostExecute(result);

        MatchStats matchStats = new MatchStats();

        //Document doc = JSoup.parse
        Document doc = (Document) Jsoup.parse(result);
        //String innings1html = doc.getElementById("innings_1").toString();
        //String nameL = doc.select("input[name=nameL]").attr("value");
        //String level = doc.select("input[name=level]").attr("value");
        //System.out.println("Debug----" + innings1html);
        //Elements links = doc.select("a");
        //Element head = doc.select("head").first();

        String matchResult = doc.select("div[class*='cb-scrcrd-status']").text();
        System.out.println(matchResult);

        //Store values in MatchStats
        matchStats = new MatchStats();

        Elements tossNode = doc.getElementsMatchingText("won the toss");
        String tossWinner = (tossNode.get(6).text()).substring(0, tossNode.get(6).text().indexOf("won")-1);
        matchStats.setTossWinner(tossWinner);

        Elements teamNames = doc.select("div[class*='cb-scrd-hdr-rw']");
        String firstTeamName = teamNames.get(0).text().substring(0, teamNames.get(0).text().indexOf("Innings")-1);
        String secondTeamName = teamNames.get(1).text().substring(0, teamNames.get(1).text().indexOf("Innings")-1);
        matchStats.setFirstInningsTeamName(firstTeamName);
        matchStats.setSecondInningsTeamName(secondTeamName);

        Element matchWinner = doc.selectFirst("div[class*='cb-scrcrd-status']");
        matchStats.setMatchWinner(matchWinner.text().substring(0, matchWinner.text().indexOf("won")-1));

        Elements matchDate = doc.select("span[class*='schedule-date']");
        matchStats.setMatchDate(matchDate.get(0).attr("timestamp"));



        Elements firstInningsNode = doc.select("div[id*='innings_1'] div[class*='cb-scrd-itms']");
        String [][] firstTeamBatsmenStats;
        String [][] secondTeamBowlersStats;

        Element node;
        int firstInningsBatsmenBattedSize = 0;
        int firstInningsBowlersBowledSize = 0;
        for(int i=0; i<firstInningsNode.size(); i++){
            node = firstInningsNode.get(i);
            if(node.childNodeSize() == 15){
                firstInningsBatsmenBattedSize++;
            }else if(node.childNodeSize() == 17)
                firstInningsBowlersBowledSize++;
        }
        firstTeamBatsmenStats = new String[firstInningsBatsmenBattedSize][5];
        secondTeamBowlersStats = new String[firstInningsBowlersBowledSize][8];

        int batsmanIndex = 0;
        int bowlerIndex = 0;
        for(int i=0; i<firstInningsNode.size(); i++){
            node = firstInningsNode.get(i);
            if(node.childNodeSize() == 15){
                String playerNameLink = node.child(0).child(0).attr("href");
                String playerRunsHit = node.child(2).text();
                String playerBallsFaced = node.child(3).text();
                String playerFoursHit = node.child(4).text();
                String playerSixesHit = node.child(5).text();
                PlayerName playerName = null;
                try {
                    playerName = new PlayerName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String playerNameString = null;
                try {
                    playerNameString = playerName.execute(playerNameLink).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                firstTeamBatsmenStats[batsmanIndex][0] = playerNameString;
                firstTeamBatsmenStats[batsmanIndex][1] = playerRunsHit;
                firstTeamBatsmenStats[batsmanIndex][2] = playerBallsFaced;
                firstTeamBatsmenStats[batsmanIndex][3] = playerFoursHit;
                firstTeamBatsmenStats[batsmanIndex][4] = playerSixesHit;
                batsmanIndex++;

            }else if (node.childNodeSize() == 17){
                String playerNameLink = node.child(0).child(0).attr("href");
                String playerOversBowled = node.child(1).text();
                String playerMaidenOversBowled = node.child(2).text();
                String playerRunsConceeded = node.child(3).text();
                String playerWicketsTaken = node.child(4).text();
                String playerNoBallsBowled = node.child(5).text();
                String playerWideBallBowled = node.child(6).text();
                String playerEconomy = node.child(7).text();
                PlayerName playerName = null;
                try {
                    playerName = new PlayerName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String playerNameString = null;
                try {
                    playerNameString = playerName.execute(playerNameLink).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                secondTeamBowlersStats[bowlerIndex][0] = playerNameString;
                secondTeamBowlersStats[bowlerIndex][1] = playerOversBowled;
                secondTeamBowlersStats[bowlerIndex][2] = playerMaidenOversBowled;
                secondTeamBowlersStats[bowlerIndex][3] = playerRunsConceeded;
                secondTeamBowlersStats[bowlerIndex][4] = playerWicketsTaken;
                secondTeamBowlersStats[bowlerIndex][5] = playerNoBallsBowled;
                secondTeamBowlersStats[bowlerIndex][6] = playerWideBallBowled;
                secondTeamBowlersStats[bowlerIndex][7] = playerEconomy;
                bowlerIndex++;

            }
        }
        matchStats.setFirstTeamBatsmenStats(firstTeamBatsmenStats);
        matchStats.setSecondTeamBowlersStats(secondTeamBowlersStats);



        //2nd innings stats
        Elements secondInningsNode = doc.select("div[id*='innings_2'] div[class*='cb-scrd-itms']");
        String [][] secondTeamBatsmenStats;
        String [][] firstTeamBowlersStats;

        int secondInningsBatsmenBattedSize = 0;
        int secondInningsBowlersBowledSize = 0;
        for(int i=0; i<secondInningsNode.size(); i++){
            node = secondInningsNode.get(i);
            if(node.childNodeSize() == 15){
                secondInningsBatsmenBattedSize++;
            }else if(node.childNodeSize() == 17)
                secondInningsBowlersBowledSize++;
        }
        secondTeamBatsmenStats = new String[secondInningsBatsmenBattedSize][5];
        firstTeamBowlersStats = new String[secondInningsBowlersBowledSize][8];

        batsmanIndex = 0;
        bowlerIndex = 0;
        for(int i=0; i<secondInningsNode.size(); i++){
            node = secondInningsNode.get(i);
            if(node.childNodeSize() == 15){
                String playerNameLink = node.child(0).child(0).attr("href");
                String playerRunsHit = node.child(2).text();
                String playerBallsFaced = node.child(3).text();
                String playerFoursHit = node.child(4).text();
                String playerSixesHit = node.child(5).text();
                PlayerName playerName = null;
                try {
                    playerName = new PlayerName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String playerNameString = null;
                try {
                    playerNameString = playerName.execute(playerNameLink).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                secondTeamBatsmenStats[batsmanIndex][0] = playerNameString;
                secondTeamBatsmenStats[batsmanIndex][1] = playerRunsHit;
                secondTeamBatsmenStats[batsmanIndex][2] = playerBallsFaced;
                secondTeamBatsmenStats[batsmanIndex][3] = playerFoursHit;
                secondTeamBatsmenStats[batsmanIndex][4] = playerSixesHit;
                batsmanIndex++;

            }else if (node.childNodeSize() == 17){
                String playerNameLink = node.child(0).child(0).attr("href");
                String playerOversBowled = node.child(1).text();
                String playerMaidenOversBowled = node.child(2).text();
                String playerRunsConceeded = node.child(3).text();
                String playerWicketsTaken = node.child(4).text();
                String playerNoBallsBowled = node.child(5).text();
                String playerWideBallBowled = node.child(6).text();
                String playerEconomy = node.child(7).text();
                PlayerName playerName = null;
                try {
                    playerName = new PlayerName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String playerNameString = null;
                try {
                    playerNameString = playerName.execute(playerNameLink).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                firstTeamBowlersStats[bowlerIndex][0] = playerNameString;
                firstTeamBowlersStats[bowlerIndex][1] = playerOversBowled;
                firstTeamBowlersStats[bowlerIndex][2] = playerMaidenOversBowled;
                firstTeamBowlersStats[bowlerIndex][3] = playerRunsConceeded;
                firstTeamBowlersStats[bowlerIndex][4] = playerWicketsTaken;
                firstTeamBowlersStats[bowlerIndex][5] = playerNoBallsBowled;
                firstTeamBowlersStats[bowlerIndex][6] = playerWideBallBowled;
                firstTeamBowlersStats[bowlerIndex][7] = playerEconomy;
                bowlerIndex++;

            }
        }
        matchStats.setSecondTeamBatsmenStats(secondTeamBatsmenStats);
        matchStats.setFirstTeamBowlersStats(firstTeamBowlersStats);
        System.out.println(matchStats);

        System.out.println("onposteexecutefinish");
        Constants.matchStats = matchStats;
        Constants.asyncTaskSyncCount = 1;

        //delegate.processFinish(matchStats);

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }


    }







}
