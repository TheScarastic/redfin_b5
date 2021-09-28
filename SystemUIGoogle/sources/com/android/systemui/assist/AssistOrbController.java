package com.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.policy.ConfigurationController;
/* loaded from: classes.dex */
public class AssistOrbController {
    private final Context mContext;
    private AssistOrbContainer mView;
    private final WindowManager mWindowManager;
    private Runnable mHideRunnable = new Runnable() { // from class: com.android.systemui.assist.AssistOrbController.1
        @Override // java.lang.Runnable
        public void run() {
            AssistOrbController.this.mView.removeCallbacks(this);
            AssistOrbController.this.mView.show(false, true, new AssistOrbController$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            AssistOrbController.this.mWindowManager.removeView(AssistOrbController.this.mView);
        }
    };
    private ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.assist.AssistOrbController.2
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            boolean z;
            if (AssistOrbController.this.mInterestingConfigChanges.applyNewConfig(AssistOrbController.this.mContext.getResources())) {
                if (AssistOrbController.this.mView != null) {
                    z = AssistOrbController.this.mView.isShowing();
                    if (AssistOrbController.this.mView.isAttachedToWindow()) {
                        AssistOrbController.this.mWindowManager.removeView(AssistOrbController.this.mView);
                    }
                } else {
                    z = false;
                }
                if (z) {
                    AssistOrbController.this.showOrb(false);
                }
            }
        }
    };
    private final InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges(-2147482748);

    /* access modifiers changed from: package-private */
    public AssistOrbController(ConfigurationController configurationController, Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        configurationController.addCallback(this.mConfigurationListener);
        this.mConfigurationListener.onConfigChanged(context.getResources().getConfiguration());
    }

    public void postHide() {
        this.mView.post(this.mHideRunnable);
    }

    public void postHideDelayed(long j) {
        this.mView.postDelayed(this.mHideRunnable, j);
    }

    /* access modifiers changed from: private */
    public void showOrb(boolean z) {
        if (this.mView == null) {
            AssistOrbContainer assistOrbContainer = (AssistOrbContainer) LayoutInflater.from(this.mContext).inflate(R$layout.assist_orb, (ViewGroup) null);
            this.mView = assistOrbContainer;
            assistOrbContainer.setVisibility(8);
            this.mView.setSystemUiVisibility(1792);
        }
        if (!this.mView.isAttachedToWindow()) {
            this.mWindowManager.addView(this.mView, getLayoutParams());
        }
        this.mView.show(true, z, null);
    }

    private WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, this.mContext.getResources().getDimensionPixelSize(R$dimen.assist_orb_scrim_height), 2033, 280, -3);
        layoutParams.token = new Binder();
        layoutParams.gravity = 8388691;
        layoutParams.setTitle("AssistPreviewPanel");
        layoutParams.softInputMode = 49;
        return layoutParams;
    }

    public void showOrb(ComponentName componentName, boolean z) {
        showOrb(true);
        maybeSwapSearchIcon(componentName, z);
    }

    private void maybeSwapSearchIcon(ComponentName componentName, boolean z) {
        replaceDrawable(this.mView.getOrb().getLogo(), componentName, "com.android.systemui.action_assist_icon", z);
    }

    public void replaceDrawable(ImageView imageView, ComponentName componentName, String str, boolean z) {
        Bundle bundle;
        int i;
        if (componentName != null) {
            try {
                PackageManager packageManager = this.mContext.getPackageManager();
                if (z) {
                    bundle = packageManager.getServiceInfo(componentName, 128).metaData;
                } else {
                    bundle = packageManager.getActivityInfo(componentName, 128).metaData;
                }
                if (!(bundle == null || (i = bundle.getInt(str)) == 0)) {
                    imageView.setImageDrawable(packageManager.getResourcesForApplication(componentName.getPackageName()).getDrawable(i));
                    return;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            } catch (Resources.NotFoundException e) {
                Log.w("AssistOrbController", "Failed to swap drawable from " + componentName.flattenToShortString(), e);
            }
        }
        imageView.setImageDrawable(null);
    }
}
