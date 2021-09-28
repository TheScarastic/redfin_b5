package com.android.systemui;

import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.view.Choreographer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class DejankUtils {
    public static final boolean STRICT_MODE_ENABLED;
    private static final Runnable sAnimationCallbackRunnable;
    private static Stack<String> sBlockingIpcs;
    private static final Choreographer sChoreographer;
    private static final Handler sHandler;
    private static boolean sImmediate;
    private static final Object sLock;
    private static final ArrayList<Runnable> sPendingRunnables;
    private static final Binder.ProxyTransactListener sProxy;
    private static boolean sTemporarilyIgnoreStrictMode;
    private static final HashSet<String> sWhitelistedFrameworkClasses;

    static {
        boolean z = false;
        if (Build.IS_ENG || SystemProperties.getBoolean("persist.sysui.strictmode", false)) {
            z = true;
        }
        STRICT_MODE_ENABLED = z;
        sChoreographer = Choreographer.getInstance();
        sHandler = new Handler();
        sPendingRunnables = new ArrayList<>();
        sBlockingIpcs = new Stack<>();
        HashSet<String> hashSet = new HashSet<>();
        sWhitelistedFrameworkClasses = hashSet;
        sLock = new Object();
        AnonymousClass1 r2 = new Binder.ProxyTransactListener() { // from class: com.android.systemui.DejankUtils.1
            public void onTransactEnded(Object obj) {
            }

            public Object onTransactStarted(IBinder iBinder, int i) {
                return null;
            }

            public Object onTransactStarted(IBinder iBinder, int i, int i2) {
                String interfaceDescriptor;
                synchronized (DejankUtils.sLock) {
                    if ((i2 & 1) != 1) {
                        if (!DejankUtils.sBlockingIpcs.empty() && ThreadUtils.isMainThread() && !DejankUtils.sTemporarilyIgnoreStrictMode) {
                            try {
                                interfaceDescriptor = iBinder.getInterfaceDescriptor();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            synchronized (DejankUtils.sLock) {
                                if (DejankUtils.sWhitelistedFrameworkClasses.contains(interfaceDescriptor)) {
                                    return null;
                                }
                                StrictMode.noteSlowCall("IPC detected on critical path: " + ((String) DejankUtils.sBlockingIpcs.peek()));
                                return null;
                            }
                        }
                    }
                    return null;
                }
            }
        };
        sProxy = r2;
        if (z) {
            hashSet.add("android.view.IWindowSession");
            hashSet.add("com.android.internal.policy.IKeyguardStateCallback");
            hashSet.add("android.os.IPowerManager");
            hashSet.add("com.android.internal.statusbar.IStatusBarService");
            Binder.setProxyTransactListener(r2);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectCustomSlowCalls().penaltyFlashScreen().penaltyLog().build());
        }
        sAnimationCallbackRunnable = DejankUtils$$ExternalSyntheticLambda0.INSTANCE;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
        int i = 0;
        while (true) {
            ArrayList<Runnable> arrayList = sPendingRunnables;
            if (i < arrayList.size()) {
                sHandler.post(arrayList.get(i));
                i++;
            } else {
                arrayList.clear();
                return;
            }
        }
    }

    public static void startDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.push(str);
            }
        }
    }

    public static void stopDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.remove(str);
            }
        }
    }

    public static void whitelistIpcs(Runnable runnable) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            runnable.run();
            return;
        }
        Object obj = sLock;
        synchronized (obj) {
            try {
                sTemporarilyIgnoreStrictMode = true;
            } catch (Throwable th) {
                throw th;
            }
        }
        try {
            runnable.run();
            synchronized (obj) {
                try {
                    sTemporarilyIgnoreStrictMode = false;
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        } catch (Throwable th3) {
            synchronized (sLock) {
                try {
                    sTemporarilyIgnoreStrictMode = false;
                    throw th3;
                } catch (Throwable th4) {
                    throw th4;
                }
            }
        }
    }

    public static <T> T whitelistIpcs(Supplier<T> supplier) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            return supplier.get();
        }
        Object obj = sLock;
        synchronized (obj) {
            try {
                sTemporarilyIgnoreStrictMode = true;
            } catch (Throwable th) {
                throw th;
            }
        }
        try {
            T t = supplier.get();
            synchronized (obj) {
                try {
                    sTemporarilyIgnoreStrictMode = false;
                } catch (Throwable th2) {
                    throw th2;
                }
            }
            return t;
        } catch (Throwable th3) {
            synchronized (sLock) {
                try {
                    sTemporarilyIgnoreStrictMode = false;
                    throw th3;
                } catch (Throwable th4) {
                    throw th4;
                }
            }
        }
    }

    public static void postAfterTraversal(Runnable runnable) {
        if (sImmediate) {
            runnable.run();
            return;
        }
        Assert.isMainThread();
        sPendingRunnables.add(runnable);
        postAnimationCallback();
    }

    public static void removeCallbacks(Runnable runnable) {
        Assert.isMainThread();
        sPendingRunnables.remove(runnable);
        sHandler.removeCallbacks(runnable);
    }

    private static void postAnimationCallback() {
        sChoreographer.postCallback(1, sAnimationCallbackRunnable, null);
    }

    @VisibleForTesting
    public static void setImmediate(boolean z) {
        sImmediate = z;
    }
}
