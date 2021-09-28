package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.qs.QSTile;
/* loaded from: classes.dex */
public class QSDetailItems extends FrameLayout {
    private static final boolean DEBUG = Log.isLoggable("QSDetailItems", 3);
    private Callback mCallback;
    private final Context mContext;
    private View mEmpty;
    private ImageView mEmptyIcon;
    private TextView mEmptyText;
    private AutoSizingList mItemList;
    private Item[] mItems;
    private final H mHandler = new H();
    private final Adapter mAdapter = new Adapter();
    private boolean mItemsVisible = true;
    private String mTag = "QSDetailItems";
    private final int mQsDetailIconOverlaySize = (int) getResources().getDimension(R$dimen.qs_detail_icon_overlay_size);

    /* loaded from: classes.dex */
    public interface Callback {
        void onDetailItemClick(Item item);

        void onDetailItemDisconnect(Item item);
    }

    /* loaded from: classes.dex */
    public static class Item {
        public boolean canDisconnect;
        public QSTile.Icon icon;
        public int icon2 = -1;
        public int iconResId;
        public CharSequence line1;
        public CharSequence line2;
        public Drawable overlay;
        public Object tag;
    }

    public QSDetailItems(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    public static QSDetailItems convertOrInflate(Context context, View view, ViewGroup viewGroup) {
        if (view instanceof QSDetailItems) {
            return (QSDetailItems) view;
        }
        return (QSDetailItems) LayoutInflater.from(context).inflate(R$layout.qs_detail_items, viewGroup, false);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        AutoSizingList autoSizingList = (AutoSizingList) findViewById(16908298);
        this.mItemList = autoSizingList;
        autoSizingList.setVisibility(8);
        this.mItemList.setAdapter(this.mAdapter);
        View findViewById = findViewById(16908292);
        this.mEmpty = findViewById;
        findViewById.setVisibility(8);
        this.mEmptyText = (TextView) this.mEmpty.findViewById(16908310);
        this.mEmptyIcon = (ImageView) this.mEmpty.findViewById(16908294);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FontSizeUtils.updateFontSize(this.mEmptyText, R$dimen.qs_detail_empty_text_size);
        int childCount = this.mItemList.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mItemList.getChildAt(i);
            FontSizeUtils.updateFontSize(childAt, 16908310, R$dimen.qs_detail_item_primary_text_size);
            FontSizeUtils.updateFontSize(childAt, 16908304, R$dimen.qs_detail_item_secondary_text_size);
        }
    }

    public void setTagSuffix(String str) {
        this.mTag = "QSDetailItems." + str;
    }

    public void setEmptyState(int i, int i2) {
        this.mEmptyIcon.post(new Runnable(i, i2) { // from class: com.android.systemui.qs.QSDetailItems$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                QSDetailItems.$r8$lambda$3vpWlzDr8LEkbY2zzovkHDJrL84(QSDetailItems.this, this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setEmptyState$0(int i, int i2) {
        this.mEmptyIcon.setImageResource(i);
        this.mEmptyText.setText(i2);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DEBUG) {
            Log.d(this.mTag, "onAttachedToWindow");
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DEBUG) {
            Log.d(this.mTag, "onDetachedFromWindow");
        }
        this.mCallback = null;
    }

    public void setCallback(Callback callback) {
        this.mHandler.removeMessages(2);
        this.mHandler.obtainMessage(2, callback).sendToTarget();
    }

    public void setItems(Item[] itemArr) {
        this.mHandler.removeMessages(1);
        this.mHandler.obtainMessage(1, itemArr).sendToTarget();
    }

    public void setItemsVisible(boolean z) {
        this.mHandler.removeMessages(3);
        this.mHandler.obtainMessage(3, z ? 1 : 0, 0).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void handleSetCallback(Callback callback) {
        this.mCallback = callback;
    }

    /* access modifiers changed from: private */
    public void handleSetItems(Item[] itemArr) {
        int i = 0;
        int length = itemArr != null ? itemArr.length : 0;
        this.mEmpty.setVisibility(length == 0 ? 0 : 8);
        AutoSizingList autoSizingList = this.mItemList;
        if (length == 0) {
            i = 8;
        }
        autoSizingList.setVisibility(i);
        this.mItems = itemArr;
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void handleSetItemsVisible(boolean z) {
        if (this.mItemsVisible != z) {
            this.mItemsVisible = z;
            for (int i = 0; i < this.mItemList.getChildCount(); i++) {
                this.mItemList.getChildAt(i).setVisibility(this.mItemsVisible ? 0 : 4);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Adapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0;
        }

        private Adapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (QSDetailItems.this.mItems != null) {
                return QSDetailItems.this.mItems.length;
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return QSDetailItems.this.mItems[i];
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Item item = QSDetailItems.this.mItems[i];
            if (view == null) {
                view = LayoutInflater.from(QSDetailItems.this.mContext).inflate(R$layout.qs_detail_item, viewGroup, false);
            }
            view.setVisibility(QSDetailItems.this.mItemsVisible ? 0 : 4);
            ImageView imageView = (ImageView) view.findViewById(16908294);
            QSTile.Icon icon = item.icon;
            if (icon != null) {
                imageView.setImageDrawable(icon.getDrawable(imageView.getContext()));
            } else {
                imageView.setImageResource(item.iconResId);
            }
            imageView.getOverlay().clear();
            Drawable drawable = item.overlay;
            if (drawable != null) {
                drawable.setBounds(0, 0, QSDetailItems.this.mQsDetailIconOverlaySize, QSDetailItems.this.mQsDetailIconOverlaySize);
                imageView.getOverlay().add(item.overlay);
            }
            TextView textView = (TextView) view.findViewById(16908310);
            textView.setText(item.line1);
            TextView textView2 = (TextView) view.findViewById(16908304);
            boolean z = !TextUtils.isEmpty(item.line2);
            textView.setMaxLines(z ? 1 : 2);
            textView2.setVisibility(z ? 0 : 8);
            textView2.setText(z ? item.line2 : null);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.QSDetailItems.Adapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (QSDetailItems.this.mCallback != null) {
                        QSDetailItems.this.mCallback.onDetailItemClick(item);
                    }
                }
            });
            ImageView imageView2 = (ImageView) view.findViewById(16908296);
            if (item.canDisconnect) {
                imageView2.setImageResource(R$drawable.ic_qs_cancel);
                imageView2.setVisibility(0);
                imageView2.setClickable(true);
                imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.qs.QSDetailItems.Adapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (QSDetailItems.this.mCallback != null) {
                            QSDetailItems.this.mCallback.onDetailItemDisconnect(item);
                        }
                    }
                });
            } else if (item.icon2 != -1) {
                imageView2.setVisibility(0);
                imageView2.setImageResource(item.icon2);
                imageView2.setClickable(false);
            } else {
                imageView2.setVisibility(8);
            }
            return view;
        }
    }

    /* loaded from: classes.dex */
    private class H extends Handler {
        public H() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            boolean z = true;
            if (i == 1) {
                QSDetailItems.this.handleSetItems((Item[]) message.obj);
            } else if (i == 2) {
                QSDetailItems.this.handleSetCallback((Callback) message.obj);
            } else if (i == 3) {
                QSDetailItems qSDetailItems = QSDetailItems.this;
                if (message.arg1 == 0) {
                    z = false;
                }
                qSDetailItems.handleSetItemsVisible(z);
            }
        }
    }
}
