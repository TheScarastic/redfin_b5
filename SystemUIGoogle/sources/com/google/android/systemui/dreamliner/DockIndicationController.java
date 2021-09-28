package com.google.android.systemui.dreamliner;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.R$anim;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class DockIndicationController implements StatusBarStateController.StateListener, View.OnClickListener, View.OnAttachStateChangeListener, ConfigurationController.ConfigurationListener {
    @VisibleForTesting
    static final String ACTION_ASSISTANT_POODLE = "com.google.android.systemui.dreamliner.ASSISTANT_POODLE";
    private static final long KEYGUARD_INDICATION_TIMEOUT_MILLIS;
    private static final long PROMO_SHOWING_TIME_MILLIS;
    private final AccessibilityManager mAccessibilityManager;
    private final Context mContext;
    @VisibleForTesting
    FrameLayout mDockPromo;
    @VisibleForTesting
    ImageView mDockedTopIcon;
    private boolean mDocking;
    private boolean mDozing;
    private final Animation mHidePromoAnimation;
    @VisibleForTesting
    boolean mIconViewsValidated;
    private final KeyguardIndicationController mKeyguardIndicationController;
    private TextView mPromoText;
    private boolean mShowPromo;
    private final Animation mShowPromoAnimation;
    private int mShowPromoTimes;
    private final StatusBar mStatusBar;
    private boolean mTopIconShowing;
    private KeyguardIndicationTextView mTopIndicationView;
    private final Runnable mHidePromoRunnable = new Runnable() { // from class: com.google.android.systemui.dreamliner.DockIndicationController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            DockIndicationController.$r8$lambda$EtEREWukZO67FWKjk6W4L_zNvnc(DockIndicationController.this);
        }
    };
    private final Runnable mDisableLiveRegionRunnable = new Runnable() { // from class: com.google.android.systemui.dreamliner.DockIndicationController$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            DockIndicationController.$r8$lambda$fgigC0tWlCjIm6J3ADubxDvwSnQ(DockIndicationController.this);
        }
    };

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
    }

    static {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        PROMO_SHOWING_TIME_MILLIS = timeUnit.toMillis(2);
        KEYGUARD_INDICATION_TIMEOUT_MILLIS = timeUnit.toMillis(15);
    }

    public DockIndicationController(Context context, KeyguardIndicationController keyguardIndicationController, StatusBar statusBar) {
        this.mContext = context;
        this.mStatusBar = statusBar;
        this.mKeyguardIndicationController = keyguardIndicationController;
        ((SysuiStatusBarStateController) Dependency.get(StatusBarStateController.class)).addCallback(this);
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R$anim.dock_promo_animation);
        this.mShowPromoAnimation = loadAnimation;
        loadAnimation.setAnimationListener(new PhotoAnimationListener() { // from class: com.google.android.systemui.dreamliner.DockIndicationController.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                DockIndicationController dockIndicationController = DockIndicationController.this;
                dockIndicationController.mDockPromo.postDelayed(dockIndicationController.mHidePromoRunnable, DockIndicationController.this.getRecommendedTimeoutMillis(DockIndicationController.PROMO_SHOWING_TIME_MILLIS));
            }
        });
        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, R$anim.dock_promo_fade_out);
        this.mHidePromoAnimation = loadAnimation2;
        loadAnimation2.setAnimationListener(new PhotoAnimationListener() { // from class: com.google.android.systemui.dreamliner.DockIndicationController.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (DockIndicationController.this.mShowPromoTimes < 3) {
                    DockIndicationController.this.showPromoInner();
                    return;
                }
                DockIndicationController.this.mKeyguardIndicationController.setVisible(true);
                DockIndicationController.this.mDockPromo.setVisibility(8);
            }
        });
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.docked_top_icon) {
            Intent intent = new Intent(ACTION_ASSISTANT_POODLE);
            intent.addFlags(1073741824);
            try {
                this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            } catch (SecurityException e) {
                Log.w("DLIndicator", "Cannot send event for intent= " + intent, e);
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        this.mDozing = z;
        updateVisibility();
        updateLiveRegionIfNeeded();
        if (!this.mDozing) {
            this.mShowPromo = false;
        } else {
            showPromoInner();
        }
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        view.removeOnAttachStateChangeListener(this);
        this.mIconViewsValidated = false;
        this.mDockedTopIcon = null;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onLocaleListChanged() {
        if (!this.mIconViewsValidated) {
            initializeIconViews();
        }
        this.mPromoText.setText(this.mContext.getResources().getString(R$string.dock_promo_text));
    }

    public void setShowing(boolean z) {
        this.mTopIconShowing = z;
        updateVisibility();
    }

    public void setDocking(boolean z) {
        this.mDocking = z;
        if (!z) {
            this.mTopIconShowing = false;
            this.mShowPromo = false;
        }
        updateVisibility();
        updateLiveRegionIfNeeded();
    }

    @VisibleForTesting
    void initializeIconViews() {
        NotificationShadeWindowView notificationShadeWindowView = this.mStatusBar.getNotificationShadeWindowView();
        ImageView imageView = (ImageView) notificationShadeWindowView.findViewById(R$id.docked_top_icon);
        this.mDockedTopIcon = imageView;
        imageView.setImageResource(R$drawable.ic_assistant_logo);
        ImageView imageView2 = this.mDockedTopIcon;
        Context context = this.mContext;
        int i = R$string.accessibility_assistant_poodle;
        imageView2.setContentDescription(context.getString(i));
        this.mDockedTopIcon.setTooltipText(this.mContext.getString(i));
        this.mDockedTopIcon.setOnClickListener(this);
        this.mDockPromo = (FrameLayout) notificationShadeWindowView.findViewById(R$id.dock_promo);
        TextView textView = (TextView) notificationShadeWindowView.findViewById(R$id.photo_promo_text);
        this.mPromoText = textView;
        textView.setAutoSizeTextTypeUniformWithConfiguration(10, 16, 1, 2);
        notificationShadeWindowView.findViewById(R$id.ambient_indication).addOnAttachStateChangeListener(this);
        this.mTopIndicationView = (KeyguardIndicationTextView) notificationShadeWindowView.findViewById(R$id.keyguard_indication_text);
        this.mIconViewsValidated = true;
    }

    public void showPromo(ResultReceiver resultReceiver) {
        this.mShowPromoTimes = 0;
        this.mShowPromo = true;
        if (!this.mDozing || !this.mDocking) {
            resultReceiver.send(1, null);
            return;
        }
        showPromoInner();
        resultReceiver.send(0, null);
    }

    public boolean isPromoShowing() {
        return this.mDockPromo.getVisibility() == 0;
    }

    /* access modifiers changed from: private */
    public void showPromoInner() {
        if (this.mDozing && this.mDocking && this.mShowPromo) {
            this.mKeyguardIndicationController.setVisible(false);
            this.mDockPromo.setVisibility(0);
            this.mDockPromo.startAnimation(this.mShowPromoAnimation);
            this.mShowPromoTimes++;
        }
    }

    /* access modifiers changed from: private */
    public void hidePromo() {
        if (this.mDozing && this.mDocking) {
            this.mDockPromo.startAnimation(this.mHidePromoAnimation);
        }
    }

    private void updateVisibility() {
        if (!this.mIconViewsValidated) {
            initializeIconViews();
        }
        if (!this.mDozing || !this.mDocking) {
            this.mDockPromo.setVisibility(8);
            this.mDockedTopIcon.setVisibility(8);
            this.mKeyguardIndicationController.setVisible(true);
        } else if (!this.mTopIconShowing) {
            this.mDockedTopIcon.setVisibility(8);
        } else {
            this.mDockedTopIcon.setVisibility(0);
        }
    }

    private void updateLiveRegionIfNeeded() {
        int accessibilityLiveRegion = this.mTopIndicationView.getAccessibilityLiveRegion();
        if (this.mDozing && this.mDocking) {
            this.mTopIndicationView.removeCallbacks(this.mDisableLiveRegionRunnable);
            this.mTopIndicationView.postDelayed(this.mDisableLiveRegionRunnable, getRecommendedTimeoutMillis(KEYGUARD_INDICATION_TIMEOUT_MILLIS));
        } else if (accessibilityLiveRegion != 1) {
            this.mTopIndicationView.setAccessibilityLiveRegion(1);
        }
    }

    /* access modifiers changed from: private */
    public void disableLiveRegion() {
        if (this.mDocking && this.mDozing) {
            this.mTopIndicationView.setAccessibilityLiveRegion(0);
        }
    }

    /* access modifiers changed from: private */
    public long getRecommendedTimeoutMillis(long j) {
        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
        return accessibilityManager == null ? j : (long) accessibilityManager.getRecommendedTimeoutMillis(Math.toIntExact(j), 2);
    }

    /* loaded from: classes2.dex */
    private static class PhotoAnimationListener implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        private PhotoAnimationListener() {
        }
    }
}
