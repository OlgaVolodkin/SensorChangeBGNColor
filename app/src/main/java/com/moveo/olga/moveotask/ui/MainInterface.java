package com.moveo.olga.moveotask.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public interface MainInterface {

    interface View {
        void startSensorService(Intent intent);
        void registerSensorReceiver(IntentFilter intentFilter);
        void changeBackgroundColor(int color);
        void showErrText(String err);

    }

    interface Presenter {
        void screenCreated(Context context);
        void broadcastOnReceive(Intent intent, Context context);

    }

    interface Model {
        void realmInit(Context context);
    }
}
