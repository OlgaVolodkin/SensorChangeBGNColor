package com.moveo.olga.moveotask.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.moveo.olga.moveotask.Consts;
import com.moveo.olga.moveotask.R;
import com.moveo.olga.moveotask.services.SensorIntentService;

public class MainPresenter implements MainInterface.Presenter {

    MainInterface.View view;
    MainModel model;

    public MainPresenter(MainActivity view) {
        this.view = view;
    }

    @Override
    public void screenCreated(Context context) {
        setInit();
        dataForSensorReceiver();
        dataForSensorService(context);
        realmInit(context);
    }

    private void setInit() {
        model = new MainModel();
    }

    @Override
    public void broadcastOnReceive(Intent intent, Context context) {
        int sensorInfo = intent.getIntExtra(Consts.Receiver.BROADCAST_INTENT_NAME, 0);
        int backColor;
        if (sensorInfo == 1) {
            backColor = context.getResources().getColor(android.R.color.holo_blue_dark);
        } else if (sensorInfo == 2) {
            backColor = context.getResources().getColor(android.R.color.holo_red_dark);
        }
        else if (sensorInfo == 3) {
            backColor = context.getResources().getColor(android.R.color.holo_green_dark);
        } else {
            backColor = context.getResources().getColor(android.R.color.holo_orange_dark);
            view.showErrText(context.getString(R.string.err_message));
        }
        view.changeBackgroundColor(backColor);
    }

    private void dataForSensorReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.Receiver.SENSOR_INFO);
        view.registerSensorReceiver(intentFilter);
    }

    private void dataForSensorService(Context context){
        Intent intent =  new Intent();
        intent.setClass(context, SensorIntentService.class);
        view.startSensorService(intent);
    }

    private void realmInit(Context context) {
        model.realmInit(context);
    }
}
