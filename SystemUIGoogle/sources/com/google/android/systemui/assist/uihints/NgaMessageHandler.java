package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.core.math.MathUtils;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
/* loaded from: classes2.dex */
public class NgaMessageHandler {
    private static final boolean VERBOSE;
    private final AssistantPresenceHandler mAssistantPresenceHandler;
    private final Set<AudioInfoListener> mAudioInfoListeners;
    private final Set<CardInfoListener> mCardInfoListeners;
    private final Set<ChipsInfoListener> mChipsInfoListeners;
    private final Set<ClearListener> mClearListeners;
    private final Set<ConfigInfoListener> mConfigInfoListeners;
    private final Set<EdgeLightsInfoListener> mEdgeLightsInfoListeners;
    private final Set<GoBackListener> mGoBackListeners;
    private final Set<GreetingInfoListener> mGreetingInfoListeners;
    private final Handler mHandler;
    private boolean mIsGestureNav;
    private final Set<KeepAliveListener> mKeepAliveListeners;
    private final Set<KeyboardInfoListener> mKeyboardInfoListeners;
    private final Set<NavBarVisibilityListener> mNavBarVisibilityListeners;
    private final NgaUiController mNgaUiController;
    private final Set<StartActivityInfoListener> mStartActivityInfoListeners;
    private final Set<TakeScreenshotListener> mTakeScreenshotListeners;
    private final Set<TranscriptionInfoListener> mTranscriptionInfoListeners;
    private final Set<WarmingListener> mWarmingListeners;
    private final Set<ZerostateInfoListener> mZerostateInfoListeners;

    /* loaded from: classes2.dex */
    public interface AudioInfoListener {
        void onAudioInfo(float f, float f2);
    }

    /* loaded from: classes2.dex */
    public interface CardInfoListener {
        void onCardInfo(boolean z, int i, boolean z2, boolean z3);
    }

    /* loaded from: classes2.dex */
    public interface ChipsInfoListener {
        void onChipsInfo(List<Bundle> list);
    }

    /* loaded from: classes2.dex */
    public interface ClearListener {
        void onClear(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface ConfigInfoListener {
        void onConfigInfo(ConfigInfo configInfo);
    }

    /* loaded from: classes2.dex */
    public interface EdgeLightsInfoListener {
        void onEdgeLightsInfo(String str, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface GoBackListener {
        void onGoBack();
    }

    /* loaded from: classes2.dex */
    public interface GreetingInfoListener {
        void onGreetingInfo(String str, PendingIntent pendingIntent);
    }

    /* loaded from: classes2.dex */
    public interface KeepAliveListener {
        void onKeepAlive(String str);
    }

    /* loaded from: classes2.dex */
    public interface KeyboardInfoListener {
        void onHideKeyboard();

        void onShowKeyboard(PendingIntent pendingIntent);
    }

    /* loaded from: classes2.dex */
    public interface NavBarVisibilityListener {
        void onVisibleRequest(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface StartActivityInfoListener {
        void onStartActivityInfo(Intent intent, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface TakeScreenshotListener {
        void onTakeScreenshot(PendingIntent pendingIntent);
    }

    /* loaded from: classes2.dex */
    public interface TranscriptionInfoListener {
        void onTranscriptionInfo(String str, PendingIntent pendingIntent, int i);
    }

    /* loaded from: classes2.dex */
    public interface WarmingListener {
        void onWarmingRequest(WarmingRequest warmingRequest);
    }

    /* loaded from: classes2.dex */
    public interface ZerostateInfoListener {
        void onHideZerostate();

        void onShowZerostate(PendingIntent pendingIntent);
    }

    /* loaded from: classes2.dex */
    public static class ConfigInfo {
        public final PendingIntent configurationCallback;
        public final boolean ngaIsAssistant;
        public PendingIntent onColorChanged;
        public final PendingIntent onKeyboardShowingChange;
        public final PendingIntent onTaskChange;
        public final PendingIntent onTouchInside;
        public final PendingIntent onTouchOutside;
        public final boolean sysUiIsNgaUi;

        ConfigInfo(Bundle bundle) {
            boolean z = bundle.getBoolean("is_available");
            boolean z2 = bundle.getBoolean("nga_is_assistant", z);
            this.ngaIsAssistant = z2;
            this.sysUiIsNgaUi = z && z2;
            this.onColorChanged = (PendingIntent) bundle.getParcelable("color_changed");
            this.onTouchOutside = (PendingIntent) bundle.getParcelable("touch_outside");
            this.onTouchInside = (PendingIntent) bundle.getParcelable("touch_inside");
            this.onTaskChange = (PendingIntent) bundle.getParcelable("task_stack_changed");
            this.onKeyboardShowingChange = (PendingIntent) bundle.getParcelable("keyboard_showing_changed");
            this.configurationCallback = (PendingIntent) bundle.getParcelable("configuration");
        }
    }

    /* loaded from: classes2.dex */
    public static class WarmingRequest {
        private final PendingIntent onWarm;
        private final float threshold;

        public WarmingRequest(PendingIntent pendingIntent, float f) {
            this.onWarm = pendingIntent;
            this.threshold = MathUtils.clamp(f, 0.0f, 1.0f);
        }

        public float getThreshold() {
            return this.threshold;
        }

        public void notify(Context context, boolean z) {
            PendingIntent pendingIntent = this.onWarm;
            if (pendingIntent != null) {
                try {
                    pendingIntent.send(context, 0, new Intent().putExtra("primed", z));
                } catch (PendingIntent.CanceledException e) {
                    Log.e("NgaMessageHandler", "Unable to warm assistant, PendingIntent cancelled", e);
                }
            }
        }
    }

    static {
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        VERBOSE = str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng");
    }

    /* access modifiers changed from: package-private */
    public NgaMessageHandler(NgaUiController ngaUiController, AssistantPresenceHandler assistantPresenceHandler, NavigationModeController navigationModeController, Set<KeepAliveListener> set, Set<AudioInfoListener> set2, Set<CardInfoListener> set3, Set<ConfigInfoListener> set4, Set<EdgeLightsInfoListener> set5, Set<TranscriptionInfoListener> set6, Set<GreetingInfoListener> set7, Set<ChipsInfoListener> set8, Set<ClearListener> set9, Set<StartActivityInfoListener> set10, Set<KeyboardInfoListener> set11, Set<ZerostateInfoListener> set12, Set<GoBackListener> set13, Set<TakeScreenshotListener> set14, Set<WarmingListener> set15, Set<NavBarVisibilityListener> set16, Handler handler) {
        this.mNgaUiController = ngaUiController;
        this.mAssistantPresenceHandler = assistantPresenceHandler;
        this.mKeepAliveListeners = set;
        this.mAudioInfoListeners = set2;
        this.mCardInfoListeners = set3;
        this.mConfigInfoListeners = set4;
        this.mEdgeLightsInfoListeners = set5;
        this.mTranscriptionInfoListeners = set6;
        this.mGreetingInfoListeners = set7;
        this.mChipsInfoListeners = set8;
        this.mClearListeners = set9;
        this.mStartActivityInfoListeners = set10;
        this.mKeyboardInfoListeners = set11;
        this.mZerostateInfoListeners = set12;
        this.mGoBackListeners = set13;
        this.mTakeScreenshotListeners = set14;
        this.mWarmingListeners = set15;
        this.mNavBarVisibilityListeners = set16;
        this.mHandler = handler;
        this.mIsGestureNav = QuickStepContract.isGesturalMode(navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.assist.uihints.NgaMessageHandler$$ExternalSyntheticLambda0
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                NgaMessageHandler.this.lambda$new$0(i);
            }
        }));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        this.mIsGestureNav = QuickStepContract.isGesturalMode(i);
    }

    /* renamed from: processBundle */
    public void lambda$processBundle$1(Bundle bundle, Runnable runnable) {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            this.mHandler.post(new Runnable(bundle, runnable) { // from class: com.google.android.systemui.assist.uihints.NgaMessageHandler$$ExternalSyntheticLambda1
                public final /* synthetic */ Bundle f$1;
                public final /* synthetic */ Runnable f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NgaMessageHandler.this.lambda$processBundle$1(this.f$1, this.f$2);
                }
            });
            return;
        }
        logBundle(bundle);
        String string = bundle.getString("action", "");
        if (string.isEmpty()) {
            Log.w("NgaMessageHandler", "No action specified, ignoring");
            return;
        }
        boolean isNgaAssistant = this.mAssistantPresenceHandler.isNgaAssistant();
        boolean isSysUiNgaUi = this.mAssistantPresenceHandler.isSysUiNgaUi();
        boolean processAlwaysAvailableActions = processAlwaysAvailableActions(string, bundle);
        if (!processAlwaysAvailableActions && isNgaAssistant && !(processAlwaysAvailableActions = processNgaActions(string, bundle)) && isSysUiNgaUi) {
            processAlwaysAvailableActions = processSysUiNgaUiActions(string, bundle);
        }
        if (!processAlwaysAvailableActions) {
            Log.w("NgaMessageHandler", String.format("Invalid action \"%s\" for state:\n  NGA is Assistant = %b\n  SysUI is NGA UI = %b", string, Boolean.valueOf(isNgaAssistant), Boolean.valueOf(isSysUiNgaUi)));
        }
        runnable.run();
    }

    private boolean processAlwaysAvailableActions(String str, Bundle bundle) {
        str.hashCode();
        if (str.equals("config")) {
            ConfigInfo configInfo = new ConfigInfo(bundle);
            for (ConfigInfoListener configInfoListener : this.mConfigInfoListeners) {
                configInfoListener.onConfigInfo(configInfo);
            }
            this.mNgaUiController.onUiMessageReceived();
        } else if (!str.equals("gesture_nav_bar_visible")) {
            return false;
        } else {
            if (this.mIsGestureNav) {
                boolean z = bundle.getBoolean("visible", true);
                for (NavBarVisibilityListener navBarVisibilityListener : this.mNavBarVisibilityListeners) {
                    navBarVisibilityListener.onVisibleRequest(z);
                }
            }
        }
        return true;
    }

    private boolean processNgaActions(String str, Bundle bundle) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 3046160:
                if (str.equals("card")) {
                    c = 0;
                    break;
                }
                break;
            case 192184798:
                if (str.equals("go_back")) {
                    c = 1;
                    break;
                }
                break;
            case 371207756:
                if (str.equals("start_activity")) {
                    c = 2;
                    break;
                }
                break;
            case 777739294:
                if (str.equals("take_screenshot")) {
                    c = 3;
                    break;
                }
                break;
            case 1124416317:
                if (str.equals("warming")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                boolean z = bundle.getBoolean("is_visible");
                int i = bundle.getInt("sysui_color", 0);
                boolean z2 = bundle.getBoolean("animate_transition", true);
                boolean z3 = bundle.getBoolean("card_forces_scrim");
                for (CardInfoListener cardInfoListener : this.mCardInfoListeners) {
                    cardInfoListener.onCardInfo(z, i, z2, z3);
                }
                break;
            case 1:
                for (GoBackListener goBackListener : this.mGoBackListeners) {
                    goBackListener.onGoBack();
                }
                break;
            case 2:
                Intent intent = (Intent) bundle.getParcelable("intent");
                boolean z4 = bundle.getBoolean("dismiss_shade");
                for (StartActivityInfoListener startActivityInfoListener : this.mStartActivityInfoListeners) {
                    startActivityInfoListener.onStartActivityInfo(intent, z4);
                }
                break;
            case 3:
                PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("on_finish");
                for (TakeScreenshotListener takeScreenshotListener : this.mTakeScreenshotListeners) {
                    takeScreenshotListener.onTakeScreenshot(pendingIntent);
                }
                break;
            case 4:
                WarmingRequest warmingRequest = new WarmingRequest((PendingIntent) bundle.getParcelable("intent"), bundle.getFloat("threshold", 0.1f));
                for (WarmingListener warmingListener : this.mWarmingListeners) {
                    warmingListener.onWarmingRequest(warmingRequest);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean processSysUiNgaUiActions(String str, Bundle bundle) {
        for (KeepAliveListener keepAliveListener : this.mKeepAliveListeners) {
            keepAliveListener.onKeepAlive(str);
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2051025175:
                if (str.equals("show_keyboard")) {
                    c = 0;
                    break;
                }
                break;
            case -2040419289:
                if (str.equals("show_zerostate")) {
                    c = 1;
                    break;
                }
                break;
            case -1160605116:
                if (str.equals("hide_keyboard")) {
                    c = 2;
                    break;
                }
                break;
            case -241763182:
                if (str.equals("transcription")) {
                    c = 3;
                    break;
                }
                break;
            case -207201236:
                if (str.equals("hide_zerostate")) {
                    c = 4;
                    break;
                }
                break;
            case 94631335:
                if (str.equals("chips")) {
                    c = 5;
                    break;
                }
                break;
            case 94746189:
                if (str.equals("clear")) {
                    c = 6;
                    break;
                }
                break;
            case 205422649:
                if (str.equals("greeting")) {
                    c = 7;
                    break;
                }
                break;
            case 771587807:
                if (str.equals("edge_lights")) {
                    c = '\b';
                    break;
                }
                break;
            case 1549039479:
                if (str.equals("audio_info")) {
                    c = '\t';
                    break;
                }
                break;
            case 1642639251:
                if (str.equals("keep_alive")) {
                    c = '\n';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("tap_action");
                for (KeyboardInfoListener keyboardInfoListener : this.mKeyboardInfoListeners) {
                    keyboardInfoListener.onShowKeyboard(pendingIntent);
                }
                break;
            case 1:
                PendingIntent pendingIntent2 = (PendingIntent) bundle.getParcelable("tap_action");
                for (ZerostateInfoListener zerostateInfoListener : this.mZerostateInfoListeners) {
                    zerostateInfoListener.onShowZerostate(pendingIntent2);
                }
                break;
            case 2:
                for (KeyboardInfoListener keyboardInfoListener2 : this.mKeyboardInfoListeners) {
                    keyboardInfoListener2.onHideKeyboard();
                }
                break;
            case 3:
                String string = bundle.getString("text");
                PendingIntent pendingIntent3 = (PendingIntent) bundle.getParcelable("tap_action");
                int i = bundle.getInt("text_color");
                for (TranscriptionInfoListener transcriptionInfoListener : this.mTranscriptionInfoListeners) {
                    transcriptionInfoListener.onTranscriptionInfo(string, pendingIntent3, i);
                }
                break;
            case 4:
                for (ZerostateInfoListener zerostateInfoListener2 : this.mZerostateInfoListeners) {
                    zerostateInfoListener2.onHideZerostate();
                }
                break;
            case 5:
                ArrayList parcelableArrayList = bundle.getParcelableArrayList("chips");
                for (ChipsInfoListener chipsInfoListener : this.mChipsInfoListeners) {
                    chipsInfoListener.onChipsInfo(parcelableArrayList);
                }
                break;
            case 6:
                boolean z = bundle.getBoolean("show_animation", true);
                for (ClearListener clearListener : this.mClearListeners) {
                    clearListener.onClear(z);
                }
                break;
            case 7:
                String string2 = bundle.getString("text");
                PendingIntent pendingIntent4 = (PendingIntent) bundle.getParcelable("tap_action");
                for (GreetingInfoListener greetingInfoListener : this.mGreetingInfoListeners) {
                    greetingInfoListener.onGreetingInfo(string2, pendingIntent4);
                }
                break;
            case '\b':
                String string3 = bundle.getString("state", "");
                boolean z2 = bundle.getBoolean("listening");
                for (EdgeLightsInfoListener edgeLightsInfoListener : this.mEdgeLightsInfoListeners) {
                    edgeLightsInfoListener.onEdgeLightsInfo(string3, z2);
                }
                break;
            case '\t':
                float f = bundle.getFloat("volume");
                float f2 = bundle.getFloat("speech_confidence");
                for (AudioInfoListener audioInfoListener : this.mAudioInfoListeners) {
                    audioInfoListener.onAudioInfo(f, f2);
                }
                break;
            case '\n':
                break;
            default:
                return false;
        }
        this.mNgaUiController.onUiMessageReceived();
        return true;
    }

    private void logBundle(Bundle bundle) {
        if (VERBOSE && !"audio_info".equals(bundle.get("action"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Contents of NGA Bundle:");
            for (String str : bundle.keySet()) {
                sb.append("\n   ");
                sb.append(str);
                sb.append(": ");
                if ("text".equals(str)) {
                    sb.append("(");
                    sb.append(bundle.getString(str).length());
                    sb.append(" characters)");
                } else if ("chips".equals(str)) {
                    ArrayList<Bundle> parcelableArrayList = bundle.getParcelableArrayList("chips");
                    if (parcelableArrayList != null) {
                        for (Bundle bundle2 : parcelableArrayList) {
                            sb.append("\n      Chip:");
                            for (String str2 : bundle2.keySet()) {
                                sb.append("\n         ");
                                sb.append(str2);
                                sb.append(": ");
                                sb.append(bundle2.get(str2));
                            }
                        }
                    }
                } else {
                    sb.append(bundle.get(str));
                }
            }
            Log.v("NgaMessageHandler", sb.toString());
        }
    }
}
