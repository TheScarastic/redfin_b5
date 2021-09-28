package com.android.wallpaper.picker;

import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.ImageWallpaperInfo;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultPackageStatusNotifier;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.PackageStatusNotifier;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.picker.CategorySelectorFragment;
import com.android.wallpaper.picker.MyPhotosStarter;
import com.android.wallpaper.picker.PreviewActivity;
import com.android.wallpaper.picker.ViewOnlyPreviewActivity;
import com.android.wallpaper.picker.individual.IndividualPickerActivity;
import com.google.android.apps.wallpaper.module.WallpaperCategoryProvider;
import com.google.android.apps.wallpaper.module.WallpaperCategoryProvider$$ExternalSyntheticLambda0;
import com.google.android.apps.wallpaper.module.WallpaperCategoryProvider$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpaperPickerDelegate implements MyPhotosStarter {
    public final FragmentActivity mActivity;
    public CategoryProvider mCategoryProvider;
    public final WallpapersUiContainer mContainer;
    public String mDownloadableIntentAction;
    public PackageStatusNotifier.Listener mDownloadableWallpaperStatusListener;
    public PackageStatusNotifier.Listener mLiveWallpaperStatusListener;
    public PackageStatusNotifier mPackageStatusNotifier;
    public WallpaperPreferences mPreferences;
    public PackageStatusNotifier.Listener mThirdPartyStatusListener;
    public WallpaperPersister mWallpaperPersister;
    public IndividualPickerActivity.IndividualPickerActivityIntentFactory mPickerIntentFactory = new IndividualPickerActivity.IndividualPickerActivityIntentFactory();
    public InlinePreviewIntentFactory mPreviewIntentFactory = new PreviewActivity.PreviewActivityIntentFactory();
    public InlinePreviewIntentFactory mViewOnlyPreviewIntentFactory = new ViewOnlyPreviewActivity.ViewOnlyPreviewActivityIntentFactory();
    public int mFormFactor = 1;
    public List<MyPhotosStarter.PermissionChangedListener> mPermissionChangedListeners = new ArrayList();

    public WallpaperPickerDelegate(WallpapersUiContainer wallpapersUiContainer, FragmentActivity fragmentActivity, Injector injector) {
        this.mContainer = wallpapersUiContainer;
        this.mActivity = fragmentActivity;
        this.mCategoryProvider = injector.getCategoryProvider(fragmentActivity);
        this.mPreferences = injector.getPreferences(fragmentActivity);
        this.mPackageStatusNotifier = injector.getPackageStatusNotifier(fragmentActivity);
        this.mWallpaperPersister = injector.getWallpaperPersister(fragmentActivity);
        Objects.requireNonNull(injector.getFormFactorChecker(fragmentActivity));
        this.mDownloadableIntentAction = injector.getDownloadableIntentAction();
    }

    public void addCategory(Category category, boolean z) {
        CategorySelectorFragment categorySelectorFragment = getCategorySelectorFragment();
        if (categorySelectorFragment != null) {
            if (z && !categorySelectorFragment.mAwaitingCategories) {
                categorySelectorFragment.mAdapter.notifyItemChanged(categorySelectorFragment.getNumColumns());
                categorySelectorFragment.mAdapter.mObservable.notifyItemRangeInserted(categorySelectorFragment.getNumColumns(), 1);
                categorySelectorFragment.mAwaitingCategories = true;
            }
            if (categorySelectorFragment.mCategories.indexOf(category) >= 0) {
                categorySelectorFragment.updateCategory(category);
                return;
            }
            int i = category.mPriority;
            int i2 = 0;
            while (i2 < categorySelectorFragment.mCategories.size() && i >= categorySelectorFragment.mCategories.get(i2).mPriority) {
                i2++;
            }
            categorySelectorFragment.mCategories.add(i2, category);
            CategorySelectorFragment.CategoryAdapter categoryAdapter = categorySelectorFragment.mAdapter;
            if (categoryAdapter != null) {
                categoryAdapter.mObservable.notifyItemRangeInserted(i2 + 0, 1);
            }
        }
    }

    public void cleanUp() {
        PackageStatusNotifier packageStatusNotifier = this.mPackageStatusNotifier;
        if (packageStatusNotifier != null) {
            ((DefaultPackageStatusNotifier) packageStatusNotifier).removeListener(this.mLiveWallpaperStatusListener);
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).removeListener(this.mThirdPartyStatusListener);
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).removeListener(this.mDownloadableWallpaperStatusListener);
        }
    }

    public final CategorySelectorFragment getCategorySelectorFragment() {
        return this.mContainer.getCategorySelectorFragment();
    }

    public boolean handleActivityResult(int i, int i2, Intent intent) {
        Uri uri;
        if (i2 != -1) {
            return false;
        }
        if (i != 0) {
            if (!(i == 1 || i == 2)) {
                if (i != 4) {
                    return false;
                }
                ((DefaultWallpaperPersister) this.mWallpaperPersister).onLiveWallpaperSet();
                populateCategories(true);
            }
            return true;
        }
        if (intent == null) {
            uri = null;
        } else {
            uri = intent.getData();
        }
        if (uri == null) {
            return true;
        }
        ImageWallpaperInfo imageWallpaperInfo = new ImageWallpaperInfo(uri);
        ((DefaultWallpaperPersister) this.mWallpaperPersister).mWallpaperInfoInPreview = imageWallpaperInfo;
        imageWallpaperInfo.showPreview(this.mActivity, this.mPreviewIntentFactory, 1);
        return false;
    }

    public void initialize(boolean z) {
        populateCategories(z);
        WallpaperPickerDelegate$$ExternalSyntheticLambda0 wallpaperPickerDelegate$$ExternalSyntheticLambda0 = new PackageStatusNotifier.Listener(this, 0) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ WallpaperPickerDelegate f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // com.android.wallpaper.module.PackageStatusNotifier.Listener
            public final void onPackageChanged(String str, int i) {
                Category category;
                switch (this.$r8$classId) {
                    case 0:
                        WallpaperPickerDelegate wallpaperPickerDelegate = this.f$0;
                        String string = wallpaperPickerDelegate.mActivity.getString(R.string.live_wallpaper_collection_id);
                        Category category2 = ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).getCategory(string);
                        if (i != 3 || (category2 != null && category2.containsThirdParty(str))) {
                            ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).fetchCategories(new CategoryReceiver(string, category2) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.4
                                public final /* synthetic */ String val$liveWallpaperCollectionId;
                                public final /* synthetic */ Category val$oldLiveWallpapersCategory;

                                {
                                    this.val$liveWallpaperCollectionId = r2;
                                    this.val$oldLiveWallpapersCategory = r3;
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void doneFetchingCategories() {
                                    Category category3 = ((DefaultCategoryProvider) WallpaperPickerDelegate.this.mCategoryProvider).getCategory(this.val$liveWallpaperCollectionId);
                                    if (category3 == null) {
                                        WallpaperPickerDelegate.this.removeCategory(this.val$oldLiveWallpapersCategory);
                                    } else if (this.val$oldLiveWallpapersCategory != null) {
                                        CategorySelectorFragment categorySelectorFragment = WallpaperPickerDelegate.this.getCategorySelectorFragment();
                                        if (categorySelectorFragment != null) {
                                            categorySelectorFragment.updateCategory(category3);
                                        }
                                    } else {
                                        WallpaperPickerDelegate.this.addCategory(category3, false);
                                    }
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void onCategoryReceived(Category category3) {
                                }
                            }, true);
                            return;
                        }
                        return;
                    case 1:
                        WallpaperPickerDelegate wallpaperPickerDelegate2 = this.f$0;
                        if (i == 1) {
                            ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(str) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.2
                                public final /* synthetic */ String val$packageName;

                                {
                                    this.val$packageName = r2;
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void doneFetchingCategories() {
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void onCategoryReceived(Category category3) {
                                    if (category3.supportsThirdParty() && category3.containsThirdParty(this.val$packageName)) {
                                        WallpaperPickerDelegate.this.addCategory(category3, false);
                                    }
                                }
                            }, true);
                            return;
                        } else if (i == 3) {
                            DefaultCategoryProvider defaultCategoryProvider = (DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider;
                            int i2 = 0;
                            int size = defaultCategoryProvider.mFetchedCategories ? defaultCategoryProvider.mCategories.size() : 0;
                            while (true) {
                                if (i2 < size) {
                                    category = ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).getCategory(i2);
                                    if (!category.supportsThirdParty() || !category.containsThirdParty(str)) {
                                        i2++;
                                    }
                                } else {
                                    category = null;
                                }
                            }
                            if (category != null) {
                                ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(category) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.3
                                    public final /* synthetic */ Category val$oldCategory;

                                    {
                                        this.val$oldCategory = r2;
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void doneFetchingCategories() {
                                        WallpaperPickerDelegate.this.removeCategory(this.val$oldCategory);
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void onCategoryReceived(Category category3) {
                                    }
                                }, true);
                                return;
                            }
                            return;
                        } else {
                            wallpaperPickerDelegate2.populateCategories(true);
                            return;
                        }
                    default:
                        WallpaperPickerDelegate wallpaperPickerDelegate3 = this.f$0;
                        Objects.requireNonNull(wallpaperPickerDelegate3);
                        if (i != 3) {
                            wallpaperPickerDelegate3.populateCategories(true);
                            return;
                        }
                        return;
                }
            }
        };
        this.mLiveWallpaperStatusListener = wallpaperPickerDelegate$$ExternalSyntheticLambda0;
        this.mThirdPartyStatusListener = new PackageStatusNotifier.Listener(this, 1) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ WallpaperPickerDelegate f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // com.android.wallpaper.module.PackageStatusNotifier.Listener
            public final void onPackageChanged(String str, int i) {
                Category category;
                switch (this.$r8$classId) {
                    case 0:
                        WallpaperPickerDelegate wallpaperPickerDelegate = this.f$0;
                        String string = wallpaperPickerDelegate.mActivity.getString(R.string.live_wallpaper_collection_id);
                        Category category2 = ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).getCategory(string);
                        if (i != 3 || (category2 != null && category2.containsThirdParty(str))) {
                            ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).fetchCategories(new CategoryReceiver(string, category2) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.4
                                public final /* synthetic */ String val$liveWallpaperCollectionId;
                                public final /* synthetic */ Category val$oldLiveWallpapersCategory;

                                {
                                    this.val$liveWallpaperCollectionId = r2;
                                    this.val$oldLiveWallpapersCategory = r3;
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void doneFetchingCategories() {
                                    Category category3 = ((DefaultCategoryProvider) WallpaperPickerDelegate.this.mCategoryProvider).getCategory(this.val$liveWallpaperCollectionId);
                                    if (category3 == null) {
                                        WallpaperPickerDelegate.this.removeCategory(this.val$oldLiveWallpapersCategory);
                                    } else if (this.val$oldLiveWallpapersCategory != null) {
                                        CategorySelectorFragment categorySelectorFragment = WallpaperPickerDelegate.this.getCategorySelectorFragment();
                                        if (categorySelectorFragment != null) {
                                            categorySelectorFragment.updateCategory(category3);
                                        }
                                    } else {
                                        WallpaperPickerDelegate.this.addCategory(category3, false);
                                    }
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void onCategoryReceived(Category category3) {
                                }
                            }, true);
                            return;
                        }
                        return;
                    case 1:
                        WallpaperPickerDelegate wallpaperPickerDelegate2 = this.f$0;
                        if (i == 1) {
                            ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(str) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.2
                                public final /* synthetic */ String val$packageName;

                                {
                                    this.val$packageName = r2;
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void doneFetchingCategories() {
                                }

                                @Override // com.android.wallpaper.model.CategoryReceiver
                                public void onCategoryReceived(Category category3) {
                                    if (category3.supportsThirdParty() && category3.containsThirdParty(this.val$packageName)) {
                                        WallpaperPickerDelegate.this.addCategory(category3, false);
                                    }
                                }
                            }, true);
                            return;
                        } else if (i == 3) {
                            DefaultCategoryProvider defaultCategoryProvider = (DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider;
                            int i2 = 0;
                            int size = defaultCategoryProvider.mFetchedCategories ? defaultCategoryProvider.mCategories.size() : 0;
                            while (true) {
                                if (i2 < size) {
                                    category = ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).getCategory(i2);
                                    if (!category.supportsThirdParty() || !category.containsThirdParty(str)) {
                                        i2++;
                                    }
                                } else {
                                    category = null;
                                }
                            }
                            if (category != null) {
                                ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(category) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.3
                                    public final /* synthetic */ Category val$oldCategory;

                                    {
                                        this.val$oldCategory = r2;
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void doneFetchingCategories() {
                                        WallpaperPickerDelegate.this.removeCategory(this.val$oldCategory);
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void onCategoryReceived(Category category3) {
                                    }
                                }, true);
                                return;
                            }
                            return;
                        } else {
                            wallpaperPickerDelegate2.populateCategories(true);
                            return;
                        }
                    default:
                        WallpaperPickerDelegate wallpaperPickerDelegate3 = this.f$0;
                        Objects.requireNonNull(wallpaperPickerDelegate3);
                        if (i != 3) {
                            wallpaperPickerDelegate3.populateCategories(true);
                            return;
                        }
                        return;
                }
            }
        };
        ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(wallpaperPickerDelegate$$ExternalSyntheticLambda0, "android.service.wallpaper.WallpaperService");
        ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(this.mThirdPartyStatusListener, "android.intent.action.SET_WALLPAPER");
        String str = this.mDownloadableIntentAction;
        if (str != null) {
            WallpaperPickerDelegate$$ExternalSyntheticLambda0 wallpaperPickerDelegate$$ExternalSyntheticLambda02 = new PackageStatusNotifier.Listener(this, 2) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate$$ExternalSyntheticLambda0
                public final /* synthetic */ int $r8$classId;
                public final /* synthetic */ WallpaperPickerDelegate f$0;

                {
                    this.$r8$classId = r3;
                    this.f$0 = r2;
                }

                @Override // com.android.wallpaper.module.PackageStatusNotifier.Listener
                public final void onPackageChanged(String str, int i) {
                    Category category;
                    switch (this.$r8$classId) {
                        case 0:
                            WallpaperPickerDelegate wallpaperPickerDelegate = this.f$0;
                            String string = wallpaperPickerDelegate.mActivity.getString(R.string.live_wallpaper_collection_id);
                            Category category2 = ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).getCategory(string);
                            if (i != 3 || (category2 != null && category2.containsThirdParty(str))) {
                                ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).fetchCategories(new CategoryReceiver(string, category2) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.4
                                    public final /* synthetic */ String val$liveWallpaperCollectionId;
                                    public final /* synthetic */ Category val$oldLiveWallpapersCategory;

                                    {
                                        this.val$liveWallpaperCollectionId = r2;
                                        this.val$oldLiveWallpapersCategory = r3;
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void doneFetchingCategories() {
                                        Category category3 = ((DefaultCategoryProvider) WallpaperPickerDelegate.this.mCategoryProvider).getCategory(this.val$liveWallpaperCollectionId);
                                        if (category3 == null) {
                                            WallpaperPickerDelegate.this.removeCategory(this.val$oldLiveWallpapersCategory);
                                        } else if (this.val$oldLiveWallpapersCategory != null) {
                                            CategorySelectorFragment categorySelectorFragment = WallpaperPickerDelegate.this.getCategorySelectorFragment();
                                            if (categorySelectorFragment != null) {
                                                categorySelectorFragment.updateCategory(category3);
                                            }
                                        } else {
                                            WallpaperPickerDelegate.this.addCategory(category3, false);
                                        }
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void onCategoryReceived(Category category3) {
                                    }
                                }, true);
                                return;
                            }
                            return;
                        case 1:
                            WallpaperPickerDelegate wallpaperPickerDelegate2 = this.f$0;
                            if (i == 1) {
                                ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(str) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.2
                                    public final /* synthetic */ String val$packageName;

                                    {
                                        this.val$packageName = r2;
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void doneFetchingCategories() {
                                    }

                                    @Override // com.android.wallpaper.model.CategoryReceiver
                                    public void onCategoryReceived(Category category3) {
                                        if (category3.supportsThirdParty() && category3.containsThirdParty(this.val$packageName)) {
                                            WallpaperPickerDelegate.this.addCategory(category3, false);
                                        }
                                    }
                                }, true);
                                return;
                            } else if (i == 3) {
                                DefaultCategoryProvider defaultCategoryProvider = (DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider;
                                int i2 = 0;
                                int size = defaultCategoryProvider.mFetchedCategories ? defaultCategoryProvider.mCategories.size() : 0;
                                while (true) {
                                    if (i2 < size) {
                                        category = ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).getCategory(i2);
                                        if (!category.supportsThirdParty() || !category.containsThirdParty(str)) {
                                            i2++;
                                        }
                                    } else {
                                        category = null;
                                    }
                                }
                                if (category != null) {
                                    ((DefaultCategoryProvider) wallpaperPickerDelegate2.mCategoryProvider).fetchCategories(new CategoryReceiver(category) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.3
                                        public final /* synthetic */ Category val$oldCategory;

                                        {
                                            this.val$oldCategory = r2;
                                        }

                                        @Override // com.android.wallpaper.model.CategoryReceiver
                                        public void doneFetchingCategories() {
                                            WallpaperPickerDelegate.this.removeCategory(this.val$oldCategory);
                                        }

                                        @Override // com.android.wallpaper.model.CategoryReceiver
                                        public void onCategoryReceived(Category category3) {
                                        }
                                    }, true);
                                    return;
                                }
                                return;
                            } else {
                                wallpaperPickerDelegate2.populateCategories(true);
                                return;
                            }
                        default:
                            WallpaperPickerDelegate wallpaperPickerDelegate3 = this.f$0;
                            Objects.requireNonNull(wallpaperPickerDelegate3);
                            if (i != 3) {
                                wallpaperPickerDelegate3.populateCategories(true);
                                return;
                            }
                            return;
                    }
                }
            };
            this.mDownloadableWallpaperStatusListener = wallpaperPickerDelegate$$ExternalSyntheticLambda02;
            ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).addListener(wallpaperPickerDelegate$$ExternalSyntheticLambda02, str);
        }
    }

    public boolean isReadExternalStoragePermissionGranted() {
        return this.mActivity.getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", this.mActivity.getPackageName()) == 0;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 3 && strArr.length > 0 && strArr[0].equals("android.permission.READ_EXTERNAL_STORAGE") && iArr.length > 0) {
            if (iArr[0] == 0) {
                for (MyPhotosStarter.PermissionChangedListener permissionChangedListener : this.mPermissionChangedListeners) {
                    permissionChangedListener.onPermissionsGranted();
                }
            } else if (!this.mActivity.shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
                for (MyPhotosStarter.PermissionChangedListener permissionChangedListener2 : this.mPermissionChangedListeners) {
                    permissionChangedListener2.onPermissionsDenied(true);
                }
            } else {
                for (MyPhotosStarter.PermissionChangedListener permissionChangedListener3 : this.mPermissionChangedListeners) {
                    permissionChangedListener3.onPermissionsDenied(false);
                }
            }
        }
        this.mPermissionChangedListeners.clear();
    }

    public void populateCategories(boolean z) {
        CategorySelectorFragment categorySelectorFragment = getCategorySelectorFragment();
        if (z && categorySelectorFragment != null) {
            categorySelectorFragment.mCategories.clear();
            categorySelectorFragment.mAdapter.mObservable.notifyChanged();
        }
        ((DefaultCategoryProvider) this.mCategoryProvider).fetchCategories(new CategoryReceiver() { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.6
            @Override // com.android.wallpaper.model.CategoryReceiver
            public void doneFetchingCategories() {
                WallpaperPickerDelegate wallpaperPickerDelegate = WallpaperPickerDelegate.this;
                boolean z2 = true;
                if (wallpaperPickerDelegate.mFormFactor == 1) {
                    CategorySelectorFragment categorySelectorFragment2 = wallpaperPickerDelegate.getCategorySelectorFragment();
                    if (categorySelectorFragment2 != null) {
                        if (categorySelectorFragment2.mAwaitingCategories) {
                            CategorySelectorFragment.CategoryAdapter categoryAdapter = categorySelectorFragment2.mAdapter;
                            categoryAdapter.mObservable.notifyItemRangeRemoved(categoryAdapter.getItemCount() - 1, 1);
                            categorySelectorFragment2.mAwaitingCategories = false;
                        }
                        if (((WallpaperCategoryProvider) categorySelectorFragment2.mCategoryProvider).mCategories.stream().filter(WallpaperCategoryProvider$$ExternalSyntheticLambda0.INSTANCE).filter(WallpaperCategoryProvider$$ExternalSyntheticLambda1.INSTANCE).count() < 2) {
                            z2 = false;
                        }
                        categorySelectorFragment2.mIsFeaturedCollectionAvailable = z2;
                        return;
                    }
                    return;
                }
                wallpaperPickerDelegate.mContainer.doneFetchingCategories();
            }

            @Override // com.android.wallpaper.model.CategoryReceiver
            public void onCategoryReceived(Category category) {
                WallpaperPickerDelegate.this.addCategory(category, true);
            }
        }, z);
    }

    public void removeCategory(Category category) {
        int indexOf;
        CategorySelectorFragment categorySelectorFragment = getCategorySelectorFragment();
        if (categorySelectorFragment != null && (indexOf = categorySelectorFragment.mCategories.indexOf(category)) >= 0) {
            categorySelectorFragment.mCategories.remove(indexOf);
            categorySelectorFragment.mAdapter.mObservable.notifyItemRangeRemoved(indexOf + 0, 1);
        }
    }

    @Override // com.android.wallpaper.picker.MyPhotosStarter
    public void requestCustomPhotoPicker(final MyPhotosStarter.PermissionChangedListener permissionChangedListener) {
        if (!isReadExternalStoragePermissionGranted()) {
            requestExternalStoragePermission(new MyPhotosStarter.PermissionChangedListener() { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.1
                @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                public void onPermissionsDenied(boolean z) {
                    permissionChangedListener.onPermissionsDenied(z);
                }

                @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
                public void onPermissionsGranted() {
                    permissionChangedListener.onPermissionsGranted();
                    WallpaperPickerDelegate wallpaperPickerDelegate = WallpaperPickerDelegate.this;
                    Objects.requireNonNull(wallpaperPickerDelegate);
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setType("image/*");
                    wallpaperPickerDelegate.mActivity.startActivityForResult(intent, 0);
                }
            });
            return;
        }
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        this.mActivity.startActivityForResult(intent, 0);
    }

    public void requestExternalStoragePermission(MyPhotosStarter.PermissionChangedListener permissionChangedListener) {
        this.mPermissionChangedListeners.add(permissionChangedListener);
        this.mActivity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 3);
    }

    public void show(String str) {
        Category category = ((DefaultCategoryProvider) this.mCategoryProvider).getCategory(str);
        if (category != null) {
            category.show(this.mActivity, this.mPickerIntentFactory, 0);
        }
    }
}
