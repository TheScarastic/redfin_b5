package com.android.wm.shell.common.split;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public final class SplitWindowManager extends WindowlessWindowManager {
    private static final String TAG = SplitWindowManager.class.getSimpleName();
    private Context mContext;
    private DividerView mDividerView;
    private SurfaceControl mLeash;
    private final ParentContainerCallbacks mParentContainerCallbacks;
    private boolean mResizingSplits;
    private SurfaceControlViewHost mViewHost;
    private final String mWindowName;

    /* loaded from: classes2.dex */
    public interface ParentContainerCallbacks {
        void attachToParentSurface(SurfaceControl.Builder builder);
    }

    public SplitWindowManager(String str, Context context, Configuration configuration, ParentContainerCallbacks parentContainerCallbacks) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mContext = context.createConfigurationContext(configuration);
        this.mParentContainerCallbacks = parentContainerCallbacks;
        this.mWindowName = str;
    }

    public void setTouchRegion(IBinder iBinder, Region region) {
        SplitWindowManager.super.setTouchRegion(iBinder, region);
    }

    public SurfaceControl getSurfaceControl(IWindow iWindow) {
        return SplitWindowManager.super.getSurfaceControl(iWindow);
    }

    public void setConfiguration(Configuration configuration) {
        SplitWindowManager.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }

    protected void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName(TAG).setHidden(false).setCallsite("SplitWindowManager#attachToParentSurface");
        this.mParentContainerCallbacks.attachToParentSurface(callsite);
        SurfaceControl build = callsite.build();
        this.mLeash = build;
        builder.setParent(build);
    }

    /* access modifiers changed from: package-private */
    public void init(SplitLayout splitLayout) {
        if (this.mDividerView == null && this.mViewHost == null) {
            Context context = this.mContext;
            this.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), (WindowlessWindowManager) this);
            this.mDividerView = (DividerView) LayoutInflater.from(this.mContext).inflate(R.layout.split_divider, (ViewGroup) null);
            Rect dividerBounds = splitLayout.getDividerBounds();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(dividerBounds.width(), dividerBounds.height(), 2034, 545521704, -3);
            layoutParams.token = new Binder();
            layoutParams.setTitle(this.mWindowName);
            layoutParams.privateFlags |= 536870976;
            this.mViewHost.setView(this.mDividerView, layoutParams);
            this.mDividerView.setup(splitLayout, this.mViewHost);
            return;
        }
        throw new UnsupportedOperationException("Try to inflate divider view again without release first");
    }

    /* access modifiers changed from: package-private */
    public void release() {
        if (this.mDividerView != null) {
            this.mDividerView = null;
        }
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        if (this.mLeash != null) {
            new SurfaceControl.Transaction().remove(this.mLeash).apply();
            this.mLeash = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void setInteractive(boolean z) {
        DividerView dividerView = this.mDividerView;
        if (dividerView != null) {
            dividerView.setInteractive(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void setResizingSplits(boolean z) {
        if (z != this.mResizingSplits) {
            try {
                ActivityTaskManager.getService().setSplitScreenResizing(z);
                this.mResizingSplits = z;
            } catch (RemoteException e) {
                Slog.w(TAG, "Error calling setSplitScreenResizing", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getSurfaceControl() {
        return this.mLeash;
    }
}
