package com.android.wallpaper.picker;

import android.app.WallpaperColors;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.FragmentActivity;
import androidx.slice.view.R$id;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.CurrentWallpaperInfoFactory;
import com.android.wallpaper.module.DefaultCurrentWallpaperInfoFactory;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.picker.CategorySelectorFragment;
import com.android.wallpaper.picker.MyPhotosStarter;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.WallpaperConnection;
import com.android.wallpaper.util.WallpaperSurfaceCallback;
import com.android.wallpaper.widget.LockScreenPreviewer;
import com.android.wallpaper.widget.PreviewPager;
import com.android.wallpaper.widget.WallpaperPickerRecyclerViewAccessibilityDelegate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class CategoryFragment extends AppbarFragment implements CategorySelectorFragment.CategorySelectorFragmentHost, WallpaperPickerRecyclerViewAccessibilityDelegate.BottomSheetHost, IndividualPickerFragment.IndividualPickerFragmentHost {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BottomSheetBehavior<View> mBottomSheetBehavior;
    public CategorySelectorFragment mCategorySelectorFragment = new CategorySelectorFragment();
    public WallpaperInfo mHomePreviewWallpaperInfo;
    public IndividualPickerFragment mIndividualPickerFragment;
    public WallpaperInfo mLockPreviewWallpaperInfo;
    public LockScreenPreviewer mLockScreenPreviewer;
    public PreviewPager mPreviewPager;
    public View mRootContainer;
    public List<View> mWallPaperPreviews;
    public WallpaperConnection mWallpaperConnection;
    public int mWallpaperIndex;
    public SurfaceView mWallpaperSurface;
    public WallpaperSurfaceCallback mWallpaperSurfaceCallback;
    public SurfaceView mWorkspaceSurface;
    public WorkspaceSurfaceHolderCallback mWorkspaceSurfaceCallback;

    /* loaded from: classes.dex */
    public interface CategoryFragmentHost extends MyPhotosStarter.MyPhotosStarterProvider {
        @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
        void cleanUp();

        @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
        void fetchCategories();

        boolean isReadExternalStoragePermissionGranted();

        @Override // com.android.wallpaper.model.PermissionRequester
        void requestExternalStoragePermission(MyPhotosStarter.PermissionChangedListener permissionChangedListener);

        void show(String str);

        @Override // com.android.wallpaper.model.WallpaperPreviewNavigator
        void showViewOnlyPreview(WallpaperInfo wallpaperInfo, boolean z);
    }

    /* loaded from: classes.dex */
    public static class PreviewPagerAdapter extends PagerAdapter {
        public List<View> mPages;

        public PreviewPagerAdapter(List<View> list) {
            this.mPages = list;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.mPages.size();
        }
    }

    public CategoryFragment() {
        new Rect();
        new Rect();
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void cleanUp() {
        getFragmentHost().cleanUp();
    }

    @Override // com.android.wallpaper.widget.WallpaperPickerRecyclerViewAccessibilityDelegate.BottomSheetHost
    public void expandBottomSheet() {
        BottomSheetBehavior<View> bottomSheetBehavior = this.mBottomSheetBehavior;
        if (bottomSheetBehavior.state != 3) {
            bottomSheetBehavior.setState(3);
        }
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void fetchCategories() {
        getFragmentHost().fetchCategories();
    }

    @Override // com.android.wallpaper.widget.WallpaperPickerRecyclerViewAccessibilityDelegate.BottomSheetHost
    public int getBottomSheetState() {
        return this.mBottomSheetBehavior.state;
    }

    @Override // com.android.wallpaper.picker.AppbarFragment
    public CharSequence getDefaultTitle() {
        return getContext().getString(R.string.app_name);
    }

    public final CategoryFragmentHost getFragmentHost() {
        return (CategoryFragmentHost) getActivity();
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost, com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public boolean isHostToolbarShown() {
        return true;
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void moveToPreviousFragment() {
        getChildFragmentManager().popBackStack();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            this.mCategorySelectorFragment.mAdapter.mObservable.notifyChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_category_picker, viewGroup, false);
        this.mWallPaperPreviews = new ArrayList();
        final CardView cardView = (CardView) layoutInflater.inflate(R.layout.wallpaper_preview_card, (ViewGroup) null);
        SurfaceView surfaceView = (SurfaceView) cardView.findViewById(R.id.workspace_surface);
        this.mWorkspaceSurface = surfaceView;
        this.mWorkspaceSurfaceCallback = new WorkspaceSurfaceHolderCallback(surfaceView, getContext(), false);
        this.mWallpaperSurface = (SurfaceView) cardView.findViewById(R.id.wallpaper_surface);
        this.mWallpaperSurfaceCallback = new WallpaperSurfaceCallback(getContext(), cardView, this.mWallpaperSurface, null, null);
        this.mWallPaperPreviews.add(cardView);
        final CardView cardView2 = (CardView) layoutInflater.inflate(R.layout.wallpaper_preview_card, (ViewGroup) null);
        cardView2.findViewById(R.id.workspace_surface).setVisibility(8);
        cardView2.findViewById(R.id.wallpaper_surface).setVisibility(8);
        final ViewGroup viewGroup2 = (ViewGroup) cardView2.findViewById(R.id.lock_screen_preview_container);
        viewGroup2.setVisibility(0);
        this.mLockScreenPreviewer = new LockScreenPreviewer(this.mLifecycleRegistry, getContext(), viewGroup2);
        this.mWallPaperPreviews.add(cardView2);
        PreviewPager previewPager = (PreviewPager) inflate.findViewById(R.id.wallpaper_preview_pager);
        this.mPreviewPager = previewPager;
        if (previewPager.isRtl()) {
            Collections.reverse(this.mWallPaperPreviews);
        }
        this.mPreviewPager.setAdapter(new PreviewPagerAdapter(this.mWallPaperPreviews));
        this.mPreviewPager.mExternalPageListener = new ViewPager.OnPageChangeListener() { // from class: com.android.wallpaper.picker.CategoryFragment.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                CategoryFragment categoryFragment = CategoryFragment.this;
                WallpaperConnection wallpaperConnection = categoryFragment.mWallpaperConnection;
                if (wallpaperConnection != null && wallpaperConnection.mEngineReady && (categoryFragment.mHomePreviewWallpaperInfo instanceof LiveWallpaperInfo)) {
                    if (f == 0.0f || f == 1.0f || i2 == 0) {
                        categoryFragment.mWallpaperSurface.setZOrderMediaOverlay(false);
                    } else {
                        categoryFragment.mWallpaperSurface.setZOrderMediaOverlay(true);
                    }
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CategoryFragment categoryFragment = CategoryFragment.this;
                if (categoryFragment.mPreviewPager.isRtl()) {
                    i = (CategoryFragment.this.mWallPaperPreviews.size() - 1) - i;
                }
                categoryFragment.mWallpaperIndex = i;
                IndividualPickerFragment individualPickerFragment = CategoryFragment.this.mIndividualPickerFragment;
                if (individualPickerFragment != null && individualPickerFragment.isVisible()) {
                    CategoryFragment categoryFragment2 = CategoryFragment.this;
                    categoryFragment2.mIndividualPickerFragment.highlightAppliedWallpaper(categoryFragment2.mWallpaperIndex);
                }
            }
        };
        FragmentActivity activity = getActivity();
        if ((activity.getPackageManager().checkPermission("android.permission.READ_WALLPAPER_INTERNAL", activity.getPackageName()) == 0) || getFragmentHost().isReadExternalStoragePermissionGranted()) {
            showCurrentWallpaper(inflate, true);
        } else {
            showCurrentWallpaper(inflate, false);
            ((Button) inflate.findViewById(R.id.permission_needed_allow_access_button)).setOnClickListener(new CategoryFragment$$ExternalSyntheticLambda3(this, inflate));
            ((TextView) inflate.findViewById(R.id.permission_needed_explanation)).setText(getResources().getString(R.string.permission_needed_explanation, getString(R.string.app_name)));
        }
        ViewGroup viewGroup3 = (ViewGroup) inflate.findViewById(R.id.category_fragment_container);
        BottomSheetBehavior<View> from = BottomSheetBehavior.from(viewGroup3);
        this.mBottomSheetBehavior = from;
        from.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() { // from class: com.android.wallpaper.picker.CategoryFragment.2
            @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
            public void onSlide(View view, float f) {
            }

            @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
            public void onStateChanged(View view, int i) {
                CategoryFragment.this.mPreviewPager.setImportantForAccessibility(i == 3 ? 4 : 1);
            }
        });
        this.mRootContainer = inflate.findViewById(R.id.root_container);
        viewGroup3.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.wallpaper.picker.CategoryFragment.3
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                int height = CategoryFragment.this.mRootContainer.getHeight() - CategoryFragment.this.mPreviewPager.getMeasuredHeight();
                CategoryFragment.this.mBottomSheetBehavior.setPeekHeight(height);
                view.setMinimumHeight(height);
                cardView.setRadius(R$color.getPreviewCornerRadius(CategoryFragment.this.getActivity(), cardView.getMeasuredWidth()));
                CardView cardView3 = cardView2;
                if (cardView3 != null) {
                    cardView3.setRadius(R$color.getPreviewCornerRadius(CategoryFragment.this.getActivity(), viewGroup2.getMeasuredWidth()));
                }
            }
        });
        viewGroup3.setOnApplyWindowInsetsListener(CategoryFragment$$ExternalSyntheticLambda1.INSTANCE);
        setUpToolbar(inflate, true);
        BackStackRecord backStackRecord = new BackStackRecord(getChildFragmentManager());
        backStackRecord.replace(R.id.category_fragment_container, this.mCategorySelectorFragment);
        backStackRecord.commitNow();
        Intent intent = getActivity().getIntent();
        String collectionId = R$id.getCollectionId(intent);
        if (!TextUtils.isEmpty(collectionId)) {
            IndividualPickerFragment individualPickerFragment = InjectorProvider.getInjector().getIndividualPickerFragment(collectionId);
            this.mIndividualPickerFragment = individualPickerFragment;
            individualPickerFragment.highlightAppliedWallpaper(this.mWallpaperIndex);
            BackStackRecord backStackRecord2 = new BackStackRecord(getChildFragmentManager());
            backStackRecord2.replace(R.id.category_fragment_container, this.mIndividualPickerFragment);
            backStackRecord2.addToBackStack(null);
            backStackRecord2.commit();
            getChildFragmentManager().executePendingTransactions();
            intent.setData(null);
        }
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mCalled = true;
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.disconnect();
            this.mWallpaperConnection = null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mCalled = true;
        this.mWallpaperSurfaceCallback.cleanUp();
        this.mWorkspaceSurfaceCallback.cleanUp();
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.disconnect();
            this.mWallpaperConnection = null;
        }
        this.mPreviewPager.setAdapter(null);
        this.mWallPaperPreviews.forEach(CategoryFragment$$ExternalSyntheticLambda5.INSTANCE);
        this.mWallPaperPreviews.clear();
    }

    @Override // com.android.wallpaper.picker.AppbarFragment, android.widget.Toolbar.OnMenuItemClickListener
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.daily_rotation) {
            return false;
        }
        IndividualPickerFragment individualPickerFragment = this.mIndividualPickerFragment;
        if (individualPickerFragment != null && individualPickerFragment.isVisible()) {
            IndividualPickerFragment individualPickerFragment2 = this.mIndividualPickerFragment;
            Objects.requireNonNull(individualPickerFragment2);
            StartRotationDialogFragment startRotationDialogFragment = new StartRotationDialogFragment();
            startRotationDialogFragment.setTargetFragment(individualPickerFragment2, 1);
            startRotationDialogFragment.show(individualPickerFragment2.mFragmentManager, "start_rotation_dialog");
        }
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        this.mCalled = true;
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.mIsVisible = false;
            wallpaperConnection.setEngineVisibility(false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        this.mCalled = true;
        InjectorProvider.getInjector().getPreferences(getActivity()).setLastAppActiveTimestamp(new Date().getTime());
        Glide.get(getActivity()).setMemoryCategory(MemoryCategory.NORMAL);
        ((DefaultCurrentWallpaperInfoFactory) InjectorProvider.getInjector().getCurrentWallpaperFactory(getActivity().getApplicationContext())).createCurrentWallpaperInfos(new CurrentWallpaperInfoFactory.WallpaperInfoCallback() { // from class: com.android.wallpaper.picker.CategoryFragment.5
            @Override // com.android.wallpaper.module.CurrentWallpaperInfoFactory.WallpaperInfoCallback
            public void onWallpaperInfoCreated(final WallpaperInfo wallpaperInfo, final WallpaperInfo wallpaperInfo2, int i) {
                new Handler().post(new Runnable() { // from class: com.android.wallpaper.picker.CategoryFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FragmentActivity activity;
                        if (CategoryFragment.this.getActivity() != null) {
                            CategoryFragment categoryFragment = CategoryFragment.this;
                            WallpaperInfo wallpaperInfo3 = wallpaperInfo;
                            categoryFragment.mHomePreviewWallpaperInfo = wallpaperInfo3;
                            WallpaperInfo wallpaperInfo4 = wallpaperInfo2;
                            if (wallpaperInfo4 == null) {
                                wallpaperInfo4 = wallpaperInfo3;
                            }
                            categoryFragment.mLockPreviewWallpaperInfo = wallpaperInfo4;
                            ImageView imageView = categoryFragment.mWallpaperSurfaceCallback.mHomeImageWallpaper;
                            if (wallpaperInfo3 != null && imageView != null && (activity = categoryFragment.getActivity()) != null) {
                                UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(activity);
                                boolean z = wallpaperInfo3 instanceof LiveWallpaperInfo;
                                ImageView imageView2 = z ^ true ? categoryFragment.mWallpaperSurfaceCallback.mHomeImageWallpaper : imageView;
                                if (imageView2 != null) {
                                    wallpaperInfo3.getThumbAsset(activity.getApplicationContext()).loadPreviewImage(activity, imageView2, R$attr.getColorAttr(categoryFragment.getActivity(), 16844080));
                                }
                                if (z) {
                                    if (categoryFragment.mWallpaperSurfaceCallback.mHomeImageWallpaper != null) {
                                        wallpaperInfo3.getThumbAsset(activity.getApplicationContext()).loadPreviewImage(activity, categoryFragment.mWallpaperSurfaceCallback.mHomeImageWallpaper, R$attr.getColorAttr(categoryFragment.getActivity(), 16844080));
                                    }
                                    FragmentActivity activity2 = categoryFragment.getActivity();
                                    if (activity2 != null) {
                                        WallpaperConnection wallpaperConnection = categoryFragment.mWallpaperConnection;
                                        if (wallpaperConnection != null) {
                                            wallpaperConnection.disconnect();
                                        }
                                        if (WallpaperConnection.isPreviewAvailable()) {
                                            ImageView imageView3 = categoryFragment.mWallpaperSurfaceCallback.mHomeImageWallpaper;
                                            android.app.WallpaperInfo wallpaperComponent = wallpaperInfo3.getWallpaperComponent();
                                            WallpaperConnection wallpaperConnection2 = new WallpaperConnection(new Intent("android.service.wallpaper.WallpaperService").setClassName(wallpaperComponent.getPackageName(), wallpaperComponent.getServiceName()), activity2, new WallpaperConnection.WallpaperConnectionListener() { // from class: com.android.wallpaper.picker.CategoryFragment.6
                                                @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
                                                public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i2) {
                                                    CategoryFragment categoryFragment2 = CategoryFragment.this;
                                                    if (categoryFragment2.mLockPreviewWallpaperInfo instanceof LiveWallpaperInfo) {
                                                        categoryFragment2.mLockScreenPreviewer.setColor(wallpaperColors);
                                                    }
                                                }
                                            }, categoryFragment.mWallpaperSurface);
                                            categoryFragment.mWallpaperConnection = wallpaperConnection2;
                                            wallpaperConnection2.mIsVisible = true;
                                            wallpaperConnection2.setEngineVisibility(true);
                                            imageView3.post(new DiskBasedLogger$$ExternalSyntheticLambda1(categoryFragment));
                                        }
                                    }
                                } else {
                                    WallpaperConnection wallpaperConnection3 = categoryFragment.mWallpaperConnection;
                                    if (wallpaperConnection3 != null) {
                                        wallpaperConnection3.disconnect();
                                        categoryFragment.mWallpaperConnection = null;
                                    }
                                }
                                ((View) imageView.getParent()).setOnClickListener(new CategoryFragment$$ExternalSyntheticLambda2(categoryFragment, wallpaperInfo3, true, userEventLogger));
                            }
                        }
                    }
                });
            }
        }, true);
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.mIsVisible = true;
            wallpaperConnection.setEngineVisibility(true);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        this.mCalled = true;
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.disconnect();
            this.mWallpaperConnection = null;
        }
    }

    @Override // com.android.wallpaper.picker.BottomActionBarFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mWallpaperSurface.getHolder().addCallback(this.mWallpaperSurfaceCallback);
        this.mWallpaperSurface.setZOrderMediaOverlay(true);
        this.mWorkspaceSurface.setZOrderMediaOverlay(true);
        this.mWorkspaceSurface.getHolder().addCallback(this.mWorkspaceSurfaceCallback);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void removeToolbarMenu() {
        this.mToolbar.getMenu().clear();
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void requestCustomPhotoPicker(MyPhotosStarter.PermissionChangedListener permissionChangedListener) {
        getFragmentHost().getMyPhotosStarter().requestCustomPhotoPicker(permissionChangedListener);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarMenu(int i) {
        this.mToolbar.inflateMenu(i);
        this.mToolbar.setOnMenuItemClickListener(this);
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost, com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarTitle(CharSequence charSequence) {
        setTitle(charSequence);
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void show(Category category) {
        if (!(category instanceof WallpaperCategory)) {
            getFragmentHost().show(category.mCollectionId);
            return;
        }
        IndividualPickerFragment individualPickerFragment = InjectorProvider.getInjector().getIndividualPickerFragment(category.mCollectionId);
        this.mIndividualPickerFragment = individualPickerFragment;
        individualPickerFragment.highlightAppliedWallpaper(this.mWallpaperIndex);
        Objects.requireNonNull(this.mIndividualPickerFragment);
        BackStackRecord backStackRecord = new BackStackRecord(getChildFragmentManager());
        backStackRecord.replace(R.id.category_fragment_container, this.mIndividualPickerFragment);
        backStackRecord.addToBackStack(null);
        backStackRecord.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public final void showCurrentWallpaper(View view, boolean z) {
        int i = 0;
        view.findViewById(R.id.wallpaper_preview_pager).setVisibility(z ? 0 : 4);
        View findViewById = view.findViewById(R.id.permission_needed);
        if (z) {
            i = 8;
        }
        findViewById.setVisibility(i);
    }
}
