package com.example.bangbangba;

/**
 * Created by Marshal Gao on 2017/7/4.
 */

public class OrderInformation {

    private int imageId;

    private String titleOrderInformation;

    private String stringState;

    private String startTime;

    private String money;

    private String buttonText;

    private int id;

    private int state;

    public OrderInformation(int imageId, String titleOrderInformation, String stringState,
                            String startTime, String money, String buttonText, int id, int state) {
        this.imageId = imageId;
        this.titleOrderInformation = titleOrderInformation;
        this.stringState = stringState;
        this.startTime = startTime;
        this.money = money;
        this.buttonText = buttonText;
        this.id = id;
        this.state = state;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitleOrderInformation() {
        return titleOrderInformation;
    }

    public String getStringState() {
        return stringState;
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

    public int getState() {
        return state;
    }

}

