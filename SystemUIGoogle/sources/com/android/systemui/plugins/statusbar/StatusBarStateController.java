package com.android.systemui.plugins.statusbar;

import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(version = 1)
@DependsOn(target = StateListener.class)
/* loaded from: classes.dex */
public interface StatusBarStateController {
    public static final int VERSION = 1;

    @ProvidesInterface(version = 1)
    /* loaded from: classes.dex */
    public interface StateListener {
        public static final int VERSION = 1;

        default void onDozeAmountChanged(float f, float f2) {
        }

        default void onDozingChanged(boolean z) {
        }

        default void onExpandedChanged(boolean z) {
        }

        default void onFullscreenStateChanged(boolean z, boolean z2) {
        }

        default void onPulsingChanged(boolean z) {
        }

        default void onStateChanged(int i) {
        }

        default void onStatePostChange() {
        }

        default void onStatePreChange(int i, int i2) {
        }
    }

    void addCallback(StateListener stateListener);

    float getDozeAmount();

    int getState();

    boolean isDozing();

    boolean isExpanded();

    boolean isPulsing();

    void removeCallback(StateListener stateListener);
}
