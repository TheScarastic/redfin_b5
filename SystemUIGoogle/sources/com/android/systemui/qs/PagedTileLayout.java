package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
/* loaded from: classes.dex */
public class PagedTileLayout extends ViewPager implements QSPanel.QSTileLayout {
    private static final Interpolator SCROLL_CUBIC = PagedTileLayout$$ExternalSyntheticLambda0.INSTANCE;
    private final PagerAdapter mAdapter;
    private AnimatorSet mBounceAnimatorSet;
    private int mExcessHeight;
    private int mLastExcessHeight;
    private float mLastExpansion;
    private boolean mListening;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener;
    private PageIndicator mPageIndicator;
    private float mPageIndicatorPosition;
    private PageListener mPageListener;
    private Scroller mScroller;
    private final ArrayList<QSPanelControllerBase.TileRecord> mTiles = new ArrayList<>();
    private final ArrayList<TileLayout> mPages = new ArrayList<>();
    private boolean mDistributeTiles = false;
    private int mPageToRestore = -1;
    private final UiEventLogger mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();
    private int mMinRows = 1;
    private int mMaxColumns = 100;
    private int mLastMaxHeight = -1;
    private int mLayoutOrientation = getResources().getConfiguration().orientation;
    private int mLayoutDirection = getLayoutDirection();

    /* loaded from: classes.dex */
    public interface PageListener {
        void onPageChanged(boolean z);
    }

    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PagedTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        AnonymousClass2 r0 = new ViewPager.SimpleOnPageChangeListener() { // from class: com.android.systemui.qs.PagedTileLayout.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                PagedTileLayout.this.updateSelected();
                if (PagedTileLayout.this.mPageIndicator != null && PagedTileLayout.this.mPageListener != null) {
                    PageListener pageListener = PagedTileLayout.this.mPageListener;
                    boolean z = false;
                    if (!PagedTileLayout.this.isLayoutRtl() ? i == 0 : i == PagedTileLayout.this.mPages.size() - 1) {
                        z = true;
                    }
                    pageListener.onPageChanged(z);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                if (PagedTileLayout.this.mPageIndicator != null) {
                    PagedTileLayout.this.mPageIndicatorPosition = ((float) i) + f;
                    PagedTileLayout.this.mPageIndicator.setLocation(PagedTileLayout.this.mPageIndicatorPosition);
                    if (PagedTileLayout.this.mPageListener != null) {
                        PageListener pageListener = PagedTileLayout.this.mPageListener;
                        boolean z = true;
                        if (i2 != 0 || (!PagedTileLayout.this.isLayoutRtl() ? i != 0 : i != PagedTileLayout.this.mPages.size() - 1)) {
                            z = false;
                        }
                        pageListener.onPageChanged(z);
                    }
                }
            }
        };
        this.mOnPageChangeListener = r0;
        AnonymousClass3 r1 = new PagerAdapter() { // from class: com.android.systemui.qs.PagedTileLayout.3
            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
                viewGroup.removeView((View) obj);
                PagedTileLayout.this.updateListening();
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i) {
                if (PagedTileLayout.this.isLayoutRtl()) {
                    i = (PagedTileLayout.this.mPages.size() - 1) - i;
                }
                ViewGroup viewGroup2 = (ViewGroup) PagedTileLayout.this.mPages.get(i);
                if (viewGroup2.getParent() != null) {
                    viewGroup.removeView(viewGroup2);
                }
                viewGroup.addView(viewGroup2);
                PagedTileLayout.this.updateListening();
                return viewGroup2;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return PagedTileLayout.this.mPages.size();
            }
        };
        this.mAdapter = r1;
        this.mScroller = new Scroller(context, SCROLL_CUBIC);
        setAdapter(r1);
        setOnPageChangeListener(r0);
        setCurrentItem(0, false);
    }

    public void setPageMargin(int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int i2 = -i;
        marginLayoutParams.setMarginStart(i2);
        marginLayoutParams.setMarginEnd(i2);
        setLayoutParams(marginLayoutParams);
        int size = this.mPages.size();
        for (int i3 = 0; i3 < size; i3++) {
            TileLayout tileLayout = this.mPages.get(i3);
            tileLayout.setPadding(i, tileLayout.getPaddingTop(), i, tileLayout.getPaddingBottom());
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void saveInstanceState(Bundle bundle) {
        bundle.putInt("current_page", getCurrentItem());
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void restoreInstanceState(Bundle bundle) {
        this.mPageToRestore = bundle.getInt("current_page", -1);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = this.mLayoutOrientation;
        int i2 = configuration.orientation;
        if (i != i2) {
            this.mLayoutOrientation = i2;
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            this.mLayoutDirection = i;
            setAdapter(this.mAdapter);
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void setCurrentItem(int i, boolean z) {
        if (isLayoutRtl()) {
            i = (this.mPages.size() - 1) - i;
        }
        super.setCurrentItem(i, z);
    }

    private int getCurrentPageNumber() {
        int currentItem = getCurrentItem();
        return this.mLayoutDirection == 1 ? (this.mPages.size() - 1) - currentItem : currentItem;
    }

    private void logVisibleTiles(TileLayout tileLayout) {
        for (int i = 0; i < tileLayout.mRecords.size(); i++) {
            QSTile qSTile = tileLayout.mRecords.get(i).tile;
            this.mUiEventLogger.logWithInstanceId(QSEvent.QS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            updateListening();
        }
    }

    public void updateListening() {
        Iterator<TileLayout> it = this.mPages.iterator();
        while (it.hasNext()) {
            TileLayout next = it.next();
            next.setListening(next.getParent() != null && this.mListening);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void fakeDragBy(float f) {
        try {
            super.fakeDragBy(f);
            postInvalidateOnAnimation();
        } catch (NullPointerException e) {
            Log.e("PagedTileLayout", "FakeDragBy called before begin", e);
            post(new Runnable(this.mPages.size() - 1) { // from class: com.android.systemui.qs.PagedTileLayout$$ExternalSyntheticLambda1
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PagedTileLayout.m172$r8$lambda$EW4Xw4iLCGdSRjqpDllHChOF4(PagedTileLayout.this, this.f$1);
                }
            });
        }
    }

    public /* synthetic */ void lambda$fakeDragBy$1(int i) {
        setCurrentItem(i, true);
        AnimatorSet animatorSet = this.mBounceAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
        setOffscreenPageLimit(1);
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void endFakeDrag() {
        try {
            super.endFakeDrag();
        } catch (NullPointerException e) {
            Log.e("PagedTileLayout", "endFakeDrag called without velocityTracker", e);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (!isFakeDragging()) {
                beginFakeDrag();
            }
            fakeDragBy((float) (getScrollX() - this.mScroller.getCurrX()));
        } else if (isFakeDragging()) {
            endFakeDrag();
            AnimatorSet animatorSet = this.mBounceAnimatorSet;
            if (animatorSet != null) {
                animatorSet.start();
            }
            setOffscreenPageLimit(1);
        }
        super.computeScroll();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPages.add(createTileLayout());
        this.mAdapter.notifyDataSetChanged();
    }

    private TileLayout createTileLayout() {
        TileLayout tileLayout = (TileLayout) LayoutInflater.from(getContext()).inflate(R$layout.qs_paged_page, (ViewGroup) this, false);
        tileLayout.setMinRows(this.mMinRows);
        tileLayout.setMaxColumns(this.mMaxColumns);
        return tileLayout;
    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.mPageIndicator = pageIndicator;
        pageIndicator.setNumPages(this.mPages.size());
        this.mPageIndicator.setLocation(this.mPageIndicatorPosition);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord) {
        ViewGroup viewGroup = (ViewGroup) tileRecord.tileView.getParent();
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getTop() + getTop();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mTiles.add(tileRecord);
        this.mDistributeTiles = true;
        requestLayout();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        if (this.mTiles.remove(tileRecord)) {
            this.mDistributeTiles = true;
            requestLayout();
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void setExpansion(float f, float f2) {
        this.mLastExpansion = f;
        updateSelected();
    }

    public void updateSelected() {
        float f = this.mLastExpansion;
        if (f <= 0.0f || f >= 1.0f) {
            boolean z = f == 1.0f;
            setImportantForAccessibility(4);
            int currentPageNumber = getCurrentPageNumber();
            int i = 0;
            while (i < this.mPages.size()) {
                TileLayout tileLayout = this.mPages.get(i);
                tileLayout.setSelected(i == currentPageNumber ? z : false);
                if (tileLayout.isSelected()) {
                    logVisibleTiles(tileLayout);
                }
                i++;
            }
            setImportantForAccessibility(0);
        }
    }

    public void setPageListener(PageListener pageListener) {
        this.mPageListener = pageListener;
    }

    private void distributeTiles() {
        emptyAndInflateOrRemovePages();
        int maxTiles = this.mPages.get(0).maxTiles();
        int size = this.mTiles.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            QSPanelControllerBase.TileRecord tileRecord = this.mTiles.get(i2);
            if (this.mPages.get(i).mRecords.size() == maxTiles) {
                i++;
            }
            this.mPages.get(i).addTile(tileRecord);
        }
    }

    private void emptyAndInflateOrRemovePages() {
        int numPages = getNumPages();
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            this.mPages.get(i).removeAllViews();
        }
        if (size != numPages) {
            while (this.mPages.size() < numPages) {
                this.mPages.add(createTileLayout());
            }
            while (this.mPages.size() > numPages) {
                ArrayList<TileLayout> arrayList = this.mPages;
                arrayList.remove(arrayList.size() - 1);
            }
            this.mPageIndicator.setNumPages(this.mPages.size());
            setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
            int i2 = this.mPageToRestore;
            if (i2 != -1) {
                setCurrentItem(i2, false);
                this.mPageToRestore = -1;
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean updateResources() {
        boolean z = false;
        for (int i = 0; i < this.mPages.size(); i++) {
            z |= this.mPages.get(i).updateResources();
        }
        if (z) {
            this.mDistributeTiles = true;
            requestLayout();
        }
        return z;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean setMinRows(int i) {
        this.mMinRows = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMinRows(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean setMaxColumns(int i) {
        this.mMaxColumns = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMaxColumns(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    public void setExcessHeight(int i) {
        this.mExcessHeight = i;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public void onMeasure(int i, int i2) {
        int size = this.mTiles.size();
        if (!(!this.mDistributeTiles && this.mLastMaxHeight == View.MeasureSpec.getSize(i2) && this.mLastExcessHeight == this.mExcessHeight)) {
            int size2 = View.MeasureSpec.getSize(i2);
            this.mLastMaxHeight = size2;
            int i3 = this.mExcessHeight;
            this.mLastExcessHeight = i3;
            if (this.mPages.get(0).updateMaxRows(size2 - i3, size) || this.mDistributeTiles) {
                this.mDistributeTiles = false;
                distributeTiles();
            }
            int i4 = this.mPages.get(0).mRows;
            for (int i5 = 0; i5 < this.mPages.size(); i5++) {
                this.mPages.get(i5).mRows = i4;
            }
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            int measuredHeight = getChildAt(i7).getMeasuredHeight();
            if (measuredHeight > i6) {
                i6 = measuredHeight;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), i6 + getPaddingBottom());
    }

    public int getColumnCount() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        return this.mPages.get(0).mColumns;
    }

    public int getNumPages() {
        int size = this.mTiles.size();
        int max = Math.max(size / this.mPages.get(0).maxTiles(), 1);
        return size > this.mPages.get(0).maxTiles() * max ? max + 1 : max;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getNumVisibleTiles() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        return this.mPages.get(getCurrentPageNumber()).mRecords.size();
    }

    public void startTileReveal(Set<String> set, final Runnable runnable) {
        if (!set.isEmpty() && this.mPages.size() >= 2 && getScrollX() == 0 && beginFakeDrag()) {
            int size = this.mPages.size() - 1;
            ArrayList arrayList = new ArrayList();
            Iterator<QSPanelControllerBase.TileRecord> it = this.mPages.get(size).mRecords.iterator();
            while (it.hasNext()) {
                QSPanelControllerBase.TileRecord next = it.next();
                if (set.contains(next.tile.getTileSpec())) {
                    arrayList.add(setupBounceAnimator(next.tileView, arrayList.size()));
                }
            }
            if (arrayList.isEmpty()) {
                endFakeDrag();
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            this.mBounceAnimatorSet = animatorSet;
            animatorSet.playTogether(arrayList);
            this.mBounceAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.PagedTileLayout.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PagedTileLayout.this.mBounceAnimatorSet = null;
                    runnable.run();
                }
            });
            setOffscreenPageLimit(size);
            int width = getWidth() * size;
            Scroller scroller = this.mScroller;
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            if (isLayoutRtl()) {
                width = -width;
            }
            scroller.startScroll(scrollX, scrollY, width, 0, 750);
            postInvalidateOnAnimation();
        }
    }

    private static Animator setupBounceAnimator(View view, int i) {
        view.setAlpha(0.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f));
        ofPropertyValuesHolder.setDuration(450L);
        ofPropertyValuesHolder.setStartDelay((long) (i * 85));
        ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator(1.3f));
        return ofPropertyValuesHolder;
    }
}
