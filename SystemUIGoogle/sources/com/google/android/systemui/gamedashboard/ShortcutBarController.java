package com.google.android.systemui.gamedashboard;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.google.android.systemui.gamedashboard.FpsController;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ShortcutBarController {
    private final Context mContext;
    private EntryPointController mEntryPointController;
    private final FpsController mFpsController;
    private boolean mIsAttached;
    private final ToastController mToast;
    private final GameDashboardUiEventLogger mUiEventLogger;
    private final ShortcutBarView mView;
    private final WindowManager mWindowManager;

    /* access modifiers changed from: package-private */
    public ShortcutBarController(Context context, WindowManager windowManager, FpsController fpsController, ConfigurationController configurationController, Handler handler, ScreenRecordController screenRecordController, Optional<TaskSurfaceHelper> optional, GameDashboardUiEventLogger gameDashboardUiEventLogger, ToastController toastController) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mFpsController = fpsController;
        this.mToast = toastController;
        this.mUiEventLogger = gameDashboardUiEventLogger;
        this.mView = ShortcutBarView.create(context, configurationController, this, handler, optional, screenRecordController, gameDashboardUiEventLogger);
    }

    public void setEntryPointController(EntryPointController entryPointController) {
        this.mEntryPointController = entryPointController;
        this.mUiEventLogger.setEntryPointController(entryPointController);
    }

    public void setExcludeBackRegionCallback(Consumer<Rect> consumer) {
        this.mView.setExcludeBackRegionCallback(consumer);
    }

    public void setNavBarButtonState(boolean z) {
        this.mView.setNavBarButtonState(z);
    }

    public void setScreenshotVisibility(boolean z) {
        if (z) {
            this.mToast.showShortcutText(R$string.game_dashboard_screenshot_shortcut_available);
        }
        this.mView.setScreenshotVisibility(z);
        onButtonVisibilityChange(z);
    }

    public boolean isScreenshotVisible() {
        return this.mView.isScreenshotVisible();
    }

    public void setRecordVisibility(boolean z) {
        if (z) {
            this.mToast.showShortcutText(R$string.game_dashboard_record_shortcut_available);
        }
        this.mView.setRecordVisibility(z);
        onButtonVisibilityChange(z);
    }

    public boolean isRecordVisible() {
        return this.mView.isRecordVisible();
    }

    public void registerFps(int i) {
        this.mFpsController.unregister();
        FpsController fpsController = this.mFpsController;
        ShortcutBarView shortcutBarView = this.mView;
        Objects.requireNonNull(shortcutBarView);
        fpsController.register(i, new FpsController.Callback() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarController$$ExternalSyntheticLambda0
            @Override // com.google.android.systemui.gamedashboard.FpsController.Callback
            public final void onFpsUpdated(float f) {
                ShortcutBarView.this.setFps(f);
            }
        });
    }

    public void unregisterFps() {
        this.mFpsController.unregister();
    }

    public void setFpsVisibility(boolean z) {
        if (z) {
            this.mToast.showShortcutText(R$string.game_dashboard_fps_counter_on);
        }
        this.mView.setFpsVisibility(z);
        onButtonVisibilityChange(z);
    }

    public boolean isFpsVisible() {
        return this.mView.isFpsVisible();
    }

    public void setEntryPointVisibility(boolean z, boolean z2) {
        this.mView.setEntryPointVisibility(z);
        onButtonVisibilityChange(z && z2);
    }

    public void setEntryPointOnTouchListener(View.OnClickListener onClickListener) {
        this.mView.setEntryPointOnTouchListener(onClickListener);
    }

    public ActivityManager.RunningTaskInfo getGameTaskInfo() {
        return this.mEntryPointController.getGameTaskInfo();
    }

    public void showDndText(boolean z) {
        if (z) {
            this.mToast.showShortcutText(R$string.game_dashboard_dnd_on);
        }
    }

    private void onButtonVisibilityChange(boolean z) {
        if (z && !this.mView.isAttachedToWindow() && this.mView.shouldBeVisible()) {
            show();
        } else if (!z && this.mView.isAttachedToWindow() && !this.mView.shouldBeVisible()) {
            hideUI();
        }
    }

    public void updateVisibility(boolean z) {
        if (z && !this.mView.isAttachedToWindow() && this.mView.shouldBeVisible()) {
            show();
        } else if (!z && this.mView.isAttachedToWindow()) {
            hideUI();
        }
    }

    public void show() {
        if (!this.mIsAttached) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 264, -3);
            layoutParams.setTrustedOverlay();
            layoutParams.setFitInsetsTypes(0);
            layoutParams.layoutInDisplayCutoutMode = 3;
            layoutParams.privateFlags |= 16;
            layoutParams.setTitle("Shortcut Bar");
            this.mWindowManager.addView(this.mView, layoutParams);
            this.mView.slideIn();
            this.mIsAttached = true;
        }
    }

    public void hide() {
        hideUI();
        unregisterFps();
    }

    public void autoUndock() {
        if (this.mIsAttached) {
            this.mView.autoUndock();
        }
    }

    private void hideUI() {
        if (this.mIsAttached) {
            this.mWindowManager.removeViewImmediate(this.mView);
            this.mIsAttached = false;
        }
        this.mToast.remove();
    }
}
