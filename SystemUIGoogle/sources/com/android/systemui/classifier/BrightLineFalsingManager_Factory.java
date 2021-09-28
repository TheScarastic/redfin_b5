package com.android.systemui.classifier;

import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightLineFalsingManager_Factory implements Factory<BrightLineFalsingManager> {
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<Set<FalsingClassifier>> classifiersProvider;
    private final Provider<DoubleTapClassifier> doubleTapClassifierProvider;
    private final Provider<FalsingDataProvider> falsingDataProvider;
    private final Provider<HistoryTracker> historyTrackerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<SingleTapClassifier> singleTapClassifierProvider;
    private final Provider<Boolean> testHarnessProvider;

    public BrightLineFalsingManager_Factory(Provider<FalsingDataProvider> provider, Provider<MetricsLogger> provider2, Provider<Set<FalsingClassifier>> provider3, Provider<SingleTapClassifier> provider4, Provider<DoubleTapClassifier> provider5, Provider<HistoryTracker> provider6, Provider<KeyguardStateController> provider7, Provider<AccessibilityManager> provider8, Provider<Boolean> provider9) {
        this.falsingDataProvider = provider;
        this.metricsLoggerProvider = provider2;
        this.classifiersProvider = provider3;
        this.singleTapClassifierProvider = provider4;
        this.doubleTapClassifierProvider = provider5;
        this.historyTrackerProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.accessibilityManagerProvider = provider8;
        this.testHarnessProvider = provider9;
    }

    @Override // javax.inject.Provider
    public BrightLineFalsingManager get() {
        return newInstance(this.falsingDataProvider.get(), this.metricsLoggerProvider.get(), this.classifiersProvider.get(), this.singleTapClassifierProvider.get(), this.doubleTapClassifierProvider.get(), this.historyTrackerProvider.get(), this.keyguardStateControllerProvider.get(), this.accessibilityManagerProvider.get(), this.testHarnessProvider.get().booleanValue());
    }

    public static BrightLineFalsingManager_Factory create(Provider<FalsingDataProvider> provider, Provider<MetricsLogger> provider2, Provider<Set<FalsingClassifier>> provider3, Provider<SingleTapClassifier> provider4, Provider<DoubleTapClassifier> provider5, Provider<HistoryTracker> provider6, Provider<KeyguardStateController> provider7, Provider<AccessibilityManager> provider8, Provider<Boolean> provider9) {
        return new BrightLineFalsingManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static BrightLineFalsingManager newInstance(FalsingDataProvider falsingDataProvider, MetricsLogger metricsLogger, Set<FalsingClassifier> set, SingleTapClassifier singleTapClassifier, DoubleTapClassifier doubleTapClassifier, HistoryTracker historyTracker, KeyguardStateController keyguardStateController, AccessibilityManager accessibilityManager, boolean z) {
        return new BrightLineFalsingManager(falsingDataProvider, metricsLogger, set, singleTapClassifier, doubleTapClassifier, historyTracker, keyguardStateController, accessibilityManager, z);
    }
}
