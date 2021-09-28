package com.android.wm.shell.startingsurface;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.SurfaceControl;
import android.view.View;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.Quantizer;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.R;
import com.android.wm.shell.common.TransactionPool;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public class SplashscreenContentDrawer {
    private int mBrandingImageHeight;
    private int mBrandingImageWidth;
    @VisibleForTesting
    final ColorCache mColorCache;
    private final Context mContext;
    private int mDefaultIconSize;
    private final IconProvider mIconProvider;
    private int mIconSize;
    private int mLastPackageContextConfigHash;
    private int mMainWindowShiftLength;
    private final Handler mSplashscreenWorkerHandler;
    private final SplashScreenWindowAttrs mTmpAttrs = new SplashScreenWindowAttrs();
    private final TransactionPool mTransactionPool;
    private static final String TAG = StartingSurfaceDrawer.TAG;
    private static final boolean DEBUG = StartingSurfaceDrawer.DEBUG_SPLASH_SCREEN;

    /* loaded from: classes2.dex */
    public static class SplashScreenWindowAttrs {
        private int mWindowBgResId = 0;
        private int mWindowBgColor = 0;
        private Drawable mSplashScreenIcon = null;
        private Drawable mBrandingImage = null;
        private int mIconBgColor = 0;
        private int mAnimationDuration = 0;
    }

    /* access modifiers changed from: package-private */
    public SplashscreenContentDrawer(Context context, TransactionPool transactionPool) {
        this.mContext = context;
        this.mIconProvider = new IconProvider(context);
        this.mTransactionPool = transactionPool;
        HandlerThread handlerThread = new HandlerThread("wmshell.splashworker", -10);
        handlerThread.start();
        Handler threadHandler = handlerThread.getThreadHandler();
        this.mSplashscreenWorkerHandler = threadHandler;
        this.mColorCache = new ColorCache(context, threadHandler);
    }

    /* access modifiers changed from: package-private */
    public void createContentView(Context context, @StartingWindowInfo.StartingWindowType int i, ActivityInfo activityInfo, int i2, Consumer<SplashScreenView> consumer) {
        this.mSplashscreenWorkerHandler.post(new Runnable(context, activityInfo, i, i2, consumer) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda0
            public final /* synthetic */ Context f$1;
            public final /* synthetic */ ActivityInfo f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ int f$4;
            public final /* synthetic */ Consumer f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SplashscreenContentDrawer.this.lambda$createContentView$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createContentView$0(Context context, ActivityInfo activityInfo, int i, int i2, Consumer consumer) {
        SplashScreenView splashScreenView;
        try {
            Trace.traceBegin(32, "makeSplashScreenContentView");
            splashScreenView = makeSplashScreenContentView(context, activityInfo, i);
            Trace.traceEnd(32);
        } catch (RuntimeException e) {
            String str = TAG;
            Slog.w(str, "failed creating starting window content at taskId: " + i2, e);
            splashScreenView = null;
        }
        consumer.accept(splashScreenView);
    }

    private void updateDensity() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(17105522);
        this.mDefaultIconSize = this.mContext.getResources().getDimensionPixelSize(17105521);
        this.mBrandingImageWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_brand_image_width);
        this.mBrandingImageHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_brand_image_height);
        this.mMainWindowShiftLength = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_exit_animation_window_shift_length);
    }

    public static int getSystemBGColor() {
        Application currentApplication = ActivityThread.currentApplication();
        if (currentApplication != null) {
            return currentApplication.getResources().getColor(R.color.splash_window_background_default);
        }
        Slog.e(TAG, "System context does not exist!");
        return -16777216;
    }

    /* access modifiers changed from: package-private */
    public int estimateTaskBackgroundColor(Context context) {
        SplashScreenWindowAttrs splashScreenWindowAttrs = new SplashScreenWindowAttrs();
        getWindowAttrs(context, splashScreenWindowAttrs);
        return peekWindowBGColor(context, splashScreenWindowAttrs);
    }

    /* access modifiers changed from: private */
    public static Drawable createDefaultBackgroundDrawable() {
        return new ColorDrawable(getSystemBGColor());
    }

    private static int peekWindowBGColor(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        Drawable drawable;
        Trace.traceBegin(32, "peekWindowBGColor");
        if (splashScreenWindowAttrs.mWindowBgColor != 0) {
            drawable = new ColorDrawable(splashScreenWindowAttrs.mWindowBgColor);
        } else if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            drawable = context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        } else {
            drawable = createDefaultBackgroundDrawable();
            String str = TAG;
            Slog.w(str, "Window background does not exist, using " + drawable);
        }
        int estimateWindowBGColor = estimateWindowBGColor(drawable);
        Trace.traceEnd(32);
        return estimateWindowBGColor;
    }

    /* access modifiers changed from: private */
    public static int estimateWindowBGColor(Drawable drawable) {
        DrawableColorTester drawableColorTester = new DrawableColorTester(drawable, 1);
        if (drawableColorTester.passFilterRatio() != 0.0f) {
            return drawableColorTester.getDominateColor();
        }
        Slog.w(TAG, "Window background is transparent, fill background with black color");
        return getSystemBGColor();
    }

    private static Drawable peekLegacySplashscreenContent(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(com.android.internal.R.styleable.Window);
        int intValue = ((Integer) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda8
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SplashscreenContentDrawer.lambda$peekLegacySplashscreenContent$1(this.f$0, (Integer) obj);
            }
        }, 0)).intValue();
        obtainStyledAttributes.recycle();
        if (intValue != 0) {
            return context.getDrawable(intValue);
        }
        if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            return context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$peekLegacySplashscreenContent$1(TypedArray typedArray, Integer num) {
        return Integer.valueOf(typedArray.getResourceId(47, num.intValue()));
    }

    private SplashScreenView makeSplashScreenContentView(Context context, ActivityInfo activityInfo, @StartingWindowInfo.StartingWindowType int i) {
        int i2;
        updateDensity();
        getWindowAttrs(context, this.mTmpAttrs);
        this.mLastPackageContextConfigHash = context.getResources().getConfiguration().hashCode();
        Drawable peekLegacySplashscreenContent = i == 4 ? peekLegacySplashscreenContent(context, this.mTmpAttrs) : null;
        if (peekLegacySplashscreenContent != null) {
            i2 = getBGColorFromCache(activityInfo, new IntSupplier(peekLegacySplashscreenContent) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda1
                public final /* synthetic */ Drawable f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return SplashscreenContentDrawer.estimateWindowBGColor(this.f$0);
                }
            });
        } else {
            i2 = getBGColorFromCache(activityInfo, new IntSupplier(context) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda2
                public final /* synthetic */ Context f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.util.function.IntSupplier
                public final int getAsInt() {
                    return SplashscreenContentDrawer.this.lambda$makeSplashScreenContentView$3(this.f$1);
                }
            });
        }
        return new StartingWindowViewBuilder(context, activityInfo).setWindowBGColor(i2).overlayDrawable(peekLegacySplashscreenContent).chooseStyle(i).build();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$makeSplashScreenContentView$3(Context context) {
        return peekWindowBGColor(context, this.mTmpAttrs);
    }

    private int getBGColorFromCache(ActivityInfo activityInfo, IntSupplier intSupplier) {
        return this.mColorCache.getWindowColor(activityInfo.packageName, this.mLastPackageContextConfigHash, this.mTmpAttrs.mWindowBgColor, this.mTmpAttrs.mWindowBgResId, intSupplier).mBgColor;
    }

    private static <T> T safeReturnAttrDefault(UnaryOperator<T> unaryOperator, T t) {
        try {
            return (T) unaryOperator.apply(t);
        } catch (RuntimeException e) {
            String str = TAG;
            Slog.w(str, "Get attribute fail, return default: " + e.getMessage());
            return t;
        }
    }

    private static void getWindowAttrs(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(com.android.internal.R.styleable.Window);
        splashScreenWindowAttrs.mWindowBgResId = obtainStyledAttributes.getResourceId(1, 0);
        splashScreenWindowAttrs.mWindowBgColor = ((Integer) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda6
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SplashscreenContentDrawer.lambda$getWindowAttrs$4(this.f$0, (Integer) obj);
            }
        }, 0)).intValue();
        splashScreenWindowAttrs.mSplashScreenIcon = (Drawable) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda3
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Drawable drawable = (Drawable) obj;
                return this.f$0.getDrawable(57);
            }
        }, null);
        splashScreenWindowAttrs.mAnimationDuration = ((Integer) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda7
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SplashscreenContentDrawer.lambda$getWindowAttrs$6(this.f$0, (Integer) obj);
            }
        }, 0)).intValue();
        splashScreenWindowAttrs.mBrandingImage = (Drawable) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda4
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Drawable drawable = (Drawable) obj;
                return this.f$0.getDrawable(59);
            }
        }, null);
        splashScreenWindowAttrs.mIconBgColor = ((Integer) safeReturnAttrDefault(new UnaryOperator(obtainStyledAttributes) { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda5
            public final /* synthetic */ TypedArray f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SplashscreenContentDrawer.lambda$getWindowAttrs$8(this.f$0, (Integer) obj);
            }
        }, 0)).intValue();
        obtainStyledAttributes.recycle();
        if (DEBUG) {
            String str = TAG;
            Slog.d(str, "window attributes color: " + Integer.toHexString(splashScreenWindowAttrs.mWindowBgColor) + " icon " + splashScreenWindowAttrs.mSplashScreenIcon + " duration " + splashScreenWindowAttrs.mAnimationDuration + " brandImage " + splashScreenWindowAttrs.mBrandingImage);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$getWindowAttrs$4(TypedArray typedArray, Integer num) {
        return Integer.valueOf(typedArray.getColor(56, num.intValue()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$getWindowAttrs$6(TypedArray typedArray, Integer num) {
        return Integer.valueOf(typedArray.getInt(58, num.intValue()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$getWindowAttrs$8(TypedArray typedArray, Integer num) {
        return Integer.valueOf(typedArray.getColor(60, num.intValue()));
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class StartingWindowViewBuilder {
        private final ActivityInfo mActivityInfo;
        private final Context mContext;
        private Drawable[] mFinalIconDrawables;
        private int mFinalIconSize;
        private Drawable mOverlayDrawable;
        private int mSuggestType;
        private int mThemeColor;

        StartingWindowViewBuilder(Context context, ActivityInfo activityInfo) {
            this.mFinalIconSize = SplashscreenContentDrawer.this.mIconSize;
            this.mContext = context;
            this.mActivityInfo = activityInfo;
        }

        StartingWindowViewBuilder setWindowBGColor(int i) {
            this.mThemeColor = i;
            return this;
        }

        StartingWindowViewBuilder overlayDrawable(Drawable drawable) {
            this.mOverlayDrawable = drawable;
            return this;
        }

        StartingWindowViewBuilder chooseStyle(int i) {
            this.mSuggestType = i;
            return this;
        }

        SplashScreenView build() {
            int i = this.mSuggestType;
            int i2 = 0;
            if (i == 3 || i == 4) {
                this.mFinalIconSize = 0;
            } else if (SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon != null) {
                Drawable drawable = SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon;
                int i3 = SplashscreenContentDrawer.this.mTmpAttrs.mAnimationDuration;
                if (SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == 0 || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == this.mThemeColor) {
                    this.mFinalIconSize = (int) (((float) this.mFinalIconSize) * 1.2f);
                }
                createIconDrawable(drawable, false);
                i2 = i3;
            } else {
                int i4 = (int) (((((float) SplashscreenContentDrawer.this.mIconSize) / ((float) SplashscreenContentDrawer.this.mDefaultIconSize)) * ((float) this.mContext.getResources().getDisplayMetrics().densityDpi) * 1.2f) + 0.5f);
                Trace.traceBegin(32, "getIcon");
                Drawable icon = SplashscreenContentDrawer.this.mIconProvider.getIcon(this.mActivityInfo, i4);
                Trace.traceEnd(32);
                if (icon == null) {
                    icon = this.mContext.getPackageManager().getDefaultActivityIcon();
                }
                if (!processAdaptiveIcon(icon)) {
                    if (SplashscreenContentDrawer.DEBUG) {
                        Slog.d(SplashscreenContentDrawer.TAG, "The icon is not an AdaptiveIconDrawable");
                    }
                    Trace.traceBegin(32, "legacy_icon_factory");
                    Bitmap createScaledBitmapWithoutShadow = new ShapeIconFactory(SplashscreenContentDrawer.this.mContext, i4, this.mFinalIconSize).createScaledBitmapWithoutShadow(icon, true);
                    Trace.traceEnd(32);
                    createIconDrawable(new BitmapDrawable(createScaledBitmapWithoutShadow), true);
                }
            }
            return fillViewWithIcon(this.mFinalIconSize, this.mFinalIconDrawables, i2);
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class ShapeIconFactory extends BaseIconFactory {
            protected ShapeIconFactory(Context context, int i, int i2) {
                super(context, i, i2, true);
            }
        }

        private void createIconDrawable(Drawable drawable, boolean z) {
            if (z) {
                this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeLegacyIconDrawable(drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
            } else {
                this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeIconDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor, this.mThemeColor, drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
            }
        }

        private boolean processAdaptiveIcon(Drawable drawable) {
            if (!(drawable instanceof AdaptiveIconDrawable)) {
                return false;
            }
            Trace.traceBegin(32, "processAdaptiveIcon");
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable;
            Drawable foreground = adaptiveIconDrawable.getForeground();
            ColorCache colorCache = SplashscreenContentDrawer.this.mColorCache;
            ActivityInfo activityInfo = this.mActivityInfo;
            ColorCache.IconColor iconColor = colorCache.getIconColor(activityInfo.packageName, activityInfo.getIconResource(), SplashscreenContentDrawer.this.mLastPackageContextConfigHash, new SplashscreenContentDrawer$StartingWindowViewBuilder$$ExternalSyntheticLambda1(foreground), new SplashscreenContentDrawer$StartingWindowViewBuilder$$ExternalSyntheticLambda0(adaptiveIconDrawable));
            if (SplashscreenContentDrawer.DEBUG) {
                String str = SplashscreenContentDrawer.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("FgMainColor=");
                sb.append(Integer.toHexString(iconColor.mFgColor));
                sb.append(" BgMainColor=");
                sb.append(Integer.toHexString(iconColor.mBgColor));
                sb.append(" IsBgComplex=");
                sb.append(iconColor.mIsBgComplex);
                sb.append(" FromCache=");
                sb.append(iconColor.mReuseCount > 0);
                sb.append(" ThemeColor=");
                sb.append(Integer.toHexString(this.mThemeColor));
                Slog.d(str, sb.toString());
            }
            if (iconColor.mIsBgComplex || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor != 0 || (!SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mBgColor) && (!iconColor.mIsBgGrayscale || SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mFgColor)))) {
                if (SplashscreenContentDrawer.DEBUG) {
                    Slog.d(SplashscreenContentDrawer.TAG, "makeSplashScreenContentView: draw whole icon");
                }
                createIconDrawable(drawable, false);
            } else {
                if (SplashscreenContentDrawer.DEBUG) {
                    Slog.d(SplashscreenContentDrawer.TAG, "makeSplashScreenContentView: choose fg icon");
                }
                this.mFinalIconSize = (int) ((((float) SplashscreenContentDrawer.this.mIconSize) * (iconColor.mFgNonTranslucentRatio < 0.44444445f ? 1.2f : 1.0f)) + 0.5f);
                createIconDrawable(foreground, false);
            }
            Trace.traceEnd(32);
            return true;
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$0(Drawable drawable) {
            return new DrawableColorTester(drawable, 2);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$1(AdaptiveIconDrawable adaptiveIconDrawable) {
            return new DrawableColorTester(adaptiveIconDrawable.getBackground());
        }

        private SplashScreenView fillViewWithIcon(int i, Drawable[] drawableArr, int i2) {
            Drawable drawable;
            Drawable drawable2 = null;
            if (drawableArr != null) {
                drawable = drawableArr.length > 0 ? drawableArr[0] : null;
                if (drawableArr.length > 1) {
                    drawable2 = drawableArr[1];
                }
            } else {
                drawable = null;
            }
            Trace.traceBegin(32, "fillViewWithIcon");
            SplashScreenView.Builder animationDurationMillis = new SplashScreenView.Builder(this.mContext).setBackgroundColor(this.mThemeColor).setOverlayDrawable(this.mOverlayDrawable).setIconSize(i).setIconBackground(drawable2).setCenterViewDrawable(drawable).setAnimationDurationMillis(i2);
            if (this.mSuggestType == 1 && SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage != null) {
                animationDurationMillis.setBrandingDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage, SplashscreenContentDrawer.this.mBrandingImageWidth, SplashscreenContentDrawer.this.mBrandingImageHeight);
            }
            final SplashScreenView build = animationDurationMillis.build();
            if (SplashscreenContentDrawer.DEBUG) {
                Slog.d(SplashscreenContentDrawer.TAG, "fillViewWithIcon surfaceWindowView " + build);
            }
            if (this.mSuggestType != 4) {
                build.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer.StartingWindowViewBuilder.1
                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewDetachedFromWindow(View view) {
                    }

                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewAttachedToWindow(View view) {
                        SplashScreenView.applySystemBarsContrastColor(view.getWindowInsetsController(), build.getInitBackgroundColor());
                    }
                });
            }
            Trace.traceEnd(32);
            return build;
        }
    }

    /* access modifiers changed from: private */
    public static boolean isRgbSimilarInHsv(int i, int i2) {
        if (i == i2) {
            return true;
        }
        float luminance = Color.luminance(i);
        float luminance2 = Color.luminance(i2);
        float f = luminance > luminance2 ? (luminance + 0.05f) / (luminance2 + 0.05f) : (luminance2 + 0.05f) / (luminance + 0.05f);
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "isRgbSimilarInHsv a: " + Integer.toHexString(i) + " b " + Integer.toHexString(i2) + " contrast ratio: " + f);
        }
        if (f < 2.0f) {
            return true;
        }
        float[] fArr = new float[3];
        float[] fArr2 = new float[3];
        Color.colorToHSV(i, fArr);
        Color.colorToHSV(i2, fArr2);
        int abs = ((((int) Math.abs(fArr[0] - fArr2[0])) + 180) % 360) - 180;
        double pow = Math.pow((double) (((float) abs) / 180.0f), 2.0d);
        double pow2 = Math.pow((double) (fArr[1] - fArr2[1]), 2.0d);
        double pow3 = Math.pow((double) (fArr[2] - fArr2[2]), 2.0d);
        double sqrt = Math.sqrt(((pow + pow2) + pow3) / 3.0d);
        if (z) {
            Slog.d(TAG, "hsvDiff " + abs + " ah " + fArr[0] + " bh " + fArr2[0] + " as " + fArr[1] + " bs " + fArr2[1] + " av " + fArr[2] + " bv " + fArr2[2] + " sqH " + pow + " sqS " + pow2 + " sqV " + pow3 + " root " + sqrt);
        }
        if (sqrt < 0.1d) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class DrawableColorTester {
        private final ColorTester mColorChecker;

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public interface ColorTester {
            int getDominantColor();

            boolean isComplexColor();

            boolean isGrayscale();

            float passFilterRatio();
        }

        DrawableColorTester(Drawable drawable) {
            this(drawable, 0);
        }

        DrawableColorTester(Drawable drawable, int i) {
            ColorTester colorTester;
            if (drawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                if (layerDrawable.getNumberOfLayers() > 0) {
                    if (SplashscreenContentDrawer.DEBUG) {
                        Slog.d(SplashscreenContentDrawer.TAG, "replace drawable with bottom layer drawable");
                    }
                    drawable = layerDrawable.getDrawable(0);
                }
            }
            if (drawable == null) {
                this.mColorChecker = new SingleColorTester((ColorDrawable) SplashscreenContentDrawer.createDefaultBackgroundDrawable());
                return;
            }
            if (drawable instanceof ColorDrawable) {
                colorTester = new SingleColorTester((ColorDrawable) drawable);
            } else {
                colorTester = new ComplexDrawableTester(drawable, i);
            }
            this.mColorChecker = colorTester;
        }

        public float passFilterRatio() {
            return this.mColorChecker.passFilterRatio();
        }

        public boolean isComplexColor() {
            return this.mColorChecker.isComplexColor();
        }

        public int getDominateColor() {
            return this.mColorChecker.getDominantColor();
        }

        public boolean isGrayscale() {
            return this.mColorChecker.isGrayscale();
        }

        /* access modifiers changed from: private */
        public static boolean isGrayscaleColor(int i) {
            int red = Color.red(i);
            int green = Color.green(i);
            return red == green && green == Color.blue(i);
        }

        /* loaded from: classes2.dex */
        private static class SingleColorTester implements ColorTester {
            private final ColorDrawable mColorDrawable;

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public boolean isComplexColor() {
                return false;
            }

            SingleColorTester(ColorDrawable colorDrawable) {
                this.mColorDrawable = colorDrawable;
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public float passFilterRatio() {
                return (float) (this.mColorDrawable.getAlpha() / 255);
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public int getDominantColor() {
                return this.mColorDrawable.getColor();
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public boolean isGrayscale() {
                return DrawableColorTester.isGrayscaleColor(this.mColorDrawable.getColor());
            }
        }

        /* loaded from: classes2.dex */
        private static class ComplexDrawableTester implements ColorTester {
            private static final AlphaFilterQuantizer ALPHA_FILTER_QUANTIZER = new AlphaFilterQuantizer();
            private final boolean mFilterTransparent;
            private final Palette mPalette;

            ComplexDrawableTester(Drawable drawable, int i) {
                int i2;
                Palette.Builder builder;
                Trace.traceBegin(32, "ComplexDrawableTester");
                Rect copyBounds = drawable.copyBounds();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int i3 = 40;
                if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                    i2 = 40;
                } else {
                    int min = Math.min(intrinsicWidth, 40);
                    i2 = Math.min(intrinsicHeight, 40);
                    i3 = min;
                }
                Bitmap createBitmap = Bitmap.createBitmap(i3, i2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                boolean z = false;
                drawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                drawable.draw(canvas);
                drawable.setBounds(copyBounds);
                z = i != 0 ? true : z;
                this.mFilterTransparent = z;
                if (z) {
                    AlphaFilterQuantizer alphaFilterQuantizer = ALPHA_FILTER_QUANTIZER;
                    alphaFilterQuantizer.setFilter(i);
                    builder = new Palette.Builder(createBitmap, alphaFilterQuantizer).maximumColorCount(5);
                } else {
                    builder = new Palette.Builder(createBitmap, (Quantizer) null).maximumColorCount(5);
                }
                this.mPalette = builder.generate();
                createBitmap.recycle();
                Trace.traceEnd(32);
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public float passFilterRatio() {
                if (this.mFilterTransparent) {
                    return ALPHA_FILTER_QUANTIZER.mPassFilterRatio;
                }
                return 1.0f;
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public boolean isComplexColor() {
                return this.mPalette.getSwatches().size() > 1;
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public int getDominantColor() {
                Palette.Swatch dominantSwatch = this.mPalette.getDominantSwatch();
                if (dominantSwatch != null) {
                    return dominantSwatch.getInt();
                }
                return -16777216;
            }

            @Override // com.android.wm.shell.startingsurface.SplashscreenContentDrawer.DrawableColorTester.ColorTester
            public boolean isGrayscale() {
                List swatches = this.mPalette.getSwatches();
                if (swatches != null) {
                    for (int size = swatches.size() - 1; size >= 0; size--) {
                        if (!DrawableColorTester.isGrayscaleColor(((Palette.Swatch) swatches.get(size)).getInt())) {
                            return false;
                        }
                    }
                }
                return true;
            }

            /* access modifiers changed from: private */
            /* loaded from: classes2.dex */
            public static class AlphaFilterQuantizer implements Quantizer {
                private IntPredicate mFilter;
                private final Quantizer mInnerQuantizer;
                private float mPassFilterRatio;
                private final IntPredicate mTranslucentFilter;
                private final IntPredicate mTransparentFilter;

                /* access modifiers changed from: private */
                public static /* synthetic */ boolean lambda$new$0(int i) {
                    return (i & -16777216) != 0;
                }

                /* access modifiers changed from: private */
                public static /* synthetic */ boolean lambda$new$1(int i) {
                    return (i & -16777216) == -16777216;
                }

                private AlphaFilterQuantizer() {
                    this.mInnerQuantizer = new VariationalKMeansQuantizer();
                    SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 = SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0.INSTANCE;
                    this.mTransparentFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                    this.mTranslucentFilter = SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda1.INSTANCE;
                    this.mFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                }

                void setFilter(int i) {
                    if (i != 2) {
                        this.mFilter = this.mTransparentFilter;
                    } else {
                        this.mFilter = this.mTranslucentFilter;
                    }
                }

                public void quantize(int[] iArr, int i) {
                    this.mPassFilterRatio = 0.0f;
                    int i2 = 0;
                    int i3 = 0;
                    for (int length = iArr.length - 1; length > 0; length--) {
                        if (this.mFilter.test(iArr[length])) {
                            i3++;
                        }
                    }
                    if (i3 == 0) {
                        if (SplashscreenContentDrawer.DEBUG) {
                            Slog.d(SplashscreenContentDrawer.TAG, "quantize: this is pure transparent image");
                        }
                        this.mInnerQuantizer.quantize(iArr, i);
                        return;
                    }
                    this.mPassFilterRatio = ((float) i3) / ((float) iArr.length);
                    int[] iArr2 = new int[i3];
                    for (int length2 = iArr.length - 1; length2 > 0; length2--) {
                        if (this.mFilter.test(iArr[length2])) {
                            iArr2[i2] = iArr[length2];
                            i2++;
                        }
                    }
                    this.mInnerQuantizer.quantize(iArr2, i);
                }

                public List<Palette.Swatch> getQuantizedColors() {
                    return this.mInnerQuantizer.getQuantizedColors();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class ColorCache extends BroadcastReceiver {
        private final ArrayMap<String, Colors> mColorMap = new ArrayMap<>();

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Colors {
            final IconColor[] mIconColors;
            final WindowColor[] mWindowColors;

            private Colors() {
                this.mWindowColors = new WindowColor[2];
                this.mIconColors = new IconColor[2];
            }
        }

        /* access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Cache {
            final int mHash;
            int mReuseCount;

            Cache(int i) {
                this.mHash = i;
            }
        }

        /* access modifiers changed from: package-private */
        /* loaded from: classes2.dex */
        public static class WindowColor extends Cache {
            final int mBgColor;

            WindowColor(int i, int i2) {
                super(i);
                this.mBgColor = i2;
            }
        }

        /* access modifiers changed from: package-private */
        /* loaded from: classes2.dex */
        public static class IconColor extends Cache {
            final int mBgColor;
            final int mFgColor;
            final float mFgNonTranslucentRatio;
            final boolean mIsBgComplex;
            final boolean mIsBgGrayscale;

            IconColor(int i, int i2, int i3, boolean z, boolean z2, float f) {
                super(i);
                this.mFgColor = i2;
                this.mBgColor = i3;
                this.mIsBgComplex = z;
                this.mIsBgGrayscale = z2;
                this.mFgNonTranslucentRatio = f;
            }
        }

        ColorCache(Context context, Handler handler) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, null, handler);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                this.mColorMap.remove(data.getEncodedSchemeSpecificPart());
            }
        }

        private static <T extends Cache> T getCache(T[] tArr, int i, int[] iArr) {
            int i2 = Integer.MAX_VALUE;
            for (int i3 = 0; i3 < 2; i3++) {
                T t = tArr[i3];
                if (t == null) {
                    i2 = -1;
                    iArr[0] = i3;
                } else if (t.mHash == i) {
                    t.mReuseCount++;
                    return t;
                } else {
                    int i4 = t.mReuseCount;
                    if (i4 < i2) {
                        iArr[0] = i3;
                        i2 = i4;
                    }
                }
            }
            return null;
        }

        WindowColor getWindowColor(String str, int i, int i2, int i3, IntSupplier intSupplier) {
            Colors colors = this.mColorMap.get(str);
            int i4 = (((i * 31) + i2) * 31) + i3;
            int[] iArr = {0};
            if (colors != null) {
                WindowColor windowColor = (WindowColor) getCache(colors.mWindowColors, i4, iArr);
                if (windowColor != null) {
                    return windowColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            WindowColor windowColor2 = new WindowColor(i4, intSupplier.getAsInt());
            colors.mWindowColors[iArr[0]] = windowColor2;
            return windowColor2;
        }

        IconColor getIconColor(String str, int i, int i2, Supplier<DrawableColorTester> supplier, Supplier<DrawableColorTester> supplier2) {
            Colors colors = this.mColorMap.get(str);
            int i3 = (i * 31) + i2;
            int[] iArr = {0};
            if (colors != null) {
                IconColor iconColor = (IconColor) getCache(colors.mIconColors, i3, iArr);
                if (iconColor != null) {
                    return iconColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            DrawableColorTester drawableColorTester = supplier.get();
            DrawableColorTester drawableColorTester2 = supplier2.get();
            IconColor iconColor2 = new IconColor(i3, drawableColorTester.getDominateColor(), drawableColorTester2.getDominateColor(), drawableColorTester2.isComplexColor(), drawableColorTester2.isGrayscale(), drawableColorTester.passFilterRatio());
            colors.mIconColors[iArr[0]] = iconColor2;
            return iconColor2;
        }
    }

    /* access modifiers changed from: package-private */
    public void applyExitAnimation(SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable) {
        new SplashScreenExitAnimation(this.mContext, splashScreenView, surfaceControl, rect, this.mMainWindowShiftLength, this.mTransactionPool, runnable).startAnimations();
    }
}
