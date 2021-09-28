package com.android.systemui.qs;

import android.database.ContentObserver;
import android.os.Handler;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes.dex */
public abstract class SecureSetting extends ContentObserver {
    private final int mDefaultValue;
    private boolean mListening;
    private int mObservedValue;
    private final SecureSettings mSecureSettings;
    private final String mSettingName;
    private int mUserId;

    protected abstract void handleValueChanged(int i, boolean z);

    public SecureSetting(SecureSettings secureSettings, Handler handler, String str, int i) {
        this(secureSettings, handler, str, i, 0);
    }

    public SecureSetting(SecureSettings secureSettings, Handler handler, String str, int i, int i2) {
        super(handler);
        this.mSecureSettings = secureSettings;
        this.mSettingName = str;
        this.mDefaultValue = i2;
        this.mObservedValue = i2;
        this.mUserId = i;
    }

    public int getValue() {
        return this.mSecureSettings.getIntForUser(this.mSettingName, this.mDefaultValue, this.mUserId);
    }

    public void setValue(int i) {
        this.mSecureSettings.putIntForUser(this.mSettingName, i, this.mUserId);
    }

    public void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            if (z) {
                this.mObservedValue = getValue();
                SecureSettings secureSettings = this.mSecureSettings;
                secureSettings.registerContentObserverForUser(secureSettings.getUriFor(this.mSettingName), false, this, this.mUserId);
                return;
            }
            this.mSecureSettings.unregisterContentObserver(this);
            this.mObservedValue = this.mDefaultValue;
        }
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z) {
        int value = getValue();
        handleValueChanged(value, value != this.mObservedValue);
        this.mObservedValue = value;
    }

    public void setUserId(int i) {
        this.mUserId = i;
        if (this.mListening) {
            setListening(false);
            setListening(true);
        }
    }

    public String getKey() {
        return this.mSettingName;
    }
}
