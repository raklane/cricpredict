package com.rakeshsdetautomation.cricpredict.users;

import java.util.Arrays;

public class Predictions {

    String[] batsmen = new String[2];
    String[] bowlers = new String[2];
    String tossWinner;
    String matchWinner;
    String[] manOfTheMatch = new String[2];

    @Override
    public String toString() {
        return "Predictions{" +
                "batsmen=" + Arrays.deepToString(batsmen) +
                ", bowlers=" + Arrays.deepToString(bowlers) +
                ", tossWinner='" + tossWinner + '\'' +
                ", matchWinner='" + matchWinner + '\'' +
                ", manOfTheMatch=" + Arrays.deepToString(manOfTheMatch) +
                '}';
    }

    public String[] getBatsmen() {
        return batsmen;
    }

    public void setBatsmen(String[] batsmen) {
        this.batsmen = batsmen;
    }

    public String[] getBowlers() {
        return bowlers;
    }

    public void setBowlers(String[] bowlers) {
        this.bowlers = bowlers;
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

    public String[] getManOfTheMatch() {
        return manOfTheMatch;
    }

    public void setManOfTheMatch(String[] manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }
}
