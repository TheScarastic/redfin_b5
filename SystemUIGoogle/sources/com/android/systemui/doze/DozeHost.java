package com.android.systemui.doze;
/* loaded from: classes.dex */
public interface DozeHost {

    /* loaded from: classes.dex */
    public interface Callback {
        default void onDozeSuppressedChanged(boolean z) {
        }

        default void onNotificationAlerted(Runnable runnable) {
        }

        default void onPowerSaveChanged(boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public interface PulseCallback {
        void onPulseFinished();

        void onPulseStarted();
    }

    void addCallback(Callback callback);

    void cancelGentleSleep();

    void dozeTimeTick();

    void extendPulse(int i);

    boolean isBlockingDoze();

    boolean isDozeSuppressed();

    boolean isPowerSaveActive();

    boolean isProvisioned();

    boolean isPulsingBlocked();

    void onIgnoreTouchWhilePulsing(boolean z);

    void onSlpiTap(float f, float f2);

    void prepareForGentleSleep(Runnable runnable);

    void pulseWhileDozing(PulseCallback pulseCallback, int i);

    void removeCallback(Callback callback);

    void setAnimateScreenOff(boolean z);

    void setAnimateWakeup(boolean z);

    default void setAodDimmingScrim(float f) {
    }

    void setDozeScreenBrightness(int i);

    void startDozing();

    void stopDozing();
}
