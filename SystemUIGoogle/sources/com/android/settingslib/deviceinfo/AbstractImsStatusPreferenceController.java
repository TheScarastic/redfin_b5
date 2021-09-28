package com.android.settingslib.deviceinfo;
/* loaded from: classes.dex */
public abstract class AbstractImsStatusPreferenceController extends AbstractConnectivityPreferenceController {
    private static final String[] CONNECTIVITY_INTENTS = {"android.bluetooth.adapter.action.STATE_CHANGED", "android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.LINK_CONFIGURATION_CHANGED", "android.net.wifi.STATE_CHANGE"};
    static final String KEY_IMS_REGISTRATION_STATE = "ims_reg_state";

    @Override // com.android.settingslib.deviceinfo.AbstractConnectivityPreferenceController
    protected String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }
}
