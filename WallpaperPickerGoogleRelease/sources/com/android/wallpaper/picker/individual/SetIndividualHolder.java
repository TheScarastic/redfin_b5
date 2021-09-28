package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.cardview.R$dimen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.BaseActivity;
import com.android.wallpaper.picker.SetWallpaperErrorDialogFragment;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import java.util.Objects;
/* loaded from: classes.dex */
public class SetIndividualHolder extends IndividualHolder implements View.OnClickListener, SelectableHolder {
    public OnSetListener mOnSetListener;
    public SelectionAnimator mSelectionAnimator;
    public View mTile;

    /* loaded from: classes.dex */
    public interface OnSetListener {
    }

    public SetIndividualHolder(Activity activity, int i, View view, SelectionAnimator selectionAnimator, OnSetListener onSetListener) {
        super(activity, i, view);
        this.mTile = view.findViewById(R.id.tile);
        this.mSelectionAnimator = selectionAnimator;
        this.mOnSetListener = onSetListener;
    }

    @Override // com.android.wallpaper.picker.individual.IndividualHolder
    public void bindWallpaper(WallpaperInfo wallpaperInfo) {
        super.bindWallpaper(wallpaperInfo);
        String wallpaperId = this.mWallpaper.getWallpaperId();
        if (wallpaperId != null && wallpaperId.equals(InjectorProvider.getInjector().getPreferences(this.mActivity.getApplicationContext()).getHomeWallpaperRemoteId())) {
            Objects.requireNonNull(this.mSelectionAnimator);
        } else {
            Objects.requireNonNull(this.mSelectionAnimator);
        }
        this.mTile.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        setWallpaper();
    }

    @Override // com.android.wallpaper.picker.individual.SelectableHolder
    public void setSelectionState(int i) {
        if (i == 2) {
            Objects.requireNonNull(this.mSelectionAnimator);
        } else if (i == 0) {
            Objects.requireNonNull(this.mSelectionAnimator);
        } else if (i == 1) {
            Objects.requireNonNull(this.mSelectionAnimator);
        }
    }

    public void setWallpaper() {
        int i;
        RecyclerView recyclerView;
        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;
        Objects.requireNonNull(this.mSelectionAnimator);
        if (this.mBindingAdapter == null || (recyclerView = this.mOwnerRecyclerView) == null || (adapter = recyclerView.getAdapter()) == null || (i = this.mOwnerRecyclerView.getAdapterPositionInRecyclerView(this)) == -1 || this.mBindingAdapter != adapter) {
            i = -1;
        }
        IndividualPickerFragment.IndividualAdapter.AnonymousClass1 r0 = (IndividualPickerFragment.IndividualAdapter.AnonymousClass1) this.mOnSetListener;
        IndividualPickerFragment.IndividualAdapter individualAdapter = IndividualPickerFragment.IndividualAdapter.this;
        int i2 = individualAdapter.mPendingSelectedAdapterPosition;
        if (i2 != -1) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = IndividualPickerFragment.this.mImageGrid.findViewHolderForAdapterPosition(i2);
            if (findViewHolderForAdapterPosition instanceof SelectableHolder) {
                ((SelectableHolder) findViewHolderForAdapterPosition).setSelectionState(0);
            }
        }
        IndividualPickerFragment.IndividualAdapter individualAdapter2 = IndividualPickerFragment.IndividualAdapter.this;
        int i3 = individualAdapter2.mSelectedAdapterPosition;
        if (i3 != -1) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = IndividualPickerFragment.this.mImageGrid.findViewHolderForAdapterPosition(i3);
            if (findViewHolderForAdapterPosition2 instanceof SelectableHolder) {
                ((SelectableHolder) findViewHolderForAdapterPosition2).setSelectionState(0);
            }
        }
        IndividualPickerFragment.IndividualAdapter.this.mPendingSelectedAdapterPosition = i;
        Context applicationContext = this.mActivity.getApplicationContext();
        Objects.requireNonNull(this.mSelectionAnimator);
        UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(applicationContext);
        userEventLogger.logIndividualWallpaperSelected(this.mWallpaper.getCollectionId(this.mActivity));
        ((DefaultWallpaperPersister) InjectorProvider.getInjector().getWallpaperPersister(applicationContext)).setIndividualWallpaper(this.mWallpaper, this.mWallpaper.getDesktopAsset(applicationContext), null, 1.0f, 2, new WallpaperPersister.SetWallpaperCallback(i, userEventLogger, applicationContext) { // from class: com.android.wallpaper.picker.individual.SetIndividualHolder.1
            public final /* synthetic */ Context val$appContext;
            public final /* synthetic */ UserEventLogger val$eventLogger;

            {
                this.val$eventLogger = r3;
                this.val$appContext = r4;
            }

            @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
            public void onError(Throwable th) {
                Log.e("SetIndividualHolder", "Could not set a wallpaper.");
                this.val$eventLogger.logWallpaperSetResult(1);
                this.val$eventLogger.logWallpaperSetFailureReason(R$dimen.isOOM(th) ? 1 : 0);
                Objects.requireNonNull(SetIndividualHolder.this.mSelectionAnimator);
                SetIndividualHolder setIndividualHolder = SetIndividualHolder.this;
                IndividualPickerFragment.IndividualAdapter.AnonymousClass1 r5 = (IndividualPickerFragment.IndividualAdapter.AnonymousClass1) setIndividualHolder.mOnSetListener;
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                int i4 = IndividualPickerFragment.$r8$clinit;
                Objects.requireNonNull(individualPickerFragment);
                SetWallpaperErrorDialogFragment newInstance = SetWallpaperErrorDialogFragment.newInstance(R.string.set_wallpaper_error_message, 2);
                newInstance.setTargetFragment(individualPickerFragment, 1);
                if (((BaseActivity) individualPickerFragment.getActivity()).mIsSafeToCommitFragmentTransaction) {
                    newInstance.show(individualPickerFragment.mFragmentManager, "individual_set_wallpaper_error_dialog");
                } else {
                    individualPickerFragment.mStagedSetWallpaperErrorDialogFragment = newInstance;
                }
                IndividualPickerFragment.this.mPendingSetIndividualHolder = setIndividualHolder;
            }

            @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
            public void onSuccess(WallpaperInfo wallpaperInfo) {
                Objects.requireNonNull(SetIndividualHolder.this.mOnSetListener);
                this.val$eventLogger.logWallpaperSet(SetIndividualHolder.this.mWallpaper.getCollectionId(this.val$appContext), SetIndividualHolder.this.mWallpaper.getWallpaperId());
                this.val$eventLogger.logWallpaperSetResult(0);
            }
        });
    }
}
