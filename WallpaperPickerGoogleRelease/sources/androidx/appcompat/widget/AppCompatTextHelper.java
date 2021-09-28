package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class AppCompatTextHelper {
    public boolean mAsyncFontPending;
    public final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    public TintInfo mDrawableBottomTint;
    public TintInfo mDrawableEndTint;
    public TintInfo mDrawableLeftTint;
    public TintInfo mDrawableRightTint;
    public TintInfo mDrawableStartTint;
    public TintInfo mDrawableTopTint;
    public Typeface mFontTypeface;
    public final TextView mView;
    public int mStyle = 0;
    public int mFontWeight = -1;

    public AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    public static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList = appCompatDrawableManager.getTintList(context, i);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }

    public void applyCompoundDrawablesTints() {
        if (!(this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null)) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:189:0x035f, code lost:
        if (r3 != null) goto L_0x0366;
     */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFromAttributes(android.util.AttributeSet r22, int r23) {
        /*
        // Method dump skipped, instructions count: 952
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatTextHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }

    public void onSetTextAppearance(Context context, int i) {
        String string;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, R$styleable.TextAppearance);
        TintTypedArray tintTypedArray = new TintTypedArray(context, obtainStyledAttributes);
        if (tintTypedArray.hasValue(14)) {
            this.mView.setAllCaps(tintTypedArray.getBoolean(14, false));
        }
        if (tintTypedArray.hasValue(0) && tintTypedArray.getDimensionPixelSize(0, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, tintTypedArray);
        if (tintTypedArray.hasValue(13) && (string = tintTypedArray.getString(13)) != null) {
            this.mView.setFontVariationSettings(string);
        }
        obtainStyledAttributes.recycle();
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            this.mView.setTypeface(typeface, this.mStyle);
        }
    }

    public final void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        String string;
        this.mStyle = tintTypedArray.getInt(2, this.mStyle);
        int i = tintTypedArray.getInt(11, -1);
        this.mFontWeight = i;
        boolean z = false;
        if (i != -1) {
            this.mStyle = (this.mStyle & 2) | 0;
        }
        int i2 = 10;
        if (tintTypedArray.hasValue(10) || tintTypedArray.hasValue(12)) {
            this.mFontTypeface = null;
            if (tintTypedArray.hasValue(12)) {
                i2 = 12;
            }
            final int i3 = this.mFontWeight;
            final int i4 = this.mStyle;
            if (!context.isRestricted()) {
                final WeakReference weakReference = new WeakReference(this.mView);
                try {
                    Typeface font = tintTypedArray.getFont(i2, this.mStyle, new ResourcesCompat.FontCallback() { // from class: androidx.appcompat.widget.AppCompatTextHelper.1
                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public void onFontRetrievalFailed(int i5) {
                        }

                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public void onFontRetrieved(Typeface typeface) {
                            int i5 = i3;
                            if (i5 != -1) {
                                typeface = Typeface.create(typeface, i5, (i4 & 2) != 0);
                            }
                            AppCompatTextHelper appCompatTextHelper = AppCompatTextHelper.this;
                            WeakReference weakReference2 = weakReference;
                            if (appCompatTextHelper.mAsyncFontPending) {
                                appCompatTextHelper.mFontTypeface = typeface;
                                TextView textView = (TextView) weakReference2.get();
                                if (textView != null) {
                                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                                    if (textView.isAttachedToWindow()) {
                                        textView.post(new Runnable(appCompatTextHelper, textView, typeface, appCompatTextHelper.mStyle) { // from class: androidx.appcompat.widget.AppCompatTextHelper.2
                                            public final /* synthetic */ int val$style;
                                            public final /* synthetic */ TextView val$textView;
                                            public final /* synthetic */ Typeface val$typeface;

                                            {
                                                this.val$textView = r2;
                                                this.val$typeface = r3;
                                                this.val$style = r4;
                                            }

                                            @Override // java.lang.Runnable
                                            public void run() {
                                                this.val$textView.setTypeface(this.val$typeface, this.val$style);
                                            }
                                        });
                                    } else {
                                        textView.setTypeface(typeface, appCompatTextHelper.mStyle);
                                    }
                                }
                            }
                        }
                    });
                    if (font != null) {
                        if (this.mFontWeight != -1) {
                            this.mFontTypeface = Typeface.create(Typeface.create(font, 0), this.mFontWeight, (this.mStyle & 2) != 0);
                        } else {
                            this.mFontTypeface = font;
                        }
                    }
                    this.mAsyncFontPending = this.mFontTypeface == null;
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface == null && (string = tintTypedArray.getString(i2)) != null) {
                if (this.mFontWeight != -1) {
                    Typeface create = Typeface.create(string, 0);
                    int i5 = this.mFontWeight;
                    if ((this.mStyle & 2) != 0) {
                        z = true;
                    }
                    this.mFontTypeface = Typeface.create(create, i5, z);
                    return;
                }
                this.mFontTypeface = Typeface.create(string, this.mStyle);
            }
        } else if (tintTypedArray.hasValue(1)) {
            this.mAsyncFontPending = false;
            int i6 = tintTypedArray.getInt(1, 1);
            if (i6 == 1) {
                this.mFontTypeface = Typeface.SANS_SERIF;
            } else if (i6 == 2) {
                this.mFontTypeface = Typeface.SERIF;
            } else if (i6 == 3) {
                this.mFontTypeface = Typeface.MONOSPACE;
            }
        }
    }
}
