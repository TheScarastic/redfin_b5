package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import dagger.internal.Factory;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusService_Factory implements Factory<ColumbusService> {
    private final Provider<List<Action>> actionsProvider;
    private final Provider<Set<FeedbackEffect>> effectsProvider;
    private final Provider<Set<Gate>> gatesProvider;
    private final Provider<GestureController> gestureControllerProvider;
    private final Provider<PowerManagerWrapper> powerManagerProvider;

    public ColumbusService_Factory(Provider<List<Action>> provider, Provider<Set<FeedbackEffect>> provider2, Provider<Set<Gate>> provider3, Provider<GestureController> provider4, Provider<PowerManagerWrapper> provider5) {
        this.actionsProvider = provider;
        this.effectsProvider = provider2;
        this.gatesProvider = provider3;
        this.gestureControllerProvider = provider4;
        this.powerManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ColumbusService get() {
        return newInstance(this.actionsProvider.get(), this.effectsProvider.get(), this.gatesProvider.get(), this.gestureControllerProvider.get(), this.powerManagerProvider.get());
    }

    public static ColumbusService_Factory create(Provider<List<Action>> provider, Provider<Set<FeedbackEffect>> provider2, Provider<Set<Gate>> provider3, Provider<GestureController> provider4, Provider<PowerManagerWrapper> provider5) {
        return new ColumbusService_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ColumbusService newInstance(List<Action> list, Set<FeedbackEffect> set, Set<Gate> set2, GestureController gestureController, PowerManagerWrapper powerManagerWrapper) {
        return new ColumbusService(list, set, set2, gestureController, powerManagerWrapper);
    }
}
