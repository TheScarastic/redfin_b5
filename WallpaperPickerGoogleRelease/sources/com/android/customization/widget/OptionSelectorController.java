package com.android.customization.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.CustomizationOption;
import com.android.internal.util.Preconditions;
import com.android.systemui.shared.R;
import com.android.wallpaper.widget.BottomActionBar$$ExternalSyntheticLambda1;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class OptionSelectorController<T extends CustomizationOption<T>> {
    public RecyclerView.Adapter<TileViewHolder> mAdapter;
    public T mAppliedOption;
    public final int mCheckmarkStyle;
    public final RecyclerView mContainer;
    public final Set<OptionSelectedListener> mListeners = new HashSet();
    public final List<T> mOptions;
    public T mSelectedOption;
    public final boolean mUseGrid;

    /* loaded from: classes.dex */
    public static final class ItemEndHorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        public final boolean mDirectionLTR;
        public final int mHorizontalSpacePx;

        public ItemEndHorizontalSpaceItemDecoration(Context context, int i, AnonymousClass1 r3) {
            this.mDirectionLTR = context.getResources().getConfiguration().getLayoutDirection() == 0;
            this.mHorizontalSpacePx = i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (recyclerView.getAdapter() != null && recyclerView.getChildAdapterPosition(view) != ((RecyclerView.Adapter) Preconditions.checkNotNull(recyclerView.getAdapter())).getItemCount() - 1) {
                if (this.mDirectionLTR) {
                    rect.right = this.mHorizontalSpacePx;
                } else {
                    rect.left = this.mHorizontalSpacePx;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OptionSelectedListener {
        void onOptionSelected(CustomizationOption customizationOption);
    }

    /* loaded from: classes.dex */
    public class OptionSelectorAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
        public OptionSelectorAccessibilityDelegate(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            int i;
            if (OptionSelectorController.this.mContainer.getLayoutManager() != null && OptionSelectorController.this.mContainer.getLayoutManager().canScrollHorizontally() && accessibilityEvent.getEventType() == 32768) {
                int childLayoutPosition = OptionSelectorController.this.mContainer.getChildLayoutPosition(view);
                int dimensionPixelOffset = (OptionSelectorController.this.mContainer.getContext().getResources().getDimensionPixelOffset(R.dimen.option_tile_margin_horizontal) * 2) + OptionSelectorController.this.mContainer.getContext().getResources().getDimensionPixelOffset(R.dimen.option_tile_width);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) OptionSelectorController.this.mContainer.getLayoutManager();
                int i2 = -1;
                View findOneVisibleChild = linearLayoutManager.findOneVisibleChild(linearLayoutManager.getChildCount() - 1, -1, true, false);
                if (findOneVisibleChild == null) {
                    i = -1;
                } else {
                    i = linearLayoutManager.getPosition(findOneVisibleChild);
                }
                if (childLayoutPosition >= i) {
                    OptionSelectorController.this.mContainer.scrollBy(dimensionPixelOffset, 0);
                } else {
                    LinearLayoutManager linearLayoutManager2 = (LinearLayoutManager) OptionSelectorController.this.mContainer.getLayoutManager();
                    View findOneVisibleChild2 = linearLayoutManager2.findOneVisibleChild(0, linearLayoutManager2.getChildCount(), true, false);
                    if (findOneVisibleChild2 != null) {
                        i2 = linearLayoutManager2.getPosition(findOneVisibleChild2);
                    }
                    if (childLayoutPosition <= i2 && childLayoutPosition != 0) {
                        OptionSelectorController.this.mContainer.scrollBy(-dimensionPixelOffset, 0);
                    }
                }
            }
            return this.mOriginalDelegate.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    /* loaded from: classes.dex */
    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TextView labelView;
        public View tileView;
        public CharSequence title = null;

        public TileViewHolder(View view) {
            super(view);
            this.labelView = (TextView) view.findViewById(R.id.option_label);
            this.tileView = view.findViewById(R.id.option_tile);
        }

        public void setContentDescription(Context context, CustomizationOption<?> customizationOption, int i) {
            View view;
            String title = customizationOption.getTitle();
            this.title = title;
            if (TextUtils.isEmpty(title) && (view = this.tileView) != null) {
                this.title = view.getContentDescription();
            }
            String string = context.getString(i, this.title);
            TextView textView = this.labelView;
            if (textView == null || TextUtils.isEmpty(textView.getText())) {
                View view2 = this.tileView;
                if (view2 != null) {
                    view2.setAccessibilityPaneTitle(string);
                    this.tileView.setContentDescription(string);
                    return;
                }
                return;
            }
            this.labelView.setAccessibilityPaneTitle(string);
            this.labelView.setContentDescription(string);
        }
    }

    public OptionSelectorController(RecyclerView recyclerView, List<T> list, boolean z, int i) {
        this.mContainer = recyclerView;
        this.mOptions = list;
        this.mUseGrid = z;
        this.mCheckmarkStyle = i;
    }

    public void initOptions(final CustomizationManager<T> customizationManager) {
        this.mContainer.setAccessibilityDelegateCompat(new OptionSelectorAccessibilityDelegate(this.mContainer));
        this.mAdapter = new RecyclerView.Adapter<TileViewHolder>() { // from class: com.android.customization.widget.OptionSelectorController.1
            public final void drawCheckmark(CustomizationOption<?> customizationOption, TileViewHolder tileViewHolder, Drawable drawable, int i, int i2, int i3) {
                Drawable foreground = tileViewHolder.tileView.getForeground();
                Drawable[] drawableArr = {foreground, drawable};
                if (foreground == null) {
                    drawableArr = new Drawable[]{drawable};
                }
                LayerDrawable layerDrawable = new LayerDrawable(drawableArr);
                int length = drawableArr.length - 1;
                layerDrawable.setLayerGravity(length, i);
                layerDrawable.setLayerWidth(length, i2);
                layerDrawable.setLayerHeight(length, i2);
                layerDrawable.setLayerInsetBottom(length, i3);
                layerDrawable.setLayerInsetRight(length, i3);
                tileViewHolder.tileView.setForeground(layerDrawable);
                tileViewHolder.setContentDescription(OptionSelectorController.this.mContainer.getContext(), customizationOption, R.string.option_applied_previewed_description);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return OptionSelectorController.this.mOptions.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i) {
                return OptionSelectorController.this.mOptions.get(i).getLayoutResId();
            }

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [androidx.recyclerview.widget.RecyclerView$ViewHolder, int] */
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(TileViewHolder tileViewHolder, int i) {
                TileViewHolder tileViewHolder2 = tileViewHolder;
                T t = OptionSelectorController.this.mOptions.get(i);
                if (t.isActive(customizationManager)) {
                    OptionSelectorController optionSelectorController = OptionSelectorController.this;
                    optionSelectorController.mAppliedOption = t;
                    if (optionSelectorController.mSelectedOption == null) {
                        optionSelectorController.mSelectedOption = t;
                    }
                }
                TextView textView = tileViewHolder2.labelView;
                if (textView != null) {
                    textView.setText(t.getTitle());
                }
                tileViewHolder2.itemView.setActivated(t.equals(OptionSelectorController.this.mSelectedOption));
                t.bindThumbnailTile(tileViewHolder2.tileView);
                tileViewHolder2.itemView.setOnClickListener(new BottomActionBar$$ExternalSyntheticLambda1(this, t));
                Resources resources = OptionSelectorController.this.mContainer.getContext().getResources();
                OptionSelectorController optionSelectorController2 = OptionSelectorController.this;
                if (optionSelectorController2.mCheckmarkStyle != 1 || !t.equals(optionSelectorController2.mAppliedOption)) {
                    OptionSelectorController optionSelectorController3 = OptionSelectorController.this;
                    if (optionSelectorController3.mCheckmarkStyle == 2 && t.equals(optionSelectorController3.mAppliedOption)) {
                        drawCheckmark(t, tileViewHolder2, resources.getDrawable(R.drawable.check_circle_grey_large, OptionSelectorController.this.mContainer.getContext().getTheme()), 17, resources.getDimensionPixelSize(R.dimen.center_check_size), 0);
                    } else if (t.equals(OptionSelectorController.this.mAppliedOption)) {
                        tileViewHolder2.setContentDescription(OptionSelectorController.this.mContainer.getContext(), t, R.string.option_previewed_description);
                    } else if (OptionSelectorController.this.mCheckmarkStyle != 0) {
                        tileViewHolder2.tileView.setForeground(null);
                    }
                } else {
                    drawCheckmark(t, tileViewHolder2, resources.getDrawable(R.drawable.check_circle_accent_24dp, OptionSelectorController.this.mContainer.getContext().getTheme()), 85, resources.getDimensionPixelSize(R.dimen.check_size), resources.getDimensionPixelOffset(R.dimen.check_offset));
                }
            }

            /* Return type fixed from 'androidx.recyclerview.widget.RecyclerView$ViewHolder' to match base method */
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public TileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new TileViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false));
            }
        };
        Resources resources = this.mContainer.getContext().getResources();
        if (this.mUseGrid) {
            RecyclerView recyclerView = this.mContainer;
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), resources.getInteger(R.integer.options_grid_num_columns)));
        } else {
            RecyclerView recyclerView2 = this.mContainer;
            recyclerView2.getContext();
            recyclerView2.setLayoutManager(new LinearLayoutManager(0, false));
        }
        this.mContainer.setAdapter(this.mAdapter);
        this.mContainer.measure(0, 0);
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.options_container_width);
        if (dimensionPixelSize == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) this.mContainer.getContext().getSystemService(WindowManager.class)).getDefaultDisplay().getMetrics(displayMetrics);
            dimensionPixelSize = displayMetrics.widthPixels;
        }
        int measuredWidth = this.mContainer.getMeasuredWidth();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R.dimen.option_tile_width);
        if (this.mUseGrid) {
            int integer = resources.getInteger(R.integer.options_grid_num_columns);
            while (dimensionPixelSize - (dimensionPixelOffset * integer) < 0) {
                integer--;
            }
            if (this.mContainer.getLayoutManager() != null) {
                ((GridLayoutManager) this.mContainer.getLayoutManager()).setSpanCount(integer);
                return;
            }
            return;
        }
        int i = dimensionPixelSize - measuredWidth;
        if (i >= 0) {
            this.mContainer.setOverScrollMode(2);
        }
        if (((float) this.mAdapter.getItemCount()) >= 4.35f) {
            int round = ((dimensionPixelSize - Math.round(((float) dimensionPixelOffset) * 4.35f)) - this.mContainer.getPaddingLeft()) / 4;
            if (round <= 0) {
                round = resources.getDimensionPixelOffset(R.dimen.option_tile_margin_horizontal);
            }
            RecyclerView recyclerView3 = this.mContainer;
            recyclerView3.addItemDecoration(new ItemEndHorizontalSpaceItemDecoration(recyclerView3.getContext(), round, null));
            return;
        }
        this.mContainer.addItemDecoration(new HorizontalSpacerItemDecoration((i / (this.mAdapter.getItemCount() + 1)) / 2));
    }

    public void setSelectedOption(T t) {
        if (this.mOptions.contains(t)) {
            updateActivatedStatus(this.mSelectedOption, false);
            updateActivatedStatus(t, true);
            T t2 = this.mSelectedOption;
            this.mSelectedOption = t;
            this.mAdapter.notifyItemChanged(this.mOptions.indexOf(t));
            if (t2 != null) {
                this.mAdapter.notifyItemChanged(this.mOptions.indexOf(t2));
            }
            if (!this.mListeners.isEmpty()) {
                T t3 = this.mSelectedOption;
                Iterator it = new HashSet(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((OptionSelectedListener) it.next()).onOptionSelected(t3);
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid option");
    }

    public final void updateActivatedStatus(T t, boolean z) {
        View view;
        int indexOf = this.mOptions.indexOf(t);
        if (indexOf >= 0) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.mContainer.findViewHolderForAdapterPosition(indexOf);
            if (findViewHolderForAdapterPosition == null || (view = findViewHolderForAdapterPosition.itemView) == null) {
                this.mAdapter.notifyItemChanged(indexOf);
                return;
            }
            view.setActivated(z);
            if (findViewHolderForAdapterPosition instanceof TileViewHolder) {
                TileViewHolder tileViewHolder = (TileViewHolder) findViewHolderForAdapterPosition;
                if (z) {
                    if (t != this.mAppliedOption || this.mCheckmarkStyle == 0) {
                        tileViewHolder.setContentDescription(this.mContainer.getContext(), t, R.string.option_previewed_description);
                    } else {
                        tileViewHolder.setContentDescription(this.mContainer.getContext(), t, R.string.option_applied_previewed_description);
                    }
                } else if (t != this.mAppliedOption || this.mCheckmarkStyle == 0) {
                    TextView textView = tileViewHolder.labelView;
                    if (textView == null || TextUtils.isEmpty(textView.getText())) {
                        View view2 = tileViewHolder.tileView;
                        if (view2 != null) {
                            view2.setAccessibilityPaneTitle(tileViewHolder.title);
                            tileViewHolder.tileView.setContentDescription(tileViewHolder.title);
                            return;
                        }
                        return;
                    }
                    tileViewHolder.labelView.setAccessibilityPaneTitle(tileViewHolder.title);
                    tileViewHolder.labelView.setContentDescription(tileViewHolder.title);
                } else {
                    tileViewHolder.setContentDescription(this.mContainer.getContext(), t, R.string.option_applied_description);
                }
            }
        }
    }
}
