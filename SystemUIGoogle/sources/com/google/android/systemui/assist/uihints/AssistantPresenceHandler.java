package com.google.android.systemui.assist.uihints;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import com.android.internal.app.AssistUtils;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes2.dex */
public class AssistantPresenceHandler implements NgaMessageHandler.ConfigInfoListener {
    private final AssistUtils mAssistUtils;
    private final ContentResolver mContentResolver;
    private boolean mGoogleIsAssistant;
    private boolean mNgaIsAssistant;
    private boolean mSysUiIsNgaUi;
    private final Set<AssistantPresenceChangeListener> mAssistantPresenceChangeListeners = new HashSet();
    private final Set<SysUiIsNgaUiChangeListener> mSysUiIsNgaUiChangeListeners = new HashSet();

    /* loaded from: classes2.dex */
    public interface AssistantPresenceChangeListener {
        void onAssistantPresenceChanged(boolean z, boolean z2);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface SysUiIsNgaUiChangeListener {
        void onSysUiIsNgaUiChanged(boolean z);
    }

    /* access modifiers changed from: package-private */
    public AssistantPresenceHandler(Context context, AssistUtils assistUtils) {
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentResolver = contentResolver;
        this.mAssistUtils = assistUtils;
        boolean z = false;
        this.mNgaIsAssistant = Settings.Secure.getInt(contentResolver, "com.google.android.systemui.assist.uihints.NGA_IS_ASSISTANT", 0) != 0;
        this.mSysUiIsNgaUi = Settings.Secure.getInt(contentResolver, "com.google.android.systemui.assist.uihints.SYS_UI_IS_NGA_UI", 0) != 0 ? true : z;
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        updateAssistantPresence(fetchIsGoogleAssistant(), configInfo.ngaIsAssistant, configInfo.sysUiIsNgaUi);
    }

    public void registerAssistantPresenceChangeListener(AssistantPresenceChangeListener assistantPresenceChangeListener) {
        this.mAssistantPresenceChangeListeners.add(assistantPresenceChangeListener);
    }

    public void registerSysUiIsNgaUiChangeListener(SysUiIsNgaUiChangeListener sysUiIsNgaUiChangeListener) {
        this.mSysUiIsNgaUiChangeListeners.add(sysUiIsNgaUiChangeListener);
    }

    public void requestAssistantPresenceUpdate() {
        updateAssistantPresence(fetchIsGoogleAssistant(), this.mNgaIsAssistant, this.mSysUiIsNgaUi);
    }

    public boolean isSysUiNgaUi() {
        return this.mSysUiIsNgaUi;
    }

    public boolean isNgaAssistant() {
        return this.mNgaIsAssistant;
    }

    private void updateAssistantPresence(boolean z, boolean z2, boolean z3) {
        boolean z4 = true;
        boolean z5 = z && z2;
        if (!z5 || !z3) {
            z4 = false;
        }
        if (!(this.mGoogleIsAssistant == z && this.mNgaIsAssistant == z5)) {
            this.mGoogleIsAssistant = z;
            this.mNgaIsAssistant = z5;
            ContentResolver contentResolver = this.mContentResolver;
            int i = z5 ? 1 : 0;
            int i2 = z5 ? 1 : 0;
            int i3 = z5 ? 1 : 0;
            Settings.Secure.putInt(contentResolver, "com.google.android.systemui.assist.uihints.NGA_IS_ASSISTANT", i);
            for (AssistantPresenceChangeListener assistantPresenceChangeListener : this.mAssistantPresenceChangeListeners) {
                assistantPresenceChangeListener.onAssistantPresenceChanged(this.mGoogleIsAssistant, this.mNgaIsAssistant);
            }
        }
        if (this.mSysUiIsNgaUi != z4) {
            this.mSysUiIsNgaUi = z4;
            ContentResolver contentResolver2 = this.mContentResolver;
            int i4 = z4 ? 1 : 0;
            int i5 = z4 ? 1 : 0;
            int i6 = z4 ? 1 : 0;
            Settings.Secure.putInt(contentResolver2, "com.google.android.systemui.assist.uihints.SYS_UI_IS_NGA_UI", i4);
            for (SysUiIsNgaUiChangeListener sysUiIsNgaUiChangeListener : this.mSysUiIsNgaUiChangeListeners) {
                sysUiIsNgaUiChangeListener.onSysUiIsNgaUiChanged(this.mSysUiIsNgaUi);
            }
        }
    }

    private boolean fetchIsGoogleAssistant() {
        ComponentName assistComponentForUser = this.mAssistUtils.getAssistComponentForUser(-2);
        return assistComponentForUser != null && "com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString());
    }
}
