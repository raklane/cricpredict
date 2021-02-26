package com.rakeshsdetautomation.cricpredict.users;

import java.util.ArrayList;
import java.util.Arrays;

public class Users {

    String userId;
    String name;
    String pointsCollected;
    String matchesTopped;
    String matchesParticipated;
    Predictions[] currentDayPredictions = new Predictions[2];
    Predictions[] previousDayPredictions = new Predictions[2];
    ArrayList<Predictions> allMatchesPrediction = new ArrayList<Predictions>();
    boolean bettingPlayer;

    public boolean isBettingPlayer() {
        return bettingPlayer;
    }

    public void setBettingPlayer(boolean bettingPlayer) {
        this.bettingPlayer = bettingPlayer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointsCollected() {
        return pointsCollected;
    }

    public void setPointsCollected(String pointsCollected) {
        this.pointsCollected = pointsCollected;
    }

    public String getMatchesTopped() {
        return matchesTopped;
    }

    public void setMatchesTopped(String matchesTopped) {
        this.matchesTopped = matchesTopped;
    }

    public String getMatchesParticipated() {
        return matchesParticipated;
    }

    public void setMatchesParticipated(String matchesParticipated) {
        this.matchesParticipated = matchesParticipated;
    }

    public Predictions[] getCurrentDayPredictions() {
        return currentDayPredictions;
    }

    public void setCurrentDayPredictions(Predictions[] currentDayPredictions) {
        this.currentDayPredictions = currentDayPredictions;
    }

    public Predictions[] getPreviousDayPredictions() {
        return previousDayPredictions;
    }

    public void setPreviousDayPredictions(Predictions[] previousDayPredictions) {
        this.previousDayPredictions = previousDayPredictions;
    }

    public ArrayList<Predictions> getAllMatchesPrediction() {
        return allMatchesPrediction;
    }

    public void setAllMatchesPrediction(ArrayList<Predictions> allMatchesPrediction) {
        this.allMatchesPrediction = allMatchesPrediction;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", pointsCollected='" + pointsCollected + '\'' +
                ", matchesTopped='" + matchesTopped + '\'' +
                ", matchesParticipated='" + matchesParticipated + '\'' +
                ", currentDayPredictions=" + Arrays.deepToString(currentDayPredictions) +
                ", previousDayPredictions=" + Arrays.deepToString(previousDayPredictions) +
                ", allMatchesPrediction=" + allMatchesPrediction.toString() +
                ", bettingPlayer=" + bettingPlayer +
                '}';
    }
}
