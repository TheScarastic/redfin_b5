package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$anim;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
/* loaded from: classes2.dex */
public class ToastController implements ConfigurationController.ConfigurationListener, NavigationModeController.ModeChangedListener {
    private final Context mContext;
    private int mFadeInAnimationDuration;
    private int mFadeOutAnimationDuration;
    private TextView mLaunchChangeView;
    private View mLaunchContainer;
    private TextView mLaunchDndMessageView;
    private TextView mLaunchGameModeMessageView;
    private View mLaunchLayout;
    private int mNavBarMode;
    private int mOrientationMargin;
    private View mRecordSaveContainer;
    private TextView mRecordSaveView;
    private TextView mShortcutView;
    private int mTranslateDownAnimationDuration;
    private int mTranslateUpAnimationDuration;
    private final UiEventLogger mUiEventLogger;
    private final WindowManager mWindowManager;
    private int mIsAddedToWindowManager = 0;
    private int mGameTaskId = 0;

    public ToastController(Context context, ConfigurationController configurationController, WindowManager windowManager, UiEventLogger uiEventLogger, NavigationModeController navigationModeController) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarMode = navigationModeController.addListener(this);
        configurationController.addCallback(this);
        setResourceValues();
    }

    public void showLaunchText(int i, int i2, boolean z) {
        int i3;
        if (this.mGameTaskId != i) {
            this.mGameTaskId = i;
            this.mLaunchDndMessageView.setVisibility(8);
            this.mLaunchGameModeMessageView.setVisibility(8);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            if (z) {
                this.mLaunchDndMessageView.setVisibility(0);
                sb.append(this.mContext.getString(R$string.game_launch_dnd_on));
                this.mLaunchDndMessageView.setText(sb);
                i3 = 1;
            } else {
                i3 = 0;
            }
            if (i2 == 2) {
                i3++;
                this.mLaunchGameModeMessageView.setVisibility(0);
                sb2.append(this.mContext.getString(R$string.game_launch_performance));
                this.mLaunchGameModeMessageView.setText(sb2);
            } else if (i2 == 3) {
                i3++;
                this.mLaunchGameModeMessageView.setVisibility(0);
                sb2.append(this.mContext.getString(R$string.game_launch_battery));
                this.mLaunchGameModeMessageView.setText(sb2);
            }
            if (i3 > 0) {
                int margin = getMargin();
                this.mLaunchLayout.measure(0, 0);
                removeViewImmediate();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, margin + this.mLaunchLayout.getMeasuredHeight(), 0, 0, 2024, 8, -3);
                layoutParams.privateFlags |= 16;
                layoutParams.layoutInDisplayCutoutMode = 3;
                layoutParams.setTitle("ToastText");
                layoutParams.setFitInsetsTypes(0);
                layoutParams.gravity = 80;
                show(layoutParams, 1);
                this.mLaunchDndMessageView.announceForAccessibility(sb);
                this.mLaunchGameModeMessageView.announceForAccessibility(sb2);
            }
        }
    }

    public void showShortcutText(int i) {
        String str = (String) this.mContext.getResources().getText(i);
        this.mShortcutView.setText(str);
        int margin = getMargin();
        removeViewImmediate();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 0, margin, 2024, 8, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setTitle("ToastText");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 80;
        show(layoutParams, 2);
        this.mShortcutView.announceForAccessibility(str);
    }

    public void showRecordSaveText() {
        String str = (String) this.mContext.getResources().getText(R$string.game_screen_record_saved);
        this.mRecordSaveView.setText(str);
        int margin = getMargin();
        this.mRecordSaveView.measure(0, 0);
        removeViewImmediate();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, margin + this.mRecordSaveView.getMeasuredHeight(), 0, 0, 2024, 8, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setTitle("ToastText");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 80;
        show(layoutParams, 3);
        this.mRecordSaveView.announceForAccessibility(str);
    }

    private void show(WindowManager.LayoutParams layoutParams, int i) {
        if (i == 1) {
            this.mLaunchLayout.setTranslationY(500.0f);
            this.mWindowManager.addView(this.mLaunchContainer, layoutParams);
            this.mIsAddedToWindowManager = 1;
            this.mLaunchLayout.setVisibility(0);
            this.mLaunchLayout.animate().translationY(0.0f).setDuration((long) this.mTranslateUpAnimationDuration).setStartDelay(1000).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.hide();
                }
            });
        } else if (i == 2) {
            this.mShortcutView.setAlpha(0.0f);
            this.mWindowManager.addView(this.mShortcutView, layoutParams);
            this.mIsAddedToWindowManager = 2;
            this.mShortcutView.setVisibility(0);
            this.mShortcutView.animate().alpha(1.0f).setDuration((long) this.mFadeInAnimationDuration).setStartDelay(0).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.hide();
                }
            });
        } else if (i == 3) {
            this.mRecordSaveView.setTranslationY(500.0f);
            this.mWindowManager.addView(this.mRecordSaveContainer, layoutParams);
            this.mIsAddedToWindowManager = 3;
            this.mRecordSaveView.setVisibility(0);
            this.mRecordSaveView.animate().translationY(0.0f).setDuration((long) this.mTranslateUpAnimationDuration).setStartDelay(1000).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.hide();
                }
            });
        }
    }

    public boolean hide() {
        int i = this.mIsAddedToWindowManager;
        if (i == 0) {
            return false;
        }
        if (i == 1) {
            this.mLaunchLayout.animate().translationY(500.0f).setDuration((long) this.mTranslateDownAnimationDuration).setStartDelay(3000).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.mLaunchLayout.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                }
            });
        } else if (i == 2) {
            this.mShortcutView.animate().alpha(0.0f).setDuration((long) this.mFadeOutAnimationDuration).setStartDelay(1000).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.mShortcutView.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                }
            });
        } else if (i == 3) {
            this.mRecordSaveView.animate().translationY(500.0f).setDuration((long) this.mTranslateDownAnimationDuration).setStartDelay(3000).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ToastController.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ToastController.this.mRecordSaveView.setVisibility(8);
                    ToastController.this.removeViewImmediate();
                }
            });
        }
        return true;
    }

    public void removeViewImmediate() {
        int i = this.mIsAddedToWindowManager;
        if (i == 1) {
            this.mWindowManager.removeViewImmediate(this.mLaunchContainer);
        } else if (i == 2) {
            this.mWindowManager.removeViewImmediate(this.mShortcutView);
        } else if (i == 3) {
            this.mWindowManager.removeViewImmediate(this.mRecordSaveContainer);
        }
        this.mIsAddedToWindowManager = 0;
        this.mLaunchLayout.animate().cancel();
        this.mShortcutView.animate().cancel();
        this.mRecordSaveView.animate().cancel();
    }

    public void remove() {
        this.mLaunchLayout.setVisibility(8);
        this.mShortcutView.setVisibility(8);
        this.mRecordSaveView.setVisibility(8);
        removeViewImmediate();
    }

    private void setResourceValues() {
        Resources resources = this.mContext.getResources();
        if (this.mIsAddedToWindowManager != 1) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.game_launch_toast, (ViewGroup) null);
            this.mLaunchContainer = inflate;
            View findViewById = inflate.findViewById(R$id.game_launch_toast);
            this.mLaunchLayout = findViewById;
            this.mLaunchDndMessageView = (TextView) findViewById.findViewById(R$id.game_launch_toast_dnd_message);
            this.mLaunchGameModeMessageView = (TextView) this.mLaunchLayout.findViewById(R$id.game_launch_toast_game_mode_message);
            TextView textView = (TextView) this.mLaunchLayout.findViewById(R$id.game_launch_toast_change);
            this.mLaunchChangeView = textView;
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.ToastController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToastController.this.lambda$setResourceValues$0(view);
                }
            });
        }
        if (this.mIsAddedToWindowManager != 2) {
            this.mShortcutView = (TextView) LayoutInflater.from(this.mContext).inflate(R$layout.game_menu_shortcut_toast, (ViewGroup) null);
        }
        if (this.mIsAddedToWindowManager != 3) {
            View inflate2 = LayoutInflater.from(this.mContext).inflate(R$layout.game_screen_record_save_toast, (ViewGroup) null);
            this.mRecordSaveContainer = inflate2;
            this.mRecordSaveView = (TextView) inflate2.findViewById(R$id.game_screen_record_save_toast);
        }
        this.mFadeInAnimationDuration = resources.getInteger(R$integer.game_toast_fade_in_animation_duration);
        this.mFadeOutAnimationDuration = resources.getInteger(R$integer.game_toast_fade_out_animation_duration);
        this.mTranslateUpAnimationDuration = resources.getInteger(R$integer.game_toast_translate_up_animation_duration);
        this.mTranslateDownAnimationDuration = resources.getInteger(R$integer.game_toast_translate_down_animation_duration);
        this.mOrientationMargin = resources.getDimensionPixelSize(R$dimen.game_toast_margin);
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        setResourceValues();
    }

    /* access modifiers changed from: private */
    /* renamed from: onLaunchClicked */
    public void lambda$setResourceValues$0(View view) {
        removeViewImmediate();
        Intent createIntentForStart = GameMenuActivity.createIntentForStart(this.mContext);
        ActivityOptions makeCustomTaskAnimation = ActivityOptions.makeCustomTaskAnimation(this.mContext, R$anim.game_dashboard_fade_in, R$anim.game_dashboard_fade_out, null, null, null);
        ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
        makeCustomTaskAnimation.setLaunchTaskId(runningTask.taskId);
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_LAUNCH, 0, runningTask.baseActivity.getPackageName());
        this.mContext.startActivity(createIntentForStart, makeCustomTaskAnimation.toBundle());
    }

    private int getMargin() {
        int i;
        int i2 = this.mContext.getResources().getConfiguration().orientation;
        int i3 = this.mOrientationMargin;
        if (i2 != 1) {
            return i3;
        }
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            i = this.mContext.getResources().getDimensionPixelSize(R$dimen.navigation_bar_gesture_height);
        } else {
            i = this.mContext.getResources().getDimensionPixelSize(R$dimen.navigation_bar_height);
        }
        return i3 + i;
    }
}
