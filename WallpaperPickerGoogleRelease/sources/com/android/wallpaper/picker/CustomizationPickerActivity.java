package com.android.wallpaper.picker;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.slice.view.R$id;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.model.PermissionRequester;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperPreviewNavigator;
import com.android.wallpaper.module.DailyLoggingAlarmScheduler;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultNetworkStatusNotifier;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.NetworkStatusNotifier;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.picker.AppbarFragment;
import com.android.wallpaper.picker.CategoryFragment;
import com.android.wallpaper.picker.CategorySelectorFragment;
import com.android.wallpaper.picker.MyPhotosStarter;
import com.android.wallpaper.picker.ViewOnlyPreviewActivity;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.util.ActivityUtils;
import com.android.wallpaper.widget.BottomActionBar;
/* loaded from: classes.dex */
public class CustomizationPickerActivity extends FragmentActivity implements AppbarFragment.AppbarFragmentHost, WallpapersUiContainer, CategoryFragment.CategoryFragmentHost, BottomActionBar.BottomActionBarHost, FragmentTransactionChecker, PermissionRequester, CategorySelectorFragment.CategorySelectorFragmentHost, IndividualPickerFragment.IndividualPickerFragmentHost, WallpaperPreviewNavigator {
    public BottomActionBar mBottomActionBar;
    public WallpaperPickerDelegate mDelegate;
    public boolean mIsSafeToCommitFragmentTransaction;
    public int mNetworkStatus;
    public NetworkStatusNotifier.Listener mNetworkStatusListener;
    public NetworkStatusNotifier mNetworkStatusNotifier;
    public UserEventLogger mUserEventLogger;

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost, com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void cleanUp() {
        this.mDelegate.cleanUp();
    }

    @Override // com.android.wallpaper.picker.WallpapersUiContainer
    public void doneFetchingCategories() {
    }

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost, com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void fetchCategories() {
        WallpaperPickerDelegate wallpaperPickerDelegate = this.mDelegate;
        wallpaperPickerDelegate.initialize(!((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).mFetchedCategories);
    }

    @Override // com.android.wallpaper.widget.BottomActionBar.BottomActionBarHost
    public BottomActionBar getBottomActionBar() {
        return this.mBottomActionBar;
    }

    @Override // com.android.wallpaper.picker.WallpapersUiContainer
    public CategorySelectorFragment getCategorySelectorFragment() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (findFragmentById instanceof CategorySelectorFragment) {
            return (CategorySelectorFragment) findFragmentById;
        }
        return null;
    }

    @Override // com.android.wallpaper.picker.MyPhotosStarter.MyPhotosStarterProvider
    public MyPhotosStarter getMyPhotosStarter() {
        return this.mDelegate;
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost, com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public boolean isHostToolbarShown() {
        return false;
    }

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost
    public boolean isReadExternalStoragePermissionGranted() {
        return this.mDelegate.isReadExternalStoragePermissionGranted();
    }

    @Override // com.android.wallpaper.picker.FragmentTransactionChecker
    public boolean isSafeToCommitFragmentTransaction() {
        return this.mIsSafeToCommitFragmentTransaction;
    }

    @Override // com.android.wallpaper.picker.AppbarFragment.AppbarFragmentHost
    public boolean isUpArrowSupported() {
        return !ActivityUtils.isSUWMode(this);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void moveToPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!this.mDelegate.handleActivityResult(i, i2, intent)) {
            return;
        }
        if (ActivityUtils.isSUWMode(this)) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            setResult(0);
            finish();
            return;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setResult(-1);
        finish();
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.HOME");
        intent2.setFlags(268468224);
        startActivity(intent2);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if ((!(findFragmentById instanceof BottomActionBarFragment) || !((BottomActionBarFragment) findFragmentById).onBackPressed()) && !getSupportFragmentManager().popBackStackImmediate() && !moveTaskToBack(false)) {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Fragment fragment;
        Injector injector = InjectorProvider.getInjector();
        this.mDelegate = new WallpaperPickerDelegate(this, this, injector);
        this.mUserEventLogger = injector.getUserEventLogger(this);
        NetworkStatusNotifier networkStatusNotifier = injector.getNetworkStatusNotifier(this);
        this.mNetworkStatusNotifier = networkStatusNotifier;
        this.mNetworkStatus = ((DefaultNetworkStatusNotifier) networkStatusNotifier).getNetworkStatus();
        super.onCreate(bundle);
        setContentView(R.layout.activity_customization_picker);
        this.mBottomActionBar = (BottomActionBar) findViewById(R.id.bottom_actionbar);
        getWindow().setDecorFitsSystemWindows(ActivityUtils.isSUWMode(this));
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            if (getIntent() != null) {
                this.mUserEventLogger.logAppLaunched(getIntent());
            }
            injector.getPreferences(this).incrementAppLaunched();
            DailyLoggingAlarmScheduler.setAlarm(getApplicationContext());
            if ("wallpaper_only".equals(getIntent().getStringExtra("com.android.launcher3.WALLPAPER_FLAVOR"))) {
                String string = getString(R.string.wallpaper_app_name);
                fragment = new WallpaperOnlyFragment();
                fragment.setArguments(AppbarFragment.createArguments(string));
            } else {
                String string2 = getString(R.string.app_name);
                fragment = new CustomizationPickerFragment();
                fragment.setArguments(AppbarFragment.createArguments(string2));
            }
            BackStackRecord backStackRecord = new BackStackRecord(getSupportFragmentManager());
            backStackRecord.replace(R.id.fragment_container, fragment);
            backStackRecord.commitNow();
        }
        Intent intent = getIntent();
        String collectionId = R$id.getCollectionId(intent);
        if (!TextUtils.isEmpty(collectionId)) {
            switchFragmentWithBackStack(new CategorySelectorFragment());
            switchFragmentWithBackStack(InjectorProvider.getInjector().getIndividualPickerFragment(collectionId));
            intent.setData(null);
        }
        WallpaperPickerDelegate wallpaperPickerDelegate = this.mDelegate;
        boolean resetIfNeeded = ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).resetIfNeeded();
        ((DefaultCategoryProvider) wallpaperPickerDelegate.mCategoryProvider).fetchCategories(new CategoryReceiver(wallpaperPickerDelegate) { // from class: com.android.wallpaper.picker.WallpaperPickerDelegate.5
            @Override // com.android.wallpaper.model.CategoryReceiver
            public void doneFetchingCategories() {
            }

            @Override // com.android.wallpaper.model.CategoryReceiver
            public void onCategoryReceived(Category category) {
            }
        }, resetIfNeeded);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsSafeToCommitFragmentTransaction = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.mDelegate.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        boolean z = true;
        this.mIsSafeToCommitFragmentTransaction = true;
        boolean equals = "wallpaper_only".equals(getIntent().getStringExtra("com.android.launcher3.WALLPAPER_FLAVOR"));
        if (Settings.Global.getInt(getContentResolver(), "device_provisioned", 0) == 0) {
            z = false;
        }
        this.mUserEventLogger.logResumed(z, equals);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        if (this.mNetworkStatusListener == null) {
            CustomizationPickerActivity$$ExternalSyntheticLambda0 customizationPickerActivity$$ExternalSyntheticLambda0 = new NetworkStatusNotifier.Listener() { // from class: com.android.wallpaper.picker.CustomizationPickerActivity$$ExternalSyntheticLambda0
                @Override // com.android.wallpaper.module.NetworkStatusNotifier.Listener
                public final void onNetworkChanged(int i) {
                    CustomizationPickerActivity customizationPickerActivity = CustomizationPickerActivity.this;
                    if (i != customizationPickerActivity.mNetworkStatus) {
                        Log.i("CustomizationPickerActivity", "Network status changes, refresh wallpaper categories.");
                        customizationPickerActivity.mNetworkStatus = i;
                        customizationPickerActivity.mDelegate.initialize(true);
                    }
                }
            };
            this.mNetworkStatusListener = customizationPickerActivity$$ExternalSyntheticLambda0;
            ((DefaultNetworkStatusNotifier) this.mNetworkStatusNotifier).registerListener(customizationPickerActivity$$ExternalSyntheticLambda0);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mUserEventLogger.logStopped();
        NetworkStatusNotifier.Listener listener = this.mNetworkStatusListener;
        if (listener != null) {
            ((DefaultNetworkStatusNotifier) this.mNetworkStatusNotifier).unregisterListener(listener);
            this.mNetworkStatusListener = null;
        }
        super.onStop();
    }

    @Override // com.android.wallpaper.picker.AppbarFragment.AppbarFragmentHost
    public void onUpArrowPressed() {
        onBackPressed();
    }

    @Override // com.android.wallpaper.picker.WallpapersUiContainer
    public void onWallpapersReady() {
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void removeToolbarMenu() {
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void requestCustomPhotoPicker(MyPhotosStarter.PermissionChangedListener permissionChangedListener) {
        this.mDelegate.requestCustomPhotoPicker(permissionChangedListener);
    }

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost, com.android.wallpaper.model.PermissionRequester
    public void requestExternalStoragePermission(MyPhotosStarter.PermissionChangedListener permissionChangedListener) {
        this.mDelegate.requestExternalStoragePermission(permissionChangedListener);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarMenu(int i) {
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost, com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarTitle(CharSequence charSequence) {
    }

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost
    public void show(String str) {
        this.mDelegate.show(str);
    }

    @Override // com.android.wallpaper.picker.CategoryFragment.CategoryFragmentHost, com.android.wallpaper.model.WallpaperPreviewNavigator
    public void showViewOnlyPreview(WallpaperInfo wallpaperInfo, boolean z) {
        WallpaperPickerDelegate wallpaperPickerDelegate = this.mDelegate;
        InlinePreviewIntentFactory inlinePreviewIntentFactory = wallpaperPickerDelegate.mViewOnlyPreviewIntentFactory;
        ViewOnlyPreviewActivity.ViewOnlyPreviewActivityIntentFactory viewOnlyPreviewActivityIntentFactory = (ViewOnlyPreviewActivity.ViewOnlyPreviewActivityIntentFactory) inlinePreviewIntentFactory;
        viewOnlyPreviewActivityIntentFactory.mIsHomeAndLockPreviews = true;
        viewOnlyPreviewActivityIntentFactory.mIsViewAsHome = z;
        wallpaperInfo.showPreview(wallpaperPickerDelegate.mActivity, inlinePreviewIntentFactory, 2);
    }

    public final void switchFragmentWithBackStack(Fragment fragment) {
        BackStackRecord backStackRecord = new BackStackRecord(getSupportFragmentManager());
        backStackRecord.replace(R.id.fragment_container, fragment);
        backStackRecord.addToBackStack(null);
        backStackRecord.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override // com.android.wallpaper.picker.CategorySelectorFragment.CategorySelectorFragmentHost
    public void show(Category category) {
        if (!(category instanceof WallpaperCategory)) {
            this.mDelegate.show(category.mCollectionId);
            return;
        }
        switchFragmentWithBackStack(InjectorProvider.getInjector().getIndividualPickerFragment(category.mCollectionId));
    }
}
