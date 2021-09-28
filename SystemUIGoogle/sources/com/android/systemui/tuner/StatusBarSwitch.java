package com.android.systemui.tuner;

import android.app.ActivityManager;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import androidx.preference.SwitchPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import java.util.Set;
/* loaded from: classes2.dex */
public class StatusBarSwitch extends SwitchPreference implements TunerService.Tunable {
    private Set<String> mHideList;

    public StatusBarSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.preference.Preference
    public void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist");
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        super.onDetached();
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(getContext(), str2);
            this.mHideList = iconHideList;
            setChecked(!iconHideList.contains(getKey()));
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public boolean persistBoolean(boolean z) {
        if (!z) {
            if (this.mHideList.contains(getKey())) {
                return true;
            }
            MetricsLogger.action(getContext(), 234, getKey());
            this.mHideList.add(getKey());
            setList(this.mHideList);
            return true;
        } else if (!this.mHideList.remove(getKey())) {
            return true;
        } else {
            MetricsLogger.action(getContext(), 233, getKey());
            setList(this.mHideList);
            return true;
        }
    }

    private void setList(Set<String> set) {
        Settings.Secure.putStringForUser(getContext().getContentResolver(), "icon_blacklist", TextUtils.join(",", set), ActivityManager.getCurrentUser());
    }
}
