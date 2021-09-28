package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ViewClippingUtil;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class HeadsUpAppearanceController implements OnHeadsUpChangedListener, DarkIconDispatcher.DarkReceiver, NotificationWakeUpCoordinator.WakeUpListener {
    private boolean mAnimationsEnabled;
    @VisibleForTesting
    float mAppearFraction;
    private final KeyguardBypassController mBypassController;
    private final View mCenteredIconView;
    private final View mClockView;
    private final CommandQueue mCommandQueue;
    private final DarkIconDispatcher mDarkIconDispatcher;
    @VisibleForTesting
    float mExpandedHeight;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private final HeadsUpStatusBarView mHeadsUpStatusBarView;
    @VisibleForTesting
    boolean mIsExpanded;
    private KeyguardStateController mKeyguardStateController;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private final NotificationPanelViewController mNotificationPanelViewController;
    private final View mOperatorNameView;
    private final ViewClippingUtil.ClippingParameters mParentClippingParams;
    private final BiConsumer<Float, Float> mSetExpandedHeight;
    private final Consumer<ExpandableNotificationRow> mSetTrackingHeadsUp;
    private boolean mShown;
    private final NotificationStackScrollLayoutController mStackScrollerController;
    private final StatusBarStateController mStatusBarStateController;
    private ExpandableNotificationRow mTrackedChild;
    private final NotificationWakeUpCoordinator mWakeUpCoordinator;

    public HeadsUpAppearanceController(NotificationIconAreaController notificationIconAreaController, HeadsUpManagerPhone headsUpManagerPhone, NotificationStackScrollLayoutController notificationStackScrollLayoutController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, CommandQueue commandQueue, NotificationPanelViewController notificationPanelViewController, View view) {
        this(notificationIconAreaController, headsUpManagerPhone, sysuiStatusBarStateController, keyguardBypassController, notificationWakeUpCoordinator, keyguardStateController, commandQueue, notificationStackScrollLayoutController, notificationPanelViewController, (HeadsUpStatusBarView) view.findViewById(R$id.heads_up_status_bar_view), view.findViewById(R$id.clock), view.findViewById(R$id.operator_name_frame), view.findViewById(R$id.centered_icon_area));
    }

    @VisibleForTesting
    public HeadsUpAppearanceController(NotificationIconAreaController notificationIconAreaController, HeadsUpManagerPhone headsUpManagerPhone, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardStateController keyguardStateController, CommandQueue commandQueue, NotificationStackScrollLayoutController notificationStackScrollLayoutController, NotificationPanelViewController notificationPanelViewController, HeadsUpStatusBarView headsUpStatusBarView, View view, View view2, View view3) {
        HeadsUpAppearanceController$$ExternalSyntheticLambda5 headsUpAppearanceController$$ExternalSyntheticLambda5 = new Consumer() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HeadsUpAppearanceController.this.setTrackingHeadsUp((ExpandableNotificationRow) obj);
            }
        };
        this.mSetTrackingHeadsUp = headsUpAppearanceController$$ExternalSyntheticLambda5;
        HeadsUpAppearanceController$$ExternalSyntheticLambda3 headsUpAppearanceController$$ExternalSyntheticLambda3 = new BiConsumer() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                HeadsUpAppearanceController.this.setAppearFraction(((Float) obj).floatValue(), ((Float) obj2).floatValue());
            }
        };
        this.mSetExpandedHeight = headsUpAppearanceController$$ExternalSyntheticLambda3;
        this.mParentClippingParams = new ViewClippingUtil.ClippingParameters() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController.1
            public boolean shouldFinish(View view4) {
                return view4.getId() == R$id.status_bar;
            }
        };
        this.mAnimationsEnabled = true;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mHeadsUpManager = headsUpManagerPhone;
        headsUpManagerPhone.addListener(this);
        this.mHeadsUpStatusBarView = headsUpStatusBarView;
        this.mCenteredIconView = view3;
        headsUpStatusBarView.setOnDrawingRectChangedListener(new Runnable() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                HeadsUpAppearanceController.this.lambda$new$0();
            }
        });
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        notificationPanelViewController.addTrackingHeadsUpListener(headsUpAppearanceController$$ExternalSyntheticLambda5);
        notificationPanelViewController.setHeadsUpAppearanceController(this);
        notificationStackScrollLayoutController.addOnExpandedHeightChangedListener(headsUpAppearanceController$$ExternalSyntheticLambda3);
        notificationStackScrollLayoutController.setHeadsUpAppearanceController(this);
        this.mClockView = view;
        this.mOperatorNameView = view2;
        DarkIconDispatcher darkIconDispatcher = (DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class);
        this.mDarkIconDispatcher = darkIconDispatcher;
        darkIconDispatcher.addDarkReceiver(this);
        headsUpStatusBarView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController.2
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view4, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (HeadsUpAppearanceController.this.shouldBeVisible()) {
                    HeadsUpAppearanceController.this.updateTopEntry();
                    HeadsUpAppearanceController.this.mStackScrollerController.requestLayout();
                }
                HeadsUpAppearanceController.this.mHeadsUpStatusBarView.removeOnLayoutChangeListener(this);
            }
        });
        this.mBypassController = keyguardBypassController;
        this.mStatusBarStateController = statusBarStateController;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        notificationWakeUpCoordinator.addListener(this);
        this.mCommandQueue = commandQueue;
        this.mKeyguardStateController = keyguardStateController;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateIsolatedIconLocation(true);
    }

    public void destroy() {
        this.mHeadsUpManager.removeListener(this);
        this.mHeadsUpStatusBarView.setOnDrawingRectChangedListener(null);
        this.mWakeUpCoordinator.removeListener(this);
        this.mNotificationPanelViewController.removeTrackingHeadsUpListener(this.mSetTrackingHeadsUp);
        this.mNotificationPanelViewController.setVerticalTranslationListener(null);
        this.mNotificationPanelViewController.setHeadsUpAppearanceController(null);
        this.mStackScrollerController.removeOnExpandedHeightChangedListener(this.mSetExpandedHeight);
        this.mDarkIconDispatcher.removeDarkReceiver(this);
    }

    private void updateIsolatedIconLocation(boolean z) {
        this.mNotificationIconAreaController.setIsolatedIconLocation(this.mHeadsUpStatusBarView.getIconDrawingRect(), z);
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpPinned(NotificationEntry notificationEntry) {
        updateTopEntry();
        lambda$updateHeadsUpHeaders$3(notificationEntry);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateTopEntry() {
        /*
            r5 = this;
            boolean r0 = r5.shouldBeVisible()
            r1 = 0
            if (r0 == 0) goto L_0x000e
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone r0 = r5.mHeadsUpManager
            com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.getTopEntry()
            goto L_0x000f
        L_0x000e:
            r0 = r1
        L_0x000f:
            com.android.systemui.statusbar.HeadsUpStatusBarView r2 = r5.mHeadsUpStatusBarView
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r2.getShowingEntry()
            com.android.systemui.statusbar.HeadsUpStatusBarView r3 = r5.mHeadsUpStatusBarView
            r3.setEntry(r0)
            if (r0 == r2) goto L_0x0043
            r3 = 1
            r4 = 0
            if (r0 != 0) goto L_0x0027
            r5.setShown(r4)
            boolean r2 = r5.mIsExpanded
        L_0x0025:
            r2 = r2 ^ r3
            goto L_0x0030
        L_0x0027:
            if (r2 != 0) goto L_0x002f
            r5.setShown(r3)
            boolean r2 = r5.mIsExpanded
            goto L_0x0025
        L_0x002f:
            r2 = r4
        L_0x0030:
            r5.updateIsolatedIconLocation(r4)
            com.android.systemui.statusbar.phone.NotificationIconAreaController r5 = r5.mNotificationIconAreaController
            if (r0 != 0) goto L_0x0038
            goto L_0x0040
        L_0x0038:
            com.android.systemui.statusbar.notification.icon.IconPack r0 = r0.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r1 = r0.getStatusBarIcon()
        L_0x0040:
            r5.showIconIsolated(r1, r2)
        L_0x0043:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.HeadsUpAppearanceController.updateTopEntry():void");
    }

    private void setShown(boolean z) {
        if (this.mShown != z) {
            this.mShown = z;
            if (z) {
                updateParentClipping(false);
                this.mHeadsUpStatusBarView.setVisibility(0);
                show(this.mHeadsUpStatusBarView);
                hide(this.mClockView, 4);
                if (this.mCenteredIconView.getVisibility() != 8) {
                    hide(this.mCenteredIconView, 4);
                }
                View view = this.mOperatorNameView;
                if (view != null) {
                    hide(view, 4);
                }
            } else {
                show(this.mClockView);
                if (this.mCenteredIconView.getVisibility() != 8) {
                    show(this.mCenteredIconView);
                }
                View view2 = this.mOperatorNameView;
                if (view2 != null) {
                    show(view2);
                }
                hide(this.mHeadsUpStatusBarView, 8, new Runnable() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        HeadsUpAppearanceController.this.lambda$setShown$1();
                    }
                });
            }
            if (this.mStatusBarStateController.getState() != 0) {
                this.mCommandQueue.recomputeDisableFlags(this.mHeadsUpStatusBarView.getContext().getDisplayId(), false);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setShown$1() {
        updateParentClipping(true);
    }

    private void updateParentClipping(boolean z) {
        ViewClippingUtil.setClippingDeactivated(this.mHeadsUpStatusBarView, !z, this.mParentClippingParams);
    }

    private void hide(View view, int i) {
        hide(view, i, null);
    }

    private void hide(View view, int i, Runnable runnable) {
        if (this.mAnimationsEnabled) {
            CrossFadeHelper.fadeOut(view, 110, 0, new Runnable(view, i, runnable) { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda0
                public final /* synthetic */ View f$0;
                public final /* synthetic */ int f$1;
                public final /* synthetic */ Runnable f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    HeadsUpAppearanceController.lambda$hide$2(this.f$0, this.f$1, this.f$2);
                }
            });
            return;
        }
        view.setVisibility(i);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$hide$2(View view, int i, Runnable runnable) {
        view.setVisibility(i);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void show(View view) {
        if (this.mAnimationsEnabled) {
            CrossFadeHelper.fadeIn(view, 110, 100);
        } else {
            view.setVisibility(0);
        }
    }

    @VisibleForTesting
    void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
    }

    @VisibleForTesting
    public boolean isShown() {
        return this.mShown;
    }

    public boolean shouldBeVisible() {
        boolean z = !this.mWakeUpCoordinator.getNotificationsFullyHidden();
        boolean z2 = !this.mIsExpanded && z;
        if (this.mBypassController.getBypassEnabled() && ((this.mStatusBarStateController.getState() == 1 || this.mKeyguardStateController.isKeyguardGoingAway()) && z)) {
            z2 = true;
        }
        if (!z2 || !this.mHeadsUpManager.hasPinnedHeadsUp()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
        updateTopEntry();
        lambda$updateHeadsUpHeaders$3(notificationEntry);
    }

    public void setAppearFraction(float f, float f2) {
        boolean z = true;
        boolean z2 = f != this.mExpandedHeight;
        this.mExpandedHeight = f;
        this.mAppearFraction = f2;
        if (f <= 0.0f) {
            z = false;
        }
        if (z2) {
            updateHeadsUpHeaders();
        }
        if (z != this.mIsExpanded) {
            this.mIsExpanded = z;
            updateTopEntry();
        }
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2 = this.mTrackedChild;
        this.mTrackedChild = expandableNotificationRow;
        if (expandableNotificationRow2 != null) {
            lambda$updateHeadsUpHeaders$3(expandableNotificationRow2.getEntry());
        }
    }

    private void updateHeadsUpHeaders() {
        this.mHeadsUpManager.getAllEntries().forEach(new Consumer() { // from class: com.android.systemui.statusbar.phone.HeadsUpAppearanceController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HeadsUpAppearanceController.this.lambda$updateHeadsUpHeaders$3((NotificationEntry) obj);
            }
        });
    }

    /* renamed from: updateHeader */
    public void lambda$updateHeadsUpHeaders$3(NotificationEntry notificationEntry) {
        float f;
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row.isPinned() || row.isHeadsUpAnimatingAway() || row == this.mTrackedChild || row.showingPulsing()) {
            f = this.mAppearFraction;
        } else {
            f = 1.0f;
        }
        row.setHeaderVisibleAmount(f);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        this.mHeadsUpStatusBarView.onDarkChanged(rect, f, i);
    }

    public void onStateChanged() {
        updateTopEntry();
    }

    /* access modifiers changed from: package-private */
    public void readFrom(HeadsUpAppearanceController headsUpAppearanceController) {
        if (headsUpAppearanceController != null) {
            this.mTrackedChild = headsUpAppearanceController.mTrackedChild;
            this.mExpandedHeight = headsUpAppearanceController.mExpandedHeight;
            this.mIsExpanded = headsUpAppearanceController.mIsExpanded;
            this.mAppearFraction = headsUpAppearanceController.mAppearFraction;
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
    public void onFullyHiddenChanged(boolean z) {
        updateTopEntry();
    }
}
