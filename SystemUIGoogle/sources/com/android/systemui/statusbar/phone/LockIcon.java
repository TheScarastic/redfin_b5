package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ViewTreeObserver;
import com.android.systemui.R$anim;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.KeyguardAffordanceView;
/* loaded from: classes.dex */
public class LockIcon extends KeyguardAffordanceView {
    private static final int[][] LOCK_ANIM_RES_IDS = {new int[]{R$anim.lock_to_error, R$anim.lock_unlock, R$anim.lock_lock, R$anim.lock_scanning}, new int[]{R$anim.lock_to_error_circular, R$anim.lock_unlock_circular, R$anim.lock_lock_circular, R$anim.lock_scanning_circular}, new int[]{R$anim.lock_to_error_filled, R$anim.lock_unlock_filled, R$anim.lock_lock_filled, R$anim.lock_scanning_filled}, new int[]{R$anim.lock_to_error_rounded, R$anim.lock_unlock_rounded, R$anim.lock_lock_rounded, R$anim.lock_scanning_rounded}};
    private boolean mDozing;
    private boolean mKeyguardJustShown;
    private int mOldState;
    private boolean mPredrawRegistered;
    private int mState;
    private int mIconColor = 0;
    private final SparseArray<Drawable> mDrawableCache = new SparseArray<>();
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.phone.LockIcon.1
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            LockIcon.this.getViewTreeObserver().removeOnPreDrawListener(this);
            LockIcon.this.mPredrawRegistered = false;
            final int i = LockIcon.this.mState;
            Drawable icon = LockIcon.this.getIcon(i);
            LockIcon.this.setImageDrawable(icon, false);
            if (i == 2) {
                LockIcon lockIcon = LockIcon.this;
                lockIcon.announceForAccessibility(lockIcon.getResources().getString(R$string.accessibility_scanning_face));
            }
            if (!(icon instanceof AnimatedVectorDrawable)) {
                return true;
            }
            final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) icon;
            animatedVectorDrawable.forceAnimationOnUI();
            animatedVectorDrawable.clearAnimationCallbacks();
            animatedVectorDrawable.registerAnimationCallback(new Animatable2.AnimationCallback() { // from class: com.android.systemui.statusbar.phone.LockIcon.1.1
                @Override // android.graphics.drawable.Animatable2.AnimationCallback
                public void onAnimationEnd(Drawable drawable) {
                    if (LockIcon.this.getDrawable() == animatedVectorDrawable && i == LockIcon.this.mState && i == 2) {
                        animatedVectorDrawable.start();
                    } else {
                        Trace.endAsyncSection("LockIcon#Animation", i);
                    }
                }
            });
            Trace.beginAsyncSection("LockIcon#Animation", i);
            animatedVectorDrawable.start();
            return true;
        }
    };

    private static int getAnimationIndexForTransition(int i, int i2, boolean z, boolean z2) {
        if (z) {
            return -1;
        }
        if (i2 == 3) {
            return 0;
        }
        if (i != 1 && i2 == 1) {
            return 1;
        }
        if (i == 1 && i2 == 0 && !z2) {
            return 2;
        }
        return i2 == 2 ? 3 : -1;
    }

    public LockIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDrawableCache.clear();
    }

    /* access modifiers changed from: private */
    public Drawable getIcon(int i) {
        int animationIndexForTransition = getAnimationIndexForTransition(this.mOldState, i, this.mDozing, this.mKeyguardJustShown);
        int themedAnimationResId = animationIndexForTransition != -1 ? getThemedAnimationResId(animationIndexForTransition) : getIconForState(i);
        if (!this.mDrawableCache.contains(themedAnimationResId)) {
            this.mDrawableCache.put(themedAnimationResId, getContext().getDrawable(themedAnimationResId));
        }
        return this.mDrawableCache.get(themedAnimationResId);
    }

    private static int getIconForState(int i) {
        if (i != 0) {
            if (i == 1) {
                return 17302488;
            }
            if (!(i == 2 || i == 3)) {
                throw new IllegalArgumentException();
            }
        }
        return 17302479;
    }

    private int getThemedAnimationResId(int i) {
        String emptyIfNull = TextUtils.emptyIfNull(Settings.Secure.getString(getContext().getContentResolver(), "theme_customization_overlay_packages"));
        if (emptyIfNull.contains("com.android.theme.icon_pack.circular.android")) {
            return LOCK_ANIM_RES_IDS[1][i];
        }
        if (emptyIfNull.contains("com.android.theme.icon_pack.filled.android")) {
            return LOCK_ANIM_RES_IDS[2][i];
        }
        if (emptyIfNull.contains("com.android.theme.icon_pack.rounded.android")) {
            return LOCK_ANIM_RES_IDS[3][i];
        }
        return LOCK_ANIM_RES_IDS[0][i];
    }
}
