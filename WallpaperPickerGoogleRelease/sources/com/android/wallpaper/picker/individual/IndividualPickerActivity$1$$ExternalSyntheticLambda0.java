package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.DownloadableLiveWallpaperInfo;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperReceiver;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.picker.WallpapersUiContainer;
import com.android.wallpaper.picker.individual.IndividualPickerActivity;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class IndividualPickerActivity$1$$ExternalSyntheticLambda0 implements WallpaperReceiver {
    public final /* synthetic */ int $r8$classId = 0;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ IndividualPickerActivity$1$$ExternalSyntheticLambda0(IndividualPickerActivity.AnonymousClass1 r2) {
        this.f$0 = r2;
    }

    public /* synthetic */ IndividualPickerActivity$1$$ExternalSyntheticLambda0(IndividualPickerUnlockableFragment.AnonymousClass1 r2) {
        this.f$0 = r2;
    }

    @Override // com.android.wallpaper.model.WallpaperReceiver
    public final void onWallpapersReceived(List list) {
        int i = 1;
        switch (this.$r8$classId) {
            case 0:
                IndividualPickerActivity.AnonymousClass1 r4 = (IndividualPickerActivity.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r4);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    WallpaperInfo wallpaperInfo = (WallpaperInfo) it.next();
                    if (wallpaperInfo.getWallpaperId().equals(IndividualPickerActivity.this.mWallpaperId)) {
                        IndividualPickerActivity individualPickerActivity = IndividualPickerActivity.this;
                        ((DefaultWallpaperPersister) individualPickerActivity.mWallpaperPersister).mWallpaperInfoInPreview = wallpaperInfo;
                        InlinePreviewIntentFactory inlinePreviewIntentFactory = individualPickerActivity.mPreviewIntentFactory;
                        if (wallpaperInfo instanceof LiveWallpaperInfo) {
                            i = 4;
                        }
                        wallpaperInfo.showPreview(individualPickerActivity, inlinePreviewIntentFactory, i);
                        return;
                    }
                }
                IndividualPickerActivity.this.finish();
                return;
            default:
                IndividualPickerUnlockableFragment.AnonymousClass1 r42 = (IndividualPickerUnlockableFragment.AnonymousClass1) this.f$0;
                IndividualPickerUnlockableFragment individualPickerUnlockableFragment = IndividualPickerUnlockableFragment.this;
                individualPickerUnlockableFragment.mIsWallpapersReceived = true;
                individualPickerUnlockableFragment.updateLoading();
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    WallpaperInfo wallpaperInfo2 = (WallpaperInfo) it2.next();
                    if (wallpaperInfo2 == null || !(wallpaperInfo2 instanceof DownloadableLiveWallpaperInfo)) {
                        IndividualPickerUnlockableFragment.this.mWallpapers.add(wallpaperInfo2);
                    } else {
                        IndividualPickerUnlockableFragment.this.mDownloadableWallpapers.add(wallpaperInfo2);
                    }
                }
                if (IndividualPickerUnlockableFragment.this.mDownloadableWallpapers.size() > 0) {
                    IndividualPickerUnlockableFragment individualPickerUnlockableFragment2 = IndividualPickerUnlockableFragment.this;
                    individualPickerUnlockableFragment2.mWallpapers.add(new WallpaperInfo(individualPickerUnlockableFragment2, "unlock_additionals_header") { // from class: com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.2
                        public final /* synthetic */ String val$collectionIdHeader;

                        {
                            this.val$collectionIdHeader = r2;
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo
                        public Asset getAsset(Context context) {
                            return null;
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo
                        public List<String> getAttributions(Context context) {
                            return null;
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo
                        public String getCollectionId(Context context) {
                            return this.val$collectionIdHeader;
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo
                        public Asset getThumbAsset(Context context) {
                            return null;
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo
                        public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory2, int i2) {
                        }

                        @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
                        public void writeToParcel(Parcel parcel, int i2) {
                        }
                    });
                    IndividualPickerUnlockableFragment individualPickerUnlockableFragment3 = IndividualPickerUnlockableFragment.this;
                    individualPickerUnlockableFragment3.mWallpapers.addAll(individualPickerUnlockableFragment3.mDownloadableWallpapers);
                }
                IndividualPickerUnlockableFragment.this.maybeSetUpImageGrid();
                IndividualPickerFragment.IndividualAdapter individualAdapter = IndividualPickerUnlockableFragment.this.mAdapter;
                if (individualAdapter != null) {
                    individualAdapter.mObservable.notifyChanged();
                }
                WallpapersUiContainer wallpapersUiContainer = IndividualPickerUnlockableFragment.this.mWallpapersUiContainer;
                if (wallpapersUiContainer != null) {
                    wallpapersUiContainer.onWallpapersReady();
                    return;
                }
                return;
        }
    }
}
