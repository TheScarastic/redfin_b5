package com.android.systemui.util.sensors;

import android.content.Context;
import android.hardware.HardwareBuffer;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorDirectChannel;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Handler;
import android.os.MemoryFile;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.concurrency.ThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class AsyncSensorManager extends SensorManager implements PluginListener<SensorManagerPlugin> {
    private final Executor mExecutor;
    private final SensorManager mInner;
    private final List<SensorManagerPlugin> mPlugins = new ArrayList();
    private final List<Sensor> mSensorCache;

    public AsyncSensorManager(SensorManager sensorManager, ThreadFactory threadFactory, PluginManager pluginManager) {
        this.mInner = sensorManager;
        this.mExecutor = threadFactory.buildExecutorOnNewThread("async_sensor");
        this.mSensorCache = sensorManager.getSensorList(-1);
        if (pluginManager != null) {
            pluginManager.addPluginListener((PluginListener) this, SensorManagerPlugin.class, true);
        }
    }

    protected List<Sensor> getFullSensorList() {
        return this.mSensorCache;
    }

    protected List<Sensor> getFullDynamicSensorList() {
        return this.mInner.getSensorList(-1);
    }

    protected boolean registerListenerImpl(SensorEventListener sensorEventListener, Sensor sensor, int i, Handler handler, int i2, int i3) {
        this.mExecutor.execute(new Runnable(sensorEventListener, sensor, i, i2, handler) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda2
            public final /* synthetic */ SensorEventListener f$1;
            public final /* synthetic */ Sensor f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ int f$4;
            public final /* synthetic */ Handler f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$registerListenerImpl$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerListenerImpl$0(SensorEventListener sensorEventListener, Sensor sensor, int i, int i2, Handler handler) {
        if (!this.mInner.registerListener(sensorEventListener, sensor, i, i2, handler)) {
            Log.e("AsyncSensorManager", "Registering " + sensorEventListener + " for " + sensor + " failed.");
        }
    }

    protected boolean flushImpl(SensorEventListener sensorEventListener) {
        return this.mInner.flush(sensorEventListener);
    }

    protected SensorDirectChannel createDirectChannelImpl(MemoryFile memoryFile, HardwareBuffer hardwareBuffer) {
        throw new UnsupportedOperationException("not implemented");
    }

    protected void destroyDirectChannelImpl(SensorDirectChannel sensorDirectChannel) {
        throw new UnsupportedOperationException("not implemented");
    }

    protected int configureDirectChannelImpl(SensorDirectChannel sensorDirectChannel, Sensor sensor, int i) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDynamicSensorCallbackImpl$1(SensorManager.DynamicSensorCallback dynamicSensorCallback, Handler handler) {
        this.mInner.registerDynamicSensorCallback(dynamicSensorCallback, handler);
    }

    protected void registerDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback dynamicSensorCallback, Handler handler) {
        this.mExecutor.execute(new Runnable(dynamicSensorCallback, handler) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda4
            public final /* synthetic */ SensorManager.DynamicSensorCallback f$1;
            public final /* synthetic */ Handler f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$registerDynamicSensorCallbackImpl$1(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterDynamicSensorCallbackImpl$2(SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.mInner.unregisterDynamicSensorCallback(dynamicSensorCallback);
    }

    protected void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.mExecutor.execute(new Runnable(dynamicSensorCallback) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda3
            public final /* synthetic */ SensorManager.DynamicSensorCallback f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$unregisterDynamicSensorCallbackImpl$2(this.f$1);
            }
        });
    }

    protected boolean requestTriggerSensorImpl(TriggerEventListener triggerEventListener, Sensor sensor) {
        if (triggerEventListener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        } else if (sensor != null) {
            this.mExecutor.execute(new Runnable(triggerEventListener, sensor) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda5
                public final /* synthetic */ TriggerEventListener f$1;
                public final /* synthetic */ Sensor f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    AsyncSensorManager.this.lambda$requestTriggerSensorImpl$3(this.f$1, this.f$2);
                }
            });
            return true;
        } else {
            throw new IllegalArgumentException("sensor cannot be null");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestTriggerSensorImpl$3(TriggerEventListener triggerEventListener, Sensor sensor) {
        if (!this.mInner.requestTriggerSensor(triggerEventListener, sensor)) {
            Log.e("AsyncSensorManager", "Requesting " + triggerEventListener + " for " + sensor + " failed.");
        }
    }

    protected boolean cancelTriggerSensorImpl(TriggerEventListener triggerEventListener, Sensor sensor, boolean z) {
        Preconditions.checkArgument(z);
        this.mExecutor.execute(new Runnable(triggerEventListener, sensor) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda6
            public final /* synthetic */ TriggerEventListener f$1;
            public final /* synthetic */ Sensor f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$cancelTriggerSensorImpl$4(this.f$1, this.f$2);
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelTriggerSensorImpl$4(TriggerEventListener triggerEventListener, Sensor sensor) {
        if (!this.mInner.cancelTriggerSensor(triggerEventListener, sensor)) {
            Log.e("AsyncSensorManager", "Canceling " + triggerEventListener + " for " + sensor + " failed.");
        }
    }

    public boolean registerPluginListener(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        if (this.mPlugins.isEmpty()) {
            Log.w("AsyncSensorManager", "No plugins registered");
            return false;
        }
        this.mExecutor.execute(new Runnable(sensor, sensorEventListener) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda7
            public final /* synthetic */ SensorManagerPlugin.Sensor f$1;
            public final /* synthetic */ SensorManagerPlugin.SensorEventListener f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$registerPluginListener$5(this.f$1, this.f$2);
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerPluginListener$5(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        for (int i = 0; i < this.mPlugins.size(); i++) {
            this.mPlugins.get(i).registerListener(sensor, sensorEventListener);
        }
    }

    public void unregisterPluginListener(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        this.mExecutor.execute(new Runnable(sensor, sensorEventListener) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda8
            public final /* synthetic */ SensorManagerPlugin.Sensor f$1;
            public final /* synthetic */ SensorManagerPlugin.SensorEventListener f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$unregisterPluginListener$6(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterPluginListener$6(SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        for (int i = 0; i < this.mPlugins.size(); i++) {
            this.mPlugins.get(i).unregisterListener(sensor, sensorEventListener);
        }
    }

    protected boolean initDataInjectionImpl(boolean z) {
        throw new UnsupportedOperationException("not implemented");
    }

    protected boolean injectSensorDataImpl(Sensor sensor, float[] fArr, int i, long j) {
        throw new UnsupportedOperationException("not implemented");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOperationParameterImpl$7(SensorAdditionalInfo sensorAdditionalInfo) {
        this.mInner.setOperationParameter(sensorAdditionalInfo);
    }

    protected boolean setOperationParameterImpl(SensorAdditionalInfo sensorAdditionalInfo) {
        this.mExecutor.execute(new Runnable(sensorAdditionalInfo) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda1
            public final /* synthetic */ SensorAdditionalInfo f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$setOperationParameterImpl$7(this.f$1);
            }
        });
        return true;
    }

    protected void unregisterListenerImpl(SensorEventListener sensorEventListener, Sensor sensor) {
        this.mExecutor.execute(new Runnable(sensor, sensorEventListener) { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda0
            public final /* synthetic */ Sensor f$1;
            public final /* synthetic */ SensorEventListener f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager.this.lambda$unregisterListenerImpl$8(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterListenerImpl$8(Sensor sensor, SensorEventListener sensorEventListener) {
        if (sensor == null) {
            this.mInner.unregisterListener(sensorEventListener);
        } else {
            this.mInner.unregisterListener(sensorEventListener, sensor);
        }
    }

    public void onPluginConnected(SensorManagerPlugin sensorManagerPlugin, Context context) {
        this.mPlugins.add(sensorManagerPlugin);
    }

    public void onPluginDisconnected(SensorManagerPlugin sensorManagerPlugin) {
        this.mPlugins.remove(sensorManagerPlugin);
    }
}
