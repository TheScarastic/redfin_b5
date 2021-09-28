package com.google.android.systemui.assist.uihints.edgelights;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import com.android.systemui.assist.AssistLogger;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.AssistantModeChangeEvent;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
/* loaded from: classes2.dex */
public final class EdgeLightsController implements NgaMessageHandler.AudioInfoListener, NgaMessageHandler.EdgeLightsInfoListener {
    private final AssistLogger mAssistLogger;
    private final Context mContext;
    private final EdgeLightsView mEdgeLightsView;
    private ModeChangeThrottler mThrottler;

    /* loaded from: classes2.dex */
    public interface ModeChangeThrottler {
        void runWhenReady(String str, Runnable runnable);
    }

    public EdgeLightsController(Context context, ViewGroup viewGroup, AssistLogger assistLogger) {
        this.mContext = context;
        this.mAssistLogger = assistLogger;
        this.mEdgeLightsView = (EdgeLightsView) viewGroup.findViewById(R$id.edge_lights);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.AudioInfoListener
    public void onAudioInfo(float f, float f2) {
        this.mEdgeLightsView.onAudioLevelUpdate(f2, f);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.EdgeLightsInfoListener
    public void onEdgeLightsInfo(String str, boolean z) {
        EdgeLightsView.Mode mode;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1911007510:
                if (str.equals("FULFILL_BOTTOM")) {
                    c = 0;
                    break;
                }
                break;
            case 2193567:
                if (str.equals("GONE")) {
                    c = 1;
                    break;
                }
                break;
            case 429932431:
                if (str.equals("HALF_LISTENING")) {
                    c = 2;
                    break;
                }
                break;
            case 1387022046:
                if (str.equals("FULFILL_PERIMETER")) {
                    c = 3;
                    break;
                }
                break;
            case 1971150571:
                if (str.equals("FULL_LISTENING")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                mode = new FulfillBottom(this.mContext, z);
                break;
            case 1:
                mode = new Gone();
                break;
            case 2:
                Log.i("EdgeLightsController", "Rendering full instead of half listening for now.");
                mode = new FullListening(this.mContext, true);
                break;
            case 3:
                mode = new FulfillPerimeter(this.mContext);
                break;
            case 4:
                mode = new FullListening(this.mContext, false);
                break;
            default:
                mode = null;
                break;
        }
        if (mode == null) {
            Log.e("EdgeLightsController", "Invalid edge lights mode: " + str);
            return;
        }
        EdgeLightsController$$ExternalSyntheticLambda0 edgeLightsController$$ExternalSyntheticLambda0 = new Runnable(mode) { // from class: com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController$$ExternalSyntheticLambda0
            public final /* synthetic */ EdgeLightsView.Mode f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                EdgeLightsController.this.lambda$onEdgeLightsInfo$0(this.f$1);
            }
        };
        ModeChangeThrottler modeChangeThrottler = this.mThrottler;
        if (modeChangeThrottler == null) {
            edgeLightsController$$ExternalSyntheticLambda0.run();
        } else {
            modeChangeThrottler.runWhenReady(str, edgeLightsController$$ExternalSyntheticLambda0);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEdgeLightsInfo$0(EdgeLightsView.Mode mode) {
        getMode().onNewModeRequest(this.mEdgeLightsView, mode);
        this.mAssistLogger.reportAssistantSessionEvent(AssistantModeChangeEvent.Companion.getEventFromMode(mode));
        mode.logState();
    }

    public void setModeChangeThrottler(ModeChangeThrottler modeChangeThrottler) {
        this.mThrottler = modeChangeThrottler;
    }

    public void setGone() {
        getMode().onNewModeRequest(this.mEdgeLightsView, new Gone());
    }

    public void setFullListening() {
        getMode().onNewModeRequest(this.mEdgeLightsView, new FullListening(this.mContext, false));
    }

    public void addListener(EdgeLightsListener edgeLightsListener) {
        this.mEdgeLightsView.addListener(edgeLightsListener);
    }

    public EdgeLightsView.Mode getMode() {
        return this.mEdgeLightsView.getMode();
    }
}
