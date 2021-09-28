package com.android.systemui.media;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$id;
import com.android.systemui.R$xml;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionLayoutController;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.animation.WidgetState;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class MediaViewController {
    public static final Companion Companion = new Companion(null);
    public static final long GUTS_ANIMATION_DURATION = 500;
    private boolean animateNextStateChange;
    private long animationDelay;
    private long animationDuration;
    private final ConfigurationController configurationController;
    private final MediaViewController$configurationListener$1 configurationListener;
    private final Context context;
    private int currentHeight;
    private int currentWidth;
    private boolean isGutsVisible;
    private final TransitionLayoutController layoutController;
    private final MediaHostStatesManager mediaHostStatesManager;
    private boolean shouldHideGutsSettings;
    public Function0<Unit> sizeChangedListener;
    private TransitionLayout transitionLayout;
    private boolean firstRefresh = true;
    private final MeasurementOutput measurement = new MeasurementOutput(0, 0);
    private TYPE type = TYPE.PLAYER;
    private final Map<CacheKey, TransitionViewState> viewStates = new LinkedHashMap();
    private int currentEndLocation = -1;
    private int currentStartLocation = -1;
    private float currentTransitionProgress = 1.0f;
    private final TransitionViewState tmpState = new TransitionViewState();
    private final TransitionViewState tmpState2 = new TransitionViewState();
    private final TransitionViewState tmpState3 = new TransitionViewState();
    private final CacheKey tmpKey = new CacheKey(0, 0, 0.0f, false, 15, null);
    private final MediaHostStatesManager.Callback stateCallback = new MediaHostStatesManager.Callback(this) { // from class: com.android.systemui.media.MediaViewController$stateCallback$1
        final /* synthetic */ MediaViewController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.media.MediaHostStatesManager.Callback
        public void onHostStateChanged(int i, MediaHostState mediaHostState) {
            Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
            if (i == this.this$0.getCurrentEndLocation() || i == this.this$0.currentStartLocation) {
                MediaViewController mediaViewController = this.this$0;
                mediaViewController.setCurrentState(mediaViewController.currentStartLocation, this.this$0.getCurrentEndLocation(), this.this$0.currentTransitionProgress, false);
            }
        }
    };
    private final ConstraintSet collapsedLayout = new ConstraintSet();
    private final ConstraintSet expandedLayout = new ConstraintSet();

    /* compiled from: MediaViewController.kt */
    /* loaded from: classes.dex */
    public enum TYPE {
        PLAYER,
        RECOMMENDATION
    }

    public MediaViewController(Context context, ConfigurationController configurationController, MediaHostStatesManager mediaHostStatesManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(mediaHostStatesManager, "mediaHostStatesManager");
        this.context = context;
        this.configurationController = configurationController;
        this.mediaHostStatesManager = mediaHostStatesManager;
        TransitionLayoutController transitionLayoutController = new TransitionLayoutController();
        this.layoutController = transitionLayoutController;
        MediaViewController$configurationListener$1 mediaViewController$configurationListener$1 = new MediaViewController$configurationListener$1(this);
        this.configurationListener = mediaViewController$configurationListener$1;
        mediaHostStatesManager.addController(this);
        transitionLayoutController.setSizeChangedListener(new Function2<Integer, Integer, Unit>(this) { // from class: com.android.systemui.media.MediaViewController.1
            final /* synthetic */ MediaViewController this$0;

            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Unit invoke(Integer num, Integer num2) {
                invoke(num.intValue(), num2.intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int i, int i2) {
                this.this$0.setCurrentWidth(i);
                this.this$0.setCurrentHeight(i2);
                this.this$0.getSizeChangedListener().invoke();
            }
        });
        configurationController.addCallback(mediaViewController$configurationListener$1);
    }

    /* compiled from: MediaViewController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final Function0<Unit> getSizeChangedListener() {
        Function0<Unit> function0 = this.sizeChangedListener;
        if (function0 != null) {
            return function0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sizeChangedListener");
        throw null;
    }

    public final void setSizeChangedListener(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.sizeChangedListener = function0;
    }

    public final int getCurrentEndLocation() {
        return this.currentEndLocation;
    }

    public final int getCurrentWidth() {
        return this.currentWidth;
    }

    public final void setCurrentWidth(int i) {
        this.currentWidth = i;
    }

    public final int getCurrentHeight() {
        return this.currentHeight;
    }

    public final void setCurrentHeight(int i) {
        this.currentHeight = i;
    }

    public final float getTranslationX() {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout == null) {
            return 0.0f;
        }
        return transitionLayout.getTranslationX();
    }

    public final float getTranslationY() {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout == null) {
            return 0.0f;
        }
        return transitionLayout.getTranslationY();
    }

    public final MediaHostStatesManager.Callback getStateCallback() {
        return this.stateCallback;
    }

    public final ConstraintSet getCollapsedLayout() {
        return this.collapsedLayout;
    }

    public final ConstraintSet getExpandedLayout() {
        return this.expandedLayout;
    }

    public final boolean isGutsVisible() {
        return this.isGutsVisible;
    }

    public final void setShouldHideGutsSettings(boolean z) {
        this.shouldHideGutsSettings = z;
    }

    public final void onDestroy() {
        this.mediaHostStatesManager.removeController(this);
        this.configurationController.removeCallback(this.configurationListener);
    }

    public final void openGuts() {
        if (!this.isGutsVisible) {
            this.isGutsVisible = true;
            animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, false);
        }
    }

    public final void closeGuts(boolean z) {
        if (this.isGutsVisible) {
            this.isGutsVisible = false;
            if (!z) {
                animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            }
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
        }
    }

    private final void ensureAllMeasurements() {
        for (Map.Entry<Integer, MediaHostState> entry : this.mediaHostStatesManager.getMediaHostStates().entrySet()) {
            obtainViewState(entry.getValue());
        }
    }

    private final ConstraintSet constraintSetForExpansion(float f) {
        return f > 0.0f ? this.expandedLayout : this.collapsedLayout;
    }

    private final void setGutsViewState(TransitionViewState transitionViewState) {
        WidgetState widgetState;
        if (this.type == TYPE.PLAYER) {
            for (Number number : PlayerViewHolder.Companion.getControlsIds()) {
                WidgetState widgetState2 = transitionViewState.getWidgetStates().get(Integer.valueOf(number.intValue()));
                if (widgetState2 != null) {
                    widgetState2.setAlpha(isGutsVisible() ? 0.0f : widgetState2.getAlpha());
                    widgetState2.setGone(isGutsVisible() ? true : widgetState2.getGone());
                }
            }
            for (Number number2 : PlayerViewHolder.Companion.getGutsIds()) {
                int intValue = number2.intValue();
                WidgetState widgetState3 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue));
                if (widgetState3 != null) {
                    widgetState3.setAlpha(isGutsVisible() ? 1.0f : 0.0f);
                }
                WidgetState widgetState4 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue));
                if (widgetState4 != null) {
                    widgetState4.setGone(!isGutsVisible());
                }
            }
        } else {
            for (Number number3 : RecommendationViewHolder.Companion.getControlsIds()) {
                WidgetState widgetState5 = transitionViewState.getWidgetStates().get(Integer.valueOf(number3.intValue()));
                if (widgetState5 != null) {
                    widgetState5.setAlpha(isGutsVisible() ? 0.0f : widgetState5.getAlpha());
                    widgetState5.setGone(isGutsVisible() ? true : widgetState5.getGone());
                }
            }
            for (Number number4 : RecommendationViewHolder.Companion.getGutsIds()) {
                int intValue2 = number4.intValue();
                WidgetState widgetState6 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue2));
                if (widgetState6 != null) {
                    widgetState6.setAlpha(isGutsVisible() ? 1.0f : 0.0f);
                }
                WidgetState widgetState7 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue2));
                if (widgetState7 != null) {
                    widgetState7.setGone(!isGutsVisible());
                }
            }
        }
        if (this.shouldHideGutsSettings && (widgetState = transitionViewState.getWidgetStates().get(Integer.valueOf(R$id.settings))) != null) {
            widgetState.setGone(true);
        }
    }

    private final TransitionViewState obtainViewState(MediaHostState mediaHostState) {
        if (mediaHostState == null || mediaHostState.getMeasurementInput() == null) {
            return null;
        }
        CacheKey key = getKey(mediaHostState, this.isGutsVisible, this.tmpKey);
        TransitionViewState transitionViewState = this.viewStates.get(key);
        if (transitionViewState != null) {
            return transitionViewState;
        }
        CacheKey copy$default = CacheKey.copy$default(key, 0, 0, 0.0f, false, 15, null);
        if (this.transitionLayout == null) {
            return null;
        }
        boolean z = true;
        if (!(mediaHostState.getExpansion() == 0.0f)) {
            if (mediaHostState.getExpansion() != 1.0f) {
                z = false;
            }
            if (!z) {
                MediaHostState copy = mediaHostState.copy();
                copy.setExpansion(0.0f);
                TransitionViewState obtainViewState = obtainViewState(copy);
                Objects.requireNonNull(obtainViewState, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                MediaHostState copy2 = mediaHostState.copy();
                copy2.setExpansion(1.0f);
                TransitionViewState obtainViewState2 = obtainViewState(copy2);
                Objects.requireNonNull(obtainViewState2, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                return TransitionLayoutController.getInterpolatedState$default(this.layoutController, obtainViewState, obtainViewState2, mediaHostState.getExpansion(), null, 8, null);
            }
        }
        TransitionLayout transitionLayout = this.transitionLayout;
        Intrinsics.checkNotNull(transitionLayout);
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        Intrinsics.checkNotNull(measurementInput);
        TransitionViewState calculateViewState = transitionLayout.calculateViewState(measurementInput, constraintSetForExpansion(mediaHostState.getExpansion()), new TransitionViewState());
        setGutsViewState(calculateViewState);
        this.viewStates.put(copy$default, calculateViewState);
        return calculateViewState;
    }

    private final CacheKey getKey(MediaHostState mediaHostState, boolean z, CacheKey cacheKey) {
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        int i = 0;
        cacheKey.setHeightMeasureSpec(measurementInput == null ? 0 : measurementInput.getHeightMeasureSpec());
        MeasurementInput measurementInput2 = mediaHostState.getMeasurementInput();
        if (measurementInput2 != null) {
            i = measurementInput2.getWidthMeasureSpec();
        }
        cacheKey.setWidthMeasureSpec(i);
        cacheKey.setExpansion(mediaHostState.getExpansion());
        cacheKey.setGutsVisible(z);
        return cacheKey;
    }

    public final void attach(TransitionLayout transitionLayout, TYPE type) {
        Intrinsics.checkNotNullParameter(transitionLayout, "transitionLayout");
        Intrinsics.checkNotNullParameter(type, "type");
        updateMediaViewControllerType(type);
        this.transitionLayout = transitionLayout;
        this.layoutController.attach(transitionLayout);
        int i = this.currentEndLocation;
        if (i != -1) {
            setCurrentState(this.currentStartLocation, i, this.currentTransitionProgress, true);
        }
    }

    public final MeasurementOutput getMeasurementsForState(MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "hostState");
        TransitionViewState obtainViewState = obtainViewState(mediaHostState);
        if (obtainViewState == null) {
            return null;
        }
        this.measurement.setMeasuredWidth(obtainViewState.getWidth());
        this.measurement.setMeasuredHeight(obtainViewState.getHeight());
        return this.measurement;
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        TransitionViewState transitionViewState;
        this.currentEndLocation = i2;
        this.currentStartLocation = i;
        this.currentTransitionProgress = f;
        boolean z2 = true;
        boolean z3 = this.animateNextStateChange && !z;
        MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i2));
        if (mediaHostState != null) {
            MediaHostState mediaHostState2 = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
            TransitionViewState obtainViewState = obtainViewState(mediaHostState);
            if (obtainViewState != null) {
                TransitionViewState updateViewStateToCarouselSize = updateViewStateToCarouselSize(obtainViewState, i2, this.tmpState2);
                Intrinsics.checkNotNull(updateViewStateToCarouselSize);
                this.layoutController.setMeasureState(updateViewStateToCarouselSize);
                this.animateNextStateChange = false;
                if (this.transitionLayout != null) {
                    TransitionViewState updateViewStateToCarouselSize2 = updateViewStateToCarouselSize(obtainViewState(mediaHostState2), i, this.tmpState3);
                    if (!mediaHostState.getVisible()) {
                        if (!(updateViewStateToCarouselSize2 == null || mediaHostState2 == null || !mediaHostState2.getVisible())) {
                            updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize2, mediaHostState2.getDisappearParameters(), f, this.tmpState);
                            transitionViewState = updateViewStateToCarouselSize2;
                        }
                        transitionViewState = updateViewStateToCarouselSize;
                    } else {
                        if (mediaHostState2 == null || mediaHostState2.getVisible()) {
                            if (!(f == 1.0f) && updateViewStateToCarouselSize2 != null) {
                                if (f != 0.0f) {
                                    z2 = false;
                                }
                                if (!z2) {
                                    updateViewStateToCarouselSize2 = this.layoutController.getInterpolatedState(updateViewStateToCarouselSize2, updateViewStateToCarouselSize, f, this.tmpState);
                                }
                            }
                            transitionViewState = updateViewStateToCarouselSize;
                        } else {
                            updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize, mediaHostState.getDisappearParameters(), 1.0f - f, this.tmpState);
                        }
                        transitionViewState = updateViewStateToCarouselSize2;
                    }
                    this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                }
            }
        }
    }

    private final TransitionViewState updateViewStateToCarouselSize(TransitionViewState transitionViewState, int i, TransitionViewState transitionViewState2) {
        TransitionViewState copy = transitionViewState == null ? null : transitionViewState.copy(transitionViewState2);
        if (copy == null) {
            return null;
        }
        MeasurementOutput measurementOutput = this.mediaHostStatesManager.getCarouselSizes().get(Integer.valueOf(i));
        if (measurementOutput != null) {
            copy.setHeight(Math.max(measurementOutput.getMeasuredHeight(), copy.getHeight()));
            copy.setWidth(Math.max(measurementOutput.getMeasuredWidth(), copy.getWidth()));
        }
        return copy;
    }

    private final void updateMediaViewControllerType(TYPE type) {
        this.type = type;
        if (type == TYPE.PLAYER) {
            this.collapsedLayout.load(this.context, R$xml.media_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_expanded);
        } else {
            this.collapsedLayout.load(this.context, R$xml.media_recommendation_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_recommendation_expanded);
        }
        refreshState();
    }

    private final TransitionViewState obtainViewStateForLocation(int i) {
        MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
        if (mediaHostState == null) {
            return null;
        }
        return obtainViewState(mediaHostState);
    }

    public final void onLocationPreChange(int i) {
        TransitionViewState obtainViewStateForLocation = obtainViewStateForLocation(i);
        if (obtainViewStateForLocation != null) {
            this.layoutController.setMeasureState(obtainViewStateForLocation);
        }
    }

    public final void animatePendingStateChange(long j, long j2) {
        this.animateNextStateChange = true;
        this.animationDuration = j;
        this.animationDelay = j2;
    }

    public final void refreshState() {
        this.viewStates.clear();
        if (this.firstRefresh) {
            ensureAllMeasurements();
            this.firstRefresh = false;
        }
        setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
    }
}
