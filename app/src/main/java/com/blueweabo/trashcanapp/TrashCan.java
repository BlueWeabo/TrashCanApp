package com.blueweabo.trashcanapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TrashCan {
    public float percentageFilled;

    public TrashCan() {}

    public TrashCan(float percentageFilled) {
        this.percentageFilled = percentageFilled;
    }

    public float getPercentageFilled() {
        return percentageFilled;
    }

    public void setPercentageFilled(float percentageFilled) {
        this.percentageFilled = percentageFilled;
    }
}
