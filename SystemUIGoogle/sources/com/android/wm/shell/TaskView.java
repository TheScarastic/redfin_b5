package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.util.CloseGuard;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class TaskView extends SurfaceView implements SurfaceHolder.Callback, ShellTaskOrganizer.TaskListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    private final CloseGuard mGuard;
    private boolean mIsInitialized;
    private Listener mListener;
    private Executor mListenerExecutor;
    private Rect mObscuredTouchRect;
    private final Executor mShellExecutor;
    private boolean mSurfaceCreated;
    private ActivityManager.RunningTaskInfo mTaskInfo;
    private SurfaceControl mTaskLeash;
    private final ShellTaskOrganizer mTaskOrganizer;
    private WindowContainerToken mTaskToken;
    private final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();
    private final Rect mTmpRect = new Rect();
    private final Rect mTmpRootRect = new Rect();
    private final int[] mTmpLocation = new int[2];

    /* loaded from: classes2.dex */
    public interface Listener {
        default void onBackPressedOnTaskRoot(int i) {
        }

        default void onInitialized() {
        }

        default void onReleased() {
        }

        default void onTaskCreated(int i, ComponentName componentName) {
        }

        default void onTaskRemovalStarted(int i) {
        }

        default void onTaskVisibilityChanged(int i, boolean z) {
        }
    }

    public TaskView(Context context, ShellTaskOrganizer shellTaskOrganizer) {
        super(context, null, 0, 0, true);
        CloseGuard closeGuard = new CloseGuard();
        this.mGuard = closeGuard;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellTaskOrganizer.getExecutor();
        setUseAlpha();
        getHolder().addCallback(this);
        closeGuard.open("release");
    }

    public void setListener(Executor executor, Listener listener) {
        if (this.mListener == null) {
            this.mListener = listener;
            this.mListenerExecutor = executor;
            return;
        }
        throw new IllegalStateException("Trying to set a listener when one has already been set");
    }

    public void startShortcutActivity(ShortcutInfo shortcutInfo, ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        try {
            ((LauncherApps) ((SurfaceView) this).mContext.getSystemService(LauncherApps.class)).startShortcut(shortcutInfo, null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startActivity(PendingIntent pendingIntent, Intent intent, ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        try {
            pendingIntent.send(((SurfaceView) this).mContext, 0, intent, null, null, null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareActivityOptions(ActivityOptions activityOptions, Rect rect) {
        Binder binder = new Binder();
        this.mShellExecutor.execute(new Runnable(binder) { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda9
            public final /* synthetic */ Binder f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                TaskView.this.lambda$prepareActivityOptions$0(this.f$1);
            }
        });
        activityOptions.setLaunchBounds(rect);
        activityOptions.setLaunchCookie(binder);
        activityOptions.setLaunchWindowingMode(6);
        activityOptions.setRemoveWithTaskOrganizer(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareActivityOptions$0(Binder binder) {
        this.mTaskOrganizer.setPendingLaunchCookieListener(binder, this);
    }

    public void setObscuredTouchRect(Rect rect) {
        this.mObscuredTouchRect = rect;
    }

    public void onLocationChanged() {
        if (this.mTaskToken != null) {
            getBoundsOnScreen(this.mTmpRect);
            getRootView().getBoundsOnScreen(this.mTmpRootRect);
            if (!this.mTmpRootRect.contains(this.mTmpRect)) {
                this.mTmpRect.offsetTo(0, 0);
            }
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.setBounds(this.mTaskToken, this.mTmpRect);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    public void release() {
        performRelease();
    }

    @Override // java.lang.Object
    protected void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
                performRelease();
            }
        } finally {
            super.finalize();
        }
    }

    private void performRelease() {
        getHolder().removeCallback(this);
        this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                TaskView.this.lambda$performRelease$1();
            }
        });
        this.mGuard.close();
        if (this.mListener != null && this.mIsInitialized) {
            this.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskView.this.lambda$performRelease$2();
                }
            });
            this.mIsInitialized = false;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performRelease$1() {
        this.mTaskOrganizer.removeListener(this);
        resetTaskInfo();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performRelease$2() {
        this.mListener.onReleased();
    }

    private void resetTaskInfo() {
        this.mTaskInfo = null;
        this.mTaskToken = null;
        this.mTaskLeash = null;
    }

    private void updateTaskVisibility() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mTaskToken, !this.mSurfaceCreated);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        if (this.mListener != null) {
            this.mListenerExecutor.execute(new Runnable(this.mTaskInfo.taskId) { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda6
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    TaskView.this.lambda$updateTaskVisibility$3(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTaskVisibility$3(int i) {
        this.mListener.onTaskVisibilityChanged(i, this.mSurfaceCreated);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        this.mTaskInfo = runningTaskInfo;
        this.mTaskToken = runningTaskInfo.token;
        this.mTaskLeash = surfaceControl;
        if (this.mSurfaceCreated) {
            this.mTransaction.reparent(surfaceControl, getSurfaceControl()).show(this.mTaskLeash).apply();
        } else {
            updateTaskVisibility();
        }
        this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, true);
        onLocationChanged();
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription != null) {
            setResizeBackgroundColor(taskDescription.getBackgroundColor());
        }
        if (this.mListener != null) {
            this.mListenerExecutor.execute(new Runnable(runningTaskInfo.taskId, runningTaskInfo.baseActivity) { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda8
                public final /* synthetic */ int f$1;
                public final /* synthetic */ ComponentName f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    TaskView.this.lambda$onTaskAppeared$4(this.f$1, this.f$2);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$4(int i, ComponentName componentName) {
        this.mListener.onTaskCreated(i, componentName);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token)) {
            if (this.mListener != null) {
                this.mListenerExecutor.execute(new Runnable(runningTaskInfo.taskId) { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda7
                    public final /* synthetic */ int f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        TaskView.this.lambda$onTaskVanished$5(this.f$1);
                    }
                });
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, false);
            this.mTransaction.reparent(this.mTaskLeash, null).apply();
            resetTaskInfo();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$5(int i) {
        this.mListener.onTaskRemovalStarted(i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription != null) {
            setResizeBackgroundColor(taskDescription.getBackgroundColor());
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token) && this.mListener != null) {
            this.mListenerExecutor.execute(new Runnable(runningTaskInfo.taskId) { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda5
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    TaskView.this.lambda$onBackPressedOnTaskRoot$6(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackPressedOnTaskRoot$6(int i) {
        this.mListener.onBackPressedOnTaskRoot(i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mTaskInfo.taskId == i) {
            builder.setParent(this.mTaskLeash);
            return;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        printWriter.println(str + this);
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskView:");
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        sb.append(runningTaskInfo != null ? Integer.valueOf(runningTaskInfo.taskId) : "null");
        return sb.toString();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = true;
        if (this.mListener != null && !this.mIsInitialized) {
            this.mIsInitialized = true;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    TaskView.this.lambda$surfaceCreated$7();
                }
            });
        }
        this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                TaskView.this.lambda$surfaceCreated$8();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceCreated$7() {
        this.mListener.onInitialized();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceCreated$8() {
        if (this.mTaskToken != null) {
            this.mTransaction.reparent(this.mTaskLeash, getSurfaceControl()).show(this.mTaskLeash).apply();
            updateTaskVisibility();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.mTaskToken != null) {
            onLocationChanged();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = false;
        this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TaskView.this.lambda$surfaceDestroyed$9();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceDestroyed$9() {
        if (this.mTaskToken != null) {
            this.mTransaction.reparent(this.mTaskLeash, null).apply();
            updateTaskVisibility();
        }
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        if (internalInsetsInfo.touchableRegion.isEmpty()) {
            internalInsetsInfo.setTouchableInsets(3);
            View rootView = getRootView();
            rootView.getLocationInWindow(this.mTmpLocation);
            Rect rect = this.mTmpRootRect;
            int[] iArr = this.mTmpLocation;
            rect.set(iArr[0], iArr[1], rootView.getWidth(), rootView.getHeight());
            internalInsetsInfo.touchableRegion.set(this.mTmpRootRect);
        }
        getLocationInWindow(this.mTmpLocation);
        Rect rect2 = this.mTmpRect;
        int[] iArr2 = this.mTmpLocation;
        rect2.set(iArr2[0], iArr2[1], iArr2[0] + getWidth(), this.mTmpLocation[1] + getHeight());
        internalInsetsInfo.touchableRegion.op(this.mTmpRect, Region.Op.DIFFERENCE);
        Rect rect3 = this.mObscuredTouchRect;
        if (rect3 != null) {
            internalInsetsInfo.touchableRegion.union(rect3);
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }
}
