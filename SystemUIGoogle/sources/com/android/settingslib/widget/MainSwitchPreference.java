package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Switch;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$styleable;
import androidx.preference.TwoStatePreference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class MainSwitchPreference extends TwoStatePreference implements OnMainSwitchChangeListener {
    private MainSwitchBar mMainSwitchBar;
    private final List<OnMainSwitchChangeListener> mSwitchChangeListeners = new ArrayList();
    private CharSequence mTitle;

    public MainSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(false);
        preferenceViewHolder.setDividerAllowedBelow(false);
        this.mMainSwitchBar = (MainSwitchBar) preferenceViewHolder.findViewById(R$id.settingslib_main_switch_bar);
        updateStatus(isChecked());
        registerListenerToSwitchBar();
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayoutResource(R$layout.settingslib_main_switch_layout);
        this.mSwitchChangeListeners.add(this);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Preference, 0, 0);
            setTitle(obtainStyledAttributes.getText(R$styleable.Preference_android_title));
            obtainStyledAttributes.recycle();
        }
    }

    @Override // androidx.preference.TwoStatePreference
    public void setChecked(boolean z) {
        super.setChecked(z);
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null && mainSwitchBar.isChecked() != z) {
            this.mMainSwitchBar.setChecked(z);
        }
    }

    @Override // androidx.preference.Preference
    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.setTitle(charSequence);
        }
    }

    @Override // com.android.settingslib.widget.OnMainSwitchChangeListener
    public void onSwitchChanged(Switch r1, boolean z) {
        super.setChecked(z);
    }

    public void updateStatus(boolean z) {
        setChecked(z);
        MainSwitchBar mainSwitchBar = this.mMainSwitchBar;
        if (mainSwitchBar != null) {
            mainSwitchBar.setTitle(this.mTitle);
            this.mMainSwitchBar.show();
        }
    }

    private void registerListenerToSwitchBar() {
        for (OnMainSwitchChangeListener onMainSwitchChangeListener : this.mSwitchChangeListeners) {
            this.mMainSwitchBar.addOnSwitchChangeListener(onMainSwitchChangeListener);
        }
        this.mSwitchChangeListeners.clear();
    }
}
