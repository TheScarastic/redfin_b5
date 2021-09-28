package com.android.systemui.media.dialog;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.ShadeController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class MediaOutputController implements LocalMediaManager.DeviceCallback {
    private static final boolean DEBUG = Log.isLoggable("MediaOutputController", 3);
    private final boolean mAboveStatusbar;
    private final ActivityStarter mActivityStarter;
    Callback mCallback;
    private final Context mContext;
    LocalMediaManager mLocalMediaManager;
    private MediaController mMediaController;
    private final MediaSessionManager mMediaSessionManager;
    private MediaOutputMetricLogger mMetricLogger;
    private final NotificationEntryManager mNotificationEntryManager;
    private final String mPackageName;
    private final ShadeController mShadeController;
    private UiEventLogger mUiEventLogger;
    private final List<MediaDevice> mGroupMediaDevices = new CopyOnWriteArrayList();
    final List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    private final MediaController.Callback mCb = new MediaController.Callback() { // from class: com.android.systemui.media.dialog.MediaOutputController.1
        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            MediaOutputController.this.mCallback.onMediaChanged();
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            int state = playbackState.getState();
            if (state == 1 || state == 2) {
                MediaOutputController.this.mCallback.onMediaStoppedOrPaused();
            }
        }
    };

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        void dismissDialog();

        void onMediaChanged();

        void onMediaStoppedOrPaused();

        void onRouteChanged();
    }

    public MediaOutputController(Context context, String str, boolean z, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ShadeController shadeController, ActivityStarter activityStarter, NotificationEntryManager notificationEntryManager, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mPackageName = str;
        this.mMediaSessionManager = mediaSessionManager;
        this.mShadeController = shadeController;
        this.mActivityStarter = activityStarter;
        this.mAboveStatusbar = z;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mLocalMediaManager = new LocalMediaManager(context, localBluetoothManager, new InfoMediaManager(context, str, null, localBluetoothManager), str);
        this.mMetricLogger = new MediaOutputMetricLogger(context, str);
        this.mUiEventLogger = uiEventLogger;
    }

    /* access modifiers changed from: package-private */
    public void start(Callback callback) {
        this.mMediaDevices.clear();
        if (!TextUtils.isEmpty(this.mPackageName)) {
            Iterator<MediaController> it = this.mMediaSessionManager.getActiveSessions(null).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MediaController next = it.next();
                if (TextUtils.equals(next.getPackageName(), this.mPackageName)) {
                    this.mMediaController = next;
                    next.unregisterCallback(this.mCb);
                    this.mMediaController.registerCallback(this.mCb);
                    break;
                }
            }
        }
        if (this.mMediaController == null && DEBUG) {
            Log.d("MediaOutputController", "No media controller for " + this.mPackageName);
        }
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager != null) {
            this.mCallback = callback;
            localMediaManager.unregisterCallback(this);
            this.mLocalMediaManager.stopScan();
            this.mLocalMediaManager.registerCallback(this);
            this.mLocalMediaManager.startScan();
        } else if (DEBUG) {
            Log.d("MediaOutputController", "No local media manager " + this.mPackageName);
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(this.mCb);
        }
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager != null) {
            localMediaManager.unregisterCallback(this);
            this.mLocalMediaManager.stopScan();
        }
        this.mMediaDevices.clear();
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public void onDeviceListUpdate(List<MediaDevice> list) {
        buildMediaDevices(list);
        this.mCallback.onRouteChanged();
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
        this.mCallback.onRouteChanged();
        this.mMetricLogger.logOutputSuccess(mediaDevice.toString(), this.mMediaDevices);
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public void onDeviceAttributesChanged() {
        this.mCallback.onRouteChanged();
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public void onRequestFailed(int i) {
        this.mCallback.onRouteChanged();
        this.mMetricLogger.logOutputFailure(this.mMediaDevices, i);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderTitle() {
        MediaMetadata metadata;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return this.mContext.getText(R$string.controls_media_title);
        }
        return metadata.getDescription().getTitle();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderSubTitle() {
        MediaMetadata metadata;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return null;
        }
        return metadata.getDescription().getSubtitle();
    }

    /* access modifiers changed from: package-private */
    public IconCompat getHeaderIcon() {
        Bitmap iconBitmap;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null) {
            return null;
        }
        MediaMetadata metadata = mediaController.getMetadata();
        if (metadata == null || (iconBitmap = metadata.getDescription().getIconBitmap()) == null) {
            if (DEBUG) {
                Log.d("MediaOutputController", "Media meta data does not contain icon information");
            }
            return getNotificationIcon();
        }
        Context context = this.mContext;
        return IconCompat.createWithBitmap(Utils.convertCornerRadiusBitmap(context, iconBitmap, (float) context.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_icon_corner_radius)));
    }

    /* access modifiers changed from: package-private */
    public IconCompat getDeviceIconCompat(MediaDevice mediaDevice) {
        Drawable icon = mediaDevice.getIcon();
        if (icon == null) {
            if (DEBUG) {
                Log.d("MediaOutputController", "getDeviceIconCompat() device : " + mediaDevice.getName() + ", drawable is null");
            }
            icon = this.mContext.getDrawable(17302326);
        }
        return BluetoothUtils.createIconWithDrawable(icon);
    }

    IconCompat getNotificationIcon() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            return null;
        }
        Iterator<NotificationEntry> it = this.mNotificationEntryManager.getActiveNotificationsForCurrentUser().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            NotificationEntry next = it.next();
            Notification notification = next.getSbn().getNotification();
            if (notification.hasMediaSession() && TextUtils.equals(next.getSbn().getPackageName(), this.mPackageName)) {
                Icon largeIcon = notification.getLargeIcon();
                if (largeIcon != null) {
                    return IconCompat.createFromIcon(largeIcon);
                }
            }
        }
        return null;
    }

    private void buildMediaDevices(List<MediaDevice> list) {
        if (this.mMediaDevices.isEmpty()) {
            MediaDevice currentConnectedMediaDevice = getCurrentConnectedMediaDevice();
            if (currentConnectedMediaDevice == null) {
                if (DEBUG) {
                    Log.d("MediaOutputController", "No connected media device.");
                }
                this.mMediaDevices.addAll(list);
                return;
            }
            for (MediaDevice mediaDevice : list) {
                if (TextUtils.equals(mediaDevice.getId(), currentConnectedMediaDevice.getId())) {
                    this.mMediaDevices.add(0, mediaDevice);
                } else {
                    this.mMediaDevices.add(mediaDevice);
                }
            }
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (MediaDevice mediaDevice2 : this.mMediaDevices) {
            Iterator<MediaDevice> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    MediaDevice next = it.next();
                    if (TextUtils.equals(mediaDevice2.getId(), next.getId())) {
                        arrayList.add(next);
                        break;
                    }
                }
            }
        }
        if (arrayList.size() != list.size()) {
            list.removeAll(arrayList);
            arrayList.addAll(list);
        }
        this.mMediaDevices.clear();
        this.mMediaDevices.addAll(arrayList);
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getGroupMediaDevices() {
        List<MediaDevice> selectedMediaDevice = getSelectedMediaDevice();
        List<MediaDevice> selectableMediaDevice = getSelectableMediaDevice();
        if (this.mGroupMediaDevices.isEmpty()) {
            this.mGroupMediaDevices.addAll(selectedMediaDevice);
            this.mGroupMediaDevices.addAll(selectableMediaDevice);
            return this.mGroupMediaDevices;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.addAll(selectedMediaDevice);
        arrayList.addAll(selectableMediaDevice);
        for (MediaDevice mediaDevice : this.mGroupMediaDevices) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    MediaDevice mediaDevice2 = (MediaDevice) it.next();
                    if (TextUtils.equals(mediaDevice.getId(), mediaDevice2.getId())) {
                        arrayList2.add(mediaDevice2);
                        arrayList.remove(mediaDevice2);
                        break;
                    }
                }
            }
        }
        if (!arrayList.isEmpty()) {
            arrayList2.addAll(arrayList);
        }
        this.mGroupMediaDevices.clear();
        this.mGroupMediaDevices.addAll(arrayList2);
        return this.mGroupMediaDevices;
    }

    /* access modifiers changed from: package-private */
    public void resetGroupMediaDevices() {
        this.mGroupMediaDevices.clear();
    }

    /* access modifiers changed from: package-private */
    public void connectDevice(MediaDevice mediaDevice) {
        this.mMetricLogger.updateOutputEndPoints(getCurrentConnectedMediaDevice(), mediaDevice);
        ThreadUtils.postOnBackgroundThread(new Runnable(mediaDevice) { // from class: com.android.systemui.media.dialog.MediaOutputController$$ExternalSyntheticLambda2
            public final /* synthetic */ MediaDevice f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaOutputController.this.lambda$connectDevice$0(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$connectDevice$0(MediaDevice mediaDevice) {
        this.mLocalMediaManager.connectDevice(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public Collection<MediaDevice> getMediaDevices() {
        return this.mMediaDevices;
    }

    /* access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedMediaDevice() {
        return this.mLocalMediaManager.getCurrentConnectedDevice();
    }

    /* access modifiers changed from: package-private */
    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        return this.mLocalMediaManager.addDeviceToPlayMedia(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        return this.mLocalMediaManager.removeDeviceFromPlayMedia(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectableMediaDevice() {
        return this.mLocalMediaManager.getSelectableMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectedMediaDevice() {
        return this.mLocalMediaManager.getSelectedMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getDeselectableMediaDevice() {
        return this.mLocalMediaManager.getDeselectableMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(int i) {
        this.mLocalMediaManager.adjustSessionVolume(i);
    }

    /* access modifiers changed from: package-private */
    public int getSessionVolumeMax() {
        return this.mLocalMediaManager.getSessionVolumeMax();
    }

    /* access modifiers changed from: package-private */
    public int getSessionVolume() {
        return this.mLocalMediaManager.getSessionVolume();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSessionName() {
        return this.mLocalMediaManager.getSessionName();
    }

    /* access modifiers changed from: package-private */
    public void releaseSession() {
        this.mLocalMediaManager.releaseSession();
    }

    /* access modifiers changed from: package-private */
    public void adjustVolume(MediaDevice mediaDevice, int i) {
        ThreadUtils.postOnBackgroundThread(new Runnable(i) { // from class: com.android.systemui.media.dialog.MediaOutputController$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaOutputController.lambda$adjustVolume$1(MediaDevice.this, this.f$1);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean hasAdjustVolumeUserRestriction() {
        if (RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_adjust_volume", UserHandle.myUserId()) != null) {
            return true;
        }
        return ((UserManager) this.mContext.getSystemService(UserManager.class)).hasBaseUserRestriction("no_adjust_volume", UserHandle.of(UserHandle.myUserId()));
    }

    /* access modifiers changed from: package-private */
    public boolean isTransferring() {
        for (MediaDevice mediaDevice : this.mMediaDevices) {
            if (mediaDevice.getState() == 1) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isZeroMode() {
        if (this.mMediaDevices.size() != 1) {
            return false;
        }
        int deviceType = this.mMediaDevices.iterator().next().getDeviceType();
        if (deviceType == 7 || deviceType == 2 || deviceType == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void launchBluetoothPairing() {
        this.mCallback.dismissDialog();
        this.mActivityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.media.dialog.MediaOutputController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                return MediaOutputController.this.lambda$launchBluetoothPairing$2();
            }
        }, null, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$launchBluetoothPairing$2() {
        this.mContext.sendBroadcast(new Intent().setAction("com.android.settings.action.LAUNCH_BLUETOOTH_PAIRING").setPackage("com.android.settings"));
        this.mShadeController.animateCollapsePanels();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void launchMediaOutputDialog() {
        this.mCallback.dismissDialog();
        new MediaOutputDialog(this.mContext, this.mAboveStatusbar, this, this.mUiEventLogger);
    }

    /* access modifiers changed from: package-private */
    public void launchMediaOutputGroupDialog() {
        this.mCallback.dismissDialog();
        new MediaOutputGroupDialog(this.mContext, this.mAboveStatusbar, this);
    }

    /* access modifiers changed from: package-private */
    public boolean isActiveRemoteDevice(MediaDevice mediaDevice) {
        List<String> features = mediaDevice.getFeatures();
        return features.contains("android.media.route.feature.REMOTE_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_AUDIO_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_VIDEO_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_GROUP_PLAYBACK");
    }

    /* access modifiers changed from: package-private */
    public boolean isVolumeControlEnabled(MediaDevice mediaDevice) {
        return !isActiveRemoteDevice(mediaDevice);
    }
}
