package com.rakeshsdetautomation.cricpredict.matches;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MatchStats {

    private MatchInfo matchInfo;

    private boolean matchStarted;
    private boolean matchPrediction;

    private String tossWinner;

    private String matchWinner;

    private String firstInningsTeamName;
    private String secondInningsTeamName;

    private String firstTeamBatsmenStats[][];
    private String firstTeamBowlersStats[][];

    private String secondTeamBatsmenStats[][];
    private String secondTeamBowlersStats[][];

    private String firstTeamBenchedPlayers[];
    private String secondTeamBenchedPlayers[];

    private String matchDate;

    @Override
    public String toString() {
        return "MatchStats{" +
                "matchInfo='" + matchInfo + '\'' + '\n' +
                "tossWinner='" + tossWinner + '\'' + '\n' +
                ", matchWinner='" + matchWinner + '\'' + '\n' +
                ", firstInningsTeamName='" + firstInningsTeamName + '\'' + '\n' +
                ", secondInningsTeamName='" + secondInningsTeamName + '\'' + '\n' +
                ", firstTeamBatsmenStats=" + Arrays.deepToString(firstTeamBatsmenStats) + '\n' +
                ", firstTeamBowlersStats=" + Arrays.deepToString(firstTeamBowlersStats) + '\n' +
                ", secondTeamBatsmenStats=" + Arrays.deepToString(secondTeamBatsmenStats) + '\n' +
                ", secondTeamBowlersStats=" + Arrays.deepToString(secondTeamBowlersStats) + '\n' +
                ", firstTeamBenchedPlayers=" + Arrays.deepToString(firstTeamBenchedPlayers) + '\n' +
                ", secondTeamBenchedPlayers=" + Arrays.deepToString(secondTeamBenchedPlayers) + '\n' +
                ", matchDate='" + matchDate + '\'' +
                '}';
    }

    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }


    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public String getFirstInningsTeamName() {
        return firstInningsTeamName;
    }

    public void setFirstInningsTeamName(String firstInningsTeamName) {
        this.firstInningsTeamName = firstInningsTeamName;
    }

    public String getSecondInningsTeamName() {
        return secondInningsTeamName;
    }

    public void setSecondInningsTeamName(String secondInningsTeamName) {
        this.secondInningsTeamName = secondInningsTeamName;
    }

    public String[][] getFirstTeamBatsmenStats() {
        return firstTeamBatsmenStats;
    }

    public void setFirstTeamBatsmenStats(String[][] firstTeamBatsmenStats) {
        this.firstTeamBatsmenStats = firstTeamBatsmenStats;
    }

    public String[][] getFirstTeamBowlersStats() {
        return firstTeamBowlersStats;
    }

    public void setFirstTeamBowlersStats(String[][] firstTeamBowlersStats) {
        this.firstTeamBowlersStats = firstTeamBowlersStats;
    }

    public String[][] getSecondTeamBatsmenStats() {
        return secondTeamBatsmenStats;
    }

    public void setSecondTeamBatsmenStats(String[][] secondTeamBatsmenStats) {
        this.secondTeamBatsmenStats = secondTeamBatsmenStats;
    }

    public String[][] getSecondTeamBowlersStats() {
        return secondTeamBowlersStats;
    }

    public void setSecondTeamBowlersStats(String[][] secondTeamBowlersStats) {
        this.secondTeamBowlersStats = secondTeamBowlersStats;
    }

    public String[] getFirstTeamBenchedPlayers() {
        return firstTeamBenchedPlayers;
    }

    public void setFirstTeamBenchedPlayers(String[] firstTeamBenchedPlayers) {
        this.firstTeamBenchedPlayers = firstTeamBenchedPlayers;
    }

    public String[] getSecondTeamBenchedPlayers() {
        return secondTeamBenchedPlayers;
    }

    public void setSecondTeamBenchedPlayers(String[] secondTeamBenchedPlayers) {
        this.secondTeamBenchedPlayers = secondTeamBenchedPlayers;
    }

    public boolean isMatchStarted() {
        return matchStarted;
    }

    public void setMatchStarted(boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isMatchPredictionOn() {


        Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();


        Date matchStartTime = new Date(matchInfo.getMatchStartTimeStamp());
        int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));

        System.out.println("Current datetime: " + currentTimeZoneDate);
        System.out.println("Match datetime: " + matchStartTime);

        System.out.println("Difference between times in hours: " + diff_hours);

        if(diff_hours>= BaseClass.predictionCutOffHours){
            System.out.println("Prediction on.");
            return true;
        }else {
            System.out.println("Prediction off.");
            return false;
        }
    }


    //@RequiresApi(api = Build.VERSION_CODES.O)
    public boolean hasMatchStarted() {

        Date currentTimeZoneDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        Date matchStartTime = new Date(matchInfo.getMatchStartTimeStamp());
        int diff_hours = (int) ((matchStartTime.getTime() - currentTimeZoneDate.getTime()) / (1000 * 60 * 60));

        System.out.println("Current datetime: " + currentTimeZoneDate);
        System.out.println("Match datetime: " + matchStartTime);

        System.out.println("Difference between times in hours: " + diff_hours);

        if(diff_hours < 0){
            System.out.println("Match started.");
            return true;
        }else {
            System.out.println("Match not started.");
            return false;
        }
    }

    public void setMatchPrediction(boolean matchPrediction) {
        this.matchPrediction = matchPrediction;
    }
}
