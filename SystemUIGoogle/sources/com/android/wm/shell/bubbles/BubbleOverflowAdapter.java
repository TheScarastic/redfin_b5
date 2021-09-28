package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BadgedImageView;
import java.util.List;
import java.util.function.Consumer;
/* compiled from: BubbleOverflowContainerView.java */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class BubbleOverflowAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Bubble> mBubbles;
    private Context mContext;
    private BubblePositioner mPositioner;
    private Consumer<Bubble> mPromoteBubbleFromOverflow;

    /* access modifiers changed from: package-private */
    public BubbleOverflowAdapter(Context context, List<Bubble> list, Consumer<Bubble> consumer, BubblePositioner bubblePositioner) {
        this.mContext = context;
        this.mBubbles = list;
        this.mPromoteBubbleFromOverflow = consumer;
        this.mPositioner = bubblePositioner;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_overflow_view, viewGroup, false);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844002, 16842806});
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(obtainStyledAttributes.getColor(1, -16777216), obtainStyledAttributes.getColor(0, -1), true);
        obtainStyledAttributes.recycle();
        ((TextView) linearLayout.findViewById(R.id.bubble_view_name)).setTextColor(ensureTextContrast);
        return new ViewHolder(linearLayout, this.mPositioner);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CharSequence charSequence;
        Bubble bubble = this.mBubbles.get(i);
        viewHolder.iconView.setRenderedBubble(bubble);
        viewHolder.iconView.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        viewHolder.iconView.setOnClickListener(new View.OnClickListener(bubble) { // from class: com.android.wm.shell.bubbles.BubbleOverflowAdapter$$ExternalSyntheticLambda0
            public final /* synthetic */ Bubble f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BubbleOverflowAdapter.$r8$lambda$uuY8587oTfqKgyQTckabnewkeY4(BubbleOverflowAdapter.this, this.f$1, view);
            }
        });
        String title = bubble.getTitle();
        if (title == null) {
            title = this.mContext.getResources().getString(R.string.notification_bubble_title);
        }
        viewHolder.iconView.setContentDescription(this.mContext.getResources().getString(R.string.bubble_content_description_single, title, bubble.getAppName()));
        viewHolder.iconView.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.bubbles.BubbleOverflowAdapter.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, BubbleOverflowAdapter.this.mContext.getResources().getString(R.string.bubble_accessibility_action_add_back)));
            }
        });
        if (bubble.getShortcutInfo() != null) {
            charSequence = bubble.getShortcutInfo().getLabel();
        } else {
            charSequence = bubble.getAppName();
        }
        viewHolder.textView.setText(charSequence);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(Bubble bubble, View view) {
        this.mBubbles.remove(bubble);
        notifyDataSetChanged();
        this.mPromoteBubbleFromOverflow.accept(bubble);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mBubbles.size();
    }

    /* compiled from: BubbleOverflowContainerView.java */
    /* loaded from: classes2.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BadgedImageView iconView;
        public TextView textView;

        ViewHolder(LinearLayout linearLayout, BubblePositioner bubblePositioner) {
            super(linearLayout);
            BadgedImageView badgedImageView = (BadgedImageView) linearLayout.findViewById(R.id.bubble_view);
            this.iconView = badgedImageView;
            badgedImageView.initialize(bubblePositioner);
            this.textView = (TextView) linearLayout.findViewById(R.id.bubble_view_name);
        }
    }
}
