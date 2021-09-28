package com.android.wallpaper.module;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import com.android.wallpaper.module.PackageStatusNotifier;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class DefaultPackageStatusNotifier implements PackageStatusNotifier {
    public final Context mAppContext;
    public final LauncherApps mLauncherApps;
    public final Map<PackageStatusNotifier.Listener, ListenerWrapper> mListeners = new HashMap();

    /* loaded from: classes.dex */
    public static class ListenerWrapper extends LauncherApps.Callback {
        public final Context mAppContext;
        public final Intent mIntentFilter;
        public final PackageStatusNotifier.Listener mListener;

        public ListenerWrapper(Context context, String str, PackageStatusNotifier.Listener listener) {
            this.mAppContext = context.getApplicationContext();
            this.mIntentFilter = new Intent(str);
            this.mListener = listener;
        }

        public final boolean isValidPackage(String str) {
            this.mIntentFilter.setPackage(str);
            PackageManager packageManager = this.mAppContext.getPackageManager();
            if (!packageManager.queryIntentServices(this.mIntentFilter, 0).isEmpty() || !packageManager.queryIntentActivities(this.mIntentFilter, 0).isEmpty()) {
                return true;
            }
            return false;
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageAdded(String str, UserHandle userHandle) {
            if (isValidPackage(str)) {
                this.mListener.onPackageChanged(str, 1);
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageChanged(String str, UserHandle userHandle) {
            if (isValidPackage(str)) {
                this.mListener.onPackageChanged(str, 2);
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackageRemoved(String str, UserHandle userHandle) {
            this.mListener.onPackageChanged(str, 3);
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesAvailable(String[] strArr, UserHandle userHandle, boolean z) {
            for (String str : strArr) {
                if (isValidPackage(str)) {
                    this.mListener.onPackageChanged(str, z ? 2 : 1);
                }
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesSuspended(String[] strArr, UserHandle userHandle) {
            for (String str : strArr) {
                if (isValidPackage(str)) {
                    this.mListener.onPackageChanged(str, 3);
                }
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesUnavailable(String[] strArr, UserHandle userHandle, boolean z) {
            for (String str : strArr) {
                if (!z && isValidPackage(str)) {
                    this.mListener.onPackageChanged(str, 3);
                }
            }
        }

        @Override // android.content.pm.LauncherApps.Callback
        public void onPackagesUnsuspended(String[] strArr, UserHandle userHandle) {
            for (String str : strArr) {
                if (isValidPackage(str)) {
                    this.mListener.onPackageChanged(str, 1);
                }
            }
        }
    }

    public DefaultPackageStatusNotifier(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mLauncherApps = (LauncherApps) context.getSystemService("launcherapps");
    }

    public void addListener(PackageStatusNotifier.Listener listener, String str) {
        ListenerWrapper listenerWrapper = new ListenerWrapper(this.mAppContext, str, listener);
        ListenerWrapper remove = this.mListeners.remove(listener);
        if (remove != null) {
            this.mLauncherApps.unregisterCallback(remove);
        }
        this.mListeners.put(listener, listenerWrapper);
        this.mLauncherApps.registerCallback(listenerWrapper);
    }

    public void removeListener(PackageStatusNotifier.Listener listener) {
        ListenerWrapper remove = this.mListeners.remove(listener);
        if (remove != null) {
            this.mLauncherApps.unregisterCallback(remove);
        }
    }
}
