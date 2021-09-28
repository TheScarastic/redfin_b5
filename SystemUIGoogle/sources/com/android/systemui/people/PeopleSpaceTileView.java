package com.android.systemui.people;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
/* loaded from: classes.dex */
public class PeopleSpaceTileView extends LinearLayout {
    private TextView mNameView;
    private ImageView mPersonIconView;
    private View mTileView;

    public PeopleSpaceTileView(Context context, ViewGroup viewGroup, String str, boolean z) {
        super(context);
        View findViewWithTag = viewGroup.findViewWithTag(str);
        this.mTileView = findViewWithTag;
        if (findViewWithTag == null) {
            LayoutInflater from = LayoutInflater.from(context);
            View inflate = from.inflate(R$layout.people_space_tile_view, viewGroup, false);
            this.mTileView = inflate;
            viewGroup.addView(inflate, -1, -1);
            this.mTileView.setTag(str);
            if (!z) {
                from.inflate(R$layout.people_space_activity_list_divider, viewGroup, true);
            }
        }
        this.mNameView = (TextView) this.mTileView.findViewById(R$id.tile_view_name);
        this.mPersonIconView = (ImageView) this.mTileView.findViewById(R$id.tile_view_person_icon);
    }

    public void setName(String str) {
        this.mNameView.setText(str);
    }

    public void setPersonIcon(Bitmap bitmap) {
        this.mPersonIconView.setImageBitmap(bitmap);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mTileView.setOnClickListener(onClickListener);
    }
}
