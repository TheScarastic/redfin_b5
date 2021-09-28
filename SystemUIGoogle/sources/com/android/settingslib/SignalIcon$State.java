package com.android.settingslib;

import java.text.SimpleDateFormat;
/* loaded from: classes.dex */
public class SignalIcon$State {
    private static SimpleDateFormat sSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public boolean activityIn;
    public boolean activityOut;
    public boolean connected;
    public boolean enabled;
    public SignalIcon$IconGroup iconGroup;
    public int inetCondition;
    public int level;
    public int rssi;
    public long time;

    public void copyFrom(SignalIcon$State signalIcon$State) {
        this.connected = signalIcon$State.connected;
        this.enabled = signalIcon$State.enabled;
        this.level = signalIcon$State.level;
        this.iconGroup = signalIcon$State.iconGroup;
        this.inetCondition = signalIcon$State.inetCondition;
        this.activityIn = signalIcon$State.activityIn;
        this.activityOut = signalIcon$State.activityOut;
        this.rssi = signalIcon$State.rssi;
        this.time = signalIcon$State.time;
    }

    public String toString() {
        if (this.time != 0) {
            StringBuilder sb = new StringBuilder();
            toString(sb);
            return sb.toString();
        }
        return "Empty " + getClass().getSimpleName();
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        sb.append("connected=");
        sb.append(this.connected);
        sb.append(',');
        sb.append("enabled=");
        sb.append(this.enabled);
        sb.append(',');
        sb.append("level=");
        sb.append(this.level);
        sb.append(',');
        sb.append("inetCondition=");
        sb.append(this.inetCondition);
        sb.append(',');
        sb.append("iconGroup=");
        sb.append(this.iconGroup);
        sb.append(',');
        sb.append("activityIn=");
        sb.append(this.activityIn);
        sb.append(',');
        sb.append("activityOut=");
        sb.append(this.activityOut);
        sb.append(',');
        sb.append("rssi=");
        sb.append(this.rssi);
        sb.append(',');
        sb.append("lastModified=");
        sb.append(sSDF.format(Long.valueOf(this.time)));
    }

    public boolean equals(Object obj) {
        if (!obj.getClass().equals(getClass())) {
            return false;
        }
        SignalIcon$State signalIcon$State = (SignalIcon$State) obj;
        if (signalIcon$State.connected == this.connected && signalIcon$State.enabled == this.enabled && signalIcon$State.level == this.level && signalIcon$State.inetCondition == this.inetCondition && signalIcon$State.iconGroup == this.iconGroup && signalIcon$State.activityIn == this.activityIn && signalIcon$State.activityOut == this.activityOut && signalIcon$State.rssi == this.rssi) {
            return true;
        }
        return false;
    }
}
