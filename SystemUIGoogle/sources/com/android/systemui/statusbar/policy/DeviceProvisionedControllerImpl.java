package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class DeviceProvisionedControllerImpl extends CurrentUserTracker implements DeviceProvisionedController {
    protected static final String TAG = "DeviceProvisionedControllerImpl";
    private final Uri mDeviceProvisionedUri;
    private final GlobalSettings mGlobalSettings;
    protected final ArrayList<DeviceProvisionedController.DeviceProvisionedListener> mListeners = new ArrayList<>();
    private final SecureSettings mSecureSettings;
    protected final ContentObserver mSettingsObserver;
    private final Uri mUserSetupUri;

    public DeviceProvisionedControllerImpl(Handler handler, BroadcastDispatcher broadcastDispatcher, GlobalSettings globalSettings, SecureSettings secureSettings) {
        super(broadcastDispatcher);
        this.mGlobalSettings = globalSettings;
        this.mSecureSettings = secureSettings;
        this.mDeviceProvisionedUri = globalSettings.getUriFor("device_provisioned");
        this.mUserSetupUri = secureSettings.getUriFor("user_setup_complete");
        this.mSettingsObserver = new ContentObserver(handler) { // from class: com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri, int i) {
                String str = DeviceProvisionedControllerImpl.TAG;
                Log.d(str, "Setting change: " + uri);
                if (DeviceProvisionedControllerImpl.this.mUserSetupUri.equals(uri)) {
                    DeviceProvisionedControllerImpl.this.notifySetupChanged();
                } else {
                    DeviceProvisionedControllerImpl.this.notifyProvisionedChanged();
                }
            }
        };
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public boolean isDeviceProvisioned() {
        return this.mGlobalSettings.getInt("device_provisioned", 0) != 0;
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public boolean isUserSetup(int i) {
        return this.mSecureSettings.getIntForUser("user_setup_complete", 0, i) != 0;
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public int getCurrentUser() {
        return ActivityManager.getCurrentUser();
    }

    public void addCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        this.mListeners.add(deviceProvisionedListener);
        if (this.mListeners.size() == 1) {
            startListening(getCurrentUser());
        }
        deviceProvisionedListener.onUserSetupChanged();
        deviceProvisionedListener.onDeviceProvisionedChanged();
    }

    public void removeCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        this.mListeners.remove(deviceProvisionedListener);
        if (this.mListeners.size() == 0) {
            stopListening();
        }
    }

    protected void startListening(int i) {
        this.mGlobalSettings.registerContentObserverForUser(this.mDeviceProvisionedUri, true, this.mSettingsObserver, 0);
        this.mSecureSettings.registerContentObserverForUser(this.mUserSetupUri, true, this.mSettingsObserver, i);
        startTracking();
    }

    protected void stopListening() {
        stopTracking();
        this.mGlobalSettings.unregisterContentObserver(this.mSettingsObserver);
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        this.mGlobalSettings.unregisterContentObserver(this.mSettingsObserver);
        this.mGlobalSettings.registerContentObserverForUser(this.mDeviceProvisionedUri, true, this.mSettingsObserver, 0);
        this.mSecureSettings.registerContentObserverForUser(this.mUserSetupUri, true, this.mSettingsObserver, i);
        notifyUserChanged();
    }

    private void notifyUserChanged() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onUserSwitched();
        }
    }

    /* access modifiers changed from: private */
    public void notifySetupChanged() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onUserSetupChanged();
        }
    }

    /* access modifiers changed from: private */
    public void notifyProvisionedChanged() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onDeviceProvisionedChanged();
        }
    }
}
