package com.rakeshsdetautomation.cricpredict.teamsinfo;

import java.util.ArrayList;

public class TeamsHolder {

    private static Teams[] team = new Teams[8];

    public TeamsHolder(){

        //set team players and names
        //***Run this class by a job once before the app is published
        team[0].setName("");
        ArrayList<String> players = new ArrayList<String>();
        players.add("");


    }

}
