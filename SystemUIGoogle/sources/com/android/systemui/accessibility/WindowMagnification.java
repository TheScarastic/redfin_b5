package com.android.systemui.accessibility;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceControl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.SystemUI;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class WindowMagnification extends SystemUI implements WindowMagnifierCallback, CommandQueue.Callbacks {
    private final AccessibilityManager mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class);
    @VisibleForTesting
    DisplayIdIndexSupplier<WindowMagnificationAnimationController> mAnimationControllerSupplier;
    private final CommandQueue mCommandQueue;
    private final Handler mHandler;
    private Configuration mLastConfiguration;
    private final ModeSwitchesController mModeSwitchesController;
    private final OverviewProxyService mOverviewProxyService;
    private SysUiState mSysUiState;
    private WindowMagnificationConnectionImpl mWindowMagnificationConnectionImpl;

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnimationControllerSupplier extends DisplayIdIndexSupplier<WindowMagnificationAnimationController> {
        private final Context mContext;
        private final Handler mHandler;
        private final SysUiState mSysUiState;
        private final WindowMagnifierCallback mWindowMagnifierCallback;

        AnimationControllerSupplier(Context context, Handler handler, WindowMagnifierCallback windowMagnifierCallback, DisplayManager displayManager, SysUiState sysUiState) {
            super(displayManager);
            this.mContext = context;
            this.mHandler = handler;
            this.mWindowMagnifierCallback = windowMagnifierCallback;
            this.mSysUiState = sysUiState;
        }

        @Override // com.android.systemui.accessibility.DisplayIdIndexSupplier
        public WindowMagnificationAnimationController createInstance(Display display) {
            return new WindowMagnificationAnimationController(this.mContext.createWindowContext(display, 2039, null), new WindowMagnificationController(this.mContext, this.mHandler, new SfVsyncFrameCallbackProvider(), null, new SurfaceControl.Transaction(), this.mWindowMagnifierCallback, this.mSysUiState));
        }
    }

    public WindowMagnification(Context context, Handler handler, CommandQueue commandQueue, ModeSwitchesController modeSwitchesController, SysUiState sysUiState, OverviewProxyService overviewProxyService) {
        super(context);
        this.mHandler = handler;
        this.mLastConfiguration = new Configuration(context.getResources().getConfiguration());
        this.mCommandQueue = commandQueue;
        this.mModeSwitchesController = modeSwitchesController;
        this.mSysUiState = sysUiState;
        this.mOverviewProxyService = overviewProxyService;
        this.mAnimationControllerSupplier = new AnimationControllerSupplier(context, handler, this, (DisplayManager) context.getSystemService(DisplayManager.class), sysUiState);
    }

    @Override // com.android.systemui.SystemUI
    public void onConfigurationChanged(Configuration configuration) {
        int diff = configuration.diff(this.mLastConfiguration);
        this.mLastConfiguration.setTo(configuration);
        this.mAnimationControllerSupplier.forEach(new Consumer(diff) { // from class: com.android.systemui.accessibility.WindowMagnification$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowMagnification.$r8$lambda$xAKD3X5_TM7cBvKz_fkVSOLulqo(this.f$0, (WindowMagnificationAnimationController) obj);
            }
        });
        ModeSwitchesController modeSwitchesController = this.mModeSwitchesController;
        if (modeSwitchesController != null) {
            modeSwitchesController.onConfigurationChanged(diff);
        }
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.accessibility.WindowMagnification.1
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public void onConnectionChanged(boolean z) {
                if (z) {
                    WindowMagnification.this.updateSysUiStateFlag();
                }
            }
        });
    }

    public void updateSysUiStateFlag() {
        WindowMagnificationAnimationController valueAt = this.mAnimationControllerSupplier.valueAt(0);
        if (valueAt != null) {
            valueAt.updateSysUiStateFlag();
        } else {
            this.mSysUiState.setFlag(524288, false).commitUpdate(0);
        }
    }

    public void enableWindowMagnification(int i, float f, float f2, float f3, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        WindowMagnificationAnimationController windowMagnificationAnimationController = this.mAnimationControllerSupplier.get(i);
        if (windowMagnificationAnimationController != null) {
            windowMagnificationAnimationController.enableWindowMagnification(f, f2, f3, iRemoteMagnificationAnimationCallback);
        }
    }

    public void setScale(int i, float f) {
        WindowMagnificationAnimationController windowMagnificationAnimationController = this.mAnimationControllerSupplier.get(i);
        if (windowMagnificationAnimationController != null) {
            windowMagnificationAnimationController.setScale(f);
        }
    }

    public void moveWindowMagnifier(int i, float f, float f2) {
        WindowMagnificationAnimationController windowMagnificationAnimationController = this.mAnimationControllerSupplier.get(i);
        if (windowMagnificationAnimationController != null) {
            windowMagnificationAnimationController.moveWindowMagnifier(f, f2);
        }
    }

    public void disableWindowMagnification(int i, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        WindowMagnificationAnimationController windowMagnificationAnimationController = this.mAnimationControllerSupplier.get(i);
        if (windowMagnificationAnimationController != null) {
            windowMagnificationAnimationController.deleteWindowMagnification(iRemoteMagnificationAnimationCallback);
        }
    }

    @Override // com.android.systemui.accessibility.WindowMagnifierCallback
    public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onWindowMagnifierBoundsChanged(i, rect);
        }
    }

    @Override // com.android.systemui.accessibility.WindowMagnifierCallback
    public void onSourceBoundsChanged(int i, Rect rect) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onSourceBoundsChanged(i, rect);
        }
    }

    @Override // com.android.systemui.accessibility.WindowMagnifierCallback
    public void onPerformScaleAction(int i, float f) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onPerformScaleAction(i, f);
        }
    }

    @Override // com.android.systemui.accessibility.WindowMagnifierCallback
    public void onAccessibilityActionPerformed(int i) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
        if (windowMagnificationConnectionImpl != null) {
            windowMagnificationConnectionImpl.onAccessibilityActionPerformed(i);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void requestWindowMagnificationConnection(boolean z) {
        if (z) {
            setWindowMagnificationConnection();
        } else {
            clearWindowMagnificationConnection();
        }
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("WindowMagnification");
        this.mAnimationControllerSupplier.forEach(new Consumer(printWriter) { // from class: com.android.systemui.accessibility.WindowMagnification$$ExternalSyntheticLambda1
            public final /* synthetic */ PrintWriter f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowMagnification.m51$r8$lambda$Y3FRlCclCMW6kYgwkHgJsSvCMY(this.f$0, (WindowMagnificationAnimationController) obj);
            }
        });
    }

    private void setWindowMagnificationConnection() {
        if (this.mWindowMagnificationConnectionImpl == null) {
            this.mWindowMagnificationConnectionImpl = new WindowMagnificationConnectionImpl(this, this.mHandler, this.mModeSwitchesController);
        }
        this.mAccessibilityManager.setWindowMagnificationConnection(this.mWindowMagnificationConnectionImpl);
    }

    private void clearWindowMagnificationConnection() {
        this.mAccessibilityManager.setWindowMagnificationConnection(null);
    }
}
