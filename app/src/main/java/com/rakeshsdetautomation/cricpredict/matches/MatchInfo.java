package com.rakeshsdetautomation.cricpredict.matches;

public class MatchInfo {

    private int matchId;
    private Long matchStartTimeStamp;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public Long getMatchStartTimeStamp() {
        return matchStartTimeStamp;
    }

    public void setMatchStartTimeStamp(Long matchStartTimeStamp) {
        this.matchStartTimeStamp = matchStartTimeStamp;
    }

    @Override
    public String toString() {
        return "MatchInfo{" +
                "matchId=" + matchId +
                ", matchStartTimeStamp=" + matchStartTimeStamp +
                '}';
    }
}
