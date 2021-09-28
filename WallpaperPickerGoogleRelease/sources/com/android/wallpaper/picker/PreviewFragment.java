package com.android.wallpaper.picker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.module.WallpaperSetter;
import com.android.wallpaper.picker.LoadWallpaperErrorDialogFragment;
import com.android.wallpaper.picker.SetWallpaperDialogFragment;
import com.android.wallpaper.picker.SetWallpaperErrorDialogFragment;
import com.android.wallpaper.util.ActivityUtils;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.WallpaperInfoView;
import com.google.android.material.tabs.TabLayout;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public abstract class PreviewFragment extends AppbarFragment implements SetWallpaperDialogFragment.Listener, SetWallpaperErrorDialogFragment.Listener, LoadWallpaperErrorDialogFragment.Listener {
    public static final Interpolator ALPHA_OUT = new PathInterpolator(0.0f, 0.0f, 0.8f, 1.0f);
    public BottomActionBar mBottomActionBar;
    public FullScreenAnimation mFullScreenAnimation;
    public Optional<Integer> mLastSelectedTabPositionOptional = Optional.empty();
    public OnBackPressedCallback mOnBackPressedCallback;
    public WallpaperPreviewBitmapTransformation mPreviewBitmapTransformation;
    public View mRootView;
    public LoadWallpaperErrorDialogFragment mStagedLoadWallpaperErrorDialogFragment;
    public SetWallpaperErrorDialogFragment mStagedSetWallpaperErrorDialogFragment;
    public boolean mTestingModeEnabled;
    public UserEventLogger mUserEventLogger;
    public boolean mViewAsHome;
    public WallpaperInfo mWallpaper;
    public WallpaperSetter mWallpaperSetter;

    /* loaded from: classes.dex */
    public final class WallpaperInfoContent extends BottomActionBar.BottomSheetContent<WallpaperInfoView> {
        public CharSequence mActionLabel;
        public Intent mExploreIntent;

        public WallpaperInfoContent(Context context) {
            super(context);
        }

        @Override // com.android.wallpaper.widget.BottomActionBar.BottomSheetContent
        public int getViewId() {
            return R.layout.wallpaper_info_view;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [android.view.View] */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x0041  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x007a  */
        /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
        @Override // com.android.wallpaper.widget.BottomActionBar.BottomSheetContent
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onViewCreated(com.android.wallpaper.widget.WallpaperInfoView r6) {
            /*
                r5 = this;
                com.android.wallpaper.widget.WallpaperInfoView r6 = (com.android.wallpaper.widget.WallpaperInfoView) r6
                com.android.wallpaper.picker.PreviewFragment r0 = com.android.wallpaper.picker.PreviewFragment.this
                com.android.wallpaper.model.WallpaperInfo r1 = r0.mWallpaper
                if (r1 != 0) goto L_0x000a
                goto L_0x0081
            L_0x000a:
                java.lang.CharSequence r1 = r5.mActionLabel
                if (r1 != 0) goto L_0x007e
                com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0 r1 = new com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0
                r1.<init>(r5, r6)
                android.content.Context r6 = r0.getContext()
                if (r6 != 0) goto L_0x001a
                goto L_0x0081
            L_0x001a:
                com.android.wallpaper.picker.PreviewFragment r0 = com.android.wallpaper.picker.PreviewFragment.this
                com.android.wallpaper.model.WallpaperInfo r0 = r0.mWallpaper
                com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda1 r2 = new com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda1
                r2.<init>(r5, r1)
                java.lang.String r5 = r0.getActionUrl(r6)
                boolean r1 = r0 instanceof com.android.wallpaper.model.LiveWallpaperInfo
                r3 = 0
                if (r1 == 0) goto L_0x003a
                r1 = r0
                com.android.wallpaper.model.LiveWallpaperInfo r1 = (com.android.wallpaper.model.LiveWallpaperInfo) r1
                android.app.WallpaperInfo r1 = r1.mInfo     // Catch: NotFoundException -> 0x003a
                android.content.pm.PackageManager r4 = r6.getPackageManager()     // Catch: NotFoundException -> 0x003a
                java.lang.CharSequence r1 = r1.loadContextDescription(r4)     // Catch: NotFoundException -> 0x003a
                goto L_0x003b
            L_0x003a:
                r1 = r3
            L_0x003b:
                boolean r4 = android.text.TextUtils.isEmpty(r1)
                if (r4 == 0) goto L_0x0049
                int r1 = r0.getActionLabelRes(r6)
                java.lang.String r1 = r6.getString(r1)
            L_0x0049:
                if (r5 == 0) goto L_0x006c
                boolean r5 = r5.isEmpty()
                if (r5 != 0) goto L_0x006c
                java.lang.String r5 = r0.getActionUrl(r6)
                android.net.Uri r5 = android.net.Uri.parse(r5)
                com.android.wallpaper.module.Injector r0 = com.android.wallpaper.module.InjectorProvider.getInjector()
                com.android.wallpaper.module.ExploreIntentChecker r6 = r0.getExploreIntentChecker(r6)
                com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda1 r0 = new com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda1
                r0.<init>(r2, r1)
                com.android.wallpaper.module.DefaultExploreIntentChecker r6 = (com.android.wallpaper.module.DefaultExploreIntentChecker) r6
                r6.fetchValidActionViewIntent(r5, r0)
                goto L_0x0081
            L_0x006c:
                java.lang.Object r5 = r2.f$0
                com.android.wallpaper.picker.PreviewFragment$WallpaperInfoContent r5 = (com.android.wallpaper.picker.PreviewFragment.WallpaperInfoContent) r5
                java.lang.Object r6 = r2.f$1
                java.lang.Runnable r6 = (java.lang.Runnable) r6
                r5.mActionLabel = r1
                r5.mExploreIntent = r3
                if (r6 == 0) goto L_0x0081
                r6.run()
                goto L_0x0081
            L_0x007e:
                r5.populateWallpaperInfo(r6)
            L_0x0081:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.PreviewFragment.WallpaperInfoContent.onViewCreated(android.view.View):void");
        }

        public final void populateWallpaperInfo(WallpaperInfoView wallpaperInfoView) {
            PreviewFragment previewFragment = PreviewFragment.this;
            WallpaperInfo wallpaperInfo = previewFragment.mWallpaper;
            CharSequence charSequence = this.mActionLabel;
            boolean z = this.mExploreIntent != null && !ActivityUtils.isSUWMode(previewFragment.getContext());
            AppbarFragment$$ExternalSyntheticLambda0 appbarFragment$$ExternalSyntheticLambda0 = new AppbarFragment$$ExternalSyntheticLambda0(this);
            List<String> attributions = wallpaperInfo.getAttributions(wallpaperInfoView.getContext());
            wallpaperInfoView.mTitle.setText((CharSequence) null);
            wallpaperInfoView.mSubtitle1.setText((CharSequence) null);
            wallpaperInfoView.mSubtitle1.setVisibility(8);
            wallpaperInfoView.mSubtitle2.setText((CharSequence) null);
            wallpaperInfoView.mSubtitle2.setVisibility(8);
            wallpaperInfoView.mExploreButton.setText((CharSequence) null);
            wallpaperInfoView.mExploreButton.setOnClickListener(null);
            wallpaperInfoView.mExploreButton.setVisibility(8);
            if (attributions.size() > 0 && attributions.get(0) != null) {
                wallpaperInfoView.mTitle.setText(attributions.get(0));
            }
            android.app.WallpaperInfo wallpaperComponent = wallpaperInfo.getWallpaperComponent();
            if (wallpaperComponent == null || wallpaperComponent.getShowMetadataInPreview()) {
                if (attributions.size() > 1 && attributions.get(1) != null) {
                    wallpaperInfoView.mSubtitle1.setVisibility(0);
                    wallpaperInfoView.mSubtitle1.setText(attributions.get(1));
                }
                if (attributions.size() > 2 && attributions.get(2) != null) {
                    wallpaperInfoView.mSubtitle2.setVisibility(0);
                    wallpaperInfoView.mSubtitle2.setText(attributions.get(2));
                }
                if (z) {
                    wallpaperInfoView.mExploreButton.setVisibility(0);
                    wallpaperInfoView.mExploreButton.setText(charSequence);
                    wallpaperInfoView.mExploreButton.setOnClickListener(appbarFragment$$ExternalSyntheticLambda0);
                }
            }
        }
    }

    public WorkspaceSurfaceHolderCallback createWorkspaceSurfaceCallback(SurfaceView surfaceView) {
        return new WorkspaceSurfaceHolderCallback(surfaceView, getContext(), false);
    }

    public void finishActivity(boolean z) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (z) {
                try {
                    Toast.makeText(activity, (int) R.string.wallpaper_set_successfully_message, 0).show();
                } catch (Resources.NotFoundException e) {
                    Log.e("PreviewFragment", "Could not show toast " + e);
                }
                activity.setResult(-1);
            }
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            activity.finish();
        }
    }

    public List<String> getAttributions(Context context) {
        return this.mWallpaper.getAttributions(context);
    }

    @Override // com.android.wallpaper.picker.AppbarFragment
    public CharSequence getDefaultTitle() {
        return getContext().getString(R.string.preview);
    }

    public abstract int getLayoutResId();

    @Override // com.android.wallpaper.picker.AppbarFragment, com.android.wallpaper.picker.BottomActionBarFragment
    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        super.onBottomActionBarReady(bottomActionBar);
        this.mBottomActionBar = bottomActionBar;
        bottomActionBar.setActionClickListener(BottomActionBar.BottomAction.EDIT, new PreviewFragment$$ExternalSyntheticLambda1(this, 0));
        setFullScreenActions(this.mRootView.findViewById(R.id.fullscreen_buttons_container));
        if (this.mOnBackPressedCallback == null) {
            this.mOnBackPressedCallback = new OnBackPressedCallback(true) { // from class: com.android.wallpaper.picker.PreviewFragment.1
                @Override // androidx.activity.OnBackPressedCallback
                public void handleOnBackPressed() {
                    PreviewFragment previewFragment = PreviewFragment.this;
                    FullScreenAnimation fullScreenAnimation = previewFragment.mFullScreenAnimation;
                    boolean z = false;
                    if (fullScreenAnimation.mIsFullScreen) {
                        fullScreenAnimation.startAnimation(false);
                        return;
                    }
                    BottomActionBar bottomActionBar2 = previewFragment.mBottomActionBar;
                    if (bottomActionBar2 != null) {
                        if (bottomActionBar2.mBottomSheetBehavior.state == 4) {
                            z = true;
                        }
                        if (!z) {
                            bottomActionBar2.hideBottomSheetAndDeselectButtonIfExpanded();
                            return;
                        }
                    }
                    previewFragment.getActivity().finish();
                }
            };
            getActivity().mOnBackPressedDispatcher.addCallback(this, this.mOnBackPressedCallback);
        }
    }

    @Override // com.android.wallpaper.picker.LoadWallpaperErrorDialogFragment.Listener
    public void onClickOk() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override // com.android.wallpaper.picker.SetWallpaperErrorDialogFragment.Listener
    public void onClickTryAgain(int i) {
        setCurrentWallpaper(i);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context applicationContext = getContext().getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        this.mUserEventLogger = injector.getUserEventLogger(applicationContext);
        this.mWallpaper = (WallpaperInfo) this.mArguments.getParcelable("wallpaper");
        boolean z = true;
        if (getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        this.mPreviewBitmapTransformation = new WallpaperPreviewBitmapTransformation(applicationContext, z);
        this.mArguments.getInt("preview_mode");
        this.mViewAsHome = this.mArguments.getBoolean("view_as_home");
        this.mTestingModeEnabled = this.mArguments.getBoolean("testing_mode_enabled");
        this.mWallpaperSetter = new WallpaperSetter(injector.getWallpaperPersister(applicationContext), injector.getPreferences(applicationContext), this.mUserEventLogger, this.mTestingModeEnabled);
        FragmentActivity activity = getActivity();
        List<String> attributions = getAttributions(activity);
        if (attributions.size() > 0 && attributions.get(0) != null) {
            activity.setTitle(attributions.get(0));
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        setUpToolbar(inflate, true);
        this.mRootView = inflate;
        this.mFullScreenAnimation = new FullScreenAnimation(inflate);
        getActivity().getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.wallpaper.picker.PreviewFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                PreviewFragment previewFragment = PreviewFragment.this;
                Interpolator interpolator = PreviewFragment.ALPHA_OUT;
                Objects.requireNonNull(previewFragment);
                view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), 0);
                FullScreenAnimation fullScreenAnimation = previewFragment.mFullScreenAnimation;
                Objects.requireNonNull(fullScreenAnimation);
                Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
                fullScreenAnimation.mStatusBarHeight = insetsIgnoringVisibility.top;
                fullScreenAnimation.mNavigationBarHeight = insetsIgnoringVisibility.bottom;
                FullScreenAnimation fullScreenAnimation2 = previewFragment.mFullScreenAnimation;
                int i = fullScreenAnimation2.mStatusBarHeight;
                TypedArray obtainStyledAttributes = fullScreenAnimation2.mView.getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
                int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, 0);
                obtainStyledAttributes.recycle();
                fullScreenAnimation2.setViewMargins(R.id.screen_preview_layout, (float) (dimensionPixelSize + i), fullScreenAnimation2.mView.getResources().getDimension(R.dimen.separated_tabs_height) + fullScreenAnimation2.mView.getResources().getDimension(R.dimen.bottom_actions_height) + ((float) fullScreenAnimation2.mNavigationBarHeight), false);
                fullScreenAnimation2.setViewMargins(R.id.bottom_action_bar_container, 0.0f, (float) fullScreenAnimation2.mNavigationBarHeight, false);
                fullScreenAnimation2.setViewMargins(R.id.separated_tabs_container, 0.0f, fullScreenAnimation2.mView.getResources().getDimension(R.dimen.bottom_actions_height) + ((float) fullScreenAnimation2.mNavigationBarHeight), true);
                fullScreenAnimation2.ensureToolbarIsCorrectlyLocated();
                return windowInsets.consumeSystemWindowInsets();
            }
        });
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mCalled = true;
        this.mWallpaperSetter.cleanUp();
    }

    @Override // com.android.wallpaper.picker.SetWallpaperDialogFragment.Listener
    public void onDialogDismissed(boolean z) {
        this.mBottomActionBar.deselectAction(BottomActionBar.BottomAction.APPLY);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        this.mCalled = true;
        InjectorProvider.getInjector().getPreferences(getActivity()).setLastAppActiveTimestamp(new Date().getTime());
        LoadWallpaperErrorDialogFragment loadWallpaperErrorDialogFragment = this.mStagedLoadWallpaperErrorDialogFragment;
        if (loadWallpaperErrorDialogFragment != null) {
            loadWallpaperErrorDialogFragment.show(getParentFragmentManager(), "load_wallpaper_error_dialog");
            this.mStagedLoadWallpaperErrorDialogFragment = null;
        }
        SetWallpaperErrorDialogFragment setWallpaperErrorDialogFragment = this.mStagedSetWallpaperErrorDialogFragment;
        if (setWallpaperErrorDialogFragment != null) {
            setWallpaperErrorDialogFragment.show(getParentFragmentManager(), "set_wallpaper_error_dialog");
            this.mStagedSetWallpaperErrorDialogFragment = null;
        }
    }

    @Override // com.android.wallpaper.picker.SetWallpaperDialogFragment.Listener
    public void onSet(int i) {
        setCurrentWallpaper(i);
    }

    public void onSetWallpaperClicked(View view) {
        this.mWallpaperSetter.requestDestination(getActivity(), this.mFragmentManager, this, this.mWallpaper instanceof LiveWallpaperInfo);
    }

    public abstract void setCurrentWallpaper(int i);

    public void setFullScreenActions(View view) {
        Button button = (Button) view.findViewById(R.id.hide_ui_preview_button);
        button.setText(this.mFullScreenAnimation.mWorkspaceVisibility ? R.string.hide_ui_preview_text : R.string.show_ui_preview_text);
        button.setOnClickListener(new PreviewFragment$$ExternalSyntheticLambda1(this, 1));
        view.findViewById(R.id.set_as_wallpaper_button).setOnClickListener(new PreviewFragment$$ExternalSyntheticLambda1(this, 2));
        FullScreenAnimation fullScreenAnimation = this.mFullScreenAnimation;
        fullScreenAnimation.mView.findViewById(R.id.bottom_actionbar).setTranslationY(fullScreenAnimation.mIsFullScreen ? fullScreenAnimation.mBottomActionBarTranslation : 0.0f);
    }

    public void setUpTabs(TabLayout tabLayout) {
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setText(R.string.home_screen_message);
        tabLayout.addTab(newTab);
        TabLayout.Tab newTab2 = tabLayout.newTab();
        newTab2.setText(R.string.lock_screen_message);
        tabLayout.addTab(newTab2);
        AnonymousClass2 r0 = new TabLayout.BaseOnTabSelectedListener() { // from class: com.android.wallpaper.picker.PreviewFragment.2
            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                PreviewFragment.this.mLastSelectedTabPositionOptional = Optional.of(Integer.valueOf(tab.position));
                PreviewFragment.this.updateScreenPreview(tab.position == 0);
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        };
        if (!tabLayout.selectedListeners.contains(r0)) {
            tabLayout.selectedListeners.add(r0);
        }
        int intValue = this.mLastSelectedTabPositionOptional.orElseGet(new PreviewFragment$$ExternalSyntheticLambda2(this)).intValue();
        tabLayout.getTabAt(intValue).select();
        updateScreenPreview(intValue == 0);
    }

    public void showLoadWallpaperErrorDialog() {
        LoadWallpaperErrorDialogFragment loadWallpaperErrorDialogFragment = new LoadWallpaperErrorDialogFragment();
        loadWallpaperErrorDialogFragment.setTargetFragment(this, 1);
        BasePreviewActivity basePreviewActivity = (BasePreviewActivity) getActivity();
        if (basePreviewActivity == null || !basePreviewActivity.mIsSafeToCommitFragmentTransaction) {
            this.mStagedLoadWallpaperErrorDialogFragment = loadWallpaperErrorDialogFragment;
        } else {
            loadWallpaperErrorDialogFragment.show(getParentFragmentManager(), "load_wallpaper_error_dialog");
        }
    }

    public void showSetWallpaperErrorDialog(int i) {
        SetWallpaperErrorDialogFragment newInstance = SetWallpaperErrorDialogFragment.newInstance(R.string.set_wallpaper_error_message, i);
        newInstance.setTargetFragment(this, 1);
        if (((BasePreviewActivity) requireActivity()).mIsSafeToCommitFragmentTransaction) {
            newInstance.show(getParentFragmentManager(), "set_wallpaper_error_dialog");
        } else {
            this.mStagedSetWallpaperErrorDialogFragment = newInstance;
        }
    }

    public abstract void updateScreenPreview(boolean z);
}
