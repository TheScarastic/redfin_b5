package com.android.systemui.tuner;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import androidx.preference.DropDownPreference;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
/* loaded from: classes2.dex */
public class ClockPreference extends DropDownPreference implements TunerService.Tunable {
    private final String mClock;
    private boolean mClockEnabled;
    private boolean mHasSeconds;
    private boolean mHasSetValue;
    private ArraySet<String> mHideList;
    private boolean mReceivedClock;
    private boolean mReceivedSeconds;

    public ClockPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClock = context.getString(17041434);
        setEntryValues(new CharSequence[]{"seconds", "default", "disabled"});
    }

    @Override // androidx.preference.Preference
    public void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist", "clock_seconds");
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        super.onDetached();
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            this.mReceivedClock = true;
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(getContext(), str2);
            this.mHideList = iconHideList;
            this.mClockEnabled = !iconHideList.contains(this.mClock);
        } else if ("clock_seconds".equals(str)) {
            this.mReceivedSeconds = true;
            this.mHasSeconds = (str2 == null || Integer.parseInt(str2) == 0) ? false : true;
        }
        if (!this.mHasSetValue && this.mReceivedClock && this.mReceivedSeconds) {
            this.mHasSetValue = true;
            boolean z = this.mClockEnabled;
            if (z && this.mHasSeconds) {
                setValue("seconds");
            } else if (z) {
                setValue("default");
            } else {
                setValue("disabled");
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public boolean persistString(String str) {
        ((TunerService) Dependency.get(TunerService.class)).setValue("clock_seconds", "seconds".equals(str) ? 1 : 0);
        if ("disabled".equals(str)) {
            this.mHideList.add(this.mClock);
        } else {
            this.mHideList.remove(this.mClock);
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue("icon_blacklist", TextUtils.join(",", this.mHideList));
        return true;
    }
}
