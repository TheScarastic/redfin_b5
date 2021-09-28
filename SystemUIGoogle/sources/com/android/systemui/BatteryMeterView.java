package com.android.systemui;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.SysuiLifecycle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class BatteryMeterView extends LinearLayout implements BatteryController.BatteryStateChangeCallback, TunerService.Tunable, DarkIconDispatcher.DarkReceiver, ConfigurationController.ConfigurationListener {
    private BatteryController mBatteryController;
    private final ImageView mBatteryIconView;
    private TextView mBatteryPercentView;
    private boolean mBatteryStateUnknown;
    private boolean mCharging;
    private final ThemedBatteryDrawable mDrawable;
    private DualToneHandler mDualToneHandler;
    private boolean mIgnoreTunerUpdates;
    private boolean mIsSubscribedForTunerUpdates;
    private int mLevel;
    private int mNonAdaptedBackgroundColor;
    private int mNonAdaptedForegroundColor;
    private int mNonAdaptedSingleToneColor;
    private final int mPercentageStyleId;
    private SettingObserver mSettingObserver;
    private boolean mShowPercentAvailable;
    private int mShowPercentMode;
    private final String mSlotBattery;
    private int mTextColor;
    private Drawable mUnknownStateDrawable;
    private int mUser;
    private final CurrentUserTracker mUserTracker;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowPercentMode = 0;
        setOrientation(0);
        setGravity(8388627);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BatteryMeterView, i, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.BatteryMeterView_frameColor, context.getColor(R$color.meter_background_color));
        this.mPercentageStyleId = obtainStyledAttributes.getResourceId(R$styleable.BatteryMeterView_textAppearance, 0);
        ThemedBatteryDrawable themedBatteryDrawable = new ThemedBatteryDrawable(context, color);
        this.mDrawable = themedBatteryDrawable;
        obtainStyledAttributes.recycle();
        this.mSettingObserver = new SettingObserver(new Handler(context.getMainLooper()));
        this.mShowPercentAvailable = context.getResources().getBoolean(17891381);
        setupLayoutTransition();
        this.mSlotBattery = context.getString(17041428);
        ImageView imageView = new ImageView(context);
        this.mBatteryIconView = imageView;
        imageView.setImageDrawable(themedBatteryDrawable);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_width), getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_height));
        marginLayoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R$dimen.battery_margin_bottom));
        addView(imageView, marginLayoutParams);
        updateShowPercent();
        this.mDualToneHandler = new DualToneHandler(context);
        onDarkChanged(new Rect(), 0.0f, -1);
        this.mUserTracker = new CurrentUserTracker((BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class)) { // from class: com.android.systemui.BatteryMeterView.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i2) {
                BatteryMeterView.this.mUser = i2;
                BatteryMeterView.this.getContext().getContentResolver().unregisterContentObserver(BatteryMeterView.this.mSettingObserver);
                BatteryMeterView.this.getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, BatteryMeterView.this.mSettingObserver, i2);
                BatteryMeterView.this.updateShowPercent();
            }
        };
        setClipChildren(false);
        setClipToPadding(false);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).observe(SysuiLifecycle.viewAttachLifecycle(this), (LifecycleOwner) this);
    }

    private void setupLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200);
        layoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, "alpha", 0.0f, 1.0f));
        layoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", 1.0f, 0.0f);
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimator(3, ofFloat);
        setLayoutTransition(layoutTransition);
    }

    public void setForceShowPercent(boolean z) {
        setPercentShowMode(z ? 1 : 0);
    }

    public void setPercentShowMode(int i) {
        if (i != this.mShowPercentMode) {
            this.mShowPercentMode = i;
            updateShowPercent();
        }
    }

    public void setIgnoreTunerUpdates(boolean z) {
        this.mIgnoreTunerUpdates = z;
        updateTunerSubscription();
    }

    private void updateTunerSubscription() {
        if (this.mIgnoreTunerUpdates) {
            unsubscribeFromTunerUpdates();
        } else {
            subscribeForTunerUpdates();
        }
    }

    private void subscribeForTunerUpdates() {
        if (!this.mIsSubscribedForTunerUpdates && !this.mIgnoreTunerUpdates) {
            ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist");
            this.mIsSubscribedForTunerUpdates = true;
        }
    }

    private void unsubscribeFromTunerUpdates() {
        if (this.mIsSubscribedForTunerUpdates) {
            ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
            this.mIsSubscribedForTunerUpdates = false;
        }
    }

    public void setColorsFromContext(Context context) {
        if (context != null) {
            this.mDualToneHandler.setColorsFromContext(context);
        }
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            setVisibility(StatusBarIconController.getIconHideList(getContext(), str2).contains(this.mSlotBattery) ? 8 : 0);
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        BatteryController batteryController = (BatteryController) Dependency.get(BatteryController.class);
        this.mBatteryController = batteryController;
        batteryController.addCallback(this);
        this.mUser = ActivityManager.getCurrentUser();
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, this.mSettingObserver, this.mUser);
        getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingObserver);
        updateShowPercent();
        subscribeForTunerUpdates();
        this.mUserTracker.startTracking();
    }

    @Override // android.view.View, android.view.ViewGroup
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mUserTracker.stopTracking();
        this.mBatteryController.removeCallback(this);
        getContext().getContentResolver().unregisterContentObserver(this.mSettingObserver);
        unsubscribeFromTunerUpdates();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mDrawable.setCharging(z);
        this.mDrawable.setBatteryLevel(i);
        this.mCharging = z;
        this.mLevel = i;
        updatePercentText();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
        this.mDrawable.setPowerSaveEnabled(z);
    }

    private TextView loadPercentView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R$layout.battery_percentage_view, (ViewGroup) null);
    }

    public void updatePercentView() {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            removeView(textView);
            this.mBatteryPercentView = null;
        }
        updateShowPercent();
    }

    /* access modifiers changed from: private */
    public void updatePercentText() {
        int i;
        if (this.mBatteryStateUnknown) {
            setContentDescription(getContext().getString(R$string.accessibility_battery_unknown));
            return;
        }
        BatteryController batteryController = this.mBatteryController;
        if (batteryController != null) {
            if (this.mBatteryPercentView == null) {
                Context context = getContext();
                if (this.mCharging) {
                    i = R$string.accessibility_battery_level_charging;
                } else {
                    i = R$string.accessibility_battery_level;
                }
                setContentDescription(context.getString(i, Integer.valueOf(this.mLevel)));
            } else if (this.mShowPercentMode != 3 || this.mCharging) {
                setPercentTextAtCurrentLevel();
            } else {
                batteryController.getEstimatedTimeRemainingString(new BatteryController.EstimateFetchCompletion() { // from class: com.android.systemui.BatteryMeterView$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.statusbar.policy.BatteryController.EstimateFetchCompletion
                    public final void onBatteryRemainingEstimateRetrieved(String str) {
                        BatteryMeterView.m41$r8$lambda$tETUFngFnD6fsDmUpqJf1miV_U(BatteryMeterView.this, str);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePercentText$0(String str) {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            if (str == null || this.mShowPercentMode != 3) {
                setPercentTextAtCurrentLevel();
                return;
            }
            textView.setText(str);
            setContentDescription(getContext().getString(R$string.accessibility_battery_level_with_estimate, Integer.valueOf(this.mLevel), str));
        }
    }

    private void setPercentTextAtCurrentLevel() {
        int i;
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            textView.setText(NumberFormat.getPercentInstance().format((double) (((float) this.mLevel) / 100.0f)));
            Context context = getContext();
            if (this.mCharging) {
                i = R$string.accessibility_battery_level_charging;
            } else {
                i = R$string.accessibility_battery_level;
            }
            setContentDescription(context.getString(i, Integer.valueOf(this.mLevel)));
        }
    }

    /* access modifiers changed from: private */
    public void updateShowPercent() {
        int i;
        boolean z = false;
        boolean z2 = this.mBatteryPercentView != null;
        if (((this.mShowPercentAvailable && (((Integer) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.BatteryMeterView$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return BatteryMeterView.m40$r8$lambda$oORmrAFv3kaLmoKmcIHJrfL7bc(BatteryMeterView.this);
            }
        })).intValue() != 0) && this.mShowPercentMode != 2) || (i = this.mShowPercentMode) == 1 || i == 3) && !this.mBatteryStateUnknown) {
            z = true;
        }
        if (z) {
            if (!z2) {
                TextView loadPercentView = loadPercentView();
                this.mBatteryPercentView = loadPercentView;
                int i2 = this.mPercentageStyleId;
                if (i2 != 0) {
                    loadPercentView.setTextAppearance(i2);
                }
                int i3 = this.mTextColor;
                if (i3 != 0) {
                    this.mBatteryPercentView.setTextColor(i3);
                }
                updatePercentText();
                addView(this.mBatteryPercentView, new ViewGroup.LayoutParams(-2, -1));
            }
        } else if (z2) {
            removeView(this.mBatteryPercentView);
            this.mBatteryPercentView = null;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Integer lambda$updateShowPercent$1() {
        return Integer.valueOf(Settings.System.getIntForUser(getContext().getContentResolver(), "status_bar_show_battery_percent", 0, this.mUser));
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        scaleBatteryMeterViews();
    }

    private Drawable getUnknownStateDrawable() {
        if (this.mUnknownStateDrawable == null) {
            Drawable drawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.ic_battery_unknown);
            this.mUnknownStateDrawable = drawable;
            drawable.setTint(this.mTextColor);
        }
        return this.mUnknownStateDrawable;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryUnknownStateChanged(boolean z) {
        if (this.mBatteryStateUnknown != z) {
            this.mBatteryStateUnknown = z;
            if (z) {
                this.mBatteryIconView.setImageDrawable(getUnknownStateDrawable());
            } else {
                this.mBatteryIconView.setImageDrawable(this.mDrawable);
            }
            updateShowPercent();
        }
    }

    private void scaleBatteryMeterViews() {
        Resources resources = getContext().getResources();
        TypedValue typedValue = new TypedValue();
        resources.getValue(R$dimen.status_bar_icon_scale_factor, typedValue, true);
        float f = typedValue.getFloat();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.status_bar_battery_icon_height);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.status_bar_battery_icon_width);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R$dimen.battery_margin_bottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (((float) dimensionPixelSize2) * f), (int) (((float) dimensionPixelSize) * f));
        layoutParams.setMargins(0, 0, 0, dimensionPixelSize3);
        this.mBatteryIconView.setLayoutParams(layoutParams);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        if (!DarkIconDispatcher.isInArea(rect, this)) {
            f = 0.0f;
        }
        this.mNonAdaptedSingleToneColor = this.mDualToneHandler.getSingleColor(f);
        this.mNonAdaptedForegroundColor = this.mDualToneHandler.getFillColor(f);
        int backgroundColor = this.mDualToneHandler.getBackgroundColor(f);
        this.mNonAdaptedBackgroundColor = backgroundColor;
        updateColors(this.mNonAdaptedForegroundColor, backgroundColor, this.mNonAdaptedSingleToneColor);
    }

    public void updateColors(int i, int i2, int i3) {
        this.mDrawable.setColors(i, i2, i3);
        this.mTextColor = i3;
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            textView.setTextColor(i3);
        }
        Drawable drawable = this.mUnknownStateDrawable;
        if (drawable != null) {
            drawable.setTint(i3);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        CharSequence charSequence = null;
        if (this.mDrawable == null) {
            str = null;
        } else {
            str = this.mDrawable.getPowerSaveEnabled() + "";
        }
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            charSequence = textView.getText();
        }
        printWriter.println("  BatteryMeterView:");
        printWriter.println("    mDrawable.getPowerSave: " + str);
        printWriter.println("    mBatteryPercentView.getText(): " + ((Object) charSequence));
        printWriter.println("    mTextColor: #" + Integer.toHexString(this.mTextColor));
        printWriter.println("    mBatteryStateUnknown: " + this.mBatteryStateUnknown);
        printWriter.println("    mLevel: " + this.mLevel);
        printWriter.println("    mMode: " + this.mShowPercentMode);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class SettingObserver extends ContentObserver {
        public SettingObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            BatteryMeterView.this.updateShowPercent();
            if (TextUtils.equals(uri.getLastPathSegment(), "battery_estimates_last_update_time")) {
                BatteryMeterView.this.updatePercentText();
            }
        }
    }
}
