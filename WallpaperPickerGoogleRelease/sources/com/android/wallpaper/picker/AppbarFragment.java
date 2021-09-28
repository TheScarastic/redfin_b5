package com.android.wallpaper.picker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.cardview.R$attr;
import androidx.fragment.app.FragmentActivity;
import com.android.systemui.shared.R;
import com.android.wallpaper.widget.BottomActionBar;
/* loaded from: classes.dex */
public abstract class AppbarFragment extends BottomActionBarFragment implements Toolbar.OnMenuItemClickListener {
    public AppbarFragmentHost mHost;
    public TextView mTitleView;
    public Toolbar mToolbar;
    public boolean mUpArrowEnabled;

    /* loaded from: classes.dex */
    public interface AppbarFragmentHost {
        boolean isUpArrowSupported();

        void onUpArrowPressed();
    }

    public static Bundle createArguments(CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("ToolbarFragment.title", charSequence);
        return bundle;
    }

    public CharSequence getDefaultTitle() {
        return null;
    }

    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mHost = (AppbarFragmentHost) context;
    }

    @Override // com.android.wallpaper.picker.BottomActionBarFragment
    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        bottomActionBar.findViewById(R.id.action_back).setVisibility((!this.mUpArrowEnabled || !this.mHost.isUpArrowSupported()) ? 0 : 8);
        this.mBottomActionBar = bottomActionBar;
        bottomActionBar.findViewById(R.id.action_back).setOnClickListener(new AppbarFragment$$ExternalSyntheticLambda0(getActivity()));
    }

    @Override // android.widget.Toolbar.OnMenuItemClickListener
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public void setTitle(CharSequence charSequence) {
        Toolbar toolbar = this.mToolbar;
        if (toolbar != null) {
            if (this.mTitleView != null) {
                toolbar.setTitle((CharSequence) null);
                this.mTitleView.setText(charSequence);
            } else {
                toolbar.setTitle(charSequence);
            }
            if (getActivity() != null) {
                FragmentActivity activity = getActivity();
                if (!TextUtils.isEmpty(null)) {
                    charSequence = null;
                }
                activity.setTitle(charSequence);
            }
        }
    }

    public void setUpToolbar(View view, boolean z) {
        CharSequence charSequence;
        this.mUpArrowEnabled = z;
        Toolbar toolbar = (Toolbar) view.findViewById(getToolbarId());
        this.mToolbar = toolbar;
        this.mTitleView = (TextView) toolbar.findViewById(R.id.custom_toolbar_title);
        Bundle bundle = this.mArguments;
        if (bundle != null) {
            charSequence = bundle.getCharSequence("ToolbarFragment.title", getDefaultTitle());
        } else {
            charSequence = getDefaultTitle();
        }
        if (!TextUtils.isEmpty(charSequence)) {
            setTitle(charSequence);
        }
        if (z && this.mHost.isUpArrowSupported()) {
            Drawable mutate = getResources().getDrawable(R.drawable.material_ic_arrow_back_black_24, null).mutate();
            mutate.setAutoMirrored(true);
            mutate.setTint(R$attr.getColorAttr(getActivity(), 16842806));
            this.mToolbar.setNavigationIcon(mutate);
            this.mToolbar.setNavigationContentDescription(R.string.bottom_action_bar_back);
            this.mToolbar.setNavigationOnClickListener(new AppbarFragment$$ExternalSyntheticLambda0(this));
        }
    }

    public void setUpToolbarMenu(int i) {
        this.mToolbar.inflateMenu(i);
        this.mToolbar.setOnMenuItemClickListener(this);
    }
}
