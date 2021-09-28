package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkScoreManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.CellSignalStrength;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.SignalIcon$MobileState;
import com.android.settingslib.Utils;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.WifiSignalController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class NetworkControllerImpl extends BroadcastReceiver implements NetworkController, DemoMode, DataUsageController.NetworkNameProvider, Dumpable {
    private final AccessPointControllerImpl mAccessPoints;
    private int mActiveMobileDataSubscription;
    private boolean mAirplaneMode;
    private final Executor mBgExecutor;
    private final Looper mBgLooper;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CallbackHandler mCallbackHandler;
    private final CarrierConfigTracker mCarrierConfigTracker;
    private final Runnable mClearForceValidated;
    private MobileMappings.Config mConfig;
    private ConfigurationController.ConfigurationListener mConfigurationListener;
    private final BitSet mConnectedTransports;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private List<SubscriptionInfo> mCurrentSubscriptions;
    private int mCurrentUserId;
    private final DataSaverController mDataSaverController;
    private final DataUsageController mDataUsageController;
    private MobileSignalController mDefaultSignalController;
    private boolean mDemoInetCondition;
    private final DemoModeController mDemoModeController;
    private WifiSignalController.WifiState mDemoWifiState;
    private final DumpManager mDumpManager;
    private int mEmergencySource;
    @VisibleForTesting
    final EthernetSignalController mEthernetSignalController;
    private final FeatureFlags mFeatureFlags;
    private boolean mForceCellularValidated;
    private final boolean mHasMobileDataFeature;
    private boolean mHasNoSubs;
    private final String[] mHistory;
    private int mHistoryIndex;
    private boolean mInetCondition;
    private boolean mIsEmergency;
    private NetworkCapabilities mLastDefaultNetworkCapabilities;
    @VisibleForTesting
    ServiceState mLastServiceState;
    @VisibleForTesting
    boolean mListening;
    private Locale mLocale;
    private final Object mLock;
    @VisibleForTesting
    final SparseArray<MobileSignalController> mMobileSignalControllers;
    private boolean mNoDefaultNetwork;
    private boolean mNoNetworksAvailable;
    private final TelephonyManager mPhone;
    private TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    private final boolean mProviderModelBehavior;
    private final boolean mProviderModelSetting;
    private final Handler mReceiverHandler;
    private final Runnable mRegisterListeners;
    private boolean mSimDetected;
    private final MobileStatusTracker.SubscriptionDefaults mSubDefaults;
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionListener;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private boolean mUserSetup;
    private final CurrentUserTracker mUserTracker;
    private final BitSet mValidatedTransports;
    private final WifiManager mWifiManager;
    @VisibleForTesting
    final WifiSignalController mWifiSignalController;
    static final boolean DEBUG = Log.isLoggable("NetworkController", 3);
    static final boolean CHATTY = Log.isLoggable("NetworkControllerChat", 3);
    private static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    public NetworkControllerImpl(Context context, Looper looper, Executor executor, SubscriptionManager subscriptionManager, CallbackHandler callbackHandler, DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, NetworkScoreManager networkScoreManager, AccessPointControllerImpl accessPointControllerImpl, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, DumpManager dumpManager) {
        this(context, connectivityManager, telephonyManager, telephonyListenerManager, wifiManager, networkScoreManager, subscriptionManager, MobileMappings.Config.readConfig(context), looper, executor, callbackHandler, accessPointControllerImpl, new DataUsageController(context), new MobileStatusTracker.SubscriptionDefaults(), deviceProvisionedController, broadcastDispatcher, demoModeController, carrierConfigTracker, featureFlags, dumpManager);
        this.mReceiverHandler.post(this.mRegisterListeners);
    }

    @VisibleForTesting
    NetworkControllerImpl(Context context, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, NetworkScoreManager networkScoreManager, SubscriptionManager subscriptionManager, MobileMappings.Config config, Looper looper, Executor executor, CallbackHandler callbackHandler, AccessPointControllerImpl accessPointControllerImpl, DataUsageController dataUsageController, MobileStatusTracker.SubscriptionDefaults subscriptionDefaults, final DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, DumpManager dumpManager) {
        Handler handler;
        this.mLock = new Object();
        this.mActiveMobileDataSubscription = -1;
        this.mMobileSignalControllers = new SparseArray<>();
        this.mConnectedTransports = new BitSet();
        this.mValidatedTransports = new BitSet();
        this.mAirplaneMode = false;
        this.mNoDefaultNetwork = false;
        this.mNoNetworksAvailable = true;
        this.mLocale = null;
        this.mCurrentSubscriptions = new ArrayList();
        this.mHistory = new String[16];
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                NetworkControllerImpl networkControllerImpl = NetworkControllerImpl.this;
                networkControllerImpl.mConfig = MobileMappings.Config.readConfig(networkControllerImpl.mContext);
                NetworkControllerImpl.this.mReceiverHandler.post(new NetworkControllerImpl$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onConfigChanged$0() {
                NetworkControllerImpl.this.handleConfigurationChanged();
            }
        };
        this.mClearForceValidated = new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.lambda$new$2();
            }
        };
        this.mRegisterListeners = new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.lambda$new$5();
            }
        };
        this.mContext = context;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mConfig = config;
        Handler handler2 = new Handler(looper);
        this.mReceiverHandler = handler2;
        this.mBgLooper = looper;
        this.mBgExecutor = executor;
        this.mCallbackHandler = callbackHandler;
        this.mDataSaverController = new DataSaverControllerImpl(context);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mSubscriptionManager = subscriptionManager;
        this.mSubDefaults = subscriptionDefaults;
        this.mConnectivityManager = connectivityManager;
        boolean isDataCapable = telephonyManager.isDataCapable();
        this.mHasMobileDataFeature = isDataCapable;
        this.mDemoModeController = demoModeController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mFeatureFlags = featureFlags;
        this.mDumpManager = dumpManager;
        this.mPhone = telephonyManager;
        this.mWifiManager = wifiManager;
        this.mLocale = context.getResources().getConfiguration().locale;
        this.mAccessPoints = accessPointControllerImpl;
        this.mDataUsageController = dataUsageController;
        dataUsageController.setNetworkController(this);
        dataUsageController.setCallback(new DataUsageController.Callback() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.2
            @Override // com.android.settingslib.net.DataUsageController.Callback
            public void onMobileDataEnabled(boolean z) {
                NetworkControllerImpl.this.mCallbackHandler.setMobileDataEnabled(z);
                NetworkControllerImpl.this.notifyControllersMobileDataChanged();
            }
        });
        this.mWifiSignalController = new WifiSignalController(context, isDataCapable, callbackHandler, this, wifiManager, connectivityManager, networkScoreManager, featureFlags);
        this.mEthernetSignalController = new EthernetSignalController(context, callbackHandler, this);
        updateAirplaneMode(true);
        AnonymousClass3 r0 = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.3
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                NetworkControllerImpl.this.onUserSwitched(i);
            }
        };
        this.mUserTracker = r0;
        r0.startTracking();
        deviceProvisionedController.addCallback(new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.4
            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public void onUserSetupChanged() {
                NetworkControllerImpl networkControllerImpl = NetworkControllerImpl.this;
                DeviceProvisionedController deviceProvisionedController2 = deviceProvisionedController;
                networkControllerImpl.setUserSetupComplete(deviceProvisionedController2.isUserSetup(deviceProvisionedController2.getCurrentUser()));
            }
        });
        AnonymousClass5 r02 = new WifiManager.ScanResultsCallback() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.5
            @Override // android.net.wifi.WifiManager.ScanResultsCallback
            public void onScanResultsAvailable() {
                NetworkControllerImpl.this.mNoNetworksAvailable = true;
                Iterator<ScanResult> it = NetworkControllerImpl.this.mWifiManager.getScanResults().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (!it.next().SSID.equals(NetworkControllerImpl.this.mWifiSignalController.getState().ssid)) {
                            NetworkControllerImpl.this.mNoNetworksAvailable = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (NetworkControllerImpl.this.mNoDefaultNetwork) {
                    NetworkControllerImpl.this.mCallbackHandler.setConnectivityStatus(NetworkControllerImpl.this.mNoDefaultNetwork, true ^ NetworkControllerImpl.this.mInetCondition, NetworkControllerImpl.this.mNoNetworksAvailable);
                }
            }
        };
        if (wifiManager != null) {
            Objects.requireNonNull(handler2);
            handler = handler2;
            wifiManager.registerScanResultsCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), r02);
        } else {
            handler = handler2;
        }
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(1) { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.6
            private Network mLastNetwork;
            private NetworkCapabilities mLastNetworkCapabilities;

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                this.mLastNetwork = null;
                this.mLastNetworkCapabilities = null;
                NetworkControllerImpl.this.mLastDefaultNetworkCapabilities = null;
                NetworkControllerImpl.this.recordLastNetworkCallback(NetworkControllerImpl.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onLost: network=" + network);
                NetworkControllerImpl.this.updateConnectivity();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                int[] iArr;
                NetworkCapabilities networkCapabilities2 = this.mLastNetworkCapabilities;
                boolean z = networkCapabilities2 != null && networkCapabilities2.hasCapability(16);
                boolean hasCapability = networkCapabilities.hasCapability(16);
                if (network.equals(this.mLastNetwork) && hasCapability == z) {
                    int[] processedTransportTypes = NetworkControllerImpl.this.getProcessedTransportTypes(networkCapabilities);
                    Arrays.sort(processedTransportTypes);
                    NetworkCapabilities networkCapabilities3 = this.mLastNetworkCapabilities;
                    if (networkCapabilities3 != null) {
                        iArr = NetworkControllerImpl.this.getProcessedTransportTypes(networkCapabilities3);
                    } else {
                        iArr = null;
                    }
                    if (iArr != null) {
                        Arrays.sort(iArr);
                    }
                    if (Arrays.equals(processedTransportTypes, iArr)) {
                        return;
                    }
                }
                this.mLastNetwork = network;
                this.mLastNetworkCapabilities = networkCapabilities;
                NetworkControllerImpl.this.mLastDefaultNetworkCapabilities = networkCapabilities;
                NetworkControllerImpl.this.recordLastNetworkCallback(NetworkControllerImpl.SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",onCapabilitiesChanged: network=" + network + ",networkCapabilities=" + networkCapabilities);
                NetworkControllerImpl.this.updateConnectivity();
            }
        }, handler);
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda0
            public final void onActiveDataSubscriptionIdChanged(int i) {
                NetworkControllerImpl.this.lambda$new$1(i);
            }
        };
        demoModeController.addCallback((DemoMode) this);
        this.mProviderModelBehavior = featureFlags.isCombinedStatusBarSignalIconsEnabled();
        this.mProviderModelSetting = featureFlags.isProviderModelSettingEnabled();
        dumpManager.registerDumpable("NetworkController", this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        this.mBgExecutor.execute(new Runnable(i) { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda7
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.lambda$new$0(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        if (keepCellularValidationBitInSwitch(this.mActiveMobileDataSubscription, i)) {
            if (DEBUG) {
                Log.d("NetworkController", ": mForceCellularValidated to true.");
            }
            this.mForceCellularValidated = true;
            this.mReceiverHandler.removeCallbacks(this.mClearForceValidated);
            this.mReceiverHandler.postDelayed(this.mClearForceValidated, 2000);
        }
        this.mActiveMobileDataSubscription = i;
        doUpdateMobileControllers();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (DEBUG) {
            Log.d("NetworkController", ": mClearForceValidated");
        }
        this.mForceCellularValidated = false;
        updateConnectivity();
    }

    boolean isInGroupDataSwitch(int i, int i2) {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        SubscriptionInfo activeSubscriptionInfo2 = this.mSubscriptionManager.getActiveSubscriptionInfo(i2);
        return (activeSubscriptionInfo == null || activeSubscriptionInfo2 == null || activeSubscriptionInfo.getGroupUuid() == null || !activeSubscriptionInfo.getGroupUuid().equals(activeSubscriptionInfo2.getGroupUuid())) ? false : true;
    }

    boolean keepCellularValidationBitInSwitch(int i, int i2) {
        if (!this.mValidatedTransports.get(0) || !isInGroupDataSwitch(i, i2)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public DataSaverController getDataSaverController() {
        return this.mDataSaverController;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* renamed from: registerListeners */
    public void lambda$new$5() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).registerListener();
        }
        if (this.mSubscriptionListener == null) {
            this.mSubscriptionListener = new SubListener(this.mBgLooper);
        }
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, this.mReceiverHandler);
        this.mListening = true;
        this.mReceiverHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.updateConnectivity();
            }
        });
        Handler handler = this.mReceiverHandler;
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        Objects.requireNonNull(wifiSignalController);
        handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                WifiSignalController.this.fetchInitialState();
            }
        });
        this.mReceiverHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.lambda$registerListeners$3();
            }
        });
        updateMobileControllers();
        this.mReceiverHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.recalculateEmergency();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerListeners$3() {
        if (this.mLastServiceState == null) {
            this.mLastServiceState = this.mPhone.getServiceState();
            if (this.mMobileSignalControllers.size() == 0) {
                recalculateEmergency();
            }
        }
    }

    private void unregisterListeners() {
        this.mListening = false;
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).unregisterListener();
        }
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionListener);
        this.mBroadcastDispatcher.unregisterReceiver(this);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public DataUsageController getMobileDataController() {
        return this.mDataUsageController;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public boolean hasMobileDataFeature() {
        return this.mHasMobileDataFeature;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public boolean hasVoiceCallingFeature() {
        return this.mPhone.getPhoneType() != 0;
    }

    /* access modifiers changed from: private */
    public int[] getProcessedTransportTypes(NetworkCapabilities networkCapabilities) {
        int[] transportTypes = networkCapabilities.getTransportTypes();
        int i = 0;
        while (true) {
            if (i < transportTypes.length) {
                if (transportTypes[i] == 0 && Utils.tryGetWifiInfoForVcn(networkCapabilities) != null) {
                    transportTypes[i] = 1;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return transportTypes;
    }

    private MobileSignalController getDataController() {
        return getControllerWithSubId(this.mSubDefaults.getActiveDataSubId());
    }

    private MobileSignalController getControllerWithSubId(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            if (DEBUG) {
                Log.e("NetworkController", "No data sim selected");
            }
            return this.mDefaultSignalController;
        } else if (this.mMobileSignalControllers.indexOfKey(i) >= 0) {
            return this.mMobileSignalControllers.get(i);
        } else {
            if (DEBUG) {
                Log.e("NetworkController", "Cannot find controller for data sub: " + i);
            }
            return this.mDefaultSignalController;
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController, com.android.settingslib.net.DataUsageController.NetworkNameProvider
    public String getMobileDataNetworkName() {
        MobileSignalController dataController = getDataController();
        return dataController != null ? dataController.getState().networkNameData : "";
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public boolean isMobileDataNetworkInService() {
        MobileSignalController dataController = getDataController();
        return dataController != null && dataController.isInService();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public int getNumberSubscriptions() {
        return this.mMobileSignalControllers.size();
    }

    /* access modifiers changed from: package-private */
    public boolean isDataControllerDisabled() {
        MobileSignalController dataController = getDataController();
        if (dataController == null) {
            return false;
        }
        return dataController.isDataDisabled();
    }

    /* access modifiers changed from: package-private */
    public boolean isCarrierMergedWifi(int i) {
        return this.mWifiSignalController.isCarrierMergedWifi(i);
    }

    /* access modifiers changed from: package-private */
    public boolean hasDefaultNetwork() {
        return !this.mNoDefaultNetwork;
    }

    /* access modifiers changed from: package-private */
    public boolean isEthernetDefault() {
        return this.mConnectedTransports.get(3);
    }

    /* access modifiers changed from: package-private */
    public String getNetworkNameForCarrierWiFi(int i) {
        MobileSignalController controllerWithSubId = getControllerWithSubId(i);
        return controllerWithSubId != null ? controllerWithSubId.getNetworkNameForCarrierWiFi() : "";
    }

    /* access modifiers changed from: package-private */
    public void notifyWifiLevelChange(int i) {
        for (int i2 = 0; i2 < this.mMobileSignalControllers.size(); i2++) {
            this.mMobileSignalControllers.valueAt(i2).notifyWifiLevelChange(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyDefaultMobileLevelChange(int i) {
        for (int i2 = 0; i2 < this.mMobileSignalControllers.size(); i2++) {
            this.mMobileSignalControllers.valueAt(i2).notifyDefaultMobileLevelChange(i);
        }
    }

    /* access modifiers changed from: private */
    public void notifyControllersMobileDataChanged() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).onMobileDataChanged();
        }
    }

    public boolean isEmergencyOnly() {
        if (this.mMobileSignalControllers.size() == 0) {
            this.mEmergencySource = 0;
            ServiceState serviceState = this.mLastServiceState;
            return serviceState != null && serviceState.isEmergencyOnly();
        }
        int defaultVoiceSubId = this.mSubDefaults.getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
                MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
                if (!valueAt.getState().isEmergency) {
                    this.mEmergencySource = valueAt.mSubscriptionInfo.getSubscriptionId() + 100;
                    if (DEBUG) {
                        Log.d("NetworkController", "Found emergency " + valueAt.mTag);
                    }
                    return false;
                }
            }
        }
        if (this.mMobileSignalControllers.indexOfKey(defaultVoiceSubId) >= 0) {
            this.mEmergencySource = defaultVoiceSubId + 200;
            if (DEBUG) {
                Log.d("NetworkController", "Getting emergency from " + defaultVoiceSubId);
            }
            return this.mMobileSignalControllers.get(defaultVoiceSubId).getState().isEmergency;
        } else if (this.mMobileSignalControllers.size() == 1) {
            this.mEmergencySource = this.mMobileSignalControllers.keyAt(0) + 400;
            if (DEBUG) {
                Log.d("NetworkController", "Getting assumed emergency from " + this.mMobileSignalControllers.keyAt(0));
            }
            return this.mMobileSignalControllers.valueAt(0).getState().isEmergency;
        } else {
            if (DEBUG) {
                Log.e("NetworkController", "Cannot find controller for voice sub: " + defaultVoiceSubId);
            }
            this.mEmergencySource = defaultVoiceSubId + 300;
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void recalculateEmergency() {
        boolean isEmergencyOnly = isEmergencyOnly();
        this.mIsEmergency = isEmergencyOnly;
        this.mCallbackHandler.setEmergencyCallsOnly(isEmergencyOnly);
    }

    public void addCallback(NetworkController.SignalCallback signalCallback) {
        signalCallback.setSubs(this.mCurrentSubscriptions);
        signalCallback.setIsAirplaneMode(new NetworkController.IconState(this.mAirplaneMode, TelephonyIcons.FLIGHT_MODE_ICON, R$string.accessibility_airplane_mode, this.mContext));
        signalCallback.setNoSims(this.mHasNoSubs, this.mSimDetected);
        if (this.mProviderModelSetting) {
            signalCallback.setConnectivityStatus(this.mNoDefaultNetwork, !this.mInetCondition, this.mNoNetworksAvailable);
        }
        this.mWifiSignalController.notifyListeners(signalCallback);
        this.mEthernetSignalController.notifyListeners(signalCallback);
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.notifyListeners(signalCallback);
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(signalCallback);
            }
        }
        this.mCallbackHandler.setListening(signalCallback, true);
    }

    public void removeCallback(NetworkController.SignalCallback signalCallback) {
        this.mCallbackHandler.setListening(signalCallback, false);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public void setWifiEnabled(final boolean z) {
        new AsyncTask<Void, Void, Void>() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.7
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                NetworkControllerImpl.this.mWifiManager.setWifiEnabled(z);
                return null;
            }
        }.execute(new Void[0]);
    }

    /* access modifiers changed from: private */
    public void onUserSwitched(int i) {
        this.mCurrentUserId = i;
        this.mAccessPoints.onUserSwitched(i);
        updateConnectivity();
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        char c;
        if (CHATTY) {
            Log.d("NetworkController", "onReceive: intent=" + intent);
        }
        String action = intent.getAction();
        action.hashCode();
        switch (action.hashCode()) {
            case -2104353374:
                if (action.equals("android.intent.action.SERVICE_STATE")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1465084191:
                if (action.equals("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1172645946:
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1138588223:
                if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1076576821:
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -229777127:
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -25388475:
                if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.mLastServiceState = ServiceState.newFromBundle(intent.getExtras());
                if (this.mMobileSignalControllers.size() == 0) {
                    recalculateEmergency();
                    return;
                }
                return;
            case 1:
                recalculateEmergency();
                return;
            case 2:
                updateConnectivity();
                return;
            case 3:
                this.mConfig = MobileMappings.Config.readConfig(this.mContext);
                this.mReceiverHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NetworkControllerImpl.this.handleConfigurationChanged();
                    }
                });
                return;
            case 4:
                refreshLocale();
                updateAirplaneMode(false);
                return;
            case 5:
                if (!intent.getBooleanExtra("rebroadcastOnUnlock", false)) {
                    updateMobileControllers();
                    return;
                }
                return;
            case 6:
                break;
            default:
                int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                if (!SubscriptionManager.isValidSubscriptionId(intExtra)) {
                    this.mWifiSignalController.handleBroadcast(intent);
                    return;
                } else if (this.mMobileSignalControllers.indexOfKey(intExtra) >= 0) {
                    this.mMobileSignalControllers.get(intExtra).handleBroadcast(intent);
                    return;
                } else {
                    updateMobileControllers();
                    return;
                }
        }
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).handleBroadcast(intent);
        }
        this.mConfig = MobileMappings.Config.readConfig(this.mContext);
        this.mReceiverHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.handleConfigurationChanged();
            }
        });
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleConfigurationChanged() {
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.setConfiguration(this.mConfig);
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(this.mCallbackHandler);
            }
        }
        refreshLocale();
    }

    /* access modifiers changed from: private */
    public void updateMobileControllers() {
        if (this.mListening) {
            doUpdateMobileControllers();
        }
    }

    private void filterMobileSubscriptionInSameGroup(List<SubscriptionInfo> list) {
        if (list.size() == 2) {
            SubscriptionInfo subscriptionInfo = list.get(0);
            SubscriptionInfo subscriptionInfo2 = list.get(1);
            if (subscriptionInfo.getGroupUuid() != null && subscriptionInfo.getGroupUuid().equals(subscriptionInfo2.getGroupUuid())) {
                if (!subscriptionInfo.isOpportunistic() && !subscriptionInfo2.isOpportunistic()) {
                    return;
                }
                if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                    if (!subscriptionInfo.isOpportunistic()) {
                        subscriptionInfo = subscriptionInfo2;
                    }
                    list.remove(subscriptionInfo);
                    return;
                }
                if (subscriptionInfo.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                    subscriptionInfo = subscriptionInfo2;
                }
                list.remove(subscriptionInfo);
            }
        }
    }

    @VisibleForTesting
    void doUpdateMobileControllers() {
        List<SubscriptionInfo> completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList == null) {
            completeActiveSubscriptionInfoList = Collections.emptyList();
        }
        filterMobileSubscriptionInSameGroup(completeActiveSubscriptionInfoList);
        if (hasCorrectMobileControllers(completeActiveSubscriptionInfoList)) {
            updateNoSims();
            return;
        }
        synchronized (this.mLock) {
            setCurrentSubscriptionsLocked(completeActiveSubscriptionInfoList);
        }
        updateNoSims();
        recalculateEmergency();
    }

    @VisibleForTesting
    protected void updateNoSims() {
        boolean z = this.mHasMobileDataFeature && this.mMobileSignalControllers.size() == 0;
        boolean hasAnySim = hasAnySim();
        if (z != this.mHasNoSubs || hasAnySim != this.mSimDetected) {
            this.mHasNoSubs = z;
            this.mSimDetected = hasAnySim;
            this.mCallbackHandler.setNoSims(z, hasAnySim);
        }
    }

    private boolean hasAnySim() {
        int activeModemCount = this.mPhone.getActiveModemCount();
        for (int i = 0; i < activeModemCount; i++) {
            int simState = this.mPhone.getSimState(i);
            if (!(simState == 1 || simState == 0)) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    public void setCurrentSubscriptionsLocked(List<SubscriptionInfo> list) {
        SparseArray sparseArray;
        int i;
        int i2;
        List<SubscriptionInfo> list2;
        List<SubscriptionInfo> list3 = list;
        Collections.sort(list3, new Comparator<SubscriptionInfo>() { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl.8
            public int compare(SubscriptionInfo subscriptionInfo, SubscriptionInfo subscriptionInfo2) {
                int i3;
                int i4;
                if (subscriptionInfo.getSimSlotIndex() == subscriptionInfo2.getSimSlotIndex()) {
                    i4 = subscriptionInfo.getSubscriptionId();
                    i3 = subscriptionInfo2.getSubscriptionId();
                } else {
                    i4 = subscriptionInfo.getSimSlotIndex();
                    i3 = subscriptionInfo2.getSimSlotIndex();
                }
                return i4 - i3;
            }
        });
        this.mCurrentSubscriptions = list3;
        SparseArray sparseArray2 = new SparseArray();
        for (int i3 = 0; i3 < this.mMobileSignalControllers.size(); i3++) {
            sparseArray2.put(this.mMobileSignalControllers.keyAt(i3), this.mMobileSignalControllers.valueAt(i3));
        }
        this.mMobileSignalControllers.clear();
        int size = list.size();
        int i4 = 0;
        while (i4 < size) {
            int subscriptionId = list3.get(i4).getSubscriptionId();
            if (sparseArray2.indexOfKey(subscriptionId) >= 0) {
                this.mMobileSignalControllers.put(subscriptionId, (MobileSignalController) sparseArray2.get(subscriptionId));
                sparseArray2.remove(subscriptionId);
                i2 = i4;
                i = size;
                list2 = list3;
                sparseArray = sparseArray2;
            } else {
                sparseArray = sparseArray2;
                i = size;
                MobileSignalController mobileSignalController = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionId), this.mCallbackHandler, this, list3.get(i4), this.mSubDefaults, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
                mobileSignalController.setUserSetupComplete(this.mUserSetup);
                this.mMobileSignalControllers.put(subscriptionId, mobileSignalController);
                list2 = list;
                i2 = i4;
                if (list2.get(i2).getSimSlotIndex() == 0) {
                    this.mDefaultSignalController = mobileSignalController;
                }
                if (this.mListening) {
                    mobileSignalController.registerListener();
                }
            }
            i4 = i2 + 1;
            list3 = list2;
            size = i;
            sparseArray2 = sparseArray;
        }
        if (this.mListening) {
            int i5 = 0;
            for (SparseArray sparseArray3 = sparseArray2; i5 < sparseArray3.size(); sparseArray3 = sparseArray3) {
                int keyAt = sparseArray3.keyAt(i5);
                if (sparseArray3.get(keyAt) == this.mDefaultSignalController) {
                    this.mDefaultSignalController = null;
                }
                ((MobileSignalController) sparseArray3.get(keyAt)).unregisterListener();
                i5++;
            }
        }
        this.mCallbackHandler.setSubs(list3);
        notifyAllListeners();
        pushConnectivityToSignals();
        updateAirplaneMode(true);
    }

    /* access modifiers changed from: private */
    public void setUserSetupComplete(boolean z) {
        this.mReceiverHandler.post(new Runnable(z) { // from class: com.android.systemui.statusbar.policy.NetworkControllerImpl$$ExternalSyntheticLambda8
            public final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NetworkControllerImpl.this.lambda$setUserSetupComplete$4(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: handleSetUserSetupComplete */
    public void lambda$setUserSetupComplete$4(boolean z) {
        this.mUserSetup = z;
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).setUserSetupComplete(this.mUserSetup);
        }
    }

    @VisibleForTesting
    boolean hasCorrectMobileControllers(List<SubscriptionInfo> list) {
        if (list.size() != this.mMobileSignalControllers.size()) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : list) {
            if (this.mMobileSignalControllers.indexOfKey(subscriptionInfo.getSubscriptionId()) < 0) {
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    void setNoNetworksAvailable(boolean z) {
        this.mNoNetworksAvailable = z;
    }

    private void updateAirplaneMode(boolean z) {
        boolean z2 = true;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 1) {
            z2 = false;
        }
        if (z2 != this.mAirplaneMode || z) {
            this.mAirplaneMode = z2;
            for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
                this.mMobileSignalControllers.valueAt(i).setAirplaneMode(this.mAirplaneMode);
            }
            notifyListeners();
        }
    }

    private void refreshLocale() {
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mWifiSignalController.refreshLocale();
            notifyAllListeners();
        }
    }

    private void notifyAllListeners() {
        notifyListeners();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).notifyListeners();
        }
        this.mWifiSignalController.notifyListeners();
        this.mEthernetSignalController.notifyListeners();
    }

    private void notifyListeners() {
        this.mCallbackHandler.setIsAirplaneMode(new NetworkController.IconState(this.mAirplaneMode, TelephonyIcons.FLIGHT_MODE_ICON, R$string.accessibility_airplane_mode, this.mContext));
        this.mCallbackHandler.setNoSims(this.mHasNoSubs, this.mSimDetected);
    }

    /* access modifiers changed from: private */
    public void updateConnectivity() {
        this.mConnectedTransports.clear();
        this.mValidatedTransports.clear();
        NetworkCapabilities networkCapabilities = this.mLastDefaultNetworkCapabilities;
        boolean z = false;
        z = false;
        z = false;
        if (networkCapabilities != null) {
            int[] transportTypes = networkCapabilities.getTransportTypes();
            for (int i : transportTypes) {
                if (i == 0 || i == 1 || i == 3) {
                    if (i != 0 || Utils.tryGetWifiInfoForVcn(this.mLastDefaultNetworkCapabilities) == null) {
                        this.mConnectedTransports.set(i);
                        if (this.mLastDefaultNetworkCapabilities.hasCapability(16)) {
                            this.mValidatedTransports.set(i);
                        }
                    } else {
                        this.mConnectedTransports.set(1);
                        if (this.mLastDefaultNetworkCapabilities.hasCapability(16)) {
                            this.mValidatedTransports.set(1);
                        }
                    }
                }
            }
        }
        if (this.mForceCellularValidated) {
            this.mValidatedTransports.set(0);
        }
        if (CHATTY) {
            Log.d("NetworkController", "updateConnectivity: mConnectedTransports=" + this.mConnectedTransports);
            Log.d("NetworkController", "updateConnectivity: mValidatedTransports=" + this.mValidatedTransports);
        }
        this.mInetCondition = this.mValidatedTransports.get(0) || this.mValidatedTransports.get(1) || this.mValidatedTransports.get(3);
        pushConnectivityToSignals();
        if (this.mProviderModelBehavior) {
            boolean z2 = !this.mConnectedTransports.get(0) && !this.mConnectedTransports.get(1) && !this.mConnectedTransports.get(3);
            this.mNoDefaultNetwork = z2;
            this.mCallbackHandler.setConnectivityStatus(z2, true ^ this.mInetCondition, this.mNoNetworksAvailable);
            for (int i2 = 0; i2 < this.mMobileSignalControllers.size(); i2++) {
                this.mMobileSignalControllers.valueAt(i2).updateNoCallingState();
            }
            notifyAllListeners();
        } else if (this.mProviderModelSetting) {
            if (!this.mConnectedTransports.get(0) && !this.mConnectedTransports.get(1) && !this.mConnectedTransports.get(3)) {
                z = true;
            }
            this.mNoDefaultNetwork = z;
            this.mCallbackHandler.setConnectivityStatus(z, !this.mInetCondition, this.mNoNetworksAvailable);
        }
    }

    private void pushConnectivityToSignals() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
        }
        this.mWifiSignalController.updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
        this.mEthernetSignalController.updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NetworkController state:");
        printWriter.println("  - telephony ------");
        printWriter.print("  hasVoiceCallingFeature()=");
        printWriter.println(hasVoiceCallingFeature());
        printWriter.println("  mListening=" + this.mListening);
        printWriter.println("  - connectivity ------");
        printWriter.print("  mConnectedTransports=");
        printWriter.println(this.mConnectedTransports);
        printWriter.print("  mValidatedTransports=");
        printWriter.println(this.mValidatedTransports);
        printWriter.print("  mInetCondition=");
        printWriter.println(this.mInetCondition);
        printWriter.print("  mAirplaneMode=");
        printWriter.println(this.mAirplaneMode);
        printWriter.print("  mLocale=");
        printWriter.println(this.mLocale);
        printWriter.print("  mLastServiceState=");
        printWriter.println(this.mLastServiceState);
        printWriter.print("  mIsEmergency=");
        printWriter.println(this.mIsEmergency);
        printWriter.print("  mEmergencySource=");
        printWriter.println(emergencyToString(this.mEmergencySource));
        printWriter.println("  - DefaultNetworkCallback -----");
        int i = 0;
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 16;
        while (true) {
            i3--;
            if (i3 < (this.mHistoryIndex + 16) - i) {
                break;
            }
            printWriter.println("  Previous NetworkCallback(" + ((this.mHistoryIndex + 16) - i3) + "): " + this.mHistory[i3 & 15]);
        }
        printWriter.println("  - config ------");
        for (int i4 = 0; i4 < this.mMobileSignalControllers.size(); i4++) {
            this.mMobileSignalControllers.valueAt(i4).dump(printWriter);
        }
        this.mWifiSignalController.dump(printWriter);
        this.mEthernetSignalController.dump(printWriter);
        this.mAccessPoints.dump(printWriter);
        this.mCallbackHandler.dump(printWriter);
    }

    private static final String emergencyToString(int i) {
        if (i > 300) {
            return "ASSUMED_VOICE_CONTROLLER(" + (i - 200) + ")";
        } else if (i > 300) {
            return "NO_SUB(" + (i - 300) + ")";
        } else if (i > 200) {
            return "VOICE_CONTROLLER(" + (i - 200) + ")";
        } else if (i <= 100) {
            return i == 0 ? "NO_CONTROLLERS" : "UNKNOWN_SOURCE";
        } else {
            return "FIRST_CONTROLLER(" + (i - 100) + ")";
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeStarted() {
        if (DEBUG) {
            Log.d("NetworkController", "Entering demo mode");
        }
        unregisterListeners();
        this.mDemoInetCondition = this.mInetCondition;
        WifiSignalController.WifiState state = this.mWifiSignalController.getState();
        this.mDemoWifiState = state;
        state.ssid = "DemoMode";
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void onDemoModeFinished() {
        if (DEBUG) {
            Log.d("NetworkController", "Exiting demo mode");
        }
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).resetLastState();
        }
        this.mWifiSignalController.resetLastState();
        this.mReceiverHandler.post(this.mRegisterListeners);
        notifyAllListeners();
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public void dispatchDemoCommand(String str, Bundle bundle) {
        char c;
        int i;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup;
        char c2;
        int i2;
        if (this.mDemoModeController.isInDemoMode()) {
            String string = bundle.getString("airplane");
            if (string != null) {
                this.mCallbackHandler.setIsAirplaneMode(new NetworkController.IconState(string.equals("show"), TelephonyIcons.FLIGHT_MODE_ICON, R$string.accessibility_airplane_mode, this.mContext));
            }
            String string2 = bundle.getString("fully");
            if (string2 != null) {
                this.mDemoInetCondition = Boolean.parseBoolean(string2);
                BitSet bitSet = new BitSet();
                if (this.mDemoInetCondition) {
                    bitSet.set(this.mWifiSignalController.mTransportType);
                }
                this.mWifiSignalController.updateConnectivity(bitSet, bitSet);
                for (int i3 = 0; i3 < this.mMobileSignalControllers.size(); i3++) {
                    MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i3);
                    if (this.mDemoInetCondition) {
                        bitSet.set(valueAt.mTransportType);
                    }
                    valueAt.updateConnectivity(bitSet, bitSet);
                }
            }
            String string3 = bundle.getString("wifi");
            if (string3 != null) {
                boolean equals = string3.equals("show");
                String string4 = bundle.getString("level");
                if (string4 != null) {
                    WifiSignalController.WifiState wifiState = this.mDemoWifiState;
                    if (string4.equals("null")) {
                        i2 = -1;
                    } else {
                        i2 = Math.min(Integer.parseInt(string4), WifiIcons.WIFI_LEVEL_COUNT - 1);
                    }
                    wifiState.level = i2;
                    WifiSignalController.WifiState wifiState2 = this.mDemoWifiState;
                    wifiState2.connected = wifiState2.level >= 0;
                }
                String string5 = bundle.getString("activity");
                if (string5 != null) {
                    switch (string5.hashCode()) {
                        case 3365:
                            if (string5.equals("in")) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 110414:
                            if (string5.equals("out")) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 100357129:
                            if (string5.equals("inout")) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            this.mWifiSignalController.setActivity(1);
                            break;
                        case 1:
                            this.mWifiSignalController.setActivity(2);
                            break;
                        case 2:
                            this.mWifiSignalController.setActivity(3);
                            break;
                        default:
                            this.mWifiSignalController.setActivity(0);
                            break;
                    }
                } else {
                    this.mWifiSignalController.setActivity(0);
                }
                String string6 = bundle.getString("ssid");
                if (string6 != null) {
                    this.mDemoWifiState.ssid = string6;
                }
                this.mDemoWifiState.enabled = equals;
                this.mWifiSignalController.notifyListeners();
            }
            String string7 = bundle.getString("sims");
            if (string7 != null) {
                int constrain = MathUtils.constrain(Integer.parseInt(string7), 1, 8);
                ArrayList arrayList = new ArrayList();
                if (constrain != this.mMobileSignalControllers.size()) {
                    this.mMobileSignalControllers.clear();
                    int activeSubscriptionInfoCountMax = this.mSubscriptionManager.getActiveSubscriptionInfoCountMax();
                    for (int i4 = activeSubscriptionInfoCountMax; i4 < activeSubscriptionInfoCountMax + constrain; i4++) {
                        arrayList.add(addSignalController(i4, i4));
                    }
                    this.mCallbackHandler.setSubs(arrayList);
                    for (int i5 = 0; i5 < this.mMobileSignalControllers.size(); i5++) {
                        this.mMobileSignalControllers.get(this.mMobileSignalControllers.keyAt(i5)).notifyListeners();
                    }
                }
            }
            String string8 = bundle.getString("nosim");
            if (string8 != null) {
                boolean equals2 = string8.equals("show");
                this.mHasNoSubs = equals2;
                this.mCallbackHandler.setNoSims(equals2, this.mSimDetected);
            }
            String string9 = bundle.getString("mobile");
            if (string9 != null) {
                boolean equals3 = string9.equals("show");
                String string10 = bundle.getString("datatype");
                String string11 = bundle.getString("slot");
                int constrain2 = MathUtils.constrain(TextUtils.isEmpty(string11) ? 0 : Integer.parseInt(string11), 0, 8);
                ArrayList arrayList2 = new ArrayList();
                while (this.mMobileSignalControllers.size() <= constrain2) {
                    int size = this.mMobileSignalControllers.size();
                    arrayList2.add(addSignalController(size, size));
                }
                if (!arrayList2.isEmpty()) {
                    this.mCallbackHandler.setSubs(arrayList2);
                }
                MobileSignalController valueAt2 = this.mMobileSignalControllers.valueAt(constrain2);
                valueAt2.getState().dataSim = string10 != null;
                valueAt2.getState().isDefault = string10 != null;
                valueAt2.getState().dataConnected = string10 != null;
                if (string10 != null) {
                    SignalIcon$MobileState state = valueAt2.getState();
                    if (string10.equals("1x")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.ONE_X;
                    } else if (string10.equals("3g")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.THREE_G;
                    } else if (string10.equals("4g")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.FOUR_G;
                    } else if (string10.equals("4g+")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.FOUR_G_PLUS;
                    } else if (string10.equals("5g")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.NR_5G;
                    } else if (string10.equals("5ge")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.LTE_CA_5G_E;
                    } else if (string10.equals("5g+")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.NR_5G_PLUS;
                    } else if (string10.equals("e")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.E;
                    } else if (string10.equals("g")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.G;
                    } else if (string10.equals("h")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.H;
                    } else if (string10.equals("h+")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.H_PLUS;
                    } else if (string10.equals("lte")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.LTE;
                    } else if (string10.equals("lte+")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.LTE_PLUS;
                    } else if (string10.equals("dis")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.DATA_DISABLED;
                    } else if (string10.equals("not")) {
                        signalIcon$MobileIconGroup = TelephonyIcons.NOT_DEFAULT_DATA;
                    } else {
                        signalIcon$MobileIconGroup = TelephonyIcons.UNKNOWN;
                    }
                    state.iconGroup = signalIcon$MobileIconGroup;
                }
                if (bundle.containsKey("roam")) {
                    valueAt2.getState().roaming = "show".equals(bundle.getString("roam"));
                }
                String string12 = bundle.getString("level");
                if (string12 != null) {
                    SignalIcon$MobileState state2 = valueAt2.getState();
                    if (string12.equals("null")) {
                        i = -1;
                    } else {
                        i = Math.min(Integer.parseInt(string12), CellSignalStrength.getNumSignalStrengthLevels());
                    }
                    state2.level = i;
                    valueAt2.getState().connected = valueAt2.getState().level >= 0;
                }
                if (bundle.containsKey("inflate")) {
                    for (int i6 = 0; i6 < this.mMobileSignalControllers.size(); i6++) {
                        this.mMobileSignalControllers.valueAt(i6).mInflateSignalStrengths = "true".equals(bundle.getString("inflate"));
                    }
                }
                String string13 = bundle.getString("activity");
                if (string13 != null) {
                    valueAt2.getState().dataConnected = true;
                    switch (string13.hashCode()) {
                        case 3365:
                            if (string13.equals("in")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 110414:
                            if (string13.equals("out")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 100357129:
                            if (string13.equals("inout")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            valueAt2.setActivity(1);
                            break;
                        case 1:
                            valueAt2.setActivity(2);
                            break;
                        case 2:
                            valueAt2.setActivity(3);
                            break;
                        default:
                            valueAt2.setActivity(0);
                            break;
                    }
                } else {
                    valueAt2.setActivity(0);
                }
                valueAt2.getState().enabled = equals3;
                valueAt2.notifyListeners();
            }
            String string14 = bundle.getString("carriernetworkchange");
            if (string14 != null) {
                boolean equals4 = string14.equals("show");
                for (int i7 = 0; i7 < this.mMobileSignalControllers.size(); i7++) {
                    this.mMobileSignalControllers.valueAt(i7).setCarrierNetworkChangeMode(equals4);
                }
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoMode
    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("network");
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void recordLastNetworkCallback(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 16;
    }

    private SubscriptionInfo addSignalController(int i, int i2) {
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo(i, "", i2, "", "", 0, 0, "", 0, null, null, null, "", false, null, null);
        MobileSignalController mobileSignalController = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionInfo.getSubscriptionId()), this.mCallbackHandler, this, subscriptionInfo, this.mSubDefaults, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
        this.mMobileSignalControllers.put(i, mobileSignalController);
        mobileSignalController.getState().userSetup = true;
        return subscriptionInfo;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public boolean hasEmergencyCryptKeeperText() {
        return EncryptionHelper.IS_DATA_ENCRYPTED;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController
    public boolean isRadioOn() {
        return !this.mAirplaneMode;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SubListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        SubListener(Looper looper) {
            super(looper);
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            NetworkControllerImpl.this.updateMobileControllers();
        }
    }
}
