package com.android.systemui.qs;

import android.app.AlertDialog;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyEventLogger;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.SecurityController;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class QSSecurityFooter implements View.OnClickListener, DialogInterface.OnClickListener {
    protected static final boolean DEBUG = Log.isLoggable("QSSecurityFooter", 3);
    private final ActivityStarter mActivityStarter;
    private final Context mContext;
    private AlertDialog mDialog;
    private final TextView mFooterText;
    protected H mHandler;
    private QSTileHost mHost;
    private boolean mIsVisible;
    private final Handler mMainHandler;
    private final ImageView mPrimaryFooterIcon;
    private Drawable mPrimaryFooterIconDrawable;
    private final View mRootView;
    private final SecurityController mSecurityController;
    private final UserTracker mUserTracker;
    private final Callback mCallback = new Callback();
    private CharSequence mFooterTextContent = null;
    private final Runnable mUpdatePrimaryIcon = new Runnable() { // from class: com.android.systemui.qs.QSSecurityFooter.1
        @Override // java.lang.Runnable
        public void run() {
            if (QSSecurityFooter.this.mPrimaryFooterIconDrawable != null) {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageDrawable(QSSecurityFooter.this.mPrimaryFooterIconDrawable);
            } else {
                QSSecurityFooter.this.mPrimaryFooterIcon.setImageResource(QSSecurityFooter.this.mFooterIconId);
            }
        }
    };
    private final Runnable mUpdateDisplayState = new Runnable() { // from class: com.android.systemui.qs.QSSecurityFooter.2
        @Override // java.lang.Runnable
        public void run() {
            if (QSSecurityFooter.this.mFooterTextContent != null) {
                QSSecurityFooter.this.mFooterText.setText(QSSecurityFooter.this.mFooterTextContent);
            }
            QSSecurityFooter.this.mRootView.setVisibility(!QSSecurityFooter.this.mIsVisible ? 8 : 0);
        }
    };
    private int mFooterIconId = R$drawable.ic_info_outline;

    /* access modifiers changed from: package-private */
    public QSSecurityFooter(View view, UserTracker userTracker, Handler handler, ActivityStarter activityStarter, SecurityController securityController, Looper looper) {
        this.mRootView = view;
        view.setOnClickListener(this);
        this.mFooterText = (TextView) view.findViewById(R$id.footer_text);
        this.mPrimaryFooterIcon = (ImageView) view.findViewById(R$id.primary_footer_icon);
        this.mContext = view.getContext();
        this.mMainHandler = handler;
        this.mActivityStarter = activityStarter;
        this.mSecurityController = securityController;
        this.mHandler = new H(looper);
        this.mUserTracker = userTracker;
    }

    public void setHostEnvironment(QSTileHost qSTileHost) {
        this.mHost = qSTileHost;
    }

    public void setListening(boolean z) {
        if (z) {
            this.mSecurityController.addCallback(this.mCallback);
            refreshState();
            return;
        }
        this.mSecurityController.removeCallback(this.mCallback);
    }

    public void onConfigurationChanged() {
        FontSizeUtils.updateFontSize(this.mFooterText, R$dimen.qs_tile_text_size);
        Resources resources = this.mContext.getResources();
        this.mFooterText.setMaxLines(resources.getInteger(R$integer.qs_security_footer_maxLines));
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.qs_footer_padding);
        this.mRootView.setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.qs_footers_margin_bottom);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mRootView.getLayoutParams();
        marginLayoutParams.bottomMargin = dimensionPixelSize2;
        marginLayoutParams.width = resources.getConfiguration().orientation == 1 ? -1 : -2;
        this.mRootView.setLayoutParams(marginLayoutParams);
        this.mRootView.setBackground(this.mContext.getDrawable(R$drawable.qs_security_footer_background));
    }

    public View getView() {
        return this.mRootView;
    }

    public boolean hasFooter() {
        return this.mRootView.getVisibility() != 8;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (hasFooter()) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleClick() {
        showDeviceMonitoringDialog();
        DevicePolicyEventLogger.createEvent(57).write();
    }

    public void showDeviceMonitoringDialog() {
        createDialog();
    }

    public void refreshState() {
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void handleRefreshState() {
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        boolean z = UserManager.isDeviceInDemoMode(this.mContext) && userInfo != null && userInfo.isDemo();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        CharSequence workProfileOrganizationName = this.mSecurityController.getWorkProfileOrganizationName();
        boolean isProfileOwnerOfOrganizationOwnedDevice = this.mSecurityController.isProfileOwnerOfOrganizationOwnedDevice();
        boolean isParentalControlsEnabled = this.mSecurityController.isParentalControlsEnabled();
        boolean isWorkProfileOn = this.mSecurityController.isWorkProfileOn();
        boolean z2 = hasCACertInWorkProfile || workProfileVpnName != null || (hasWorkProfile && isNetworkLoggingEnabled);
        boolean z3 = (isDeviceManaged && !z) || hasCACertInCurrentUser || primaryVpnName != null || isProfileOwnerOfOrganizationOwnedDevice || isParentalControlsEnabled || (z2 && isWorkProfileOn);
        this.mIsVisible = z3;
        if (!z3 || !isProfileOwnerOfOrganizationOwnedDevice || (z2 && isWorkProfileOn)) {
            this.mRootView.setClickable(true);
            this.mRootView.findViewById(R$id.footer_icon).setVisibility(0);
        } else {
            this.mRootView.setClickable(false);
            this.mRootView.findViewById(R$id.footer_icon).setVisibility(8);
        }
        this.mFooterTextContent = getFooterText(isDeviceManaged, hasWorkProfile, hasCACertInCurrentUser, hasCACertInWorkProfile, isNetworkLoggingEnabled, primaryVpnName, workProfileVpnName, deviceOwnerOrganizationName, workProfileOrganizationName, isProfileOwnerOfOrganizationOwnedDevice, isParentalControlsEnabled, isWorkProfileOn);
        int i = R$drawable.ic_info_outline;
        if (!(primaryVpnName == null && workProfileVpnName == null)) {
            if (this.mSecurityController.isVpnBranded()) {
                i = R$drawable.stat_sys_branded_vpn;
            } else {
                i = R$drawable.stat_sys_vpn_ic;
            }
        }
        if (this.mFooterIconId != i) {
            this.mFooterIconId = i;
        }
        if (!isParentalControlsEnabled) {
            this.mPrimaryFooterIconDrawable = null;
        } else if (this.mPrimaryFooterIconDrawable == null) {
            this.mPrimaryFooterIconDrawable = this.mSecurityController.getIcon(this.mSecurityController.getDeviceAdminInfo());
        }
        this.mMainHandler.post(this.mUpdatePrimaryIcon);
        this.mMainHandler.post(this.mUpdateDisplayState);
    }

    protected CharSequence getFooterText(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String str, String str2, CharSequence charSequence, CharSequence charSequence2, boolean z6, boolean z7, boolean z8) {
        if (z7) {
            return this.mContext.getString(R$string.quick_settings_disclosure_parental_controls);
        }
        if (!z) {
            if (!z4 || !z8) {
                if (z3) {
                    return this.mContext.getString(R$string.quick_settings_disclosure_monitoring);
                }
                if (str != null && str2 != null) {
                    return this.mContext.getString(R$string.quick_settings_disclosure_vpns);
                }
                if (str2 != null && z8) {
                    return this.mContext.getString(R$string.quick_settings_disclosure_managed_profile_named_vpn, str2);
                }
                if (str != null) {
                    if (z2) {
                        return this.mContext.getString(R$string.quick_settings_disclosure_personal_profile_named_vpn, str);
                    }
                    return this.mContext.getString(R$string.quick_settings_disclosure_named_vpn, str);
                } else if (z2 && z5 && z8) {
                    return this.mContext.getString(R$string.quick_settings_disclosure_managed_profile_network_activity);
                } else {
                    if (!z6) {
                        return null;
                    }
                    if (charSequence2 == null) {
                        return this.mContext.getString(R$string.quick_settings_disclosure_management);
                    }
                    return this.mContext.getString(R$string.quick_settings_disclosure_named_management, charSequence2);
                }
            } else if (charSequence2 == null) {
                return this.mContext.getString(R$string.quick_settings_disclosure_managed_profile_monitoring);
            } else {
                return this.mContext.getString(R$string.quick_settings_disclosure_named_managed_profile_monitoring, charSequence2);
            }
        } else if (z3 || z4 || z5) {
            if (charSequence == null) {
                return this.mContext.getString(R$string.quick_settings_disclosure_management_monitoring);
            }
            return this.mContext.getString(R$string.quick_settings_disclosure_named_management_monitoring, charSequence);
        } else if (str == null || str2 == null) {
            if (str == null && str2 == null) {
                if (charSequence == null) {
                    return this.mContext.getString(R$string.quick_settings_disclosure_management);
                }
                if (isFinancedDevice()) {
                    return this.mContext.getString(R$string.quick_settings_financed_disclosure_named_management, charSequence);
                }
                return this.mContext.getString(R$string.quick_settings_disclosure_named_management, charSequence);
            } else if (charSequence == null) {
                Context context = this.mContext;
                int i = R$string.quick_settings_disclosure_management_named_vpn;
                Object[] objArr = new Object[1];
                if (str == null) {
                    str = str2;
                }
                objArr[0] = str;
                return context.getString(i, objArr);
            } else {
                Context context2 = this.mContext;
                int i2 = R$string.quick_settings_disclosure_named_management_named_vpn;
                Object[] objArr2 = new Object[2];
                objArr2[0] = charSequence;
                if (str == null) {
                    str = str2;
                }
                objArr2[1] = str;
                return context2.getString(i2, objArr2);
            }
        } else if (charSequence == null) {
            return this.mContext.getString(R$string.quick_settings_disclosure_management_vpns);
        } else {
            return this.mContext.getString(R$string.quick_settings_disclosure_named_management_vpns, charSequence);
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            Intent intent = new Intent("android.settings.ENTERPRISE_PRIVACY_SETTINGS");
            this.mDialog.dismiss();
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }
    }

    private void createDialog() {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext, 0);
        this.mDialog = systemUIDialog;
        systemUIDialog.requestWindowFeature(1);
        this.mDialog.setButton(-1, getPositiveButton(), this);
        this.mDialog.setButton(-2, getNegativeButton(), this);
        this.mDialog.setView(createDialogView());
        this.mDialog.show();
        this.mDialog.getWindow().setLayout(-1, -2);
    }

    View createDialogView() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return createParentalControlsDialogView();
        }
        return createOrganizationDialogView();
    }

    private View createOrganizationDialogView() {
        boolean isDeviceManaged = this.mSecurityController.isDeviceManaged();
        boolean hasWorkProfile = this.mSecurityController.hasWorkProfile();
        CharSequence deviceOwnerOrganizationName = this.mSecurityController.getDeviceOwnerOrganizationName();
        boolean hasCACertInCurrentUser = this.mSecurityController.hasCACertInCurrentUser();
        boolean hasCACertInWorkProfile = this.mSecurityController.hasCACertInWorkProfile();
        boolean isNetworkLoggingEnabled = this.mSecurityController.isNetworkLoggingEnabled();
        String primaryVpnName = this.mSecurityController.getPrimaryVpnName();
        String workProfileVpnName = this.mSecurityController.getWorkProfileVpnName();
        boolean z = false;
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.quick_settings_footer_dialog, (ViewGroup) null, false);
        ((TextView) inflate.findViewById(R$id.device_management_subtitle)).setText(getManagementTitle(deviceOwnerOrganizationName));
        CharSequence managementMessage = getManagementMessage(isDeviceManaged, deviceOwnerOrganizationName);
        if (managementMessage == null) {
            inflate.findViewById(R$id.device_management_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.device_management_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(R$id.device_management_warning)).setText(managementMessage);
            this.mDialog.setButton(-2, getSettingsButton(), this);
        }
        CharSequence caCertsMessage = getCaCertsMessage(isDeviceManaged, hasCACertInCurrentUser, hasCACertInWorkProfile);
        if (caCertsMessage == null) {
            inflate.findViewById(R$id.ca_certs_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.ca_certs_disclosures).setVisibility(0);
            TextView textView = (TextView) inflate.findViewById(R$id.ca_certs_warning);
            textView.setText(caCertsMessage);
            textView.setMovementMethod(new LinkMovementMethod());
        }
        CharSequence networkLoggingMessage = getNetworkLoggingMessage(isDeviceManaged, isNetworkLoggingEnabled);
        if (networkLoggingMessage == null) {
            inflate.findViewById(R$id.network_logging_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.network_logging_disclosures).setVisibility(0);
            ((TextView) inflate.findViewById(R$id.network_logging_warning)).setText(networkLoggingMessage);
        }
        CharSequence vpnMessage = getVpnMessage(isDeviceManaged, hasWorkProfile, primaryVpnName, workProfileVpnName);
        if (vpnMessage == null) {
            inflate.findViewById(R$id.vpn_disclosures).setVisibility(8);
        } else {
            inflate.findViewById(R$id.vpn_disclosures).setVisibility(0);
            TextView textView2 = (TextView) inflate.findViewById(R$id.vpn_warning);
            textView2.setText(vpnMessage);
            textView2.setMovementMethod(new LinkMovementMethod());
        }
        boolean z2 = managementMessage != null;
        boolean z3 = caCertsMessage != null;
        boolean z4 = networkLoggingMessage != null;
        if (vpnMessage != null) {
            z = true;
        }
        configSubtitleVisibility(z2, z3, z4, z, inflate);
        return inflate;
    }

    private View createParentalControlsDialogView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.quick_settings_footer_dialog_parental_controls, (ViewGroup) null, false);
        DeviceAdminInfo deviceAdminInfo = this.mSecurityController.getDeviceAdminInfo();
        Drawable icon = this.mSecurityController.getIcon(deviceAdminInfo);
        if (icon != null) {
            ((ImageView) inflate.findViewById(R$id.parental_controls_icon)).setImageDrawable(icon);
        }
        ((TextView) inflate.findViewById(R$id.parental_controls_title)).setText(this.mSecurityController.getLabel(deviceAdminInfo));
        return inflate;
    }

    protected void configSubtitleVisibility(boolean z, boolean z2, boolean z3, boolean z4, View view) {
        if (!z) {
            int i = z3 ? (z2 ? 1 : 0) + 1 : z2 ? 1 : 0;
            if (z4) {
                i++;
            }
            if (i == 1) {
                if (z2) {
                    view.findViewById(R$id.ca_certs_subtitle).setVisibility(8);
                }
                if (z3) {
                    view.findViewById(R$id.network_logging_subtitle).setVisibility(8);
                }
                if (z4) {
                    view.findViewById(R$id.vpn_subtitle).setVisibility(8);
                }
            }
        }
    }

    String getSettingsButton() {
        return this.mContext.getString(R$string.monitoring_button_view_policies);
    }

    private String getPositiveButton() {
        return this.mContext.getString(R$string.ok);
    }

    private String getNegativeButton() {
        if (this.mSecurityController.isParentalControlsEnabled()) {
            return this.mContext.getString(R$string.monitoring_button_view_controls);
        }
        return null;
    }

    protected CharSequence getManagementMessage(boolean z, CharSequence charSequence) {
        if (!z) {
            return null;
        }
        if (charSequence == null) {
            return this.mContext.getString(R$string.monitoring_description_management);
        }
        if (isFinancedDevice()) {
            return this.mContext.getString(R$string.monitoring_financed_description_named_management, charSequence, charSequence);
        }
        return this.mContext.getString(R$string.monitoring_description_named_management, charSequence);
    }

    protected CharSequence getCaCertsMessage(boolean z, boolean z2, boolean z3) {
        if (!z2 && !z3) {
            return null;
        }
        if (z) {
            return this.mContext.getString(R$string.monitoring_description_management_ca_certificate);
        }
        if (z3) {
            return this.mContext.getString(R$string.monitoring_description_managed_profile_ca_certificate);
        }
        return this.mContext.getString(R$string.monitoring_description_ca_certificate);
    }

    protected CharSequence getNetworkLoggingMessage(boolean z, boolean z2) {
        if (!z2) {
            return null;
        }
        if (z) {
            return this.mContext.getString(R$string.monitoring_description_management_network_logging);
        }
        return this.mContext.getString(R$string.monitoring_description_managed_profile_network_logging);
    }

    protected CharSequence getVpnMessage(boolean z, boolean z2, String str, String str2) {
        if (str == null && str2 == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (z) {
            if (str == null || str2 == null) {
                Context context = this.mContext;
                int i = R$string.monitoring_description_named_vpn;
                Object[] objArr = new Object[1];
                if (str == null) {
                    str = str2;
                }
                objArr[0] = str;
                spannableStringBuilder.append((CharSequence) context.getString(i, objArr));
            } else {
                spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_two_named_vpns, str, str2));
            }
        } else if (str != null && str2 != null) {
            spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_two_named_vpns, str, str2));
        } else if (str2 != null) {
            spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_managed_profile_named_vpn, str2));
        } else if (z2) {
            spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_personal_profile_named_vpn, str));
        } else {
            spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_named_vpn, str));
        }
        spannableStringBuilder.append((CharSequence) this.mContext.getString(R$string.monitoring_description_vpn_settings_separator));
        spannableStringBuilder.append(this.mContext.getString(R$string.monitoring_description_vpn_settings), new VpnSpan(), 0);
        return spannableStringBuilder;
    }

    CharSequence getManagementTitle(CharSequence charSequence) {
        if (charSequence == null || !isFinancedDevice()) {
            return this.mContext.getString(R$string.monitoring_title_device_owned);
        }
        return this.mContext.getString(R$string.monitoring_title_financed_device, charSequence);
    }

    private boolean isFinancedDevice() {
        if (this.mSecurityController.isDeviceManaged()) {
            SecurityController securityController = this.mSecurityController;
            if (securityController.getDeviceOwnerType(securityController.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Callback implements SecurityController.SecurityControllerCallback {
        private Callback() {
        }

        @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
        public void onStateChanged() {
            QSSecurityFooter.this.refreshState();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class H extends Handler {
        private H(Looper looper) {
            super(looper);
        }

        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:11:0x0001 */
        /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: com.android.systemui.qs.QSSecurityFooter$H */
        /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: com.android.systemui.qs.QSSecurityFooter$H */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.qs.QSTileHost] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str = null;
            try {
                int i = message.what;
                if (i == 1) {
                    QSSecurityFooter.this.handleRefreshState();
                } else if (i == 0) {
                    QSSecurityFooter.this.handleClick();
                }
            } catch (Throwable th) {
                while (true) {
                    str = "Error in " + str;
                    Log.w("QSSecurityFooter", str, th);
                    this = QSSecurityFooter.this.mHost;
                    this.warn(str, th);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class VpnSpan extends ClickableSpan {
        @Override // java.lang.Object
        public int hashCode() {
            return 314159257;
        }

        protected VpnSpan() {
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            Intent intent = new Intent("android.settings.VPN_SETTINGS");
            QSSecurityFooter.this.mDialog.dismiss();
            QSSecurityFooter.this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }

        @Override // java.lang.Object
        public boolean equals(Object obj) {
            return obj instanceof VpnSpan;
        }
    }
}
