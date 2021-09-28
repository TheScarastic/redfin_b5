package com.google.android.systemui.smartspace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.ViewPager;
import com.android.systemui.bcsmartspace.R$dimen;
import com.android.systemui.bcsmartspace.R$id;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class BcSmartspaceView extends FrameLayout implements BcSmartspaceDataPlugin.SmartspaceTargetListener, BcSmartspaceDataPlugin.SmartspaceView {
    private static ArraySet<String> mLastReceivedTargets = new ArraySet<>();
    private BcSmartspaceDataPlugin mDataProvider;
    private CheckLongPressHelper mLongPressHelper;
    private PageIndicator mPageIndicator;
    private List<? extends Parcelable> mPendingTargets;
    private Animator mRunningAnimation;
    private ViewPager mViewPager;
    private int mCardPosition = 0;
    private boolean mAnimateSmartspaceUpdate = false;
    private int mScrollState = 0;
    private final CardPagerAdapter mAdapter = new CardPagerAdapter(this);
    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.1
        private int mCurrentPosition = -1;

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            BcSmartspaceView.this.mScrollState = i;
            if (i == 0 && BcSmartspaceView.this.mPendingTargets != null) {
                BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                bcSmartspaceView.onSmartspaceTargetsUpdated(bcSmartspaceView.mPendingTargets);
                BcSmartspaceView.this.mPendingTargets = null;
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            if (BcSmartspaceView.this.mPageIndicator != null) {
                BcSmartspaceView.this.mPageIndicator.setPageOffset(i, f);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            BcSmartspaceView.this.mCardPosition = i;
            BcSmartspaceCard cardAtPosition = BcSmartspaceView.this.mAdapter.getCardAtPosition(this.mCurrentPosition);
            this.mCurrentPosition = i;
            SmartspaceTarget targetAtPosition = BcSmartspaceView.this.mAdapter.getTargetAtPosition(this.mCurrentPosition);
            BcSmartspaceView.this.logSmartspaceEvent(targetAtPosition, this.mCurrentPosition, BcSmartspaceEvent.SMARTSPACE_CARD_SEEN);
            if (BcSmartspaceView.this.mDataProvider == null) {
                Log.w("BcSmartspaceView", "Cannot notify target hidden/shown smartspace events: data provider null");
                return;
            }
            if (cardAtPosition == null) {
                Log.w("BcSmartspaceView", "Cannot notify target hidden smartspace event: hidden card null.");
            } else {
                SmartspaceTarget target = cardAtPosition.getTarget();
                if (target == null) {
                    Log.w("BcSmartspaceView", "Cannot notify target hidden smartspace event: hidden card smartspace target null.");
                } else {
                    SmartspaceTargetEvent.Builder builder = new SmartspaceTargetEvent.Builder(3);
                    builder.setSmartspaceTarget(target);
                    SmartspaceAction baseAction = target.getBaseAction();
                    if (baseAction != null) {
                        builder.setSmartspaceActionId(baseAction.getId());
                    }
                    BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder.build());
                }
            }
            if (targetAtPosition == null) {
                Log.w("BcSmartspaceView", "Cannot notify target shown smartspace event: shown card smartspace target null.");
                return;
            }
            SmartspaceTargetEvent.Builder builder2 = new SmartspaceTargetEvent.Builder(2);
            builder2.setSmartspaceTarget(targetAtPosition);
            SmartspaceAction baseAction2 = targetAtPosition.getBaseAction();
            if (baseAction2 != null) {
                builder2.setSmartspaceActionId(baseAction2.getId());
            }
            BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder2.build());
        }
    };

    public BcSmartspaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(z ? 6 : 7).build());
        }
        int loggingDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(getContext().getPackageName(), this.mAdapter.getDozeAmount());
        if (!z) {
            return;
        }
        if (loggingDisplaySurface == 3 || loggingDisplaySurface == 2) {
            logCurrentDisplayedCardSeen();
        }
    }

    public void logCurrentDisplayedCardSeen() {
        int count = this.mAdapter.getCount();
        int i = this.mCardPosition;
        if (count <= i) {
            Log.w("BcSmartspaceView", "Current card number does not present in the Adapter.");
            return;
        }
        SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(i);
        if (targetAtPosition != null) {
            logSmartspaceEvent(targetAtPosition, this.mCardPosition, BcSmartspaceEvent.SMARTSPACE_CARD_SEEN);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mViewPager = (ViewPager) findViewById(R$id.smartspace_card_pager);
        this.mPageIndicator = (PageIndicator) findViewById(R$id.smartspace_page_indicator);
        this.mLongPressHelper = new CheckLongPressHelper(this.mViewPager);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.addOnPageChangeListener(this.mOnPageChangeListener);
        this.mPageIndicator.setNumPages(this.mAdapter.getCount());
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i2);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.enhanced_smartspace_height);
        if (size <= 0 || size >= dimensionPixelSize) {
            super.onMeasure(i, i2);
            setScaleX(1.0f);
            setScaleY(1.0f);
            resetPivot();
            return;
        }
        float f = (float) size;
        float f2 = (float) dimensionPixelSize;
        float f3 = f / f2;
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(Math.round(((float) View.MeasureSpec.getSize(i)) / f3), 1073741824), View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824));
        setScaleX(f3);
        setScaleY(f3);
        setPivotX(0.0f);
        setPivotY(f2 / 2.0f);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void registerDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin) {
        if (!Objects.equals(bcSmartspaceDataPlugin, this.mDataProvider)) {
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.mDataProvider;
            if (bcSmartspaceDataPlugin2 != null) {
                bcSmartspaceDataPlugin2.unregisterListener(this);
            }
            this.mDataProvider = bcSmartspaceDataPlugin;
            bcSmartspaceDataPlugin.registerListener(this);
            this.mAdapter.setDataProvider(this.mDataProvider);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        int i;
        boolean z = true;
        if (this.mScrollState == 0 || this.mAdapter.getCount() <= 1) {
            if (getLayoutDirection() != 1) {
                z = false;
            }
            int currentItem = this.mViewPager.getCurrentItem();
            if (z) {
                i = this.mAdapter.getCount() - currentItem;
                ArrayList arrayList = new ArrayList(list);
                Collections.reverse(arrayList);
                list = arrayList;
            } else {
                i = currentItem;
            }
            BcSmartspaceCard cardAtPosition = this.mAdapter.getCardAtPosition(currentItem);
            this.mAdapter.setTargets(list);
            int count = this.mAdapter.getCount();
            if (z) {
                this.mViewPager.setCurrentItem(Math.max(0, Math.min(count - 1, count - i)), false);
            }
            PageIndicator pageIndicator = this.mPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(count);
            }
            if (this.mAnimateSmartspaceUpdate) {
                animateSmartspaceUpdate(cardAtPosition);
            }
            for (int i2 = 0; i2 < count; i2++) {
                SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(i2);
                if (!mLastReceivedTargets.contains(targetAtPosition.getSmartspaceTargetId())) {
                    logSmartspaceEvent(targetAtPosition, i2, BcSmartspaceEvent.SMARTSPACE_CARD_RECEIVED);
                }
            }
            mLastReceivedTargets.clear();
            mLastReceivedTargets.addAll((Collection) this.mAdapter.getTargets().stream().map(BcSmartspaceView$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList()));
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mPendingTargets = list;
    }

    /* access modifiers changed from: private */
    public void logSmartspaceEvent(SmartspaceTarget smartspaceTarget, int i, BcSmartspaceEvent bcSmartspaceEvent) {
        BcSmartspaceLogger.log(bcSmartspaceEvent, new BcSmartspaceCardLoggingInfo.Builder().setInstanceId(InstanceId.create(smartspaceTarget).intValue()).setLoggingCardType(BcSmartSpaceUtil.getLoggingCardType(smartspaceTarget.getFeatureType())).setDisplaySurface(BcSmartSpaceUtil.getLoggingDisplaySurface(getContext().getPackageName(), this.mAdapter.getDozeAmount())).setRank(i).setCardinality(this.mAdapter.getCount()).build());
    }

    private void animateSmartspaceUpdate(final BcSmartspaceCard bcSmartspaceCard) {
        if (bcSmartspaceCard != null && this.mRunningAnimation == null && bcSmartspaceCard.getParent() == null) {
            final ViewGroup viewGroup = (ViewGroup) this.mViewPager.getParent();
            bcSmartspaceCard.measure(View.MeasureSpec.makeMeasureSpec(this.mViewPager.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mViewPager.getHeight(), 1073741824));
            bcSmartspaceCard.layout(this.mViewPager.getLeft(), this.mViewPager.getTop(), this.mViewPager.getRight(), this.mViewPager.getBottom());
            AnimatorSet animatorSet = new AnimatorSet();
            float dimension = getContext().getResources().getDimension(R$dimen.enhanced_smartspace_dismiss_margin);
            animatorSet.play(ObjectAnimator.ofFloat(bcSmartspaceCard, View.TRANSLATION_Y, 0.0f, ((float) (-getHeight())) - dimension));
            animatorSet.play(ObjectAnimator.ofFloat(bcSmartspaceCard, View.ALPHA, 1.0f, 0.0f));
            animatorSet.play(ObjectAnimator.ofFloat(this.mViewPager, View.TRANSLATION_Y, ((float) getHeight()) + dimension, 0.0f));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    viewGroup.getOverlay().add(bcSmartspaceCard);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    viewGroup.getOverlay().remove(bcSmartspaceCard);
                    BcSmartspaceView.this.mRunningAnimation = null;
                    BcSmartspaceView.this.mAnimateSmartspaceUpdate = false;
                }
            });
            this.mRunningAnimation = animatorSet;
            animatorSet.start();
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setPrimaryTextColor(int i) {
        this.mAdapter.setPrimaryTextColor(i);
        this.mPageIndicator.setPrimaryColor(i);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setDozeAmount(float f) {
        this.mPageIndicator.setAlpha(1.0f - f);
        this.mAdapter.setDozeAmount(f);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setIntentStarter(BcSmartspaceDataPlugin.IntentStarter intentStarter) {
        BcSmartSpaceUtil.setIntentStarter(intentStarter);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setFalsingManager(FalsingManager falsingManager) {
        BcSmartSpaceUtil.setFalsingManager(falsingManager);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setDnd(Drawable drawable, String str) {
        this.mAdapter.setDnd(drawable, str);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setNextAlarm(Drawable drawable, String str) {
        this.mAdapter.setNextAlarm(drawable, str);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public void setMediaTarget(SmartspaceTarget smartspaceTarget) {
        this.mAdapter.setMediaTarget(smartspaceTarget);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.mLongPressHelper.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (super.onInterceptTouchEvent(motionEvent) || this.mLongPressHelper.hasPerformedLongPress()) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewParent, android.view.ViewGroup
    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (z) {
            this.mLongPressHelper.cancelLongPress();
        }
        super.requestDisallowInterceptTouchEvent(z);
    }

    @Override // android.view.View
    public void cancelLongPress() {
        super.cancelLongPress();
        this.mLongPressHelper.cancelLongPress();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isLongClickable()) {
            return super.onTouchEvent(motionEvent);
        }
        super.onTouchEvent(motionEvent);
        this.mLongPressHelper.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mViewPager.setOnLongClickListener(onLongClickListener);
    }
}
