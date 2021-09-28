package com.android.settingslib.media;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.R$color;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class MediaDevice implements Comparable<MediaDevice> {
    private int mConnectedRecord;
    protected final Context mContext;
    protected final String mPackageName;
    protected final MediaRoute2Info mRouteInfo;
    protected final MediaRouter2Manager mRouterManager;
    private int mState;
    int mType;

    public void disconnect() {
    }

    public abstract Drawable getIcon();

    public abstract Drawable getIconWithoutBackground();

    public abstract String getId();

    public abstract String getName();

    protected boolean isCarKitDevice() {
        return false;
    }

    public abstract boolean isConnected();

    protected boolean isFastPairDevice() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public MediaDevice(Context context, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        this.mContext = context;
        this.mRouteInfo = mediaRoute2Info;
        this.mRouterManager = mediaRouter2Manager;
        this.mPackageName = str;
        setType(mediaRoute2Info);
    }

    private void setType(MediaRoute2Info mediaRoute2Info) {
        if (mediaRoute2Info == null) {
            this.mType = 4;
            return;
        }
        int type = mediaRoute2Info.getType();
        if (type == 2) {
            this.mType = 7;
        } else if (type == 3 || type == 4) {
            this.mType = 2;
        } else {
            if (type != 8) {
                if (!(type == 9 || type == 22)) {
                    if (type != 23) {
                        if (type != 2000) {
                            switch (type) {
                                case 11:
                                case 12:
                                case 13:
                                    break;
                                default:
                                    this.mType = 5;
                                    return;
                            }
                        } else {
                            this.mType = 6;
                            return;
                        }
                    }
                }
                this.mType = 1;
                return;
            }
            this.mType = 4;
        }
    }

    /* access modifiers changed from: package-private */
    public void initDeviceRecord() {
        ConnectionRecordManager.getInstance().fetchLastSelectedDevice(this.mContext);
        this.mConnectedRecord = ConnectionRecordManager.getInstance().fetchConnectionRecord(this.mContext, getId());
    }

    /* access modifiers changed from: package-private */
    public void setColorFilter(Drawable drawable) {
        drawable.setColorFilter(new PorterDuffColorFilter(this.mContext.getResources().getColorStateList(R$color.advanced_icon_color, this.mContext.getTheme()).getDefaultColor(), PorterDuff.Mode.SRC_IN));
    }

    void setConnectedRecord() {
        this.mConnectedRecord++;
        ConnectionRecordManager.getInstance().setConnectionRecord(this.mContext, getId(), this.mConnectedRecord);
    }

    public void requestSetVolume(int i) {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info == null) {
            Log.w("MediaDevice", "Unable to set volume. RouteInfo is empty");
        } else {
            this.mRouterManager.setRouteVolume(mediaRoute2Info, i);
        }
    }

    public int getMaxVolume() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getVolumeMax();
        }
        Log.w("MediaDevice", "Unable to get max volume. RouteInfo is empty");
        return 0;
    }

    public int getCurrentVolume() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getVolume();
        }
        Log.w("MediaDevice", "Unable to get current volume. RouteInfo is empty");
        return 0;
    }

    public int getDeviceType() {
        return this.mType;
    }

    public boolean connect() {
        if (this.mRouteInfo == null) {
            Log.w("MediaDevice", "Unable to connect. RouteInfo is empty");
            return false;
        }
        setConnectedRecord();
        this.mRouterManager.selectRoute(this.mPackageName, this.mRouteInfo);
        return true;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public int getState() {
        return this.mState;
    }

    public int compareTo(MediaDevice mediaDevice) {
        if (isConnected() ^ mediaDevice.isConnected()) {
            return isConnected() ? -1 : 1;
        }
        int i = this.mType;
        int i2 = mediaDevice.mType;
        if (i != i2) {
            return i < i2 ? -1 : 1;
        }
        if (isFastPairDevice()) {
            return -1;
        }
        if (mediaDevice.isFastPairDevice()) {
            return 1;
        }
        if (isCarKitDevice()) {
            return -1;
        }
        if (mediaDevice.isCarKitDevice()) {
            return 1;
        }
        String lastSelectedDevice = ConnectionRecordManager.getInstance().getLastSelectedDevice();
        if (TextUtils.equals(lastSelectedDevice, getId())) {
            return -1;
        }
        if (TextUtils.equals(lastSelectedDevice, mediaDevice.getId())) {
            return 1;
        }
        int i3 = this.mConnectedRecord;
        int i4 = mediaDevice.mConnectedRecord;
        if (i3 == i4 || (i4 <= 0 && i3 <= 0)) {
            return getName().compareToIgnoreCase(mediaDevice.getName());
        }
        return i4 - i3;
    }

    public List<String> getFeatures() {
        MediaRoute2Info mediaRoute2Info = this.mRouteInfo;
        if (mediaRoute2Info != null) {
            return mediaRoute2Info.getFeatures();
        }
        Log.w("MediaDevice", "Unable to get features. RouteInfo is empty");
        return new ArrayList();
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof MediaDevice)) {
            return false;
        }
        return ((MediaDevice) obj).getId().equals(getId());
    }
}
