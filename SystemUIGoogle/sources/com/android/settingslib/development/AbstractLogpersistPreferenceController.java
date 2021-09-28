package com.android.settingslib.development;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemProperties;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settingslib.R$array;
import com.android.settingslib.core.ConfirmationDialogController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
/* loaded from: classes.dex */
public abstract class AbstractLogpersistPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, LifecycleObserver, OnCreate, OnDestroy, ConfirmationDialogController {
    static final String ACTUAL_LOGPERSIST_PROPERTY = "logd.logpersistd";
    static final String ACTUAL_LOGPERSIST_PROPERTY_BUFFER = "logd.logpersistd.buffer";
    static final String SELECT_LOGPERSIST_PROPERTY_SERVICE = "logcatd";
    private ListPreference mLogpersist;
    private boolean mLogpersistCleared;
    private final BroadcastReceiver mReceiver;

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference != this.mLogpersist) {
            return false;
        }
        writeLogpersistOption(obj, false);
        return true;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnCreate
    public void onCreate(Bundle bundle) {
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mReceiver, new IntentFilter("com.android.settingslib.development.AbstractLogdSizePreferenceController.LOGD_SIZE_UPDATED"));
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mReceiver);
    }

    public void updateLogpersistValues() {
        char c;
        if (this.mLogpersist != null) {
            String str = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY);
            if (str == null) {
                str = "";
            }
            String str2 = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY_BUFFER);
            if (str2 == null || str2.length() == 0) {
                str2 = "all";
            }
            if (!str.equals(SELECT_LOGPERSIST_PROPERTY_SERVICE)) {
                c = 0;
            } else if (!str2.equals("kernel")) {
                if (!str2.equals("all") && !str2.contains("radio") && str2.contains("security") && str2.contains("kernel")) {
                    c = 2;
                    if (!str2.contains("default")) {
                        String[] strArr = {"main", "events", "system", "crash"};
                        for (int i = 0; i < 4; i++) {
                            if (str2.contains(strArr[i])) {
                            }
                        }
                    }
                }
                c = 1;
                break;
            } else {
                c = 3;
            }
            this.mLogpersist.setValue(this.mContext.getResources().getStringArray(R$array.select_logpersist_values)[c]);
            this.mLogpersist.setSummary(this.mContext.getResources().getStringArray(R$array.select_logpersist_summaries)[c]);
            if (c != 0) {
                this.mLogpersistCleared = false;
            } else if (!this.mLogpersistCleared) {
                SystemProperties.set(ACTUAL_LOGPERSIST_PROPERTY, "clear");
                SystemPropPoker.getInstance().poke();
                this.mLogpersistCleared = true;
            }
        }
    }

    protected void setLogpersistOff(boolean z) {
        String str;
        SystemProperties.set("persist.logd.logpersistd.buffer", "");
        SystemProperties.set(ACTUAL_LOGPERSIST_PROPERTY_BUFFER, "");
        SystemProperties.set("persist.logd.logpersistd", "");
        if (z) {
            str = "";
        } else {
            str = "stop";
        }
        SystemProperties.set(ACTUAL_LOGPERSIST_PROPERTY, str);
        SystemPropPoker.getInstance().poke();
        if (z) {
            updateLogpersistValues();
            return;
        }
        for (int i = 0; i < 3 && (r6 = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY)) != null && !r6.equals(""); i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void writeLogpersistOption(Object obj, boolean z) {
        String str;
        if (this.mLogpersist != null) {
            String str2 = SystemProperties.get("persist.log.tag");
            if (str2 != null && str2.startsWith("Settings")) {
                obj = null;
                z = true;
            }
            if (obj == null || obj.toString().equals("")) {
                if (z) {
                    this.mLogpersistCleared = false;
                } else if (!this.mLogpersistCleared && (str = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY)) != null && str.equals(SELECT_LOGPERSIST_PROPERTY_SERVICE)) {
                    showConfirmationDialog(this.mLogpersist);
                    return;
                }
                setLogpersistOff(true);
                return;
            }
            String str3 = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY_BUFFER);
            if (str3 != null && !str3.equals(obj.toString())) {
                setLogpersistOff(false);
            }
            SystemProperties.set("persist.logd.logpersistd.buffer", obj.toString());
            SystemProperties.set("persist.logd.logpersistd", SELECT_LOGPERSIST_PROPERTY_SERVICE);
            SystemPropPoker.getInstance().poke();
            for (int i = 0; i < 3 && ((r7 = SystemProperties.get(ACTUAL_LOGPERSIST_PROPERTY)) == null || !r7.equals(SELECT_LOGPERSIST_PROPERTY_SERVICE)); i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException unused) {
                }
            }
            updateLogpersistValues();
        }
    }
}
