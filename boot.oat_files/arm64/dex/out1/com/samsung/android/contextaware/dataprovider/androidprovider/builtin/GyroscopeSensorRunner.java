package com.samsung.android.contextaware.dataprovider.androidprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.RawSensorProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class GyroscopeSensorRunner extends RawSensorProvider {
    public GyroscopeSensorRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        this(version, context, looper, observable, 60000);
    }

    public GyroscopeSensorRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable, int rate) {
        super(version, context, looper, observable, rate);
    }

    protected final int getSensorType() {
        return 4;
    }

    public final String[] getContextValueNames() {
        return new String[]{"GyroX", "GyroY", "GyroZ"};
    }

    public final String getContextType() {
        return ContextType.ANDROID_RUNNER_GYROSCOPE_SENSOR.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
