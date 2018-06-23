package com.moveo.olga.moveotask;

public class Consts {

    public static class Receiver {
        public static String SENSOR_INFO = "sensor_info";
        public static String BROADCAST_INTENT_NAME = "sensor";
    }

    public static class Realm {
        public static String REALM_NAME = "realm";
    }

    public static class IntentService {
        public static String INTENT_SERVICE_NAME = "SensorIntentService";
    }

    public static class Sensor {
        public static int UP = 1;
        public static int DOWN = 2;
        public static int USER = 3;
        public static int ERR_MESSAGE = 999;
    }
}
