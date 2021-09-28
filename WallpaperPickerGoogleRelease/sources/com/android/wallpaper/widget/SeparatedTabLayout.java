package com.android.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.viewpager2.widget.ViewPager2;
import com.android.systemui.shared.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public final class SeparatedTabLayout extends TabLayout {

    /* loaded from: classes.dex */
    public static class SeparatedTabLayoutOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        public int mPreviousScrollState = 0;
        public int mScrollState = 0;
        public final WeakReference<TabLayout> mTabLayoutRef;

        public SeparatedTabLayoutOnPageChangeCallback(TabLayout tabLayout, AnonymousClass1 r2) {
            this.mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrollStateChanged(int i) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = i;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrolled(int i, float f, int i2) {
            if (f == 0.0f) {
                boolean z = true;
                if (!(this.mPreviousScrollState == 1 && this.mScrollState == 2)) {
                    z = false;
                }
                if (z) {
                    updateTabPositionIfNeeded(i);
                }
            }
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int i) {
            boolean z = true;
            if (!(this.mPreviousScrollState == 1 && this.mScrollState == 2)) {
                z = false;
            }
            if (!z) {
                updateTabPositionIfNeeded(i);
            }
        }

        public final void updateTabPositionIfNeeded(int i) {
            TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != i && i < tabLayout.getTabCount()) {
                tabLayout.selectTab(tabLayout.getTabAt(i), true);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SeparatedTabLayoutOnTabSelectedListener implements TabLayout.BaseOnTabSelectedListener {
        public final WeakReference<ViewPager2> mViewPagerRef;

        public SeparatedTabLayoutOnTabSelectedListener(ViewPager2 viewPager2, AnonymousClass1 r2) {
            this.mViewPagerRef = new WeakReference<>(viewPager2);
        }

        @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
        public void onTabReselected(TabLayout.Tab tab) {
        }

        @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
        public void onTabSelected(TabLayout.Tab tab) {
            int i;
            ViewPager2 viewPager2 = this.mViewPagerRef.get();
            if (viewPager2 != null && viewPager2.mCurrentItem != (i = tab.position)) {
                viewPager2.setCurrentItem(i, true);
            }
        }

        @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
        public void onTabUnselected(TabLayout.Tab tab) {
        }
    }

    public SeparatedTabLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.material.tabs.TabLayout
    public TabLayout.Tab newTab() {
        TabLayout.Tab newTab = super.newTab();
        newTab.view.setBackgroundResource(R.drawable.separated_tabs_ripple_mask);
        return newTab;
    }
}
