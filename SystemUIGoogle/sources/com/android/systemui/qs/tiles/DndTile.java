package com.android.systemui.qs.tiles;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.notification.EnableZenModeDialog;
import com.android.systemui.Prefs;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.SysUIToast;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.volume.ZenModePanel;
/* loaded from: classes.dex */
public class DndTile extends QSTileImpl<QSTile.BooleanState> {
    private final ZenModeController mController;
    private boolean mListening;
    private final SharedPreferences mSharedPreferences;
    private boolean mShowingDetail;
    private final ZenModeController.Callback mZenCallback;
    private static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    private static final Intent ZEN_PRIORITY_SETTINGS = new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    private final SharedPreferences.OnSharedPreferenceChangeListener mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: com.android.systemui.qs.tiles.DndTile.2
        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if ("DndTileCombinedIcon".equals(str) || "DndTileVisible".equals(str)) {
                DndTile.this.refreshState();
            }
        }
    };
    private final ZenModePanel.Callback mZenModePanelCallback = new ZenModePanel.Callback() { // from class: com.android.systemui.qs.tiles.DndTile.4
        @Override // com.android.systemui.volume.ZenModePanel.Callback
        public void onExpanded(boolean z) {
        }

        @Override // com.android.systemui.volume.ZenModePanel.Callback
        public void onInteraction() {
        }

        @Override // com.android.systemui.volume.ZenModePanel.Callback
        public void onPrioritySettings() {
            ((QSTileImpl) DndTile.this).mActivityStarter.postStartActivityDismissingKeyguard(DndTile.ZEN_PRIORITY_SETTINGS, 0);
        }
    };
    private final DndDetailAdapter mDetailAdapter = new DndDetailAdapter();

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowActionBarOverlay;
    }

    public DndTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ZenModeController zenModeController, SharedPreferences sharedPreferences) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        AnonymousClass3 r1 = new ZenModeController.Callback() { // from class: com.android.systemui.qs.tiles.DndTile.3
            @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
            public void onZenChanged(int i) {
                DndTile.this.refreshState(Integer.valueOf(i));
                if (DndTile.this.isShowingDetail()) {
                    DndTile.this.mDetailAdapter.updatePanel();
                }
            }

            @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
            public void onConfigChanged(ZenModeConfig zenModeConfig) {
                if (DndTile.this.isShowingDetail()) {
                    DndTile.this.mDetailAdapter.updatePanel();
                }
            }
        };
        this.mZenCallback = r1;
        this.mController = zenModeController;
        this.mSharedPreferences = sharedPreferences;
        zenModeController.observe(getLifecycle(), (Lifecycle) r1);
    }

    public static void setVisible(Context context, boolean z) {
        Prefs.putBoolean(context, "DndTileVisible", z);
    }

    public static boolean isVisible(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("DndTileVisible", false);
    }

    public static void setCombinedIcon(Context context, boolean z) {
        Prefs.putBoolean(context, "DndTileCombinedIcon", z);
    }

    public static boolean isCombinedIcon(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("DndTileCombinedIcon", false);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public DetailAdapter getDetailAdapter() {
        return this.mDetailAdapter;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return ZEN_SETTINGS;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).value) {
            this.mController.setZen(0, null, this.TAG);
        } else {
            showDetail(true);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void showDetail(boolean z) {
        int i = Settings.Secure.getInt(this.mContext.getContentResolver(), "zen_duration", 0);
        if ((Settings.Secure.getInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0) == 0 || Settings.Secure.getInt(this.mContext.getContentResolver(), "zen_settings_updated", 0) == 1) ? false : true) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 0);
            this.mController.setZen(1, null, this.TAG);
            Intent intent = new Intent("android.settings.ZEN_MODE_ONBOARDING");
            intent.addFlags(268468224);
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        } else if (i == -1) {
            this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.DndTile$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DndTile.m197$r8$lambda$C2kxSXpqaTyXmpRRwk9GvS2bDo(DndTile.this);
                }
            });
        } else if (i != 0) {
            this.mController.setZen(1, ZenModeConfig.toTimeCondition(this.mContext, i, this.mHost.getUserId(), true).id, this.TAG);
        } else {
            this.mController.setZen(1, null, this.TAG);
        }
    }

    public /* synthetic */ void lambda$showDetail$1() {
        Dialog createDialog = new EnableZenModeDialog(this.mContext).createDialog();
        createDialog.getWindow().setType(2009);
        SystemUIDialog.setShowForAllUsers(createDialog, true);
        SystemUIDialog.registerDismissListener(createDialog);
        SystemUIDialog.setWindowOnTop(createDialog);
        this.mUiHandler.post(new Runnable(createDialog) { // from class: com.android.systemui.qs.tiles.DndTile$$ExternalSyntheticLambda0
            public final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DndTile.$r8$lambda$QIsQc0WwG9DACxs3mLJhfTux5Y0(this.f$0);
            }
        });
        this.mHost.collapsePanels();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleSecondaryClick(View view) {
        if (this.mController.isVolumeRestricted()) {
            this.mHost.collapsePanels();
            Context context = this.mContext;
            SysUIToast.makeText(context, context.getString(17040141), 1).show();
        } else if (!((QSTile.BooleanState) this.mState).value) {
            this.mController.addCallback(new ZenModeController.Callback() { // from class: com.android.systemui.qs.tiles.DndTile.1
                @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
                public void onZenChanged(int i) {
                    DndTile.this.mController.removeCallback(this);
                    DndTile.this.showDetail(true);
                }
            });
            this.mController.setZen(1, null, this.TAG);
        } else {
            showDetail(true);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_dnd_label);
    }

    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        ZenModeController zenModeController = this.mController;
        if (zenModeController != null) {
            int intValue = obj instanceof Integer ? ((Integer) obj).intValue() : zenModeController.getZen();
            boolean z = intValue != 0;
            boolean z2 = booleanState.value != z;
            if (booleanState.slash == null) {
                booleanState.slash = new QSTile.SlashState();
            }
            booleanState.dualTarget = true;
            booleanState.value = z;
            booleanState.state = z ? 2 : 1;
            booleanState.slash.isSlashed = !z;
            booleanState.label = getTileLabel();
            booleanState.secondaryLabel = TextUtils.emptyIfNull(ZenModeConfig.getDescription(this.mContext, intValue != 0, this.mController.getConfig(), false));
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302819);
            checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_adjust_volume");
            if (intValue == 1) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + ((Object) booleanState.secondaryLabel);
            } else if (intValue == 2) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + this.mContext.getString(R$string.accessibility_quick_settings_dnd_none_on) + ", " + ((Object) booleanState.secondaryLabel);
            } else if (intValue != 3) {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd);
            } else {
                booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_dnd) + ", " + this.mContext.getString(R$string.accessibility_quick_settings_dnd_alarms_on) + ", " + ((Object) booleanState.secondaryLabel);
            }
            if (z2) {
                fireToggleStateChanged(booleanState.value);
            }
            booleanState.dualLabelContentDescription = this.mContext.getResources().getString(R$string.accessibility_quick_settings_open_settings, getTileLabel());
            booleanState.expandedAccessibilityClassName = Switch.class.getName();
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.BooleanState) this.mState).value) {
            return this.mContext.getString(R$string.accessibility_quick_settings_dnd_changed_on);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_dnd_changed_off);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                Prefs.registerListener(this.mContext, this.mPrefListener);
            } else {
                Prefs.unregisterListener(this.mContext, this.mPrefListener);
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return isVisible(this.mSharedPreferences);
    }

    /* loaded from: classes.dex */
    public final class DndDetailAdapter implements DetailAdapter, View.OnAttachStateChangeListener {
        private boolean mAuto;
        private ZenModePanel mZenPanel;

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public int getMetricsCategory() {
            return 149;
        }

        private DndDetailAdapter() {
            DndTile.this = r1;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public CharSequence getTitle() {
            return ((QSTileImpl) DndTile.this).mContext.getString(R$string.quick_settings_dnd_label);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Boolean getToggleState() {
            return Boolean.valueOf(((QSTile.BooleanState) ((QSTileImpl) DndTile.this).mState).value);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Intent getSettingsIntent() {
            return DndTile.ZEN_SETTINGS;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public void setToggleState(boolean z) {
            MetricsLogger.action(((QSTileImpl) DndTile.this).mContext, 166, z);
            if (!z) {
                DndTile.this.mController.setZen(0, null, ((QSTileImpl) DndTile.this).TAG);
                this.mAuto = false;
                return;
            }
            DndTile.this.mController.setZen(1, null, ((QSTileImpl) DndTile.this).TAG);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public View createDetailView(Context context, View view, ViewGroup viewGroup) {
            ZenModePanel zenModePanel;
            if (view != null) {
                zenModePanel = (ZenModePanel) view;
            } else {
                zenModePanel = (ZenModePanel) LayoutInflater.from(context).inflate(R$layout.zen_mode_panel, viewGroup, false);
            }
            this.mZenPanel = zenModePanel;
            if (view == null) {
                zenModePanel.init(DndTile.this.mController);
                this.mZenPanel.addOnAttachStateChangeListener(this);
                this.mZenPanel.setCallback(DndTile.this.mZenModePanelCallback);
                this.mZenPanel.setEmptyState(R$drawable.ic_qs_dnd_detail_empty, R$string.dnd_is_off);
            }
            updatePanel();
            return this.mZenPanel;
        }

        public void updatePanel() {
            String str;
            if (this.mZenPanel != null) {
                this.mAuto = false;
                if (DndTile.this.mController.getZen() == 0) {
                    this.mZenPanel.setState(2);
                    return;
                }
                ZenModeConfig config = DndTile.this.mController.getConfig();
                ZenModeConfig.ZenRule zenRule = config.manualRule;
                String ownerCaption = (zenRule == null || (str = zenRule.enabler) == null) ? "" : getOwnerCaption(str);
                for (ZenModeConfig.ZenRule zenRule2 : config.automaticRules.values()) {
                    if (zenRule2.isAutomaticActive()) {
                        if (ownerCaption.isEmpty()) {
                            ownerCaption = ((QSTileImpl) DndTile.this).mContext.getString(R$string.qs_dnd_prompt_auto_rule, zenRule2.name);
                        } else {
                            ownerCaption = ((QSTileImpl) DndTile.this).mContext.getString(R$string.qs_dnd_prompt_auto_rule_app);
                        }
                    }
                }
                if (ownerCaption.isEmpty()) {
                    this.mZenPanel.setState(0);
                    return;
                }
                this.mAuto = true;
                this.mZenPanel.setState(1);
                this.mZenPanel.setAutoText(ownerCaption);
            }
        }

        private String getOwnerCaption(String str) {
            CharSequence loadLabel;
            PackageManager packageManager = ((QSTileImpl) DndTile.this).mContext.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
                if (applicationInfo == null || (loadLabel = applicationInfo.loadLabel(packageManager)) == null) {
                    return "";
                }
                return ((QSTileImpl) DndTile.this).mContext.getString(R$string.qs_dnd_prompt_app, loadLabel.toString().trim());
            } catch (Throwable th) {
                Slog.w(((QSTileImpl) DndTile.this).TAG, "Error loading owner caption", th);
                return "";
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            DndTile.this.mShowingDetail = true;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            DndTile.this.mShowingDetail = false;
            this.mZenPanel = null;
        }
    }
}
