package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import java.io.FileNotFoundException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class IllustrationPreference extends Preference {
    private final Animatable2.AnimationCallback mAnimationCallback = new Animatable2.AnimationCallback() { // from class: com.android.settingslib.widget.IllustrationPreference.1
        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            ((Animatable) drawable).start();
        }
    };
    private final Animatable2Compat.AnimationCallback mAnimationCallbackCompat = new Animatable2Compat.AnimationCallback() { // from class: com.android.settingslib.widget.IllustrationPreference.2
        @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            ((Animatable) drawable).start();
        }
    };
    private Drawable mImageDrawable;
    private int mImageResId;
    private Uri mImageUri;
    private boolean mIsAutoScale;
    private View mMiddleGroundView;

    public IllustrationPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        ImageView.ScaleType scaleType;
        super.onBindViewHolder(preferenceViewHolder);
        FrameLayout frameLayout = (FrameLayout) preferenceViewHolder.findViewById(R$id.middleground_layout);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) preferenceViewHolder.findViewById(R$id.lottie_view);
        int i = getContext().getResources().getDisplayMetrics().widthPixels;
        int i2 = getContext().getResources().getDisplayMetrics().heightPixels;
        FrameLayout frameLayout2 = (FrameLayout) preferenceViewHolder.findViewById(R$id.illustration_frame);
        ViewGroup.LayoutParams layoutParams = frameLayout2.getLayoutParams();
        if (i >= i2) {
            i = i2;
        }
        layoutParams.width = i;
        frameLayout2.setLayoutParams(layoutParams);
        handleImageWithAnimation(lottieAnimationView);
        boolean z = this.mIsAutoScale;
        if (z) {
            if (z) {
                scaleType = ImageView.ScaleType.CENTER_CROP;
            } else {
                scaleType = ImageView.ScaleType.CENTER_INSIDE;
            }
            lottieAnimationView.setScaleType(scaleType);
        }
        handleMiddleGroundView(frameLayout);
    }

    private void handleMiddleGroundView(ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        View view = this.mMiddleGroundView;
        if (view != null) {
            viewGroup.addView(view);
            viewGroup.setVisibility(0);
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void handleImageWithAnimation(LottieAnimationView lottieAnimationView) {
        if (this.mImageDrawable != null) {
            resetAnimations(lottieAnimationView);
            lottieAnimationView.setImageDrawable(this.mImageDrawable);
            Drawable drawable = lottieAnimationView.getDrawable();
            if (drawable != null) {
                startAnimation(drawable);
            }
        }
        if (this.mImageUri != null) {
            resetAnimations(lottieAnimationView);
            lottieAnimationView.setImageURI(this.mImageUri);
            Drawable drawable2 = lottieAnimationView.getDrawable();
            if (drawable2 != null) {
                startAnimation(drawable2);
            } else {
                startLottieAnimationWith(lottieAnimationView, this.mImageUri);
            }
        }
        if (this.mImageResId > 0) {
            resetAnimations(lottieAnimationView);
            lottieAnimationView.setImageResource(this.mImageResId);
            Drawable drawable3 = lottieAnimationView.getDrawable();
            if (drawable3 != null) {
                startAnimation(drawable3);
            } else {
                startLottieAnimationWith(lottieAnimationView, this.mImageResId);
            }
        }
    }

    private void startAnimation(Drawable drawable) {
        if (drawable instanceof Animatable) {
            if (drawable instanceof Animatable2) {
                ((Animatable2) drawable).registerAnimationCallback(this.mAnimationCallback);
            } else if (drawable instanceof Animatable2Compat) {
                ((Animatable2Compat) drawable).registerAnimationCallback(this.mAnimationCallbackCompat);
            } else if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).setOneShot(false);
            }
            ((Animatable) drawable).start();
        }
    }

    private static void startLottieAnimationWith(LottieAnimationView lottieAnimationView, Uri uri) {
        InputStream inputStreamFromUri = getInputStreamFromUri(lottieAnimationView.getContext(), uri);
        lottieAnimationView.setFailureListener(new LottieListener(uri) { // from class: com.android.settingslib.widget.IllustrationPreference$$ExternalSyntheticLambda1
            public final /* synthetic */ Uri f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.airbnb.lottie.LottieListener
            public final void onResult(Object obj) {
                IllustrationPreference.$r8$lambda$5pjDnmd4zeR88c57sclRwEzzBoI(this.f$0, (Throwable) obj);
            }
        });
        lottieAnimationView.setAnimation(inputStreamFromUri, null);
        lottieAnimationView.setRepeatCount(-1);
        lottieAnimationView.playAnimation();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startLottieAnimationWith$0(Uri uri, Throwable th) {
        Log.w("IllustrationPreference", "Invalid illustration image uri: " + uri, th);
    }

    private static void startLottieAnimationWith(LottieAnimationView lottieAnimationView, int i) {
        lottieAnimationView.setFailureListener(new LottieListener(i) { // from class: com.android.settingslib.widget.IllustrationPreference$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.airbnb.lottie.LottieListener
            public final void onResult(Object obj) {
                IllustrationPreference.$r8$lambda$GUyPcAVcZWRdo9NXlFGH9IYTjnE(this.f$0, (Throwable) obj);
            }
        });
        lottieAnimationView.setAnimation(i);
        lottieAnimationView.setRepeatCount(-1);
        lottieAnimationView.playAnimation();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startLottieAnimationWith$1(int i, Throwable th) {
        Log.w("IllustrationPreference", "Invalid illustration resource id: " + i, th);
    }

    private static void resetAnimations(LottieAnimationView lottieAnimationView) {
        resetAnimation(lottieAnimationView.getDrawable());
        lottieAnimationView.cancelAnimation();
    }

    private static void resetAnimation(Drawable drawable) {
        if (drawable instanceof Animatable) {
            if (drawable instanceof Animatable2) {
                ((Animatable2) drawable).clearAnimationCallbacks();
            } else if (drawable instanceof Animatable2Compat) {
                ((Animatable2Compat) drawable).clearAnimationCallbacks();
            }
            ((Animatable) drawable).stop();
        }
    }

    private static InputStream getInputStreamFromUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.w("IllustrationPreference", "Cannot find content uri: " + uri, e);
            return null;
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayoutResource(R$layout.illustration_preference);
        this.mIsAutoScale = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView, 0, 0);
            this.mImageResId = obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_rawRes, 0);
            obtainStyledAttributes.recycle();
        }
    }
}
