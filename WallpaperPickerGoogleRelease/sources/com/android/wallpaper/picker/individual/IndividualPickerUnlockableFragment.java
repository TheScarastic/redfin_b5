package com.android.wallpaper.picker.individual;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.customization.picker.grid.GridFragment$$ExternalSyntheticLambda1;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultPackageStatusNotifier;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class IndividualPickerUnlockableFragment extends IndividualPickerFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public CategoryProvider mCategoryProvider;
    public List<WallpaperInfo> mDownloadableWallpapers = new ArrayList();
    public Injector mInjector;

    /* loaded from: classes.dex */
    public class IndividualUnlockAdapter extends IndividualPickerFragment.IndividualAdapter {
        public IndividualUnlockAdapter(List<WallpaperInfo> list) {
            super(list);
        }

        public final RecyclerView.ViewHolder createTitleHolder(ViewGroup viewGroup, boolean z) {
            View inflate = LayoutInflater.from(IndividualPickerUnlockableFragment.this.getActivity()).inflate(R.layout.grid_item_unlock_additionals_header, viewGroup, false);
            if (z) {
                inflate.setPadding(inflate.getPaddingStart(), 0, inflate.getPaddingEnd(), inflate.getPaddingBottom());
            }
            return new UnlockAdditionalsHeaderHolder(inflate);
        }

        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            int itemViewType = super.getItemViewType(i);
            if (i >= IndividualPickerUnlockableFragment.this.mWallpapers.size() || !TextUtils.equals("unlock_additionals_header", IndividualPickerUnlockableFragment.this.mWallpapers.get(i).getCollectionId(IndividualPickerUnlockableFragment.this.getContext()))) {
                return itemViewType;
            }
            return i == 0 ? 5 : 4;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0014, code lost:
            if ((r0 instanceof com.android.wallpaper.model.DesktopCustomCategory) != false) goto L_0x0016;
         */
        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onBindIndividualHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder r3, int r4) {
            /*
                r2 = this;
                super.onBindIndividualHolder(r3, r4)
                com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.this
                boolean r0 = r0.shouldShowRotationTile()
                if (r0 != 0) goto L_0x0016
                com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.this
                com.android.wallpaper.model.WallpaperCategory r0 = r0.mCategory
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0 instanceof com.android.wallpaper.model.DesktopCustomCategory
                if (r0 == 0) goto L_0x0018
            L_0x0016:
                int r4 = r4 + -1
            L_0x0018:
                com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.this
                java.util.List<com.android.wallpaper.model.WallpaperInfo> r0 = r0.mWallpapers
                java.lang.Object r4 = r0.get(r4)
                com.android.wallpaper.model.WallpaperInfo r4 = (com.android.wallpaper.model.WallpaperInfo) r4
                com.android.wallpaper.picker.individual.IndividualPickerFragment r0 = com.android.wallpaper.picker.individual.IndividualPickerFragment.this
                java.util.Set<java.lang.String> r0 = r0.mAppliedWallpaperIds
                java.lang.String r1 = r4.getWallpaperId()
                boolean r0 = r0.contains(r1)
                if (r0 != 0) goto L_0x0038
                r0 = 2131230888(0x7f0800a8, float:1.8077841E38)
                boolean r4 = r4 instanceof com.android.wallpaper.model.DownloadableLiveWallpaperInfo
                r2.showBadge(r3, r0, r4)
            L_0x0038:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.IndividualUnlockAdapter.onBindIndividualHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }

        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 1) {
                onBindRotationHolder(viewHolder, i);
            } else if (itemViewType == 2) {
                onBindIndividualHolder(viewHolder, i);
            } else if (itemViewType == 3) {
                ((MyPhotosViewHolder) viewHolder).bind();
            } else if (itemViewType != 4 && itemViewType != 5) {
                Log.e("IndividualPickerUnlockableFrgmnt", "Unsupported viewType " + itemViewType + " in IndividualAdapter");
            }
        }

        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            RecyclerView.ViewHolder onCreateViewHolder = super.onCreateViewHolder(viewGroup, i);
            if (onCreateViewHolder == null) {
                if (i == 4) {
                    return createTitleHolder(viewGroup, false);
                }
                if (i == 5) {
                    return createTitleHolder(viewGroup, true);
                }
                Log.e("IndividualPickerUnlockableFrgmnt", "Unsupported viewType " + i + " in IndividualAdapter");
            }
            return onCreateViewHolder;
        }
    }

    /* loaded from: classes.dex */
    public static class UnlockAdditionalsHeaderHolder extends RecyclerView.ViewHolder {
        public UnlockAdditionalsHeaderHolder(View view) {
            super(view);
        }
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment
    public void fetchWallpapers(final boolean z) {
        CategoryProvider categoryProvider = this.mCategoryProvider;
        if (categoryProvider == null) {
            Log.w("IndividualPickerUnlockableFrgmnt", "fetchWallpapers with null category provider");
            return;
        }
        ((DefaultCategoryProvider) categoryProvider).fetchCategories(new CategoryReceiver() { // from class: com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.1
            @Override // com.android.wallpaper.model.CategoryReceiver
            public void doneFetchingCategories() {
            }

            @Override // com.android.wallpaper.model.CategoryReceiver
            public void onCategoryReceived(Category category) {
                if (TextUtils.equals(category.mCollectionId, IndividualPickerUnlockableFragment.this.mCategory.mCollectionId) && (category instanceof WallpaperCategory)) {
                    IndividualPickerUnlockableFragment.this.mWallpapers.clear();
                    IndividualPickerUnlockableFragment.this.mDownloadableWallpapers.clear();
                    IndividualPickerUnlockableFragment individualPickerUnlockableFragment = IndividualPickerUnlockableFragment.this;
                    individualPickerUnlockableFragment.mIsWallpapersReceived = false;
                    individualPickerUnlockableFragment.updateLoading();
                    ((WallpaperCategory) category).fetchWallpapers(IndividualPickerUnlockableFragment.this.getContext(), new IndividualPickerActivity$1$$ExternalSyntheticLambda0(this), z);
                }
            }
        }, z);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment
    public boolean isFewerColumnLayout() {
        Context context;
        if (this.mWallpapers == null || (context = getContext()) == null || this.mWallpapers.stream().filter(new GridFragment$$ExternalSyntheticLambda1(context)).count() > 8) {
            return false;
        }
        return true;
    }

    @Override // com.android.wallpaper.picker.AppbarFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mCategoryProvider = InjectorProvider.getInjector().getCategoryProvider(getActivity());
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment
    public void onCategoryLoaded() {
        super.onCategoryLoaded();
        String str = this.mCategory.mCollectionId;
        if (str != null && str.contains("nexus_live_category")) {
            PreviewPager$$ExternalSyntheticLambda1 previewPager$$ExternalSyntheticLambda1 = new PreviewPager$$ExternalSyntheticLambda1(this);
            this.mAppStatusListener = previewPager$$ExternalSyntheticLambda1;
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(previewPager$$ExternalSyntheticLambda1, "android.service.wallpaper.WallpaperService");
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(this.mAppStatusListener, "com.google.pixel.livewallpaper.action.DOWNLOAD_LIVE_WALLPAPER");
        }
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment
    public void setUpImageGrid() {
        IndividualUnlockAdapter individualUnlockAdapter = new IndividualUnlockAdapter(this.mWallpapers);
        this.mAdapter = individualUnlockAdapter;
        this.mImageGrid.setAdapter(individualUnlockAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getNumColumns());
        gridLayoutManager.mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() { // from class: com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment.3
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                int itemViewType = IndividualPickerUnlockableFragment.this.mAdapter.getItemViewType(i);
                if (itemViewType == 1 || itemViewType == 2 || itemViewType == 3) {
                    return 1;
                }
                if (itemViewType == 4 || itemViewType == 5) {
                    return gridLayoutManager.mSpanCount;
                }
                return -1;
            }
        };
        this.mImageGrid.setLayoutManager(gridLayoutManager);
    }
}
