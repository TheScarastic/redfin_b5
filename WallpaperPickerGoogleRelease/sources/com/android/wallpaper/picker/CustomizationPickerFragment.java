package com.android.wallpaper.picker;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.customization.picker.grid.GridFragment$$ExternalSyntheticLambda1;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.widget.BottomActionBar$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class CustomizationPickerFragment extends AppbarFragment implements CustomizationSectionController.CustomizationSectionNavigationController {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Bundle mBackStackSavedInstanceState;
    public NestedScrollView mNestedScrollView;
    public final List<CustomizationSectionController<?>> mSectionControllers = new ArrayList();

    public List<CustomizationSectionController<?>> getAvailableSections(List<CustomizationSectionController<?>> list) {
        return (List) list.stream().filter(new GridFragment$$ExternalSyntheticLambda1(this)).collect(Collectors.toList());
    }

    @Override // com.android.wallpaper.picker.AppbarFragment
    public int getToolbarId() {
        return R.id.action_bar;
    }

    public void navigateTo(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        BackStackRecord backStackRecord = new BackStackRecord(supportFragmentManager);
        backStackRecord.replace(R.id.fragment_container, fragment);
        backStackRecord.addToBackStack(null);
        backStackRecord.commit();
        supportFragmentManager.executePendingTransactions();
    }

    @Override // com.android.wallpaper.picker.BottomActionBarFragment
    public boolean onBackPressed() {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(":settings:fragment_args_key")) {
            this.mSectionControllers.forEach(CustomizationPickerFragment$$ExternalSyntheticLambda1.INSTANCE);
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0055, code lost:
        if ((r0 != null && r0.hasExtra(":settings:fragment_args_key")) != false) goto L_0x0057;
     */
    @Override // androidx.fragment.app.Fragment
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View onCreateView(android.view.LayoutInflater r17, android.view.ViewGroup r18, android.os.Bundle r19) {
        /*
        // Method dump skipped, instructions count: 325
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.picker.CustomizationPickerFragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle):android.view.View");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        Bundle bundle = new Bundle();
        this.mBackStackSavedInstanceState = bundle;
        onSaveInstanceStateInternal(bundle);
        this.mSectionControllers.forEach(CustomizationPickerFragment$$ExternalSyntheticLambda2.INSTANCE);
        this.mSectionControllers.clear();
        this.mCalled = true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        onSaveInstanceStateInternal(bundle);
    }

    public final void onSaveInstanceStateInternal(Bundle bundle) {
        NestedScrollView nestedScrollView = this.mNestedScrollView;
        if (nestedScrollView != null) {
            bundle.putInt("SCROLL_POSITION_Y", nestedScrollView.getScrollY());
        }
        this.mSectionControllers.forEach(new BottomActionBar$$ExternalSyntheticLambda4(bundle));
    }
}
