package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class StatusBarTouchableRegionManager implements Dumpable {
    private final Context mContext;
    private int mDisplayCutoutTouchableRegionSize;
    private final HeadsUpManagerPhone mHeadsUpManager;
    private View mNotificationPanelView;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private View mNotificationShadeWindowView;
    private StatusBar mStatusBar;
    private int mStatusBarHeight;
    private boolean mIsStatusBarExpanded = false;
    private boolean mShouldAdjustInsets = false;
    private boolean mForceCollapsedUntilLayout = false;
    private Region mTouchableRegion = new Region();
    private final ViewTreeObserver.OnComputeInternalInsetsListener mOnComputeInternalInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.5
        public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            if (!StatusBarTouchableRegionManager.this.mIsStatusBarExpanded && !StatusBarTouchableRegionManager.this.mStatusBar.isBouncerShowing()) {
                internalInsetsInfo.setTouchableInsets(3);
                internalInsetsInfo.touchableRegion.set(StatusBarTouchableRegionManager.this.calculateTouchableRegion());
            }
        }
    };

    public StatusBarTouchableRegionManager(Context context, NotificationShadeWindowController notificationShadeWindowController, ConfigurationController configurationController, HeadsUpManagerPhone headsUpManagerPhone) {
        this.mContext = context;
        initResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                StatusBarTouchableRegionManager.this.initResources();
            }
        });
        this.mHeadsUpManager = headsUpManagerPhone;
        headsUpManagerPhone.addListener(new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.2
            @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
            public void onHeadsUpPinnedModeChanged(boolean z) {
                if (Log.isLoggable("TouchableRegionManager", 5)) {
                    Log.w("TouchableRegionManager", "onHeadsUpPinnedModeChanged");
                }
                StatusBarTouchableRegionManager.this.updateTouchableRegion();
            }
        });
        headsUpManagerPhone.addHeadsUpPhoneListener(new HeadsUpManagerPhone.OnHeadsUpPhoneListenerChange() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.3
            @Override // com.android.systemui.statusbar.phone.HeadsUpManagerPhone.OnHeadsUpPhoneListenerChange
            public void onHeadsUpGoingAwayStateChanged(boolean z) {
                if (!z) {
                    StatusBarTouchableRegionManager.this.updateTouchableRegionAfterLayout();
                } else {
                    StatusBarTouchableRegionManager.this.updateTouchableRegion();
                }
            }
        });
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        notificationShadeWindowController.setForcePluginOpenListener(new NotificationShadeWindowController.ForcePluginOpenListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.NotificationShadeWindowController.ForcePluginOpenListener
            public final void onChange(boolean z) {
                StatusBarTouchableRegionManager.this.lambda$new$0(z);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(boolean z) {
        updateTouchableRegion();
    }

    /* access modifiers changed from: protected */
    public void setup(StatusBar statusBar, View view) {
        this.mStatusBar = statusBar;
        this.mNotificationShadeWindowView = view;
        this.mNotificationPanelView = view.findViewById(R$id.notification_panel);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarTouchableRegionManager state:");
        printWriter.print("  mTouchableRegion=");
        printWriter.println(this.mTouchableRegion);
    }

    /* access modifiers changed from: package-private */
    public void setPanelExpanded(boolean z) {
        if (z != this.mIsStatusBarExpanded) {
            this.mIsStatusBarExpanded = z;
            if (z) {
                this.mForceCollapsedUntilLayout = false;
            }
            updateTouchableRegion();
        }
    }

    /* access modifiers changed from: package-private */
    public Region calculateTouchableRegion() {
        Region touchableRegion = this.mHeadsUpManager.getTouchableRegion();
        if (touchableRegion != null) {
            this.mTouchableRegion.set(touchableRegion);
        } else {
            this.mTouchableRegion.set(0, 0, this.mNotificationShadeWindowView.getWidth(), this.mStatusBarHeight);
            updateRegionForNotch(this.mTouchableRegion);
        }
        return this.mTouchableRegion;
    }

    /* access modifiers changed from: private */
    public void initResources() {
        Resources resources = this.mContext.getResources();
        this.mDisplayCutoutTouchableRegionSize = resources.getDimensionPixelSize(17105196);
        this.mStatusBarHeight = resources.getDimensionPixelSize(17105525);
    }

    /* access modifiers changed from: private */
    public void updateTouchableRegion() {
        View view = this.mNotificationShadeWindowView;
        boolean z = true;
        boolean z2 = (view == null || view.getRootWindowInsets() == null || this.mNotificationShadeWindowView.getRootWindowInsets().getDisplayCutout() == null) ? false : true;
        if (!this.mHeadsUpManager.hasPinnedHeadsUp() && !this.mHeadsUpManager.isHeadsUpGoingAway() && !this.mForceCollapsedUntilLayout && !z2 && !this.mNotificationShadeWindowController.getForcePluginOpen()) {
            z = false;
        }
        if (z != this.mShouldAdjustInsets) {
            if (z) {
                this.mNotificationShadeWindowView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
                this.mNotificationShadeWindowView.requestLayout();
            } else {
                this.mNotificationShadeWindowView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
            }
            this.mShouldAdjustInsets = z;
        }
    }

    /* access modifiers changed from: private */
    public void updateTouchableRegionAfterLayout() {
        View view = this.mNotificationPanelView;
        if (view != null) {
            this.mForceCollapsedUntilLayout = true;
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager.4
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    if (!StatusBarTouchableRegionManager.this.mNotificationPanelView.isVisibleToUser()) {
                        StatusBarTouchableRegionManager.this.mNotificationPanelView.removeOnLayoutChangeListener(this);
                        StatusBarTouchableRegionManager.this.mForceCollapsedUntilLayout = false;
                        StatusBarTouchableRegionManager.this.updateTouchableRegion();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void updateRegionForNotch(Region region) {
        WindowInsets rootWindowInsets = this.mNotificationShadeWindowView.getRootWindowInsets();
        if (rootWindowInsets == null) {
            Log.w("TouchableRegionManager", "StatusBarWindowView is not attached.");
            return;
        }
        DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
        if (displayCutout != null) {
            Rect rect = new Rect();
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 48, rect);
            rect.offset(0, this.mDisplayCutoutTouchableRegionSize);
            region.union(rect);
        }
    }
}
