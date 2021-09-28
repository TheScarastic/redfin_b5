package com.android.wm.shell.bubbles;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.CornerPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.R;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.common.AlphaOptimizedButton;
import com.android.wm.shell.common.TriangleShape;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class BubbleExpandedView extends LinearLayout {
    private int mBackgroundColorFloating;
    private Bubble mBubble;
    private BubbleController mController;
    private float mCornerRadius;
    private ShapeDrawable mCurrentPointer;
    private final FrameLayout mExpandedViewContainer;
    private int[] mExpandedViewContainerLocation;
    private boolean mImeVisible;
    private boolean mIsAlphaAnimating;
    private boolean mIsContentVisible;
    private boolean mIsOverflow;
    private ShapeDrawable mLeftPointer;
    private AlphaOptimizedButton mManageButton;
    private int mManageButtonHeight;
    private int mMinHeight;
    private boolean mNeedsNewHeight;
    private int mOverflowHeight;
    private BubbleOverflowContainerView mOverflowView;
    private PendingIntent mPendingIntent;
    private CornerPathEffect mPointerEffect;
    private int mPointerHeight;
    private int mPointerMargin;
    private float mPointerOverlap;
    private float mPointerRadius;
    private View mPointerView;
    private int mPointerWidth;
    private BubblePositioner mPositioner;
    private ShapeDrawable mRightPointer;
    private BubbleStackView mStackView;
    private int mTaskId;
    private TaskView mTaskView;
    private final TaskView.Listener mTaskViewListener;
    private ShapeDrawable mTopPointer;

    public BubbleExpandedView(Context context) {
        this(context, null);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BubbleExpandedView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTaskId = -1;
        this.mIsContentVisible = false;
        this.mIsAlphaAnimating = false;
        this.mCornerRadius = 0.0f;
        this.mExpandedViewContainer = new FrameLayout(getContext());
        this.mTaskViewListener = new TaskView.Listener() { // from class: com.android.wm.shell.bubbles.BubbleExpandedView.1
            private boolean mInitialized = false;
            private boolean mDestroyed = false;

            @Override // com.android.wm.shell.TaskView.Listener
            public void onInitialized() {
                if (!this.mDestroyed && !this.mInitialized) {
                    ActivityOptions makeCustomAnimation = ActivityOptions.makeCustomAnimation(BubbleExpandedView.this.getContext(), 0, 0);
                    Rect rect = new Rect();
                    BubbleExpandedView.this.mTaskView.getBoundsOnScreen(rect);
                    BubbleExpandedView.this.post(new BubbleExpandedView$1$$ExternalSyntheticLambda1(this, makeCustomAnimation, rect));
                    this.mInitialized = true;
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onInitialized$0(ActivityOptions activityOptions, Rect rect) {
                try {
                    activityOptions.setTaskAlwaysOnTop(true);
                    activityOptions.setLaunchedFromBubble(true);
                    if (BubbleExpandedView.this.mIsOverflow || !BubbleExpandedView.this.mBubble.hasMetadataShortcutId()) {
                        Intent intent = new Intent();
                        intent.addFlags(524288);
                        intent.addFlags(134217728);
                        if (BubbleExpandedView.this.mBubble != null) {
                            BubbleExpandedView.this.mBubble.setIntentActive();
                        }
                        BubbleExpandedView.this.mTaskView.startActivity(BubbleExpandedView.this.mPendingIntent, intent, activityOptions, rect);
                        return;
                    }
                    activityOptions.setApplyActivityFlagsForBubbles(true);
                    BubbleExpandedView.this.mTaskView.startShortcutActivity(BubbleExpandedView.this.mBubble.getShortcutInfo(), activityOptions, rect);
                } catch (RuntimeException e) {
                    Log.w("Bubbles", "Exception while displaying bubble: " + BubbleExpandedView.this.getBubbleKey() + ", " + e.getMessage() + "; removing bubble");
                    BubbleExpandedView.this.mController.removeBubble(BubbleExpandedView.this.getBubbleKey(), 10);
                }
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onReleased() {
                this.mDestroyed = true;
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskCreated(int i3, ComponentName componentName) {
                BubbleExpandedView.this.mTaskId = i3;
                BubbleExpandedView.this.setContentVisibility(true);
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskVisibilityChanged(int i3, boolean z) {
                BubbleExpandedView.this.setContentVisibility(z);
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskRemovalStarted(int i3) {
                if (BubbleExpandedView.this.mBubble != null) {
                    BubbleExpandedView.this.post(new BubbleExpandedView$1$$ExternalSyntheticLambda0(this));
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onTaskRemovalStarted$1() {
                BubbleExpandedView.this.mController.removeBubble(BubbleExpandedView.this.mBubble.getKey(), 3);
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onBackPressedOnTaskRoot(int i3) {
                if (BubbleExpandedView.this.mTaskId == i3 && BubbleExpandedView.this.mStackView.isExpanded()) {
                    BubbleExpandedView.this.mController.collapseStack();
                }
            }
        };
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mManageButton = (AlphaOptimizedButton) LayoutInflater.from(getContext()).inflate(R.layout.bubble_manage_button, (ViewGroup) this, false);
        updateDimensions();
        View findViewById = findViewById(R.id.pointer_view);
        this.mPointerView = findViewById;
        this.mCurrentPointer = this.mTopPointer;
        findViewById.setVisibility(4);
        setContentVisibility(false);
        this.mExpandedViewContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.wm.shell.bubbles.BubbleExpandedView.2
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), BubbleExpandedView.this.mCornerRadius);
            }
        });
        this.mExpandedViewContainer.setClipToOutline(true);
        this.mExpandedViewContainer.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        addView(this.mExpandedViewContainer);
        bringChildToFront(this.mManageButton);
        applyThemeAttrs();
        setClipToPadding(false);
        setOnTouchListener(new View.OnTouchListener() { // from class: com.android.wm.shell.bubbles.BubbleExpandedView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return BubbleExpandedView.this.lambda$onFinishInflate$0(view, motionEvent);
            }
        });
        setLayoutDirection(3);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view, MotionEvent motionEvent) {
        if (this.mTaskView == null) {
            return false;
        }
        Rect rect = new Rect();
        this.mTaskView.getBoundsOnScreen(rect);
        if (motionEvent.getRawY() < ((float) rect.top) || motionEvent.getRawY() > ((float) rect.bottom) || (motionEvent.getRawX() >= ((float) rect.left) && motionEvent.getRawX() <= ((float) rect.right))) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void initialize(BubbleController bubbleController, BubbleStackView bubbleStackView, boolean z) {
        this.mController = bubbleController;
        this.mStackView = bubbleStackView;
        this.mIsOverflow = z;
        this.mPositioner = bubbleController.getPositioner();
        if (this.mIsOverflow) {
            BubbleOverflowContainerView bubbleOverflowContainerView = (BubbleOverflowContainerView) LayoutInflater.from(getContext()).inflate(R.layout.bubble_overflow_container, (ViewGroup) null);
            this.mOverflowView = bubbleOverflowContainerView;
            bubbleOverflowContainerView.setBubbleController(this.mController);
            this.mExpandedViewContainer.addView(this.mOverflowView, new FrameLayout.LayoutParams(-1, -1));
            this.mExpandedViewContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            bringChildToFront(this.mOverflowView);
            this.mManageButton.setVisibility(8);
            return;
        }
        TaskView taskView = new TaskView(((LinearLayout) this).mContext, this.mController.getTaskOrganizer());
        this.mTaskView = taskView;
        taskView.setListener(this.mController.getMainExecutor(), this.mTaskViewListener);
        this.mExpandedViewContainer.addView(this.mTaskView);
        bringChildToFront(this.mTaskView);
    }

    /* access modifiers changed from: package-private */
    public void updateDimensions() {
        Resources resources = getResources();
        this.mMinHeight = resources.getDimensionPixelSize(R.dimen.bubble_expanded_default_height);
        this.mOverflowHeight = resources.getDimensionPixelSize(R.dimen.bubble_overflow_height);
        updateFontSize();
        this.mPointerMargin = resources.getDimensionPixelSize(R.dimen.bubble_pointer_margin);
        this.mPointerWidth = resources.getDimensionPixelSize(R.dimen.bubble_pointer_width);
        this.mPointerHeight = resources.getDimensionPixelSize(R.dimen.bubble_pointer_height);
        this.mPointerRadius = (float) getResources().getDimensionPixelSize(R.dimen.bubble_pointer_radius);
        this.mPointerEffect = new CornerPathEffect(this.mPointerRadius);
        this.mPointerOverlap = (float) getResources().getDimensionPixelSize(R.dimen.bubble_pointer_overlap);
        this.mTopPointer = new ShapeDrawable(TriangleShape.create((float) this.mPointerWidth, (float) this.mPointerHeight, true));
        this.mLeftPointer = new ShapeDrawable(TriangleShape.createHorizontal((float) this.mPointerWidth, (float) this.mPointerHeight, true));
        this.mRightPointer = new ShapeDrawable(TriangleShape.createHorizontal((float) this.mPointerWidth, (float) this.mPointerHeight, false));
        if (this.mPointerView != null) {
            updatePointerView();
        }
        this.mManageButtonHeight = resources.getDimensionPixelSize(R.dimen.bubble_manage_button_height);
        AlphaOptimizedButton alphaOptimizedButton = this.mManageButton;
        if (alphaOptimizedButton != null) {
            int visibility = alphaOptimizedButton.getVisibility();
            removeView(this.mManageButton);
            AlphaOptimizedButton alphaOptimizedButton2 = (AlphaOptimizedButton) LayoutInflater.from(getContext()).inflate(R.layout.bubble_manage_button, (ViewGroup) this, false);
            this.mManageButton = alphaOptimizedButton2;
            addView(alphaOptimizedButton2);
            this.mManageButton.setVisibility(visibility);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateFontSize() {
        float dimensionPixelSize = (float) ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(17105541);
        AlphaOptimizedButton alphaOptimizedButton = this.mManageButton;
        if (alphaOptimizedButton != null) {
            alphaOptimizedButton.setTextSize(0, dimensionPixelSize);
        }
        BubbleOverflowContainerView bubbleOverflowContainerView = this.mOverflowView;
        if (bubbleOverflowContainerView != null) {
            bubbleOverflowContainerView.updateFontSize();
        }
    }

    /* access modifiers changed from: package-private */
    public void applyThemeAttrs() {
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16844145, 16844002});
        this.mCornerRadius = (float) obtainStyledAttributes.getDimensionPixelSize(0, 0);
        int color = obtainStyledAttributes.getColor(1, -1);
        this.mBackgroundColorFloating = color;
        this.mExpandedViewContainer.setBackgroundColor(color);
        obtainStyledAttributes.recycle();
        if (this.mTaskView != null && ScreenDecorationsUtils.supportsRoundedCornersOnWindows(((LinearLayout) this).mContext.getResources())) {
            this.mTaskView.setCornerRadius(this.mCornerRadius);
        }
        updatePointerView();
    }

    private void updatePointerView() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mPointerView.getLayoutParams();
        ShapeDrawable shapeDrawable = this.mCurrentPointer;
        if (shapeDrawable == this.mLeftPointer || shapeDrawable == this.mRightPointer) {
            layoutParams.width = this.mPointerHeight;
            layoutParams.height = this.mPointerWidth;
        } else {
            layoutParams.width = this.mPointerWidth;
            layoutParams.height = this.mPointerHeight;
        }
        shapeDrawable.setTint(this.mBackgroundColorFloating);
        Paint paint = this.mCurrentPointer.getPaint();
        paint.setColor(this.mBackgroundColorFloating);
        paint.setPathEffect(this.mPointerEffect);
        this.mPointerView.setLayoutParams(layoutParams);
        this.mPointerView.setBackground(this.mCurrentPointer);
    }

    /* access modifiers changed from: private */
    public String getBubbleKey() {
        Bubble bubble = this.mBubble;
        return bubble != null ? bubble.getKey() : "null";
    }

    /* access modifiers changed from: package-private */
    public void setSurfaceZOrderedOnTop(boolean z) {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.setZOrderedOnTop(z, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void setImeVisible(boolean z) {
        this.mImeVisible = z;
        if (!z && this.mNeedsNewHeight) {
            updateHeight();
        }
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl.ScreenshotHardwareBuffer snapshotActivitySurface() {
        if (this.mIsOverflow) {
            Picture picture = new Picture();
            BubbleOverflowContainerView bubbleOverflowContainerView = this.mOverflowView;
            bubbleOverflowContainerView.draw(picture.beginRecording(bubbleOverflowContainerView.getWidth(), this.mOverflowView.getHeight()));
            picture.endRecording();
            Bitmap createBitmap = Bitmap.createBitmap(picture);
            return new SurfaceControl.ScreenshotHardwareBuffer(createBitmap.getHardwareBuffer(), createBitmap.getColorSpace(), false);
        }
        TaskView taskView = this.mTaskView;
        if (taskView == null || taskView.getSurfaceControl() == null) {
            return null;
        }
        return SurfaceControl.captureLayers(this.mTaskView.getSurfaceControl(), new Rect(0, 0, this.mTaskView.getWidth(), this.mTaskView.getHeight()), 1.0f);
    }

    /* access modifiers changed from: package-private */
    public int[] getTaskViewLocationOnScreen() {
        if (this.mIsOverflow) {
            return this.mOverflowView.getLocationOnScreen();
        }
        TaskView taskView = this.mTaskView;
        return taskView != null ? taskView.getLocationOnScreen() : new int[]{0, 0};
    }

    /* access modifiers changed from: package-private */
    public void setManageClickListener(View.OnClickListener onClickListener) {
        this.mManageButton.setOnClickListener(onClickListener);
    }

    /* access modifiers changed from: package-private */
    public void updateObscuredTouchableRegion() {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.onLocationChanged();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mImeVisible = false;
        this.mNeedsNewHeight = false;
    }

    /* access modifiers changed from: package-private */
    public void setAlphaAnimating(boolean z) {
        this.mIsAlphaAnimating = z;
        if (!z) {
            setContentVisibility(this.mIsContentVisible);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTaskViewAlpha(float f) {
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.setAlpha(f);
        }
        AlphaOptimizedButton alphaOptimizedButton = this.mManageButton;
        if (alphaOptimizedButton != null && alphaOptimizedButton.getVisibility() == 0) {
            this.mManageButton.setAlpha(f);
        }
    }

    /* access modifiers changed from: package-private */
    public void setContentVisibility(boolean z) {
        this.mIsContentVisible = z;
        TaskView taskView = this.mTaskView;
        if (taskView != null && !this.mIsAlphaAnimating) {
            taskView.setAlpha(z ? 1.0f : 0.0f);
        }
    }

    /* access modifiers changed from: package-private */
    public TaskView getTaskView() {
        return this.mTaskView;
    }

    /* access modifiers changed from: package-private */
    public int getTaskId() {
        return this.mTaskId;
    }

    /* access modifiers changed from: package-private */
    public void update(Bubble bubble) {
        if (this.mStackView == null) {
            Log.w("Bubbles", "Stack is null for bubble: " + bubble);
            return;
        }
        boolean z = this.mBubble == null || didBackingContentChange(bubble);
        if (z || (bubble != null && bubble.getKey().equals(this.mBubble.getKey()))) {
            this.mBubble = bubble;
            this.mManageButton.setContentDescription(getResources().getString(R.string.bubbles_settings_button_description, bubble.getAppName()));
            this.mManageButton.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.bubbles.BubbleExpandedView.3
                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    BubbleExpandedView.this.mStackView.setupLocalMenu(accessibilityNodeInfo);
                }
            });
            if (z) {
                PendingIntent bubbleIntent = this.mBubble.getBubbleIntent();
                this.mPendingIntent = bubbleIntent;
                if ((bubbleIntent != null || this.mBubble.hasMetadataShortcutId()) && this.mTaskView != null) {
                    setContentVisibility(false);
                    this.mTaskView.setVisibility(0);
                }
            }
            applyThemeAttrs();
            return;
        }
        Log.w("Bubbles", "Trying to update entry with different key, new bubble: " + bubble.getKey() + " old bubble: " + bubble.getKey());
    }

    private boolean didBackingContentChange(Bubble bubble) {
        return (this.mBubble != null && this.mPendingIntent != null) != (bubble.getBubbleIntent() != null);
    }

    void updateHeight() {
        float f;
        FrameLayout.LayoutParams layoutParams;
        if (this.mExpandedViewContainerLocation != null) {
            Bubble bubble = this.mBubble;
            if ((bubble != null && this.mTaskView != null) || this.mIsOverflow) {
                if (this.mIsOverflow) {
                    f = (float) (this.mPositioner.isLargeScreen() ? getMaxExpandedHeight() : this.mOverflowHeight);
                } else {
                    f = bubble.getDesiredHeight(((LinearLayout) this).mContext);
                }
                float max = Math.max(Math.min(Math.max(f, (float) this.mMinHeight), (float) getMaxExpandedHeight()), (float) this.mMinHeight);
                if (this.mIsOverflow) {
                    layoutParams = (FrameLayout.LayoutParams) this.mOverflowView.getLayoutParams();
                } else {
                    layoutParams = (FrameLayout.LayoutParams) this.mTaskView.getLayoutParams();
                }
                this.mNeedsNewHeight = ((float) layoutParams.height) != max;
                if (!this.mImeVisible) {
                    layoutParams.height = (int) max;
                    if (this.mIsOverflow) {
                        this.mOverflowView.setLayoutParams(layoutParams);
                    } else {
                        this.mTaskView.setLayoutParams(layoutParams);
                    }
                    this.mNeedsNewHeight = false;
                }
            }
        }
    }

    private int getMaxExpandedHeight() {
        int i;
        int[] iArr = this.mExpandedViewContainerLocation;
        int i2 = 0;
        int i3 = iArr != null ? iArr[1] - this.mPositioner.getInsets().top : 0;
        if (!this.mIsOverflow) {
            i2 = this.mManageButtonHeight;
        }
        if (this.mPositioner.showBubblesVertically()) {
            i = this.mPointerWidth;
        } else {
            i = (int) ((((float) this.mPointerHeight) - this.mPointerOverlap) + ((float) this.mPointerMargin));
        }
        return ((((this.mPositioner.getAvailableRect().height() - i3) - getPaddingTop()) - getPaddingBottom()) - i2) - i;
    }

    public void updateView(int[] iArr) {
        this.mExpandedViewContainerLocation = iArr;
        updateHeight();
        TaskView taskView = this.mTaskView;
        if (taskView != null && taskView.getVisibility() == 0 && this.mTaskView.isAttachedToWindow()) {
            this.mTaskView.onLocationChanged();
        }
        if (this.mIsOverflow) {
            this.mOverflowView.show();
        }
    }

    public void setPointerPosition(float f, boolean z) {
        float f2;
        boolean showBubblesVertically = this.mPositioner.showBubblesVertically();
        float f3 = 0.0f;
        float f4 = (!showBubblesVertically || !z) ? 0.0f : ((float) this.mPointerHeight) - this.mPointerOverlap;
        float f5 = (!showBubblesVertically || z) ? 0.0f : ((float) this.mPointerHeight) - this.mPointerOverlap;
        if (!showBubblesVertically) {
            f3 = ((float) this.mPointerHeight) - this.mPointerOverlap;
        }
        setPadding((int) f4, (int) f3, (int) f5, 0);
        float expandedViewY = this.mPositioner.getExpandedViewY();
        float normalizedCircleSize = (float) IconNormalizer.getNormalizedCircleSize(this.mPositioner.getBubbleSize());
        if (showBubblesVertically) {
            f2 = f + (((float) this.mPositioner.getBubbleSize()) / 2.0f);
        } else {
            f2 = f + (normalizedCircleSize / 2.0f);
            expandedViewY = (float) this.mPointerWidth;
        }
        post(new Runnable(showBubblesVertically, f2 - expandedViewY, z) { // from class: com.android.wm.shell.bubbles.BubbleExpandedView$$ExternalSyntheticLambda1
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ boolean f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                BubbleExpandedView.this.lambda$setPointerPosition$1(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setPointerPosition$1(boolean z, float f, boolean z2) {
        float f2;
        float f3;
        if (z) {
            f2 = f - (((float) this.mPointerWidth) / 2.0f);
            if (z2) {
                f3 = ((float) (-this.mPointerHeight)) + this.mPointerOverlap;
            } else {
                f3 = ((float) (getWidth() - ((LinearLayout) this).mPaddingRight)) - this.mPointerOverlap;
            }
        } else {
            float f4 = this.mPointerOverlap;
            f3 = f - (((float) this.mPointerWidth) / 2.0f);
            f2 = f4;
        }
        this.mPointerView.setTranslationY(f2);
        this.mPointerView.setTranslationX(f3);
        this.mCurrentPointer = z ? z2 ? this.mLeftPointer : this.mRightPointer : this.mTopPointer;
        updatePointerView();
        this.mPointerView.setVisibility(0);
    }

    public void getManageButtonBoundsOnScreen(Rect rect) {
        this.mManageButton.getBoundsOnScreen(rect);
    }

    public void cleanUpExpandedState() {
        if (getTaskId() != -1) {
            try {
                ActivityTaskManager.getService().removeTask(getTaskId());
            } catch (RemoteException e) {
                Log.w("Bubbles", e.getMessage());
            }
        }
        TaskView taskView = this.mTaskView;
        if (taskView != null) {
            taskView.release();
            removeView(this.mTaskView);
            this.mTaskView = null;
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("BubbleExpandedView");
        printWriter.print("  taskId:               ");
        printWriter.println(this.mTaskId);
        printWriter.print("  stackView:            ");
        printWriter.println(this.mStackView);
    }
}
