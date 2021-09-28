package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthDialog;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class AuthBiometricView extends LinearLayout {
    private final AccessibilityManager mAccessibilityManager;
    private final View.OnClickListener mBackgroundClickListener;
    protected Callback mCallback;
    @VisibleForTesting
    Button mCancelButton;
    @VisibleForTesting
    Button mConfirmButton;
    private TextView mDescriptionView;
    protected boolean mDialogSizeAnimating;
    private int mEffectiveUserId;
    protected final Handler mHandler;
    private View mIconHolderView;
    private float mIconOriginalY;
    protected ImageView mIconView;
    protected TextView mIndicatorView;
    private final Injector mInjector;
    @VisibleForTesting
    AuthDialog.LayoutParams mLayoutParams;
    @VisibleForTesting
    Button mNegativeButton;
    private AuthPanelController mPanelController;
    private PromptInfo mPromptInfo;
    private boolean mRequireConfirmation;
    private final Runnable mResetErrorRunnable;
    private final Runnable mResetHelpRunnable;
    protected Bundle mSavedState;
    private int mSize;
    protected int mState;
    private TextView mSubtitleView;
    protected final int mTextColorError;
    protected final int mTextColorHint;
    private TextView mTitleView;
    @VisibleForTesting
    Button mTryAgainButton;
    @VisibleForTesting
    Button mUseCredentialButton;
    private int mUserId;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        void onAction(int i);
    }

    protected abstract int getDelayAfterAuthenticatedDurationMs();

    protected abstract int getStateForAfterError();

    protected abstract void handleResetAfterError();

    protected abstract void handleResetAfterHelp();

    protected boolean supportsManualRetry() {
        return false;
    }

    protected abstract boolean supportsSmallDialog();

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class Injector {
        AuthBiometricView mBiometricView;

        public int getDelayAfterError() {
            return 2000;
        }

        public int getMediumToLargeAnimationDurationMs() {
            return 450;
        }

        Injector() {
        }

        public Button getNegativeButton() {
            return (Button) this.mBiometricView.findViewById(R$id.button_negative);
        }

        public Button getCancelButton() {
            return (Button) this.mBiometricView.findViewById(R$id.button_cancel);
        }

        public Button getUseCredentialButton() {
            return (Button) this.mBiometricView.findViewById(R$id.button_use_credential);
        }

        public Button getConfirmButton() {
            return (Button) this.mBiometricView.findViewById(R$id.button_confirm);
        }

        public Button getTryAgainButton() {
            return (Button) this.mBiometricView.findViewById(R$id.button_try_again);
        }

        public TextView getTitleView() {
            return (TextView) this.mBiometricView.findViewById(R$id.title);
        }

        public TextView getSubtitleView() {
            return (TextView) this.mBiometricView.findViewById(R$id.subtitle);
        }

        public TextView getDescriptionView() {
            return (TextView) this.mBiometricView.findViewById(R$id.description);
        }

        public TextView getIndicatorView() {
            return (TextView) this.mBiometricView.findViewById(R$id.indicator);
        }

        public ImageView getIconView() {
            return (ImageView) this.mBiometricView.findViewById(R$id.biometric_icon);
        }

        public View getIconHolderView() {
            return this.mBiometricView.findViewById(R$id.biometric_icon_frame);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (this.mState == 6) {
            Log.w("BiometricPrompt/AuthBiometricView", "Ignoring background click after authenticated");
            return;
        }
        int i = this.mSize;
        if (i == 1) {
            Log.w("BiometricPrompt/AuthBiometricView", "Ignoring background click during small dialog");
        } else if (i == 3) {
            Log.w("BiometricPrompt/AuthBiometricView", "Ignoring background click during large dialog");
        } else {
            this.mCallback.onAction(2);
        }
    }

    public AuthBiometricView(Context context) {
        this(context, null);
    }

    public AuthBiometricView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, new Injector());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public AuthBiometricView(Context context, AttributeSet attributeSet, Injector injector) {
        super(context, attributeSet);
        this.mSize = 0;
        this.mBackgroundClickListener = new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$new$0(view);
            }
        };
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mTextColorError = getResources().getColor(R$color.biometric_dialog_error, context.getTheme());
        this.mTextColorHint = getResources().getColor(R$color.biometric_dialog_gray, context.getTheme());
        this.mInjector = injector;
        injector.mBiometricView = this;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mResetErrorRunnable = new Runnable() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                AuthBiometricView.this.lambda$new$1();
            }
        };
        this.mResetHelpRunnable = new Runnable() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                AuthBiometricView.this.lambda$new$2();
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        updateState(getStateForAfterError());
        handleResetAfterError();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        updateState(2);
        handleResetAfterHelp();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    public void setPanelController(AuthPanelController authPanelController) {
        this.mPanelController = authPanelController;
    }

    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setBackgroundView(View view) {
        view.setOnClickListener(this.mBackgroundClickListener);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    public void setRequireConfirmation(boolean z) {
        this.mRequireConfirmation = z;
    }

    @VisibleForTesting
    void updateSize(final int i) {
        Log.v("BiometricPrompt/AuthBiometricView", "Current size: " + this.mSize + " New size: " + i);
        if (i == 1) {
            this.mTitleView.setVisibility(8);
            this.mSubtitleView.setVisibility(8);
            this.mDescriptionView.setVisibility(8);
            this.mIndicatorView.setVisibility(8);
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setVisibility(8);
            float dimension = getResources().getDimension(R$dimen.biometric_dialog_icon_padding);
            this.mIconHolderView.setY(((float) (getHeight() - this.mIconHolderView.getHeight())) - dimension);
            this.mPanelController.updateForContentDimensions(this.mLayoutParams.mMediumWidth, ((this.mIconHolderView.getHeight() + (((int) dimension) * 2)) - this.mIconHolderView.getPaddingTop()) - this.mIconHolderView.getPaddingBottom(), 0);
            this.mSize = i;
        } else if (this.mSize == 1 && i == 2) {
            if (!this.mDialogSizeAnimating) {
                this.mDialogSizeAnimating = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mIconHolderView.getY(), this.mIconOriginalY);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        AuthBiometricView.this.lambda$updateSize$3(valueAnimator);
                    }
                });
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda3
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        AuthBiometricView.this.lambda$updateSize$4(valueAnimator);
                    }
                });
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(150L);
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthBiometricView.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        AuthBiometricView.this.mTitleView.setVisibility(0);
                        AuthBiometricView.this.mIndicatorView.setVisibility(0);
                        if (AuthBiometricView.this.isDeviceCredentialAllowed()) {
                            AuthBiometricView.this.mUseCredentialButton.setVisibility(0);
                        } else {
                            AuthBiometricView.this.mNegativeButton.setVisibility(0);
                        }
                        if (AuthBiometricView.this.supportsManualRetry()) {
                            AuthBiometricView.this.mTryAgainButton.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mSubtitleView.getText())) {
                            AuthBiometricView.this.mSubtitleView.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mDescriptionView.getText())) {
                            AuthBiometricView.this.mDescriptionView.setVisibility(0);
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        AuthBiometricView.this.mSize = i;
                        AuthBiometricView authBiometricView = AuthBiometricView.this;
                        authBiometricView.mDialogSizeAnimating = false;
                        Utils.notifyAccessibilityContentChanged(authBiometricView.mAccessibilityManager, AuthBiometricView.this);
                    }
                });
                animatorSet.play(ofFloat).with(ofFloat2);
                animatorSet.start();
                AuthPanelController authPanelController = this.mPanelController;
                AuthDialog.LayoutParams layoutParams = this.mLayoutParams;
                authPanelController.updateForContentDimensions(layoutParams.mMediumWidth, layoutParams.mMediumHeight, 150);
            } else {
                return;
            }
        } else if (i == 2) {
            AuthPanelController authPanelController2 = this.mPanelController;
            AuthDialog.LayoutParams layoutParams2 = this.mLayoutParams;
            authPanelController2.updateForContentDimensions(layoutParams2.mMediumWidth, layoutParams2.mMediumHeight, 0);
            this.mSize = i;
        } else if (i == 3) {
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(getY(), getY() - getResources().getDimension(R$dimen.biometric_dialog_medium_to_large_translation_offset));
            ofFloat3.setDuration((long) this.mInjector.getMediumToLargeAnimationDurationMs());
            ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthBiometricView.lambda$updateSize$5(AuthBiometricView.this, valueAnimator);
                }
            });
            ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthBiometricView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (this.getParent() != null) {
                        ((ViewGroup) this.getParent()).removeView(this);
                    }
                    AuthBiometricView.this.mSize = i;
                }
            });
            ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat4.setDuration((long) (this.mInjector.getMediumToLargeAnimationDurationMs() / 2));
            ofFloat4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AuthBiometricView.lambda$updateSize$6(AuthBiometricView.this, valueAnimator);
                }
            });
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController3 = this.mPanelController;
            authPanelController3.updateForContentDimensions(authPanelController3.getContainerWidth(), this.mPanelController.getContainerHeight(), this.mInjector.getMediumToLargeAnimationDurationMs());
            AnimatorSet animatorSet2 = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            arrayList.add(ofFloat3);
            arrayList.add(ofFloat4);
            animatorSet2.playTogether(arrayList);
            animatorSet2.setDuration((long) ((this.mInjector.getMediumToLargeAnimationDurationMs() * 2) / 3));
            animatorSet2.start();
        } else {
            Log.e("BiometricPrompt/AuthBiometricView", "Unknown transition from: " + this.mSize + " to: " + i);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSize$3(ValueAnimator valueAnimator) {
        this.mIconHolderView.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSize$4(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mTitleView.setAlpha(floatValue);
        this.mIndicatorView.setAlpha(floatValue);
        this.mNegativeButton.setAlpha(floatValue);
        this.mCancelButton.setAlpha(floatValue);
        this.mTryAgainButton.setAlpha(floatValue);
        if (!TextUtils.isEmpty(this.mSubtitleView.getText())) {
            this.mSubtitleView.setAlpha(floatValue);
        }
        if (!TextUtils.isEmpty(this.mDescriptionView.getText())) {
            this.mDescriptionView.setAlpha(floatValue);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateSize$5(AuthBiometricView authBiometricView, ValueAnimator valueAnimator) {
        authBiometricView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateSize$6(AuthBiometricView authBiometricView, ValueAnimator valueAnimator) {
        authBiometricView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void updateState(int i) {
        Log.v("BiometricPrompt/AuthBiometricView", "newState: " + i);
        if (i == 1 || i == 2) {
            removePendingAnimations();
            if (this.mRequireConfirmation) {
                this.mConfirmButton.setEnabled(false);
                this.mConfirmButton.setVisibility(0);
            }
        } else if (i != 4) {
            if (i == 5) {
                removePendingAnimations();
                this.mNegativeButton.setVisibility(8);
                this.mCancelButton.setVisibility(0);
                this.mUseCredentialButton.setVisibility(8);
                this.mConfirmButton.setEnabled(true);
                this.mConfirmButton.setVisibility(0);
                this.mIndicatorView.setTextColor(this.mTextColorHint);
                this.mIndicatorView.setText(R$string.biometric_dialog_tap_confirm);
                this.mIndicatorView.setVisibility(0);
            } else if (i != 6) {
                Log.w("BiometricPrompt/AuthBiometricView", "Unhandled state: " + i);
            } else {
                if (this.mSize != 1) {
                    this.mConfirmButton.setVisibility(8);
                    this.mNegativeButton.setVisibility(8);
                    this.mUseCredentialButton.setVisibility(8);
                    this.mCancelButton.setVisibility(8);
                    this.mIndicatorView.setVisibility(4);
                }
                announceForAccessibility(getResources().getString(R$string.biometric_dialog_authenticated));
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        AuthBiometricView.this.lambda$updateState$7();
                    }
                }, (long) getDelayAfterAuthenticatedDurationMs());
            }
        } else if (this.mSize == 1) {
            updateSize(2);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
        this.mState = i;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$7() {
        this.mCallback.onAction(1);
    }

    public void onDialogAnimatedIn() {
        updateState(2);
    }

    public void onAuthenticationSucceeded() {
        removePendingAnimations();
        if (this.mRequireConfirmation) {
            updateState(5);
        } else {
            updateState(6);
        }
    }

    public void onAuthenticationFailed(int i, String str) {
        showTemporaryMessage(str, this.mResetErrorRunnable);
        updateState(4);
    }

    public void onError(int i, String str) {
        showTemporaryMessage(str, this.mResetErrorRunnable);
        updateState(4);
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                AuthBiometricView.this.lambda$onError$8();
            }
        }, (long) this.mInjector.getDelayAfterError());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onError$8() {
        this.mCallback.onAction(5);
    }

    public void onHelp(int i, String str) {
        if (this.mSize != 2) {
            Log.w("BiometricPrompt/AuthBiometricView", "Help received in size: " + this.mSize);
        } else if (TextUtils.isEmpty(str)) {
            Log.w("BiometricPrompt/AuthBiometricView", "Ignoring blank help message");
        } else {
            showTemporaryMessage(str, this.mResetHelpRunnable);
            updateState(3);
        }
    }

    public void onSaveState(Bundle bundle) {
        bundle.putInt("confirm_visibility", this.mConfirmButton.getVisibility());
        bundle.putInt("try_agian_visibility", this.mTryAgainButton.getVisibility());
        bundle.putInt("state", this.mState);
        bundle.putString("indicator_string", this.mIndicatorView.getText() != null ? this.mIndicatorView.getText().toString() : "");
        bundle.putBoolean("error_is_temporary", this.mHandler.hasCallbacks(this.mResetErrorRunnable));
        bundle.putBoolean("hint_is_temporary", this.mHandler.hasCallbacks(this.mResetHelpRunnable));
        bundle.putInt("size", this.mSize);
    }

    public void restoreState(Bundle bundle) {
        this.mSavedState = bundle;
    }

    private void setTextOrHide(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setText(charSequence);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    private void removePendingAnimations() {
        this.mHandler.removeCallbacks(this.mResetHelpRunnable);
        this.mHandler.removeCallbacks(this.mResetErrorRunnable);
    }

    private void showTemporaryMessage(String str, Runnable runnable) {
        removePendingAnimations();
        this.mIndicatorView.setText(str);
        this.mIndicatorView.setTextColor(this.mTextColorError);
        this.mIndicatorView.setVisibility(0);
        this.mHandler.postDelayed(runnable, (long) this.mInjector.getDelayAfterError());
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        onFinishInflateInternal();
    }

    @VisibleForTesting
    void onFinishInflateInternal() {
        this.mTitleView = this.mInjector.getTitleView();
        this.mSubtitleView = this.mInjector.getSubtitleView();
        this.mDescriptionView = this.mInjector.getDescriptionView();
        this.mIconView = this.mInjector.getIconView();
        this.mIconHolderView = this.mInjector.getIconHolderView();
        this.mIndicatorView = this.mInjector.getIndicatorView();
        this.mNegativeButton = this.mInjector.getNegativeButton();
        this.mCancelButton = this.mInjector.getCancelButton();
        this.mUseCredentialButton = this.mInjector.getUseCredentialButton();
        this.mConfirmButton = this.mInjector.getConfirmButton();
        this.mTryAgainButton = this.mInjector.getTryAgainButton();
        this.mNegativeButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$onFinishInflateInternal$9(view);
            }
        });
        this.mCancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$onFinishInflateInternal$10(view);
            }
        });
        this.mUseCredentialButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$onFinishInflateInternal$11(view);
            }
        });
        this.mConfirmButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$onFinishInflateInternal$12(view);
            }
        });
        this.mTryAgainButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AuthBiometricView.this.lambda$onFinishInflateInternal$13(view);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflateInternal$9(View view) {
        this.mCallback.onAction(3);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflateInternal$10(View view) {
        this.mCallback.onAction(2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflateInternal$11(View view) {
        startTransitionToCredentialUI();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflateInternal$12(View view) {
        updateState(6);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflateInternal$13(View view) {
        updateState(2);
        this.mCallback.onAction(4);
        this.mTryAgainButton.setVisibility(8);
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: package-private */
    public void startTransitionToCredentialUI() {
        updateSize(3);
        this.mCallback.onAction(6);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onAttachedToWindowInternal();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void onAttachedToWindowInternal() {
        String str;
        this.mTitleView.setText(this.mPromptInfo.getTitle());
        if (isDeviceCredentialAllowed()) {
            int credentialType = Utils.getCredentialType(((LinearLayout) this).mContext, this.mEffectiveUserId);
            if (credentialType == 1) {
                str = getResources().getString(R$string.biometric_dialog_use_pin);
            } else if (credentialType == 2) {
                str = getResources().getString(R$string.biometric_dialog_use_pattern);
            } else if (credentialType != 3) {
                str = getResources().getString(R$string.biometric_dialog_use_password);
            } else {
                str = getResources().getString(R$string.biometric_dialog_use_password);
            }
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setText(str);
            this.mUseCredentialButton.setVisibility(0);
        } else {
            this.mNegativeButton.setText(this.mPromptInfo.getNegativeButtonText());
        }
        setTextOrHide(this.mSubtitleView, this.mPromptInfo.getSubtitle());
        setTextOrHide(this.mDescriptionView, this.mPromptInfo.getDescription());
        Bundle bundle = this.mSavedState;
        if (bundle == null) {
            updateState(1);
            return;
        }
        updateState(bundle.getInt("state"));
        this.mConfirmButton.setVisibility(this.mSavedState.getInt("confirm_visibility"));
        if (this.mConfirmButton.getVisibility() == 8) {
            setRequireConfirmation(false);
        }
        this.mTryAgainButton.setVisibility(this.mSavedState.getInt("try_agian_visibility"));
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    /* access modifiers changed from: package-private */
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getId() == R$id.space_above_icon || childAt.getId() == R$id.space_below_icon || childAt.getId() == R$id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.biometric_icon_frame) {
                View findViewById = findViewById(R$id.biometric_icon);
                childAt.measure(View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.biometric_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            }
            if (childAt.getVisibility() != 8) {
                i3 += childAt.getMeasuredHeight();
            }
        }
        return new AuthDialog.LayoutParams(i, i3);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        AuthDialog.LayoutParams onMeasureInternal = onMeasureInternal(Math.min(size, size2), size2);
        this.mLayoutParams = onMeasureInternal;
        setMeasuredDimension(onMeasureInternal.mMediumWidth, onMeasureInternal.mMediumHeight);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        onLayoutInternal();
    }

    @VisibleForTesting
    void onLayoutInternal() {
        if (this.mIconOriginalY == 0.0f) {
            this.mIconOriginalY = this.mIconHolderView.getY();
            Bundle bundle = this.mSavedState;
            if (bundle == null) {
                updateSize((this.mRequireConfirmation || !supportsSmallDialog()) ? 2 : 1);
                return;
            }
            updateSize(bundle.getInt("size"));
            String string = this.mSavedState.getString("indicator_string");
            if (this.mSavedState.getBoolean("hint_is_temporary")) {
                onHelp(0, string);
            } else if (this.mSavedState.getBoolean("error_is_temporary")) {
                onAuthenticationFailed(0, string);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceCredentialAllowed() {
        return Utils.isDeviceCredentialAllowed(this.mPromptInfo);
    }

    /* access modifiers changed from: package-private */
    public int getSize() {
        return this.mSize;
    }
}
