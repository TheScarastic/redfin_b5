package com.android.systemui.dagger;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory implements Factory<AccessibilityFloatingMenuController> {
    private final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    private final Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<AccessibilityButtonTargetsObserver> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
        this.accessibilityButtonTargetsObserverProvider = provider2;
        this.accessibilityButtonModeObserverProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public AccessibilityFloatingMenuController get() {
        return provideAccessibilityFloatingMenuController(this.module, this.contextProvider.get(), this.accessibilityButtonTargetsObserverProvider.get(), this.accessibilityButtonModeObserverProvider.get(), this.keyguardUpdateMonitorProvider.get());
    }

    public static DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory create(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<AccessibilityButtonTargetsObserver> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        return new DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory(dependencyProvider, provider, provider2, provider3, provider4);
    }

    public static AccessibilityFloatingMenuController provideAccessibilityFloatingMenuController(DependencyProvider dependencyProvider, Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return (AccessibilityFloatingMenuController) Preconditions.checkNotNullFromProvides(dependencyProvider.provideAccessibilityFloatingMenuController(context, accessibilityButtonTargetsObserver, accessibilityButtonModeObserver, keyguardUpdateMonitor));
    }
}
