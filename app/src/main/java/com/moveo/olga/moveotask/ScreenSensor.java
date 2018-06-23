package com.moveo.olga.moveotask;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ScreenSensor extends RealmObject {

    @Required
    private Integer screenPosition;

    public Integer getScreenPosition() {
        return screenPosition;
    }

    public void setScreenPosition(Integer screenPosition) {
        this.screenPosition = screenPosition;
    }
}