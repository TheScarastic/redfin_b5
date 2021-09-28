package com.android.systemui.assist;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$anim;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.Lazy;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class AssistManager {
    private final AssistDisclosure mAssistDisclosure;
    protected final AssistLogger mAssistLogger;
    protected final AssistUtils mAssistUtils;
    private final CommandQueue mCommandQueue;
    protected final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final AssistOrbController mOrbController;
    private final PhoneStateMonitor mPhoneStateMonitor;
    private IVoiceInteractionSessionShowCallback mShowCallback = new IVoiceInteractionSessionShowCallback.Stub() { // from class: com.android.systemui.assist.AssistManager.1
        public void onFailed() throws RemoteException {
            AssistManager.this.mOrbController.postHide();
        }

        public void onShown() throws RemoteException {
            AssistManager.this.mOrbController.postHide();
        }
    };
    protected final Lazy<SysUiState> mSysUiState;
    private final UiController mUiController;

    /* loaded from: classes.dex */
    public interface UiController {
        void hide();

        void onGestureCompletion(float f);

        void onInvocationProgress(int i, float f);
    }

    /* access modifiers changed from: protected */
    public final int toLoggingSubType(int i, int i2) {
        return (i << 1) | 0 | (i2 << 4);
    }

    public AssistManager(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, CommandQueue commandQueue, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, ConfigurationController configurationController, Lazy<SysUiState> lazy, DefaultUiController defaultUiController, AssistLogger assistLogger) {
        this.mContext = context;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mAssistUtils = assistUtils;
        this.mAssistDisclosure = new AssistDisclosure(context, new Handler());
        this.mPhoneStateMonitor = phoneStateMonitor;
        this.mAssistLogger = assistLogger;
        this.mOrbController = new AssistOrbController(configurationController, context);
        registerVoiceInteractionSessionListener();
        this.mUiController = defaultUiController;
        this.mSysUiState = lazy;
        overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.assist.AssistManager.2
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public void onAssistantProgress(float f) {
                AssistManager.this.onInvocationProgress(1, f);
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public void onAssistantGestureCompletion(float f) {
                AssistManager.this.onGestureCompletion(f);
            }
        });
    }

    protected void registerVoiceInteractionSessionListener() {
        this.mAssistUtils.registerVoiceInteractionSessionListener(new IVoiceInteractionSessionListener.Stub() { // from class: com.android.systemui.assist.AssistManager.3
            public void onVoiceSessionShown() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_UPDATE);
            }

            public void onVoiceSessionHidden() throws RemoteException {
                AssistManager.this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
            }

            public void onSetUiHints(Bundle bundle) {
                if ("set_assist_gesture_constrained".equals(bundle.getString("action"))) {
                    AssistManager.this.mSysUiState.get().setFlag(8192, bundle.getBoolean("should_constrain", false)).commitUpdate(0);
                }
            }
        });
    }

    protected boolean shouldShowOrb() {
        return !ActivityManager.isLowRamDeviceStatic();
    }

    public void startAssist(Bundle bundle) {
        ComponentName assistInfo = getAssistInfo();
        if (assistInfo != null) {
            boolean equals = assistInfo.equals(getVoiceInteractorComponentName());
            if (!equals || (!isVoiceSessionRunning() && shouldShowOrb())) {
                this.mOrbController.showOrb(assistInfo, equals);
                this.mOrbController.postHideDelayed(equals ? 2500 : 1000);
            }
            if (bundle == null) {
                bundle = new Bundle();
            }
            int i = bundle.getInt("invocation_type", 0);
            int phoneState = this.mPhoneStateMonitor.getPhoneState();
            bundle.putInt("invocation_phone_state", phoneState);
            bundle.putLong("invocation_time_ms", SystemClock.elapsedRealtime());
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, true, assistInfo, Integer.valueOf(phoneState));
            logStartAssistLegacy(i, phoneState);
            startAssistInternal(bundle, assistInfo, equals);
        }
    }

    public void onInvocationProgress(int i, float f) {
        this.mUiController.onInvocationProgress(i, f);
    }

    public void onGestureCompletion(float f) {
        this.mUiController.onGestureCompletion(f);
    }

    public void hideAssist() {
        this.mAssistUtils.hideCurrentSession();
    }

    private void startAssistInternal(Bundle bundle, ComponentName componentName, boolean z) {
        if (z) {
            startVoiceInteractor(bundle);
        } else {
            startAssistActivity(bundle, componentName);
        }
    }

    private void startAssistActivity(Bundle bundle, ComponentName componentName) {
        final Intent assistIntent;
        if (this.mDeviceProvisionedController.isDeviceProvisioned()) {
            boolean z = false;
            this.mCommandQueue.animateCollapsePanels(3, false);
            if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_structure_enabled", 1, -2) != 0) {
                z = true;
            }
            SearchManager searchManager = (SearchManager) this.mContext.getSystemService("search");
            if (searchManager != null && (assistIntent = searchManager.getAssistIntent(z)) != null) {
                assistIntent.setComponent(componentName);
                assistIntent.putExtras(bundle);
                if (z && AssistUtils.isDisclosureEnabled(this.mContext)) {
                    showDisclosure();
                }
                try {
                    final ActivityOptions makeCustomAnimation = ActivityOptions.makeCustomAnimation(this.mContext, R$anim.search_launch_enter, R$anim.search_launch_exit);
                    assistIntent.addFlags(268435456);
                    AsyncTask.execute(new Runnable() { // from class: com.android.systemui.assist.AssistManager.4
                        @Override // java.lang.Runnable
                        public void run() {
                            AssistManager.this.mContext.startActivityAsUser(assistIntent, makeCustomAnimation.toBundle(), new UserHandle(-2));
                        }
                    });
                } catch (ActivityNotFoundException unused) {
                    Log.w("AssistManager", "Activity not found for " + assistIntent.getAction());
                }
            }
        }
    }

    private void startVoiceInteractor(Bundle bundle) {
        this.mAssistUtils.showSessionForActiveService(bundle, 4, this.mShowCallback, (IBinder) null);
    }

    public void launchVoiceAssistFromKeyguard() {
        this.mAssistUtils.launchVoiceAssistFromKeyguard();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$canVoiceAssistBeLaunchedFromKeyguard$0() {
        return Boolean.valueOf(this.mAssistUtils.activeServiceSupportsLaunchFromKeyguard());
    }

    public boolean canVoiceAssistBeLaunchedFromKeyguard() {
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.assist.AssistManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return AssistManager.this.lambda$canVoiceAssistBeLaunchedFromKeyguard$0();
            }
        })).booleanValue();
    }

    public ComponentName getVoiceInteractorComponentName() {
        return this.mAssistUtils.getActiveServiceComponentName();
    }

    private boolean isVoiceSessionRunning() {
        return this.mAssistUtils.isSessionRunning();
    }

    public ComponentName getAssistInfoForUser(int i) {
        return this.mAssistUtils.getAssistComponentForUser(i);
    }

    private ComponentName getAssistInfo() {
        return getAssistInfoForUser(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showDisclosure() {
        this.mAssistDisclosure.postShow();
    }

    public void onLockscreenShown() {
        AsyncTask.execute(new Runnable() { // from class: com.android.systemui.assist.AssistManager.5
            @Override // java.lang.Runnable
            public void run() {
                AssistManager.this.mAssistUtils.onLockscreenShown();
            }
        });
    }

    public int toLoggingSubType(int i) {
        return toLoggingSubType(i, this.mPhoneStateMonitor.getPhoneState());
    }

    protected void logStartAssistLegacy(int i, int i2) {
        MetricsLogger.action(new LogMaker(1716).setType(1).setSubtype(toLoggingSubType(i, i2)));
    }
}
