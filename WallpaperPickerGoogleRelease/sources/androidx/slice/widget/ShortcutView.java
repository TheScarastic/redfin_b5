package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.widget.SliceView;
import com.android.systemui.shared.R;
import java.util.Set;
/* loaded from: classes.dex */
public class ShortcutView extends SliceChildView {
    public SliceItem mActionItem;
    public IconCompat mIcon;
    public int mLargeIconSize;
    public ListContent mListContent;
    public Set<SliceItem> mLoadingActions;
    public int mSmallIconSize;

    public ShortcutView(Context context) {
        super(context);
        Resources resources = getResources();
        this.mSmallIconSize = resources.getDimensionPixelSize(R.dimen.abc_slice_icon_size);
        this.mLargeIconSize = resources.getDimensionPixelSize(R.dimen.abc_slice_shortcut_size);
    }

    @Override // androidx.slice.widget.SliceChildView
    public Set<SliceItem> getLoadingActions() {
        return this.mLoadingActions;
    }

    @Override // android.view.View
    public boolean performClick() {
        if (this.mListContent == null) {
            return false;
        }
        if (!callOnClick()) {
            try {
                SliceItem sliceItem = this.mActionItem;
                if (sliceItem != null) {
                    sliceItem.fireActionInternal(null, null);
                    SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                    if (onSliceActionListener != null) {
                        EventInfo eventInfo = new EventInfo(3, 1, -1, 0);
                        SliceItem sliceItem2 = this.mActionItem;
                        if (sliceItem2 == null) {
                            sliceItem2 = this.mListContent.mSliceItem;
                        }
                        onSliceActionListener.onSliceAction(eventInfo, sliceItem2);
                    }
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("ShortcutView", "PendingIntent for slice cannot be sent", e);
            }
        }
        return true;
    }

    @Override // androidx.slice.widget.SliceChildView
    public void resetView() {
        this.mListContent = null;
        this.mActionItem = null;
        this.mIcon = null;
        setBackground(null);
        removeAllViews();
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setLoadingActions(Set<SliceItem> set) {
        this.mLoadingActions = set;
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceContent(ListContent listContent) {
        ImageView.ScaleType scaleType;
        resetView();
        this.mListContent = listContent;
        if (listContent != null) {
            SliceActionImpl sliceActionImpl = (SliceActionImpl) listContent.getShortcut(getContext());
            this.mActionItem = sliceActionImpl.mActionItem;
            this.mIcon = sliceActionImpl.mIcon;
            boolean z = sliceActionImpl.mImageMode == 0;
            SliceItem sliceItem = this.mListContent.mColorItem;
            int i = sliceItem != null ? sliceItem.getInt() : -1;
            if (i == -1) {
                i = SliceViewUtil.getColorAttr(getContext(), 16843829);
            }
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.setTint(i);
            ImageView imageView = new ImageView(getContext());
            if (this.mIcon != null && z) {
                imageView.setBackground(shapeDrawable);
            }
            addView(imageView);
            if (this.mIcon != null) {
                int i2 = z ? this.mSmallIconSize : this.mLargeIconSize;
                Context context = getContext();
                IconCompat iconCompat = this.mIcon;
                boolean z2 = !z;
                ImageView imageView2 = new ImageView(context);
                iconCompat.checkResource(context);
                imageView2.setImageDrawable(iconCompat.toIcon(context).loadDrawable(context));
                if (z2) {
                    scaleType = ImageView.ScaleType.CENTER_CROP;
                } else {
                    scaleType = ImageView.ScaleType.CENTER_INSIDE;
                }
                imageView2.setScaleType(scaleType);
                addView(imageView2);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView2.getLayoutParams();
                if (z2) {
                    Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    imageView2.layout(0, 0, i2, i2);
                    imageView2.draw(canvas);
                    imageView2.setImageBitmap(SliceViewUtil.getCircularBitmap(createBitmap));
                } else {
                    imageView2.setColorFilter(-1);
                }
                layoutParams.width = i2;
                layoutParams.height = i2;
                layoutParams.gravity = 17;
                setClickable(true);
            } else {
                setClickable(false);
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams2.gravity = 17;
            setLayoutParams(layoutParams2);
        }
    }
}
