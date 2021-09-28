package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.R$string;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.util.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
/* loaded from: classes.dex */
public class CastControllerImpl implements CastController {
    private static final boolean DEBUG = Log.isLoggable("CastController", 3);
    private boolean mCallbackRegistered;
    private final Context mContext;
    private boolean mDiscovering;
    private final MediaRouter mMediaRouter;
    private MediaProjectionInfo mProjection;
    private final MediaProjectionManager.Callback mProjectionCallback;
    private final MediaProjectionManager mProjectionManager;
    @GuardedBy({"mCallbacks"})
    private final ArrayList<CastController.Callback> mCallbacks = new ArrayList<>();
    private final ArrayMap<String, MediaRouter.RouteInfo> mRoutes = new ArrayMap<>();
    private final Object mDiscoveringLock = new Object();
    private final Object mProjectionLock = new Object();
    private final MediaRouter.SimpleCallback mMediaCallback = new MediaRouter.SimpleCallback() { // from class: com.android.systemui.statusbar.policy.CastControllerImpl.1
        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d("CastController", "onRouteAdded: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d("CastController", "onRouteChanged: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d("CastController", "onRouteRemoved: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d("CastController", "onRouteSelected(" + i + "): " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d("CastController", "onRouteUnselected(" + i + "): " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }
    };

    public CastControllerImpl(Context context, DumpManager dumpManager) {
        MediaProjectionManager.Callback r0 = new MediaProjectionManager.Callback() { // from class: com.android.systemui.statusbar.policy.CastControllerImpl.2
            public void onStart(MediaProjectionInfo mediaProjectionInfo) {
                CastControllerImpl.this.setProjection(mediaProjectionInfo, true);
            }

            public void onStop(MediaProjectionInfo mediaProjectionInfo) {
                CastControllerImpl.this.setProjection(mediaProjectionInfo, false);
            }
        };
        this.mProjectionCallback = r0;
        this.mContext = context;
        MediaRouter mediaRouter = (MediaRouter) context.getSystemService("media_router");
        this.mMediaRouter = mediaRouter;
        mediaRouter.setRouterGroupId("android.media.mirroring_group");
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService("media_projection");
        this.mProjectionManager = mediaProjectionManager;
        this.mProjection = mediaProjectionManager.getActiveProjectionInfo();
        mediaProjectionManager.addCallback(r0, new Handler());
        dumpManager.registerDumpable("CastController", this);
        if (DEBUG) {
            Log.d("CastController", "new CastController()");
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("CastController state:");
        printWriter.print("  mDiscovering=");
        printWriter.println(this.mDiscovering);
        printWriter.print("  mCallbackRegistered=");
        printWriter.println(this.mCallbackRegistered);
        printWriter.print("  mCallbacks.size=");
        synchronized (this.mCallbacks) {
            printWriter.println(this.mCallbacks.size());
        }
        printWriter.print("  mRoutes.size=");
        printWriter.println(this.mRoutes.size());
        for (int i = 0; i < this.mRoutes.size(); i++) {
            printWriter.print("    ");
            printWriter.println(routeToString(this.mRoutes.valueAt(i)));
        }
        printWriter.print("  mProjection=");
        printWriter.println(this.mProjection);
    }

    public void addCallback(CastController.Callback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.add(callback);
        }
        fireOnCastDevicesChanged(callback);
        synchronized (this.mDiscoveringLock) {
            handleDiscoveryChangeLocked();
        }
    }

    public void removeCallback(CastController.Callback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.remove(callback);
        }
        synchronized (this.mDiscoveringLock) {
            handleDiscoveryChangeLocked();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CastController
    public void setDiscovering(boolean z) {
        synchronized (this.mDiscoveringLock) {
            if (this.mDiscovering != z) {
                this.mDiscovering = z;
                if (DEBUG) {
                    Log.d("CastController", "setDiscovering: " + z);
                }
                handleDiscoveryChangeLocked();
            }
        }
    }

    private void handleDiscoveryChangeLocked() {
        boolean isEmpty;
        if (this.mCallbackRegistered) {
            this.mMediaRouter.removeCallback(this.mMediaCallback);
            this.mCallbackRegistered = false;
        }
        if (this.mDiscovering) {
            this.mMediaRouter.addCallback(4, this.mMediaCallback, 4);
            this.mCallbackRegistered = true;
            return;
        }
        synchronized (this.mCallbacks) {
            isEmpty = this.mCallbacks.isEmpty();
        }
        if (!isEmpty) {
            this.mMediaRouter.addCallback(4, this.mMediaCallback, 8);
            this.mCallbackRegistered = true;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CastController
    public void setCurrentUserId(int i) {
        this.mMediaRouter.rebindAsUser(i);
    }

    @Override // com.android.systemui.statusbar.policy.CastController
    public List<CastController.CastDevice> getCastDevices() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mRoutes) {
            for (MediaRouter.RouteInfo routeInfo : this.mRoutes.values()) {
                CastController.CastDevice castDevice = new CastController.CastDevice();
                castDevice.id = routeInfo.getTag().toString();
                CharSequence name = routeInfo.getName(this.mContext);
                String str = null;
                castDevice.name = name != null ? name.toString() : null;
                CharSequence description = routeInfo.getDescription();
                if (description != null) {
                    str = description.toString();
                }
                castDevice.description = str;
                int statusCode = routeInfo.getStatusCode();
                if (statusCode == 2) {
                    castDevice.state = 1;
                } else {
                    if (!routeInfo.isSelected() && statusCode != 6) {
                        castDevice.state = 0;
                    }
                    castDevice.state = 2;
                }
                castDevice.tag = routeInfo;
                arrayList.add(castDevice);
            }
        }
        synchronized (this.mProjectionLock) {
            if (this.mProjection != null) {
                CastController.CastDevice castDevice2 = new CastController.CastDevice();
                castDevice2.id = this.mProjection.getPackageName();
                castDevice2.name = getAppName(this.mProjection.getPackageName());
                castDevice2.description = this.mContext.getString(R$string.quick_settings_casting);
                castDevice2.state = 2;
                castDevice2.tag = this.mProjection;
                arrayList.add(castDevice2);
            }
        }
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.policy.CastController
    public void startCasting(CastController.CastDevice castDevice) {
        Object obj;
        if (castDevice != null && (obj = castDevice.tag) != null) {
            MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) obj;
            if (DEBUG) {
                Log.d("CastController", "startCasting: " + routeToString(routeInfo));
            }
            this.mMediaRouter.selectRoute(4, routeInfo);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CastController
    public void stopCasting(CastController.CastDevice castDevice) {
        boolean z = castDevice.tag instanceof MediaProjectionInfo;
        if (DEBUG) {
            Log.d("CastController", "stopCasting isProjection=" + z);
        }
        if (z) {
            MediaProjectionInfo mediaProjectionInfo = (MediaProjectionInfo) castDevice.tag;
            if (Objects.equals(this.mProjectionManager.getActiveProjectionInfo(), mediaProjectionInfo)) {
                this.mProjectionManager.stopActiveProjection();
                return;
            }
            Log.w("CastController", "Projection is no longer active: " + mediaProjectionInfo);
            return;
        }
        this.mMediaRouter.getFallbackRoute().select();
    }

    /* access modifiers changed from: private */
    public void setProjection(MediaProjectionInfo mediaProjectionInfo, boolean z) {
        boolean z2;
        MediaProjectionInfo mediaProjectionInfo2 = this.mProjection;
        synchronized (this.mProjectionLock) {
            boolean equals = Objects.equals(mediaProjectionInfo, this.mProjection);
            z2 = true;
            if (z && !equals) {
                this.mProjection = mediaProjectionInfo;
            } else if (z || !equals) {
                z2 = false;
            } else {
                this.mProjection = null;
            }
        }
        if (z2) {
            if (DEBUG) {
                Log.d("CastController", "setProjection: " + mediaProjectionInfo2 + " -> " + this.mProjection);
            }
            fireOnCastDevicesChanged();
        }
    }

    private String getAppName(String str) {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (Utils.isHeadlessRemoteDisplayProvider(packageManager, str)) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                CharSequence loadLabel = applicationInfo.loadLabel(packageManager);
                if (!TextUtils.isEmpty(loadLabel)) {
                    return loadLabel.toString();
                }
            }
            Log.w("CastController", "No label found for package: " + str);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("CastController", "Error getting appName for package: " + str, e);
        }
        return str;
    }

    /* access modifiers changed from: private */
    public void updateRemoteDisplays() {
        synchronized (this.mRoutes) {
            this.mRoutes.clear();
            int routeCount = this.mMediaRouter.getRouteCount();
            for (int i = 0; i < routeCount; i++) {
                MediaRouter.RouteInfo routeAt = this.mMediaRouter.getRouteAt(i);
                if (routeAt.isEnabled() && routeAt.matchesTypes(4)) {
                    ensureTagExists(routeAt);
                    this.mRoutes.put(routeAt.getTag().toString(), routeAt);
                }
            }
            MediaRouter.RouteInfo selectedRoute = this.mMediaRouter.getSelectedRoute(4);
            if (selectedRoute != null && !selectedRoute.isDefault()) {
                ensureTagExists(selectedRoute);
                this.mRoutes.put(selectedRoute.getTag().toString(), selectedRoute);
            }
        }
        fireOnCastDevicesChanged();
    }

    private void ensureTagExists(MediaRouter.RouteInfo routeInfo) {
        if (routeInfo.getTag() == null) {
            routeInfo.setTag(UUID.randomUUID().toString());
        }
    }

    void fireOnCastDevicesChanged() {
        synchronized (this.mCallbacks) {
            Iterator<CastController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                fireOnCastDevicesChanged(it.next());
            }
        }
    }

    private void fireOnCastDevicesChanged(CastController.Callback callback) {
        callback.onCastDevicesChanged();
    }

    /* access modifiers changed from: private */
    public static String routeToString(MediaRouter.RouteInfo routeInfo) {
        if (routeInfo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(routeInfo.getName());
        sb.append('/');
        sb.append(routeInfo.getDescription());
        sb.append('@');
        sb.append(routeInfo.getDeviceAddress());
        sb.append(",status=");
        sb.append(routeInfo.getStatus());
        if (routeInfo.isDefault()) {
            sb.append(",default");
        }
        if (routeInfo.isEnabled()) {
            sb.append(",enabled");
        }
        if (routeInfo.isConnecting()) {
            sb.append(",connecting");
        }
        if (routeInfo.isSelected()) {
            sb.append(",selected");
        }
        sb.append(",id=");
        sb.append(routeInfo.getTag());
        return sb.toString();
    }
}
