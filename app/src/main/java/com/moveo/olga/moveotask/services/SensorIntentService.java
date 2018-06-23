package com.moveo.olga.moveotask.services;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.moveo.olga.moveotask.Consts;
import com.moveo.olga.moveotask.ScreenSensor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;


public class SensorIntentService extends IntentService implements SensorEventListener {

    Timer timer;
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    ScreenSensor sensor;
    float xValue, yValue, zValue;
    boolean accelerometerPresent;
    Integer lastSensorPosition = 0;
    int realmIdCount = -1;
    String TAG = "SensorIntentService";


    public SensorIntentService() {
        super("SensorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        addServiceSensor();
        if (accelerometerPresent) {
            timer = new Timer();
            if (intent != null) {
                timer.schedule(new TimerTask() {
                    public void run() {
                        try {
                            saveDataToRealm();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 300);
            }
        }
        else {
            sendSensorInfoToClient(999);
        }
    }

    private void addServiceSensor() {
        sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensorList.size() > 0) {
            accelerometerPresent = true;
            accelerometerSensor = sensorList.get(0);
            sensorManager.registerListener(this, accelerometerSensor, SENSOR_DELAY_FASTEST); //SENSOR_DELAY_NORMAL
        } else {
            accelerometerPresent = false;
            Log.i(TAG,"No accelerometer present!");
        }
    }

    private Integer getScreenSensorData() {
        Integer sensorPosition;
        if (yValue >= 6) {
            sensorPosition = 3;
            Log.i(TAG, "USER");
        } else if (zValue >= 0) {
            sensorPosition = 1;
            Log.i(TAG, "UP");
        } else {
            sensorPosition = 2;
            Log.i(TAG, "DOWN");
        }
        return sensorPosition;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xValue = sensorEvent.values[0];
        yValue = sensorEvent.values[1];
        zValue = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


    private void saveDataToRealm() {
        Integer sensorPosition = getScreenSensorData();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance(); // opens "realm.realm"
            if (realmIdCount < 0) {
                RealmResults<ScreenSensor> realmList = realm.where(ScreenSensor.class).findAll();
                realmIdCount = realmList.size() + 1;
            } else {
                realmIdCount = realmIdCount + 1;
            }
            realm.beginTransaction();
            if (realmIdCount == 500) {
                realm.deleteAll();
                realmIdCount = 0;
            }
            sensor = realm.createObject(ScreenSensor.class);
            sensor.setScreenPosition(sensorPosition);
            realm.commitTransaction();
            realm.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,e.getMessage());
        }

        finally {
            if(realm != null) {
                realm.close();
            }
            if (sensorPosition != lastSensorPosition) {
                sendSensorInfoToClient(sensorPosition);
            }
            lastSensorPosition = sensorPosition;
        }
    }

    private void sendSensorInfoToClient(int msg){
        Intent intent = new Intent();
        intent.setAction(Consts.Receiver.SENSOR_INFO);
        intent.putExtra(Consts.Receiver.BROADCAST_INTENT_NAME, msg);
        sendBroadcast(intent);
    }


    private int getLastDataFromRealm(Realm realm) {
        RealmResults<ScreenSensor> listM = realm.where(ScreenSensor.class).findAll();
        int lastScreenPosition = 0;
        if (listM.size() > 0) {
            lastScreenPosition = listM.last().getScreenPosition();
        }
        return lastScreenPosition;
    }
}

