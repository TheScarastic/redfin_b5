package com.android.systemui.wallet.controller;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import android.service.quickaccesswallet.GetWalletCardsRequest;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import com.android.systemui.R$dimen;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.SystemClock;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class QuickAccessWalletController {
    private static final long RECREATION_TIME_WINDOW = TimeUnit.MINUTES.toMillis(10);
    private final SystemClock mClock;
    private final Context mContext;
    private ContentObserver mDefaultPaymentAppObserver;
    private final Executor mExecutor;
    private long mQawClientCreatedTimeMillis;
    private QuickAccessWalletClient mQuickAccessWalletClient;
    private final SecureSettings mSecureSettings;
    private ContentObserver mWalletPreferenceObserver;
    private int mWalletPreferenceChangeEvents = 0;
    private int mDefaultPaymentAppChangeEvents = 0;
    private boolean mWalletEnabled = false;

    /* loaded from: classes2.dex */
    public enum WalletChangeEvent {
        DEFAULT_PAYMENT_APP_CHANGE,
        WALLET_PREFERENCE_CHANGE
    }

    public QuickAccessWalletController(Context context, Executor executor, SecureSettings secureSettings, QuickAccessWalletClient quickAccessWalletClient, SystemClock systemClock) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mQuickAccessWalletClient = quickAccessWalletClient;
        this.mClock = systemClock;
        this.mQawClientCreatedTimeMillis = systemClock.elapsedRealtime();
    }

    public boolean isWalletEnabled() {
        return this.mWalletEnabled;
    }

    public QuickAccessWalletClient getWalletClient() {
        return this.mQuickAccessWalletClient;
    }

    public void setupWalletChangeObservers(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback, WalletChangeEvent... walletChangeEventArr) {
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE) {
                setupWalletPreferenceObserver();
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE) {
                setupDefaultPaymentAppObserver(onWalletCardsRetrievedCallback);
            }
        }
    }

    public void unregisterWalletChangeObservers(WalletChangeEvent... walletChangeEventArr) {
        ContentObserver contentObserver;
        ContentObserver contentObserver2;
        for (WalletChangeEvent walletChangeEvent : walletChangeEventArr) {
            if (walletChangeEvent == WalletChangeEvent.WALLET_PREFERENCE_CHANGE && (contentObserver2 = this.mWalletPreferenceObserver) != null) {
                int i = this.mWalletPreferenceChangeEvents - 1;
                this.mWalletPreferenceChangeEvents = i;
                if (i == 0) {
                    this.mSecureSettings.unregisterContentObserver(contentObserver2);
                }
            } else if (walletChangeEvent == WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE && (contentObserver = this.mDefaultPaymentAppObserver) != null) {
                int i2 = this.mDefaultPaymentAppChangeEvents - 1;
                this.mDefaultPaymentAppChangeEvents = i2;
                if (i2 == 0) {
                    this.mSecureSettings.unregisterContentObserver(contentObserver);
                }
            }
        }
    }

    public void updateWalletPreference() {
        this.mWalletEnabled = this.mQuickAccessWalletClient.isWalletServiceAvailable() && this.mQuickAccessWalletClient.isWalletFeatureAvailable() && this.mQuickAccessWalletClient.isWalletFeatureAvailableWhenDeviceLocked();
    }

    public void queryWalletCards(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        if (this.mClock.elapsedRealtime() - this.mQawClientCreatedTimeMillis > RECREATION_TIME_WINDOW) {
            Log.i("QAWController", "Re-creating the QAW client to avoid stale.");
            reCreateWalletClient();
        }
        if (!this.mQuickAccessWalletClient.isWalletFeatureAvailable()) {
            Log.d("QAWController", "QuickAccessWallet feature is not available.");
            return;
        }
        this.mQuickAccessWalletClient.getWalletCards(this.mExecutor, new GetWalletCardsRequest(this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_tile_card_view_width), this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_tile_card_view_height), this.mContext.getResources().getDimensionPixelSize(R$dimen.wallet_icon_size), 1), onWalletCardsRetrievedCallback);
    }

    public void reCreateWalletClient() {
        this.mQuickAccessWalletClient = QuickAccessWalletClient.create(this.mContext);
        this.mQawClientCreatedTimeMillis = this.mClock.elapsedRealtime();
    }

    private void setupDefaultPaymentAppObserver(final QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback) {
        if (this.mDefaultPaymentAppObserver == null) {
            this.mDefaultPaymentAppObserver = new ContentObserver(null) { // from class: com.android.systemui.wallet.controller.QuickAccessWalletController.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    QuickAccessWalletController.this.mExecutor.execute(new QuickAccessWalletController$1$$ExternalSyntheticLambda0(this, onWalletCardsRetrievedCallback));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onChange$0(QuickAccessWalletClient.OnWalletCardsRetrievedCallback onWalletCardsRetrievedCallback2) {
                    QuickAccessWalletController.this.reCreateWalletClient();
                    QuickAccessWalletController.this.updateWalletPreference();
                    QuickAccessWalletController.this.queryWalletCards(onWalletCardsRetrievedCallback2);
                }
            };
            this.mSecureSettings.registerContentObserver(Settings.Secure.getUriFor("nfc_payment_default_component"), false, this.mDefaultPaymentAppObserver);
        }
        this.mDefaultPaymentAppChangeEvents++;
    }

    private void setupWalletPreferenceObserver() {
        if (this.mWalletPreferenceObserver == null) {
            this.mWalletPreferenceObserver = new ContentObserver(null) { // from class: com.android.systemui.wallet.controller.QuickAccessWalletController.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    QuickAccessWalletController.this.mExecutor.execute(new QuickAccessWalletController$2$$ExternalSyntheticLambda0(this));
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$onChange$0() {
                    QuickAccessWalletController.this.updateWalletPreference();
                }
            };
            this.mSecureSettings.registerContentObserver(Settings.Secure.getUriFor("lockscreen_show_wallet"), false, this.mWalletPreferenceObserver);
        }
        this.mWalletPreferenceChangeEvents++;
    }
}
