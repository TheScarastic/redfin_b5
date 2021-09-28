package com.android.systemui.statusbar;

import android.content.Context;
import android.util.FeatureFlagUtils;
import com.android.systemui.R$bool;
import com.android.systemui.flags.FeatureFlagReader;
/* loaded from: classes.dex */
public class FeatureFlags {
    private final Context mContext;
    private final FeatureFlagReader mFlagReader;

    public FeatureFlags(FeatureFlagReader featureFlagReader, Context context) {
        this.mFlagReader = featureFlagReader;
        this.mContext = context;
    }

    public boolean isNewNotifPipelineEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_notification_pipeline2);
    }

    public boolean isNewNotifPipelineRenderingEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_notification_pipeline2_rendering);
    }

    public boolean isTwoColumnNotificationShadeEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_notification_twocolumn);
    }

    public boolean isKeyguardLayoutEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_keyguard_layout);
    }

    public boolean useNewLockscreenAnimations() {
        return this.mFlagReader.isEnabled(R$bool.flag_lockscreen_animations);
    }

    public boolean isMonetEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_monet);
    }

    public boolean isPMLiteEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_pm_lite);
    }

    public boolean isChargingRippleEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_charging_ripple);
    }

    public boolean isOngoingCallStatusBarChipEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_ongoing_call_status_bar_chip);
    }

    public boolean isSmartspaceEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_smartspace);
    }

    public boolean isSmartspaceDedupingEnabled() {
        return isSmartspaceEnabled() && this.mFlagReader.isEnabled(R$bool.flag_smartspace_deduping);
    }

    public boolean isNewKeyguardSwipeAnimationEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_new_unlock_swipe_animation);
    }

    public boolean isSmartSpaceSharedElementTransitionEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_smartspace_shared_element_transition);
    }

    public boolean isCombinedStatusBarSignalIconsEnabled() {
        return this.mFlagReader.isEnabled(R$bool.flag_combined_status_bar_signal_icons);
    }

    public boolean isProviderModelSettingEnabled() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_provider_model");
    }
}
