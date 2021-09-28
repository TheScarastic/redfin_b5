package com.android.systemui.wallet.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Bundle;
import android.os.Handler;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletServiceEvent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$menu;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class WalletActivity extends LifecycleActivity implements QuickAccessWalletClient.WalletServiceEventListener {
    private final ActivityStarter mActivityStarter;
    private final Executor mExecutor;
    private FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final Handler mHandler;
    private boolean mHasRegisteredListener;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final UiEventLogger mUiEventLogger;
    private final UserTracker mUserTracker;
    private QuickAccessWalletClient mWalletClient;
    private WalletScreenController mWalletScreenController;

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onCreate$2() {
        return false;
    }

    public WalletActivity(KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, ActivityStarter activityStarter, Executor executor, Handler handler, FalsingManager falsingManager, FalsingCollector falsingCollector, UserTracker userTracker, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarKeyguardViewManager statusBarKeyguardViewManager, UiEventLogger uiEventLogger) {
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mActivityStarter = activityStarter;
        this.mExecutor = executor;
        this.mHandler = handler;
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mUserTracker = userTracker;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mUiEventLogger = uiEventLogger;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(Integer.MIN_VALUE);
        requestWindowFeature(1);
        setContentView(R$layout.quick_access_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R$id.action_bar);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(getHomeIndicatorDrawable());
        getActionBar().setHomeActionContentDescription(R$string.accessibility_desc_close);
        WalletView walletView = (WalletView) requireViewById(R$id.wallet_view);
        this.mWalletClient = QuickAccessWalletClient.create(this);
        this.mWalletScreenController = new WalletScreenController(this, walletView, this.mWalletClient, this.mActivityStarter, this.mExecutor, this.mHandler, this.mUserTracker, this.mFalsingManager, this.mKeyguardUpdateMonitor, this.mKeyguardStateController, this.mUiEventLogger);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.wallet.ui.WalletActivity.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
                Log.d("WalletActivity", "Biometric running state has changed.");
                WalletActivity.this.mWalletScreenController.queryWalletCards();
            }
        };
        walletView.setFalsingCollector(this.mFalsingCollector);
        walletView.setShowWalletAppOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WalletActivity.this.lambda$onCreate$1(view);
            }
        });
        walletView.setDeviceLockedActionOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WalletActivity.this.lambda$onCreate$3(view);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        if (this.mWalletClient.createWalletIntent() == null) {
            Log.w("WalletActivity", "Unable to create wallet app intent.");
        } else if (!this.mKeyguardStateController.isUnlocked() && this.mFalsingManager.isFalseTap(1)) {
        } else {
            if (this.mKeyguardStateController.isUnlocked()) {
                this.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
                this.mActivityStarter.startActivity(this.mWalletClient.createWalletIntent(), true);
                finish();
                return;
            }
            this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_SHOW_ALL_BUTTON);
            this.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.wallet.ui.WalletActivity$$ExternalSyntheticLambda2
                @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                public final boolean onDismiss() {
                    return WalletActivity.this.lambda$onCreate$0();
                }
            }, false, true);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$0() {
        this.mUiEventLogger.log(WalletUiEvent.QAW_SHOW_ALL);
        this.mActivityStarter.startActivity(this.mWalletClient.createWalletIntent(), true);
        finish();
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        Log.d("WalletActivity", "Wallet action button is clicked.");
        if (this.mFalsingManager.isFalseTap(1)) {
            Log.d("WalletActivity", "False tap detected on wallet action button.");
            return;
        }
        this.mUiEventLogger.log(WalletUiEvent.QAW_UNLOCK_FROM_UNLOCK_BUTTON);
        this.mKeyguardDismissUtil.executeWhenUnlocked(WalletActivity$$ExternalSyntheticLambda3.INSTANCE, false, false);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        if (!this.mHasRegisteredListener) {
            this.mWalletClient.addWalletServiceEventListener(this);
            this.mHasRegisteredListener = true;
        }
        this.mKeyguardStateController.addCallback(this.mWalletScreenController);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mWalletScreenController.queryWalletCards();
        this.mKeyguardViewManager.requestFp(true, Utils.getColorAttrDefaultColor(this, 17956900));
        this.mKeyguardViewManager.requestFace(true);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mKeyguardViewManager.requestFp(false, -1);
        this.mKeyguardViewManager.requestFace(false);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R$menu.wallet_activity_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onWalletServiceEvent(WalletServiceEvent walletServiceEvent) {
        int eventType = walletServiceEvent.getEventType();
        if (eventType == 1) {
            return;
        }
        if (eventType != 2) {
            Log.w("WalletActivity", "onWalletServiceEvent: Unknown event type");
        } else {
            this.mWalletScreenController.queryWalletCards();
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId != R$id.wallet_lockscreen_settings) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            this.mActivityStarter.startActivity(new Intent("android.settings.LOCK_SCREEN_SETTINGS").addFlags(335544320), true);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onDestroy() {
        this.mKeyguardStateController.removeCallback(this.mWalletScreenController);
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mKeyguardUpdateMonitorCallback;
        if (keyguardUpdateMonitorCallback != null) {
            this.mKeyguardUpdateMonitor.removeCallback(keyguardUpdateMonitorCallback);
        }
        this.mWalletScreenController.onDismissed();
        this.mWalletClient.removeWalletServiceEventListener(this);
        this.mHasRegisteredListener = false;
        super.onDestroy();
    }

    private Drawable getHomeIndicatorDrawable() {
        Drawable drawable = getDrawable(R$drawable.ic_close);
        drawable.setTint(getColor(17170466));
        return drawable;
    }
}
