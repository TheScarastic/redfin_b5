package com.android.wallpaper.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.systemui.shared.R;
import com.android.wallpaper.R$styleable;
import com.android.wallpaper.util.ScreenSizeCalculator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class PreviewPager extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public PagerAdapter mAdapter;
    public ViewPager.OnPageChangeListener mExternalPageListener;
    public final View mNextArrow;
    public final PageIndicator mPageIndicator;
    public final ViewPager.OnPageChangeListener mPageListener;
    public int mPageStyle;
    public final View mPreviousArrow;
    public float mScreenAspectRatio;
    public final ViewPager mViewPager;

    /* loaded from: classes.dex */
    public static class PreviewPagerScroller extends Scroller {
        public PreviewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override // android.widget.Scroller
        public void startScroll(int i, int i2, int i3, int i4, int i5) {
            super.startScroll(i, i2, i3, i4, 500);
        }
    }

    public PreviewPager(Context context) {
        this(context, null);
    }

    public final void initIndicator() {
        PageIndicator pageIndicator = this.mPageIndicator;
        int count = this.mAdapter.getCount();
        pageIndicator.setVisibility(count > 1 ? 0 : 4);
        if (pageIndicator.mAnimating) {
            Log.w("PageIndicator", "setNumPages during animation");
        }
        while (count < pageIndicator.getChildCount()) {
            pageIndicator.removeViewAt(pageIndicator.getChildCount() - 1);
        }
        TypedArray obtainStyledAttributes = pageIndicator.getContext().obtainStyledAttributes(new int[]{16843818});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        while (count > pageIndicator.getChildCount()) {
            ImageView imageView = new ImageView(pageIndicator.getContext());
            imageView.setImageResource(R.drawable.minor_a_b);
            imageView.setImageTintList(ColorStateList.valueOf(color));
            pageIndicator.addView(imageView, new ViewGroup.LayoutParams(pageIndicator.mPageIndicatorWidth, pageIndicator.mPageIndicatorHeight));
        }
        pageIndicator.setIndex(pageIndicator.mPosition >> 1);
        this.mPageIndicator.setLocation((float) this.mViewPager.mCurItem);
    }

    public boolean isRtl() {
        ViewPager viewPager = this.mViewPager;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (viewPager.isLayoutDirectionResolved()) {
            return this.mViewPager.getLayoutDirection() == 1;
        }
        Locale locale = Locale.getDefault();
        int i = TextUtilsCompat.$r8$clinit;
        return TextUtils.getLayoutDirectionFromLocale(locale) == 1;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.mPageStyle == 1) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2) - ((View) this.mPageIndicator.getParent()).getLayoutParams().height;
            if (size > 0) {
                int paddingBottom = (size / 2) - (((int) (((float) ((size2 - this.mViewPager.getPaddingBottom()) - this.mViewPager.getPaddingTop())) / this.mScreenAspectRatio)) / 2);
                ViewPager viewPager = this.mViewPager;
                viewPager.setPaddingRelative(paddingBottom, viewPager.getPaddingTop(), paddingBottom, this.mViewPager.getPaddingBottom());
            }
        }
        super.onMeasure(i, i2);
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter == null) {
            this.mAdapter = null;
            this.mViewPager.setAdapter(null);
            return;
        }
        int i = 0;
        if (this.mViewPager.mAdapter != null) {
            if (isRtl()) {
                i = (this.mAdapter.getCount() - 1) - this.mViewPager.mCurItem;
            } else {
                i = this.mViewPager.mCurItem;
            }
        }
        this.mAdapter = pagerAdapter;
        this.mViewPager.setAdapter(pagerAdapter);
        ViewPager viewPager = this.mViewPager;
        if (isRtl()) {
            i = (this.mAdapter.getCount() - 1) - i;
        }
        viewPager.setCurrentItem(i);
        PagerAdapter pagerAdapter2 = this.mAdapter;
        pagerAdapter2.mObservable.registerObserver(new DataSetObserver() { // from class: com.android.wallpaper.widget.PreviewPager.3
            @Override // android.database.DataSetObserver
            public void onChanged() {
                PreviewPager previewPager = PreviewPager.this;
                int i2 = PreviewPager.$r8$clinit;
                previewPager.initIndicator();
            }
        });
        initIndicator();
        updateIndicator(this.mViewPager.mCurItem);
    }

    public final void updateIndicator(int i) {
        int count = this.mAdapter.getCount();
        int i2 = 8;
        if (count > 1) {
            this.mPreviousArrow.setVisibility(i != 0 ? 0 : 8);
            View view = this.mNextArrow;
            if (i != count - 1) {
                i2 = 0;
            }
            view.setVisibility(i2);
            return;
        }
        this.mPageIndicator.setVisibility(8);
        this.mPreviousArrow.setVisibility(8);
        this.mNextArrow.setVisibility(8);
    }

    public PreviewPager(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PreviewPager(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(R.layout.preview_pager, this);
        Resources resources = context.getResources();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PreviewPager, i, 0);
        this.mPageStyle = obtainStyledAttributes.getInteger(0, 0);
        obtainStyledAttributes.recycle();
        ViewPager viewPager = (ViewPager) findViewById(R.id.preview_viewpager);
        this.mViewPager = viewPager;
        viewPager.setPageTransformer(false, new PreviewPager$$ExternalSyntheticLambda1(this), 0);
        viewPager.setPageMargin(resources.getDimensionPixelOffset(R.dimen.preview_page_gap));
        viewPager.setClipToPadding(false);
        int i2 = this.mPageStyle;
        if (i2 == 0) {
            int max = Math.max(resources.getDimensionPixelOffset(R.dimen.preview_page_horizontal_margin), viewPager.getResources().getDisplayMetrics().widthPixels / 8);
            viewPager.setPadding(max, resources.getDimensionPixelOffset(R.dimen.preview_page_top_margin), max, resources.getDimensionPixelOffset(R.dimen.preview_page_bottom_margin));
        } else if (i2 == 1) {
            Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService(WindowManager.class)).getDefaultDisplay());
            this.mScreenAspectRatio = ((float) screenSize.y) / ((float) screenSize.x);
            viewPager.setPadding(0, resources.getDimensionPixelOffset(R.dimen.preview_page_top_margin), 0, resources.getDimensionPixelOffset(R.dimen.preview_page_bottom_margin));
            viewPager.setPageMargin(screenSize.x / 2);
            viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.wallpaper.widget.PreviewPager.1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
                    PreviewPager.this.mViewPager.setPageMargin(view.getPaddingEnd());
                    PreviewPager.this.mViewPager.removeOnLayoutChangeListener(this);
                }
            });
        }
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            declaredField.set(viewPager, new PreviewPagerScroller(context, new LinearOutSlowInInterpolator()));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            Log.e("PreviewPager", "Failed to setup pager scroller.", e);
        }
        this.mPageIndicator = (PageIndicator) findViewById(R.id.page_indicator);
        View findViewById = findViewById(R.id.arrow_previous);
        this.mPreviousArrow = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener(this, 0) { // from class: com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ PreviewPager f$0;

            {
                this.$r8$classId = r2;
                this.f$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        ViewPager viewPager2 = this.f$0.mViewPager;
                        viewPager2.setCurrentItem(viewPager2.mCurItem - 1, true);
                        return;
                    default:
                        ViewPager viewPager3 = this.f$0.mViewPager;
                        viewPager3.setCurrentItem(viewPager3.mCurItem + 1, true);
                        return;
                }
            }
        });
        View findViewById2 = findViewById(R.id.arrow_next);
        this.mNextArrow = findViewById2;
        findViewById2.setOnClickListener(new View.OnClickListener(this, 1) { // from class: com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ PreviewPager f$0;

            {
                this.$r8$classId = r2;
                this.f$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        ViewPager viewPager2 = this.f$0.mViewPager;
                        viewPager2.setCurrentItem(viewPager2.mCurItem - 1, true);
                        return;
                    default:
                        ViewPager viewPager3 = this.f$0.mViewPager;
                        viewPager3.setCurrentItem(viewPager3.mCurItem + 1, true);
                        return;
                }
            }
        });
        AnonymousClass4 r8 = new ViewPager.OnPageChangeListener() { // from class: com.android.wallpaper.widget.PreviewPager.4
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i3) {
                ViewPager.OnPageChangeListener onPageChangeListener = PreviewPager.this.mExternalPageListener;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(i3);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i3, float f, int i4) {
                PreviewPager.this.mPageIndicator.setLocation(((float) Math.round((((float) i3) + f) * 100.0f)) / 100.0f);
                ViewPager.OnPageChangeListener onPageChangeListener = PreviewPager.this.mExternalPageListener;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(i3, f, i4);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i3) {
                int count = PreviewPager.this.mAdapter.getCount();
                if (i3 >= 0 && i3 < count) {
                    PreviewPager.this.updateIndicator(i3);
                    ViewPager.OnPageChangeListener onPageChangeListener = PreviewPager.this.mExternalPageListener;
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(i3);
                    }
                }
            }
        };
        this.mPageListener = r8;
        ViewPager viewPager2 = this.mViewPager;
        if (viewPager2.mOnPageChangeListeners == null) {
            viewPager2.mOnPageChangeListeners = new ArrayList();
        }
        viewPager2.mOnPageChangeListeners.add(r8);
    }
}
