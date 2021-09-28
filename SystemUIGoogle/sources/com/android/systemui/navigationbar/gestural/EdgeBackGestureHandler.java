package com.android.systemui.navigationbar.gestural;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.Display;
import android.view.ISystemGestureExclusionListener;
import android.view.IWindowManager;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.android.internal.policy.GestureNavigationSettingsObserver;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.EdgeBackGestureHandlerProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class EdgeBackGestureHandler extends CurrentUserTracker implements PluginListener<NavigationEdgeBackPlugin>, ProtoTraceable<SystemUiTraceProto> {
    private BackGestureTfClassifierProvider mBackGestureTfClassifierProvider;
    private float mBottomGestureHeight;
    private final Context mContext;
    private boolean mDisabledForQuickstep;
    private final int mDisplayId;
    private NavigationEdgeBackPlugin mEdgeBackPlugin;
    private int mEdgeWidthLeft;
    private int mEdgeWidthRight;
    private final FalsingManager mFalsingManager;
    private boolean mGestureBlockingActivityRunning;
    private final GestureNavigationSettingsObserver mGestureNavigationSettingsObserver;
    private InputChannelCompat$InputEventReceiver mInputEventReceiver;
    private InputMonitor mInputMonitor;
    private boolean mIsAttached;
    private boolean mIsBackGestureAllowed;
    private boolean mIsEnabled;
    private boolean mIsGesturalModeEnabled;
    private boolean mIsInPipMode;
    private boolean mIsNavBarShownTransiently;
    private boolean mIsOnLeftEdge;
    private int mLeftInset;
    private final int mLongPressTimeout;
    private int mMLEnableWidth;
    private float mMLModelThreshold;
    private float mMLResults;
    private final Executor mMainExecutor;
    private final NavigationModeController mNavigationModeController;
    private final OverviewProxyService mOverviewProxyService;
    private String mPackageName;
    private final PluginManager mPluginManager;
    private final ProtoTracer mProtoTracer;
    private int mRightInset;
    private Runnable mStateChangeCallback;
    private int mSysUiFlags;
    private final SysUiState mSysUiState;
    private float mTouchSlop;
    private boolean mUseMLModel;
    private final ViewConfiguration mViewConfiguration;
    private Map<String, Integer> mVocab;
    private final WindowManager mWindowManager;
    private final IWindowManager mWindowManagerService;
    private static final int MAX_LONG_PRESS_TIMEOUT = SystemProperties.getInt("gestures.back_timeout", 250);
    private static final boolean ENABLE_PER_WINDOW_INPUT_ROTATION = SystemProperties.getBoolean("persist.debug.per_window_input_rotation", false);
    private ISystemGestureExclusionListener mGestureExclusionListener = new ISystemGestureExclusionListener.Stub() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.1
        public void onSystemGestureExclusionChanged(int i, Region region, Region region2) {
            if (i == EdgeBackGestureHandler.this.mDisplayId) {
                EdgeBackGestureHandler.this.mMainExecutor.execute(new EdgeBackGestureHandler$1$$ExternalSyntheticLambda0(this, region, region2));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemGestureExclusionChanged$0(Region region, Region region2) {
            EdgeBackGestureHandler.this.mExcludeRegion.set(region);
            Region region3 = EdgeBackGestureHandler.this.mUnrestrictedExcludeRegion;
            if (region2 != null) {
                region = region2;
            }
            region3.set(region);
        }
    };
    private OverviewProxyService.OverviewProxyListener mQuickSwitchListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.2
        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onPrioritizedRotation(int i) {
            EdgeBackGestureHandler.this.mStartingQuickstepRotation = i;
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            edgeBackGestureHandler.updateDisabledForQuickstep(edgeBackGestureHandler.mContext.getResources().getConfiguration());
        }
    };
    private TaskStackChangeListener mTaskStackListener = new TaskStackChangeListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.3
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskStackChanged() {
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            edgeBackGestureHandler.mGestureBlockingActivityRunning = edgeBackGestureHandler.isGestureBlockingActivityRunning();
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskCreated(int i, ComponentName componentName) {
            if (componentName != null) {
                EdgeBackGestureHandler.this.mPackageName = componentName.getPackageName();
                return;
            }
            EdgeBackGestureHandler.this.mPackageName = "_UNKNOWN";
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onActivityPinned(String str, int i, int i2, int i3) {
            EdgeBackGestureHandler.this.mIsInPipMode = true;
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onActivityUnpinned() {
            EdgeBackGestureHandler.this.mIsInPipMode = false;
        }
    };
    private DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.4
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (!"systemui".equals(properties.getNamespace())) {
                return;
            }
            if (properties.getKeyset().contains("back_gesture_ml_model_threshold") || properties.getKeyset().contains("use_back_gesture_ml_model") || properties.getKeyset().contains("back_gesture_ml_model_name")) {
                EdgeBackGestureHandler.this.updateMLModelState();
            }
        }
    };
    private final List<ComponentName> mGestureBlockingActivities = new ArrayList();
    private final Point mDisplaySize = new Point();
    private final Rect mPipExcludedBounds = new Rect();
    private final Rect mNavBarOverlayExcludedBounds = new Rect();
    private final Region mExcludeRegion = new Region();
    private final Region mUnrestrictedExcludeRegion = new Region();
    private int mStartingQuickstepRotation = -1;
    private final PointF mDownPoint = new PointF();
    private final PointF mEndPoint = new PointF();
    private boolean mThresholdCrossed = false;
    private boolean mAllowGesture = false;
    private boolean mLogGesture = false;
    private boolean mInRejectedExclusion = false;
    private LogArray mPredictionLog = new LogArray(10);
    private LogArray mGestureLogInsideInsets = new LogArray(10);
    private LogArray mGestureLogOutsideInsets = new LogArray(10);
    private final NavigationEdgeBackPlugin.BackCallback mBackCallback = new NavigationEdgeBackPlugin.BackCallback() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.5
        @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin.BackCallback
        public void triggerBack() {
            EdgeBackGestureHandler.this.mFalsingManager.isFalseTouch(16);
            EdgeBackGestureHandler.this.sendEvent(0, 4);
            int i = 1;
            EdgeBackGestureHandler.this.sendEvent(1, 4);
            EdgeBackGestureHandler.this.mOverviewProxyService.notifyBackAction(true, (int) EdgeBackGestureHandler.this.mDownPoint.x, (int) EdgeBackGestureHandler.this.mDownPoint.y, false, !EdgeBackGestureHandler.this.mIsOnLeftEdge);
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            if (edgeBackGestureHandler.mInRejectedExclusion) {
                i = 2;
            }
            edgeBackGestureHandler.logGesture(i);
        }

        @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin.BackCallback
        public void cancelBack() {
            EdgeBackGestureHandler.this.logGesture(4);
            EdgeBackGestureHandler.this.mOverviewProxyService.notifyBackAction(false, (int) EdgeBackGestureHandler.this.mDownPoint.x, (int) EdgeBackGestureHandler.this.mDownPoint.y, false, !EdgeBackGestureHandler.this.mIsOnLeftEdge);
        }
    };
    private final SysUiState.SysUiStateCallback mSysUiStateCallback = new SysUiState.SysUiStateCallback() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.6
        @Override // com.android.systemui.model.SysUiState.SysUiStateCallback
        public void onSystemUiStateChanged(int i) {
            EdgeBackGestureHandler.this.mSysUiFlags = i;
        }
    };

    EdgeBackGestureHandler(Context context, OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager) {
        super(broadcastDispatcher);
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = executor;
        this.mOverviewProxyService = overviewProxyService;
        this.mSysUiState = sysUiState;
        this.mPluginManager = pluginManager;
        this.mProtoTracer = protoTracer;
        this.mNavigationModeController = navigationModeController;
        this.mViewConfiguration = viewConfiguration;
        this.mWindowManager = windowManager;
        this.mWindowManagerService = iWindowManager;
        this.mFalsingManager = falsingManager;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(context.getString(17039985));
        if (unflattenFromString != null) {
            String packageName = unflattenFromString.getPackageName();
            try {
                Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(packageName);
                int identifier = resourcesForApplication.getIdentifier("gesture_blocking_activities", "array", packageName);
                if (identifier == 0) {
                    Log.e("EdgeBackGestureHandler", "No resource found for gesture-blocking activities");
                } else {
                    for (String str : resourcesForApplication.getStringArray(identifier)) {
                        this.mGestureBlockingActivities.add(ComponentName.unflattenFromString(str));
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("EdgeBackGestureHandler", "Failed to add gesture blocking activities", e);
            }
        }
        this.mLongPressTimeout = Math.min(MAX_LONG_PRESS_TIMEOUT, ViewConfiguration.getLongPressTimeout());
        this.mGestureNavigationSettingsObserver = new GestureNavigationSettingsObserver(this.mContext.getMainThreadHandler(), this.mContext, new Runnable() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                EdgeBackGestureHandler.this.onNavigationSettingsChanged();
            }
        });
        updateCurrentUserResources();
    }

    public void setStateChangeCallback(Runnable runnable) {
        this.mStateChangeCallback = runnable;
    }

    public void updateCurrentUserResources() {
        Resources resources = this.mNavigationModeController.getCurrentUserContext().getResources();
        this.mEdgeWidthLeft = this.mGestureNavigationSettingsObserver.getLeftSensitivity(resources);
        this.mEdgeWidthRight = this.mGestureNavigationSettingsObserver.getRightSensitivity(resources);
        this.mIsBackGestureAllowed = !this.mGestureNavigationSettingsObserver.areNavigationButtonForcedVisible();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mBottomGestureHeight = TypedValue.applyDimension(1, DeviceConfig.getFloat("systemui", "back_gesture_bottom_height", resources.getDimension(17105352) / displayMetrics.density), displayMetrics);
        int applyDimension = (int) TypedValue.applyDimension(1, 12.0f, displayMetrics);
        this.mMLEnableWidth = applyDimension;
        int i = this.mEdgeWidthRight;
        if (applyDimension > i) {
            this.mMLEnableWidth = i;
        }
        int i2 = this.mMLEnableWidth;
        int i3 = this.mEdgeWidthLeft;
        if (i2 > i3) {
            this.mMLEnableWidth = i3;
        }
        this.mTouchSlop = ((float) this.mViewConfiguration.getScaledTouchSlop()) * DeviceConfig.getFloat("systemui", "back_gesture_slop_multiplier", 0.75f);
    }

    public void updateNavigationBarOverlayExcludeRegion(Rect rect) {
        this.mNavBarOverlayExcludedBounds.set(rect);
    }

    /* access modifiers changed from: private */
    public void onNavigationSettingsChanged() {
        boolean isHandlingGestures = isHandlingGestures();
        updateCurrentUserResources();
        if (isHandlingGestures != isHandlingGestures()) {
            this.mStateChangeCallback.run();
        }
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        updateIsEnabled();
        updateCurrentUserResources();
    }

    public void onNavBarAttached() {
        this.mIsAttached = true;
        this.mProtoTracer.add(this);
        this.mOverviewProxyService.addCallback(this.mQuickSwitchListener);
        this.mSysUiState.addCallback(this.mSysUiStateCallback);
        updateIsEnabled();
        startTracking();
    }

    public void onNavBarDetached() {
        this.mIsAttached = false;
        this.mProtoTracer.remove(this);
        this.mOverviewProxyService.removeCallback(this.mQuickSwitchListener);
        this.mSysUiState.removeCallback(this.mSysUiStateCallback);
        updateIsEnabled();
        stopTracking();
    }

    public void onNavigationModeChanged(int i) {
        this.mIsGesturalModeEnabled = QuickStepContract.isGesturalMode(i);
        updateIsEnabled();
        updateCurrentUserResources();
    }

    public void onNavBarTransientStateChanged(boolean z) {
        this.mIsNavBarShownTransiently = z;
    }

    private void disposeInputChannel() {
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.mInputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
    }

    private void updateIsEnabled() {
        boolean z = this.mIsAttached && this.mIsGesturalModeEnabled;
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            disposeInputChannel();
            NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
            if (navigationEdgeBackPlugin != null) {
                navigationEdgeBackPlugin.onDestroy();
                this.mEdgeBackPlugin = null;
            }
            if (!this.mIsEnabled) {
                this.mGestureNavigationSettingsObserver.unregister();
                this.mPluginManager.removePluginListener(this);
                TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
                DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.unregisterSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e) {
                    Log.e("EdgeBackGestureHandler", "Failed to unregister window manager callbacks", e);
                }
            } else {
                this.mGestureNavigationSettingsObserver.register();
                updateDisplaySize();
                TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
                Executor executor = this.mMainExecutor;
                Objects.requireNonNull(executor);
                DeviceConfig.addOnPropertiesChangedListener("systemui", new Executor(executor) { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda2
                    public final /* synthetic */ Executor f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        this.f$0.execute(runnable);
                    }
                }, this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.registerSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e2) {
                    Log.e("EdgeBackGestureHandler", "Failed to register window manager callbacks", e2);
                }
                InputMonitor monitorGestureInput = InputManager.getInstance().monitorGestureInput("edge-swipe", this.mDisplayId);
                this.mInputMonitor = monitorGestureInput;
                this.mInputEventReceiver = new InputChannelCompat$InputEventReceiver(monitorGestureInput.getInputChannel(), Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
                    public final void onInputEvent(InputEvent inputEvent) {
                        EdgeBackGestureHandler.this.onInputEvent(inputEvent);
                    }
                });
                setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext));
                this.mPluginManager.addPluginListener((PluginListener) this, NavigationEdgeBackPlugin.class, false);
            }
            updateMLModelState();
        }
    }

    public void onPluginConnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin, Context context) {
        setEdgeBackPlugin(navigationEdgeBackPlugin);
    }

    public void onPluginDisconnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext));
    }

    private void setEdgeBackPlugin(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        NavigationEdgeBackPlugin navigationEdgeBackPlugin2 = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin2 != null) {
            navigationEdgeBackPlugin2.onDestroy();
        }
        this.mEdgeBackPlugin = navigationEdgeBackPlugin;
        navigationEdgeBackPlugin.setBackCallback(this.mBackCallback);
        this.mEdgeBackPlugin.setLayoutParams(createLayoutParams());
        updateDisplaySize();
    }

    public boolean isHandlingGestures() {
        return this.mIsEnabled && this.mIsBackGestureAllowed;
    }

    public void setPipStashExclusionBounds(Rect rect) {
        this.mPipExcludedBounds.set(rect);
    }

    private WindowManager.LayoutParams createLayoutParams() {
        Resources resources = this.mContext.getResources();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(resources.getDimensionPixelSize(R$dimen.navigation_edge_panel_width), resources.getDimensionPixelSize(R$dimen.navigation_edge_panel_height), 2024, 280, -3);
        layoutParams.accessibilityTitle = this.mContext.getString(R$string.nav_bar_edge_panel);
        layoutParams.windowAnimations = 0;
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("EdgeBackGestureHandler" + this.mContext.getDisplayId());
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTrustedOverlay();
        return layoutParams;
    }

    /* access modifiers changed from: private */
    public void onInputEvent(InputEvent inputEvent) {
        Display display;
        int rotation;
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            if (ENABLE_PER_WINDOW_INPUT_ROTATION && (rotation = (display = this.mContext.getDisplay()).getRotation()) != 0) {
                Point point = new Point();
                display.getRealSize(point);
                motionEvent = MotionEvent.obtain(motionEvent);
                motionEvent.transform(MotionEvent.createRotateMatrix(rotation, point.x, point.y));
            }
            onMotionEvent(motionEvent);
        }
    }

    /* access modifiers changed from: private */
    public void updateMLModelState() {
        boolean z = this.mIsEnabled && DeviceConfig.getBoolean("systemui", "use_back_gesture_ml_model", false);
        if (z != this.mUseMLModel) {
            if (z) {
                this.mBackGestureTfClassifierProvider = SystemUIFactory.getInstance().createBackGestureTfClassifierProvider(this.mContext.getAssets(), DeviceConfig.getString("systemui", "back_gesture_ml_model_name", "backgesture"));
                this.mMLModelThreshold = DeviceConfig.getFloat("systemui", "back_gesture_ml_model_threshold", 0.9f);
                if (this.mBackGestureTfClassifierProvider.isActive()) {
                    this.mVocab = this.mBackGestureTfClassifierProvider.loadVocab(this.mContext.getAssets());
                    this.mUseMLModel = true;
                    return;
                }
            }
            this.mUseMLModel = false;
            BackGestureTfClassifierProvider backGestureTfClassifierProvider = this.mBackGestureTfClassifierProvider;
            if (backGestureTfClassifierProvider != null) {
                backGestureTfClassifierProvider.release();
                this.mBackGestureTfClassifierProvider = null;
            }
        }
    }

    private int getBackGesturePredictionsCategory(int i, int i2, int i3) {
        int i4;
        if (i3 == -1) {
            return -1;
        }
        int i5 = this.mDisplaySize.x;
        if (((double) i) <= ((double) i5) / 2.0d) {
            i4 = 1;
        } else {
            i = i5 - i;
            i4 = 2;
        }
        float predict = this.mBackGestureTfClassifierProvider.predict(new Object[]{new long[]{(long) i5}, new long[]{(long) i}, new long[]{(long) i4}, new long[]{(long) i3}, new long[]{(long) i2}});
        this.mMLResults = predict;
        if (predict == -1.0f) {
            return -1;
        }
        return predict >= this.mMLModelThreshold ? 1 : 0;
    }

    private boolean isWithinInsets(int i, int i2) {
        Point point = this.mDisplaySize;
        if (((float) i2) >= ((float) point.y) - this.mBottomGestureHeight) {
            return false;
        }
        if (i <= (this.mEdgeWidthLeft + this.mLeftInset) * 2 || i >= point.x - ((this.mEdgeWidthRight + this.mRightInset) * 2)) {
            return true;
        }
        return false;
    }

    private boolean isWithinTouchRegion(int i, int i2) {
        int backGesturePredictionsCategory;
        if (((!this.mIsInPipMode || !this.mPipExcludedBounds.contains(i, i2)) ? null : 1) != null || this.mNavBarOverlayExcludedBounds.contains(i, i2)) {
            return false;
        }
        Map<String, Integer> map = this.mVocab;
        int intValue = map != null ? map.getOrDefault(this.mPackageName, -1).intValue() : -1;
        int i3 = this.mEdgeWidthLeft;
        int i4 = this.mLeftInset;
        boolean z = i < i3 + i4 || i >= (this.mDisplaySize.x - this.mEdgeWidthRight) - this.mRightInset;
        if (z) {
            int i5 = this.mMLEnableWidth;
            if (((i < i4 + i5 || i >= (this.mDisplaySize.x - i5) - this.mRightInset) ? 1 : null) == null && this.mUseMLModel && (backGesturePredictionsCategory = getBackGesturePredictionsCategory(i, i2, intValue)) != -1) {
                z = backGesturePredictionsCategory == 1;
            }
        }
        LogArray logArray = this.mPredictionLog;
        int i6 = z ? 1 : 0;
        int i7 = z ? 1 : 0;
        int i8 = z ? 1 : 0;
        int i9 = z ? 1 : 0;
        int i10 = z ? 1 : 0;
        int i11 = z ? 1 : 0;
        logArray.log(String.format("Prediction [%d,%d,%d,%d,%f,%d]", Long.valueOf(System.currentTimeMillis()), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(intValue), Float.valueOf(this.mMLResults), Integer.valueOf(i6)));
        if (this.mIsNavBarShownTransiently) {
            this.mLogGesture = true;
            return z;
        } else if (this.mExcludeRegion.contains(i, i2)) {
            if (z) {
                this.mOverviewProxyService.notifyBackAction(false, -1, -1, false, !this.mIsOnLeftEdge);
                PointF pointF = this.mEndPoint;
                pointF.x = -1.0f;
                pointF.y = -1.0f;
                this.mLogGesture = true;
                logGesture(3);
            }
            return false;
        } else {
            this.mInRejectedExclusion = this.mUnrestrictedExcludeRegion.contains(i, i2);
            this.mLogGesture = true;
            return z;
        }
    }

    private void cancelGesture(MotionEvent motionEvent) {
        this.mAllowGesture = false;
        this.mLogGesture = false;
        this.mInRejectedExclusion = false;
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(3);
        this.mEdgeBackPlugin.onMotionEvent(obtain);
        obtain.recycle();
    }

    /* access modifiers changed from: private */
    public void logGesture(int i) {
        if (this.mLogGesture) {
            this.mLogGesture = false;
            String str = (!this.mUseMLModel || !this.mVocab.containsKey(this.mPackageName) || this.mVocab.get(this.mPackageName).intValue() >= 100) ? "" : this.mPackageName;
            PointF pointF = this.mDownPoint;
            float f = pointF.y;
            int i2 = (int) f;
            int i3 = this.mIsOnLeftEdge ? 1 : 2;
            int i4 = (int) pointF.x;
            int i5 = (int) f;
            PointF pointF2 = this.mEndPoint;
            SysUiStatsLog.write(224, i, i2, i3, i4, i5, (int) pointF2.x, (int) pointF2.y, this.mEdgeWidthLeft + this.mLeftInset, this.mDisplaySize.x - (this.mEdgeWidthRight + this.mRightInset), this.mUseMLModel ? this.mMLResults : -2.0f, str);
        }
    }

    private void onMotionEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mInputEventReceiver.setBatchingEnabled(false);
            this.mIsOnLeftEdge = motionEvent.getX() <= ((float) (this.mEdgeWidthLeft + this.mLeftInset));
            this.mMLResults = 0.0f;
            this.mLogGesture = false;
            this.mInRejectedExclusion = false;
            boolean isWithinInsets = isWithinInsets((int) motionEvent.getX(), (int) motionEvent.getY());
            boolean z = !this.mDisabledForQuickstep && this.mIsBackGestureAllowed && isWithinInsets && !this.mGestureBlockingActivityRunning && !QuickStepContract.isBackGestureDisabled(this.mSysUiFlags) && isWithinTouchRegion((int) motionEvent.getX(), (int) motionEvent.getY());
            this.mAllowGesture = z;
            if (z) {
                this.mEdgeBackPlugin.setIsLeftPanel(this.mIsOnLeftEdge);
                this.mEdgeBackPlugin.onMotionEvent(motionEvent);
            }
            if (this.mLogGesture) {
                this.mDownPoint.set(motionEvent.getX(), motionEvent.getY());
                this.mEndPoint.set(-1.0f, -1.0f);
                this.mThresholdCrossed = false;
            }
            (isWithinInsets ? this.mGestureLogInsideInsets : this.mGestureLogOutsideInsets).log(String.format("Gesture [%d,alw=%B,%B,%B,%B,disp=%s,wl=%d,il=%d,wr=%d,ir=%d,excl=%s]", Long.valueOf(System.currentTimeMillis()), Boolean.valueOf(this.mAllowGesture), Boolean.valueOf(this.mIsOnLeftEdge), Boolean.valueOf(this.mIsBackGestureAllowed), Boolean.valueOf(QuickStepContract.isBackGestureDisabled(this.mSysUiFlags)), this.mDisplaySize, Integer.valueOf(this.mEdgeWidthLeft), Integer.valueOf(this.mLeftInset), Integer.valueOf(this.mEdgeWidthRight), Integer.valueOf(this.mRightInset), this.mExcludeRegion));
        } else if (this.mAllowGesture || this.mLogGesture) {
            if (!this.mThresholdCrossed) {
                this.mEndPoint.x = (float) ((int) motionEvent.getX());
                this.mEndPoint.y = (float) ((int) motionEvent.getY());
                if (actionMasked == 5) {
                    if (this.mAllowGesture) {
                        logGesture(6);
                        cancelGesture(motionEvent);
                    }
                    this.mLogGesture = false;
                    return;
                } else if (actionMasked == 2) {
                    if (motionEvent.getEventTime() - motionEvent.getDownTime() > ((long) this.mLongPressTimeout)) {
                        if (this.mAllowGesture) {
                            logGesture(7);
                            cancelGesture(motionEvent);
                        }
                        this.mLogGesture = false;
                        return;
                    }
                    float abs = Math.abs(motionEvent.getX() - this.mDownPoint.x);
                    float abs2 = Math.abs(motionEvent.getY() - this.mDownPoint.y);
                    if (abs2 > abs && abs2 > this.mTouchSlop) {
                        if (this.mAllowGesture) {
                            logGesture(8);
                            cancelGesture(motionEvent);
                        }
                        this.mLogGesture = false;
                        return;
                    } else if (abs > abs2 && abs > this.mTouchSlop) {
                        if (this.mAllowGesture) {
                            this.mThresholdCrossed = true;
                            this.mInputMonitor.pilferPointers();
                            this.mInputEventReceiver.setBatchingEnabled(true);
                        } else {
                            logGesture(5);
                        }
                    }
                }
            }
            if (this.mAllowGesture) {
                this.mEdgeBackPlugin.onMotionEvent(motionEvent);
            }
        }
        this.mProtoTracer.scheduleFrameUpdate();
    }

    /* access modifiers changed from: private */
    public void updateDisabledForQuickstep(Configuration configuration) {
        int rotation = configuration.windowConfiguration.getRotation();
        int i = this.mStartingQuickstepRotation;
        this.mDisabledForQuickstep = i > -1 && i != rotation;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mStartingQuickstepRotation > -1) {
            updateDisabledForQuickstep(configuration);
        }
        updateDisplaySize();
    }

    private void updateDisplaySize() {
        Rect bounds = this.mWindowManager.getMaximumWindowMetrics().getBounds();
        this.mDisplaySize.set(bounds.width(), bounds.height());
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setDisplaySize(this.mDisplaySize);
        }
    }

    /* access modifiers changed from: private */
    public boolean sendEvent(int i, int i2) {
        long uptimeMillis = SystemClock.uptimeMillis();
        KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, i, i2, 0, 0, -1, 0, 72, 257);
        keyEvent.setDisplayId(this.mContext.getDisplay().getDisplayId());
        return InputManager.getInstance().injectInputEvent(keyEvent, 0);
    }

    public void setInsets(int i, int i2) {
        this.mLeftInset = i;
        this.mRightInset = i2;
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setInsets(i, i2);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("EdgeBackGestureHandler:");
        printWriter.println("  mIsEnabled=" + this.mIsEnabled);
        printWriter.println("  mIsAttached=" + this.mIsAttached);
        printWriter.println("  mIsBackGestureAllowed=" + this.mIsBackGestureAllowed);
        printWriter.println("  mIsGesturalModeEnabled=" + this.mIsGesturalModeEnabled);
        printWriter.println("  mIsNavBarShownTransiently=" + this.mIsNavBarShownTransiently);
        printWriter.println("  mGestureBlockingActivityRunning=" + this.mGestureBlockingActivityRunning);
        printWriter.println("  mAllowGesture=" + this.mAllowGesture);
        printWriter.println("  mUseMLModel=" + this.mUseMLModel);
        printWriter.println("  mDisabledForQuickstep=" + this.mDisabledForQuickstep);
        printWriter.println("  mStartingQuickstepRotation=" + this.mStartingQuickstepRotation);
        printWriter.println("  mInRejectedExclusion=" + this.mInRejectedExclusion);
        printWriter.println("  mExcludeRegion=" + this.mExcludeRegion);
        printWriter.println("  mUnrestrictedExcludeRegion=" + this.mUnrestrictedExcludeRegion);
        printWriter.println("  mIsInPipMode=" + this.mIsInPipMode);
        printWriter.println("  mPipExcludedBounds=" + this.mPipExcludedBounds);
        printWriter.println("  mNavBarOverlayExcludedBounds=" + this.mNavBarOverlayExcludedBounds);
        printWriter.println("  mEdgeWidthLeft=" + this.mEdgeWidthLeft);
        printWriter.println("  mEdgeWidthRight=" + this.mEdgeWidthRight);
        printWriter.println("  mLeftInset=" + this.mLeftInset);
        printWriter.println("  mRightInset=" + this.mRightInset);
        printWriter.println("  mMLEnableWidth=" + this.mMLEnableWidth);
        printWriter.println("  mMLModelThreshold=" + this.mMLModelThreshold);
        printWriter.println("  mTouchSlop=" + this.mTouchSlop);
        printWriter.println("  mBottomGestureHeight=" + this.mBottomGestureHeight);
        printWriter.println("  mPredictionLog=" + String.join("\n", this.mPredictionLog));
        printWriter.println("  mGestureLogInsideInsets=" + String.join("\n", this.mGestureLogInsideInsets));
        printWriter.println("  mGestureLogOutsideInsets=" + String.join("\n", this.mGestureLogOutsideInsets));
        printWriter.println("  mEdgeBackPlugin=" + this.mEdgeBackPlugin);
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.dump(printWriter);
        }
    }

    /* access modifiers changed from: private */
    public boolean isGestureBlockingActivityRunning() {
        ComponentName componentName;
        ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
        if (runningTask == null) {
            componentName = null;
        } else {
            componentName = runningTask.topActivity;
        }
        if (componentName != null) {
            this.mPackageName = componentName.getPackageName();
        } else {
            this.mPackageName = "_UNKNOWN";
        }
        return componentName != null && this.mGestureBlockingActivities.contains(componentName);
    }

    public void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.edgeBackGestureHandler == null) {
            systemUiTraceProto.edgeBackGestureHandler = new EdgeBackGestureHandlerProto();
        }
        systemUiTraceProto.edgeBackGestureHandler.allowGesture = this.mAllowGesture;
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final BroadcastDispatcher mBroadcastDispatcher;
        private final Executor mExecutor;
        private final FalsingManager mFalsingManager;
        private final NavigationModeController mNavigationModeController;
        private final OverviewProxyService mOverviewProxyService;
        private final PluginManager mPluginManager;
        private final ProtoTracer mProtoTracer;
        private final SysUiState mSysUiState;
        private final ViewConfiguration mViewConfiguration;
        private final WindowManager mWindowManager;
        private final IWindowManager mWindowManagerService;

        public Factory(OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager) {
            this.mOverviewProxyService = overviewProxyService;
            this.mSysUiState = sysUiState;
            this.mPluginManager = pluginManager;
            this.mExecutor = executor;
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mProtoTracer = protoTracer;
            this.mNavigationModeController = navigationModeController;
            this.mViewConfiguration = viewConfiguration;
            this.mWindowManager = windowManager;
            this.mWindowManagerService = iWindowManager;
            this.mFalsingManager = falsingManager;
        }

        public EdgeBackGestureHandler create(Context context) {
            return new EdgeBackGestureHandler(context, this.mOverviewProxyService, this.mSysUiState, this.mPluginManager, this.mExecutor, this.mBroadcastDispatcher, this.mProtoTracer, this.mNavigationModeController, this.mViewConfiguration, this.mWindowManager, this.mWindowManagerService, this.mFalsingManager);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LogArray extends ArrayDeque<String> {
        private final int mLength;

        LogArray(int i) {
            this.mLength = i;
        }

        void log(String str) {
            if (size() >= this.mLength) {
                removeFirst();
            }
            addLast(str);
        }
    }
}
