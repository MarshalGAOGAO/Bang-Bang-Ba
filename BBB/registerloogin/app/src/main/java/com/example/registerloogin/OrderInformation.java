package com.example.registerloogin;

/**
 * Created by Marshal Gao on 2017/7/4.
 */

public class OrderInformation {

    private int imageId;

    private String titleOrderInformation;

    private String state;

    private String startTime;

    private String money;

    private String buttonText;

    private int id;

    public OrderInformation(int imageId, String titleOrderInformation, String state,
                            String startTime, String money, String buttonText, int id) {
        this.imageId = imageId;
        this.titleOrderInformation = titleOrderInformation;
        this.state = state;
        this.startTime = startTime;
        this.money = money;
        this.buttonText = buttonText;
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitleOrderInformation() {
        return titleOrderInformation;
    }

    public String getState() {
        return state;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getMoney() {
        return money;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getId() {
        return id;
    }

}

