package com.android.systemui.statusbar;

import android.view.ViewGroup;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface NotificationShadeWindowController extends RemoteInputController.Callback {

    /* loaded from: classes.dex */
    public interface ForcePluginOpenListener {
        void onChange(boolean z);
    }

    /* loaded from: classes.dex */
    public interface OtherwisedCollapsedListener {
        void setWouldOtherwiseCollapse(boolean z);
    }

    default void attach() {
    }

    default boolean getForcePluginOpen() {
        return false;
    }

    default boolean getPanelExpanded() {
        return false;
    }

    default boolean isLaunchingActivity() {
        return false;
    }

    default boolean isShowingWallpaper() {
        return false;
    }

    default void notifyStateChangedCallbacks() {
    }

    @Override // com.android.systemui.statusbar.RemoteInputController.Callback
    default void onRemoteInputActive(boolean z) {
    }

    default void registerCallback(StatusBarWindowCallback statusBarWindowCallback) {
    }

    default void setBackdropShowing(boolean z) {
    }

    default void setBackgroundBlurRadius(int i) {
    }

    default void setBouncerShowing(boolean z) {
    }

    default void setDozeScreenBrightness(int i) {
    }

    default void setFaceAuthDisplayBrightness(float f) {
    }

    default void setForceDozeBrightness(boolean z) {
    }

    default void setForcePluginOpen(boolean z, Object obj) {
    }

    default void setForcePluginOpenListener(ForcePluginOpenListener forcePluginOpenListener) {
    }

    default void setForceWindowCollapsed(boolean z) {
    }

    default void setHeadsUpShowing(boolean z) {
    }

    default void setKeyguardFadingAway(boolean z) {
    }

    default void setKeyguardGoingAway(boolean z) {
    }

    default void setKeyguardNeedsInput(boolean z) {
    }

    default void setKeyguardOccluded(boolean z) {
    }

    default void setKeyguardShowing(boolean z) {
    }

    default void setLaunchingActivity(boolean z) {
    }

    default void setLightRevealScrimAmount(float f) {
    }

    default void setNotTouchable(boolean z) {
    }

    default void setNotificationShadeFocusable(boolean z) {
    }

    default void setNotificationShadeView(ViewGroup viewGroup) {
    }

    default void setPanelExpanded(boolean z) {
    }

    default void setPanelVisible(boolean z) {
    }

    default void setQsExpanded(boolean z) {
    }

    default void setRequestTopUi(boolean z, String str) {
    }

    default void setScrimsVisibility(int i) {
    }

    default void setScrimsVisibilityListener(Consumer<Integer> consumer) {
    }

    default void setStateListener(OtherwisedCollapsedListener otherwisedCollapsedListener) {
    }

    default void setWallpaperSupportsAmbientMode(boolean z) {
    }

    default void unregisterCallback(StatusBarWindowCallback statusBarWindowCallback) {
    }
}
