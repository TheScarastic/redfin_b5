package com.android.wallpaper.widget;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Point;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.shared.R;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.TimeUtils$TimeTicker;
import com.google.common.math.IntMath;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class LockScreenPreviewer implements LifecycleObserver {
    public static final ExecutorService sExecutorService = Executors.newSingleThreadExecutor();
    public final Context mContext;
    public final String mDatePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE, MMM d");
    public final Lifecycle mLifecycle;
    public final TextView mLockDate;
    public final TextView mLockTime;
    public TimeUtils$TimeTicker mTicker;

    public LockScreenPreviewer(Lifecycle lifecycle, Context context, final ViewGroup viewGroup) {
        this.mLifecycle = lifecycle;
        this.mContext = context;
        final View inflate = LayoutInflater.from(context).inflate(R.layout.lock_screen_preview, (ViewGroup) null);
        this.mLockTime = (TextView) inflate.findViewById(R.id.lock_time);
        this.mLockDate = (TextView) inflate.findViewById(R.id.lock_date);
        final Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService(WindowManager.class)).getDefaultDisplay());
        final boolean z = context.getResources().getConfiguration().getLayoutDirection() == 0;
        final View rootView = viewGroup.getRootView();
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener(this) { // from class: com.android.wallpaper.widget.LockScreenPreviewer.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                int i9;
                float f;
                int measuredHeight = viewGroup.getMeasuredHeight();
                int measuredWidth = viewGroup.getMeasuredWidth();
                inflate.measure(View.MeasureSpec.makeMeasureSpec(screenSize.x, IntMath.MAX_SIGNED_POWER_OF_TWO), View.MeasureSpec.makeMeasureSpec(screenSize.y, IntMath.MAX_SIGNED_POWER_OF_TWO));
                View view2 = inflate;
                Point point = screenSize;
                view2.layout(0, 0, point.x, point.y);
                if (measuredHeight > 0) {
                    f = (float) measuredHeight;
                    i9 = screenSize.y;
                } else {
                    f = (float) measuredWidth;
                    i9 = screenSize.x;
                }
                float f2 = f / ((float) i9);
                inflate.setScaleX(f2);
                inflate.setScaleY(f2);
                View view3 = inflate;
                view3.setPivotX(z ? 0.0f : (float) view3.getMeasuredWidth());
                inflate.setPivotY(0.0f);
                viewGroup.removeAllViews();
                ViewGroup viewGroup2 = viewGroup;
                View view4 = inflate;
                viewGroup2.addView(view4, view4.getMeasuredWidth(), inflate.getMeasuredHeight());
                rootView.removeOnLayoutChangeListener(this);
            }
        });
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (this.mTicker != null) {
            sExecutorService.submit(new LockScreenPreviewer$$ExternalSyntheticLambda0(this, 0));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mTicker == null) {
            sExecutorService.submit(new LockScreenPreviewer$$ExternalSyntheticLambda0(this, 1));
        }
        updateDateTime();
    }

    public void release() {
        LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) this.mLifecycle;
        lifecycleRegistry.enforceMainThreadIfNeeded("removeObserver");
        lifecycleRegistry.mObserverMap.remove(this);
        if (this.mTicker != null) {
            sExecutorService.submit(new LockScreenPreviewer$$ExternalSyntheticLambda0(this, 0));
        }
    }

    public void setColor(WallpaperColors wallpaperColors) {
        boolean z = true;
        if (!(wallpaperColors == null || (wallpaperColors.getColorHints() & 1) == 0)) {
            z = false;
        }
        int color = this.mContext.getColor(z ? R.color.text_color_light : R.color.text_color_dark);
        int color2 = this.mContext.getColor(z ? R.color.smartspace_preview_shadow_color_dark : R.color.smartspace_preview_shadow_color_transparent);
        this.mLockDate.setTextColor(color);
        this.mLockDate.setShadowLayer(this.mContext.getResources().getDimension(R.dimen.smartspace_preview_key_ambient_shadow_blur), 0.0f, 0.0f, color2);
    }

    public void setDateViewVisibility(boolean z) {
        this.mLockDate.setVisibility(z ? 0 : 4);
    }

    public final void updateDateTime() {
        Calendar instance = Calendar.getInstance(TimeZone.getDefault());
        this.mLockDate.setText(DateFormat.format(this.mDatePattern, instance));
        TextView textView = this.mLockTime;
        textView.setText(DateFormat.format(DateFormat.is24HourFormat(textView.getContext()) ? "HH\nmm" : "hh\nmm", instance));
    }
}
