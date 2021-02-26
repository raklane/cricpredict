package com.rakeshsdetautomation.prediction_history;

public class PredictionHistory {

    private String match_name;
    private String match_date;
    private String batsman1;
    private String batsman2;
    private String bowler1;
    private String bowler2;
    private String mom1;
    private String mom2;
    private String matchWinner;
    private String tossWinner;
    private int batsman1_points;
    private int batsman2_points;
    private int bowler1_points;
    private int bowler2_points;
    private int mom1_points;
    private int mom2_points;
    private int match_winner_points;
    private int toss_wineer_points;
    private int total_Points;
    private boolean match_topper;

    public PredictionHistory(String match_name, String match_date, String batsman1, String batsman2, String bowler1, String bowler2, String mom1, String mom2, String matchWinner, String tossWinner, int batsman1_points, int batsman2_points, int bowler1_points, int bowler2_points, int mom1_points, int mom2_points, int match_winner_points, int toss_wineer_points, int total_Points, boolean match_topper) {
        this.match_name = match_name;
        this.match_date = match_date;
        this.batsman1 = batsman1;
        this.batsman2 = batsman2;
        this.bowler1 = bowler1;
        this.bowler2 = bowler2;
        this.mom1 = mom1;
        this.mom2 = mom2;
        this.matchWinner = matchWinner;
        this.tossWinner = tossWinner;
        this.batsman1_points = batsman1_points;
        this.batsman2_points = batsman2_points;
        this.bowler1_points = bowler1_points;
        this.bowler2_points = bowler2_points;
        this.mom1_points = mom1_points;
        this.mom2_points = mom2_points;
        this.match_winner_points = match_winner_points;
        this.toss_wineer_points = toss_wineer_points;
        this.total_Points = total_Points;
        this.match_topper = match_topper;
    }

    public PredictionHistory(){

    }

    public String getBatsman1() {
        return batsman1;
    }

    public void setBatsman1(String batsman1) {
        this.batsman1 = batsman1;
    }

    public String getBatsman2() {
        return batsman2;
    }

    public void setBatsman2(String batsman2) {
        this.batsman2 = batsman2;
    }

    public String getBowler1() {
        return bowler1;
    }

    public void setBowler1(String bowler1) {
        this.bowler1 = bowler1;
    }

    public String getBowler2() {
        return bowler2;
    }

    public void setBowler2(String bowler2) {
        this.bowler2 = bowler2;
    }

    public String getMom1() {
        return mom1;
    }

    public void setMom1(String mom1) {
        this.mom1 = mom1;
    }

    public String getMom2() {
        return mom2;
    }

    public void setMom2(String mom2) {
        this.mom2 = mom2;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public int getBatsman1_points() {
        return batsman1_points;
    }

    public void setBatsman1_points(int batsman1_points) {
        this.batsman1_points = batsman1_points;
    }

    public int getBatsman2_points() {
        return batsman2_points;
    }

    public void setBatsman2_points(int batsman2_points) {
        this.batsman2_points = batsman2_points;
    }

    public int getBowler1_points() {
        return bowler1_points;
    }

    public void setBowler1_points(int bowler1_points) {
        this.bowler1_points = bowler1_points;
    }

    public int getBowler2_points() {
        return bowler2_points;
    }

    public void setBowler2_points(int bowler2_points) {
        this.bowler2_points = bowler2_points;
    }

    public int getMom1_points() {
        return mom1_points;
    }

    public void setMom1_points(int mom1_points) {
        this.mom1_points = mom1_points;
    }

    public int getMom2_points() {
        return mom2_points;
    }

    public void setMom2_points(int mom2_points) {
        this.mom2_points = mom2_points;
    }

    public int getMatch_winner_points() {
        return match_winner_points;
    }

    public void setMatch_winner_points(int match_winner_points) {
        this.match_winner_points = match_winner_points;
    }

    public int getToss_wineer_points() {
        return toss_wineer_points;
    }

    public void setToss_wineer_points(int toss_wineer_points) {
        this.toss_wineer_points = toss_wineer_points;
    }

    public int getTotal_Points() {
        return total_Points;
    }

    public void setTotal_Points(int total_Points) {
        this.total_Points = total_Points;
    }

    public boolean isMatch_topper() {
        return match_topper;
    }

    public void setMatch_topper(boolean match_topper) {
        this.match_topper = match_topper;
    }
}
