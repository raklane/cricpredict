package com.rakeshsdetautomation.cricpredict.users;

public class MilestoneListItemClass {

    private String category;
    private String name;
    private String userId;
    private int points;

    public MilestoneListItemClass(String category, String name, String userId, int points) {
        this.category = category;
        this.name = name;
        this.userId = userId;
        this.points = points;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "MilestoneListItemClass{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", points=" + points +
                '}';
    }

    public MilestoneListItemClass() {
    }
}
