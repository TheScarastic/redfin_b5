package com.android.wallpaper.module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Rect;
import androidx.cardview.R$dimen;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.CurrentWallpaperInfoFactory;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.SetWallpaperDialogFragment;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class WallpaperSetter {
    public Optional<Integer> mCurrentScreenOrientation = Optional.empty();
    public final WallpaperPreferences mPreferences;
    public ProgressDialog mProgressDialog;
    public final boolean mTestingModeEnabled;
    public final UserEventLogger mUserEventLogger;
    public final WallpaperPersister mWallpaperPersister;

    public WallpaperSetter(WallpaperPersister wallpaperPersister, WallpaperPreferences wallpaperPreferences, UserEventLogger userEventLogger, boolean z) {
        this.mTestingModeEnabled = z;
        this.mWallpaperPersister = wallpaperPersister;
        this.mPreferences = wallpaperPreferences;
        this.mUserEventLogger = userEventLogger;
    }

    public void cleanUp() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    public final void onWallpaperApplied(WallpaperInfo wallpaperInfo, Activity activity) {
        this.mUserEventLogger.logWallpaperSet(wallpaperInfo.getCollectionId(activity), wallpaperInfo.getWallpaperId());
        this.mPreferences.setPendingWallpaperSetStatus(0);
        this.mUserEventLogger.logWallpaperSetResult(0);
        cleanUp();
        this.mCurrentScreenOrientation.ifPresent(new WallpaperSetter$$ExternalSyntheticLambda1(this, activity));
    }

    public final void onWallpaperApplyError(Throwable th, Activity activity) {
        this.mPreferences.setPendingWallpaperSetStatus(0);
        this.mUserEventLogger.logWallpaperSetResult(1);
        this.mUserEventLogger.logWallpaperSetFailureReason(R$dimen.isOOM(th) ? 1 : 0);
        cleanUp();
        this.mCurrentScreenOrientation.ifPresent(new WallpaperSetter$$ExternalSyntheticLambda1(this, activity));
    }

    public void requestDestination(final Activity activity, FragmentManager fragmentManager, final SetWallpaperDialogFragment.Listener listener, boolean z) {
        CurrentWallpaperInfoFactory currentWallpaperFactory = InjectorProvider.getInjector().getCurrentWallpaperFactory(activity);
        saveAndLockScreenOrientationIfNeeded(activity);
        ((DefaultCurrentWallpaperInfoFactory) currentWallpaperFactory).createCurrentWallpaperInfos(new CurrentWallpaperInfoFactory.WallpaperInfoCallback(R.string.set_wallpaper_dialog_message, new SetWallpaperDialogFragment.Listener() { // from class: com.android.wallpaper.module.WallpaperSetter.3
            @Override // com.android.wallpaper.picker.SetWallpaperDialogFragment.Listener
            public void onDialogDismissed(boolean z2) {
                if (!z2) {
                    WallpaperSetter wallpaperSetter = WallpaperSetter.this;
                    wallpaperSetter.mCurrentScreenOrientation.ifPresent(new WallpaperSetter$$ExternalSyntheticLambda1(wallpaperSetter, activity));
                }
                SetWallpaperDialogFragment.Listener listener2 = listener;
                if (listener2 != null) {
                    listener2.onDialogDismissed(z2);
                }
            }

            @Override // com.android.wallpaper.picker.SetWallpaperDialogFragment.Listener
            public void onSet(int i) {
                SetWallpaperDialogFragment.Listener listener2 = listener;
                if (listener2 != null) {
                    listener2.onSet(i);
                }
            }
        }, z, listener, activity, fragmentManager) { // from class: com.android.wallpaper.module.WallpaperSetter$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;
            public final /* synthetic */ SetWallpaperDialogFragment.Listener f$2;
            public final /* synthetic */ boolean f$3;
            public final /* synthetic */ SetWallpaperDialogFragment.Listener f$4;
            public final /* synthetic */ Activity f$5;
            public final /* synthetic */ FragmentManager f$6;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
                this.f$6 = r7;
            }

            @Override // com.android.wallpaper.module.CurrentWallpaperInfoFactory.WallpaperInfoCallback
            public final void onWallpaperInfoCreated(WallpaperInfo wallpaperInfo, WallpaperInfo wallpaperInfo2, int i) {
                WallpaperSetter wallpaperSetter = WallpaperSetter.this;
                int i2 = this.f$1;
                SetWallpaperDialogFragment.Listener listener2 = this.f$2;
                boolean z2 = this.f$3;
                SetWallpaperDialogFragment.Listener listener3 = this.f$4;
                Activity activity2 = this.f$5;
                FragmentManager fragmentManager2 = this.f$6;
                Objects.requireNonNull(wallpaperSetter);
                SetWallpaperDialogFragment setWallpaperDialogFragment = new SetWallpaperDialogFragment();
                setWallpaperDialogFragment.mTitleResId = i2;
                setWallpaperDialogFragment.mListener = listener2;
                if ((wallpaperInfo instanceof LiveWallpaperInfo) && wallpaperInfo2 == null) {
                    if (z2) {
                        listener3.onSet(2);
                        wallpaperSetter.mCurrentScreenOrientation.ifPresent(new WallpaperSetter$$ExternalSyntheticLambda1(wallpaperSetter, activity2));
                        return;
                    }
                    setWallpaperDialogFragment.mHomeAvailable = false;
                    setWallpaperDialogFragment.updateButtonsVisibility();
                }
                if (z2) {
                    setWallpaperDialogFragment.mLockAvailable = false;
                    setWallpaperDialogFragment.updateButtonsVisibility();
                }
                setWallpaperDialogFragment.show(fragmentManager2, "set_wallpaper_dialog");
            }
        }, true);
    }

    public final void saveAndLockScreenOrientationIfNeeded(Activity activity) {
        if (!this.mCurrentScreenOrientation.isPresent()) {
            this.mCurrentScreenOrientation = Optional.of(Integer.valueOf(activity.getRequestedOrientation()));
            activity.setRequestedOrientation(14);
        }
    }

    public void setCurrentWallpaper(final Activity activity, final WallpaperInfo wallpaperInfo, Asset asset, int i, float f, Rect rect, final WallpaperPersister.SetWallpaperCallback setWallpaperCallback) {
        if (wallpaperInfo instanceof LiveWallpaperInfo) {
            LiveWallpaperInfo liveWallpaperInfo = (LiveWallpaperInfo) wallpaperInfo;
            try {
                saveAndLockScreenOrientationIfNeeded(activity);
                if (i != 1) {
                    WallpaperManager instance = WallpaperManager.getInstance(activity);
                    instance.setWallpaperComponent(liveWallpaperInfo.mInfo.getComponent());
                    instance.setWallpaperOffsetSteps(0.5f, 0.0f);
                    instance.setWallpaperOffsets(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0.5f, 0.0f);
                    if (i == 2) {
                        instance.clear(2);
                    }
                    onWallpaperApplied(liveWallpaperInfo, activity);
                    if (setWallpaperCallback != null) {
                        setWallpaperCallback.onSuccess(liveWallpaperInfo);
                        return;
                    }
                    return;
                }
                throw new IllegalArgumentException("Live wallpaper cannot be applied on lock screen only");
            } catch (IOException | RuntimeException e) {
                onWallpaperApplyError(e, activity);
                if (setWallpaperCallback != null) {
                    setWallpaperCallback.onError(e);
                }
            }
        } else {
            this.mPreferences.setPendingWallpaperSetStatus(1);
            saveAndLockScreenOrientationIfNeeded(activity);
            Glide.get(activity).clearMemory();
            if (!this.mTestingModeEnabled && !activity.isFinishing()) {
                ProgressDialog progressDialog = new ProgressDialog(activity, R.style.LightDialogTheme);
                this.mProgressDialog = progressDialog;
                progressDialog.setTitle((CharSequence) null);
                this.mProgressDialog.setMessage(activity.getString(R.string.set_wallpaper_progress_message));
                this.mProgressDialog.setIndeterminate(true);
                if (activity instanceof LifecycleOwner) {
                    ((LifecycleOwner) activity).getLifecycle().addObserver(new LifecycleEventObserver() { // from class: com.android.wallpaper.module.WallpaperSetter.1
                        @Override // androidx.lifecycle.LifecycleEventObserver
                        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                            ProgressDialog progressDialog2;
                            if (event == Lifecycle.Event.ON_DESTROY && (progressDialog2 = WallpaperSetter.this.mProgressDialog) != null) {
                                progressDialog2.dismiss();
                            }
                        }
                    });
                }
                this.mProgressDialog.show();
            }
            ((DefaultWallpaperPersister) this.mWallpaperPersister).setIndividualWallpaper(wallpaperInfo, asset, rect, f, i, new WallpaperPersister.SetWallpaperCallback() { // from class: com.android.wallpaper.module.WallpaperSetter.2
                @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
                public void onError(Throwable th) {
                    WallpaperSetter.this.onWallpaperApplyError(th, activity);
                    WallpaperPersister.SetWallpaperCallback setWallpaperCallback2 = setWallpaperCallback;
                    if (setWallpaperCallback2 != null) {
                        setWallpaperCallback2.onError(th);
                    }
                }

                @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
                public void onSuccess(WallpaperInfo wallpaperInfo2) {
                    WallpaperSetter.this.onWallpaperApplied(wallpaperInfo, activity);
                    WallpaperPersister.SetWallpaperCallback setWallpaperCallback2 = setWallpaperCallback;
                    if (setWallpaperCallback2 != null) {
                        setWallpaperCallback2.onSuccess(wallpaperInfo);
                    }
                }
            });
        }
    }
}
