package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.GameManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
/* loaded from: classes2.dex */
public final class GameMenuActivity extends Activity implements View.OnApplyWindowInsetsListener {
    private static final IntentFilter DND_FILTER = new IntentFilter("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL");
    private final ActivityStarter mActivityStarter;
    private View mBackNavigationButton;
    private RadioButton mBatteryModeRadioButton;
    private final Context mContext;
    private final EntryPointController mController;
    private View mCurrentView;
    private WidgetContainer mCurrentWidgetContainer;
    private GameDashboardButton mDndButton;
    private final GameModeDndController mDndController;
    private GameManager mGameManager;
    private View mGameMenuMainView;
    private View mGameModeView;
    private WidgetView mGameModeWidget;
    private boolean mGameModesSupported;
    private final LayoutInflater mLayoutInflater;
    private final Handler mMainHandler;
    private int mMaxWidgetsPerContainer;
    private RadioButton mPerformanceModeRadioButton;
    private PlayGamesWidget mPlayGamesWidget;
    private View mSettingsButton;
    private int mShortAnimationDuration;
    private final ShortcutBarController mShortcutBarController;
    private RadioButton mStandardModeRadioButton;
    private final GameDashboardUiEventLogger mUiEventLogger;
    private LinearLayout mWidgetsView;
    private YouTubeLiveWidget mYouTubeLiveWidget;
    private final ArraySet<Integer> mAvailableModes = new ArraySet<>();
    private final BroadcastReceiver mDndReceiver = createDndReceiver();

    public static Intent createIntentForStart(Context context) {
        return new Intent(context, GameMenuActivity.class);
    }

    public GameMenuActivity(Context context, EntryPointController entryPointController, ActivityStarter activityStarter, ShortcutBarController shortcutBarController, GameModeDndController gameModeDndController, LayoutInflater layoutInflater, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mController = entryPointController;
        this.mShortcutBarController = shortcutBarController;
        this.mActivityStarter = activityStarter;
        this.mDndController = gameModeDndController;
        this.mLayoutInflater = layoutInflater;
        this.mMainHandler = handler;
        this.mUiEventLogger = gameDashboardUiEventLogger;
        gameDashboardUiEventLogger.setEntryPointController(entryPointController);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.game_dashboard_game_menu);
        View decorView = getWindow().getDecorView();
        decorView.getWindowInsetsController().hide(WindowInsets.Type.navigationBars());
        decorView.setOnApplyWindowInsetsListener(this);
        this.mGameManager = (GameManager) getSystemService(GameManager.class);
        initAvailableGameModes();
        this.mShortAnimationDuration = getResources().getInteger(17694720);
        View findViewById = findViewById(R$id.game_menu_back_button);
        this.mBackNavigationButton = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GameMenuActivity.this.lambda$onCreate$0(view);
            }
        });
        View view = this.mBackNavigationButton;
        int i = R$dimen.game_dashboard_back_button_touch_region_padding;
        expandTouchTargetArea(view, i);
        View findViewById2 = findViewById(R$id.game_menu_settings_button);
        this.mSettingsButton = findViewById2;
        findViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$1(view2);
            }
        });
        expandTouchTargetArea(this.mSettingsButton, i);
        View findViewById3 = findViewById(R$id.game_menu_close_button);
        findViewById3.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$2(view2);
            }
        });
        expandTouchTargetArea(findViewById3, R$dimen.game_menu_close_button_touch_region_padding);
        this.mGameMenuMainView = findViewById(R$id.game_menu_main);
        GameDashboardButton gameDashboardButton = (GameDashboardButton) findViewById(R$id.game_menu_screenshot);
        gameDashboardButton.setOnClickListener(new View.OnClickListener(gameDashboardButton) { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda16
            public final /* synthetic */ GameDashboardButton f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$3(this.f$1, view2);
            }
        });
        gameDashboardButton.setToggled(this.mShortcutBarController.isScreenshotVisible(), false);
        GameDashboardButton gameDashboardButton2 = (GameDashboardButton) findViewById(R$id.game_menu_screen_record);
        gameDashboardButton2.setOnClickListener(new View.OnClickListener(gameDashboardButton2) { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda15
            public final /* synthetic */ GameDashboardButton f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$4(this.f$1, view2);
            }
        });
        gameDashboardButton2.setToggled(this.mShortcutBarController.isRecordVisible(), false);
        GameDashboardButton gameDashboardButton3 = (GameDashboardButton) findViewById(R$id.game_menu_fps);
        gameDashboardButton3.setOnClickListener(new View.OnClickListener(gameDashboardButton3) { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda17
            public final /* synthetic */ GameDashboardButton f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$5(this.f$1, view2);
            }
        });
        gameDashboardButton3.setToggled(this.mShortcutBarController.isFpsVisible(), false);
        this.mContext.registerReceiver(this.mDndReceiver, DND_FILTER);
        this.mGameModeView = findViewById(R$id.game_mode);
        this.mPerformanceModeRadioButton = (RadioButton) findViewById(R$id.performance_mode_radio_button);
        if (this.mAvailableModes.contains(2)) {
            this.mPerformanceModeRadioButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GameMenuActivity.this.lambda$onCreate$6(view2);
                }
            });
            this.mPerformanceModeRadioButton.setEnabled(true);
            findViewById(R$id.performance_mode).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GameMenuActivity.this.lambda$onCreate$7(view2);
                }
            });
        } else {
            this.mPerformanceModeRadioButton.setEnabled(false);
        }
        RadioButton radioButton = (RadioButton) findViewById(R$id.standard_mode_radio_button);
        this.mStandardModeRadioButton = radioButton;
        radioButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$8(view2);
            }
        });
        int i2 = R$id.battery_mode_radio_button;
        this.mBatteryModeRadioButton = (RadioButton) findViewById(i2);
        this.mBatteryModeRadioButton = (RadioButton) findViewById(i2);
        if (this.mAvailableModes.contains(3)) {
            this.mBatteryModeRadioButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GameMenuActivity.this.lambda$onCreate$9(view2);
                }
            });
            findViewById(R$id.battery_mode).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda12
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GameMenuActivity.this.lambda$onCreate$10(view2);
                }
            });
            this.mBatteryModeRadioButton.setEnabled(true);
        } else {
            this.mBatteryModeRadioButton.setEnabled(false);
        }
        this.mDndButton = (GameDashboardButton) findViewById(R$id.game_menu_dnd);
        findViewById(R$id.standard_mode).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                GameMenuActivity.this.lambda$onCreate$11(view2);
            }
        });
        this.mCurrentView = this.mGameMenuMainView;
        initWidgets();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        onBackPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        startActivityForResult(new Intent("com.google.android.settings.games.GAME_SETTINGS").setPackage("com.android.settings"), 0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_CLOSE);
        this.mShortcutBarController.autoUndock();
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(GameDashboardButton gameDashboardButton, View view) {
        this.mShortcutBarController.setScreenshotVisibility(!gameDashboardButton.isToggled());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(GameDashboardButton gameDashboardButton, View view) {
        this.mShortcutBarController.setRecordVisibility(!gameDashboardButton.isToggled());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$5(GameDashboardButton gameDashboardButton, View view) {
        boolean z = !gameDashboardButton.isToggled();
        this.mShortcutBarController.setFpsVisibility(z);
        if (z) {
            this.mShortcutBarController.registerFps(this.mController.getGameTaskId());
        } else {
            this.mShortcutBarController.unregisterFps();
        }
    }

    private void expandTouchTargetArea(View view, int i) {
        View view2 = (View) view.getParent();
        view2.post(new Runnable(i, view, view2) { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda18
            public final /* synthetic */ int f$1;
            public final /* synthetic */ View f$2;
            public final /* synthetic */ View f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                GameMenuActivity.this.lambda$expandTouchTargetArea$12(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$expandTouchTargetArea$12(int i, View view, View view2) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(i);
        Rect rect = new Rect();
        view.getHitRect(rect);
        rect.top -= dimensionPixelSize;
        rect.bottom += dimensionPixelSize;
        rect.left -= dimensionPixelSize;
        rect.right += dimensionPixelSize;
        view2.setTouchDelegate(new TouchDelegate(rect, view));
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        updateDndButton();
        updateWidgets();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (this.mCurrentView.getId() == R$id.game_menu_main) {
            super.onBackPressed();
            return;
        }
        navigateToView(this.mGameMenuMainView, R$string.game_dashboard_game_menu_title);
        updateWidgets();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.mContext.unregisterReceiver(this.mDndReceiver);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            finish();
        }
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i;
        int i2;
        DisplayCutout displayCutout = windowInsets != null ? windowInsets.getDisplayCutout() : null;
        if (displayCutout != null) {
            int safeInsetLeft = displayCutout.getSafeInsetLeft();
            int safeInsetRight = displayCutout.getSafeInsetRight();
            if (safeInsetLeft >= safeInsetRight) {
                i2 = safeInsetLeft - safeInsetRight;
                i = 0;
            } else {
                i = safeInsetRight - safeInsetLeft;
                i2 = 0;
            }
            view.setPadding(i, 0, i2, 0);
            windowInsets.consumeDisplayCutout();
        }
        return windowInsets;
    }

    void updateDndButton() {
        this.mDndController.refreshRule();
        this.mDndButton.setToggled(this.mDndController.isGameModeDndOn(), false);
        this.mDndButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GameMenuActivity.this.lambda$updateDndButton$13(view);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDndButton$13(View view) {
        boolean z = !this.mDndController.isGameModeDndOn();
        this.mShortcutBarController.showDndText(z);
        this.mDndController.setGameModeDndOn(z);
    }

    private void initWidgets() {
        this.mWidgetsView = (LinearLayout) findViewById(R$id.game_menu_widgets);
        if (this.mContext.getResources().getConfiguration().orientation != 2) {
            this.mMaxWidgetsPerContainer = 2;
        } else {
            this.mMaxWidgetsPerContainer = 3;
        }
        LayoutInflater layoutInflater = this.mLayoutInflater;
        int i = R$layout.game_menu_widget_container;
        WidgetContainer widgetContainer = (WidgetContainer) layoutInflater.inflate(i, (ViewGroup) this.mWidgetsView, false);
        this.mCurrentWidgetContainer = widgetContainer;
        WidgetView widgetView = (WidgetView) this.mLayoutInflater.inflate(R$layout.game_menu_widget, (ViewGroup) widgetContainer, false);
        this.mGameModeWidget = widgetView;
        this.mCurrentWidgetContainer.addWidget(widgetView);
        YouTubeLiveWidget create = YouTubeLiveWidget.create(this, this.mCurrentWidgetContainer, this.mUiEventLogger);
        this.mYouTubeLiveWidget = create;
        this.mCurrentWidgetContainer.addWidget(create.getView());
        if (this.mCurrentWidgetContainer.getWidgetCount() == this.mMaxWidgetsPerContainer) {
            this.mWidgetsView.addView(this.mCurrentWidgetContainer);
            this.mCurrentWidgetContainer = (WidgetContainer) this.mLayoutInflater.inflate(i, (ViewGroup) this.mWidgetsView, false);
        }
        PlayGamesWidget create2 = PlayGamesWidget.create(this, this.mCurrentWidgetContainer, this.mMainHandler, this.mUiEventLogger);
        this.mPlayGamesWidget = create2;
        if (create2 != null) {
            this.mCurrentWidgetContainer.addWidget(create2.getView());
        }
        if (this.mCurrentWidgetContainer.getWidgetCount() == 1 && this.mMaxWidgetsPerContainer == 2) {
            while (this.mCurrentWidgetContainer.getWidgetCount() < this.mMaxWidgetsPerContainer) {
                this.mCurrentWidgetContainer.addWidget(PlaceholderWidget.create(this, this.mCurrentWidgetContainer).getView());
            }
        }
        this.mWidgetsView.addView(this.mCurrentWidgetContainer);
    }

    private void initAvailableGameModes() {
        for (int i : this.mGameManager.getAvailableGameModes(this.mController.getGamePackageName())) {
            this.mAvailableModes.add(Integer.valueOf(i));
        }
        if (!this.mAvailableModes.contains(0)) {
            this.mAvailableModes.add(1);
            this.mGameModesSupported = true;
        }
    }

    private void updateWidgets() {
        String gamePackageName = this.mController.getGamePackageName();
        updateGameModeWidget(gamePackageName);
        this.mYouTubeLiveWidget.update(gamePackageName);
        PlayGamesWidget playGamesWidget = this.mPlayGamesWidget;
        if (playGamesWidget != null) {
            playGamesWidget.update(gamePackageName);
        }
    }

    private void updateGameModeWidget(String str) {
        int gameMode = this.mGameManager.getGameMode(str);
        if (!this.mGameModesSupported) {
            this.mGameModeWidget.setEnabled(false);
            this.mGameModeWidget.update(getDrawable(R$drawable.ic_game_mode), R$string.game_menu_game_mode_title, R$string.game_mode_unsupported_description, null);
        } else if (gameMode == 2) {
            this.mGameModeWidget.setEnabled(true);
            this.mGameModeWidget.update(getDrawable(R$drawable.ic_game_mode_performance_widget), R$string.game_mode_performance_title, R$string.game_mode_performance_description, new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GameMenuActivity.this.lambda$updateGameModeWidget$14(view);
                }
            });
        } else if (gameMode == 3) {
            this.mGameModeWidget.setEnabled(true);
            this.mGameModeWidget.update(getDrawable(R$drawable.ic_game_mode_battery_widget), R$string.game_mode_battery_title, R$string.game_mode_battery_description, new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GameMenuActivity.this.lambda$updateGameModeWidget$15(view);
                }
            });
        } else {
            this.mGameModeWidget.update(getDrawable(R$drawable.ic_game_mode), R$string.game_menu_game_mode_title, R$string.game_menu_game_mode_description, new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GameMenuActivity.this.lambda$updateGameModeWidget$16(view);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateGameModeWidget$14(View view) {
        navigateToGameModeView();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateGameModeWidget$15(View view) {
        navigateToGameModeView();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateGameModeWidget$16(View view) {
        navigateToGameModeView();
    }

    private void navigateToGameModeView() {
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_GAME_MODE_WIDGET);
        navigateToView(this.mGameModeView, R$string.game_menu_game_mode_title);
        toggleGameModeRadioButtons(this.mGameManager.getGameMode(this.mController.getGamePackageName()));
    }

    /* access modifiers changed from: private */
    /* renamed from: onGameModeSelectionChanged */
    public void lambda$onCreate$9(View view) {
        int i;
        int id = view.getId();
        if (id == R$id.performance_mode || id == R$id.performance_mode_radio_button) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_PERFORMANCE);
            i = 2;
        } else if (id == R$id.standard_mode || id == R$id.standard_mode_radio_button) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_STANDARD);
            i = 1;
        } else if (id == R$id.battery_mode || id == R$id.battery_mode_radio_button) {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_GAME_MODE_BATTERY);
            i = 3;
        } else {
            i = 0;
        }
        String gamePackageName = this.mController.getGamePackageName();
        int gameMode = this.mGameManager.getGameMode(gamePackageName);
        if (gameMode != i) {
            toggleGameModeRadioButtons(gameMode);
            showRestartDialog(gamePackageName, i);
        }
    }

    private void showRestartDialog(String str, int i) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this);
        systemUIDialog.setTitle(R$string.game_mode_restart_dialog_title);
        if (i == 1) {
            systemUIDialog.setMessage(R$string.game_mode_restart_dialog_message_standard);
        } else if (i == 2) {
            systemUIDialog.setMessage(R$string.game_mode_restart_dialog_message_performance);
        } else if (i == 3) {
            systemUIDialog.setMessage(R$string.game_mode_restart_dialog_message_battery);
        }
        systemUIDialog.setPositiveButton(R$string.game_mode_restart_dialog_confirm, new DialogInterface.OnClickListener(i, str) { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;
            public final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                GameMenuActivity.this.lambda$showRestartDialog$17(this.f$1, this.f$2, dialogInterface, i2);
            }
        });
        systemUIDialog.setNegativeButton(R$string.game_mode_restart_dialog_cancel, new DialogInterface.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                GameMenuActivity.this.lambda$showRestartDialog$18(dialogInterface, i2);
            }
        });
        systemUIDialog.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showRestartDialog$17(int i, String str, DialogInterface dialogInterface, int i2) {
        toggleGameModeRadioButtons(i);
        this.mGameManager.setGameMode(str, i);
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_RESTART_NOW);
        restartGameActivity();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showRestartDialog$18(DialogInterface dialogInterface, int i) {
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_RESTART_CANCELLED);
    }

    private void restartGameActivity() {
        try {
            String gamePackageName = this.mController.getGamePackageName();
            ActivityManager.getService().forceStopPackage(this.mController.getGamePackageName(), -2);
            this.mActivityStarter.startActivity(getPackageManager().getLaunchIntentForPackage(gamePackageName), true);
        } catch (RemoteException e) {
            Log.e("GameMenuActivity", "Fail to restart the game: " + e.getMessage());
        }
        finish();
    }

    private void navigateToView(final View view, final int i) {
        if (view != null) {
            view.setAlpha(0.0f);
            view.setVisibility(0);
            view.animate().alpha(1.0f).setDuration((long) this.mShortAnimationDuration).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (GameMenuActivity.this.mCurrentView.getId() == R$id.game_menu_main) {
                        GameMenuActivity.this.mBackNavigationButton.setVisibility(0);
                        GameMenuActivity.this.mSettingsButton.setVisibility(4);
                    } else {
                        GameMenuActivity.this.mBackNavigationButton.setVisibility(4);
                        GameMenuActivity.this.mSettingsButton.setVisibility(0);
                    }
                    ((TextView) GameMenuActivity.this.findViewById(R$id.game_menu_title)).setText(i);
                    GameMenuActivity.this.mCurrentView = view;
                }
            });
            final View view2 = this.mCurrentView;
            view2.animate().alpha(0.0f).setDuration((long) this.mShortAnimationDuration).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view2.setVisibility(8);
                }
            });
        }
    }

    private void toggleGameModeRadioButtons(int i) {
        boolean z = false;
        this.mPerformanceModeRadioButton.setChecked(i == 2);
        this.mStandardModeRadioButton.setChecked(i == 1);
        RadioButton radioButton = this.mBatteryModeRadioButton;
        if (i == 3) {
            z = true;
        }
        radioButton.setChecked(z);
    }

    private BroadcastReceiver createDndReceiver() {
        return new BroadcastReceiver() { // from class: com.google.android.systemui.gamedashboard.GameMenuActivity.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                GameDashboardUiEventLogger.GameDashboardEvent gameDashboardEvent;
                if (GameMenuActivity.this.mDndController != null && "android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL".equals(intent.getAction())) {
                    GameMenuActivity.this.mDndController.refreshRule();
                    if (GameMenuActivity.this.mDndButton.isToggled() != GameMenuActivity.this.mDndController.isGameModeDndOn()) {
                        GameMenuActivity.this.mDndButton.setToggled(GameMenuActivity.this.mDndController.isGameModeDndOn(), true);
                    }
                    GameDashboardUiEventLogger gameDashboardUiEventLogger = GameMenuActivity.this.mUiEventLogger;
                    if (GameMenuActivity.this.mDndController.isGameModeDndOn()) {
                        gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_ENABLE_DND;
                    } else {
                        gameDashboardEvent = GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_DISABLE_DND;
                    }
                    gameDashboardUiEventLogger.log(gameDashboardEvent);
                }
            }
        };
    }
}
