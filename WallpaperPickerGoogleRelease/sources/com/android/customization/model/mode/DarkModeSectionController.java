package com.android.customization.model.mode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.customization.picker.mode.DarkModeSectionView;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class DarkModeSectionController implements CustomizationSectionController<DarkModeSectionView>, LifecycleObserver {
    public static final ExecutorService sExecutorService = Executors.newSingleThreadExecutor();
    public final BatterySaverStateReceiver mBatterySaverStateReceiver = new BatterySaverStateReceiver(null);
    public Context mContext;
    public DarkModeSectionView mDarkModeSectionView;
    public final Lifecycle mLifecycle;
    public final PowerManager mPowerManager;

    /* loaded from: classes.dex */
    public class BatterySaverStateReceiver extends BroadcastReceiver {
        public BatterySaverStateReceiver(AnonymousClass1 r2) {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            DarkModeSectionController darkModeSectionController;
            DarkModeSectionView darkModeSectionView;
            if (TextUtils.equals(intent.getAction(), "android.os.action.POWER_SAVE_MODE_CHANGED") && (darkModeSectionView = (darkModeSectionController = DarkModeSectionController.this).mDarkModeSectionView) != null) {
                darkModeSectionView.setEnabled(!darkModeSectionController.mPowerManager.isPowerSaveMode());
            }
        }
    }

    public DarkModeSectionController(Context context, Lifecycle lifecycle) {
        this.mContext = context;
        this.mLifecycle = lifecycle;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        lifecycle.addObserver(this);
    }

    /* Return type fixed from 'com.android.wallpaper.picker.SectionView' to match base method */
    @Override // com.android.wallpaper.model.CustomizationSectionController
    public DarkModeSectionView createView(Context context) {
        DarkModeSectionView darkModeSectionView = (DarkModeSectionView) LayoutInflater.from(context).inflate(R.layout.dark_mode_section_view, (ViewGroup) null);
        this.mDarkModeSectionView = darkModeSectionView;
        darkModeSectionView.mSectionViewListener = new PreviewPager$$ExternalSyntheticLambda1(this);
        this.mDarkModeSectionView.setEnabled(!((PowerManager) context.getSystemService(PowerManager.class)).isPowerSaveMode());
        return this.mDarkModeSectionView;
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public boolean isAvailable(Context context) {
        return context != null && ContextCompat.checkSelfPermission(context, "android.permission.MODIFY_DAY_NIGHT_MODE") == 0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        sExecutorService.submit(new DarkModeSectionController$$ExternalSyntheticLambda0(this, 0));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        sExecutorService.submit(new DarkModeSectionController$$ExternalSyntheticLambda0(this, 1));
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public void release() {
        LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) this.mLifecycle;
        lifecycleRegistry.enforceMainThreadIfNeeded("removeObserver");
        lifecycleRegistry.mObserverMap.remove(this);
        this.mContext = null;
    }
}
