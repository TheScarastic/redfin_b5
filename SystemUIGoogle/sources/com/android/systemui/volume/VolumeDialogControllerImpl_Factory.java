package com.android.systemui.volume;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.os.Vibrator;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VolumeDialogControllerImpl_Factory implements Factory<VolumeDialogControllerImpl> {
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<IAudioService> iAudioServiceProvider;
    private final Provider<NotificationManager> notificationManagerProvider;
    private final Provider<Optional<Vibrator>> optionalVibratorProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<RingerModeTracker> ringerModeTrackerProvider;
    private final Provider<ThreadFactory> theadFactoryProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public VolumeDialogControllerImpl_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<RingerModeTracker> provider3, Provider<ThreadFactory> provider4, Provider<AudioManager> provider5, Provider<NotificationManager> provider6, Provider<Optional<Vibrator>> provider7, Provider<IAudioService> provider8, Provider<AccessibilityManager> provider9, Provider<PackageManager> provider10, Provider<WakefulnessLifecycle> provider11) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.ringerModeTrackerProvider = provider3;
        this.theadFactoryProvider = provider4;
        this.audioManagerProvider = provider5;
        this.notificationManagerProvider = provider6;
        this.optionalVibratorProvider = provider7;
        this.iAudioServiceProvider = provider8;
        this.accessibilityManagerProvider = provider9;
        this.packageManagerProvider = provider10;
        this.wakefulnessLifecycleProvider = provider11;
    }

    @Override // javax.inject.Provider
    public VolumeDialogControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.ringerModeTrackerProvider.get(), this.theadFactoryProvider.get(), this.audioManagerProvider.get(), this.notificationManagerProvider.get(), this.optionalVibratorProvider.get(), this.iAudioServiceProvider.get(), this.accessibilityManagerProvider.get(), this.packageManagerProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static VolumeDialogControllerImpl_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<RingerModeTracker> provider3, Provider<ThreadFactory> provider4, Provider<AudioManager> provider5, Provider<NotificationManager> provider6, Provider<Optional<Vibrator>> provider7, Provider<IAudioService> provider8, Provider<AccessibilityManager> provider9, Provider<PackageManager> provider10, Provider<WakefulnessLifecycle> provider11) {
        return new VolumeDialogControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static VolumeDialogControllerImpl newInstance(Context context, BroadcastDispatcher broadcastDispatcher, RingerModeTracker ringerModeTracker, ThreadFactory threadFactory, AudioManager audioManager, NotificationManager notificationManager, Optional<Vibrator> optional, IAudioService iAudioService, AccessibilityManager accessibilityManager, PackageManager packageManager, WakefulnessLifecycle wakefulnessLifecycle) {
        return new VolumeDialogControllerImpl(context, broadcastDispatcher, ringerModeTracker, threadFactory, audioManager, notificationManager, optional, iAudioService, accessibilityManager, packageManager, wakefulnessLifecycle);
    }
}
