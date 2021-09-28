package com.android.systemui.recents;

import android.app.trust.TrustManager;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.Dependency;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.Optional;
/* loaded from: classes.dex */
public class OverviewProxyRecentsImpl implements RecentsImplementation {
    private Context mContext;
    private Handler mHandler;
    private OverviewProxyService mOverviewProxyService;
    private final Lazy<StatusBar> mStatusBarLazy;
    private TrustManager mTrustManager;

    public OverviewProxyRecentsImpl(Optional<Lazy<StatusBar>> optional) {
        this.mStatusBarLazy = optional.orElse(null);
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public void onStart(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
        this.mTrustManager = (TrustManager) context.getSystemService("trust");
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public void showRecentApps(boolean z) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewShown(z);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview show event to launcher.", e);
            }
        }
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public void hideRecentApps(boolean z, boolean z2) {
        IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
        if (proxy != null) {
            try {
                proxy.onOverviewHidden(z, z2);
            } catch (RemoteException e) {
                Log.e("OverviewProxyRecentsImpl", "Failed to send overview hide event to launcher.", e);
            }
        }
    }

    @Override // com.android.systemui.recents.RecentsImplementation
    public void toggleRecentApps() {
        if (this.mOverviewProxyService.getProxy() != null) {
            OverviewProxyRecentsImpl$$ExternalSyntheticLambda0 overviewProxyRecentsImpl$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.systemui.recents.OverviewProxyRecentsImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OverviewProxyRecentsImpl.m199$r8$lambda$7C70YxacMEOtydiJgffqcvlF8(OverviewProxyRecentsImpl.this);
                }
            };
            Lazy<StatusBar> lazy = this.mStatusBarLazy;
            if (lazy == null || !lazy.get().isKeyguardShowing()) {
                overviewProxyRecentsImpl$$ExternalSyntheticLambda0.run();
            } else {
                this.mStatusBarLazy.get().executeRunnableDismissingKeyguard(new Runnable(overviewProxyRecentsImpl$$ExternalSyntheticLambda0) { // from class: com.android.systemui.recents.OverviewProxyRecentsImpl$$ExternalSyntheticLambda1
                    public final /* synthetic */ Runnable f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        OverviewProxyRecentsImpl.this.lambda$toggleRecentApps$1(this.f$1);
                    }
                }, null, true, false, true);
            }
        }
    }

    public /* synthetic */ void lambda$toggleRecentApps$0() {
        try {
            if (this.mOverviewProxyService.getProxy() != null) {
                this.mOverviewProxyService.getProxy().onOverviewToggle();
                this.mOverviewProxyService.notifyToggleRecentApps();
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyRecentsImpl", "Cannot send toggle recents through proxy service.", e);
        }
    }

    public /* synthetic */ void lambda$toggleRecentApps$1(Runnable runnable) {
        this.mTrustManager.reportKeyguardShowingChanged();
        this.mHandler.post(runnable);
    }
}
