package com.android.systemui.qs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.SettingsButton;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class QSFooterViewController extends ViewController<QSFooterView> implements QSFooter {
    private final ActivityStarter mActivityStarter;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private boolean mExpanded;
    private final FalsingManager mFalsingManager;
    private final GlobalActionsDialogLite mGlobalActionsDialog;
    private boolean mListening;
    private final MetricsLogger mMetricsLogger;
    private final MultiUserSwitchController mMultiUserSwitchController;
    private final QSPanelController mQsPanelController;
    private final QuickQSPanelController mQuickQSPanelController;
    private final boolean mShowPMLiteButton;
    private final TunerService mTunerService;
    private final UiEventLogger mUiEventLogger;
    private final UserInfoController mUserInfoController;
    private final UserManager mUserManager;
    private final UserTracker mUserTracker;
    private final UserInfoController.OnUserInfoChangedListener mOnUserInfoChangedListener = new UserInfoController.OnUserInfoChangedListener() { // from class: com.android.systemui.qs.QSFooterViewController.1
        @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
        public void onUserInfoChanged(String str, Drawable drawable, String str2) {
            ((QSFooterView) ((ViewController) QSFooterViewController.this).mView).onUserInfoChanged(drawable, QSFooterViewController.this.mUserManager.isGuestUser(KeyguardUpdateMonitor.getCurrentUser()));
        }
    };
    private final View.OnClickListener mSettingsOnClickListener = new View.OnClickListener() { // from class: com.android.systemui.qs.QSFooterViewController.2
        public static /* synthetic */ void lambda$onClick$0() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (QSFooterViewController.this.mExpanded && !QSFooterViewController.this.mFalsingManager.isFalseTap(1)) {
                if (view == QSFooterViewController.this.mSettingsButton) {
                    if (!QSFooterViewController.this.mDeviceProvisionedController.isCurrentUserSetup()) {
                        QSFooterViewController.this.mActivityStarter.postQSRunnableDismissingKeyguard(QSFooterViewController$2$$ExternalSyntheticLambda2.INSTANCE);
                        return;
                    }
                    QSFooterViewController.this.mMetricsLogger.action(QSFooterViewController.this.mExpanded ? 406 : 490);
                    if (QSFooterViewController.this.mSettingsButton.isTunerClick()) {
                        QSFooterViewController.this.mActivityStarter.postQSRunnableDismissingKeyguard(new QSFooterViewController$2$$ExternalSyntheticLambda0(this));
                    } else {
                        QSFooterViewController.this.startSettingsActivity();
                    }
                } else if (view == QSFooterViewController.this.mPowerMenuLite) {
                    QSFooterViewController.this.mUiEventLogger.log(GlobalActionsDialogLite.GlobalActionsEvent.GA_OPEN_QS);
                    QSFooterViewController.this.mGlobalActionsDialog.showOrHideDialog(false, true);
                }
            }
        }

        public /* synthetic */ void lambda$onClick$2() {
            if (QSFooterViewController.this.isTunerEnabled()) {
                QSFooterViewController.this.mTunerService.showResetRequest(new QSFooterViewController$2$$ExternalSyntheticLambda1(this));
            } else {
                Toast.makeText(QSFooterViewController.this.getContext(), R$string.tuner_toast, 1).show();
                QSFooterViewController.this.mTunerService.setTunerEnabled(true);
            }
            QSFooterViewController.this.startSettingsActivity();
        }

        public /* synthetic */ void lambda$onClick$1() {
            QSFooterViewController.this.startSettingsActivity();
        }
    };
    private final SettingsButton mSettingsButton = (SettingsButton) ((QSFooterView) this.mView).findViewById(R$id.settings_button);
    private final View mSettingsButtonContainer = ((QSFooterView) this.mView).findViewById(R$id.settings_button_container);
    private final TextView mBuildText = (TextView) ((QSFooterView) this.mView).findViewById(R$id.build);
    private final View mEdit = ((QSFooterView) this.mView).findViewById(16908291);
    private final PageIndicator mPageIndicator = (PageIndicator) ((QSFooterView) this.mView).findViewById(R$id.footer_page_indicator);
    private final View mPowerMenuLite = ((QSFooterView) this.mView).findViewById(R$id.pm_lite);

    public QSFooterViewController(QSFooterView qSFooterView, UserManager userManager, UserInfoController userInfoController, ActivityStarter activityStarter, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, QSPanelController qSPanelController, MultiUserSwitchController multiUserSwitchController, QuickQSPanelController quickQSPanelController, TunerService tunerService, MetricsLogger metricsLogger, FalsingManager falsingManager, boolean z, GlobalActionsDialogLite globalActionsDialogLite, UiEventLogger uiEventLogger) {
        super(qSFooterView);
        this.mUserManager = userManager;
        this.mUserInfoController = userInfoController;
        this.mActivityStarter = activityStarter;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mUserTracker = userTracker;
        this.mQsPanelController = qSPanelController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mTunerService = tunerService;
        this.mMetricsLogger = metricsLogger;
        this.mFalsingManager = falsingManager;
        this.mMultiUserSwitchController = multiUserSwitchController;
        this.mShowPMLiteButton = z;
        this.mGlobalActionsDialog = globalActionsDialogLite;
        this.mUiEventLogger = uiEventLogger;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mMultiUserSwitchController.init();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        if (this.mShowPMLiteButton) {
            this.mPowerMenuLite.setVisibility(0);
            this.mPowerMenuLite.setOnClickListener(this.mSettingsOnClickListener);
        } else {
            this.mPowerMenuLite.setVisibility(8);
        }
        ((QSFooterView) this.mView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                QSFooterViewController.this.lambda$onViewAttached$0(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.mSettingsButton.setOnClickListener(this.mSettingsOnClickListener);
        this.mBuildText.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return QSFooterViewController.this.lambda$onViewAttached$1(view);
            }
        });
        this.mEdit.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                QSFooterViewController.$r8$lambda$WTPr0DEA84vT7wWIdmTFKQRtb34(QSFooterViewController.this, view);
            }
        });
        this.mQsPanelController.setFooterPageIndicator(this.mPageIndicator);
        ((QSFooterView) this.mView).updateEverything(isTunerEnabled(), this.mMultiUserSwitchController.isMultiUserEnabled());
    }

    public /* synthetic */ void lambda$onViewAttached$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        ((QSFooterView) this.mView).updateAnimator(i3 - i, this.mQuickQSPanelController.getNumQuickTiles());
    }

    public /* synthetic */ boolean lambda$onViewAttached$1(View view) {
        CharSequence text = this.mBuildText.getText();
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        ((ClipboardManager) this.mUserTracker.getUserContext().getSystemService(ClipboardManager.class)).setPrimaryClip(ClipData.newPlainText(getResources().getString(R$string.build_number_clip_data_label), text));
        Toast.makeText(getContext(), R$string.build_number_copy_toast, 0).show();
        return true;
    }

    public /* synthetic */ void lambda$onViewAttached$3(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mActivityStarter.postQSRunnableDismissingKeyguard(new Runnable(view) { // from class: com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda3
                public final /* synthetic */ View f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    QSFooterViewController.this.lambda$onViewAttached$2(this.f$1);
                }
            });
        }
    }

    public /* synthetic */ void lambda$onViewAttached$2(View view) {
        this.mQsPanelController.showEdit(view);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        setListening(false);
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setVisibility(int i) {
        ((QSFooterView) this.mView).setVisibility(i);
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setExpanded(boolean z) {
        this.mExpanded = z;
        ((QSFooterView) this.mView).setExpanded(z, isTunerEnabled(), this.mMultiUserSwitchController.isMultiUserEnabled());
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setExpansion(float f) {
        ((QSFooterView) this.mView).setExpansion(f);
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                this.mUserInfoController.addCallback(this.mOnUserInfoChangedListener);
            } else {
                this.mUserInfoController.removeCallback(this.mOnUserInfoChangedListener);
            }
        }
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setKeyguardShowing(boolean z) {
        ((QSFooterView) this.mView).setKeyguardShowing();
    }

    @Override // com.android.systemui.qs.QSFooter
    public void setExpandClickListener(View.OnClickListener onClickListener) {
        ((QSFooterView) this.mView).setExpandClickListener(onClickListener);
    }

    @Override // com.android.systemui.qs.QSFooter
    public void disable(int i, int i2, boolean z) {
        ((QSFooterView) this.mView).disable(i2, isTunerEnabled(), this.mMultiUserSwitchController.isMultiUserEnabled());
    }

    public void startSettingsActivity() {
        View view = this.mSettingsButtonContainer;
        this.mActivityStarter.startActivity(new Intent("android.settings.SETTINGS"), true, view != null ? ActivityLaunchAnimator.Controller.fromView(view, 33) : null);
    }

    public boolean isTunerEnabled() {
        return this.mTunerService.isTunerEnabled();
    }
}
