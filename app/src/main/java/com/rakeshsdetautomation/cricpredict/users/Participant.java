package com.rakeshsdetautomation.cricpredict.users;

public class Participant {

    private String participantName;
    private int participantRank;
    private int matchesPlayed;
    private int participantScore;
    private int topScoredInMatches;
    private String userId;

    public Participant(int participantRank, String participantName, int matchesPlayed, int participantScore, int topScoredInMatches, String userId) {
        this.participantRank = participantRank;
        this.participantName = participantName;
        this.matchesPlayed = matchesPlayed;
        this.participantScore = participantScore;
        this.topScoredInMatches = topScoredInMatches;
        this.userId = userId;
    }

    public Participant() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public int getParticipantRank() {
        return participantRank;
    }

    public void setParticipantRank(int participantRank) {
        this.participantRank = participantRank;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getParticipantScore() {
        return participantScore;
    }

    public void setParticipantScore(int participantScore) {
        this.participantScore = participantScore;
    }

    public int getTopScoredInMatches() {
        return topScoredInMatches;
    }

    public void setTopScoredInMatches(int topScoredInMatches) {
        this.topScoredInMatches = topScoredInMatches;
    }
}
