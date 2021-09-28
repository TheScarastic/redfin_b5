package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
/* loaded from: classes.dex */
public class StackStateAnimator {
    private ValueAnimator mBottomOverScrollAnimator;
    private long mCurrentAdditionalDelay;
    private long mCurrentLength;
    private final int mGoToFullShadeAppearingTranslation;
    private int mHeadsUpAppearHeightBottom;
    public NotificationStackScrollLayout mHostLayout;
    private final int mPulsingAppearingTranslation;
    private boolean mShadeExpanded;
    private NotificationShelf mShelf;
    private ValueAnimator mTopOverScrollAnimator;
    private final ExpandableViewState mTmpState = new ExpandableViewState();
    private ArrayList<NotificationStackScrollLayout.AnimationEvent> mNewEvents = new ArrayList<>();
    private ArrayList<View> mNewAddChildren = new ArrayList<>();
    private HashSet<View> mHeadsUpAppearChildren = new HashSet<>();
    private HashSet<View> mHeadsUpDisappearChildren = new HashSet<>();
    private HashSet<Animator> mAnimatorSet = new HashSet<>();
    private Stack<AnimatorListenerAdapter> mAnimationListenerPool = new Stack<>();
    private AnimationFilter mAnimationFilter = new AnimationFilter();
    private ArrayList<ExpandableView> mTransientViewsToRemove = new ArrayList<>();
    private int[] mTmpLocation = new int[2];
    private final AnimationProperties mAnimationProperties = new AnimationProperties() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.1
        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return StackStateAnimator.this.mAnimationFilter;
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimatorListenerAdapter getAnimationFinishListener(Property property) {
            return StackStateAnimator.this.getGlobalAnimationFinishedListener();
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public boolean wasAdded(View view) {
            return StackStateAnimator.this.mNewAddChildren.contains(view);
        }
    };

    public StackStateAnimator(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mHostLayout = notificationStackScrollLayout;
        this.mGoToFullShadeAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(R$dimen.go_to_full_shade_appearing_translation);
        this.mPulsingAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(R$dimen.pulsing_notification_appear_translation);
    }

    public boolean isRunning() {
        return !this.mAnimatorSet.isEmpty();
    }

    public void startAnimationForEvents(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList, long j) {
        processAnimationEvents(arrayList);
        int childCount = this.mHostLayout.getChildCount();
        this.mAnimationFilter.applyCombination(this.mNewEvents);
        this.mCurrentAdditionalDelay = j;
        this.mCurrentLength = NotificationStackScrollLayout.AnimationEvent.combineLength(this.mNewEvents);
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) this.mHostLayout.getChildAt(i2);
            ExpandableViewState viewState = expandableView.getViewState();
            if (!(viewState == null || expandableView.getVisibility() == 8 || applyWithoutAnimation(expandableView, viewState))) {
                if (this.mAnimationProperties.wasAdded(expandableView) && i < 5) {
                    i++;
                }
                initAnimationProperties(expandableView, viewState, i);
                viewState.animateTo(expandableView, this.mAnimationProperties);
            }
        }
        if (!isRunning()) {
            onAnimationFinished();
        }
        this.mHeadsUpAppearChildren.clear();
        this.mHeadsUpDisappearChildren.clear();
        this.mNewEvents.clear();
        this.mNewAddChildren.clear();
    }

    private void initAnimationProperties(ExpandableView expandableView, ExpandableViewState expandableViewState, int i) {
        boolean wasAdded = this.mAnimationProperties.wasAdded(expandableView);
        this.mAnimationProperties.duration = this.mCurrentLength;
        adaptDurationWhenGoingToFullShade(expandableView, expandableViewState, wasAdded, i);
        this.mAnimationProperties.delay = 0;
        if (!wasAdded) {
            if (!this.mAnimationFilter.hasDelays) {
                return;
            }
            if (expandableViewState.yTranslation == expandableView.getTranslationY() && expandableViewState.zTranslation == expandableView.getTranslationZ() && expandableViewState.alpha == expandableView.getAlpha() && expandableViewState.height == expandableView.getActualHeight() && expandableViewState.clipTopAmount == expandableView.getClipTopAmount()) {
                return;
            }
        }
        this.mAnimationProperties.delay = this.mCurrentAdditionalDelay + calculateChildAnimationDelay(expandableViewState, i);
    }

    private void adaptDurationWhenGoingToFullShade(ExpandableView expandableView, ExpandableViewState expandableViewState, boolean z, int i) {
        boolean z2 = expandableView instanceof StackScrollerDecorView;
        int i2 = 0;
        if ((z || z2) && this.mAnimationFilter.hasGoToFullShadeEvent) {
            if (!z2) {
                i2 = this.mGoToFullShadeAppearingTranslation;
                this.mAnimationProperties.duration = ((long) (((float) Math.pow((double) i, 0.699999988079071d)) * 100.0f)) + 514;
            }
            expandableView.setTranslationY(expandableViewState.yTranslation + ((float) i2));
        }
    }

    private boolean applyWithoutAnimation(ExpandableView expandableView, ExpandableViewState expandableViewState) {
        if (this.mShadeExpanded || ViewState.isAnimatingY(expandableView) || this.mHeadsUpDisappearChildren.contains(expandableView) || this.mHeadsUpAppearChildren.contains(expandableView) || NotificationStackScrollLayout.isPinnedHeadsUp(expandableView)) {
            return false;
        }
        expandableViewState.applyToView(expandableView);
        return true;
    }

    private long calculateChildAnimationDelay(ExpandableViewState expandableViewState, int i) {
        ExpandableView expandableView;
        AnimationFilter animationFilter = this.mAnimationFilter;
        if (animationFilter.hasGoToFullShadeEvent) {
            return calculateDelayGoToFullShade(expandableViewState, i);
        }
        long j = animationFilter.customDelay;
        if (j != -1) {
            return j;
        }
        long j2 = 0;
        Iterator<NotificationStackScrollLayout.AnimationEvent> it = this.mNewEvents.iterator();
        while (it.hasNext()) {
            NotificationStackScrollLayout.AnimationEvent next = it.next();
            long j3 = 80;
            int i2 = next.animationType;
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 == 2) {
                        j3 = 32;
                    }
                }
                int i3 = expandableViewState.notGoneIndex;
                View view = next.viewAfterChangingView;
                if (view == null) {
                    expandableView = this.mHostLayout.getLastChildNotGone();
                } else {
                    expandableView = (ExpandableView) view;
                }
                if (expandableView != null) {
                    int i4 = expandableView.getViewState().notGoneIndex;
                    if (i3 >= i4) {
                        i3++;
                    }
                    j2 = Math.max(((long) Math.max(0, Math.min(2, Math.abs(i3 - i4) - 1))) * j3, j2);
                }
            } else {
                j2 = Math.max(((long) (2 - Math.max(0, Math.min(2, Math.abs(expandableViewState.notGoneIndex - next.mChangingView.getViewState().notGoneIndex) - 1)))) * 80, j2);
            }
        }
        return j2;
    }

    private long calculateDelayGoToFullShade(ExpandableViewState expandableViewState, int i) {
        int notGoneIndex = this.mShelf.getNotGoneIndex();
        float f = (float) expandableViewState.notGoneIndex;
        float f2 = (float) notGoneIndex;
        long j = 0;
        if (f > f2) {
            j = 0 + ((long) (((double) (((float) Math.pow((double) i, 0.699999988079071d)) * 48.0f)) * 0.25d));
            f = f2;
        }
        return j + ((long) (((float) Math.pow((double) f, 0.699999988079071d)) * 48.0f));
    }

    /* access modifiers changed from: private */
    public AnimatorListenerAdapter getGlobalAnimationFinishedListener() {
        if (!this.mAnimationListenerPool.empty()) {
            return this.mAnimationListenerPool.pop();
        }
        return new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.2
            private boolean mWasCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                StackStateAnimator.this.mAnimatorSet.remove(animator);
                if (StackStateAnimator.this.mAnimatorSet.isEmpty() && !this.mWasCancelled) {
                    StackStateAnimator.this.onAnimationFinished();
                }
                StackStateAnimator.this.mAnimationListenerPool.push(this);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                StackStateAnimator.this.mAnimatorSet.add(animator);
            }
        };
    }

    /* access modifiers changed from: private */
    public void onAnimationFinished() {
        this.mHostLayout.onChildAnimationFinished();
        Iterator<ExpandableView> it = this.mTransientViewsToRemove.iterator();
        while (it.hasNext()) {
            ExpandableView next = it.next();
            next.getTransientContainer().removeTransientView(next);
        }
        this.mTransientViewsToRemove.clear();
    }

    private void processAnimationEvents(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList) {
        float f;
        Iterator<NotificationStackScrollLayout.AnimationEvent> it = arrayList.iterator();
        while (it.hasNext()) {
            NotificationStackScrollLayout.AnimationEvent next = it.next();
            ExpandableView expandableView = next.mChangingView;
            int i = next.animationType;
            if (i == 0) {
                ExpandableViewState viewState = expandableView.getViewState();
                if (viewState != null && !viewState.gone) {
                    viewState.applyToView(expandableView);
                    this.mNewAddChildren.add(expandableView);
                    this.mNewEvents.add(next);
                }
            } else {
                boolean z = true;
                if (i == 1) {
                    if (expandableView.getVisibility() != 0) {
                        removeTransientView(expandableView);
                    } else {
                        if (next.viewAfterChangingView != null) {
                            float translationY = expandableView.getTranslationY();
                            if (expandableView instanceof ExpandableNotificationRow) {
                                View view = next.viewAfterChangingView;
                                if (view instanceof ExpandableNotificationRow) {
                                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                                    ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) view;
                                    if (expandableNotificationRow.isRemoved() && expandableNotificationRow.wasChildInGroupWhenRemoved() && !expandableNotificationRow2.isChildInGroup()) {
                                        translationY = expandableNotificationRow.getTranslationWhenRemoved();
                                    }
                                }
                            }
                            float actualHeight = (float) expandableView.getActualHeight();
                            f = Math.max(Math.min(((((ExpandableView) next.viewAfterChangingView).getViewState().yTranslation - (translationY + (actualHeight / 2.0f))) * 2.0f) / actualHeight, 1.0f), -1.0f);
                        } else {
                            f = -1.0f;
                        }
                        expandableView.performRemoveAnimation(464, 0, f, false, 0.0f, new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                StackStateAnimator.$r8$lambda$LYQlcHxC7zGJPqjqyA153AejeYY(ExpandableView.this);
                            }
                        }, null);
                    }
                } else if (i == 2) {
                    if (this.mHostLayout.isFullySwipedOut(expandableView) && expandableView.getTransientContainer() != null) {
                        expandableView.getTransientContainer().removeTransientView(expandableView);
                    }
                } else if (i == 10) {
                    ((ExpandableNotificationRow) expandableView).prepareExpansionChanged();
                } else if (i == 11) {
                    this.mTmpState.copyFrom(expandableView.getViewState());
                    if (next.headsUpFromBottom) {
                        this.mTmpState.yTranslation = (float) this.mHeadsUpAppearHeightBottom;
                    } else {
                        expandableView.performAddAnimation(0, 400, true);
                    }
                    this.mHeadsUpAppearChildren.add(expandableView);
                    this.mTmpState.applyToView(expandableView);
                } else if (i == 12 || i == 13) {
                    this.mHeadsUpDisappearChildren.add(expandableView);
                    StackStateAnimator$$ExternalSyntheticLambda0 stackStateAnimator$$ExternalSyntheticLambda0 = null;
                    if (expandableView.getParent() == null) {
                        this.mHostLayout.addTransientView(expandableView, 0);
                        expandableView.setTransientContainer(this.mHostLayout);
                        this.mTmpState.initFrom(expandableView);
                        stackStateAnimator$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                StackStateAnimator.m311$r8$lambda$7KglFEZum__Mzlbzw_kCiIWeF0(ExpandableView.this);
                            }
                        };
                    }
                    float f2 = 0.0f;
                    if (expandableView instanceof ExpandableNotificationRow) {
                        ExpandableNotificationRow expandableNotificationRow3 = (ExpandableNotificationRow) expandableView;
                        z = true ^ expandableNotificationRow3.isDismissed();
                        NotificationEntry entry = expandableNotificationRow3.getEntry();
                        StatusBarIconView statusBarIcon = entry.getIcons().getStatusBarIcon();
                        StatusBarIconView centeredIcon = entry.getIcons().getCenteredIcon();
                        if (!(centeredIcon == null || centeredIcon.getParent() == null)) {
                            statusBarIcon = centeredIcon;
                        }
                        if (statusBarIcon.getParent() != null) {
                            statusBarIcon.getLocationOnScreen(this.mTmpLocation);
                            float translationX = (((float) this.mTmpLocation[0]) - statusBarIcon.getTranslationX()) + ViewState.getFinalTranslationX(statusBarIcon) + (((float) statusBarIcon.getWidth()) * 0.25f);
                            this.mHostLayout.getLocationOnScreen(this.mTmpLocation);
                            f2 = translationX - ((float) this.mTmpLocation[0]);
                        }
                    }
                    if (z) {
                        this.mAnimationProperties.delay += expandableView.performRemoveAnimation(400, 0, 0.0f, true, f2, stackStateAnimator$$ExternalSyntheticLambda0, getGlobalAnimationFinishedListener());
                    } else if (stackStateAnimator$$ExternalSyntheticLambda0 != null) {
                        stackStateAnimator$$ExternalSyntheticLambda0.run();
                    }
                }
                this.mNewEvents.add(next);
            }
        }
    }

    public static void removeTransientView(ExpandableView expandableView) {
        if (expandableView.getTransientContainer() != null) {
            expandableView.getTransientContainer().removeTransientView(expandableView);
        }
    }

    public void animateOverScrollToAmount(float f, final boolean z, final boolean z2) {
        float currentOverScrollAmount = this.mHostLayout.getCurrentOverScrollAmount(z);
        if (f != currentOverScrollAmount) {
            cancelOverScrollAnimators(z);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(currentOverScrollAmount, f);
            ofFloat.setDuration(360L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StackStateAnimator.this.mHostLayout.setOverScrollAmount(((Float) valueAnimator.getAnimatedValue()).floatValue(), z, false, false, z2);
                }
            });
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (z) {
                        StackStateAnimator.this.mTopOverScrollAnimator = null;
                    } else {
                        StackStateAnimator.this.mBottomOverScrollAnimator = null;
                    }
                }
            });
            ofFloat.start();
            if (z) {
                this.mTopOverScrollAnimator = ofFloat;
            } else {
                this.mBottomOverScrollAnimator = ofFloat;
            }
        }
    }

    public void cancelOverScrollAnimators(boolean z) {
        ValueAnimator valueAnimator = z ? this.mTopOverScrollAnimator : this.mBottomOverScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void setHeadsUpAppearHeightBottom(int i) {
        this.mHeadsUpAppearHeightBottom = i;
    }

    public void setShadeExpanded(boolean z) {
        this.mShadeExpanded = z;
    }

    public void setShelf(NotificationShelf notificationShelf) {
        this.mShelf = notificationShelf;
    }
}
