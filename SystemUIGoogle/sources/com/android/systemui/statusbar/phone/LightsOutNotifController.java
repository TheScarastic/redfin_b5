package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public class LightsOutNotifController {
    @VisibleForTesting
    int mAppearance;
    private final CommandQueue mCommandQueue;
    private int mDisplayId;
    private final NotificationEntryManager mEntryManager;
    private View mLightsOutNotifView;
    private final WindowManager mWindowManager;
    private final CommandQueue.Callbacks mCallback = new CommandQueue.Callbacks() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController.2
        @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
        public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
            if (i == LightsOutNotifController.this.mDisplayId) {
                LightsOutNotifController lightsOutNotifController = LightsOutNotifController.this;
                lightsOutNotifController.mAppearance = i2;
                lightsOutNotifController.updateLightsOutView();
            }
        }
    };
    private final NotificationEntryListener mEntryListener = new NotificationEntryListener() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController.3
        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onNotificationAdded(NotificationEntry notificationEntry) {
            LightsOutNotifController.this.updateLightsOutView();
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onPostEntryUpdated(NotificationEntry notificationEntry) {
            LightsOutNotifController.this.updateLightsOutView();
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            LightsOutNotifController.this.updateLightsOutView();
        }
    };

    /* access modifiers changed from: package-private */
    public LightsOutNotifController(WindowManager windowManager, NotificationEntryManager notificationEntryManager, CommandQueue commandQueue) {
        this.mWindowManager = windowManager;
        this.mEntryManager = notificationEntryManager;
        this.mCommandQueue = commandQueue;
    }

    /* access modifiers changed from: package-private */
    public void setLightsOutNotifView(View view) {
        destroy();
        this.mLightsOutNotifView = view;
        if (view != null) {
            view.setVisibility(8);
            this.mLightsOutNotifView.setAlpha(0.0f);
            init();
        }
    }

    private void destroy() {
        this.mEntryManager.removeNotificationEntryListener(this.mEntryListener);
        this.mCommandQueue.removeCallback(this.mCallback);
    }

    private void init() {
        this.mDisplayId = this.mWindowManager.getDefaultDisplay().getDisplayId();
        this.mEntryManager.addNotificationEntryListener(this.mEntryListener);
        this.mCommandQueue.addCallback(this.mCallback);
        updateLightsOutView();
    }

    private boolean hasActiveNotifications() {
        return this.mEntryManager.hasActiveNotifications();
    }

    @VisibleForTesting
    void updateLightsOutView() {
        final boolean shouldShowDot;
        if (this.mLightsOutNotifView != null && (shouldShowDot = shouldShowDot()) != isShowingDot()) {
            float f = 0.0f;
            if (shouldShowDot) {
                this.mLightsOutNotifView.setAlpha(0.0f);
                this.mLightsOutNotifView.setVisibility(0);
            }
            ViewPropertyAnimator animate = this.mLightsOutNotifView.animate();
            if (shouldShowDot) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(shouldShowDot ? 750 : 250).setInterpolator(new AccelerateInterpolator(2.0f)).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.LightsOutNotifController.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    LightsOutNotifController.this.mLightsOutNotifView.setAlpha(shouldShowDot ? 1.0f : 0.0f);
                    LightsOutNotifController.this.mLightsOutNotifView.setVisibility(shouldShowDot ? 0 : 8);
                }
            }).start();
        }
    }

    @VisibleForTesting
    boolean isShowingDot() {
        return this.mLightsOutNotifView.getVisibility() == 0 && this.mLightsOutNotifView.getAlpha() == 1.0f;
    }

    @VisibleForTesting
    boolean shouldShowDot() {
        return hasActiveNotifications() && areLightsOut();
    }

    @VisibleForTesting
    boolean areLightsOut() {
        return (this.mAppearance & 4) != 0;
    }
}
