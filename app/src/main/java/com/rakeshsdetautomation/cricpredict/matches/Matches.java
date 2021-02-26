package com.rakeshsdetautomation.cricpredict.matches;

import java.util.ArrayList;

public class Matches {

    MatchInfo[] currentDayMatchesInfo = new MatchInfo[2];
    MatchInfo[] previousDayMatchesInfo = new MatchInfo[2];
    ArrayList<MatchInfo> pastMatchesInfo = new ArrayList<MatchInfo>();
    ArrayList<MatchInfo> upcomingMatchesInfo = new ArrayList<MatchInfo>();
    ArrayList<MatchInfo> allMatchesInfo = new ArrayList<MatchInfo>();

    public MatchInfo[] getCurrentDayMatchesInfo() {
        return currentDayMatchesInfo;
    }

    public void setCurrentDayMatchesInfo(MatchInfo[] currentDayMatchesInfo) {
        this.currentDayMatchesInfo = currentDayMatchesInfo;
    }

    public MatchInfo[] getPreviousDayMatchesInfo() {
        return previousDayMatchesInfo;
    }

    public void setPreviousDayMatchesInfo(MatchInfo[] previousDayMatchesInfo) {
        this.previousDayMatchesInfo = previousDayMatchesInfo;
    }

    public ArrayList<MatchInfo> getPastMatchesInfo() {
        return pastMatchesInfo;
    }

    public void setPastMatchesInfo(ArrayList<MatchInfo> pastMatchesInfo) {
        this.pastMatchesInfo = pastMatchesInfo;
    }

    public ArrayList<MatchInfo> getUpcomingMatchesInfo() {
        return upcomingMatchesInfo;
    }

    public void setUpcomingMatchesInfo(ArrayList<MatchInfo> upcomingMatchesInfo) {
        this.upcomingMatchesInfo = upcomingMatchesInfo;
    }

    public ArrayList<MatchInfo> getAllMatchesInfo() {
        return allMatchesInfo;
    }

    public void setAllMatchesInfo(ArrayList<MatchInfo> allMatchesInfo) {
        this.allMatchesInfo = allMatchesInfo;
    }

    public Matches(MatchInfo[] currentDayMatchesInfo, MatchInfo[] previousDayMatchesInfo, ArrayList<MatchInfo> pastMatchesInfo, ArrayList<MatchInfo> upcomingMatchesInfo, ArrayList<MatchInfo> allMatchesInfo) {
        this.currentDayMatchesInfo = currentDayMatchesInfo;
        this.previousDayMatchesInfo = previousDayMatchesInfo;
        this.pastMatchesInfo = pastMatchesInfo;
        this.upcomingMatchesInfo = upcomingMatchesInfo;
        this.allMatchesInfo = allMatchesInfo;
    }
}
