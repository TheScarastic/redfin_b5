package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BubbleData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class BubbleOverflowContainerView extends LinearLayout {
    private BubbleOverflowAdapter mAdapter;
    private BubbleController mController;
    private final BubbleData.Listener mDataListener;
    private LinearLayout mEmptyState;
    private ImageView mEmptyStateImage;
    private TextView mEmptyStateSubtitle;
    private TextView mEmptyStateTitle;
    private View.OnKeyListener mKeyListener;
    private List<Bubble> mOverflowBubbles;
    private RecyclerView mRecyclerView;

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 4) {
            return false;
        }
        this.mController.collapseStack();
        return true;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class OverflowGridLayoutManager extends GridLayoutManager {
        OverflowGridLayoutManager(Context context, int i) {
            super(context, i);
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int itemCount = state.getItemCount();
            int columnCountForAccessibility = super.getColumnCountForAccessibility(recycler, state);
            return itemCount < columnCountForAccessibility ? itemCount : columnCountForAccessibility;
        }
    }

    public BubbleOverflowContainerView(Context context) {
        this(context, null);
    }

    public BubbleOverflowContainerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleOverflowContainerView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BubbleOverflowContainerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mOverflowBubbles = new ArrayList();
        this.mKeyListener = new View.OnKeyListener() { // from class: com.android.wm.shell.bubbles.BubbleOverflowContainerView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i3, KeyEvent keyEvent) {
                return BubbleOverflowContainerView.this.lambda$new$0(view, i3, keyEvent);
            }
        };
        this.mDataListener = new BubbleData.Listener() { // from class: com.android.wm.shell.bubbles.BubbleOverflowContainerView.1
            @Override // com.android.wm.shell.bubbles.BubbleData.Listener
            public void applyUpdate(BubbleData.Update update) {
                Bubble bubble = update.removedOverflowBubble;
                if (bubble != null) {
                    bubble.cleanupViews();
                    int indexOf = BubbleOverflowContainerView.this.mOverflowBubbles.indexOf(bubble);
                    BubbleOverflowContainerView.this.mOverflowBubbles.remove(bubble);
                    BubbleOverflowContainerView.this.mAdapter.notifyItemRemoved(indexOf);
                }
                Bubble bubble2 = update.addedOverflowBubble;
                if (bubble2 != null) {
                    int indexOf2 = BubbleOverflowContainerView.this.mOverflowBubbles.indexOf(bubble2);
                    if (indexOf2 > 0) {
                        BubbleOverflowContainerView.this.mOverflowBubbles.remove(bubble2);
                        BubbleOverflowContainerView.this.mOverflowBubbles.add(0, bubble2);
                        BubbleOverflowContainerView.this.mAdapter.notifyItemMoved(indexOf2, 0);
                    } else {
                        BubbleOverflowContainerView.this.mOverflowBubbles.add(0, bubble2);
                        BubbleOverflowContainerView.this.mAdapter.notifyItemInserted(0);
                    }
                }
                BubbleOverflowContainerView.this.updateEmptyStateVisibility();
            }
        };
        setFocusableInTouchMode(true);
    }

    public void setBubbleController(BubbleController bubbleController) {
        this.mController = bubbleController;
    }

    public void show() {
        requestFocus();
        updateOverflow();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mRecyclerView = (RecyclerView) findViewById(R.id.bubble_overflow_recycler);
        this.mEmptyState = (LinearLayout) findViewById(R.id.bubble_overflow_empty_state);
        this.mEmptyStateTitle = (TextView) findViewById(R.id.bubble_overflow_empty_title);
        this.mEmptyStateSubtitle = (TextView) findViewById(R.id.bubble_overflow_empty_subtitle);
        this.mEmptyStateImage = (ImageView) findViewById(R.id.bubble_overflow_empty_state_image);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BubbleController bubbleController = this.mController;
        if (bubbleController != null) {
            bubbleController.updateWindowFlagsForOverflow(true);
        }
        setOnKeyListener(this.mKeyListener);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BubbleController bubbleController = this.mController;
        if (bubbleController != null) {
            bubbleController.updateWindowFlagsForOverflow(false);
        }
        setOnKeyListener(null);
    }

    void updateOverflow() {
        this.mRecyclerView.setLayoutManager(new OverflowGridLayoutManager(getContext(), getResources().getInteger(R.integer.bubbles_overflow_columns)));
        Context context = getContext();
        List<Bubble> list = this.mOverflowBubbles;
        BubbleController bubbleController = this.mController;
        Objects.requireNonNull(bubbleController);
        BubbleOverflowAdapter bubbleOverflowAdapter = new BubbleOverflowAdapter(context, list, new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleOverflowContainerView$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BubbleController.this.promoteBubbleFromOverflow((Bubble) obj);
            }
        }, this.mController.getPositioner());
        this.mAdapter = bubbleOverflowAdapter;
        this.mRecyclerView.setAdapter(bubbleOverflowAdapter);
        this.mOverflowBubbles.clear();
        this.mOverflowBubbles.addAll(this.mController.getOverflowBubbles());
        this.mAdapter.notifyDataSetChanged();
        this.mController.setOverflowListener(this.mDataListener);
        updateEmptyStateVisibility();
        updateTheme();
    }

    void updateEmptyStateVisibility() {
        int i = 0;
        this.mEmptyState.setVisibility(this.mOverflowBubbles.isEmpty() ? 0 : 8);
        RecyclerView recyclerView = this.mRecyclerView;
        if (this.mOverflowBubbles.isEmpty()) {
            i = 8;
        }
        recyclerView.setVisibility(i);
    }

    void updateTheme() {
        Drawable drawable;
        int i;
        Resources resources = getResources();
        boolean z = (resources.getConfiguration().uiMode & 48) == 32;
        ImageView imageView = this.mEmptyStateImage;
        if (z) {
            drawable = resources.getDrawable(R.drawable.bubble_ic_empty_overflow_dark);
        } else {
            drawable = resources.getDrawable(R.drawable.bubble_ic_empty_overflow_light);
        }
        imageView.setImageDrawable(drawable);
        View findViewById = findViewById(R.id.bubble_overflow_container);
        if (z) {
            i = resources.getColor(R.color.bubbles_dark);
        } else {
            i = resources.getColor(R.color.bubbles_light);
        }
        findViewById.setBackgroundColor(i);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16844002, 16842808});
        int i2 = -16777216;
        int color = obtainStyledAttributes.getColor(0, z ? -16777216 : -1);
        if (z) {
            i2 = -1;
        }
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(obtainStyledAttributes.getColor(1, i2), color, z);
        obtainStyledAttributes.recycle();
        setBackgroundColor(color);
        this.mEmptyStateTitle.setTextColor(ensureTextContrast);
        this.mEmptyStateSubtitle.setTextColor(ensureTextContrast);
    }

    public void updateFontSize() {
        float dimensionPixelSize = (float) ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(17105541);
        this.mEmptyStateTitle.setTextSize(0, dimensionPixelSize);
        this.mEmptyStateSubtitle.setTextSize(0, dimensionPixelSize);
    }
}
