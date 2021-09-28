package com.android.wallpaper.picker.individual;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.customization.picker.grid.GridFragment$$ExternalSyntheticLambda1;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.DesktopCustomCategory;
import com.android.wallpaper.model.ThirdPartyLiveWallpaperCategory;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperReceiver;
import com.android.wallpaper.model.WallpaperRotationInitializer;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultPackageStatusNotifier;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.PackageStatusNotifier;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.module.WallpaperChangedNotifier;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.module.WallpaperSetter;
import com.android.wallpaper.picker.AppbarFragment;
import com.android.wallpaper.picker.CurrentWallpaperBottomSheetPresenter;
import com.android.wallpaper.picker.FragmentTransactionChecker;
import com.android.wallpaper.picker.MyPhotosStarter;
import com.android.wallpaper.picker.RotationStarter;
import com.android.wallpaper.picker.SetWallpaperDialogFragment;
import com.android.wallpaper.picker.SetWallpaperErrorDialogFragment;
import com.android.wallpaper.picker.StartRotationDialogFragment;
import com.android.wallpaper.picker.StartRotationErrorDialogFragment;
import com.android.wallpaper.picker.TopLevelPickerActivity;
import com.android.wallpaper.picker.WallpapersUiContainer;
import com.android.wallpaper.picker.individual.SetIndividualHolder;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import com.android.wallpaper.widget.WallpaperPickerRecyclerViewAccessibilityDelegate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
/* loaded from: classes.dex */
public class IndividualPickerFragment extends AppbarFragment implements RotationStarter, StartRotationErrorDialogFragment.Listener, CurrentWallpaperBottomSheetPresenter.RefreshListener, SetWallpaperErrorDialogFragment.Listener, SetWallpaperDialogFragment.Listener, StartRotationDialogFragment.Listener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public IndividualAdapter mAdapter;
    public PackageStatusNotifier.Listener mAppStatusListener;
    public Set<String> mAppliedWallpaperIds;
    public WallpaperInfo mAppliedWallpaperInfo;
    public WallpaperCategory mCategory;
    public CategoryProvider mCategoryProvider;
    public Runnable mCurrentWallpaperBottomSheetExpandedRunnable;
    public CurrentWallpaperBottomSheetPresenter mCurrentWallpaperBottomSheetPresenter;
    public int mFormFactor;
    public Handler mHandler;
    public RecyclerView mImageGrid;
    public boolean mIsWallpapersReceived;
    public ContentLoadingProgressBar mLoading;
    public PackageStatusNotifier mPackageStatusNotifier;
    public SetIndividualHolder mPendingSetIndividualHolder;
    public ProgressDialog mProgressDialog;
    public Random mRandom;
    public SetWallpaperErrorDialogFragment mStagedSetWallpaperErrorDialogFragment;
    public StartRotationErrorDialogFragment mStagedStartRotationErrorDialogFragment;
    public Point mTileSizePx;
    public UserEventLogger mUserEventLogger;
    public WallpaperChangedNotifier mWallpaperChangedNotifier;
    public int mWallpaperDestination;
    public WallpaperManager mWallpaperManager;
    public WallpaperPersister mWallpaperPersister;
    public WallpaperPreferences mWallpaperPreferences;
    public WallpaperRotationInitializer mWallpaperRotationInitializer;
    public WallpaperSetter mWallpaperSetter;
    public List<WallpaperInfo> mWallpapers;
    public WallpapersUiContainer mWallpapersUiContainer;
    public boolean mWasUpdateRunnableRun;
    public WallpaperChangedNotifier.Listener mWallpaperChangedListener = new WallpaperChangedNotifier.Listener() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.1
        @Override // com.android.wallpaper.module.WallpaperChangedNotifier.Listener
        public void onWallpaperChanged() {
            IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
            if (individualPickerFragment.mFormFactor == 0) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = individualPickerFragment.mImageGrid.findViewHolderForAdapterPosition(individualPickerFragment.mAdapter.mSelectedAdapterPosition);
                if (IndividualPickerFragment.this.mWallpaperPreferences.getHomeWallpaperRemoteId() != null) {
                    IndividualAdapter individualAdapter = IndividualPickerFragment.this.mAdapter;
                    IndividualAdapter.access$200(individualAdapter, individualAdapter.mPendingSelectedAdapterPosition);
                } else if (findViewHolderForAdapterPosition instanceof SelectableHolder) {
                    ((SelectableHolder) findViewHolderForAdapterPosition).setSelectionState(0);
                }
            }
        }
    };
    public Runnable mUpdateDailyWallpaperThumbRunnable = new Runnable() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.2
        @Override // java.lang.Runnable
        public void run() {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = IndividualPickerFragment.this.mImageGrid.findViewHolderForAdapterPosition(0);
            if (findViewHolderForAdapterPosition instanceof DesktopRotationHolder) {
                IndividualPickerFragment.access$300(IndividualPickerFragment.this, (DesktopRotationHolder) findViewHolderForAdapterPosition);
                return;
            }
            IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
            individualPickerFragment.mHandler.postDelayed(individualPickerFragment.mUpdateDailyWallpaperThumbRunnable, 4000);
        }
    };

    /* loaded from: classes.dex */
    public class EmptySelectionAnimator implements SelectionAnimator {
        public EmptySelectionAnimator(IndividualPickerFragment individualPickerFragment) {
        }
    }

    /* loaded from: classes.dex */
    public class GridPaddingDecoration extends RecyclerView.ItemDecoration {
        public final int mPaddingBottom;
        public final int mPaddingHorizontal;

        public GridPaddingDecoration(IndividualPickerFragment individualPickerFragment, int i, int i2) {
            this.mPaddingHorizontal = i;
            this.mPaddingBottom = i2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getChildAdapterPosition(view) >= 0) {
                int i = this.mPaddingHorizontal;
                rect.left = i;
                rect.right = i;
                rect.bottom = this.mPaddingBottom;
            }
        }
    }

    /* loaded from: classes.dex */
    public class IndividualAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public int mPendingSelectedAdapterPosition = -1;
        public int mSelectedAdapterPosition = -1;
        public final List<WallpaperInfo> mWallpapers;

        public IndividualAdapter(List<WallpaperInfo> list) {
            this.mWallpapers = list;
        }

        public static void access$200(IndividualAdapter individualAdapter, int i) {
            if (individualAdapter.mPendingSelectedAdapterPosition != individualAdapter.mSelectedAdapterPosition) {
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                CurrentWallpaperBottomSheetPresenter currentWallpaperBottomSheetPresenter = individualPickerFragment.mCurrentWallpaperBottomSheetPresenter;
                if (currentWallpaperBottomSheetPresenter != null) {
                    ((TopLevelPickerActivity) currentWallpaperBottomSheetPresenter).refreshCurrentWallpapers(individualPickerFragment);
                    IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                    Runnable runnable = individualPickerFragment2.mCurrentWallpaperBottomSheetExpandedRunnable;
                    if (runnable != null) {
                        individualPickerFragment2.mHandler.removeCallbacks(runnable);
                    }
                    IndividualPickerFragment individualPickerFragment3 = IndividualPickerFragment.this;
                    AnonymousClass2 r1 = new Runnable() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter.2
                        @Override // java.lang.Runnable
                        public void run() {
                            ((TopLevelPickerActivity) IndividualPickerFragment.this.mCurrentWallpaperBottomSheetPresenter).setCurrentWallpapersExpanded(true);
                        }
                    };
                    individualPickerFragment3.mCurrentWallpaperBottomSheetExpandedRunnable = r1;
                    individualPickerFragment3.mHandler.postDelayed(r1, 100);
                }
                if (IndividualPickerFragment.this.getActivity() != null) {
                    int i2 = individualAdapter.mSelectedAdapterPosition;
                    boolean z = false;
                    if (i2 >= 0) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = IndividualPickerFragment.this.mImageGrid.findViewHolderForAdapterPosition(i2);
                        if (findViewHolderForAdapterPosition instanceof SelectableHolder) {
                            ((SelectableHolder) findViewHolderForAdapterPosition).setSelectionState(0);
                        }
                    }
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = IndividualPickerFragment.this.mImageGrid.findViewHolderForAdapterPosition(i);
                    if (findViewHolderForAdapterPosition2 instanceof SelectableHolder) {
                        ((SelectableHolder) findViewHolderForAdapterPosition2).setSelectionState(2);
                    }
                    individualAdapter.mSelectedAdapterPosition = i;
                    int i3 = ((GridLayoutManager) IndividualPickerFragment.this.mImageGrid.getLayoutManager()).mSpanCount;
                    if (i / i3 == ((int) Math.ceil((double) (((float) individualAdapter.getItemCount()) / ((float) i3)))) - 1) {
                        z = true;
                    }
                    IndividualPickerFragment.this.updateImageGridPadding(z);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (!IndividualPickerFragment.this.shouldShowRotationTile()) {
                WallpaperCategory wallpaperCategory = IndividualPickerFragment.this.mCategory;
                Objects.requireNonNull(wallpaperCategory);
                if (!(wallpaperCategory instanceof DesktopCustomCategory)) {
                    return this.mWallpapers.size();
                }
            }
            return this.mWallpapers.size() + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (IndividualPickerFragment.this.shouldShowRotationTile() && i == 0) {
                return 1;
            }
            WallpaperCategory wallpaperCategory = IndividualPickerFragment.this.mCategory;
            Objects.requireNonNull(wallpaperCategory);
            return (!(wallpaperCategory instanceof DesktopCustomCategory) || IndividualPickerFragment.this.isRotationEnabled() || i != 0) ? 2 : 3;
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x003d  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0056  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x005a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onBindIndividualHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder r4, int r5) {
            /*
                r3 = this;
                com.android.wallpaper.picker.individual.IndividualPickerFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                boolean r0 = r0.shouldShowRotationTile()
                if (r0 != 0) goto L_0x0016
                com.android.wallpaper.picker.individual.IndividualPickerFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                com.android.wallpaper.model.WallpaperCategory r0 = r0.mCategory
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0 instanceof com.android.wallpaper.model.DesktopCustomCategory
                if (r0 == 0) goto L_0x0014
                goto L_0x0016
            L_0x0014:
                r0 = r5
                goto L_0x0018
            L_0x0016:
                int r0 = r5 + -1
            L_0x0018:
                java.util.List<com.android.wallpaper.model.WallpaperInfo> r1 = r3.mWallpapers
                java.lang.Object r0 = r1.get(r0)
                com.android.wallpaper.model.WallpaperInfo r0 = (com.android.wallpaper.model.WallpaperInfo) r0
                android.view.View r1 = r4.itemView
                android.content.Context r1 = r1.getContext()
                r0.computePlaceholderColor(r1)
                r1 = r4
                com.android.wallpaper.picker.individual.IndividualHolder r1 = (com.android.wallpaper.picker.individual.IndividualHolder) r1
                r1.bindWallpaper(r0)
                com.android.wallpaper.picker.individual.IndividualPickerFragment r1 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                java.util.Set<java.lang.String> r1 = r1.mAppliedWallpaperIds
                java.lang.String r2 = r0.getWallpaperId()
                boolean r1 = r1.contains(r2)
                if (r1 == 0) goto L_0x0043
                r3.mSelectedAdapterPosition = r5
                com.android.wallpaper.picker.individual.IndividualPickerFragment r5 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                r5.mAppliedWallpaperInfo = r0
            L_0x0043:
                android.view.View r5 = r4.itemView
                r0 = 2131362436(0x7f0a0284, float:1.8344653E38)
                android.view.View r5 = r5.findViewById(r0)
                androidx.cardview.widget.CardView r5 = (androidx.cardview.widget.CardView) r5
                com.android.wallpaper.picker.individual.IndividualPickerFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                boolean r0 = r0.isFewerColumnLayout()
                if (r0 == 0) goto L_0x005a
                r0 = 2131165432(0x7f0700f8, float:1.794508E38)
                goto L_0x005d
            L_0x005a:
                r0 = 2131165433(0x7f0700f9, float:1.7945083E38)
            L_0x005d:
                com.android.wallpaper.picker.individual.IndividualPickerFragment r2 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                android.content.res.Resources r2 = r2.getResources()
                float r0 = r2.getDimension(r0)
                r5.setRadius(r0)
                r5 = 2131230987(0x7f08010b, float:1.8078042E38)
                r3.showBadge(r4, r5, r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter.onBindIndividualHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }

        public void onBindRotationHolder(RecyclerView.ViewHolder viewHolder, int i) {
            IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
            if (individualPickerFragment.mFormFactor == 0) {
                String str = individualPickerFragment.mCategory.mCollectionId;
                DesktopRotationHolder desktopRotationHolder = (DesktopRotationHolder) viewHolder;
                if (desktopRotationHolder.mWallpaperPreferences.getWallpaperPresentationMode() != 2 || !str.equals(desktopRotationHolder.mWallpaperPreferences.getHomeWallpaperCollectionId())) {
                    Objects.requireNonNull(desktopRotationHolder.mSelectionAnimator);
                } else {
                    Objects.requireNonNull(desktopRotationHolder.mSelectionAnimator);
                }
                if (IndividualPickerFragment.this.mWallpaperPreferences.getWallpaperPresentationMode() == 2 && str.equals(IndividualPickerFragment.this.mWallpaperPreferences.getHomeWallpaperCollectionId())) {
                    this.mSelectedAdapterPosition = i;
                }
                if (!IndividualPickerFragment.this.mWasUpdateRunnableRun && !this.mWallpapers.isEmpty()) {
                    IndividualPickerFragment.access$300(IndividualPickerFragment.this, desktopRotationHolder);
                    IndividualPickerFragment.this.mWasUpdateRunnableRun = true;
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 1) {
                onBindRotationHolder(viewHolder, i);
            } else if (itemViewType == 2) {
                onBindIndividualHolder(viewHolder, i);
            } else if (itemViewType != 3) {
                Log.e("IndividualPickerFrgmnt", "Unsupported viewType " + itemViewType + " in IndividualAdapter");
            } else {
                ((MyPhotosViewHolder) viewHolder).bind();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 1) {
                View inflate = LayoutInflater.from(IndividualPickerFragment.this.getActivity()).inflate(R.layout.grid_item_rotation_desktop, viewGroup, false);
                EmptySelectionAnimator emptySelectionAnimator = new EmptySelectionAnimator(IndividualPickerFragment.this);
                FragmentActivity activity = IndividualPickerFragment.this.getActivity();
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                return new DesktopRotationHolder(activity, individualPickerFragment.mTileSizePx.y, inflate, emptySelectionAnimator, individualPickerFragment);
            } else if (i == 2) {
                View inflate2 = LayoutInflater.from(IndividualPickerFragment.this.getActivity()).inflate(R.layout.grid_item_image, viewGroup, false);
                IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                if (individualPickerFragment2.mFormFactor != 0) {
                    return new PreviewIndividualHolder(IndividualPickerFragment.this.getActivity(), IndividualPickerFragment.this.mTileSizePx.y, inflate2);
                }
                return new SetIndividualHolder(IndividualPickerFragment.this.getActivity(), IndividualPickerFragment.this.mTileSizePx.y, inflate2, new EmptySelectionAnimator(individualPickerFragment2), new SetIndividualHolder.OnSetListener() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter.1
                });
            } else if (i != 3) {
                Log.e("IndividualPickerFrgmnt", "Unsupported viewType " + i + " in IndividualAdapter");
                return null;
            } else {
                return new MyPhotosViewHolder(IndividualPickerFragment.this.getActivity(), ((MyPhotosStarter.MyPhotosStarterProvider) IndividualPickerFragment.this.getActivity()).getMyPhotosStarter(), IndividualPickerFragment.this.mTileSizePx.y, LayoutInflater.from(IndividualPickerFragment.this.getActivity()).inflate(R.layout.grid_item_my_photos, viewGroup, false));
            }
        }

        public void showBadge(RecyclerView.ViewHolder viewHolder, int i, boolean z) {
            float f;
            ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.indicator_icon);
            if (z) {
                if (IndividualPickerFragment.this.isFewerColumnLayout()) {
                    f = IndividualPickerFragment.this.getResources().getDimension(R.dimen.grid_item_badge_margin);
                } else {
                    f = IndividualPickerFragment.this.getResources().getDimension(R.dimen.grid_item_badge_margin_small);
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                int i2 = (int) f;
                layoutParams.setMargins(i2, i2, i2, i2);
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(i);
                imageView.setVisibility(0);
                return;
            }
            imageView.setVisibility(8);
        }
    }

    /* loaded from: classes.dex */
    public interface IndividualPickerFragmentHost {
        boolean isHostToolbarShown();

        void moveToPreviousFragment();

        void removeToolbarMenu();

        void setToolbarMenu(int i);

        void setToolbarTitle(CharSequence charSequence);
    }

    public static void access$1000(IndividualPickerFragment individualPickerFragment, int i) {
        FragmentTransactionChecker fragmentTransactionChecker = (FragmentTransactionChecker) individualPickerFragment.getActivity();
        if (fragmentTransactionChecker != null) {
            StartRotationErrorDialogFragment startRotationErrorDialogFragment = new StartRotationErrorDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("network_preference", i);
            startRotationErrorDialogFragment.setArguments(bundle);
            startRotationErrorDialogFragment.setTargetFragment(individualPickerFragment, 1);
            if (fragmentTransactionChecker.isSafeToCommitFragmentTransaction()) {
                startRotationErrorDialogFragment.show(individualPickerFragment.mFragmentManager, "start_rotation_error_dialog");
            } else {
                individualPickerFragment.mStagedStartRotationErrorDialogFragment = startRotationErrorDialogFragment;
            }
        }
    }

    public static void access$300(IndividualPickerFragment individualPickerFragment, DesktopRotationHolder desktopRotationHolder) {
        individualPickerFragment.mWallpapers.get(individualPickerFragment.mRandom.nextInt(individualPickerFragment.mWallpapers.size())).getThumbAsset(individualPickerFragment.getActivity()).loadDrawableWithTransition(desktopRotationHolder.mActivity, desktopRotationHolder.mThumbnailView, desktopRotationHolder.mThumbnailView.getDrawable() == null ? 300 : RecyclerView.MAX_SCROLL_DURATION, new Asset.DrawableLoadedListener() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.3
            @Override // com.android.wallpaper.asset.Asset.DrawableLoadedListener
            public void onDrawableLoaded() {
                if (IndividualPickerFragment.this.getActivity() != null) {
                    IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                    individualPickerFragment2.mHandler.postDelayed(individualPickerFragment2.mUpdateDailyWallpaperThumbRunnable, (long) 4000);
                }
            }
        }, R$attr.getColorAttr(desktopRotationHolder.mActivity, 16844080));
    }

    public void fetchWallpapers(boolean z) {
        this.mWallpapers.clear();
        this.mIsWallpapersReceived = false;
        updateLoading();
        this.mCategory.fetchWallpapers(getActivity().getApplicationContext(), new WallpaperReceiver() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.5
            @Override // com.android.wallpaper.model.WallpaperReceiver
            public void onWallpapersReceived(List<WallpaperInfo> list) {
                FragmentActivity activity;
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                individualPickerFragment.mIsWallpapersReceived = true;
                individualPickerFragment.updateLoading();
                for (WallpaperInfo wallpaperInfo : list) {
                    IndividualPickerFragment.this.mWallpapers.add(wallpaperInfo);
                }
                IndividualPickerFragment.this.maybeSetUpImageGrid();
                IndividualAdapter individualAdapter = IndividualPickerFragment.this.mAdapter;
                if (individualAdapter != null) {
                    individualAdapter.mObservable.notifyChanged();
                }
                WallpapersUiContainer wallpapersUiContainer = IndividualPickerFragment.this.mWallpapersUiContainer;
                if (wallpapersUiContainer != null) {
                    wallpapersUiContainer.onWallpapersReady();
                } else if (list.isEmpty() && (activity = IndividualPickerFragment.this.getActivity()) != null && IndividualPickerFragment.this.mFormFactor == 1) {
                    activity.finish();
                }
            }
        }, z);
    }

    public final IndividualPickerFragmentHost getIndividualPickerFragmentHost() {
        Fragment fragment = this.mParentFragment;
        if (fragment != null) {
            return (IndividualPickerFragmentHost) fragment;
        }
        return (IndividualPickerFragmentHost) getActivity();
    }

    public int getNumColumns() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return 1;
        }
        if (isFewerColumnLayout()) {
            return R$color.getNumColumns(activity, R$color.getActivityWindowWidthPx(activity), 2, 2);
        }
        return R$color.getNumColumns(activity, R$color.getActivityWindowWidthPx(activity), 3, 4);
    }

    public void highlightAppliedWallpaper(int i) {
        String str;
        this.mWallpaperDestination = i;
        if (this.mWallpapers != null) {
            boolean z = false;
            showCheckMarkAndBorderForAppliedWallpaper(false);
            WallpaperPreferences preferences = InjectorProvider.getInjector().getPreferences(getContext());
            android.app.WallpaperInfo wallpaperInfo = this.mWallpaperManager.getWallpaperInfo();
            if (this.mWallpaperManager.getWallpaperId(2) < 0) {
                z = true;
            }
            if (z || this.mWallpaperDestination == 0) {
                str = wallpaperInfo != null ? wallpaperInfo.getServiceName() : preferences.getHomeWallpaperRemoteId();
            } else {
                str = preferences.getLockWallpaperRemoteId();
            }
            this.mAppliedWallpaperInfo = this.mWallpapers.stream().filter(IndividualPickerFragment$$ExternalSyntheticLambda1.INSTANCE).filter(new GridFragment$$ExternalSyntheticLambda1(str)).findFirst().orElse(null);
            showCheckMarkAndBorderForAppliedWallpaper(true);
        }
    }

    public boolean isFewerColumnLayout() {
        List<WallpaperInfo> list = this.mWallpapers;
        return list != null && list.size() <= 8;
    }

    public boolean isRotationEnabled() {
        return this.mWallpaperRotationInitializer != null;
    }

    public void maybeSetUpImageGrid() {
        int i;
        int i2;
        int i3;
        Point point;
        if (!(this.mImageGrid == null || this.mCategory == null || getContext() == null)) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) this.mImageGrid.getLayoutManager();
            boolean z = (gridLayoutManager == null || gridLayoutManager.mSpanCount == getNumColumns()) ? false : true;
            if (this.mAdapter == null || z) {
                int itemDecorationCount = this.mImageGrid.getItemDecorationCount();
                for (int i4 = 0; i4 < itemDecorationCount; i4++) {
                    this.mImageGrid.removeItemDecorationAt(i4);
                }
                RecyclerView recyclerView = this.mImageGrid;
                if (isFewerColumnLayout()) {
                    i = getResources().getDimensionPixelSize(R.dimen.grid_item_featured_individual_padding_horizontal);
                } else {
                    i = getResources().getDimensionPixelSize(R.dimen.grid_item_individual_padding_horizontal);
                }
                if (isFewerColumnLayout()) {
                    i2 = getResources().getDimensionPixelSize(R.dimen.grid_item_featured_individual_padding_bottom);
                } else {
                    i2 = getResources().getDimensionPixelSize(R.dimen.grid_item_individual_padding_bottom);
                }
                recyclerView.addItemDecoration(new GridPaddingDecoration(this, i, i2));
                if (isFewerColumnLayout()) {
                    i3 = getResources().getDimensionPixelSize(R.dimen.featured_wallpaper_grid_edge_space);
                } else {
                    i3 = getResources().getDimensionPixelSize(R.dimen.wallpaper_grid_edge_space);
                }
                RecyclerView recyclerView2 = this.mImageGrid;
                recyclerView2.setPadding(i3, recyclerView2.getPaddingTop(), i3, this.mImageGrid.getPaddingBottom());
                if (isFewerColumnLayout()) {
                    FragmentActivity activity = getActivity();
                    Resources resources = activity.getResources();
                    int activityWindowWidthPx = R$color.getActivityWindowWidthPx(activity);
                    point = R$color.getSquareTileSize(R$color.getNumColumns(activity, activityWindowWidthPx, 2, 2), activityWindowWidthPx, resources.getDimensionPixelSize(R.dimen.grid_item_featured_individual_padding_horizontal), resources.getDimensionPixelSize(R.dimen.featured_wallpaper_grid_edge_space));
                } else {
                    FragmentActivity activity2 = getActivity();
                    Resources resources2 = activity2.getResources();
                    int activityWindowWidthPx2 = R$color.getActivityWindowWidthPx(activity2);
                    point = R$color.getSquareTileSize(R$color.getNumColumns(activity2, activityWindowWidthPx2, 3, 4), activityWindowWidthPx2, resources2.getDimensionPixelSize(R.dimen.grid_item_individual_padding_horizontal), resources2.getDimensionPixelSize(R.dimen.wallpaper_grid_edge_space));
                }
                this.mTileSizePx = point;
                setUpImageGrid();
                this.mImageGrid.setAccessibilityDelegateCompat(new WallpaperPickerRecyclerViewAccessibilityDelegate(this.mImageGrid, (WallpaperPickerRecyclerViewAccessibilityDelegate.BottomSheetHost) this.mParentFragment, getNumColumns()));
            }
        }
    }

    public void onCategoryLoaded() {
        if (getIndividualPickerFragmentHost() != null) {
            if (getIndividualPickerFragmentHost().isHostToolbarShown()) {
                getIndividualPickerFragmentHost().setToolbarTitle(this.mCategory.mTitle);
            } else {
                setTitle(this.mCategory.mTitle);
            }
            this.mWallpaperRotationInitializer = this.mCategory.getWallpaperRotationInitializer();
            if (this.mToolbar != null && isRotationEnabled()) {
                setUpToolbarMenu(R.menu.individual_picker_menu);
            }
            fetchWallpapers(false);
            WallpaperCategory wallpaperCategory = this.mCategory;
            Objects.requireNonNull(wallpaperCategory);
            if (wallpaperCategory instanceof ThirdPartyLiveWallpaperCategory) {
                PreviewPager$$ExternalSyntheticLambda1 previewPager$$ExternalSyntheticLambda1 = new PreviewPager$$ExternalSyntheticLambda1(this);
                this.mAppStatusListener = previewPager$$ExternalSyntheticLambda1;
                ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(previewPager$$ExternalSyntheticLambda1, "android.service.wallpaper.WallpaperService");
            }
        }
    }

    @Override // com.android.wallpaper.picker.SetWallpaperErrorDialogFragment.Listener
    public void onClickTryAgain(int i) {
        SetIndividualHolder setIndividualHolder = this.mPendingSetIndividualHolder;
        if (setIndividualHolder != null) {
            setIndividualHolder.setWallpaper();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Injector injector = InjectorProvider.getInjector();
        Context applicationContext = getContext().getApplicationContext();
        this.mWallpaperPreferences = injector.getPreferences(applicationContext);
        WallpaperChangedNotifier instance = WallpaperChangedNotifier.getInstance();
        this.mWallpaperChangedNotifier = instance;
        WallpaperChangedNotifier.Listener listener = this.mWallpaperChangedListener;
        if (!instance.mListeners.contains(listener)) {
            instance.mListeners.add(listener);
        }
        this.mWallpaperManager = WallpaperManager.getInstance(applicationContext);
        Objects.requireNonNull(injector.getFormFactorChecker(applicationContext));
        this.mFormFactor = 1;
        this.mPackageStatusNotifier = injector.getPackageStatusNotifier(applicationContext);
        this.mUserEventLogger = injector.getUserEventLogger(applicationContext);
        WallpaperPersister wallpaperPersister = injector.getWallpaperPersister(applicationContext);
        this.mWallpaperPersister = wallpaperPersister;
        this.mWallpaperSetter = new WallpaperSetter(wallpaperPersister, injector.getPreferences(applicationContext), injector.getUserEventLogger(applicationContext), false);
        this.mWallpapers = new ArrayList();
        this.mRandom = new Random();
        this.mHandler = new Handler();
        if (!(bundle == null || bundle.getInt("IndividualPickerFragment.NIGHT_MODE") == (getResources().getConfiguration().uiMode & 48))) {
            Glide.get(getContext()).clearMemory();
        }
        CategoryProvider categoryProvider = injector.getCategoryProvider(applicationContext);
        this.mCategoryProvider = categoryProvider;
        ((DefaultCategoryProvider) categoryProvider).fetchCategories(new CategoryReceiver() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.4
            @Override // com.android.wallpaper.model.CategoryReceiver
            public void doneFetchingCategories() {
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                Category category = ((DefaultCategoryProvider) individualPickerFragment.mCategoryProvider).getCategory(individualPickerFragment.mArguments.getString("category_collection_id"));
                if (category == null || (category instanceof WallpaperCategory)) {
                    IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                    WallpaperCategory wallpaperCategory = (WallpaperCategory) category;
                    individualPickerFragment2.mCategory = wallpaperCategory;
                    if (wallpaperCategory == null) {
                        DiskBasedLogger.e("IndividualPickerFrgmnt", "Failed to find the category.", individualPickerFragment2.getContext());
                        IndividualPickerFragment.this.getIndividualPickerFragmentHost().moveToPreviousFragment();
                        Toast.makeText(IndividualPickerFragment.this.getContext(), (int) R.string.collection_not_exist_msg, 0).show();
                        return;
                    }
                    individualPickerFragment2.onCategoryLoaded();
                }
            }

            @Override // com.android.wallpaper.model.CategoryReceiver
            public void onCategoryReceived(Category category) {
            }
        }, false);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String str;
        View inflate = layoutInflater.inflate(R.layout.fragment_individual_picker, viewGroup, false);
        boolean z = true;
        if (getIndividualPickerFragmentHost().isHostToolbarShown()) {
            inflate.findViewById(R.id.header_bar).setVisibility(8);
            this.mUpArrowEnabled = true;
            if (isRotationEnabled()) {
                getIndividualPickerFragmentHost().setToolbarMenu(R.menu.individual_picker_menu);
            }
        } else {
            setUpToolbar(inflate, true);
            if (isRotationEnabled()) {
                setUpToolbarMenu(R.menu.individual_picker_menu);
            }
            WallpaperCategory wallpaperCategory = this.mCategory;
            if (wallpaperCategory != null) {
                setTitle(wallpaperCategory.mTitle);
            }
        }
        WallpaperPreferences preferences = InjectorProvider.getInjector().getPreferences(getContext());
        android.app.WallpaperInfo wallpaperInfo = this.mWallpaperManager.getWallpaperInfo();
        ArraySet arraySet = new ArraySet();
        if (wallpaperInfo != null) {
            str = wallpaperInfo.getServiceName();
        } else {
            str = preferences.getHomeWallpaperRemoteId();
        }
        if (!TextUtils.isEmpty(str)) {
            arraySet.add(str);
        }
        if (this.mWallpaperManager.getWallpaperId(2) < 0) {
            z = false;
        }
        String lockWallpaperRemoteId = preferences.getLockWallpaperRemoteId();
        if (z && !TextUtils.isEmpty(lockWallpaperRemoteId)) {
            arraySet.add(lockWallpaperRemoteId);
        }
        this.mAppliedWallpaperIds = arraySet;
        this.mImageGrid = (RecyclerView) inflate.findViewById(R.id.wallpaper_grid);
        if (this.mFormFactor == 0) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.grid_padding_desktop);
            updateImageGridPadding(false);
            this.mImageGrid.setScrollBarSize(dimensionPixelSize);
        }
        this.mLoading = (ContentLoadingProgressBar) inflate.findViewById(R.id.loading_indicator);
        updateLoading();
        maybeSetUpImageGrid();
        this.mImageGrid.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, final int i2) {
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                if (individualPickerFragment.mCurrentWallpaperBottomSheetPresenter != null) {
                    Runnable runnable = individualPickerFragment.mCurrentWallpaperBottomSheetExpandedRunnable;
                    if (runnable != null) {
                        individualPickerFragment.mHandler.removeCallbacks(runnable);
                    }
                    IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                    AnonymousClass1 r4 = new Runnable() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (i2 > 0) {
                                ((TopLevelPickerActivity) IndividualPickerFragment.this.mCurrentWallpaperBottomSheetPresenter).setCurrentWallpapersExpanded(false);
                            } else {
                                ((TopLevelPickerActivity) IndividualPickerFragment.this.mCurrentWallpaperBottomSheetPresenter).setCurrentWallpapersExpanded(true);
                            }
                        }
                    };
                    individualPickerFragment2.mCurrentWallpaperBottomSheetExpandedRunnable = r4;
                    individualPickerFragment2.mHandler.postDelayed(r4, 100);
                }
            }
        });
        inflate.setOnApplyWindowInsetsListener(IndividualPickerFragment$$ExternalSyntheticLambda0.INSTANCE);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mCalled = true;
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        WallpaperChangedNotifier wallpaperChangedNotifier = this.mWallpaperChangedNotifier;
        wallpaperChangedNotifier.mListeners.remove(this.mWallpaperChangedListener);
        PackageStatusNotifier.Listener listener = this.mAppStatusListener;
        if (listener != null) {
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).removeListener(listener);
        }
        this.mWallpaperSetter.cleanUp();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mCalled = true;
        getIndividualPickerFragmentHost().removeToolbarMenu();
    }

    @Override // com.android.wallpaper.picker.AppbarFragment, android.widget.Toolbar.OnMenuItemClickListener
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.daily_rotation) {
            return false;
        }
        StartRotationDialogFragment startRotationDialogFragment = new StartRotationDialogFragment();
        startRotationDialogFragment.setTargetFragment(this, 1);
        startRotationDialogFragment.show(this.mFragmentManager, "start_rotation_dialog");
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        this.mCalled = true;
        InjectorProvider.getInjector().getPreferences(getActivity()).setLastAppActiveTimestamp(new Date().getTime());
        Glide.get(getActivity()).setMemoryCategory(MemoryCategory.NORMAL);
        StartRotationErrorDialogFragment startRotationErrorDialogFragment = this.mStagedStartRotationErrorDialogFragment;
        if (startRotationErrorDialogFragment != null) {
            startRotationErrorDialogFragment.show(this.mFragmentManager, "start_rotation_error_dialog");
            this.mStagedStartRotationErrorDialogFragment = null;
        }
        SetWallpaperErrorDialogFragment setWallpaperErrorDialogFragment = this.mStagedSetWallpaperErrorDialogFragment;
        if (setWallpaperErrorDialogFragment != null) {
            setWallpaperErrorDialogFragment.show(this.mFragmentManager, "individual_set_wallpaper_error_dialog");
            this.mStagedSetWallpaperErrorDialogFragment = null;
        }
        if (shouldShowRotationTile() && this.mWasUpdateRunnableRun && !this.mWallpapers.isEmpty()) {
            this.mUpdateDailyWallpaperThumbRunnable.run();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("IndividualPickerFragment.NIGHT_MODE", getResources().getConfiguration().uiMode & 48);
    }

    @Override // com.android.wallpaper.picker.SetWallpaperDialogFragment.Listener
    public void onSet(int i) {
        Log.e("IndividualPickerFrgmnt", "Unable to set wallpaper since the selected wallpaper info is null");
    }

    @Override // com.android.wallpaper.picker.StartRotationDialogFragment.Listener
    public void onStartRotationDialogDismiss(DialogInterface dialogInterface) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        this.mCalled = true;
        this.mHandler.removeCallbacks(this.mUpdateDailyWallpaperThumbRunnable);
    }

    @Override // com.android.wallpaper.picker.StartRotationErrorDialogFragment.Listener
    public void retryStartRotation(int i) {
        startRotation(i);
    }

    public void setUpImageGrid() {
        IndividualAdapter individualAdapter = new IndividualAdapter(this.mWallpapers);
        this.mAdapter = individualAdapter;
        this.mImageGrid.setAdapter(individualAdapter);
        this.mImageGrid.setLayoutManager(new GridLayoutManager(getActivity(), getNumColumns()));
    }

    public boolean shouldShowRotationTile() {
        return this.mFormFactor == 0 && isRotationEnabled();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004a, code lost:
        if ((r1 instanceof com.android.wallpaper.model.DesktopCustomCategory) != false) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0018, code lost:
        if ((r1 instanceof com.android.wallpaper.model.DesktopCustomCategory) != false) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void showCheckMarkAndBorderForAppliedWallpaper(boolean r4) {
        /*
            r3 = this;
            com.android.wallpaper.model.WallpaperInfo r0 = r3.mAppliedWallpaperInfo
            if (r0 != 0) goto L_0x0005
            goto L_0x0032
        L_0x0005:
            java.util.List<com.android.wallpaper.model.WallpaperInfo> r1 = r3.mWallpapers
            int r0 = r1.indexOf(r0)
            boolean r1 = r3.shouldShowRotationTile()
            if (r1 != 0) goto L_0x001a
            com.android.wallpaper.model.WallpaperCategory r1 = r3.mCategory
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1 instanceof com.android.wallpaper.model.DesktopCustomCategory
            if (r1 == 0) goto L_0x001c
        L_0x001a:
            int r0 = r0 + 1
        L_0x001c:
            androidx.recyclerview.widget.RecyclerView r1 = r3.mImageGrid
            androidx.recyclerview.widget.RecyclerView$ViewHolder r1 = r1.findViewHolderForAdapterPosition(r0)
            if (r1 == 0) goto L_0x002d
            com.android.wallpaper.picker.individual.IndividualPickerFragment$IndividualAdapter r0 = r3.mAdapter
            r2 = 2131230987(0x7f08010b, float:1.8078042E38)
            r0.showBadge(r1, r2, r4)
            goto L_0x0032
        L_0x002d:
            com.android.wallpaper.picker.individual.IndividualPickerFragment$IndividualAdapter r1 = r3.mAdapter
            r1.notifyItemChanged(r0)
        L_0x0032:
            com.android.wallpaper.model.WallpaperInfo r0 = r3.mAppliedWallpaperInfo
            if (r0 != 0) goto L_0x0037
            goto L_0x0061
        L_0x0037:
            java.util.List<com.android.wallpaper.model.WallpaperInfo> r1 = r3.mWallpapers
            int r0 = r1.indexOf(r0)
            boolean r1 = r3.shouldShowRotationTile()
            if (r1 != 0) goto L_0x004c
            com.android.wallpaper.model.WallpaperCategory r1 = r3.mCategory
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1 instanceof com.android.wallpaper.model.DesktopCustomCategory
            if (r1 == 0) goto L_0x004e
        L_0x004c:
            int r0 = r0 + 1
        L_0x004e:
            androidx.recyclerview.widget.RecyclerView r1 = r3.mImageGrid
            androidx.recyclerview.widget.RecyclerView$ViewHolder r1 = r1.findViewHolderForAdapterPosition(r0)
            if (r1 == 0) goto L_0x005c
            android.view.View r3 = r1.itemView
            r3.setActivated(r4)
            goto L_0x0061
        L_0x005c:
            com.android.wallpaper.picker.individual.IndividualPickerFragment$IndividualAdapter r3 = r3.mAdapter
            r3.notifyItemChanged(r0)
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.individual.IndividualPickerFragment.showCheckMarkAndBorderForAppliedWallpaper(boolean):void");
    }

    @Override // com.android.wallpaper.picker.RotationStarter
    public void startRotation(final int i) {
        if (!isRotationEnabled()) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Rotation is not enabled for this category ");
            m.append(this.mCategory.mTitle);
            Log.e("IndividualPickerFrgmnt", m.toString());
            return;
        }
        if (this.mFormFactor == 1) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.LightDialogTheme);
            this.mProgressDialog = progressDialog;
            progressDialog.setTitle((CharSequence) null);
            this.mProgressDialog.setMessage(getResources().getString(R.string.start_rotation_progress_message));
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.show();
        }
        if (this.mFormFactor == 0) {
            this.mAdapter.mPendingSelectedAdapterPosition = 0;
        }
        final Context applicationContext = getActivity().getApplicationContext();
        this.mWallpaperRotationInitializer.setFirstWallpaperInRotation(applicationContext, i, new WallpaperRotationInitializer.Listener() { // from class: com.android.wallpaper.picker.individual.IndividualPickerFragment.7
            @Override // com.android.wallpaper.model.WallpaperRotationInitializer.Listener
            public void onError() {
                ProgressDialog progressDialog2 = IndividualPickerFragment.this.mProgressDialog;
                if (progressDialog2 != null) {
                    progressDialog2.dismiss();
                }
                IndividualPickerFragment.access$1000(IndividualPickerFragment.this, i);
                IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                if (individualPickerFragment.mFormFactor == 0) {
                    ((DesktopRotationHolder) individualPickerFragment.mImageGrid.findViewHolderForAdapterPosition(0)).setSelectionState(0);
                }
            }

            @Override // com.android.wallpaper.model.WallpaperRotationInitializer.Listener
            public void onFirstWallpaperInRotationSet() {
                ProgressDialog progressDialog2 = IndividualPickerFragment.this.mProgressDialog;
                if (progressDialog2 != null) {
                    progressDialog2.dismiss();
                }
                FragmentActivity activity = IndividualPickerFragment.this.getActivity();
                if (IndividualPickerFragment.this.mWallpaperRotationInitializer.startRotation(applicationContext)) {
                    if (activity != null) {
                        IndividualPickerFragment individualPickerFragment = IndividualPickerFragment.this;
                        if (individualPickerFragment.mFormFactor == 1) {
                            try {
                                Toast.makeText(individualPickerFragment.getActivity(), (int) R.string.wallpaper_set_successfully_message, 0).show();
                            } catch (Resources.NotFoundException e) {
                                Log.e("IndividualPickerFrgmnt", "Could not show toast " + e);
                            }
                            activity.setResult(-1);
                            activity.finish();
                            return;
                        }
                    }
                    IndividualPickerFragment individualPickerFragment2 = IndividualPickerFragment.this;
                    if (individualPickerFragment2.mFormFactor == 0) {
                        IndividualAdapter.access$200(individualPickerFragment2.mAdapter, 0);
                        return;
                    }
                    return;
                }
                IndividualPickerFragment.access$1000(IndividualPickerFragment.this, i);
                IndividualPickerFragment individualPickerFragment3 = IndividualPickerFragment.this;
                if (individualPickerFragment3.mFormFactor == 0) {
                    ((DesktopRotationHolder) individualPickerFragment3.mImageGrid.findViewHolderForAdapterPosition(0)).setSelectionState(0);
                }
            }
        });
    }

    public void updateImageGridPadding(boolean z) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.grid_padding_desktop);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.current_wallpaper_bottom_sheet_layout_height);
        if (!z) {
            dimensionPixelSize2 = 0;
        }
        this.mImageGrid.setPadding(dimensionPixelSize, dimensionPixelSize, 0, dimensionPixelSize2);
    }

    public void updateLoading() {
        ContentLoadingProgressBar contentLoadingProgressBar = this.mLoading;
        if (contentLoadingProgressBar != null) {
            if (this.mIsWallpapersReceived) {
                contentLoadingProgressBar.hide();
            } else {
                contentLoadingProgressBar.show();
            }
        }
    }
}
