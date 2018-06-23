package com.moveo.olga.moveotask.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moveo.olga.moveotask.R;

public class MainActivity extends AppCompatActivity implements MainInterface.View {

    MainPresenter presenter;
    Context context;
    SensorReceiver sensorReceiver;
    RelativeLayout relativeLayout;
    TextView errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        setInit();
        presenter.screenCreated(context);
    }

    private void setInit() {
        presenter = new MainPresenter(this);
        context = this;
    }

    private void initialization() {
        relativeLayout = findViewById(R.id.parentLayout);
        errorMessage = findViewById(R.id.errTextView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sensorReceiver);
    }

    @Override
    public void startSensorService(Intent intent) {
        startService(intent);
    }

    @Override
    public void registerSensorReceiver(IntentFilter intentFilter) {
        sensorReceiver = new MainActivity.SensorReceiver();
        registerReceiver(sensorReceiver, intentFilter);
    }

    @Override
    public void changeBackgroundColor(int color) {
        relativeLayout.setBackgroundColor(color);
    }

    @Override
    public void showErrText(String err) {
        errorMessage.setText(err);
    }



    private class SensorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.broadcastOnReceive(intent, context);
        }
    }
}


