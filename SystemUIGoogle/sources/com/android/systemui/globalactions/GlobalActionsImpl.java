package com.android.systemui.globalactions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$style;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
/* loaded from: classes.dex */
public class GlobalActionsImpl implements GlobalActions, CommandQueue.Callbacks {
    private final BlurUtils mBlurUtils;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private boolean mDisabled;
    private GlobalActionsDialogLite mGlobalActionsDialog;
    private final Lazy<GlobalActionsDialogLite> mGlobalActionsDialogLazy;
    private final KeyguardStateController mKeyguardStateController;

    public GlobalActionsImpl(Context context, CommandQueue commandQueue, Lazy<GlobalActionsDialogLite> lazy, BlurUtils blurUtils, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController) {
        this.mContext = context;
        this.mGlobalActionsDialogLazy = lazy;
        this.mKeyguardStateController = keyguardStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mBlurUtils = blurUtils;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    @Override // com.android.systemui.plugins.GlobalActions
    public void destroy() {
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        GlobalActionsDialogLite globalActionsDialogLite = this.mGlobalActionsDialog;
        if (globalActionsDialogLite != null) {
            globalActionsDialogLite.destroy();
            this.mGlobalActionsDialog = null;
        }
    }

    @Override // com.android.systemui.plugins.GlobalActions
    public void showGlobalActions(GlobalActions.GlobalActionsManager globalActionsManager) {
        if (!this.mDisabled) {
            GlobalActionsDialogLite globalActionsDialogLite = this.mGlobalActionsDialogLazy.get();
            this.mGlobalActionsDialog = globalActionsDialogLite;
            globalActionsDialogLite.showOrHideDialog(this.mKeyguardStateController.isShowing(), this.mDeviceProvisionedController.isDeviceProvisioned());
        }
    }

    @Override // com.android.systemui.plugins.GlobalActions
    public void showShutdownUi(boolean z, String str) {
        int i;
        ScrimDrawable scrimDrawable = new ScrimDrawable();
        Dialog dialog = new Dialog(this.mContext, R$style.Theme_SystemUI_Dialog_GlobalActions);
        dialog.setOnShowListener(new DialogInterface.OnShowListener(scrimDrawable, dialog) { // from class: com.android.systemui.globalactions.GlobalActionsImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ ScrimDrawable f$1;
            public final /* synthetic */ Dialog f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                GlobalActionsImpl.$r8$lambda$znZgs6HApTku0mDCtkvQnyk5YlI(GlobalActionsImpl.this, this.f$1, this.f$2, dialogInterface);
            }
        });
        Window window = dialog.getWindow();
        window.requestFeature(1);
        window.getAttributes().systemUiVisibility |= 1792;
        window.getDecorView();
        window.getAttributes().width = -1;
        window.getAttributes().height = -1;
        window.getAttributes().layoutInDisplayCutoutMode = 3;
        window.setType(2020);
        window.getAttributes().setFitInsetsTypes(0);
        window.clearFlags(2);
        window.addFlags(17629472);
        window.setBackgroundDrawable(scrimDrawable);
        window.setWindowAnimations(R$style.Animation_ShutdownUi);
        dialog.setContentView(17367309);
        dialog.setCancelable(false);
        if (this.mBlurUtils.supportsBlursOnWindows()) {
            i = Utils.getColorAttrDefaultColor(this.mContext, R$attr.wallpaperTextColor);
        } else {
            i = this.mContext.getResources().getColor(R$color.global_actions_shutdown_ui_text);
        }
        ((ProgressBar) dialog.findViewById(16908301)).getIndeterminateDrawable().setTint(i);
        TextView textView = (TextView) dialog.findViewById(16908308);
        TextView textView2 = (TextView) dialog.findViewById(16908309);
        textView.setTextColor(i);
        textView2.setTextColor(i);
        textView2.setText(getRebootMessage(z, str));
        String reasonMessage = getReasonMessage(str);
        if (reasonMessage != null) {
            textView.setVisibility(0);
            textView.setText(reasonMessage);
        }
        dialog.show();
    }

    public /* synthetic */ void lambda$showShutdownUi$0(ScrimDrawable scrimDrawable, Dialog dialog, DialogInterface dialogInterface) {
        if (this.mBlurUtils.supportsBlursOnWindows()) {
            scrimDrawable.setAlpha(255);
            this.mBlurUtils.applyBlur(dialog.getWindow().getDecorView().getViewRootImpl(), this.mBlurUtils.blurRadiusOfRatio(1.0f), true);
            return;
        }
        scrimDrawable.setAlpha((int) (this.mContext.getResources().getFloat(R$dimen.shutdown_scrim_behind_alpha) * 255.0f));
    }

    private int getRebootMessage(boolean z, String str) {
        if (str != null && str.startsWith("recovery-update")) {
            return 17041241;
        }
        if ((str == null || !str.equals("recovery")) && !z) {
            return 17041386;
        }
        return 17041237;
    }

    private String getReasonMessage(String str) {
        if (str != null && str.startsWith("recovery-update")) {
            return this.mContext.getString(17041242);
        }
        if (str == null || !str.equals("recovery")) {
            return null;
        }
        return this.mContext.getString(17041238);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        GlobalActionsDialogLite globalActionsDialogLite;
        boolean z2 = (i3 & 8) != 0;
        if (i == this.mContext.getDisplayId() && z2 != this.mDisabled) {
            this.mDisabled = z2;
            if (z2 && (globalActionsDialogLite = this.mGlobalActionsDialog) != null) {
                globalActionsDialogLite.dismissDialog();
            }
        }
    }
}
