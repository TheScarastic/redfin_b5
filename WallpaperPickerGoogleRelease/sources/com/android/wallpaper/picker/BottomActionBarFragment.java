package com.android.wallpaper.picker;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.systemui.shared.R;
import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.BottomActionBar$$ExternalSyntheticLambda4;
import com.android.wallpaper.widget.BottomActionBar$$ExternalSyntheticLambda7;
/* loaded from: classes.dex */
public class BottomActionBarFragment extends Fragment {
    public BottomActionBar mBottomActionBar;

    public boolean onBackPressed() {
        return false;
    }

    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        this.mBottomActionBar = bottomActionBar;
        bottomActionBar.findViewById(R.id.action_back).setOnClickListener(new AppbarFragment$$ExternalSyntheticLambda0(getActivity()));
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        BottomActionBar bottomActionBar;
        FragmentActivity activity = getActivity();
        if (activity instanceof BottomActionBar.BottomActionBarHost) {
            bottomActionBar = ((BottomActionBar.BottomActionBarHost) activity).getBottomActionBar();
        } else {
            View view2 = this.mView;
            bottomActionBar = view2 != null ? (BottomActionBar) view2.findViewById(R.id.bottom_actionbar) : null;
        }
        this.mBottomActionBar = bottomActionBar;
        if (bottomActionBar != null) {
            bottomActionBar.setVisibility(8);
            bottomActionBar.showActionsOnly(new BottomActionBar.BottomAction[0]);
            bottomActionBar.enableActions();
            bottomActionBar.mActionMap.values().forEach(BottomActionBar$$ExternalSyntheticLambda7.INSTANCE);
            bottomActionBar.findViewById(R.id.action_back).setOnClickListener(null);
            bottomActionBar.mActionMap.keySet().forEach(new BottomActionBar$$ExternalSyntheticLambda4(bottomActionBar));
            bottomActionBar.mContentViewMap.clear();
            bottomActionBar.mActionSelectedListeners.clear();
            bottomActionBar.mBottomSheetView.removeAllViews();
            BottomActionBar.QueueStateBottomSheetBehavior<ViewGroup> queueStateBottomSheetBehavior = bottomActionBar.mBottomSheetBehavior;
            queueStateBottomSheetBehavior.mStateQueue.clear();
            queueStateBottomSheetBehavior.mIsQueueProcessing = false;
            bottomActionBar.mSelectedAction = null;
            onBottomActionBarReady(this.mBottomActionBar);
        }
    }
}
