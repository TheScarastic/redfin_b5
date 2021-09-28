package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory implements Factory<Set<NgaMessageHandler.EdgeLightsInfoListener>> {
    private final Provider<EdgeLightsController> edgeLightsControllerProvider;
    private final Provider<NgaInputHandler> ngaInputHandlerProvider;

    public AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory(Provider<EdgeLightsController> provider, Provider<NgaInputHandler> provider2) {
        this.edgeLightsControllerProvider = provider;
        this.ngaInputHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Set<NgaMessageHandler.EdgeLightsInfoListener> get() {
        return bindEdgeLightsInfoListeners(this.edgeLightsControllerProvider.get(), this.ngaInputHandlerProvider.get());
    }

    public static AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory create(Provider<EdgeLightsController> provider, Provider<NgaInputHandler> provider2) {
        return new AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory(provider, provider2);
    }

    public static Set<NgaMessageHandler.EdgeLightsInfoListener> bindEdgeLightsInfoListeners(EdgeLightsController edgeLightsController, NgaInputHandler ngaInputHandler) {
        return (Set) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.bindEdgeLightsInfoListeners(edgeLightsController, ngaInputHandler));
    }
}
