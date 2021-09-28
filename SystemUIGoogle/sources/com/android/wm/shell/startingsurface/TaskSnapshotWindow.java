package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ContextImpl;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.HardwareBuffer;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InsetsFlags;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.window.ClientWindowFrames;
import android.window.StartingWindowInfo;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.DecorView;
import com.android.internal.view.BaseIWindow;
import com.android.wm.shell.common.ShellExecutor;
/* loaded from: classes2.dex */
public class TaskSnapshotWindow {
    private static final String TAG = StartingSurfaceDrawer.TAG;
    private static final Point TMP_SURFACE_SIZE = new Point();
    private final int mActivityType;
    private final Paint mBackgroundPaint;
    private final Runnable mClearWindowHandler;
    private final long mDelayRemovalTime;
    private boolean mHasDrawn;
    private final int mOrientationOnCreation;
    private final IWindowSession mSession;
    private long mShownTime;
    private boolean mSizeMismatch;
    private TaskSnapshot mSnapshot;
    private final ShellExecutor mSplashScreenExecutor;
    private final int mStatusBarColor;
    private final SurfaceControl mSurfaceControl;
    private final SystemBarBackgroundPainter mSystemBarBackgroundPainter;
    private final Rect mTaskBounds;
    private final CharSequence mTitle;
    private final SurfaceControl.Transaction mTransaction;
    private final Window mWindow;
    private final Rect mFrame = new Rect();
    private final Rect mSystemBarInsets = new Rect();
    private final RectF mTmpSnapshotSize = new RectF();
    private final RectF mTmpDstFrame = new RectF();
    private final Matrix mSnapshotMatrix = new Matrix();
    private final float[] mTmpFloat9 = new float[9];

    /* access modifiers changed from: package-private */
    public static TaskSnapshotWindow create(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot, ShellExecutor shellExecutor, Runnable runnable) {
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo.taskInfo;
        int i = runningTaskInfo.taskId;
        WindowManager.LayoutParams layoutParams = startingWindowInfo.topOpaqueWindowLayoutParams;
        WindowManager.LayoutParams layoutParams2 = startingWindowInfo.mainWindowLayoutParams;
        InsetsState insetsState = startingWindowInfo.topOpaqueWindowInsetsState;
        if (layoutParams == null || layoutParams2 == null || insetsState == null) {
            String str = TAG;
            Slog.w(str, "unable to create taskSnapshot surface for task: " + i);
            return null;
        }
        WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
        int i2 = layoutParams.insetsFlags.appearance;
        int i3 = layoutParams.flags;
        int i4 = layoutParams.privateFlags;
        layoutParams3.packageName = layoutParams2.packageName;
        layoutParams3.windowAnimations = layoutParams2.windowAnimations;
        layoutParams3.dimAmount = layoutParams2.dimAmount;
        layoutParams3.type = 3;
        layoutParams3.format = taskSnapshot.getHardwareBuffer().getFormat();
        layoutParams3.flags = (-830922809 & i3) | 8 | 16;
        layoutParams3.privateFlags = (131072 & i4) | 536870912 | 33554432;
        layoutParams3.token = iBinder;
        layoutParams3.width = -1;
        layoutParams3.height = -1;
        InsetsFlags insetsFlags = layoutParams3.insetsFlags;
        insetsFlags.appearance = i2;
        insetsFlags.behavior = layoutParams.insetsFlags.behavior;
        layoutParams3.layoutInDisplayCutoutMode = layoutParams.layoutInDisplayCutoutMode;
        layoutParams3.setFitInsetsTypes(layoutParams.getFitInsetsTypes());
        layoutParams3.setFitInsetsSides(layoutParams.getFitInsetsSides());
        layoutParams3.setFitInsetsIgnoringVisibility(layoutParams.isFitInsetsIgnoringVisibility());
        layoutParams3.setTitle(String.format("SnapshotStartingWindow for taskId=%s", Integer.valueOf(i)));
        Point taskSize = taskSnapshot.getTaskSize();
        Rect rect = new Rect(0, 0, taskSize.x, taskSize.y);
        int orientation = taskSnapshot.getOrientation();
        int i5 = runningTaskInfo.topActivityType;
        int i6 = runningTaskInfo.displayId;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        SurfaceControl surfaceControl = new SurfaceControl();
        ClientWindowFrames clientWindowFrames = new ClientWindowFrames();
        InsetsSourceControl[] insetsSourceControlArr = new InsetsSourceControl[0];
        MergedConfiguration mergedConfiguration = new MergedConfiguration();
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription == null) {
            taskDescription = new ActivityManager.TaskDescription();
            taskDescription.setBackgroundColor(-1);
        }
        TaskSnapshotWindow taskSnapshotWindow = new TaskSnapshotWindow(surfaceControl, taskSnapshot, layoutParams3.getTitle(), taskDescription, i2, i3, i4, rect, orientation, i5, taskSnapshot.hasImeSurface() ? 350 : 100, insetsState, runnable, shellExecutor);
        Window window = taskSnapshotWindow.mWindow;
        InsetsState insetsState2 = new InsetsState();
        InputChannel inputChannel = new InputChannel();
        try {
            Trace.traceBegin(32, "TaskSnapshot#addToDisplay");
            int addToDisplay = windowSession.addToDisplay(window, layoutParams3, 8, i6, insetsState2, inputChannel, insetsState2, insetsSourceControlArr);
            Trace.traceEnd(32);
            if (addToDisplay < 0) {
                String str2 = TAG;
                Slog.w(str2, "Failed to add snapshot starting window res=" + addToDisplay);
                return null;
            }
        } catch (RemoteException unused) {
            taskSnapshotWindow.clearWindowSynced();
        }
        window.setOuter(taskSnapshotWindow);
        try {
            Trace.traceBegin(32, "TaskSnapshot#relayout");
            windowSession.relayout(window, layoutParams3, -1, -1, 0, 0, -1, clientWindowFrames, mergedConfiguration, surfaceControl, insetsState2, insetsSourceControlArr, TMP_SURFACE_SIZE);
            Trace.traceEnd(32);
        } catch (RemoteException unused2) {
            taskSnapshotWindow.clearWindowSynced();
        }
        taskSnapshotWindow.setFrames(clientWindowFrames.frame, getSystemBarInsets(clientWindowFrames.frame, insetsState));
        taskSnapshotWindow.drawSnapshot();
        return taskSnapshotWindow;
    }

    public TaskSnapshotWindow(SurfaceControl surfaceControl, TaskSnapshot taskSnapshot, CharSequence charSequence, ActivityManager.TaskDescription taskDescription, int i, int i2, int i3, Rect rect, int i4, int i5, long j, InsetsState insetsState, Runnable runnable, ShellExecutor shellExecutor) {
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        this.mSplashScreenExecutor = shellExecutor;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        this.mSession = windowSession;
        Window window = new Window();
        this.mWindow = window;
        window.setSession(windowSession);
        this.mSurfaceControl = surfaceControl;
        this.mSnapshot = taskSnapshot;
        this.mTitle = charSequence;
        int backgroundColor = taskDescription.getBackgroundColor();
        paint.setColor(backgroundColor == 0 ? -1 : backgroundColor);
        this.mTaskBounds = rect;
        this.mSystemBarBackgroundPainter = new SystemBarBackgroundPainter(i2, i3, i, taskDescription, 1.0f, insetsState);
        this.mStatusBarColor = taskDescription.getStatusBarColor();
        this.mOrientationOnCreation = i4;
        this.mActivityType = i5;
        this.mDelayRemovalTime = j;
        this.mTransaction = new SurfaceControl.Transaction();
        this.mClearWindowHandler = runnable;
    }

    /* access modifiers changed from: package-private */
    public int getBackgroundColor() {
        return this.mBackgroundPaint.getColor();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: remove */
    public void lambda$remove$0() {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mShownTime;
        long j2 = this.mDelayRemovalTime;
        if (uptimeMillis - j >= j2 || this.mActivityType == 2) {
            try {
                this.mSession.remove(this.mWindow);
            } catch (RemoteException unused) {
            }
        } else {
            this.mSplashScreenExecutor.executeDelayed(new Runnable() { // from class: com.android.wm.shell.startingsurface.TaskSnapshotWindow$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskSnapshotWindow.this.lambda$remove$0();
                }
            }, (j + j2) - uptimeMillis);
        }
    }

    public void setFrames(Rect rect, Rect rect2) {
        this.mFrame.set(rect);
        this.mSystemBarInsets.set(rect2);
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        this.mSizeMismatch = (this.mFrame.width() == hardwareBuffer.getWidth() && this.mFrame.height() == hardwareBuffer.getHeight()) ? false : true;
        this.mSystemBarBackgroundPainter.setInsets(rect2);
    }

    static Rect getSystemBarInsets(Rect rect, InsetsState insetsState) {
        return insetsState.calculateInsets(rect, WindowInsets.Type.systemBars(), false);
    }

    private void drawSnapshot() {
        if (this.mSizeMismatch) {
            drawSizeMismatchSnapshot();
        } else {
            drawSizeMatchSnapshot();
        }
        this.mShownTime = SystemClock.uptimeMillis();
        this.mHasDrawn = true;
        reportDrawn();
        this.mSnapshot = null;
    }

    private void drawSizeMatchSnapshot() {
        this.mTransaction.setBuffer(this.mSurfaceControl, GraphicBuffer.createFromHardwareBuffer(this.mSnapshot.getHardwareBuffer())).setColorSpace(this.mSurfaceControl, this.mSnapshot.getColorSpace()).apply();
    }

    private void drawSizeMismatchSnapshot() {
        Rect rect;
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        SurfaceSession surfaceSession = new SurfaceSession();
        boolean z = Math.abs((((float) hardwareBuffer.getWidth()) / ((float) hardwareBuffer.getHeight())) - (((float) this.mFrame.width()) / ((float) this.mFrame.height()))) > 0.01f;
        SurfaceControl.Builder builder = new SurfaceControl.Builder(surfaceSession);
        SurfaceControl build = builder.setName(((Object) this.mTitle) + " - task-snapshot-surface").setBLASTLayer().setFormat(hardwareBuffer.getFormat()).setParent(this.mSurfaceControl).setCallsite("TaskSnapshotWindow.drawSizeMismatchSnapshot").build();
        this.mTransaction.show(build);
        if (z) {
            Rect calculateSnapshotCrop = calculateSnapshotCrop();
            rect = calculateSnapshotFrame(calculateSnapshotCrop);
            this.mTransaction.setWindowCrop(build, calculateSnapshotCrop);
            this.mTransaction.setPosition(build, (float) rect.left, (float) rect.top);
            this.mTmpSnapshotSize.set(calculateSnapshotCrop);
            this.mTmpDstFrame.set(rect);
        } else {
            rect = null;
            this.mTmpSnapshotSize.set(0.0f, 0.0f, (float) hardwareBuffer.getWidth(), (float) hardwareBuffer.getHeight());
            this.mTmpDstFrame.set(this.mFrame);
            this.mTmpDstFrame.offsetTo(0.0f, 0.0f);
        }
        this.mSnapshotMatrix.setRectToRect(this.mTmpSnapshotSize, this.mTmpDstFrame, Matrix.ScaleToFit.FILL);
        this.mTransaction.setMatrix(build, this.mSnapshotMatrix, this.mTmpFloat9);
        GraphicBuffer createFromHardwareBuffer = GraphicBuffer.createFromHardwareBuffer(this.mSnapshot.getHardwareBuffer());
        this.mTransaction.setColorSpace(build, this.mSnapshot.getColorSpace());
        this.mTransaction.setBuffer(build, createFromHardwareBuffer);
        if (z) {
            GraphicBuffer create = GraphicBuffer.create(this.mFrame.width(), this.mFrame.height(), 1, 2336);
            Canvas lockCanvas = create.lockCanvas();
            drawBackgroundAndBars(lockCanvas, rect);
            create.unlockCanvasAndPost(lockCanvas);
            this.mTransaction.setBuffer(this.mSurfaceControl, create);
        }
        this.mTransaction.apply();
    }

    public Rect calculateSnapshotCrop() {
        Rect rect = new Rect();
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        int i = 0;
        rect.set(0, 0, hardwareBuffer.getWidth(), hardwareBuffer.getHeight());
        Rect contentInsets = this.mSnapshot.getContentInsets();
        float width = ((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x);
        float height = ((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y);
        boolean z = this.mTaskBounds.top == 0 && this.mFrame.top == 0;
        int i2 = (int) (((float) contentInsets.left) * width);
        if (!z) {
            i = (int) (((float) contentInsets.top) * height);
        }
        rect.inset(i2, i, (int) (((float) contentInsets.right) * width), (int) (((float) contentInsets.bottom) * height));
        return rect;
    }

    public Rect calculateSnapshotFrame(Rect rect) {
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        Rect rect2 = new Rect(0, 0, (int) ((((float) rect.width()) / (((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x))) + 0.5f), (int) ((((float) rect.height()) / (((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y))) + 0.5f));
        rect2.offset(this.mSystemBarInsets.left, 0);
        return rect2;
    }

    public void drawBackgroundAndBars(Canvas canvas, Rect rect) {
        int i;
        int statusBarColorViewHeight = this.mSystemBarBackgroundPainter.getStatusBarColorViewHeight();
        boolean z = true;
        boolean z2 = canvas.getWidth() > rect.right;
        if (canvas.getHeight() <= rect.bottom) {
            z = false;
        }
        if (z2) {
            float f = (float) rect.right;
            float f2 = Color.alpha(this.mStatusBarColor) == 255 ? (float) statusBarColorViewHeight : 0.0f;
            float width = (float) canvas.getWidth();
            if (z) {
                i = rect.bottom;
            } else {
                i = canvas.getHeight();
            }
            canvas.drawRect(f, f2, width, (float) i, this.mBackgroundPaint);
        }
        if (z) {
            canvas.drawRect(0.0f, (float) rect.bottom, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mBackgroundPaint);
        }
        this.mSystemBarBackgroundPainter.drawDecors(canvas, rect);
    }

    /* access modifiers changed from: private */
    public void clearWindowSynced() {
        this.mSplashScreenExecutor.executeDelayed(this.mClearWindowHandler, 0);
    }

    /* access modifiers changed from: private */
    public void reportDrawn() {
        try {
            this.mSession.finishDrawing(this.mWindow, (SurfaceControl.Transaction) null);
        } catch (RemoteException unused) {
            clearWindowSynced();
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Window extends BaseIWindow {
        private TaskSnapshotWindow mOuter;

        Window() {
        }

        public void setOuter(TaskSnapshotWindow taskSnapshotWindow) {
            this.mOuter = taskSnapshotWindow;
        }

        public void resized(ClientWindowFrames clientWindowFrames, boolean z, MergedConfiguration mergedConfiguration, boolean z2, boolean z3, int i) {
            TaskSnapshotWindow taskSnapshotWindow = this.mOuter;
            if (taskSnapshotWindow != null) {
                taskSnapshotWindow.mSplashScreenExecutor.execute(new TaskSnapshotWindow$Window$$ExternalSyntheticLambda0(this, mergedConfiguration, z));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$resized$0(MergedConfiguration mergedConfiguration, boolean z) {
            if (mergedConfiguration != null && this.mOuter.mOrientationOnCreation != mergedConfiguration.getMergedConfiguration().orientation) {
                this.mOuter.clearWindowSynced();
            } else if (z && this.mOuter.mHasDrawn) {
                this.mOuter.reportDrawn();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class SystemBarBackgroundPainter {
        private final InsetsState mInsetsState;
        private final int mNavigationBarColor;
        private final Paint mNavigationBarPaint;
        private final float mScale;
        private final int mStatusBarColor;
        private final Paint mStatusBarPaint;
        private final Rect mSystemBarInsets = new Rect();
        private final int mWindowFlags;
        private final int mWindowPrivateFlags;

        SystemBarBackgroundPainter(int i, int i2, int i3, ActivityManager.TaskDescription taskDescription, float f, InsetsState insetsState) {
            Paint paint = new Paint();
            this.mStatusBarPaint = paint;
            Paint paint2 = new Paint();
            this.mNavigationBarPaint = paint2;
            this.mWindowFlags = i;
            this.mWindowPrivateFlags = i2;
            this.mScale = f;
            ContextImpl systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
            int color = systemUiContext.getColor(17171088);
            int calculateBarColor = DecorView.calculateBarColor(i, 67108864, color, taskDescription.getStatusBarColor(), i3, 8, taskDescription.getEnsureStatusBarContrastWhenTransparent());
            this.mStatusBarColor = calculateBarColor;
            int calculateBarColor2 = DecorView.calculateBarColor(i, 134217728, color, taskDescription.getNavigationBarColor(), i3, 16, taskDescription.getEnsureNavigationBarContrastWhenTransparent() && systemUiContext.getResources().getBoolean(17891596));
            this.mNavigationBarColor = calculateBarColor2;
            paint.setColor(calculateBarColor);
            paint2.setColor(calculateBarColor2);
            this.mInsetsState = insetsState;
        }

        void setInsets(Rect rect) {
            this.mSystemBarInsets.set(rect);
        }

        int getStatusBarColorViewHeight() {
            if (DecorView.STATUS_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mStatusBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0)) {
                return (int) (((float) this.mSystemBarInsets.top) * this.mScale);
            }
            return 0;
        }

        private boolean isNavigationBarColorViewVisible() {
            return DecorView.NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mNavigationBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0);
        }

        void drawDecors(Canvas canvas, Rect rect) {
            drawStatusBarBackground(canvas, rect, getStatusBarColorViewHeight());
            drawNavigationBarBackground(canvas);
        }

        void drawStatusBarBackground(Canvas canvas, Rect rect, int i) {
            if (i > 0 && Color.alpha(this.mStatusBarColor) != 0) {
                if (rect == null || canvas.getWidth() > rect.right) {
                    canvas.drawRect((float) (rect != null ? rect.right : 0), 0.0f, (float) (canvas.getWidth() - ((int) (((float) this.mSystemBarInsets.right) * this.mScale))), (float) i, this.mStatusBarPaint);
                }
            }
        }

        @VisibleForTesting
        void drawNavigationBarBackground(Canvas canvas) {
            Rect rect = new Rect();
            DecorView.getNavigationBarRect(canvas.getWidth(), canvas.getHeight(), this.mSystemBarInsets, rect, this.mScale);
            if (isNavigationBarColorViewVisible() && Color.alpha(this.mNavigationBarColor) != 0 && !rect.isEmpty()) {
                canvas.drawRect(rect, this.mNavigationBarPaint);
            }
        }
    }
}
