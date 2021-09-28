package com.android.systemui.media;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.media.PlayerViewHolder;
import com.android.systemui.media.RecommendationViewHolder;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.systemui.util.animation.UniqueObjectHostViewKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaCarouselController implements Dumpable {
    private final ActivityStarter activityStarter;
    private int carouselMeasureHeight;
    private int carouselMeasureWidth;
    private final MediaCarouselController$configListener$1 configListener;
    private final Context context;
    private int currentCarouselHeight;
    private int currentCarouselWidth;
    private boolean currentlyShowingOnlyActive;
    private MediaHostState desiredHostState;
    private boolean isRtl;
    private final MediaScrollView mediaCarousel;
    private final MediaCarouselScrollHandler mediaCarouselScrollHandler;
    private final ViewGroup mediaContent;
    private final Provider<MediaControlPanel> mediaControlPanelFactory;
    private final ViewGroup mediaFrame;
    private final MediaHostStatesManager mediaHostStatesManager;
    private final MediaDataManager mediaManager;
    private boolean needsReordering;
    private final PageIndicator pageIndicator;
    private boolean playersVisible;
    private View settingsButton;
    private boolean shouldScrollToActivePlayer;
    private final SystemClock systemClock;
    public Function0<Unit> updateUserVisibility;
    private final VisualStabilityManager.Callback visualStabilityCallback;
    private final VisualStabilityManager visualStabilityManager;
    private int desiredLocation = -1;
    private int currentEndLocation = -1;
    private int currentStartLocation = -1;
    private float currentTransitionProgress = 1.0f;
    private Set<String> keysNeedRemoval = new LinkedHashSet();
    private int bgColor = getBackgroundColor();
    private boolean currentlyExpanded = true;

    public final void logSmartspaceCardReported(int i, int i2, boolean z, int i3) {
        logSmartspaceCardReported$default(this, i, i2, z, i3, 0, 16, null);
    }

    public MediaCarouselController(Context context, Provider<MediaControlPanel> provider, VisualStabilityManager visualStabilityManager, MediaHostStatesManager mediaHostStatesManager, ActivityStarter activityStarter, SystemClock systemClock, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(provider, "mediaControlPanelFactory");
        Intrinsics.checkNotNullParameter(visualStabilityManager, "visualStabilityManager");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager, "mediaHostStatesManager");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        Intrinsics.checkNotNullParameter(mediaDataManager, "mediaManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context;
        this.mediaControlPanelFactory = provider;
        this.visualStabilityManager = visualStabilityManager;
        this.mediaHostStatesManager = mediaHostStatesManager;
        this.activityStarter = activityStarter;
        this.systemClock = systemClock;
        this.mediaManager = mediaDataManager;
        MediaCarouselController$configListener$1 mediaCarouselController$configListener$1 = new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.media.MediaCarouselController$configListener$1
            final /* synthetic */ MediaCarouselController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                MediaCarouselController.access$recreatePlayers(this.this$0);
                MediaCarouselController.access$inflateSettingsButton(this.this$0);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                MediaCarouselController.access$recreatePlayers(this.this$0);
                MediaCarouselController.access$inflateSettingsButton(this.this$0);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                if (configuration != null) {
                    MediaCarouselController mediaCarouselController = this.this$0;
                    boolean z = true;
                    if (configuration.getLayoutDirection() != 1) {
                        z = false;
                    }
                    MediaCarouselController.access$setRtl(mediaCarouselController, z);
                }
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                MediaCarouselController.access$recreatePlayers(this.this$0);
                MediaCarouselController.access$inflateSettingsButton(this.this$0);
            }
        };
        this.configListener = mediaCarouselController$configListener$1;
        dumpManager.registerDumpable("MediaCarouselController", this);
        ViewGroup inflateMediaCarousel = inflateMediaCarousel();
        this.mediaFrame = inflateMediaCarousel;
        View requireViewById = inflateMediaCarousel.requireViewById(R$id.media_carousel_scroller);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "mediaFrame.requireViewById(R.id.media_carousel_scroller)");
        MediaScrollView mediaScrollView = (MediaScrollView) requireViewById;
        this.mediaCarousel = mediaScrollView;
        View requireViewById2 = inflateMediaCarousel.requireViewById(R$id.media_page_indicator);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "mediaFrame.requireViewById(R.id.media_page_indicator)");
        PageIndicator pageIndicator = (PageIndicator) requireViewById2;
        this.pageIndicator = pageIndicator;
        this.mediaCarouselScrollHandler = new MediaCarouselScrollHandler(mediaScrollView, pageIndicator, delayableExecutor, new Function0<Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController.1
            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                ((MediaCarouselController) this.receiver).onSwipeToDismiss();
            }
        }, new Function0<Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController.2
            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                ((MediaCarouselController) this.receiver).updatePageIndicatorLocation();
            }
        }, new Function1<Boolean, Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController.3
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Boolean bool) {
                invoke(bool.booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).closeGuts(z);
            }
        }, falsingCollector, falsingManager, new Function1<Boolean, Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController.4
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Boolean bool) {
                invoke(bool.booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).logSmartspaceImpression(z);
            }
        });
        setRtl(context.getResources().getConfiguration().getLayoutDirection() == 1);
        inflateSettingsButton();
        View requireViewById3 = mediaScrollView.requireViewById(R$id.media_carousel);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "mediaCarousel.requireViewById(R.id.media_carousel)");
        this.mediaContent = (ViewGroup) requireViewById3;
        configurationController.addCallback(mediaCarouselController$configListener$1);
        AnonymousClass5 r1 = new VisualStabilityManager.Callback(this) { // from class: com.android.systemui.media.MediaCarouselController.5
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.Callback
            public final void onChangeAllowed() {
                if (this.this$0.needsReordering) {
                    this.this$0.needsReordering = false;
                    this.this$0.reorderAllPlayers(null);
                }
                Set<String> set = this.this$0.keysNeedRemoval;
                MediaCarouselController mediaCarouselController = this.this$0;
                for (String str : set) {
                    MediaCarouselController.removePlayer$default(mediaCarouselController, str, false, false, 6, null);
                }
                this.this$0.keysNeedRemoval.clear();
                MediaCarouselController mediaCarouselController2 = this.this$0;
                if (mediaCarouselController2.updateUserVisibility != null) {
                    mediaCarouselController2.getUpdateUserVisibility().invoke();
                }
                this.this$0.getMediaCarouselScrollHandler().scrollToStart();
            }
        };
        this.visualStabilityCallback = r1;
        visualStabilityManager.addReorderingAllowedCallback(r1, true);
        mediaDataManager.addListener(new MediaDataManager.Listener(this) { // from class: com.android.systemui.media.MediaCarouselController.6
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
                MediaPlayerData mediaPlayerData;
                MediaControlPanel mediaPlayer;
                Intrinsics.checkNotNullParameter(str, "key");
                Intrinsics.checkNotNullParameter(mediaData, "data");
                if (this.this$0.addOrUpdatePlayer(str, str2, mediaData) && (mediaPlayer = (mediaPlayerData = MediaPlayerData.INSTANCE).getMediaPlayer(str)) != null) {
                    this.this$0.logSmartspaceCardReported(759, mediaPlayer.mInstanceId, false, mediaPlayer.getSurfaceForSmartspaceLogging(), mediaPlayerData.getMediaPlayerIndex(str));
                }
                if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && z2 && !this.this$0.getMediaCarouselScrollHandler().getQsExpanded()) {
                    MediaCarouselController mediaCarouselController = this.this$0;
                    mediaCarouselController.logSmartspaceImpression(mediaCarouselController.getMediaCarouselScrollHandler().getQsExpanded());
                }
                Boolean isPlaying = mediaData.isPlaying();
                boolean z3 = true;
                Boolean valueOf = isPlaying == null ? null : Boolean.valueOf(!isPlaying.booleanValue());
                if (!(valueOf == null ? mediaData.isClearable() : valueOf.booleanValue()) || mediaData.getActive()) {
                    z3 = false;
                }
                if (!z3 || Utils.useMediaResumption(this.this$0.context)) {
                    this.this$0.keysNeedRemoval.remove(str);
                } else if (this.this$0.visualStabilityManager.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str);
                }
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
                Intrinsics.checkNotNullParameter(str, "key");
                Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
                if (smartspaceMediaData.isActive()) {
                    this.this$0.addSmartspaceMediaRecommendations(str, smartspaceMediaData, z);
                    MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                    MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str);
                    if (mediaPlayer != null) {
                        MediaCarouselController mediaCarouselController = this.this$0;
                        mediaCarouselController.logSmartspaceCardReported(759, mediaPlayer.mInstanceId, true, mediaPlayer.getSurfaceForSmartspaceLogging(), mediaPlayerData.getMediaPlayerIndex(str));
                        if (mediaCarouselController.getMediaCarouselScrollHandler().getVisibleToUser() && mediaCarouselController.getMediaCarouselScrollHandler().getVisibleMediaIndex() == mediaPlayerData.getMediaPlayerIndex(str)) {
                            MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController, 800, mediaPlayer.mInstanceId, true, mediaPlayer.getSurfaceForSmartspaceLogging(), 0, 16, null);
                            return;
                        }
                        return;
                    }
                    return;
                }
                onSmartspaceMediaDataRemoved(smartspaceMediaData.getTargetId(), true);
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataRemoved(String str) {
                Intrinsics.checkNotNullParameter(str, "key");
                MediaCarouselController.removePlayer$default(this.this$0, str, false, false, 6, null);
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
                Intrinsics.checkNotNullParameter(str, "key");
                if (z || this.this$0.visualStabilityManager.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str);
                }
            }
        });
        inflateMediaCarousel.addOnLayoutChangeListener(new View.OnLayoutChangeListener(this) { // from class: com.android.systemui.media.MediaCarouselController.7
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                this.this$0.updatePageIndicatorLocation();
            }
        });
        mediaHostStatesManager.addCallback(new MediaHostStatesManager.Callback(this) { // from class: com.android.systemui.media.MediaCarouselController.8
            final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.media.MediaHostStatesManager.Callback
            public void onHostStateChanged(int i, MediaHostState mediaHostState) {
                Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
                if (i == this.this$0.desiredLocation) {
                    MediaCarouselController mediaCarouselController = this.this$0;
                    MediaCarouselController.onDesiredLocationChanged$default(mediaCarouselController, mediaCarouselController.desiredLocation, mediaHostState, false, 0, 0, 24, null);
                }
            }
        });
    }

    public final MediaCarouselScrollHandler getMediaCarouselScrollHandler() {
        return this.mediaCarouselScrollHandler;
    }

    public final ViewGroup getMediaFrame() {
        return this.mediaFrame;
    }

    public final void setShouldScrollToActivePlayer(boolean z) {
        this.shouldScrollToActivePlayer = z;
    }

    public final void setRtl(boolean z) {
        if (z != this.isRtl) {
            this.isRtl = z;
            this.mediaFrame.setLayoutDirection(z ? 1 : 0);
            this.mediaCarouselScrollHandler.scrollToStart();
        }
    }

    private final void setCurrentlyExpanded(boolean z) {
        if (this.currentlyExpanded != z) {
            this.currentlyExpanded = z;
            for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
                mediaControlPanel.setListening(this.currentlyExpanded);
            }
        }
    }

    public final Function0<Unit> getUpdateUserVisibility() {
        Function0<Unit> function0 = this.updateUserVisibility;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("updateUserVisibility");
        throw null;
    }

    public final void setUpdateUserVisibility(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.updateUserVisibility = function0;
    }

    public final void inflateSettingsButton() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel_settings_button, this.mediaFrame, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.View");
        View view = this.settingsButton;
        if (view != null) {
            ViewGroup viewGroup = this.mediaFrame;
            if (view != null) {
                viewGroup.removeView(view);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                throw null;
            }
        }
        this.settingsButton = inflate;
        this.mediaFrame.addView(inflate);
        this.mediaCarouselScrollHandler.onSettingsButtonUpdated(inflate);
        View view2 = this.settingsButton;
        if (view2 != null) {
            view2.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.media.MediaCarouselController$inflateSettingsButton$2
                final /* synthetic */ MediaCarouselController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    MediaCarouselController.access$getActivityStarter$p(this.this$0).startActivity(MediaCarouselControllerKt.settingsIntent, true);
                }
            });
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
    }

    private final ViewGroup inflateMediaCarousel() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel, (ViewGroup) new UniqueObjectHostView(this.context), false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        viewGroup.setLayoutDirection(3);
        return viewGroup;
    }

    public final void reorderAllPlayers(MediaPlayerData.MediaSortKey mediaSortKey) {
        Unit unit;
        RecommendationViewHolder recommendationViewHolder;
        this.mediaContent.removeAllViews();
        Iterator<MediaControlPanel> it = MediaPlayerData.INSTANCE.players().iterator();
        while (true) {
            unit = null;
            if (!it.hasNext()) {
                break;
            }
            MediaControlPanel next = it.next();
            PlayerViewHolder playerViewHolder = next.getPlayerViewHolder();
            if (playerViewHolder != null) {
                this.mediaContent.addView(playerViewHolder.getPlayer());
                unit = Unit.INSTANCE;
            }
            if (unit == null && (recommendationViewHolder = next.getRecommendationViewHolder()) != null) {
                this.mediaContent.addView(recommendationViewHolder.getRecommendations());
            }
        }
        this.mediaCarouselScrollHandler.onPlayersChanged();
        if (this.shouldScrollToActivePlayer) {
            int i = 0;
            this.shouldScrollToActivePlayer = false;
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            int firstActiveMediaIndex = mediaPlayerData.firstActiveMediaIndex();
            int i2 = -1;
            if (firstActiveMediaIndex != -1) {
                if (mediaSortKey != null) {
                    Iterator<T> it2 = mediaPlayerData.playerKeys().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        Object next2 = it2.next();
                        if (i < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        if (Intrinsics.areEqual(mediaSortKey, (MediaPlayerData.MediaSortKey) next2)) {
                            i2 = i;
                            break;
                        }
                        i++;
                    }
                    getMediaCarouselScrollHandler().scrollToPlayer(i2, firstActiveMediaIndex);
                    unit = Unit.INSTANCE;
                }
                if (unit == null) {
                    new Function0<Unit>(this, firstActiveMediaIndex) { // from class: com.android.systemui.media.MediaCarouselController$reorderAllPlayers$4
                        final /* synthetic */ int $activeMediaIndex;
                        final /* synthetic */ MediaCarouselController this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$activeMediaIndex = r2;
                        }

                        @Override // kotlin.jvm.functions.Function0
                        public final void invoke() {
                            MediaCarouselScrollHandler.scrollToPlayer$default(this.this$0.getMediaCarouselScrollHandler(), 0, this.$activeMediaIndex, 1, null);
                        }
                    };
                }
            }
        }
    }

    public final boolean addOrUpdatePlayer(String str, String str2, MediaData mediaData) {
        TransitionLayout player;
        MediaData copy$default = MediaData.copy$default(mediaData, 0, false, this.bgColor, null, null, null, null, null, null, null, null, null, null, null, false, null, false, false, null, false, null, false, 0, 8388603, null);
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        mediaPlayerData.moveIfExists(str2, str);
        MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str);
        MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex());
        if (mediaPlayer == null) {
            MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
            PlayerViewHolder.Companion companion = PlayerViewHolder.Companion;
            LayoutInflater from = LayoutInflater.from(this.context);
            Intrinsics.checkNotNullExpressionValue(from, "from(context)");
            mediaControlPanel.attachPlayer(companion.create(from, this.mediaContent));
            mediaControlPanel.getMediaViewController().setSizeChangedListener(new Function0<Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController$addOrUpdatePlayer$1
                @Override // kotlin.jvm.functions.Function0
                public final void invoke() {
                    MediaCarouselController.access$updateCarouselDimensions((MediaCarouselController) this.receiver);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            PlayerViewHolder playerViewHolder = mediaControlPanel.getPlayerViewHolder();
            if (!(playerViewHolder == null || (player = playerViewHolder.getPlayer()) == null)) {
                player.setLayoutParams(layoutParams);
            }
            mediaControlPanel.bindPlayer(copy$default, str);
            mediaControlPanel.setListening(this.currentlyExpanded);
            Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newPlayer");
            mediaPlayerData.addMediaPlayer(str, copy$default, mediaControlPanel, this.systemClock);
            Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newPlayer");
            updatePlayerToState(mediaControlPanel, true);
            reorderAllPlayers(mediaSortKey);
        } else {
            mediaPlayer.bindPlayer(copy$default, str);
            mediaPlayerData.addMediaPlayer(str, copy$default, mediaPlayer, this.systemClock);
            if (this.visualStabilityManager.isReorderingAllowed() || this.shouldScrollToActivePlayer) {
                reorderAllPlayers(mediaSortKey);
            } else {
                this.needsReordering = true;
            }
        }
        updatePageIndicator();
        this.mediaCarouselScrollHandler.onPlayersChanged();
        UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaCarousel, true);
        if (mediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
            Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
        }
        if (mediaPlayer == null) {
            return true;
        }
        return false;
    }

    public final void addSmartspaceMediaRecommendations(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        TransitionLayout recommendations;
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.getMediaPlayer(str) != null) {
            Log.w("MediaCarouselController", "Skip adding smartspace target in carousel");
            return;
        }
        String smartspaceMediaKey = mediaPlayerData.smartspaceMediaKey();
        if (smartspaceMediaKey != null) {
            mediaPlayerData.removeMediaPlayer(smartspaceMediaKey);
        }
        MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
        RecommendationViewHolder.Companion companion = RecommendationViewHolder.Companion;
        LayoutInflater from = LayoutInflater.from(this.context);
        Intrinsics.checkNotNullExpressionValue(from, "from(context)");
        mediaControlPanel.attachRecommendation(companion.create(from, this.mediaContent));
        mediaControlPanel.getMediaViewController().setSizeChangedListener(new Function0<Unit>(this) { // from class: com.android.systemui.media.MediaCarouselController$addSmartspaceMediaRecommendations$2
            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                MediaCarouselController.access$updateCarouselDimensions((MediaCarouselController) this.receiver);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        RecommendationViewHolder recommendationViewHolder = mediaControlPanel.getRecommendationViewHolder();
        if (!(recommendationViewHolder == null || (recommendations = recommendationViewHolder.getRecommendations()) == null)) {
            recommendations.setLayoutParams(layoutParams);
        }
        mediaControlPanel.bindRecommendation(SmartspaceMediaData.copy$default(smartspaceMediaData, null, false, false, null, null, null, this.bgColor, 63, null));
        Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newRecs");
        mediaPlayerData.addMediaRecommendation(str, smartspaceMediaData, mediaControlPanel, z, this.systemClock);
        Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "newRecs");
        updatePlayerToState(mediaControlPanel, true);
        reorderAllPlayers((MediaPlayerData.MediaSortKey) CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), this.mediaCarouselScrollHandler.getVisibleMediaIndex()));
        updatePageIndicator();
        UniqueObjectHostViewKt.setRequiresRemeasuring(this.mediaCarousel, true);
        if (mediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
            Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
        }
    }

    public static /* synthetic */ void removePlayer$default(MediaCarouselController mediaCarouselController, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        if ((i & 4) != 0) {
            z2 = true;
        }
        mediaCarouselController.removePlayer(str, z, z2);
    }

    public final void removePlayer(String str, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaControlPanel removeMediaPlayer = MediaPlayerData.INSTANCE.removeMediaPlayer(str);
        if (removeMediaPlayer != null) {
            getMediaCarouselScrollHandler().onPrePlayerRemoved(removeMediaPlayer);
            ViewGroup viewGroup = this.mediaContent;
            PlayerViewHolder playerViewHolder = removeMediaPlayer.getPlayerViewHolder();
            TransitionLayout transitionLayout = null;
            viewGroup.removeView(playerViewHolder == null ? null : playerViewHolder.getPlayer());
            ViewGroup viewGroup2 = this.mediaContent;
            RecommendationViewHolder recommendationViewHolder = removeMediaPlayer.getRecommendationViewHolder();
            if (recommendationViewHolder != null) {
                transitionLayout = recommendationViewHolder.getRecommendations();
            }
            viewGroup2.removeView(transitionLayout);
            removeMediaPlayer.onDestroy();
            getMediaCarouselScrollHandler().onPlayersChanged();
            updatePageIndicator();
            if (z) {
                this.mediaManager.dismissMediaData(str, 0);
            }
            if (z2) {
                this.mediaManager.dismissSmartspaceRecommendation(str, 0);
            }
        }
    }

    public final void recreatePlayers() {
        this.bgColor = getBackgroundColor();
        this.pageIndicator.setTintList(ColorStateList.valueOf(getForegroundColor()));
        Iterator<T> it = MediaPlayerData.INSTANCE.mediaData().iterator();
        while (it.hasNext()) {
            Triple triple = (Triple) it.next();
            String str = (String) triple.component1();
            MediaData mediaData = (MediaData) triple.component2();
            if (((Boolean) triple.component3()).booleanValue()) {
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                SmartspaceMediaData smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                removePlayer(str, false, false);
                if (smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core != null) {
                    addSmartspaceMediaRecommendations(smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getTargetId(), smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core, mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
                }
            } else {
                removePlayer(str, false, false);
                addOrUpdatePlayer(str, null, mediaData);
            }
        }
    }

    private final int getBackgroundColor() {
        return this.context.getColor(17170502);
    }

    private final int getForegroundColor() {
        return this.context.getColor(17170511);
    }

    private final void updatePageIndicator() {
        int childCount = this.mediaContent.getChildCount();
        this.pageIndicator.setNumPages(childCount);
        if (childCount == 1) {
            this.pageIndicator.setLocation(0.0f);
        }
        updatePageIndicatorAlpha();
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        if (i == this.currentStartLocation && i2 == this.currentEndLocation) {
            if ((f == this.currentTransitionProgress) && !z) {
                return;
            }
        }
        this.currentStartLocation = i;
        this.currentEndLocation = i2;
        this.currentTransitionProgress = f;
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            Intrinsics.checkNotNullExpressionValue(mediaControlPanel, "mediaPlayer");
            updatePlayerToState(mediaControlPanel, z);
        }
        maybeResetSettingsCog();
        updatePageIndicatorAlpha();
    }

    private final void updatePageIndicatorAlpha() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean z = false;
        boolean visible = mediaHostState == null ? false : mediaHostState.getVisible();
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        if (mediaHostState2 != null) {
            z = mediaHostState2.getVisible();
        }
        float f = 1.0f;
        float f2 = z ? 1.0f : 0.0f;
        float f3 = visible ? 1.0f : 0.0f;
        if (!visible || !z) {
            float f4 = this.currentTransitionProgress;
            if (!visible) {
                f4 = 1.0f - f4;
            }
            f = MathUtils.lerp(f2, f3, MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 0.0f, 1.0f, f4), 0.0f, 1.0f));
        }
        this.pageIndicator.setAlpha(f);
    }

    public final void updatePageIndicatorLocation() {
        int i;
        int i2;
        if (this.isRtl) {
            i2 = this.pageIndicator.getWidth();
            i = this.currentCarouselWidth;
        } else {
            i2 = this.currentCarouselWidth;
            i = this.pageIndicator.getWidth();
        }
        this.pageIndicator.setTranslationX((((float) (i2 - i)) / 2.0f) + this.mediaCarouselScrollHandler.getContentTranslation());
        ViewGroup.LayoutParams layoutParams = this.pageIndicator.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        PageIndicator pageIndicator = this.pageIndicator;
        pageIndicator.setTranslationY((float) ((this.currentCarouselHeight - pageIndicator.getHeight()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin));
    }

    public final void updateCarouselDimensions() {
        int i = 0;
        int i2 = 0;
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            MediaViewController mediaViewController = mediaControlPanel.getMediaViewController();
            Intrinsics.checkNotNullExpressionValue(mediaViewController, "mediaPlayer.mediaViewController");
            i = Math.max(i, mediaViewController.getCurrentWidth() + ((int) mediaViewController.getTranslationX()));
            i2 = Math.max(i2, mediaViewController.getCurrentHeight() + ((int) mediaViewController.getTranslationY()));
        }
        if (!(i == this.currentCarouselWidth && i2 == this.currentCarouselHeight)) {
            this.currentCarouselWidth = i;
            this.currentCarouselHeight = i2;
            this.mediaCarouselScrollHandler.setCarouselBounds(i, i2);
            updatePageIndicatorLocation();
        }
    }

    private final void maybeResetSettingsCog() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean showsOnlyActiveMedia = mediaHostState == null ? true : mediaHostState.getShowsOnlyActiveMedia();
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        boolean showsOnlyActiveMedia2 = mediaHostState2 == null ? showsOnlyActiveMedia : mediaHostState2.getShowsOnlyActiveMedia();
        if (this.currentlyShowingOnlyActive == showsOnlyActiveMedia) {
            float f = this.currentTransitionProgress;
            boolean z = false;
            if (!(f == 1.0f)) {
                if (f == 0.0f) {
                    z = true;
                }
                if (z || showsOnlyActiveMedia2 == showsOnlyActiveMedia) {
                    return;
                }
            } else {
                return;
            }
        }
        this.currentlyShowingOnlyActive = showsOnlyActiveMedia;
        this.mediaCarouselScrollHandler.resetTranslation(true);
    }

    private final void updatePlayerToState(MediaControlPanel mediaControlPanel, boolean z) {
        mediaControlPanel.getMediaViewController().setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
    }

    public static /* synthetic */ void onDesiredLocationChanged$default(MediaCarouselController mediaCarouselController, int i, MediaHostState mediaHostState, boolean z, long j, long j2, int i2, Object obj) {
        mediaCarouselController.onDesiredLocationChanged(i, mediaHostState, z, (i2 & 8) != 0 ? 200 : j, (i2 & 16) != 0 ? 0 : j2);
    }

    public final void onDesiredLocationChanged(int i, MediaHostState mediaHostState, boolean z, long j, long j2) {
        if (mediaHostState != null) {
            this.desiredLocation = i;
            this.desiredHostState = mediaHostState;
            setCurrentlyExpanded(mediaHostState.getExpansion() > 0.0f);
            boolean z2 = !this.currentlyExpanded && !this.mediaManager.hasActiveMedia() && mediaHostState.getShowsOnlyActiveMedia();
            for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
                if (z) {
                    mediaControlPanel.getMediaViewController().animatePendingStateChange(j, j2);
                }
                if (z2 && mediaControlPanel.getMediaViewController().isGutsVisible()) {
                    mediaControlPanel.closeGuts(!z);
                }
                mediaControlPanel.getMediaViewController().onLocationPreChange(i);
            }
            getMediaCarouselScrollHandler().setShowsSettingsButton(!mediaHostState.getShowsOnlyActiveMedia());
            getMediaCarouselScrollHandler().setFalsingProtectionNeeded(mediaHostState.getFalsingProtectionNeeded());
            boolean visible = mediaHostState.getVisible();
            if (visible != this.playersVisible) {
                this.playersVisible = visible;
                if (visible) {
                    MediaCarouselScrollHandler.resetTranslation$default(getMediaCarouselScrollHandler(), false, 1, null);
                }
            }
            updateCarouselSize();
        }
    }

    public static /* synthetic */ void closeGuts$default(MediaCarouselController mediaCarouselController, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        mediaCarouselController.closeGuts(z);
    }

    public final void closeGuts(boolean z) {
        for (MediaControlPanel mediaControlPanel : MediaPlayerData.INSTANCE.players()) {
            mediaControlPanel.closeGuts(z);
        }
    }

    private final void updateCarouselSize() {
        MediaHostState mediaHostState = this.desiredHostState;
        MeasurementInput measurementInput = null;
        MeasurementInput measurementInput2 = mediaHostState == null ? null : mediaHostState.getMeasurementInput();
        int width = measurementInput2 == null ? 0 : measurementInput2.getWidth();
        MediaHostState mediaHostState2 = this.desiredHostState;
        MeasurementInput measurementInput3 = mediaHostState2 == null ? null : mediaHostState2.getMeasurementInput();
        int height = measurementInput3 == null ? 0 : measurementInput3.getHeight();
        if ((width != this.carouselMeasureWidth && width != 0) || (height != this.carouselMeasureHeight && height != 0)) {
            this.carouselMeasureWidth = width;
            this.carouselMeasureHeight = height;
            int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R$dimen.qs_media_padding) + width;
            MediaHostState mediaHostState3 = this.desiredHostState;
            MeasurementInput measurementInput4 = mediaHostState3 == null ? null : mediaHostState3.getMeasurementInput();
            int widthMeasureSpec = measurementInput4 == null ? 0 : measurementInput4.getWidthMeasureSpec();
            MediaHostState mediaHostState4 = this.desiredHostState;
            if (mediaHostState4 != null) {
                measurementInput = mediaHostState4.getMeasurementInput();
            }
            this.mediaCarousel.measure(widthMeasureSpec, measurementInput == null ? 0 : measurementInput.getHeightMeasureSpec());
            MediaScrollView mediaScrollView = this.mediaCarousel;
            mediaScrollView.layout(0, 0, width, mediaScrollView.getMeasuredHeight());
            this.mediaCarouselScrollHandler.setPlayerWidthPlusPadding(dimensionPixelSize);
        }
    }

    public final void logSmartspaceImpression(boolean z) {
        int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.players().size() > visibleMediaIndex) {
            Object elementAt = CollectionsKt.elementAt(mediaPlayerData.players(), visibleMediaIndex);
            Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.players().elementAt(visibleMediaIndex)");
            MediaControlPanel mediaControlPanel = (MediaControlPanel) elementAt;
            boolean hasActiveMediaOrRecommendationCard = mediaPlayerData.hasActiveMediaOrRecommendationCard();
            boolean z2 = mediaControlPanel.getRecommendationViewHolder() != null;
            if (hasActiveMediaOrRecommendationCard || z) {
                logSmartspaceCardReported$default(this, 800, mediaControlPanel.mInstanceId, z2, mediaControlPanel.getSurfaceForSmartspaceLogging(), 0, 16, null);
            }
        }
    }

    public static /* synthetic */ void logSmartspaceCardReported$default(MediaCarouselController mediaCarouselController, int i, int i2, boolean z, int i3, int i4, int i5, Object obj) {
        if ((i5 & 16) != 0) {
            i4 = mediaCarouselController.mediaCarouselScrollHandler.getVisibleMediaIndex();
        }
        mediaCarouselController.logSmartspaceCardReported(i, i2, z, i3, i4);
    }

    public final void logSmartspaceCardReported(int i, int i2, boolean z, int i3, int i4) {
        if (z || this.mediaManager.getSmartspaceMediaData().isActive() || MediaPlayerData.INSTANCE.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() != null) {
            SysUiStatsLog.write(352, i, i2, z ? 9 : 8, i3, i4, this.mediaContent.getChildCount());
        }
    }

    public final void onSwipeToDismiss() {
        Collection<MediaControlPanel> players = MediaPlayerData.INSTANCE.players();
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = players.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (((MediaControlPanel) next).getRecommendationViewHolder() != null) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            logSmartspaceCardReported(761, ((MediaControlPanel) arrayList.get(0)).mInstanceId, true, ((MediaControlPanel) arrayList.get(0)).getSurfaceForSmartspaceLogging(), -1);
        } else {
            int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            if (mediaPlayerData.players().size() > visibleMediaIndex) {
                Object elementAt = CollectionsKt.elementAt(mediaPlayerData.players(), visibleMediaIndex);
                Intrinsics.checkNotNullExpressionValue(elementAt, "MediaPlayerData.players().elementAt(visibleMediaIndex)");
                MediaControlPanel mediaControlPanel = (MediaControlPanel) elementAt;
                logSmartspaceCardReported(761, mediaControlPanel.mInstanceId, false, mediaControlPanel.getSurfaceForSmartspaceLogging(), -1);
            }
        }
        this.mediaManager.onSwipeToDismiss();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("keysNeedRemoval: ", this.keysNeedRemoval));
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        printWriter.println(Intrinsics.stringPlus("playerKeys: ", mediaPlayerData.playerKeys()));
        printWriter.println(Intrinsics.stringPlus("smartspaceMediaData: ", mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        printWriter.println(Intrinsics.stringPlus("shouldPrioritizeSs: ", Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
    }
}
