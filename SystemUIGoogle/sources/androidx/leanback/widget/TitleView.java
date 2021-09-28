package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.leanback.R$attr;
import androidx.leanback.R$id;
import androidx.leanback.R$layout;
/* loaded from: classes.dex */
public class TitleView extends FrameLayout {
    private int flags;
    private ImageView mBadgeView;
    private boolean mHasSearchListener;
    private SearchOrbView mSearchOrbView;
    private TextView mTextView;
    private final TitleViewAdapter mTitleViewAdapter;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.browseTitleViewStyle);
    }

    public TitleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.flags = 6;
        this.mHasSearchListener = false;
        this.mTitleViewAdapter = new TitleViewAdapter() { // from class: androidx.leanback.widget.TitleView.1
        };
        View inflate = LayoutInflater.from(context).inflate(R$layout.lb_title_view, this);
        this.mBadgeView = (ImageView) inflate.findViewById(R$id.title_badge);
        this.mTextView = (TextView) inflate.findViewById(R$id.title_text);
        this.mSearchOrbView = (SearchOrbView) inflate.findViewById(R$id.title_orb);
        setClipToPadding(false);
        setClipChildren(false);
    }
}
