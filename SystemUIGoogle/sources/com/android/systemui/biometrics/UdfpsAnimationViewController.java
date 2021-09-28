package com.android.systemui.biometrics;

import android.graphics.PointF;
import android.graphics.RectF;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.UdfpsAnimationView;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.ViewController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class UdfpsAnimationViewController<T extends UdfpsAnimationView> extends ViewController<T> implements Dumpable {
    final DumpManager mDumpManger;
    boolean mNotificationShadeExpanded;
    final StatusBar mStatusBar;
    private final StatusBar.ExpansionChangedListener mStatusBarExpansionChangedListener = new StatusBar.ExpansionChangedListener() { // from class: com.android.systemui.biometrics.UdfpsAnimationViewController.1
        @Override // com.android.systemui.statusbar.phone.StatusBar.ExpansionChangedListener
        public void onExpansionChanged(float f, boolean z) {
            UdfpsAnimationViewController udfpsAnimationViewController = UdfpsAnimationViewController.this;
            udfpsAnimationViewController.mNotificationShadeExpanded = z;
            ((UdfpsAnimationView) ((ViewController) udfpsAnimationViewController).mView).onExpansionChanged(f, z);
            UdfpsAnimationViewController.this.updatePauseAuth();
        }
    };
    final StatusBarStateController mStatusBarStateController;

    /* access modifiers changed from: package-private */
    public int getPaddingX() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getPaddingY() {
        return 0;
    }

    abstract String getTag();

    /* access modifiers changed from: package-private */
    public boolean listenForTouchesOutsideView() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public void onTouchOutsideView() {
    }

    /* access modifiers changed from: protected */
    public UdfpsAnimationViewController(T t, StatusBarStateController statusBarStateController, StatusBar statusBar, DumpManager dumpManager) {
        super(t);
        this.mStatusBarStateController = statusBarStateController;
        this.mStatusBar = statusBar;
        this.mDumpManger = dumpManager;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.mStatusBar.addExpansionChangedListener(this.mStatusBarExpansionChangedListener);
        this.mDumpManger.registerDumpable(getDumpTag(), this);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        this.mStatusBar.removeExpansionChangedListener(this.mStatusBarExpansionChangedListener);
        this.mDumpManger.unregisterDumpable(getDumpTag());
    }

    private String getDumpTag() {
        return getTag() + " (" + this + ")";
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("mNotificationShadeExpanded=" + this.mNotificationShadeExpanded);
        printWriter.println("shouldPauseAuth()=" + shouldPauseAuth());
        printWriter.println("isPauseAuth=" + ((UdfpsAnimationView) this.mView).isPauseAuth());
    }

    /* access modifiers changed from: package-private */
    public boolean shouldPauseAuth() {
        return this.mNotificationShadeExpanded;
    }

    /* access modifiers changed from: package-private */
    public void updatePauseAuth() {
        if (((UdfpsAnimationView) this.mView).setPauseAuth(shouldPauseAuth())) {
            ((UdfpsAnimationView) this.mView).postInvalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public void onSensorRectUpdated(RectF rectF) {
        ((UdfpsAnimationView) this.mView).onSensorRectUpdated(rectF);
    }

    /* access modifiers changed from: package-private */
    public void dozeTimeTick() {
        if (((UdfpsAnimationView) this.mView).dozeTimeTick()) {
            ((UdfpsAnimationView) this.mView).postInvalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public PointF getTouchTranslation() {
        return new PointF(0.0f, 0.0f);
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStarting() {
        ((UdfpsAnimationView) this.mView).onIlluminationStarting();
        ((UdfpsAnimationView) this.mView).postInvalidate();
    }

    /* access modifiers changed from: package-private */
    public void onIlluminationStopped() {
        ((UdfpsAnimationView) this.mView).onIlluminationStopped();
        ((UdfpsAnimationView) this.mView).postInvalidate();
    }
}
