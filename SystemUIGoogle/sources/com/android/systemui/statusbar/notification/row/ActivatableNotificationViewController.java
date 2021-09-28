package com.android.systemui.statusbar.notification.row;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public class ActivatableNotificationViewController extends ViewController<ActivatableNotificationView> {
    private final AccessibilityManager mAccessibilityManager;
    private final ExpandableOutlineViewController mExpandableOutlineViewController;
    private final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final NotificationTapHelper mNotificationTapHelper;
    private final TouchHandler mTouchHandler = new TouchHandler();

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
    }

    public ActivatableNotificationViewController(ActivatableNotificationView activatableNotificationView, NotificationTapHelper.Factory factory, ExpandableOutlineViewController expandableOutlineViewController, AccessibilityManager accessibilityManager, FalsingManager falsingManager, FalsingCollector falsingCollector) {
        super(activatableNotificationView);
        this.mExpandableOutlineViewController = expandableOutlineViewController;
        this.mAccessibilityManager = accessibilityManager;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        ActivatableNotificationViewController$$ExternalSyntheticLambda0 activatableNotificationViewController$$ExternalSyntheticLambda0 = new NotificationTapHelper.ActivationListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.phone.NotificationTapHelper.ActivationListener
            public final void onActiveChanged(boolean z) {
                ActivatableNotificationViewController.this.lambda$new$0(z);
            }
        };
        ActivatableNotificationView activatableNotificationView2 = (ActivatableNotificationView) this.mView;
        Objects.requireNonNull(activatableNotificationView2);
        ActivatableNotificationViewController$$ExternalSyntheticLambda1 activatableNotificationViewController$$ExternalSyntheticLambda1 = new NotificationTapHelper.DoubleTapListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController$$ExternalSyntheticLambda1
            @Override // com.android.systemui.statusbar.phone.NotificationTapHelper.DoubleTapListener
            public final boolean onDoubleTap() {
                return ActivatableNotificationView.this.performClick();
            }
        };
        ActivatableNotificationView activatableNotificationView3 = (ActivatableNotificationView) this.mView;
        Objects.requireNonNull(activatableNotificationView3);
        this.mNotificationTapHelper = factory.create(activatableNotificationViewController$$ExternalSyntheticLambda0, activatableNotificationViewController$$ExternalSyntheticLambda1, new NotificationTapHelper.SlideBackListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController$$ExternalSyntheticLambda2
            @Override // com.android.systemui.statusbar.phone.NotificationTapHelper.SlideBackListener
            public final boolean onSlideBack() {
                return ActivatableNotificationView.this.handleSlideBack();
            }
        });
        ((ActivatableNotificationView) this.mView).setOnActivatedListener(new ActivatableNotificationView.OnActivatedListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController.1
            @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView.OnActivatedListener
            public void onActivationReset(ActivatableNotificationView activatableNotificationView4) {
            }

            @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView.OnActivatedListener
            public void onActivated(ActivatableNotificationView activatableNotificationView4) {
                ActivatableNotificationViewController.this.mFalsingCollector.onNotificationActive();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z) {
        if (z) {
            ((ActivatableNotificationView) this.mView).makeActive();
            this.mFalsingCollector.onNotificationActive();
            return;
        }
        ((ActivatableNotificationView) this.mView).makeInactive(true);
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mExpandableOutlineViewController.init();
        ((ActivatableNotificationView) this.mView).setOnTouchListener(this.mTouchHandler);
        ((ActivatableNotificationView) this.mView).setTouchHandler(this.mTouchHandler);
        ((ActivatableNotificationView) this.mView).setAccessibilityManager(this.mAccessibilityManager);
    }

    /* loaded from: classes.dex */
    class TouchHandler implements Gefingerpoken, View.OnTouchListener {
        private boolean mBlockNextTouch;

        @Override // com.android.systemui.Gefingerpoken
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.android.systemui.Gefingerpoken
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        TouchHandler() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (this.mBlockNextTouch) {
                this.mBlockNextTouch = false;
                return true;
            }
            if (motionEvent.getAction() == 1) {
                ((ActivatableNotificationView) ((ViewController) ActivatableNotificationViewController.this).mView).setLastActionUpTime(SystemClock.uptimeMillis());
            }
            if (ActivatableNotificationViewController.this.mAccessibilityManager.isTouchExplorationEnabled() || motionEvent.getAction() != 1) {
                return false;
            }
            boolean isFalseTap = ActivatableNotificationViewController.this.mFalsingManager.isFalseTap(1);
            if (!isFalseTap && (view instanceof ActivatableNotificationView)) {
                ((ActivatableNotificationView) view).onTap();
            }
            return isFalseTap;
        }
    }
}
