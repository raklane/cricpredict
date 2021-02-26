package com.rakeshsdetautomation.cricpredict.teamsinfo;

import java.util.ArrayList;

public class Teams {

    private String name;
    private ArrayList<String> players = new ArrayList<String>();
    private int numberOfMatchesPlayed = 0;
    private int numberOfMatchesLost = 0;
    private boolean qualified = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public int getNumberOfMatchesPlayed() {
        return numberOfMatchesPlayed;
    }

    public void setNumberOfMatchesPlayed(int numberOfMatchesPlayed) {
        this.numberOfMatchesPlayed = numberOfMatchesPlayed;
    }

    public int getNumberOfMatchesLost() {
        return numberOfMatchesLost;
    }

    public void setNumberOfMatchesLost(int numberOfMatchesLost) {
        this.numberOfMatchesLost = numberOfMatchesLost;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }
}
