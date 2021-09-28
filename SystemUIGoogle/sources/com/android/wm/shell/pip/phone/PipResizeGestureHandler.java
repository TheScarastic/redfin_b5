package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.provider.DeviceConfig;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.internal.policy.TaskResizingAlgorithm;
import com.android.wm.shell.R;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Function;
/* loaded from: classes2.dex */
public class PipResizeGestureHandler {
    private boolean mAllowGesture;
    private final Context mContext;
    private int mCtrlType;
    private int mDelta;
    private final int mDisplayId;
    private boolean mEnableDragCornerResize;
    private boolean mEnablePinchResize;
    private InputEventReceiver mInputEventReceiver;
    private InputMonitor mInputMonitor;
    private boolean mIsAttached;
    private boolean mIsEnabled;
    private boolean mIsSysUiStateValid;
    private final ShellExecutor mMainExecutor;
    private final PipMotionHelper mMotionHelper;
    private final Function<Rect, Rect> mMovementBoundsSupplier;
    private int mOhmOffset;
    private final PhonePipMenuController mPhonePipMenuController;
    private final PipBoundsAlgorithm mPipBoundsAlgorithm;
    private final PipBoundsState mPipBoundsState;
    private final PipDismissTargetHandler mPipDismissTargetHandler;
    private final PipTaskOrganizer mPipTaskOrganizer;
    private final PipUiEventLogger mPipUiEventLogger;
    private boolean mThresholdCrossed;
    private float mTouchSlop;
    private final Runnable mUpdateMovementBoundsRunnable;
    private final Region mTmpRegion = new Region();
    private final PointF mDownPoint = new PointF();
    private final PointF mDownSecondPoint = new PointF();
    private final PointF mLastPoint = new PointF();
    private final PointF mLastSecondPoint = new PointF();
    private final Point mMaxSize = new Point();
    private final Point mMinSize = new Point();
    private final Rect mLastResizeBounds = new Rect();
    private final Rect mUserResizeBounds = new Rect();
    private final Rect mDownBounds = new Rect();
    private final Rect mDragCornerSize = new Rect();
    private final Rect mTmpTopLeftCorner = new Rect();
    private final Rect mTmpTopRightCorner = new Rect();
    private final Rect mTmpBottomLeftCorner = new Rect();
    private final Rect mTmpBottomRightCorner = new Rect();
    private final Rect mDisplayBounds = new Rect();
    private boolean mOngoingPinchToResize = false;
    private float mAngle = 0.0f;
    int mFirstIndex = -1;
    int mSecondIndex = -1;
    private final PipPinchResizingAlgorithm mPinchResizingAlgorithm = new PipPinchResizingAlgorithm();

    public PipResizeGestureHandler(Context context, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipDismissTargetHandler pipDismissTargetHandler, Function<Rect, Rect> function, Runnable runnable, PipUiEventLogger pipUiEventLogger, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = shellExecutor;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        this.mMovementBoundsSupplier = function;
        this.mUpdateMovementBoundsRunnable = runnable;
        this.mPhonePipMenuController = phonePipMenuController;
        this.mPipUiEventLogger = pipUiEventLogger;
    }

    public void init() {
        this.mContext.getDisplay().getRealSize(this.mMaxSize);
        reloadResources();
        this.mEnablePinchResize = DeviceConfig.getBoolean("systemui", "pip_pinch_resize", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipResizeGestureHandler.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("pip_pinch_resize")) {
                    PipResizeGestureHandler.this.mEnablePinchResize = properties.getBoolean("pip_pinch_resize", true);
                }
            }
        });
    }

    public void onConfigurationChanged() {
        reloadResources();
    }

    public void onSystemUiStateChanged(boolean z) {
        this.mIsSysUiStateValid = z;
    }

    private void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mDelta = resources.getDimensionPixelSize(R.dimen.pip_resize_edge_size);
        this.mEnableDragCornerResize = resources.getBoolean(R.bool.config_pipEnableDragCornerResize);
        this.mTouchSlop = (float) ViewConfiguration.get(this.mContext).getScaledTouchSlop();
    }

    private void resetDragCorners() {
        Rect rect = this.mDragCornerSize;
        int i = this.mDelta;
        rect.set(0, 0, i, i);
        this.mTmpTopLeftCorner.set(this.mDragCornerSize);
        this.mTmpTopRightCorner.set(this.mDragCornerSize);
        this.mTmpBottomLeftCorner.set(this.mDragCornerSize);
        this.mTmpBottomRightCorner.set(this.mDragCornerSize);
    }

    private void disposeInputChannel() {
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
    }

    public void onActivityPinned() {
        this.mIsAttached = true;
        updateIsEnabled();
    }

    public void onActivityUnpinned() {
        this.mIsAttached = false;
        this.mUserResizeBounds.setEmpty();
        updateIsEnabled();
    }

    private void updateIsEnabled() {
        boolean z = this.mIsAttached;
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            disposeInputChannel();
            if (this.mIsEnabled) {
                this.mInputMonitor = InputManager.getInstance().monitorGestureInput("pip-resize", this.mDisplayId);
                try {
                    this.mMainExecutor.executeBlocking(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipResizeGestureHandler$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            PipResizeGestureHandler.$r8$lambda$y3nbkw5BASZg3VO5nA6uBp5FOBQ(PipResizeGestureHandler.this);
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to create input event receiver", e);
                }
            }
        }
    }

    public /* synthetic */ void lambda$updateIsEnabled$0() {
        this.mInputEventReceiver = new PipResizeInputEventReceiver(this.mInputMonitor.getInputChannel(), Looper.myLooper());
    }

    void onInputEvent(InputEvent inputEvent) {
        if ((this.mEnableDragCornerResize || this.mEnablePinchResize) && !this.mPipBoundsState.isStashed() && (inputEvent instanceof MotionEvent)) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            int actionMasked = motionEvent.getActionMasked();
            Rect bounds = this.mPipBoundsState.getBounds();
            if ((actionMasked == 1 || actionMasked == 3) && !bounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY()) && this.mPhonePipMenuController.isMenuVisible()) {
                this.mPhonePipMenuController.hideMenu();
            }
            if (this.mEnablePinchResize && this.mOngoingPinchToResize) {
                onPinchResize(motionEvent);
            } else if (this.mEnableDragCornerResize) {
                onDragCornerResize(motionEvent);
            }
        }
    }

    public boolean hasOngoingGesture() {
        return this.mCtrlType != 0 || this.mOngoingPinchToResize;
    }

    public boolean isWithinDragResizeRegion(int i, int i2) {
        Rect bounds;
        if (!this.mEnableDragCornerResize || (bounds = this.mPipBoundsState.getBounds()) == null) {
            return false;
        }
        resetDragCorners();
        Rect rect = this.mTmpTopLeftCorner;
        int i3 = bounds.left;
        int i4 = this.mDelta;
        rect.offset(i3 - (i4 / 2), bounds.top - (i4 / 2));
        Rect rect2 = this.mTmpTopRightCorner;
        int i5 = bounds.right;
        int i6 = this.mDelta;
        rect2.offset(i5 - (i6 / 2), bounds.top - (i6 / 2));
        Rect rect3 = this.mTmpBottomLeftCorner;
        int i7 = bounds.left;
        int i8 = this.mDelta;
        rect3.offset(i7 - (i8 / 2), bounds.bottom - (i8 / 2));
        Rect rect4 = this.mTmpBottomRightCorner;
        int i9 = bounds.right;
        int i10 = this.mDelta;
        rect4.offset(i9 - (i10 / 2), bounds.bottom - (i10 / 2));
        this.mTmpRegion.setEmpty();
        this.mTmpRegion.op(this.mTmpTopLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpTopRightCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomRightCorner, Region.Op.UNION);
        return this.mTmpRegion.contains(i, i2);
    }

    public boolean isUsingPinchToZoom() {
        return this.mEnablePinchResize;
    }

    public boolean isResizing() {
        return this.mAllowGesture;
    }

    public boolean willStartResizeGesture(MotionEvent motionEvent) {
        if (!isInValidSysUiState()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            return isWithinDragResizeRegion((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }
        if (actionMasked != 5 || !this.mEnablePinchResize || motionEvent.getPointerCount() != 2) {
            return false;
        }
        onPinchResize(motionEvent);
        boolean z = this.mAllowGesture;
        this.mOngoingPinchToResize = z;
        return z;
    }

    private void setCtrlType(int i, int i2) {
        Rect bounds = this.mPipBoundsState.getBounds();
        Rect apply = this.mMovementBoundsSupplier.apply(bounds);
        this.mDisplayBounds.set(apply.left, apply.top, apply.right + bounds.width(), apply.bottom + bounds.height());
        if (this.mTmpTopLeftCorner.contains(i, i2)) {
            int i3 = bounds.top;
            Rect rect = this.mDisplayBounds;
            if (!(i3 == rect.top || bounds.left == rect.left)) {
                int i4 = this.mCtrlType | 1;
                this.mCtrlType = i4;
                this.mCtrlType = i4 | 4;
            }
        }
        if (this.mTmpTopRightCorner.contains(i, i2)) {
            int i5 = bounds.top;
            Rect rect2 = this.mDisplayBounds;
            if (!(i5 == rect2.top || bounds.right == rect2.right)) {
                int i6 = this.mCtrlType | 2;
                this.mCtrlType = i6;
                this.mCtrlType = i6 | 4;
            }
        }
        if (this.mTmpBottomRightCorner.contains(i, i2)) {
            int i7 = bounds.bottom;
            Rect rect3 = this.mDisplayBounds;
            if (!(i7 == rect3.bottom || bounds.right == rect3.right)) {
                int i8 = this.mCtrlType | 2;
                this.mCtrlType = i8;
                this.mCtrlType = i8 | 8;
            }
        }
        if (this.mTmpBottomLeftCorner.contains(i, i2)) {
            int i9 = bounds.bottom;
            Rect rect4 = this.mDisplayBounds;
            if (i9 != rect4.bottom && bounds.left != rect4.left) {
                int i10 = this.mCtrlType | 1;
                this.mCtrlType = i10;
                this.mCtrlType = i10 | 8;
            }
        }
    }

    private boolean isInValidSysUiState() {
        return this.mIsSysUiStateValid;
    }

    void onPinchResize(MotionEvent motionEvent) {
        int i;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.mFirstIndex = -1;
            this.mSecondIndex = -1;
            this.mAllowGesture = false;
            finishResize();
        }
        if (motionEvent.getPointerCount() == 2) {
            Rect bounds = this.mPipBoundsState.getBounds();
            if (actionMasked == 5 && this.mFirstIndex == -1 && this.mSecondIndex == -1 && bounds.contains((int) motionEvent.getRawX(0), (int) motionEvent.getRawY(0)) && bounds.contains((int) motionEvent.getRawX(1), (int) motionEvent.getRawY(1))) {
                this.mAllowGesture = true;
                this.mFirstIndex = 0;
                this.mSecondIndex = 1;
                this.mDownPoint.set(motionEvent.getRawX(0), motionEvent.getRawY(this.mFirstIndex));
                this.mDownSecondPoint.set(motionEvent.getRawX(this.mSecondIndex), motionEvent.getRawY(this.mSecondIndex));
                this.mDownBounds.set(bounds);
                this.mLastPoint.set(this.mDownPoint);
                PointF pointF = this.mLastSecondPoint;
                pointF.set(pointF);
                this.mLastResizeBounds.set(this.mDownBounds);
            }
            if (actionMasked == 2 && (i = this.mFirstIndex) != -1 && this.mSecondIndex != -1) {
                float rawX = motionEvent.getRawX(i);
                float rawY = motionEvent.getRawY(this.mFirstIndex);
                float rawX2 = motionEvent.getRawX(this.mSecondIndex);
                float rawY2 = motionEvent.getRawY(this.mSecondIndex);
                this.mLastPoint.set(rawX, rawY);
                this.mLastSecondPoint.set(rawX2, rawY2);
                if (!this.mThresholdCrossed && (distanceBetween(this.mDownSecondPoint, this.mLastSecondPoint) > this.mTouchSlop || distanceBetween(this.mDownPoint, this.mLastPoint) > this.mTouchSlop)) {
                    pilferPointers();
                    this.mThresholdCrossed = true;
                    this.mDownPoint.set(this.mLastPoint);
                    this.mDownSecondPoint.set(this.mLastSecondPoint);
                    if (this.mPhonePipMenuController.isMenuVisible()) {
                        this.mPhonePipMenuController.hideMenu();
                    }
                }
                if (this.mThresholdCrossed) {
                    float calculateBoundsAndAngle = this.mPinchResizingAlgorithm.calculateBoundsAndAngle(this.mDownPoint, this.mDownSecondPoint, this.mLastPoint, this.mLastSecondPoint, this.mMinSize, this.mMaxSize, this.mDownBounds, this.mLastResizeBounds);
                    this.mAngle = calculateBoundsAndAngle;
                    this.mPipTaskOrganizer.scheduleUserResizePip(this.mDownBounds, this.mLastResizeBounds, calculateBoundsAndAngle, null);
                    this.mPipBoundsState.setHasUserResizedPip(true);
                }
            }
        }
    }

    private void onDragCornerResize(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY() - ((float) this.mOhmOffset);
        boolean z = false;
        if (actionMasked == 0) {
            this.mLastResizeBounds.setEmpty();
            if (isInValidSysUiState() && isWithinDragResizeRegion((int) x, (int) y)) {
                z = true;
            }
            this.mAllowGesture = z;
            if (z) {
                setCtrlType((int) x, (int) y);
                this.mDownPoint.set(x, y);
                this.mDownBounds.set(this.mPipBoundsState.getBounds());
            }
        } else if (this.mAllowGesture) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (!this.mThresholdCrossed) {
                        PointF pointF = this.mDownPoint;
                        if (Math.hypot((double) (x - pointF.x), (double) (y - pointF.y)) > ((double) this.mTouchSlop)) {
                            this.mThresholdCrossed = true;
                            this.mDownPoint.set(x, y);
                            this.mInputMonitor.pilferPointers();
                        }
                    }
                    if (this.mThresholdCrossed) {
                        if (this.mPhonePipMenuController.isMenuVisible()) {
                            this.mPhonePipMenuController.hideMenu(0, false);
                        }
                        Rect bounds = this.mPipBoundsState.getBounds();
                        Rect rect = this.mLastResizeBounds;
                        PointF pointF2 = this.mDownPoint;
                        float f = pointF2.x;
                        float f2 = pointF2.y;
                        int i = this.mCtrlType;
                        Point point = this.mMinSize;
                        rect.set(TaskResizingAlgorithm.resizeDrag(x, y, f, f2, bounds, i, point.x, point.y, this.mMaxSize, true, this.mDownBounds.width() > this.mDownBounds.height()));
                        this.mPipBoundsAlgorithm.transformBoundsToAspectRatio(this.mLastResizeBounds, this.mPipBoundsState.getAspectRatio(), false, true);
                        this.mPipTaskOrganizer.scheduleUserResizePip(this.mDownBounds, this.mLastResizeBounds, null);
                        this.mPipBoundsState.setHasUserResizedPip(true);
                        return;
                    }
                    return;
                } else if (actionMasked != 3) {
                    if (actionMasked == 5) {
                        this.mAllowGesture = false;
                        return;
                    }
                    return;
                }
            }
            finishResize();
        }
    }

    private void finishResize() {
        if (!this.mLastResizeBounds.isEmpty()) {
            PipResizeGestureHandler$$ExternalSyntheticLambda1 pipResizeGestureHandler$$ExternalSyntheticLambda1 = new Consumer() { // from class: com.android.wm.shell.pip.phone.PipResizeGestureHandler$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PipResizeGestureHandler.$r8$lambda$WtDbF9Y9YztVsta_tic2Eewwo9Y(PipResizeGestureHandler.this, (Rect) obj);
                }
            };
            if (this.mOngoingPinchToResize) {
                Rect rect = new Rect(this.mLastResizeBounds);
                if (((float) this.mLastResizeBounds.width()) >= ((float) this.mMaxSize.x) * 0.9f || ((float) this.mLastResizeBounds.height()) >= ((float) this.mMaxSize.y) * 0.9f) {
                    Rect rect2 = this.mLastResizeBounds;
                    Point point = this.mMaxSize;
                    resizeRectAboutCenter(rect2, point.x, point.y);
                }
                Rect rect3 = this.mLastResizeBounds;
                int i = rect3.left;
                Rect movementBounds = this.mPipBoundsAlgorithm.getMovementBounds(rect3);
                int i2 = Math.abs(i - movementBounds.left) < Math.abs(movementBounds.right - i) ? movementBounds.left : movementBounds.right;
                Rect rect4 = this.mLastResizeBounds;
                rect4.offsetTo(i2, rect4.top);
                this.mPipBoundsAlgorithm.applySnapFraction(this.mLastResizeBounds, this.mPipBoundsAlgorithm.getSnapFraction(this.mLastResizeBounds, movementBounds));
                this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, this.mLastResizeBounds, 250, this.mAngle, pipResizeGestureHandler$$ExternalSyntheticLambda1);
            } else {
                this.mPipTaskOrganizer.scheduleFinishResizePip(this.mLastResizeBounds, 7, pipResizeGestureHandler$$ExternalSyntheticLambda1);
            }
            this.mPipDismissTargetHandler.setMagneticFieldRadiusPercent((((float) this.mLastResizeBounds.width()) / ((float) this.mMinSize.x)) / 2.0f);
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_RESIZE);
            return;
        }
        resetState();
    }

    public /* synthetic */ void lambda$finishResize$1(Rect rect) {
        this.mUserResizeBounds.set(this.mLastResizeBounds);
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundsRunnable.run();
        resetState();
    }

    private void resetState() {
        this.mCtrlType = 0;
        this.mAngle = 0.0f;
        this.mOngoingPinchToResize = false;
        this.mAllowGesture = false;
        this.mThresholdCrossed = false;
    }

    public void setUserResizeBounds(Rect rect) {
        this.mUserResizeBounds.set(rect);
    }

    public void invalidateUserResizeBounds() {
        this.mUserResizeBounds.setEmpty();
    }

    public Rect getUserResizeBounds() {
        return this.mUserResizeBounds;
    }

    Rect getLastResizeBounds() {
        return this.mLastResizeBounds;
    }

    void pilferPointers() {
        this.mInputMonitor.pilferPointers();
    }

    public void updateMaxSize(int i, int i2) {
        this.mMaxSize.set(i, i2);
    }

    public void updateMinSize(int i, int i2) {
        this.mMinSize.set(i, i2);
    }

    public void setOhmOffset(int i) {
        this.mOhmOffset = i;
    }

    private float distanceBetween(PointF pointF, PointF pointF2) {
        return (float) Math.hypot((double) (pointF2.x - pointF.x), (double) (pointF2.y - pointF.y));
    }

    private void resizeRectAboutCenter(Rect rect, int i, int i2) {
        int centerX = rect.centerX() - (i / 2);
        int centerY = rect.centerY() - (i2 / 2);
        rect.set(centerX, centerY, i + centerX, i2 + centerY);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PipResizeGestureHandler");
        printWriter.println(str2 + "mAllowGesture=" + this.mAllowGesture);
        printWriter.println(str2 + "mIsAttached=" + this.mIsAttached);
        printWriter.println(str2 + "mIsEnabled=" + this.mIsEnabled);
        printWriter.println(str2 + "mEnablePinchResize=" + this.mEnablePinchResize);
        printWriter.println(str2 + "mThresholdCrossed=" + this.mThresholdCrossed);
        printWriter.println(str2 + "mOhmOffset=" + this.mOhmOffset);
    }

    /* loaded from: classes2.dex */
    public class PipResizeInputEventReceiver extends BatchedInputEventReceiver {
        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        PipResizeInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper, Choreographer.getSfInstance());
            PipResizeGestureHandler.this = r1;
        }

        public void onInputEvent(InputEvent inputEvent) {
            PipResizeGestureHandler.this.onInputEvent(inputEvent);
            finishInputEvent(inputEvent, true);
        }
    }
}
