package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.service.quickaccesswallet.WalletCard;
import android.util.Log;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.ui.WalletActivity;
import java.util.List;
/* loaded from: classes.dex */
public class QuickAccessWalletTile extends QSTileImpl<QSTile.State> {
    @VisibleForTesting
    Drawable mCardViewDrawable;
    private final QuickAccessWalletController mController;
    private final KeyguardStateController mKeyguardStateController;
    private final PackageManager mPackageManager;
    private final SecureSettings mSecureSettings;
    private WalletCard mSelectedCard;
    private final CharSequence mLabel = this.mContext.getString(R$string.wallet_title);
    private final WalletCardRetriever mCardRetriever = new WalletCardRetriever();
    private boolean mIsWalletUpdating = true;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public QuickAccessWalletTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, KeyguardStateController keyguardStateController, PackageManager packageManager, SecureSettings secureSettings, QuickAccessWalletController quickAccessWalletController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mController = quickAccessWalletController;
        this.mKeyguardStateController = keyguardStateController;
        this.mPackageManager = packageManager;
        this.mSecureSettings = secureSettings;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (z) {
            this.mController.setupWalletChangeObservers(this.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
            if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
                Log.i("QuickAccessWalletTile", "QAW service is unavailable, recreating the wallet client.");
                this.mController.reCreateWalletClient();
            }
            this.mController.queryWalletCards(this.mCardRetriever);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mUiHandler.post(new Runnable(controller) { // from class: com.android.systemui.qs.tiles.QuickAccessWalletTile$$ExternalSyntheticLambda0
            public final /* synthetic */ ActivityLaunchAnimator.Controller f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                QuickAccessWalletTile.$r8$lambda$EuQEGzcUPDUHonbXRkk2TfbRaew(QuickAccessWalletTile.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$handleClick$0(ActivityLaunchAnimator.Controller controller) {
        if (this.mSelectedCard != null) {
            Intent addFlags = new Intent(this.mContext, WalletActivity.class).setAction("android.intent.action.VIEW").addFlags(335544320);
            if (this.mKeyguardStateController.isUnlocked()) {
                this.mActivityStarter.startActivity(addFlags, true, controller);
                return;
            }
            this.mHost.collapsePanels();
            this.mContext.startActivity(addFlags);
        } else if (this.mController.getWalletClient().createWalletIntent() == null) {
            Log.w("QuickAccessWalletTile", "Could not get intent of the wallet app.");
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(this.mController.getWalletClient().createWalletIntent(), 0, controller);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(QSTile.State state, Object obj) {
        QSTile.Icon icon;
        int i;
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        if (serviceLabel == null) {
            serviceLabel = this.mLabel;
        }
        state.label = serviceLabel;
        state.contentDescription = serviceLabel;
        Drawable tileIcon = this.mController.getWalletClient().getTileIcon();
        if (tileIcon == null) {
            icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_wallet_lockscreen);
        } else {
            icon = new QSTileImpl.DrawableIcon(tileIcon);
        }
        state.icon = icon;
        boolean z = !this.mKeyguardStateController.isUnlocked();
        if (!this.mController.getWalletClient().isWalletServiceAvailable() || !this.mController.getWalletClient().isWalletFeatureAvailable()) {
            state.state = 0;
            state.secondaryLabel = null;
            state.sideViewCustomDrawable = null;
            return;
        }
        WalletCard walletCard = this.mSelectedCard;
        if (walletCard == null) {
            state.state = 1;
            Context context = this.mContext;
            if (this.mIsWalletUpdating) {
                i = R$string.wallet_secondary_label_updating;
            } else {
                i = R$string.wallet_secondary_label_no_card;
            }
            state.secondaryLabel = context.getString(i);
            state.sideViewCustomDrawable = null;
        } else if (z) {
            state.state = 1;
            state.secondaryLabel = this.mContext.getString(R$string.wallet_secondary_label_device_locked);
            state.sideViewCustomDrawable = null;
        } else {
            state.state = 2;
            state.secondaryLabel = walletCard.getContentDescription();
            state.sideViewCustomDrawable = this.mCardViewDrawable;
        }
        state.stateDescription = state.secondaryLabel;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mPackageManager.hasSystemFeature("android.hardware.nfc.hce") && !this.mPackageManager.hasSystemFeature("org.chromium.arc") && this.mSecureSettings.getString("nfc_payment_default_component") != null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        CharSequence serviceLabel = this.mController.getWalletClient().getServiceLabel();
        return serviceLabel == null ? this.mLabel : serviceLabel;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleDestroy() {
        super.handleDestroy();
        this.mController.unregisterWalletChangeObservers(QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        private WalletCardRetriever() {
            QuickAccessWalletTile.this = r1;
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            Log.i("QuickAccessWalletTile", "Successfully retrieved wallet cards.");
            QuickAccessWalletTile.this.mIsWalletUpdating = false;
            List<WalletCard> walletCards = getWalletCardsResponse.getWalletCards();
            if (walletCards.isEmpty()) {
                Log.d("QuickAccessWalletTile", "No wallet cards exist.");
                QuickAccessWalletTile quickAccessWalletTile = QuickAccessWalletTile.this;
                quickAccessWalletTile.mCardViewDrawable = null;
                quickAccessWalletTile.mSelectedCard = null;
                QuickAccessWalletTile.this.refreshState();
                return;
            }
            int selectedIndex = getWalletCardsResponse.getSelectedIndex();
            if (selectedIndex >= walletCards.size()) {
                Log.w("QuickAccessWalletTile", "Error retrieving cards: Invalid selected card index.");
                QuickAccessWalletTile.this.mSelectedCard = null;
                QuickAccessWalletTile.this.mCardViewDrawable = null;
                return;
            }
            QuickAccessWalletTile.this.mSelectedCard = walletCards.get(selectedIndex);
            QuickAccessWalletTile quickAccessWalletTile2 = QuickAccessWalletTile.this;
            quickAccessWalletTile2.mCardViewDrawable = quickAccessWalletTile2.mSelectedCard.getCardImage().loadDrawable(((QSTileImpl) QuickAccessWalletTile.this).mContext);
            QuickAccessWalletTile.this.refreshState();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            QuickAccessWalletTile.this.mIsWalletUpdating = false;
            QuickAccessWalletTile quickAccessWalletTile = QuickAccessWalletTile.this;
            quickAccessWalletTile.mCardViewDrawable = null;
            quickAccessWalletTile.mSelectedCard = null;
            QuickAccessWalletTile.this.refreshState();
        }
    }
}
