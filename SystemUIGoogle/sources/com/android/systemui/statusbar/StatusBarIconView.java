package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.notification.NotificationIconDozeHelper;
import com.android.systemui.statusbar.notification.NotificationUtils;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class StatusBarIconView extends AnimatedImageView implements StatusIconDisplayable {
    private final int ANIMATION_DURATION_FAST;
    private boolean mAlwaysScaleIcon;
    private int mAnimationStartColor;
    private final boolean mBlocked;
    private int mCachedContrastBackgroundColor;
    private ValueAnimator mColorAnimator;
    private final ValueAnimator.AnimatorUpdateListener mColorUpdater;
    private int mContrastedDrawableColor;
    private int mCurrentSetColor;
    private int mDecorColor;
    private int mDensity;
    private boolean mDismissed;
    private ObjectAnimator mDotAnimator;
    private float mDotAppearAmount;
    private final Paint mDotPaint;
    private float mDotRadius;
    private float mDozeAmount;
    private final NotificationIconDozeHelper mDozer;
    private int mDrawableColor;
    private StatusBarIcon mIcon;
    private float mIconAppearAmount;
    private ObjectAnimator mIconAppearAnimator;
    private int mIconColor;
    private float mIconScale;
    private boolean mIncreasedSize;
    private boolean mIsInShelf;
    private Runnable mLayoutRunnable;
    private float[] mMatrix;
    private ColorMatrixColorFilter mMatrixColorFilter;
    private boolean mNightMode;
    private StatusBarNotification mNotification;
    private Drawable mNumberBackground;
    private Paint mNumberPain;
    private String mNumberText;
    private int mNumberX;
    private int mNumberY;
    private Runnable mOnDismissListener;
    private OnVisibilityChangedListener mOnVisibilityChangedListener;
    private boolean mShowsConversation;
    @ViewDebug.ExportedProperty
    private String mSlot;
    private int mStaticDotRadius;
    private int mStatusBarIconDrawingSize;
    private int mStatusBarIconDrawingSizeIncreased;
    private int mStatusBarIconSize;
    private float mSystemIconDefaultScale;
    private float mSystemIconDesiredHeight;
    private float mSystemIconIntrinsicHeight;
    private int mVisibleState;
    private static final Property<StatusBarIconView, Float> ICON_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>("iconAppearAmount") { // from class: com.android.systemui.statusbar.StatusBarIconView.1
        public void setValue(StatusBarIconView statusBarIconView, float f) {
            statusBarIconView.setIconAppearAmount(f);
        }

        public Float get(StatusBarIconView statusBarIconView) {
            return Float.valueOf(statusBarIconView.getIconAppearAmount());
        }
    };
    private static final Property<StatusBarIconView, Float> DOT_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>("dot_appear_amount") { // from class: com.android.systemui.statusbar.StatusBarIconView.2
        public void setValue(StatusBarIconView statusBarIconView, float f) {
            statusBarIconView.setDotAppearAmount(f);
        }

        public Float get(StatusBarIconView statusBarIconView) {
            return Float.valueOf(statusBarIconView.getDotAppearAmount());
        }
    };

    /* loaded from: classes.dex */
    public interface OnVisibilityChangedListener {
        void onVisibilityChanged(int i);
    }

    @Override // com.android.systemui.statusbar.AnimatedImageView, android.widget.ImageView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setColorInternal(NotificationUtils.interpolateColors(this.mAnimationStartColor, this.mIconColor, valueAnimator.getAnimatedFraction()));
    }

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification) {
        this(context, str, statusBarNotification, false);
    }

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification, boolean z) {
        super(context);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 15.0f / 17.0f;
        this.ANIMATION_DURATION_FAST = 100;
        boolean z2 = true;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new StatusBarIconView$$ExternalSyntheticLambda0(this);
        this.mCachedContrastBackgroundColor = 0;
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = z;
        this.mSlot = str;
        Paint paint = new Paint();
        this.mNumberPain = paint;
        paint.setTextAlign(Paint.Align.CENTER);
        this.mNumberPain.setColor(context.getColor(R$drawable.notification_number_text_color));
        this.mNumberPain.setAntiAlias(true);
        setNotification(statusBarNotification);
        setScaleType(ImageView.ScaleType.CENTER);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNightMode = (context.getResources().getConfiguration().uiMode & 48) != 32 ? false : z2;
        initializeDecorColor();
        reloadDimens();
        maybeUpdateIconScaleDimens();
    }

    public StatusBarIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 15.0f / 17.0f;
        this.ANIMATION_DURATION_FAST = 100;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new StatusBarIconView$$ExternalSyntheticLambda0(this);
        this.mCachedContrastBackgroundColor = 0;
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = false;
        this.mAlwaysScaleIcon = true;
        reloadDimens();
        maybeUpdateIconScaleDimens();
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
    }

    private void maybeUpdateIconScaleDimens() {
        if (this.mNotification != null || this.mAlwaysScaleIcon) {
            updateIconScaleForNotifications();
        } else {
            updateIconScaleForSystemIcons();
        }
    }

    private void updateIconScaleForNotifications() {
        this.mIconScale = ((float) (this.mIncreasedSize ? this.mStatusBarIconDrawingSizeIncreased : this.mStatusBarIconDrawingSize)) / ((float) this.mStatusBarIconSize);
        updatePivot();
    }

    private void updateIconScaleForSystemIcons() {
        float iconHeight = getIconHeight();
        if (iconHeight != 0.0f) {
            this.mIconScale = this.mSystemIconDesiredHeight / iconHeight;
        } else {
            this.mIconScale = this.mSystemIconDefaultScale;
        }
    }

    private float getIconHeight() {
        if (getDrawable() != null) {
            return (float) getDrawable().getIntrinsicHeight();
        }
        return this.mSystemIconIntrinsicHeight;
    }

    public float getIconScaleIncreased() {
        return ((float) this.mStatusBarIconDrawingSizeIncreased) / ((float) this.mStatusBarIconDrawingSize);
    }

    public float getIconScale() {
        return this.mIconScale;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.densityDpi;
        if (i != this.mDensity) {
            this.mDensity = i;
            reloadDimens();
            updateDrawable();
            maybeUpdateIconScaleDimens();
        }
        boolean z = (configuration.uiMode & 48) == 32;
        if (z != this.mNightMode) {
            this.mNightMode = z;
            initializeDecorColor();
        }
    }

    private void reloadDimens() {
        boolean z = this.mDotRadius == ((float) this.mStaticDotRadius);
        Resources resources = getResources();
        this.mStaticDotRadius = resources.getDimensionPixelSize(R$dimen.overflow_dot_radius);
        this.mStatusBarIconSize = resources.getDimensionPixelSize(R$dimen.status_bar_icon_size);
        this.mStatusBarIconDrawingSizeIncreased = resources.getDimensionPixelSize(R$dimen.status_bar_icon_drawing_size_dark);
        this.mStatusBarIconDrawingSize = resources.getDimensionPixelSize(R$dimen.status_bar_icon_drawing_size);
        if (z) {
            this.mDotRadius = (float) this.mStaticDotRadius;
        }
        this.mSystemIconDesiredHeight = resources.getDimension(17105530);
        float dimension = resources.getDimension(17105529);
        this.mSystemIconIntrinsicHeight = dimension;
        this.mSystemIconDefaultScale = this.mSystemIconDesiredHeight / dimension;
    }

    public void setNotification(StatusBarNotification statusBarNotification) {
        this.mNotification = statusBarNotification;
        if (statusBarNotification != null) {
            setContentDescription(statusBarNotification.getNotification());
        }
        maybeUpdateIconScaleDimens();
    }

    public boolean equalIcons(Icon icon, Icon icon2) {
        if (icon == icon2) {
            return true;
        }
        if (icon.getType() != icon2.getType()) {
            return false;
        }
        int type = icon.getType();
        if (type == 2) {
            return icon.getResPackage().equals(icon2.getResPackage()) && icon.getResId() == icon2.getResId();
        }
        if (type == 4 || type == 6) {
            return icon.getUriString().equals(icon2.getUriString());
        }
        return false;
    }

    public boolean set(StatusBarIcon statusBarIcon) {
        StatusBarIcon statusBarIcon2 = this.mIcon;
        int i = 0;
        boolean z = statusBarIcon2 != null && equalIcons(statusBarIcon2.icon, statusBarIcon.icon);
        boolean z2 = z && this.mIcon.iconLevel == statusBarIcon.iconLevel;
        StatusBarIcon statusBarIcon3 = this.mIcon;
        boolean z3 = statusBarIcon3 != null && statusBarIcon3.visible == statusBarIcon.visible;
        boolean z4 = statusBarIcon3 != null && statusBarIcon3.number == statusBarIcon.number;
        this.mIcon = statusBarIcon.clone();
        setContentDescription(statusBarIcon.contentDescription);
        if (!z) {
            if (!updateDrawable(false)) {
                return false;
            }
            setTag(R$id.icon_is_grayscale, null);
            maybeUpdateIconScaleDimens();
        }
        if (!z2) {
            setImageLevel(statusBarIcon.iconLevel);
        }
        if (!z4) {
            if (statusBarIcon.number <= 0 || !getContext().getResources().getBoolean(R$bool.config_statusBarShowNumber)) {
                this.mNumberBackground = null;
                this.mNumberText = null;
            } else {
                if (this.mNumberBackground == null) {
                    this.mNumberBackground = getContext().getResources().getDrawable(R$drawable.ic_notification_overlay);
                }
                placeNumber();
            }
            invalidate();
        }
        if (!z3) {
            if (!statusBarIcon.visible || this.mBlocked) {
                i = 8;
            }
            setVisibility(i);
        }
        return true;
    }

    public void updateDrawable() {
        updateDrawable(true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean updateDrawable(boolean r7) {
        /*
            r6 = this;
            java.lang.String r0 = "StatusBarIconView"
            com.android.internal.statusbar.StatusBarIcon r1 = r6.mIcon
            r2 = 0
            if (r1 != 0) goto L_0x0008
            return r2
        L_0x0008:
            android.graphics.drawable.Drawable r1 = r6.getIcon(r1)     // Catch: OutOfMemoryError -> 0x00b1
            if (r1 != 0) goto L_0x0031
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = "No icon for slot "
            r7.append(r1)
            java.lang.String r1 = r6.mSlot
            r7.append(r1)
            java.lang.String r1 = "; "
            r7.append(r1)
            com.android.internal.statusbar.StatusBarIcon r6 = r6.mIcon
            android.graphics.drawable.Icon r6 = r6.icon
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            android.util.Log.w(r0, r6)
            return r2
        L_0x0031:
            boolean r3 = r1 instanceof android.graphics.drawable.BitmapDrawable
            java.lang.String r4 = "Drawable is too large ("
            if (r3 == 0) goto L_0x0069
            r3 = r1
            android.graphics.drawable.BitmapDrawable r3 = (android.graphics.drawable.BitmapDrawable) r3
            android.graphics.Bitmap r5 = r3.getBitmap()
            if (r5 == 0) goto L_0x0069
            android.graphics.Bitmap r3 = r3.getBitmap()
            int r3 = r3.getByteCount()
            r5 = 104857600(0x6400000, float:3.6111186E-35)
            if (r3 <= r5) goto L_0x0078
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r4)
            r7.append(r3)
            java.lang.String r1 = " bytes) "
            r7.append(r1)
            com.android.internal.statusbar.StatusBarIcon r6 = r6.mIcon
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            android.util.Log.w(r0, r6)
            return r2
        L_0x0069:
            int r3 = r1.getIntrinsicWidth()
            r5 = 5000(0x1388, float:7.006E-42)
            if (r3 > r5) goto L_0x0083
            int r3 = r1.getIntrinsicHeight()
            if (r3 <= r5) goto L_0x0078
            goto L_0x0083
        L_0x0078:
            if (r7 == 0) goto L_0x007e
            r7 = 0
            r6.setImageDrawable(r7)
        L_0x007e:
            r6.setImageDrawable(r1)
            r6 = 1
            return r6
        L_0x0083:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r4)
            int r3 = r1.getIntrinsicWidth()
            r7.append(r3)
            java.lang.String r3 = "x"
            r7.append(r3)
            int r1 = r1.getIntrinsicHeight()
            r7.append(r1)
            java.lang.String r1 = ") "
            r7.append(r1)
            com.android.internal.statusbar.StatusBarIcon r6 = r6.mIcon
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            android.util.Log.w(r0, r6)
            return r2
        L_0x00b1:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = "OOM while inflating "
            r7.append(r1)
            com.android.internal.statusbar.StatusBarIcon r1 = r6.mIcon
            android.graphics.drawable.Icon r1 = r1.icon
            r7.append(r1)
            java.lang.String r1 = " for slot "
            r7.append(r1)
            java.lang.String r6 = r6.mSlot
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            android.util.Log.w(r0, r6)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.StatusBarIconView.updateDrawable(boolean):boolean");
    }

    public Icon getSourceIcon() {
        return this.mIcon.icon;
    }

    Drawable getIcon(StatusBarIcon statusBarIcon) {
        Context context = getContext();
        StatusBarNotification statusBarNotification = this.mNotification;
        if (statusBarNotification != null) {
            context = statusBarNotification.getPackageContext(getContext());
        }
        Context context2 = getContext();
        if (context == null) {
            context = getContext();
        }
        return getIcon(context2, context, statusBarIcon);
    }

    public static Drawable getIcon(Context context, Context context2, StatusBarIcon statusBarIcon) {
        int identifier = statusBarIcon.user.getIdentifier();
        if (identifier == -1) {
            identifier = 0;
        }
        Drawable loadDrawableAsUser = statusBarIcon.icon.loadDrawableAsUser(context2, identifier);
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(R$dimen.status_bar_icon_scale_factor, typedValue, true);
        float f = typedValue.getFloat();
        if (f == 1.0f) {
            return loadDrawableAsUser;
        }
        return new ScalingDrawableWrapper(loadDrawableAsUser, f);
    }

    public StatusBarIcon getStatusBarIcon() {
        return this.mIcon;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        StatusBarNotification statusBarNotification = this.mNotification;
        if (statusBarNotification != null) {
            accessibilityEvent.setParcelableData(statusBarNotification.getNotification());
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mNumberBackground != null) {
            placeNumber();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateDrawable();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        if (this.mIconAppearAmount > 0.0f) {
            canvas.save();
            float f2 = this.mIconScale;
            float f3 = this.mIconAppearAmount;
            canvas.scale(f2 * f3, f2 * f3, (float) (getWidth() / 2), (float) (getHeight() / 2));
            super.onDraw(canvas);
            canvas.restore();
        }
        Drawable drawable = this.mNumberBackground;
        if (drawable != null) {
            drawable.draw(canvas);
            canvas.drawText(this.mNumberText, (float) this.mNumberX, (float) this.mNumberY, this.mNumberPain);
        }
        if (this.mDotAppearAmount != 0.0f) {
            float alpha = ((float) Color.alpha(this.mDecorColor)) / 255.0f;
            float f4 = this.mDotAppearAmount;
            if (f4 <= 1.0f) {
                f = this.mDotRadius * f4;
            } else {
                float f5 = f4 - 1.0f;
                alpha *= 1.0f - f5;
                f = NotificationUtils.interpolate(this.mDotRadius, (float) (getWidth() / 4), f5);
            }
            this.mDotPaint.setAlpha((int) (alpha * 255.0f));
            canvas.drawCircle((float) (this.mStatusBarIconSize / 2), (float) (getHeight() / 2), f, this.mDotPaint);
        }
    }

    protected void debug(int i) {
        super.debug(i);
        Log.d("View", ImageView.debugIndent(i) + "slot=" + this.mSlot);
        Log.d("View", ImageView.debugIndent(i) + "icon=" + this.mIcon);
    }

    void placeNumber() {
        String str;
        if (this.mIcon.number > getContext().getResources().getInteger(17694723)) {
            str = getContext().getResources().getString(17039383);
        } else {
            str = NumberFormat.getIntegerInstance().format((long) this.mIcon.number);
        }
        this.mNumberText = str;
        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect();
        this.mNumberPain.getTextBounds(str, 0, str.length(), rect);
        int i = rect.right - rect.left;
        int i2 = rect.bottom - rect.top;
        this.mNumberBackground.getPadding(rect);
        int i3 = rect.left + i + rect.right;
        if (i3 < this.mNumberBackground.getMinimumWidth()) {
            i3 = this.mNumberBackground.getMinimumWidth();
        }
        int i4 = rect.right;
        this.mNumberX = (width - i4) - (((i3 - i4) - rect.left) / 2);
        int i5 = rect.top + i2 + rect.bottom;
        if (i5 < this.mNumberBackground.getMinimumWidth()) {
            i5 = this.mNumberBackground.getMinimumWidth();
        }
        int i6 = rect.bottom;
        this.mNumberY = (height - i6) - ((((i5 - rect.top) - i2) - i6) / 2);
        this.mNumberBackground.setBounds(width - i3, height - i5, width, height);
    }

    private void setContentDescription(Notification notification) {
        if (notification != null) {
            String contentDescForNotification = contentDescForNotification(((ImageView) this).mContext, notification);
            if (!TextUtils.isEmpty(contentDescForNotification)) {
                setContentDescription(contentDescForNotification);
            }
        }
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        return "StatusBarIconView(slot=" + this.mSlot + " icon=" + this.mIcon + " notification=" + this.mNotification + ")";
    }

    public StatusBarNotification getNotification() {
        return this.mNotification;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public String getSlot() {
        return this.mSlot;
    }

    public static String contentDescForNotification(Context context, Notification notification) {
        CharSequence charSequence;
        CharSequence charSequence2 = "";
        try {
            charSequence = Notification.Builder.recoverBuilder(context, notification).loadHeaderAppName();
        } catch (RuntimeException e) {
            Log.e("StatusBarIconView", "Unable to recover builder", e);
            Parcelable parcelable = notification.extras.getParcelable("android.appInfo");
            charSequence = parcelable instanceof ApplicationInfo ? String.valueOf(((ApplicationInfo) parcelable).loadLabel(context.getPackageManager())) : charSequence2;
        }
        CharSequence charSequence3 = notification.extras.getCharSequence("android.title");
        CharSequence charSequence4 = notification.extras.getCharSequence("android.text");
        CharSequence charSequence5 = notification.tickerText;
        if (TextUtils.equals(charSequence3, charSequence)) {
            charSequence3 = charSequence4;
        }
        if (!TextUtils.isEmpty(charSequence3)) {
            charSequence2 = charSequence3;
        } else if (!TextUtils.isEmpty(charSequence5)) {
            charSequence2 = charSequence5;
        }
        return context.getString(R$string.accessibility_desc_notification_icon, charSequence, charSequence2);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setDecorColor(int i) {
        this.mDecorColor = i;
        updateDecorColor();
    }

    private void initializeDecorColor() {
        if (this.mNotification != null) {
            setDecorColor(getContext().getColor(this.mNightMode ? 17170974 : 17170975));
        }
    }

    private void updateDecorColor() {
        int interpolateColors = NotificationUtils.interpolateColors(this.mDecorColor, -1, this.mDozeAmount);
        if (this.mDotPaint.getColor() != interpolateColors) {
            this.mDotPaint.setColor(interpolateColors);
            if (this.mDotAppearAmount != 0.0f) {
                invalidate();
            }
        }
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setStaticDrawableColor(int i) {
        this.mDrawableColor = i;
        setColorInternal(i);
        updateContrastedStaticColor();
        this.mIconColor = i;
        this.mDozer.setColor(i);
    }

    private void setColorInternal(int i) {
        this.mCurrentSetColor = i;
        updateIconColor();
    }

    private void updateIconColor() {
        if (this.mShowsConversation) {
            setColorFilter((ColorFilter) null);
        } else if (this.mCurrentSetColor != 0) {
            if (this.mMatrixColorFilter == null) {
                this.mMatrix = new float[20];
                this.mMatrixColorFilter = new ColorMatrixColorFilter(this.mMatrix);
            }
            updateTintMatrix(this.mMatrix, NotificationUtils.interpolateColors(this.mCurrentSetColor, -1, this.mDozeAmount), this.mDozeAmount * 0.67f);
            this.mMatrixColorFilter.setColorMatrixArray(this.mMatrix);
            setColorFilter((ColorFilter) null);
            setColorFilter(this.mMatrixColorFilter);
        } else {
            this.mDozer.updateGrayscale(this, this.mDozeAmount);
        }
    }

    private static void updateTintMatrix(float[] fArr, int i, float f) {
        Arrays.fill(fArr, 0.0f);
        fArr[4] = (float) Color.red(i);
        fArr[9] = (float) Color.green(i);
        fArr[14] = (float) Color.blue(i);
        fArr[18] = (((float) Color.alpha(i)) / 255.0f) + f;
    }

    public void setIconColor(int i, boolean z) {
        if (this.mIconColor != i) {
            this.mIconColor = i;
            ValueAnimator valueAnimator = this.mColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            int i2 = this.mCurrentSetColor;
            if (i2 != i) {
                if (!z || i2 == 0) {
                    setColorInternal(i);
                    return;
                }
                this.mAnimationStartColor = i2;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.mColorAnimator = ofFloat;
                ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                this.mColorAnimator.setDuration(100L);
                this.mColorAnimator.addUpdateListener(this.mColorUpdater);
                this.mColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        StatusBarIconView.this.mColorAnimator = null;
                        StatusBarIconView.this.mAnimationStartColor = 0;
                    }
                });
                this.mColorAnimator.start();
            }
        }
    }

    public int getStaticDrawableColor() {
        return this.mDrawableColor;
    }

    /* access modifiers changed from: package-private */
    public int getContrastedStaticDrawableColor(int i) {
        if (this.mCachedContrastBackgroundColor != i) {
            this.mCachedContrastBackgroundColor = i;
            updateContrastedStaticColor();
        }
        return this.mContrastedDrawableColor;
    }

    private void updateContrastedStaticColor() {
        if (Color.alpha(this.mCachedContrastBackgroundColor) != 255) {
            this.mContrastedDrawableColor = this.mDrawableColor;
            return;
        }
        int i = this.mDrawableColor;
        if (!ContrastColorUtil.satisfiesTextContrast(this.mCachedContrastBackgroundColor, i)) {
            float[] fArr = new float[3];
            ColorUtils.colorToHSL(this.mDrawableColor, fArr);
            if (fArr[1] < 0.2f) {
                i = 0;
            }
            i = ContrastColorUtil.resolveContrastColor(((ImageView) this).mContext, i, this.mCachedContrastBackgroundColor, !ContrastColorUtil.isColorLight(this.mCachedContrastBackgroundColor));
        }
        this.mContrastedDrawableColor = i;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setVisibleState(int i) {
        setVisibleState(i, true, null);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setVisibleState(int i, boolean z) {
        setVisibleState(i, z, null);
    }

    public void setVisibleState(int i, boolean z, Runnable runnable) {
        setVisibleState(i, z, runnable, 0);
    }

    public void setVisibleState(int i, boolean z, final Runnable runnable, long j) {
        float f;
        Interpolator interpolator;
        boolean z2;
        boolean z3 = false;
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            ObjectAnimator objectAnimator = this.mIconAppearAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = this.mDotAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.cancel();
            }
            if (z) {
                Interpolator interpolator2 = Interpolators.FAST_OUT_LINEAR_IN;
                if (i == 0) {
                    interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
                    f = 1.0f;
                } else {
                    f = 0.0f;
                    interpolator = interpolator2;
                }
                float iconAppearAmount = getIconAppearAmount();
                long j2 = 100;
                if (f != iconAppearAmount) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ICON_APPEAR_AMOUNT, iconAppearAmount, f);
                    this.mIconAppearAnimator = ofFloat;
                    ofFloat.setInterpolator(interpolator);
                    this.mIconAppearAnimator.setDuration(j == 0 ? 100 : j);
                    this.mIconAppearAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.4
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            StatusBarIconView.this.mIconAppearAnimator = null;
                            StatusBarIconView.this.runRunnable(runnable);
                        }
                    });
                    this.mIconAppearAnimator.start();
                    z2 = true;
                } else {
                    z2 = false;
                }
                float f2 = i == 0 ? 2.0f : 0.0f;
                if (i == 1) {
                    interpolator2 = Interpolators.LINEAR_OUT_SLOW_IN;
                    f2 = 1.0f;
                }
                float dotAppearAmount = getDotAppearAmount();
                if (f2 != dotAppearAmount) {
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, DOT_APPEAR_AMOUNT, dotAppearAmount, f2);
                    this.mDotAnimator = ofFloat2;
                    ofFloat2.setInterpolator(interpolator2);
                    ObjectAnimator objectAnimator3 = this.mDotAnimator;
                    if (j != 0) {
                        j2 = j;
                    }
                    objectAnimator3.setDuration(j2);
                    final boolean z4 = !z2;
                    this.mDotAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.5
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            StatusBarIconView.this.mDotAnimator = null;
                            if (z4) {
                                StatusBarIconView.this.runRunnable(runnable);
                            }
                        }
                    });
                    this.mDotAnimator.start();
                    z3 = true;
                } else {
                    z3 = z2;
                }
            } else {
                setIconAppearAmount(i == 0 ? 1.0f : 0.0f);
                setDotAppearAmount(i == 1 ? 1.0f : i == 0 ? 2.0f : 0.0f);
            }
        }
        if (!z3) {
            runRunnable(runnable);
        }
    }

    /* access modifiers changed from: private */
    public void runRunnable(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setIconAppearAmount(float f) {
        if (this.mIconAppearAmount != f) {
            this.mIconAppearAmount = f;
            invalidate();
        }
    }

    public float getIconAppearAmount() {
        return this.mIconAppearAmount;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public int getVisibleState() {
        return this.mVisibleState;
    }

    public void setDotAppearAmount(float f) {
        if (this.mDotAppearAmount != f) {
            this.mDotAppearAmount = f;
            invalidate();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        OnVisibilityChangedListener onVisibilityChangedListener = this.mOnVisibilityChangedListener;
        if (onVisibilityChangedListener != null) {
            onVisibilityChangedListener.onVisibilityChanged(i);
        }
    }

    public float getDotAppearAmount() {
        return this.mDotAppearAmount;
    }

    public void setDozing(boolean z, boolean z2, long j) {
        this.mDozer.setDozing(new Consumer() { // from class: com.android.systemui.statusbar.StatusBarIconView$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StatusBarIconView.this.lambda$setDozing$1((Float) obj);
            }
        }, z, z2, j, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setDozing$1(Float f) {
        this.mDozeAmount = f.floatValue();
        updateDecorColor();
        updateIconColor();
        updateAllowAnimation();
    }

    private void updateAllowAnimation() {
        float f = this.mDozeAmount;
        if (f == 0.0f || f == 1.0f) {
            setAllowAnimation(f == 0.0f);
        }
    }

    @Override // android.view.View
    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (((float) rect.left) + translationX);
        rect.right = (int) (((float) rect.right) + translationX);
        rect.top = (int) (((float) rect.top) + translationY);
        rect.bottom = (int) (((float) rect.bottom) + translationY);
    }

    public void setIsInShelf(boolean z) {
        this.mIsInShelf = z;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Runnable runnable = this.mLayoutRunnable;
        if (runnable != null) {
            runnable.run();
            this.mLayoutRunnable = null;
        }
        updatePivot();
    }

    private void updatePivot() {
        if (isLayoutRtl()) {
            setPivotX(((this.mIconScale + 1.0f) / 2.0f) * ((float) getWidth()));
        } else {
            setPivotX(((1.0f - this.mIconScale) / 2.0f) * ((float) getWidth()));
        }
        setPivotY((((float) getHeight()) - (this.mIconScale * ((float) getWidth()))) / 2.0f);
    }

    public void executeOnLayout(Runnable runnable) {
        this.mLayoutRunnable = runnable;
    }

    public void setDismissed() {
        this.mDismissed = true;
        Runnable runnable = this.mOnDismissListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setOnDismissListener(Runnable runnable) {
        this.mOnDismissListener = runnable;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        int tint = DarkIconDispatcher.getTint(rect, this, i);
        setImageTintList(ColorStateList.valueOf(tint));
        setDecorColor(tint);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public boolean isIconVisible() {
        StatusBarIcon statusBarIcon = this.mIcon;
        return statusBarIcon != null && statusBarIcon.visible;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public boolean isIconBlocked() {
        return this.mBlocked;
    }

    public void setIncreasedSize(boolean z) {
        this.mIncreasedSize = z;
        maybeUpdateIconScaleDimens();
    }

    public void setShowsConversation(boolean z) {
        if (this.mShowsConversation != z) {
            this.mShowsConversation = z;
            updateIconColor();
        }
    }

    public boolean showsConversation() {
        return this.mShowsConversation;
    }
}
