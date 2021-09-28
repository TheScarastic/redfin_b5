package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import com.android.systemui.assist.ui.DefaultUiController;
import com.google.android.systemui.assist.GoogleAssistLogger;
/* loaded from: classes2.dex */
public class GoogleDefaultUiController extends DefaultUiController {
    public GoogleDefaultUiController(Context context, GoogleAssistLogger googleAssistLogger) {
        super(context, googleAssistLogger);
        context.getResources();
        setGoogleAssistant(false);
        AssistantInvocationLightsView assistantInvocationLightsView = (AssistantInvocationLightsView) LayoutInflater.from(context).inflate(R$layout.invocation_lights, (ViewGroup) this.mRoot, false);
        this.mInvocationLightsView = assistantInvocationLightsView;
        this.mRoot.addView(assistantInvocationLightsView);
    }

    public void setGoogleAssistant(boolean z) {
        ((AssistantInvocationLightsView) this.mInvocationLightsView).setGoogleAssistant(z);
    }
}
