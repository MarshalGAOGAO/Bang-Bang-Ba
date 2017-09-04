package com.example.bangbangba;

/**
 * Created by Marshal Gao on 2017/7/19.
 */

public class HomeBottom {

    private int imageId;

    private String title;

    private String startTime;

    private String initiator;

    private String creditValue;

    private int state;

    private int id;

    private String money;

    public HomeBottom(int imageId, String title, String startTime, String initiator,
                      String creditValue, int state, int id, String money) {
        this.imageId = imageId;
        this.title = title;
        this.startTime = startTime;
        this.initiator = initiator;
        this.creditValue = creditValue;
        this.state = state;
        this.id = id;
        this.money = money;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public int getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public String getMoney() {
        return money;
    }

}
