package com.android.systemui.statusbar.policy;

import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.R$drawable;
import com.android.settingslib.SignalIcon$IconGroup;
/* loaded from: classes.dex */
public class WifiIcons {
    public static final int[][] QS_WIFI_SIGNAL_STRENGTH;
    public static final SignalIcon$IconGroup UNMERGED_WIFI;
    static final int[] WIFI_FULL_ICONS;
    static final int WIFI_LEVEL_COUNT;
    private static final int[] WIFI_NO_INTERNET_ICONS;
    static final int[][] WIFI_SIGNAL_STRENGTH;

    static {
        int[] iArr = {17302885, 17302886, 17302887, 17302888, 17302889};
        WIFI_FULL_ICONS = iArr;
        int[] iArr2 = {R$drawable.ic_no_internet_wifi_signal_0, R$drawable.ic_no_internet_wifi_signal_1, R$drawable.ic_no_internet_wifi_signal_2, R$drawable.ic_no_internet_wifi_signal_3, R$drawable.ic_no_internet_wifi_signal_4};
        WIFI_NO_INTERNET_ICONS = iArr2;
        int[][] iArr3 = {iArr2, iArr};
        QS_WIFI_SIGNAL_STRENGTH = iArr3;
        WIFI_SIGNAL_STRENGTH = iArr3;
        WIFI_LEVEL_COUNT = iArr3[0].length;
        UNMERGED_WIFI = new SignalIcon$IconGroup("Wi-Fi Icons", iArr3, iArr3, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302885, 17302885, 17302885, 17302885, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
    }
}
