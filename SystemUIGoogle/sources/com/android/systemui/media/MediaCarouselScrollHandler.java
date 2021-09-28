package com.android.systemui.media;

import android.content.res.Resources;
import android.graphics.Outline;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.core.view.GestureDetectorCompat;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselScrollHandler.kt */
/* loaded from: classes.dex */
public final class MediaCarouselScrollHandler {
    private int carouselHeight;
    private int carouselWidth;
    private final Function1<Boolean, Unit> closeGuts;
    private float contentTranslation;
    private int cornerRadius;
    private final Function0<Unit> dismissCallback;
    private final FalsingCollector falsingCollector;
    private final FalsingManager falsingManager;
    private boolean falsingProtectionNeeded;
    private final GestureDetectorCompat gestureDetector;
    private final MediaCarouselScrollHandler$gestureListener$1 gestureListener;
    private final Function1<Boolean, Unit> logSmartspaceImpression;
    private final DelayableExecutor mainExecutor;
    private ViewGroup mediaContent;
    private final PageIndicator pageIndicator;
    private int playerWidthPlusPadding;
    private boolean qsExpanded;
    private final MediaCarouselScrollHandler$scrollChangedListener$1 scrollChangedListener;
    private int scrollIntoCurrentMedia;
    private final MediaScrollView scrollView;
    private View settingsButton;
    private boolean showsSettingsButton;
    private final MediaCarouselScrollHandler$touchListener$1 touchListener;
    private Function0<Unit> translationChangedListener;
    private int visibleMediaIndex;
    private boolean visibleToUser;
    public static final Companion Companion = new Companion(null);
    private static final MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 CONTENT_TRANSLATION = new MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1();

    /* JADX DEBUG: Multi-variable search result rejected for r7v0, resolved type: kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> */
    /* JADX DEBUG: Multi-variable search result rejected for r10v0, resolved type: kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    public MediaCarouselScrollHandler(MediaScrollView mediaScrollView, PageIndicator pageIndicator, DelayableExecutor delayableExecutor, Function0<Unit> function0, Function0<Unit> function02, Function1<? super Boolean, Unit> function1, FalsingCollector falsingCollector, FalsingManager falsingManager, Function1<? super Boolean, Unit> function12) {
        Intrinsics.checkNotNullParameter(mediaScrollView, "scrollView");
        Intrinsics.checkNotNullParameter(pageIndicator, "pageIndicator");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(function0, "dismissCallback");
        Intrinsics.checkNotNullParameter(function02, "translationChangedListener");
        Intrinsics.checkNotNullParameter(function1, "closeGuts");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(function12, "logSmartspaceImpression");
        this.scrollView = mediaScrollView;
        this.pageIndicator = pageIndicator;
        this.mainExecutor = delayableExecutor;
        this.dismissCallback = function0;
        this.translationChangedListener = function02;
        this.closeGuts = function1;
        this.falsingCollector = falsingCollector;
        this.falsingManager = falsingManager;
        this.logSmartspaceImpression = function12;
        MediaCarouselScrollHandler$gestureListener$1 mediaCarouselScrollHandler$gestureListener$1 = new GestureDetector.SimpleOnGestureListener(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$gestureListener$1
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                return MediaCarouselScrollHandler.access$onFling(this.this$0, f, f2);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
                Intrinsics.checkNotNull(motionEvent);
                Intrinsics.checkNotNull(motionEvent2);
                return mediaCarouselScrollHandler.onScroll(motionEvent, motionEvent2, f);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                if (!this.this$0.getFalsingProtectionNeeded()) {
                    return false;
                }
                MediaCarouselScrollHandler.access$getFalsingCollector$p(this.this$0).onNotificationStartDismissing();
                return false;
            }
        };
        this.gestureListener = mediaCarouselScrollHandler$gestureListener$1;
        MediaCarouselScrollHandler$touchListener$1 mediaCarouselScrollHandler$touchListener$1 = new Gefingerpoken(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$touchListener$1
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.Gefingerpoken
            public boolean onTouchEvent(MotionEvent motionEvent) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
                Intrinsics.checkNotNull(motionEvent);
                return mediaCarouselScrollHandler.onTouch(motionEvent);
            }

            @Override // com.android.systemui.Gefingerpoken
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
                Intrinsics.checkNotNull(motionEvent);
                return mediaCarouselScrollHandler.onInterceptTouch(motionEvent);
            }
        };
        this.touchListener = mediaCarouselScrollHandler$touchListener$1;
        MediaCarouselScrollHandler$scrollChangedListener$1 mediaCarouselScrollHandler$scrollChangedListener$1 = new View.OnScrollChangeListener(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollChangedListener$1
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnScrollChangeListener
            public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                if (this.this$0.getPlayerWidthPlusPadding() != 0) {
                    int relativeScrollX = this.this$0.scrollView.getRelativeScrollX();
                    MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
                    mediaCarouselScrollHandler.onMediaScrollingChanged(relativeScrollX / mediaCarouselScrollHandler.getPlayerWidthPlusPadding(), relativeScrollX % this.this$0.getPlayerWidthPlusPadding());
                }
            }
        };
        this.scrollChangedListener = mediaCarouselScrollHandler$scrollChangedListener$1;
        this.gestureDetector = new GestureDetectorCompat(mediaScrollView.getContext(), mediaCarouselScrollHandler$gestureListener$1);
        mediaScrollView.setTouchListener(mediaCarouselScrollHandler$touchListener$1);
        mediaScrollView.setOverScrollMode(2);
        this.mediaContent = mediaScrollView.getContentContainer();
        mediaScrollView.setOnScrollChangeListener(mediaCarouselScrollHandler$scrollChangedListener$1);
        mediaScrollView.setOutlineProvider(new ViewOutlineProvider(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler.1
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                if (outline != null) {
                    outline.setRoundRect(0, 0, this.this$0.carouselWidth, this.this$0.carouselHeight, (float) this.this$0.cornerRadius);
                }
            }
        });
    }

    public final boolean isRtl() {
        return this.scrollView.isLayoutRtl();
    }

    public final boolean getFalsingProtectionNeeded() {
        return this.falsingProtectionNeeded;
    }

    public final void setFalsingProtectionNeeded(boolean z) {
        this.falsingProtectionNeeded = z;
    }

    public final int getVisibleMediaIndex() {
        return this.visibleMediaIndex;
    }

    public final float getContentTranslation() {
        return this.contentTranslation;
    }

    public final void setContentTranslation(float f) {
        this.contentTranslation = f;
        this.mediaContent.setTranslationX(f);
        updateSettingsPresentation();
        this.translationChangedListener.invoke();
        updateClipToOutline();
    }

    public final int getPlayerWidthPlusPadding() {
        return this.playerWidthPlusPadding;
    }

    public final void setPlayerWidthPlusPadding(int i) {
        this.playerWidthPlusPadding = i;
        int i2 = this.visibleMediaIndex * i;
        int i3 = this.scrollIntoCurrentMedia;
        this.scrollView.setRelativeScrollX(i3 > i ? i2 + (i - (i3 - i)) : i2 + i3);
    }

    public final void setShowsSettingsButton(boolean z) {
        this.showsSettingsButton = z;
    }

    public final boolean getVisibleToUser() {
        return this.visibleToUser;
    }

    public final void setVisibleToUser(boolean z) {
        this.visibleToUser = z;
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final void onSettingsButtonUpdated(View view) {
        Intrinsics.checkNotNullParameter(view, "button");
        this.settingsButton = view;
        if (view != null) {
            Resources resources = view.getResources();
            View view2 = this.settingsButton;
            if (view2 != null) {
                this.cornerRadius = resources.getDimensionPixelSize(Utils.getThemeAttr(view2.getContext(), 16844145));
                updateSettingsPresentation();
                this.scrollView.invalidateOutline();
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        throw null;
    }

    private final void updateSettingsPresentation() {
        int i = 4;
        if (this.showsSettingsButton) {
            View view = this.settingsButton;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                throw null;
            } else if (view.getWidth() > 0) {
                float map = MathUtils.map(0.0f, (float) getMaxTranslation(), 0.0f, 1.0f, Math.abs(this.contentTranslation));
                float f = 1.0f - map;
                View view2 = this.settingsButton;
                if (view2 != null) {
                    float f2 = ((float) (-view2.getWidth())) * f * 0.3f;
                    if (isRtl()) {
                        if (this.contentTranslation > 0.0f) {
                            float width = ((float) this.scrollView.getWidth()) - f2;
                            View view3 = this.settingsButton;
                            if (view3 != null) {
                                f2 = -(width - ((float) view3.getWidth()));
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                                throw null;
                            }
                        } else {
                            f2 = -f2;
                        }
                    } else if (this.contentTranslation <= 0.0f) {
                        float width2 = ((float) this.scrollView.getWidth()) - f2;
                        View view4 = this.settingsButton;
                        if (view4 != null) {
                            f2 = width2 - ((float) view4.getWidth());
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                            throw null;
                        }
                    }
                    float f3 = f * ((float) 50);
                    View view5 = this.settingsButton;
                    if (view5 != null) {
                        view5.setRotation(f3 * (-Math.signum(this.contentTranslation)));
                        float saturate = MathUtils.saturate(MathUtils.map(0.5f, 1.0f, 0.0f, 1.0f, map));
                        View view6 = this.settingsButton;
                        if (view6 != null) {
                            view6.setAlpha(saturate);
                            View view7 = this.settingsButton;
                            if (view7 != null) {
                                if (!(saturate == 0.0f)) {
                                    i = 0;
                                }
                                view7.setVisibility(i);
                                View view8 = this.settingsButton;
                                if (view8 != null) {
                                    view8.setTranslationX(f2);
                                    View view9 = this.settingsButton;
                                    if (view9 != null) {
                                        int height = this.scrollView.getHeight();
                                        View view10 = this.settingsButton;
                                        if (view10 != null) {
                                            view9.setTranslationY(((float) (height - view10.getHeight())) / 2.0f);
                                            return;
                                        } else {
                                            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                                            throw null;
                                        }
                                    } else {
                                        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                                        throw null;
                                    }
                                } else {
                                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                                    throw null;
                                }
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                                throw null;
                            }
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                            throw null;
                        }
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
                    throw null;
                }
            }
        }
        View view11 = this.settingsButton;
        if (view11 != null) {
            view11.setVisibility(4);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
            throw null;
        }
    }

    public final boolean onTouch(MotionEvent motionEvent) {
        float f;
        boolean z = true;
        boolean z2 = motionEvent.getAction() == 1;
        if (z2 && this.falsingProtectionNeeded) {
            this.falsingCollector.onNotificationStopDismissing();
        }
        if (!this.gestureDetector.onTouchEvent(motionEvent)) {
            if (z2 || motionEvent.getAction() == 3) {
                int relativeScrollX = this.scrollView.getRelativeScrollX();
                int i = this.playerWidthPlusPadding;
                int i2 = relativeScrollX % i;
                int i3 = i2 > i / 2 ? i - i2 : i2 * -1;
                if (i3 != 0) {
                    if (isRtl()) {
                        i3 = -i3;
                    }
                    this.mainExecutor.execute(new Runnable(this, this.scrollView.getRelativeScrollX() + i3) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$1
                        final /* synthetic */ int $newScrollX;
                        final /* synthetic */ MediaCarouselScrollHandler this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$newScrollX = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaCarouselScrollHandler.access$getScrollView$p(this.this$0).smoothScrollTo(this.$newScrollX, MediaCarouselScrollHandler.access$getScrollView$p(this.this$0).getScrollY());
                        }
                    });
                }
                float contentTranslation = this.scrollView.getContentTranslation();
                if (!(contentTranslation == 0.0f)) {
                    if (Math.abs(contentTranslation) >= ((float) (getMaxTranslation() / 2)) && !isFalseTouch()) {
                        z = false;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = ((float) getMaxTranslation()) * Math.signum(contentTranslation);
                        if (!this.showsSettingsButton) {
                            this.mainExecutor.executeDelayed(new Runnable(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$2
                                final /* synthetic */ MediaCarouselScrollHandler this$0;

                                /* access modifiers changed from: package-private */
                                {
                                    this.this$0 = r1;
                                }

                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.this$0.dismissCallback.invoke();
                                }
                            }, 100);
                        }
                    }
                    PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
                    this.scrollView.setAnimationTargetX(f);
                }
            }
            return false;
        } else if (!z2) {
            return false;
        } else {
            this.scrollView.cancelCurrentScroll();
            return true;
        }
    }

    private final boolean isFalseTouch() {
        if (!this.falsingProtectionNeeded || !this.falsingManager.isFalseTouch(1)) {
            return false;
        }
        return true;
    }

    private final int getMaxTranslation() {
        if (!this.showsSettingsButton) {
            return this.playerWidthPlusPadding;
        }
        View view = this.settingsButton;
        if (view != null) {
            return view.getWidth();
        }
        Intrinsics.throwUninitializedPropertyAccessException("settingsButton");
        throw null;
    }

    public final boolean onInterceptTouch(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f) {
        Intrinsics.checkNotNullParameter(motionEvent, "down");
        Intrinsics.checkNotNullParameter(motionEvent2, "lastMotion");
        float x = motionEvent2.getX() - motionEvent.getX();
        float contentTranslation = this.scrollView.getContentTranslation();
        int i = (contentTranslation > 0.0f ? 1 : (contentTranslation == 0.0f ? 0 : -1));
        boolean z = false;
        if ((i == 0) && this.scrollView.canScrollHorizontally((int) (-x))) {
            return false;
        }
        float f2 = contentTranslation - f;
        float abs = Math.abs(f2);
        if (abs > ((float) getMaxTranslation())) {
            if (!(Math.signum(f) == Math.signum(contentTranslation))) {
                f2 = Math.abs(contentTranslation) > ((float) getMaxTranslation()) ? contentTranslation - (f * 0.2f) : Math.signum(f2) * (((float) getMaxTranslation()) + ((abs - ((float) getMaxTranslation())) * 0.2f));
            }
        }
        if (!(Math.signum(f2) == Math.signum(contentTranslation))) {
            if (i == 0) {
                z = true;
            }
            if (!z && this.scrollView.canScrollHorizontally(-((int) f2))) {
                f2 = 0.0f;
            }
        }
        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(this);
        if (instance.isRunning()) {
            instance.spring(CONTENT_TRANSLATION, f2, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
        } else {
            setContentTranslation(f2);
        }
        this.scrollView.setAnimationTargetX(f2);
        return true;
    }

    public final boolean onFling(float f, float f2) {
        float f3 = f * f;
        double d = (double) f2;
        boolean z = false;
        if (((double) f3) < 0.5d * d * d || f3 < 1000000.0f) {
            return false;
        }
        float contentTranslation = this.scrollView.getContentTranslation();
        float f4 = 0.0f;
        if (!(contentTranslation == 0.0f)) {
            if (Math.signum(f) == Math.signum(contentTranslation)) {
                z = true;
            }
            if (z && !isFalseTouch()) {
                f4 = ((float) getMaxTranslation()) * Math.signum(contentTranslation);
                if (!this.showsSettingsButton) {
                    this.mainExecutor.executeDelayed(new Runnable(this) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$1
                        final /* synthetic */ MediaCarouselScrollHandler this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaCarouselScrollHandler.access$getDismissCallback$p(this.this$0).invoke();
                        }
                    }, 100);
                }
            }
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f4, f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(f4);
        } else {
            int relativeScrollX = this.scrollView.getRelativeScrollX();
            int i = this.playerWidthPlusPadding;
            int i2 = i > 0 ? relativeScrollX / i : 0;
            if (!isRtl() ? f < 0.0f : f > 0.0f) {
                i2++;
            }
            this.mainExecutor.execute(new Runnable(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, Math.max(0, i2)))) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$2
                final /* synthetic */ View $view;
                final /* synthetic */ MediaCarouselScrollHandler this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$view = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    MediaCarouselScrollHandler.access$getScrollView$p(this.this$0).smoothScrollTo(this.$view.getLeft(), MediaCarouselScrollHandler.access$getScrollView$p(this.this$0).getScrollY());
                }
            });
        }
        return true;
    }

    public static /* synthetic */ void resetTranslation$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaCarouselScrollHandler.resetTranslation(z);
    }

    public final void resetTranslation(boolean z) {
        if (this.scrollView.getContentTranslation() == 0.0f) {
            return;
        }
        if (z) {
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(0.0f);
            return;
        }
        PhysicsAnimator.Companion.getInstance(this).cancel();
        setContentTranslation(0.0f);
    }

    private final void updateClipToOutline() {
        boolean z = true;
        if ((this.contentTranslation == 0.0f) && this.scrollIntoCurrentMedia == 0) {
            z = false;
        }
        this.scrollView.setClipToOutline(z);
    }

    public final void onMediaScrollingChanged(int i, int i2) {
        boolean z = false;
        boolean z2 = this.scrollIntoCurrentMedia != 0;
        this.scrollIntoCurrentMedia = i2;
        if (i2 != 0) {
            z = true;
        }
        int i3 = this.visibleMediaIndex;
        if (!(i == i3 && z2 == z)) {
            this.visibleMediaIndex = i;
            if (i3 != i && this.visibleToUser) {
                this.logSmartspaceImpression.invoke(Boolean.valueOf(this.qsExpanded));
            }
            this.closeGuts.invoke(Boolean.FALSE);
            updatePlayerVisibilities();
        }
        float f = (float) this.visibleMediaIndex;
        int i4 = this.playerWidthPlusPadding;
        float f2 = f + (i4 > 0 ? ((float) i2) / ((float) i4) : 0.0f);
        if (isRtl()) {
            f2 = (((float) this.mediaContent.getChildCount()) - f2) - ((float) 1);
        }
        this.pageIndicator.setLocation(f2);
        updateClipToOutline();
    }

    public final void onPlayersChanged() {
        updatePlayerVisibilities();
        updateMediaPaddings();
    }

    private final void updateMediaPaddings() {
        int dimensionPixelSize = this.scrollView.getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_padding);
        int childCount = this.mediaContent.getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = this.mediaContent.getChildAt(i);
                int i3 = i == childCount + -1 ? 0 : dimensionPixelSize;
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (marginLayoutParams.getMarginEnd() != i3) {
                    marginLayoutParams.setMarginEnd(i3);
                    childAt.setLayoutParams(marginLayoutParams);
                }
                if (i2 < childCount) {
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    private final void updatePlayerVisibilities() {
        boolean z = this.scrollIntoCurrentMedia != 0;
        int childCount = this.mediaContent.getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = this.mediaContent.getChildAt(i);
                int i3 = this.visibleMediaIndex;
                childAt.setVisibility(i == i3 || (i == i3 + 1 && z) ? 0 : 4);
                if (i2 < childCount) {
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    public final void onPrePlayerRemoved(MediaControlPanel mediaControlPanel) {
        Intrinsics.checkNotNullParameter(mediaControlPanel, "removed");
        ViewGroup viewGroup = this.mediaContent;
        PlayerViewHolder playerViewHolder = mediaControlPanel.getPlayerViewHolder();
        int indexOfChild = viewGroup.indexOfChild(playerViewHolder == null ? null : playerViewHolder.getPlayer());
        int i = this.visibleMediaIndex;
        boolean z = true;
        boolean z2 = indexOfChild <= i;
        if (z2) {
            this.visibleMediaIndex = Math.max(0, i - 1);
        }
        if (!isRtl()) {
            z = z2;
        } else if (z2) {
            z = false;
        }
        if (z) {
            MediaScrollView mediaScrollView = this.scrollView;
            mediaScrollView.setScrollX(Math.max(mediaScrollView.getScrollX() - this.playerWidthPlusPadding, 0));
        }
    }

    public final void setCarouselBounds(int i, int i2) {
        int i3 = this.carouselHeight;
        if (i2 != i3 || i != i3) {
            this.carouselWidth = i;
            this.carouselHeight = i2;
            this.scrollView.invalidateOutline();
        }
    }

    public final void scrollToStart() {
        this.scrollView.setRelativeScrollX(0);
    }

    public static /* synthetic */ void scrollToPlayer$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = -1;
        }
        mediaCarouselScrollHandler.scrollToPlayer(i, i2);
    }

    public final void scrollToPlayer(int i, int i2) {
        if (i >= 0 && i < this.mediaContent.getChildCount()) {
            this.scrollView.setRelativeScrollX(i * this.playerWidthPlusPadding);
        }
        this.mainExecutor.executeDelayed(new Runnable(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, i2))) { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollToPlayer$1
            final /* synthetic */ View $view;
            final /* synthetic */ MediaCarouselScrollHandler this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$view = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.scrollView.smoothScrollTo(this.$view.getLeft(), this.this$0.scrollView.getScrollY());
            }
        }, 100);
    }

    /* compiled from: MediaCarouselScrollHandler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
