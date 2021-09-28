package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.view.R$id;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.picker.MyPhotosStarter;
import com.android.wallpaper.picker.PreviewActivity;
import com.android.wallpaper.util.DisplayMetricsRetriever;
import com.android.wallpaper.widget.WallpaperPickerRecyclerViewAccessibilityDelegate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class CategorySelectorFragment extends AppbarFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public CategoryAdapter mAdapter;
    public boolean mAwaitingCategories;
    public ArrayList<Category> mCategories;
    public final CategoryProvider mCategoryProvider = InjectorProvider.getInjector().getCategoryProvider(getContext());
    public RecyclerView mImageGrid;
    public boolean mIsFeaturedCollectionAvailable;
    public Point mTileSizePx;

    /* loaded from: classes.dex */
    public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MyPhotosStarter.PermissionChangedListener {
        public List<Category> mCategories;

        public CategoryAdapter(List list, AnonymousClass1 r3) {
            this.mCategories = list;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.mCategories.size() + 0;
            return CategorySelectorFragment.this.mAwaitingCategories ? size + 1 : size;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (CategorySelectorFragment.this.mAwaitingCategories && i == getItemCount() - 1) {
                return 4;
            }
            if (i == 0) {
                return 1;
            }
            if (CategorySelectorFragment.this.mIsFeaturedCollectionAvailable) {
                return (i == 1 || i == 2) ? 2 : 3;
            }
            return 3;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType == 1 || itemViewType == 2 || itemViewType == 3) {
                Category category = this.mCategories.get(i + 0);
                CategoryHolder categoryHolder = (CategoryHolder) viewHolder;
                categoryHolder.mCategory = category;
                categoryHolder.mTitleView.setText(category.mTitle);
                categoryHolder.drawThumbnailAndOverlayIcon();
            } else if (itemViewType != 4) {
                Log.e("CategorySelectorFragment", "Unsupported viewType " + itemViewType + " in CategoryAdapter");
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater from = LayoutInflater.from(CategorySelectorFragment.this.getActivity());
            if (i == 1) {
                return new MyPhotosCategoryHolder(CategorySelectorFragment.this, from.inflate(R.layout.grid_item_category, viewGroup, false));
            } else if (i == 2) {
                return new FeaturedCategoryHolder(CategorySelectorFragment.this, from.inflate(R.layout.grid_item_category, viewGroup, false));
            } else if (i == 3) {
                return new CategoryHolder(from.inflate(R.layout.grid_item_category, viewGroup, false));
            } else if (i != 4) {
                Log.e("CategorySelectorFragment", "Unsupported viewType " + i + " in CategoryAdapter");
                return null;
            } else {
                return new LoadingIndicatorHolder(CategorySelectorFragment.this, from.inflate(R.layout.grid_item_loading_indicator, viewGroup, false), null);
            }
        }

        @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
        public void onPermissionsDenied(boolean z) {
            if (z) {
                new AlertDialog.Builder(CategorySelectorFragment.this.getActivity(), R.style.LightDialogTheme).setMessage(CategorySelectorFragment.this.getString(R.string.permission_needed_explanation_go_to_settings)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(R.string.settings_button_label, new CategoryFragment$$ExternalSyntheticLambda0(this)).create().show();
            }
        }

        @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
        public void onPermissionsGranted() {
            this.mObservable.notifyChanged();
        }
    }

    /* loaded from: classes.dex */
    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Category mCategory;
        public ImageView mImageView;
        public ImageView mOverlayIconView;
        public TextView mTitleView;

        public CategoryHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.mImageView = (ImageView) view.findViewById(R.id.image);
            this.mOverlayIconView = (ImageView) view.findViewById(R.id.overlay_icon);
            this.mTitleView = (TextView) view.findViewById(R.id.category_title);
            CardView cardView = (CardView) view.findViewById(R.id.category);
            cardView.getLayoutParams().height = CategorySelectorFragment.this.mTileSizePx.y;
            cardView.setRadius(CategorySelectorFragment.this.getResources().getDimension(R.dimen.grid_item_all_radius_small));
        }

        public final void drawThumbnailAndOverlayIcon() {
            this.mOverlayIconView.setImageDrawable(this.mCategory.getOverlayIcon(CategorySelectorFragment.this.getActivity().getApplicationContext()));
            int overlayIconSizeDp = (int) (((float) this.mCategory.getOverlayIconSizeDp()) * DisplayMetricsRetriever.getInstance().getDisplayMetrics(CategorySelectorFragment.this.getResources(), CategorySelectorFragment.this.getActivity().getWindowManager().getDefaultDisplay()).density);
            this.mOverlayIconView.getLayoutParams().width = overlayIconSizeDp;
            this.mOverlayIconView.getLayoutParams().height = overlayIconSizeDp;
            Asset thumbnail = this.mCategory.getThumbnail(CategorySelectorFragment.this.getActivity().getApplicationContext());
            if (thumbnail != null) {
                thumbnail.loadDrawable(CategorySelectorFragment.this.getActivity(), this.mImageView, R$attr.getColorAttr(CategorySelectorFragment.this.getActivity(), 16844080));
                return;
            }
            FragmentActivity activity = CategorySelectorFragment.this.getActivity();
            Objects.requireNonNull(activity, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
            RequestBuilder<Drawable> asDrawable = Glide.get(activity).requestManagerRetriever.get(activity).asDrawable();
            asDrawable.load((Object) null);
            asDrawable.into(this.mImageView);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FragmentActivity activity = CategorySelectorFragment.this.getActivity();
            UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(activity);
            userEventLogger.logCategorySelected(this.mCategory.mCollectionId);
            if (this.mCategory.supportsCustomPhotos()) {
                CategorySelectorFragment.this.getCategorySelectorFragmentHost().requestCustomPhotoPicker(new MyPhotosStarter.PermissionChangedListener() { // from class: com.android.wallpaper.picker.CategorySelectorFragment.CategoryHolder.1
                    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                    public void onPermissionsDenied(boolean z) {
                    }

                    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                    public void onPermissionsGranted() {
                        CategoryHolder.this.drawThumbnailAndOverlayIcon();
                    }
                });
            } else if (this.mCategory.isSingleWallpaperCategory()) {
                WallpaperInfo singleWallpaper = this.mCategory.getSingleWallpaper();
                userEventLogger.logIndividualWallpaperSelected(this.mCategory.mCollectionId);
                ((DefaultWallpaperPersister) InjectorProvider.getInjector().getWallpaperPersister(activity)).mWallpaperInfoInPreview = singleWallpaper;
                singleWallpaper.showPreview(activity, new PreviewActivity.PreviewActivityIntentFactory(), singleWallpaper instanceof LiveWallpaperInfo ? 4 : 1);
            } else {
                CategorySelectorFragment.this.getCategorySelectorFragmentHost().show(this.mCategory);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface CategorySelectorFragmentHost {
        void cleanUp();

        void fetchCategories();

        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
        boolean isHostToolbarShown();

        void requestCustomPhotoPicker(MyPhotosStarter.PermissionChangedListener permissionChangedListener);

        @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
        void setToolbarTitle(CharSequence charSequence);

        void show(Category category);
    }

    /* loaded from: classes.dex */
    public class CategorySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        public CategoryAdapter mAdapter;

        public CategorySpanSizeLookup(CategoryAdapter categoryAdapter, AnonymousClass1 r3) {
            this.mAdapter = categoryAdapter;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            if (i < 0 || this.mAdapter.getItemViewType(i) == 4 || this.mAdapter.getItemViewType(i) == 1) {
                CategorySelectorFragment categorySelectorFragment = CategorySelectorFragment.this;
                int i2 = CategorySelectorFragment.$r8$clinit;
                return categorySelectorFragment.getNumColumns() * 2;
            } else if (this.mAdapter.getItemViewType(i) == 2) {
                return (CategorySelectorFragment.this.getNumColumns() * 2) / 2;
            } else {
                return 2;
            }
        }
    }

    /* loaded from: classes.dex */
    public class FeaturedCategoryHolder extends CategoryHolder {
        public FeaturedCategoryHolder(CategorySelectorFragment categorySelectorFragment, View view) {
            super(view);
            CardView cardView = (CardView) view.findViewById(R.id.category);
            cardView.getLayoutParams().height = R$color.getFeaturedCategoryTileSize(categorySelectorFragment.getActivity()).y;
            cardView.setRadius(categorySelectorFragment.getResources().getDimension(R.dimen.grid_item_all_radius));
        }
    }

    /* loaded from: classes.dex */
    public class GridPaddingDecoration extends RecyclerView.ItemDecoration {
        public final int mPadding;

        public GridPaddingDecoration(int i) {
            this.mPadding = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getChildAdapterPosition(view) + 0 >= 0) {
                int i = this.mPadding;
                rect.left = i;
                rect.right = i;
            }
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
            if ((childViewHolder instanceof MyPhotosCategoryHolder) || (childViewHolder instanceof FeaturedCategoryHolder)) {
                rect.bottom = CategorySelectorFragment.this.getResources().getDimensionPixelSize(R.dimen.grid_item_featured_category_padding_bottom);
            } else {
                rect.bottom = CategorySelectorFragment.this.getResources().getDimensionPixelSize(R.dimen.grid_item_category_padding_bottom);
            }
        }
    }

    /* loaded from: classes.dex */
    public class LoadingIndicatorHolder extends RecyclerView.ViewHolder {
        public LoadingIndicatorHolder(CategorySelectorFragment categorySelectorFragment, View view, AnonymousClass1 r3) {
            super(view);
            ((ProgressBar) view.findViewById(R.id.loading_indicator)).getIndeterminateDrawable().setColorFilter(R$attr.getColorAttr(categorySelectorFragment.getActivity(), 16843829), PorterDuff.Mode.SRC_IN);
        }
    }

    /* loaded from: classes.dex */
    public class MyPhotosCategoryHolder extends CategoryHolder {
        public MyPhotosCategoryHolder(CategorySelectorFragment categorySelectorFragment, View view) {
            super(view);
            CardView cardView = (CardView) view.findViewById(R.id.category);
            int i = R$color.getFeaturedCategoryTileSize(categorySelectorFragment.getActivity()).y;
            cardView.getLayoutParams().height = i;
            cardView.setRadius((float) i);
        }
    }

    public CategorySelectorFragment() {
        ArrayList<Category> arrayList = new ArrayList<>();
        this.mCategories = arrayList;
        this.mAdapter = new CategoryAdapter(arrayList, null);
    }

    public final CategorySelectorFragmentHost getCategorySelectorFragmentHost() {
        Fragment fragment = this.mParentFragment;
        if (fragment != null) {
            return (CategorySelectorFragmentHost) fragment;
        }
        return (CategorySelectorFragmentHost) getActivity();
    }

    public final int getNumColumns() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return 1;
        }
        return R$color.getNumColumns(activity, R$color.getActivityWindowWidthPx(activity), 3, 3);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_category_selector, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.category_grid);
        this.mImageGrid = recyclerView;
        recyclerView.addItemDecoration(new GridPaddingDecoration(getResources().getDimensionPixelSize(R.dimen.grid_item_category_padding_horizontal)));
        FragmentActivity activity = getActivity();
        Resources resources = activity.getResources();
        int activityWindowWidthPx = R$color.getActivityWindowWidthPx(activity);
        this.mTileSizePx = R$color.getSquareTileSize(R$color.getNumColumns(activity, activityWindowWidthPx, 3, 3), activityWindowWidthPx, resources.getDimensionPixelSize(R.dimen.grid_item_category_padding_horizontal), resources.getDimensionPixelSize(R.dimen.category_grid_edge_space));
        this.mImageGrid.setAdapter(this.mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getNumColumns() * 2);
        gridLayoutManager.mSpanSizeLookup = new CategorySpanSizeLookup(this.mAdapter, null);
        this.mImageGrid.setLayoutManager(gridLayoutManager);
        this.mImageGrid.setAccessibilityDelegateCompat(new WallpaperPickerRecyclerViewAccessibilityDelegate(this.mImageGrid, (WallpaperPickerRecyclerViewAccessibilityDelegate.BottomSheetHost) this.mParentFragment, getNumColumns()));
        if (getCategorySelectorFragmentHost().isHostToolbarShown()) {
            inflate.findViewById(R.id.header_bar).setVisibility(8);
            getCategorySelectorFragmentHost().setToolbarTitle(getResources().getText(R.string.wallpaper_title));
        } else {
            setUpToolbar(inflate, true);
            setTitle(getResources().getText(R.string.wallpaper_title));
        }
        if (!R$id.isDeepLink(getActivity().getIntent())) {
            getCategorySelectorFragmentHost().fetchCategories();
        }
        inflate.setOnApplyWindowInsetsListener(CategorySelectorFragment$$ExternalSyntheticLambda0.INSTANCE);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        getCategorySelectorFragmentHost().cleanUp();
        this.mCalled = true;
    }

    public void updateCategory(Category category) {
        int indexOf = this.mCategories.indexOf(category);
        if (indexOf >= 0) {
            this.mCategories.remove(indexOf);
            this.mCategories.add(indexOf, category);
            this.mAdapter.notifyItemChanged(indexOf + 0);
        }
    }
}
