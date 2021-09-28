package com.google.android.systemui.ambientmusic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.AutoReinflateContainer;
import com.android.systemui.Dependency;
import com.android.systemui.R$anim;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
/* loaded from: classes2.dex */
public class AmbientIndicationContainer extends AutoReinflateContainer implements DozeReceiver, View.OnClickListener, StatusBarStateController.StateListener, NotificationMediaManager.MediaListener {
    private Drawable mAmbientIconOverride;
    private int mAmbientIndicationIconSize;
    private Drawable mAmbientMusicAnimation;
    private PendingIntent mAmbientMusicIntent;
    private Drawable mAmbientMusicNoteIcon;
    private int mAmbientMusicNoteIconIconSize;
    private CharSequence mAmbientMusicText;
    private boolean mAmbientSkipUnlock;
    private int mBurnInPreventionOffset;
    private float mDozeAmount;
    private boolean mDozing;
    private int mDrawablePadding;
    private final Handler mHandler;
    private final Rect mIconBounds = new Rect();
    private int mIndicationTextMode;
    private boolean mIsKeyguardLayoutEnabled;
    private int mMediaPlaybackState;
    private boolean mNotificationsHidden;
    private Drawable mReverseChargingAnimation;
    private CharSequence mReverseChargingMessage;
    private StatusBar mStatusBar;
    private int mStatusBarState;
    private TextView mText;
    private int mTextColor;
    private ValueAnimator mTextColorAnimator;
    private final WakeLock mWakeLock;
    private CharSequence mWirelessChargingMessage;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updatePill$2() {
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updatePill$3() {
    }

    public AmbientIndicationContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mWakeLock = createWakeLock(((FrameLayout) this).mContext, handler);
    }

    @VisibleForTesting
    WakeLock createWakeLock(Context context, Handler handler) {
        return new DelayedWakeLock(handler, WakeLock.createPartial(context, "AmbientIndication"));
    }

    public void initializeView(StatusBar statusBar, FeatureFlags featureFlags) {
        this.mStatusBar = statusBar;
        this.mIsKeyguardLayoutEnabled = featureFlags.isKeyguardLayoutEnabled();
        addInflateListener(new AutoReinflateContainer.InflateListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer$$ExternalSyntheticLambda2
            @Override // com.android.systemui.AutoReinflateContainer.InflateListener
            public final void onInflated(View view) {
                AmbientIndicationContainer.this.lambda$initializeView$0(view);
            }
        });
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                AmbientIndicationContainer.this.lambda$initializeView$1(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeView$0(View view) {
        this.mText = (TextView) findViewById(R$id.ambient_indication_text);
        this.mAmbientMusicAnimation = ((FrameLayout) this).mContext.getDrawable(R$anim.audioanim_animation);
        this.mAmbientMusicNoteIcon = ((FrameLayout) this).mContext.getDrawable(R$drawable.ic_music_note);
        this.mReverseChargingAnimation = ((FrameLayout) this).mContext.getDrawable(R$anim.reverse_charging_animation);
        this.mTextColor = this.mText.getCurrentTextColor();
        this.mAmbientIndicationIconSize = getResources().getDimensionPixelSize(R$dimen.ambient_indication_icon_size);
        this.mAmbientMusicNoteIconIconSize = getResources().getDimensionPixelSize(R$dimen.ambient_indication_note_icon_size);
        this.mBurnInPreventionOffset = getResources().getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
        this.mDrawablePadding = this.mText.getCompoundDrawablePadding();
        updateColors();
        updatePill();
        this.mText.setOnClickListener(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeView$1(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateBottomPadding();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.AutoReinflateContainer, android.view.View, android.view.ViewGroup
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((StatusBarStateController) Dependency.get(StatusBarStateController.class)).addCallback(this);
        ((NotificationMediaManager) Dependency.get(NotificationMediaManager.class)).addCallback(this);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.AutoReinflateContainer, android.view.View, android.view.ViewGroup
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((StatusBarStateController) Dependency.get(StatusBarStateController.class)).removeCallback(this);
        ((NotificationMediaManager) Dependency.get(NotificationMediaManager.class)).removeCallback(this);
        this.mMediaPlaybackState = 0;
    }

    public void setAmbientMusic(CharSequence charSequence, PendingIntent pendingIntent, int i, boolean z) {
        this.mAmbientMusicText = charSequence;
        this.mAmbientMusicIntent = pendingIntent;
        this.mAmbientSkipUnlock = z;
        this.mAmbientIconOverride = getAmbientIconOverride(i, ((FrameLayout) this).mContext);
        updatePill();
    }

    static Drawable getAmbientIconOverride(int i, Context context) {
        switch (i) {
            case 1:
                return context.getDrawable(R$drawable.ic_music_search);
            case 2:
            default:
                return null;
            case 3:
                return context.getDrawable(R$drawable.ic_music_not_found);
            case 4:
                return context.getDrawable(R$drawable.ic_cloud_off);
            case 5:
                return context.getDrawable(R$drawable.ic_favorite);
            case 6:
                return context.getDrawable(R$drawable.ic_favorite_border);
            case 7:
                return context.getDrawable(R$drawable.ic_error);
            case 8:
                return context.getDrawable(R$drawable.ic_favorite_note);
        }
    }

    public static Drawable getNowPlayingIcon(int i, Context context) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.ambient_indication_note_icon_size);
        Drawable ambientIconOverride = getAmbientIconOverride(i, context);
        if (ambientIconOverride == null) {
            ambientIconOverride = context.getDrawable(R$drawable.ic_music_note);
            dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.ambient_indication_icon_size);
        }
        if (ambientIconOverride != null) {
            Rect rect = new Rect();
            rect.set(0, 0, ambientIconOverride.getIntrinsicWidth(), ambientIconOverride.getIntrinsicHeight());
            MathUtils.fitRect(rect, dimensionPixelSize);
            ambientIconOverride.setBounds(rect);
        }
        return ambientIconOverride;
    }

    public void setWirelessChargingMessage(CharSequence charSequence) {
        this.mWirelessChargingMessage = charSequence;
        this.mReverseChargingMessage = null;
        updatePill();
    }

    public void setReverseChargingMessage(CharSequence charSequence) {
        this.mWirelessChargingMessage = null;
        this.mReverseChargingMessage = charSequence;
        updatePill();
    }

    private void updatePill() {
        int i = this.mIndicationTextMode;
        boolean z = true;
        this.mIndicationTextMode = 1;
        CharSequence charSequence = this.mAmbientMusicText;
        int i2 = 0;
        boolean z2 = this.mText.getVisibility() == 0;
        Drawable drawable = z2 ? this.mAmbientMusicNoteIcon : this.mAmbientMusicAnimation;
        Drawable drawable2 = this.mAmbientIconOverride;
        if (drawable2 != null) {
            drawable = drawable2;
        }
        CharSequence charSequence2 = this.mAmbientMusicText;
        boolean z3 = charSequence2 != null && charSequence2.length() == 0;
        this.mText.setClickable(this.mAmbientMusicIntent != null);
        if (!TextUtils.isEmpty(this.mReverseChargingMessage)) {
            this.mIndicationTextMode = 2;
            charSequence = this.mReverseChargingMessage;
            drawable = this.mReverseChargingAnimation;
            this.mText.setClickable(false);
            z3 = false;
        } else if (!TextUtils.isEmpty(this.mWirelessChargingMessage)) {
            this.mIndicationTextMode = 3;
            charSequence = this.mWirelessChargingMessage;
            this.mText.setClickable(false);
            z3 = false;
            drawable = null;
        }
        this.mText.setText(charSequence);
        this.mText.setContentDescription(charSequence);
        if (drawable != null) {
            this.mIconBounds.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            MathUtils.fitRect(this.mIconBounds, drawable == this.mAmbientMusicNoteIcon ? this.mAmbientMusicNoteIconIconSize : this.mAmbientIndicationIconSize);
            drawable.setBounds(this.mIconBounds);
        }
        Drawable drawable3 = isLayoutRtl() ? null : drawable;
        this.mText.setCompoundDrawables(drawable3, null, drawable3 == null ? drawable : null, null);
        this.mText.setCompoundDrawablePadding((z3 || drawable == null) ? 0 : this.mDrawablePadding);
        if ((TextUtils.isEmpty(charSequence) && !z3) || this.mNotificationsHidden) {
            z = false;
        }
        TextView textView = this.mText;
        if (!z) {
            i2 = 8;
        }
        textView.setVisibility(i2);
        if (!z) {
            this.mText.animate().cancel();
            if (drawable != null && (drawable instanceof AnimatedVectorDrawable)) {
                ((AnimatedVectorDrawable) drawable).reset();
            }
            this.mHandler.post(this.mWakeLock.wrap(AmbientIndicationContainer$$ExternalSyntheticLambda3.INSTANCE));
        } else if (!z2) {
            this.mWakeLock.acquire("AmbientIndication");
            if (drawable != null && (drawable instanceof AnimatedVectorDrawable)) {
                ((AnimatedVectorDrawable) drawable).start();
            }
            TextView textView2 = this.mText;
            textView2.setTranslationY((float) (textView2.getHeight() / 2));
            this.mText.setAlpha(0.0f);
            this.mText.animate().alpha(1.0f).translationY(0.0f).setStartDelay(150).setDuration(100).setListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AmbientIndicationContainer.this.mWakeLock.release("AmbientIndication");
                }
            }).setInterpolator(Interpolators.DECELERATE_QUINT).start();
        } else if (i == this.mIndicationTextMode) {
            this.mHandler.post(this.mWakeLock.wrap(AmbientIndicationContainer$$ExternalSyntheticLambda4.INSTANCE));
        } else if (drawable != null && (drawable instanceof AnimatedVectorDrawable)) {
            this.mWakeLock.acquire("AmbientIndication");
            ((AnimatedVectorDrawable) drawable).start();
            this.mWakeLock.release("AmbientIndication");
        }
        updateBottomPadding();
    }

    private void updateBottomPadding() {
        this.mStatusBar.getPanelController().setAmbientIndicationBottomPadding(this.mText.getVisibility() == 0 ? this.mStatusBar.getNotificationScrollLayout().getBottom() - getTop() : 0);
    }

    public void hideAmbientMusic() {
        setAmbientMusic(null, null, 0, false);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mAmbientMusicIntent != null) {
            this.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), this.mText, "AMBIENT_MUSIC_CLICK");
            if (this.mAmbientSkipUnlock) {
                sendBroadcastWithoutDismissingKeyguard(this.mAmbientMusicIntent);
            } else {
                this.mStatusBar.startPendingIntentDismissingKeyguard(this.mAmbientMusicIntent);
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        this.mDozing = z;
        updateVisibility();
        this.mText.setEnabled(!z);
        updateColors();
        updateBurnInOffsets();
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        updatePill();
        updateBurnInOffsets();
    }

    private void updateBurnInOffsets() {
        int burnInOffset = BurnInHelperKt.getBurnInOffset(this.mBurnInPreventionOffset * 2, true);
        int i = this.mBurnInPreventionOffset;
        setTranslationX(((float) (burnInOffset - i)) * this.mDozeAmount);
        setTranslationY(((float) (BurnInHelperKt.getBurnInOffset(i * 2, false) - this.mBurnInPreventionOffset)) * this.mDozeAmount);
    }

    private void updateColors() {
        ValueAnimator valueAnimator = this.mTextColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mTextColorAnimator.cancel();
        }
        int defaultColor = this.mText.getTextColors().getDefaultColor();
        int i = this.mDozing ? -1 : this.mTextColor;
        if (defaultColor == i) {
            this.mText.setTextColor(i);
            this.mText.setCompoundDrawableTintList(ColorStateList.valueOf(i));
            return;
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(defaultColor, i);
        this.mTextColorAnimator = ofArgb;
        ofArgb.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        this.mTextColorAnimator.setDuration(500L);
        this.mTextColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                AmbientIndicationContainer.this.lambda$updateColors$4(valueAnimator2);
            }
        });
        this.mTextColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AmbientIndicationContainer.this.mTextColorAnimator = null;
            }
        });
        this.mTextColorAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateColors$4(ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        this.mText.setTextColor(intValue);
        this.mText.setCompoundDrawableTintList(ColorStateList.valueOf(intValue));
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        this.mStatusBarState = i;
        updateVisibility();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozeAmountChanged(float f, float f2) {
        this.mDozeAmount = f2;
        updateBurnInOffsets();
    }

    private void sendBroadcastWithoutDismissingKeyguard(PendingIntent pendingIntent) {
        if (!pendingIntent.isActivity()) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.w("AmbientIndication", "Sending intent failed: " + e);
            }
        }
    }

    private void updateVisibility() {
        if (this.mStatusBarState == 1) {
            setVisibility(0);
        } else {
            setVisibility(4);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
    public void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        if (this.mMediaPlaybackState != i) {
            this.mMediaPlaybackState = i;
            if (isMediaPlaying()) {
                hideAmbientMusic();
            }
        }
    }

    protected boolean isMediaPlaying() {
        return NotificationMediaManager.isPlayingState(this.mMediaPlaybackState);
    }
}
