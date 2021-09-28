package com.android.systemui.controls.ui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskViewFactory;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl_Factory implements Factory<ControlActionCoordinatorImpl> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<DelayableExecutor> bgExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ControlsMetricsLogger> controlsMetricsLoggerProvider;
    private final Provider<GlobalActionsComponent> globalActionsComponentProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Optional<TaskViewFactory>> taskViewFactoryProvider;
    private final Provider<ControlsUiController> uiControllerProvider;
    private final Provider<DelayableExecutor> uiExecutorProvider;

    public ControlActionCoordinatorImpl_Factory(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<ActivityStarter> provider4, Provider<KeyguardStateController> provider5, Provider<GlobalActionsComponent> provider6, Provider<Optional<TaskViewFactory>> provider7, Provider<BroadcastDispatcher> provider8, Provider<ControlsUiController> provider9, Provider<ControlsMetricsLogger> provider10) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.uiExecutorProvider = provider3;
        this.activityStarterProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.globalActionsComponentProvider = provider6;
        this.taskViewFactoryProvider = provider7;
        this.broadcastDispatcherProvider = provider8;
        this.uiControllerProvider = provider9;
        this.controlsMetricsLoggerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public ControlActionCoordinatorImpl get() {
        return newInstance(this.contextProvider.get(), this.bgExecutorProvider.get(), this.uiExecutorProvider.get(), this.activityStarterProvider.get(), this.keyguardStateControllerProvider.get(), this.globalActionsComponentProvider.get(), this.taskViewFactoryProvider.get(), this.broadcastDispatcherProvider.get(), DoubleCheck.lazy(this.uiControllerProvider), this.controlsMetricsLoggerProvider.get());
    }

    public static ControlActionCoordinatorImpl_Factory create(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<ActivityStarter> provider4, Provider<KeyguardStateController> provider5, Provider<GlobalActionsComponent> provider6, Provider<Optional<TaskViewFactory>> provider7, Provider<BroadcastDispatcher> provider8, Provider<ControlsUiController> provider9, Provider<ControlsMetricsLogger> provider10) {
        return new ControlActionCoordinatorImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static ControlActionCoordinatorImpl newInstance(Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ActivityStarter activityStarter, KeyguardStateController keyguardStateController, GlobalActionsComponent globalActionsComponent, Optional<TaskViewFactory> optional, BroadcastDispatcher broadcastDispatcher, Lazy<ControlsUiController> lazy, ControlsMetricsLogger controlsMetricsLogger) {
        return new ControlActionCoordinatorImpl(context, delayableExecutor, delayableExecutor2, activityStarter, keyguardStateController, globalActionsComponent, optional, broadcastDispatcher, lazy, controlsMetricsLogger);
    }
}
