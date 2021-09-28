package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaControlPanel_Factory implements Factory<MediaControlPanel> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    private final Provider<MediaCarouselController> mediaCarouselControllerProvider;
    private final Provider<MediaDataManager> mediaDataManagerProvider;
    private final Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
    private final Provider<MediaViewController> mediaViewControllerProvider;
    private final Provider<SeekBarViewModel> seekBarViewModelProvider;

    public MediaControlPanel_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<ActivityStarter> provider3, Provider<MediaViewController> provider4, Provider<SeekBarViewModel> provider5, Provider<MediaDataManager> provider6, Provider<KeyguardDismissUtil> provider7, Provider<MediaOutputDialogFactory> provider8, Provider<MediaCarouselController> provider9) {
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.activityStarterProvider = provider3;
        this.mediaViewControllerProvider = provider4;
        this.seekBarViewModelProvider = provider5;
        this.mediaDataManagerProvider = provider6;
        this.keyguardDismissUtilProvider = provider7;
        this.mediaOutputDialogFactoryProvider = provider8;
        this.mediaCarouselControllerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public MediaControlPanel get() {
        return newInstance(this.contextProvider.get(), this.backgroundExecutorProvider.get(), this.activityStarterProvider.get(), this.mediaViewControllerProvider.get(), this.seekBarViewModelProvider.get(), DoubleCheck.lazy(this.mediaDataManagerProvider), this.keyguardDismissUtilProvider.get(), this.mediaOutputDialogFactoryProvider.get(), this.mediaCarouselControllerProvider.get());
    }

    public static MediaControlPanel_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<ActivityStarter> provider3, Provider<MediaViewController> provider4, Provider<SeekBarViewModel> provider5, Provider<MediaDataManager> provider6, Provider<KeyguardDismissUtil> provider7, Provider<MediaOutputDialogFactory> provider8, Provider<MediaCarouselController> provider9) {
        return new MediaControlPanel_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static MediaControlPanel newInstance(Context context, Executor executor, ActivityStarter activityStarter, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, KeyguardDismissUtil keyguardDismissUtil, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController) {
        return new MediaControlPanel(context, executor, activityStarter, mediaViewController, seekBarViewModel, lazy, keyguardDismissUtil, mediaOutputDialogFactory, mediaCarouselController);
    }
}
