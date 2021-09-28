package com.android.systemui.privacy.television;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.SystemUI;
import com.android.systemui.privacy.PrivacyChipBuilder;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.television.PrivacyChipDrawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class TvOngoingPrivacyChip extends SystemUI implements PrivacyItemController.Callback, PrivacyChipDrawable.PrivacyChipDrawableListener {
    private boolean mAllIndicatorsEnabled;
    public final int mAnimationDurationMs;
    private ObjectAnimator mAnimator;
    private PrivacyChipDrawable mChipDrawable;
    private final Context mContext;
    private final int mIconMarginStart;
    private final int mIconSize;
    private LinearLayout mIconsContainer;
    private ViewGroup mIndicatorView;
    private boolean mMicCameraIndicatorFlagEnabled;
    private final PrivacyItemController mPrivacyItemController;
    private boolean mViewAndWindowAdded;
    private List<PrivacyItem> mPrivacyItems = Collections.emptyList();
    private final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable mCollapseRunnable = new Runnable() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            TvOngoingPrivacyChip.this.collapseChip();
        }
    };
    private final Runnable mAccessibilityRunnable = new Runnable() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            TvOngoingPrivacyChip.this.makeAccessibilityAnnouncement();
        }
    };
    private final List<PrivacyItem> mItemsBeforeLastAnnouncement = new LinkedList();
    private int mState = 0;

    public TvOngoingPrivacyChip(Context context, PrivacyItemController privacyItemController) {
        super(context);
        this.mContext = context;
        this.mPrivacyItemController = privacyItemController;
        Resources resources = context.getResources();
        this.mIconMarginStart = Math.round(resources.getDimension(R$dimen.privacy_chip_icon_margin_in_between));
        this.mIconSize = resources.getDimensionPixelSize(R$dimen.privacy_chip_icon_size);
        this.mAnimationDurationMs = resources.getInteger(R$integer.privacy_chip_animation_millis);
        this.mMicCameraIndicatorFlagEnabled = privacyItemController.getMicCameraAvailable();
        this.mAllIndicatorsEnabled = privacyItemController.getAllIndicatorsAvailable();
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mPrivacyItemController.addCallback(this);
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        ArrayList arrayList = new ArrayList(list);
        arrayList.removeIf(TvOngoingPrivacyChip$$ExternalSyntheticLambda2.INSTANCE);
        if (isChipDisabled()) {
            fadeOutIndicator();
            this.mPrivacyItems = arrayList;
        } else if (arrayList.size() != this.mPrivacyItems.size() || !this.mPrivacyItems.containsAll(arrayList)) {
            this.mPrivacyItems = arrayList;
            postAccessibilityAnnouncement();
            updateChip();
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onPrivacyItemsChanged$0(PrivacyItem privacyItem) {
        return privacyItem.getPrivacyType() == PrivacyType.TYPE_LOCATION;
    }

    private void updateChip() {
        if (this.mPrivacyItems.isEmpty()) {
            fadeOutIndicator();
            return;
        }
        int i = this.mState;
        if (i == 0) {
            createAndShowIndicator();
        } else if (i == 1 || i == 2) {
            updateIcons();
            collapseLater();
        } else if (i == 3 || i == 4) {
            this.mState = 2;
            updateIcons();
            animateIconAppearance();
        }
    }

    private void collapseLater() {
        this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
        this.mUiThreadHandler.postDelayed(this.mCollapseRunnable, 4000);
    }

    /* access modifiers changed from: private */
    public void collapseChip() {
        if (this.mState == 2) {
            this.mState = 3;
            PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
            if (privacyChipDrawable != null) {
                privacyChipDrawable.collapse();
            }
            animateIconDisappearance();
        }
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public void onFlagMicCameraChanged(boolean z) {
        this.mMicCameraIndicatorFlagEnabled = z;
        updateChipOnFlagChanged();
    }

    private boolean isChipDisabled() {
        return !this.mMicCameraIndicatorFlagEnabled && !this.mAllIndicatorsEnabled;
    }

    private void updateChipOnFlagChanged() {
        if (isChipDisabled()) {
            fadeOutIndicator();
        } else {
            updateChip();
        }
    }

    private void fadeOutIndicator() {
        int i = this.mState;
        if (i != 0 && i != 4) {
            this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
            if (this.mViewAndWindowAdded) {
                this.mState = 4;
                animateIconDisappearance();
            } else {
                this.mState = 0;
                removeIndicatorView();
            }
            PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
            if (privacyChipDrawable != null) {
                privacyChipDrawable.updateIcons(0);
            }
        }
    }

    private void createAndShowIndicator() {
        boolean z = true;
        this.mState = 1;
        if (this.mIndicatorView != null || this.mViewAndWindowAdded) {
            removeIndicatorView();
        }
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R$layout.tv_ongoing_privacy_chip, (ViewGroup) null);
        this.mIndicatorView = viewGroup;
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (TvOngoingPrivacyChip.this.mState == 1) {
                    TvOngoingPrivacyChip.this.mViewAndWindowAdded = true;
                    TvOngoingPrivacyChip.this.mIndicatorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    TvOngoingPrivacyChip.this.postAccessibilityAnnouncement();
                    TvOngoingPrivacyChip.this.animateIconAppearance();
                    TvOngoingPrivacyChip.this.mChipDrawable.startInitialFadeIn();
                }
            }
        });
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        PrivacyChipDrawable privacyChipDrawable = new PrivacyChipDrawable(this.mContext);
        this.mChipDrawable = privacyChipDrawable;
        privacyChipDrawable.setListener(this);
        this.mChipDrawable.setRtl(z);
        ImageView imageView = (ImageView) this.mIndicatorView.findViewById(R$id.chip_drawable);
        if (imageView != null) {
            imageView.setImageDrawable(this.mChipDrawable);
        }
        LinearLayout linearLayout = (LinearLayout) this.mIndicatorView.findViewById(R$id.icons_container);
        this.mIconsContainer = linearLayout;
        linearLayout.setAlpha(0.0f);
        updateIcons();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2006, 8, -3);
        layoutParams.gravity = (z ? 3 : 5) | 48;
        layoutParams.setTitle("MicrophoneCaptureIndicator");
        layoutParams.packageName = this.mContext.getPackageName();
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mIndicatorView, layoutParams);
    }

    private void updateIcons() {
        List<Drawable> generateIcons = new PrivacyChipBuilder(this.mContext, this.mPrivacyItems).generateIcons();
        this.mIconsContainer.removeAllViews();
        for (int i = 0; i < generateIcons.size(); i++) {
            Drawable drawable = generateIcons.get(i);
            drawable.mutate().setTint(this.mContext.getColor(R$color.privacy_icon_tint));
            ImageView imageView = new ImageView(this.mContext);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout linearLayout = this.mIconsContainer;
            int i2 = this.mIconSize;
            linearLayout.addView(imageView, i2, i2);
            if (i != 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                marginLayoutParams.setMarginStart(this.mIconMarginStart);
                imageView.setLayoutParams(marginLayoutParams);
            }
        }
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            privacyChipDrawable.updateIcons(generateIcons.size());
        }
    }

    /* access modifiers changed from: private */
    public void animateIconAppearance() {
        animateIconAlphaTo(1.0f);
    }

    private void animateIconDisappearance() {
        animateIconAlphaTo(0.0f);
    }

    private void animateIconAlphaTo(float f) {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator == null) {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mAnimator = objectAnimator2;
            objectAnimator2.setTarget(this.mIconsContainer);
            this.mAnimator.setProperty(View.ALPHA);
            this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip.2
                boolean mCancelled;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator, boolean z) {
                    this.mCancelled = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        TvOngoingPrivacyChip.this.onIconAnimationFinished();
                    }
                }
            });
        } else if (objectAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (this.mIconsContainer.getAlpha() != f) {
            this.mAnimator.setDuration((long) this.mAnimationDurationMs);
            this.mAnimator.setFloatValues(f);
            this.mAnimator.start();
        }
    }

    @Override // com.android.systemui.privacy.television.PrivacyChipDrawable.PrivacyChipDrawableListener
    public void onFadeOutFinished() {
        if (this.mState == 4) {
            removeIndicatorView();
            this.mState = 0;
        }
    }

    /* access modifiers changed from: private */
    public void onIconAnimationFinished() {
        int i = this.mState;
        if (i == 1 || i == 2) {
            collapseLater();
        }
        int i2 = this.mState;
        if (i2 == 1) {
            this.mState = 2;
        } else if (i2 == 4) {
            removeIndicatorView();
            this.mState = 0;
        }
    }

    private void removeIndicatorView() {
        ViewGroup viewGroup;
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        if (!(windowManager == null || (viewGroup = this.mIndicatorView) == null)) {
            windowManager.removeView(viewGroup);
        }
        this.mIndicatorView = null;
        this.mAnimator = null;
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            privacyChipDrawable.setListener(null);
            this.mChipDrawable = null;
        }
        this.mViewAndWindowAdded = false;
    }

    /* access modifiers changed from: private */
    public void postAccessibilityAnnouncement() {
        this.mUiThreadHandler.removeCallbacks(this.mAccessibilityRunnable);
        if (this.mPrivacyItems.size() == 0) {
            makeAccessibilityAnnouncement();
        } else {
            this.mUiThreadHandler.postDelayed(this.mAccessibilityRunnable, 500);
        }
    }

    /* access modifiers changed from: private */
    public void makeAccessibilityAnnouncement() {
        int i;
        if (this.mIndicatorView != null) {
            List<PrivacyItem> list = this.mItemsBeforeLastAnnouncement;
            PrivacyType privacyType = PrivacyType.TYPE_CAMERA;
            boolean listContainsPrivacyType = listContainsPrivacyType(list, privacyType);
            boolean listContainsPrivacyType2 = listContainsPrivacyType(this.mPrivacyItems, privacyType);
            List<PrivacyItem> list2 = this.mItemsBeforeLastAnnouncement;
            PrivacyType privacyType2 = PrivacyType.TYPE_MICROPHONE;
            boolean listContainsPrivacyType3 = listContainsPrivacyType(list2, privacyType2);
            boolean listContainsPrivacyType4 = listContainsPrivacyType(this.mPrivacyItems, privacyType2);
            if (!listContainsPrivacyType && listContainsPrivacyType2 && !listContainsPrivacyType3 && listContainsPrivacyType4) {
                i = R$string.mic_and_camera_recording_announcement;
            } else if (!listContainsPrivacyType || listContainsPrivacyType2 || !listContainsPrivacyType3 || listContainsPrivacyType4) {
                if (!listContainsPrivacyType || listContainsPrivacyType2) {
                    i = (listContainsPrivacyType || !listContainsPrivacyType2) ? 0 : R$string.camera_recording_announcement;
                } else {
                    i = R$string.camera_stopped_recording_announcement;
                }
                if (i != 0) {
                    this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
                    i = 0;
                }
                if (listContainsPrivacyType3 && !listContainsPrivacyType4) {
                    i = R$string.mic_stopped_recording_announcement;
                } else if (!listContainsPrivacyType3 && listContainsPrivacyType4) {
                    i = R$string.mic_recording_announcement;
                }
            } else {
                i = R$string.mic_camera_stopped_recording_announcement;
            }
            if (i != 0) {
                this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
            }
            this.mItemsBeforeLastAnnouncement.clear();
            this.mItemsBeforeLastAnnouncement.addAll(this.mPrivacyItems);
        }
    }

    private boolean listContainsPrivacyType(List<PrivacyItem> list, PrivacyType privacyType) {
        for (PrivacyItem privacyItem : list) {
            if (privacyItem.getPrivacyType() == privacyType) {
                return true;
            }
        }
        return false;
    }
}
