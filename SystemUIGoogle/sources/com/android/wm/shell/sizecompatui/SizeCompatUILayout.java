package com.android.wm.shell.sizecompatui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.view.SurfaceControl;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SizeCompatUILayout {
    @VisibleForTesting
    SizeCompatRestartButton mButton;
    final int mButtonSize;
    @VisibleForTesting
    final SizeCompatUIWindowManager mButtonWindowManager;
    private final SizeCompatUIController.SizeCompatUICallback mCallback;
    private Context mContext;
    private final int mDisplayId;
    private DisplayLayout mDisplayLayout;
    @VisibleForTesting
    SizeCompatHintPopup mHint;
    @VisibleForTesting
    SizeCompatUIWindowManager mHintWindowManager;
    final int mPopupOffsetX;
    final int mPopupOffsetY;
    boolean mShouldShowHint;
    private final SyncTransactionQueue mSyncQueue;
    private Configuration mTaskConfig;
    private final int mTaskId;
    private ShellTaskOrganizer.TaskListener mTaskListener;

    /* access modifiers changed from: package-private */
    public SizeCompatUILayout(SyncTransactionQueue syncTransactionQueue, SizeCompatUIController.SizeCompatUICallback sizeCompatUICallback, Context context, Configuration configuration, int i, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, boolean z) {
        this.mSyncQueue = syncTransactionQueue;
        this.mCallback = sizeCompatUICallback;
        Context createConfigurationContext = context.createConfigurationContext(configuration);
        this.mContext = createConfigurationContext;
        this.mTaskConfig = configuration;
        this.mDisplayId = createConfigurationContext.getDisplayId();
        this.mTaskId = i;
        this.mTaskListener = taskListener;
        this.mDisplayLayout = displayLayout;
        this.mShouldShowHint = !z;
        this.mButtonWindowManager = new SizeCompatUIWindowManager(this.mContext, configuration, this);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.size_compat_button_size);
        this.mButtonSize = dimensionPixelSize;
        this.mPopupOffsetX = dimensionPixelSize / 4;
        this.mPopupOffsetY = dimensionPixelSize;
    }

    /* access modifiers changed from: package-private */
    public void createSizeCompatButton(boolean z) {
        if (!z && this.mButton == null) {
            this.mButton = this.mButtonWindowManager.createSizeCompatButton();
            updateButtonSurfacePosition();
            if (this.mShouldShowHint) {
                this.mShouldShowHint = false;
                createSizeCompatHint();
            }
        }
    }

    private void createSizeCompatHint() {
        if (this.mHint == null) {
            SizeCompatUIWindowManager createHintWindowManager = createHintWindowManager();
            this.mHintWindowManager = createHintWindowManager;
            this.mHint = createHintWindowManager.createSizeCompatHint();
            updateHintSurfacePosition();
        }
    }

    @VisibleForTesting
    SizeCompatUIWindowManager createHintWindowManager() {
        return new SizeCompatUIWindowManager(this.mContext, this.mTaskConfig, this);
    }

    /* access modifiers changed from: package-private */
    public void dismissHint() {
        this.mHint = null;
        SizeCompatUIWindowManager sizeCompatUIWindowManager = this.mHintWindowManager;
        if (sizeCompatUIWindowManager != null) {
            sizeCompatUIWindowManager.release();
            this.mHintWindowManager = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        dismissHint();
        this.mButton = null;
        this.mButtonWindowManager.release();
    }

    /* access modifiers changed from: package-private */
    public void updateSizeCompatInfo(Configuration configuration, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        Configuration configuration2 = this.mTaskConfig;
        ShellTaskOrganizer.TaskListener taskListener2 = this.mTaskListener;
        this.mTaskConfig = configuration;
        this.mTaskListener = taskListener;
        this.mContext = this.mContext.createConfigurationContext(configuration);
        this.mButtonWindowManager.setConfiguration(configuration);
        SizeCompatUIWindowManager sizeCompatUIWindowManager = this.mHintWindowManager;
        if (sizeCompatUIWindowManager != null) {
            sizeCompatUIWindowManager.setConfiguration(configuration);
        }
        if (this.mButton == null || taskListener2 != taskListener) {
            release();
            createSizeCompatButton(z);
            return;
        }
        if (!configuration.windowConfiguration.getBounds().equals(configuration2.windowConfiguration.getBounds())) {
            updateButtonSurfacePosition();
            updateHintSurfacePosition();
        }
        if (configuration.getLayoutDirection() != configuration2.getLayoutDirection()) {
            this.mButton.setLayoutDirection(configuration.getLayoutDirection());
            updateButtonSurfacePosition();
            SizeCompatHintPopup sizeCompatHintPopup = this.mHint;
            if (sizeCompatHintPopup != null) {
                sizeCompatHintPopup.setLayoutDirection(configuration.getLayoutDirection());
                updateHintSurfacePosition();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDisplayLayout(DisplayLayout displayLayout) {
        if (displayLayout != this.mDisplayLayout) {
            Rect rect = new Rect();
            Rect rect2 = new Rect();
            this.mDisplayLayout.getStableBounds(rect);
            displayLayout.getStableBounds(rect2);
            this.mDisplayLayout = displayLayout;
            if (!rect.equals(rect2)) {
                updateButtonSurfacePosition();
                updateHintSurfacePosition();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateImeVisibility(boolean z) {
        SizeCompatRestartButton sizeCompatRestartButton = this.mButton;
        if (sizeCompatRestartButton == null) {
            createSizeCompatButton(z);
            return;
        }
        int i = z ? 8 : 0;
        if (sizeCompatRestartButton.getVisibility() != i) {
            this.mButton.setVisibility(i);
        }
        SizeCompatHintPopup sizeCompatHintPopup = this.mHint;
        if (sizeCompatHintPopup != null && sizeCompatHintPopup.getVisibility() != i) {
            this.mHint.setVisibility(i);
        }
    }

    /* access modifiers changed from: package-private */
    public WindowManager.LayoutParams getButtonWindowLayoutParams() {
        int i = this.mButtonSize;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i, 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(SizeCompatRestartButton.class.getSimpleName() + getTaskId());
        layoutParams.privateFlags = layoutParams.privateFlags | 536870976;
        return layoutParams;
    }

    /* access modifiers changed from: package-private */
    public WindowManager.LayoutParams getHintWindowLayoutParams(SizeCompatHintPopup sizeCompatHintPopup) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(sizeCompatHintPopup.getMeasuredWidth(), sizeCompatHintPopup.getMeasuredHeight(), 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(SizeCompatHintPopup.class.getSimpleName() + getTaskId());
        layoutParams.privateFlags = layoutParams.privateFlags | 536870976;
        layoutParams.windowAnimations = 16973910;
        return layoutParams;
    }

    /* access modifiers changed from: package-private */
    public void attachToParentSurface(SurfaceControl.Builder builder) {
        this.mTaskListener.attachChildSurfaceToTask(this.mTaskId, builder);
    }

    /* access modifiers changed from: package-private */
    public void onRestartButtonClicked() {
        this.mCallback.onSizeCompatRestartButtonClicked(this.mTaskId);
    }

    /* access modifiers changed from: package-private */
    public void onRestartButtonLongClicked() {
        createSizeCompatHint();
    }

    @VisibleForTesting
    void updateButtonSurfacePosition() {
        int i;
        int i2;
        if (this.mButton != null && this.mButtonWindowManager.getSurfaceControl() != null) {
            SurfaceControl surfaceControl = this.mButtonWindowManager.getSurfaceControl();
            Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
            Rect rect = new Rect();
            this.mDisplayLayout.getStableBounds(rect);
            rect.intersect(bounds);
            if (getLayoutDirection() == 1) {
                i2 = rect.left;
                i = bounds.left;
            } else {
                i2 = rect.right - bounds.left;
                i = this.mButtonSize;
            }
            updateSurfacePosition(surfaceControl, i2 - i, (rect.bottom - bounds.top) - this.mButtonSize);
        }
    }

    void updateHintSurfacePosition() {
        SizeCompatUIWindowManager sizeCompatUIWindowManager;
        int i;
        if (this.mHint != null && (sizeCompatUIWindowManager = this.mHintWindowManager) != null && sizeCompatUIWindowManager.getSurfaceControl() != null) {
            SurfaceControl surfaceControl = this.mHintWindowManager.getSurfaceControl();
            Rect bounds = this.mTaskConfig.windowConfiguration.getBounds();
            Rect rect = new Rect();
            this.mDisplayLayout.getStableBounds(rect);
            rect.intersect(bounds);
            if (getLayoutDirection() == 1) {
                i = (rect.left - bounds.left) + this.mPopupOffsetX;
            } else {
                i = ((rect.right - bounds.left) - this.mPopupOffsetX) - this.mHint.getMeasuredWidth();
            }
            updateSurfacePosition(surfaceControl, i, ((rect.bottom - bounds.top) - this.mPopupOffsetY) - this.mHint.getMeasuredHeight());
        }
    }

    private void updateSurfacePosition(SurfaceControl surfaceControl, int i, int i2) {
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(surfaceControl, i, i2) { // from class: com.android.wm.shell.sizecompatui.SizeCompatUILayout$$ExternalSyntheticLambda0
            public final /* synthetic */ SurfaceControl f$0;
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                SizeCompatUILayout.lambda$updateSurfacePosition$0(this.f$0, this.f$1, this.f$2, transaction);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateSurfacePosition$0(SurfaceControl surfaceControl, int i, int i2, SurfaceControl.Transaction transaction) {
        transaction.setPosition(surfaceControl, (float) i, (float) i2);
        transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
    }

    /* access modifiers changed from: package-private */
    public int getDisplayId() {
        return this.mDisplayId;
    }

    /* access modifiers changed from: package-private */
    public int getTaskId() {
        return this.mTaskId;
    }

    private int getLayoutDirection() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection();
    }
}
