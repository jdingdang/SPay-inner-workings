package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

public interface ISensorHubCmdProtocol {
    public static final byte APDR_MODE_SLEEP = (byte) 1;
    public static final byte APDR_MODE_WAKEUP = (byte) 0;
    public static final byte AP_SLEEP = (byte) -46;
    public static final byte AP_WAKEUP = (byte) -47;
    public static final byte CATEGORY_LIB = (byte) 23;
    public static final byte INST_LIB_ADD = (byte) -79;
    public static final byte INST_LIB_GETVALUE = (byte) -72;
    public static final byte INST_LIB_NOTI = (byte) -76;
    public static final byte INST_LIB_PUTVALUE = (byte) -63;
    public static final byte INST_LIB_REMOVE = (byte) -78;
    public static final byte INST_VOICE_ADD = (byte) -31;
    public static final byte INST_VOICE_REMOVE = (byte) -30;
    public static final byte MODE_BATCH = (byte) 2;
    public static final byte MODE_EXTANDED_INTERRUPT = (byte) 3;
    public static final byte MODE_INTERRUPT = (byte) 1;
    public static final byte MODE_NORMAL = (byte) 0;
    public static final byte MSG_TYPE_COMMAND = (byte) 3;
    public static final byte MSG_TYPE_OUTPUT = (byte) 5;
    public static final byte MSG_TYPE_REG = (byte) 1;
    public static final byte MSG_TYPE_SETTING = (byte) 4;
    public static final byte MSG_TYPE_UNREG = (byte) 2;
    public static final byte TYPE_ABNORMAL_PRESSURE_MONITOR = (byte) 49;
    public static final byte TYPE_ABNORMAL_SHOCK_SERVICE = (byte) 38;
    public static final byte TYPE_ABNORMAL_SHOCK_USER_ACTION = (byte) 1;
    public static final byte TYPE_ACCELEROMETER = (byte) 0;
    public static final byte TYPE_ACTIVITY_TRACKER_BATCH_CURRENT_INFO = (byte) 2;
    public static final byte TYPE_ACTIVITY_TRACKER_CURRENT_INFO = (byte) 1;
    public static final byte TYPE_ACTIVITY_TRACKER_SERVICE = (byte) 26;
    public static final byte TYPE_ANY_MOTION_DETECTOR_DATA = (byte) 1;
    public static final byte TYPE_ANY_MOTION_DETECTOR_SERVICE = (byte) 58;
    public static final byte TYPE_AOD_SERVICE_TYPE = (byte) 1;
    public static final byte TYPE_APDR_CUR_POS = (byte) 22;
    public static final byte TYPE_APDR_GPS_STATUS = (byte) 5;
    public static final byte TYPE_APDR_MAG_SENSOR_OFFSET = (byte) 3;
    public static final byte TYPE_APDR_SERVICE = (byte) 28;
    public static final byte TYPE_APDR_UTC_TIME = (byte) 4;
    public static final byte TYPE_APDR_WAKEUP_STEP_COUNT = (byte) 1;
    public static final byte TYPE_APDR_WAKEUP_TIME_COUNT = (byte) 2;
    public static final byte TYPE_AUTO_BRIGHTNESS_DEVICE_MODE = (byte) 1;
    public static final byte TYPE_AUTO_BRIGHTNESS_EBOOK_CONFIG_DATA = (byte) 2;
    public static final byte TYPE_AUTO_BRIGHTNESS_SERVICE = (byte) 48;
    public static final byte TYPE_AUTO_ROTATION_SERVICE = (byte) 7;
    public static final byte TYPE_BAROMETER = (byte) 3;
    public static final byte TYPE_BOTTOM_FLAT_DETECTOR_SERVICE = (byte) 101;
    public static final byte TYPE_BOUNCE_LONG_MOTION_SERVICE = (byte) 20;
    public static final byte TYPE_BOUNCE_SHORT_MOTION_SERVICE = (byte) 18;
    public static final byte TYPE_CALL_MOTION_SERVICE = (byte) 41;
    public static final byte TYPE_CALL_POSE_SERVICE = (byte) 2;
    public static final byte TYPE_CALL_STATE = (byte) 48;
    public static final byte TYPE_CAPTURE_MOTION_SERVICE = (byte) 39;
    public static final byte TYPE_CARE_GIVER_SERVICE = (byte) 30;
    public static final byte TYPE_CARRYING_STATUS_MONITOR_SERVICE = (byte) 102;
    public static final byte TYPE_CURRENT_CELL = (byte) 17;
    public static final byte TYPE_CURRENT_POSITION = (byte) 22;
    public static final byte TYPE_CURRENT_TIME = (byte) 14;
    public static final byte TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE = (byte) 56;
    public static final byte TYPE_DIRECT_CALL_SERVICE = (byte) 10;
    public static final byte TYPE_DUAL_DISPLAY_ANGLE_SERVICE = (byte) 53;
    public static final byte TYPE_EAD_DURATION = (byte) 2;
    public static final byte TYPE_EAD_MODE = (byte) 1;
    public static final byte TYPE_EAD_SERVICE = (byte) 52;
    public static final byte TYPE_ENVIRONMENT_SENSOR_SERVICE = (byte) 12;
    public static final byte TYPE_EXERCISE_DATA = (byte) 1;
    public static final byte TYPE_EXERCISE_GPS_ONOFF_STATUS = (byte) 37;
    public static final byte TYPE_EXERCISE_SERVICE = (byte) 46;
    public static final byte TYPE_FLAT_MOTION_FOR_TABLE_MODE_SERVICE = (byte) 45;
    public static final byte TYPE_FLAT_MOTION_SERVICE = (byte) 21;
    public static final byte TYPE_FLIP_COVER_ACTION_SERVICE = (byte) 14;
    public static final byte TYPE_GEOMAGNETIC = (byte) 2;
    public static final byte TYPE_GESTURE = (byte) 4;
    public static final byte TYPE_GESTURE_SERVICE = (byte) 5;
    public static final byte TYPE_GET_AOD_CURRENT_STATUS = (byte) 1;
    public static final byte TYPE_GET_AOD_CURRENT_VERSION = (byte) 2;
    public static final byte TYPE_GET_EXERCISE_BATCH_DATA = (byte) 1;
    public static final byte TYPE_GYROSCOPE = (byte) 1;
    public static final byte TYPE_GYRO_TEMPERATURE_SERVICE = (byte) 15;
    public static final byte TYPE_HALL_SENSOR_SERVICE = (byte) 50;
    public static final byte TYPE_LIFE_LOG_COMPONENT_SERVICE = (byte) 29;
    public static final byte TYPE_LIFE_LOG_COMPONENT_STAYING_AREA_RADIUS = (byte) 4;
    public static final byte TYPE_LIFE_LOG_COMPONENT_STAYING_RADIUS = (byte) 3;
    public static final byte TYPE_LIFE_LOG_COMPONENT_STOP_PERIOD = (byte) 1;
    public static final byte TYPE_LIFE_LOG_COMPONENT_WAIT_PERIOD = (byte) 2;
    public static final byte TYPE_LIGHT = (byte) 7;
    public static final byte TYPE_MAIN_SCREEN_DETECTION_SERVICE = (byte) 57;
    public static final byte TYPE_MOTION_SERVICE = (byte) 4;
    public static final byte TYPE_MOVEMENT_ALERT_SERVICE = (byte) 22;
    public static final byte TYPE_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS = (byte) 1;
    public static final byte TYPE_MOVEMENT_FOR_POSITIONING_SERVICE = (byte) 9;
    public static final byte TYPE_MOVEMENT_SERVICE = (byte) 8;
    public static final byte TYPE_NOISE_LEVEL_DETECT_SERVICE = (byte) 2;
    public static final byte TYPE_PEDOMETER_CURRENT_INFO = (byte) 1;
    public static final byte TYPE_PEDOMETER_DELIVERY_COUNT = (byte) 21;
    public static final byte TYPE_PEDOMETER_EXERCISE_MODE = (byte) 2;
    public static final byte TYPE_PEDOMETER_SERVICE = (byte) 3;
    public static final byte TYPE_PEDOMETER_STEP_LENGTH_SCALE_FACTOR = (byte) 1;
    public static final byte TYPE_PHONE_STATE_COVER_STATUS = (byte) 1;
    public static final byte TYPE_PHONE_STATE_MONITOR_SERVICE = (byte) 47;
    public static final byte TYPE_POWER_NOTI = (byte) 13;
    public static final byte TYPE_PROXIMITY = (byte) 5;
    public static final byte TYPE_PUT_DOWN_MOTION_SERVICE = (byte) 16;
    public static final byte TYPE_SENSORHUB_TIMER_COUNT = (byte) 51;
    public static final byte TYPE_SENSORHUB_TIMER_SERVICE = (byte) 126;
    public static final byte TYPE_SENSOR_STATUS_CHECK_SERVICE = (byte) 59;
    public static final byte TYPE_SHAKE_MOTION_SERVICE = (byte) 13;
    public static final byte TYPE_SLEEP_MONITOR_CURRENT_INFO = (byte) 1;
    public static final byte TYPE_SLEEP_MONITOR_SERVICE = (byte) 37;
    public static final byte TYPE_SLOCATION_SERVICE = (byte) 55;
    public static final byte TYPE_SLPI_RESET_STATE = (byte) 99;
    public static final byte TYPE_SPECIFIC_POSE_ALERT_SERVICE = (byte) 25;
    public static final byte TYPE_STAYING_ALERT_CURRENT_LOCATION = (byte) 3;
    public static final byte TYPE_STAYING_ALERT_SERVICE = (byte) 27;
    public static final byte TYPE_STAYING_ALERT_STOP_PERIOD = (byte) 1;
    public static final byte TYPE_STAYING_ALERT_WAIT_PERIOD = (byte) 2;
    public static final byte TYPE_STEP_COUNT_ALERT_SERVICE = (byte) 6;
    public static final byte TYPE_STEP_COUNT_TIMER_SERVICE = (byte) 125;
    public static final byte TYPE_STEP_COUNT_TIMER_STEPCOUNT = (byte) 50;
    public static final byte TYPE_STEP_LEVEL_MONITOR_INACTIVE_TIME_DURATION = (byte) 5;
    public static final byte TYPE_STEP_LEVEL_MONITOR_NOTIFICATION_COUNT = (byte) 1;
    public static final byte TYPE_STEP_LEVEL_MONITOR_NOTIFICATION_END_TIME = (byte) 3;
    public static final byte TYPE_STEP_LEVEL_MONITOR_NOTIFICATION_START_TIME = (byte) 2;
    public static final byte TYPE_STEP_LEVEL_MONITOR_POWER_STEP_DURATION = (byte) 4;
    public static final byte TYPE_STEP_LEVEL_MONITOR_SERVICE = (byte) 44;
    public static final byte TYPE_STOP_ALERT_SERVICE = (byte) 11;
    public static final byte TYPE_TELEPHONY_NOTI = (byte) 17;
    public static final byte TYPE_TEMPERATURE_ALERT_SERVICE = (byte) 24;
    public static final byte TYPE_TEMPERATURE_HUMIDITY = (byte) 6;
    public static final byte TYPE_TEST_FLAT_MOTION_SERVICE = (byte) 23;
    public static final byte TYPE_USER_INFO_GENDER = (byte) 20;
    public static final byte TYPE_USER_INFO_HEIGHT = (byte) 18;
    public static final byte TYPE_USER_INFO_WEIGHT = (byte) 19;
    public static final byte TYPE_WAKE_UP_VOICE_SERVICE = (byte) 1;
    public static final byte TYPE_WAKE_UP_VOICE_SOUND_SOURCE_AM = (byte) 1;
    public static final byte TYPE_WAKE_UP_VOICE_SOUND_SOURCE_GRAMMER = (byte) 2;
    public static final byte TYPE_WIRELESS_CHARGING_MONITOR = (byte) 54;
    public static final byte TYPE_WRIST_UP_MOTION_SERVICE = (byte) 19;

    void sendCmdToSensorHub(byte b, byte b2, byte[] bArr);
}
