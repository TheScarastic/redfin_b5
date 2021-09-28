package com.android.wallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class WallpaperInfoView extends LinearLayout {
    public Button mExploreButton;
    public TextView mSubtitle1;
    public TextView mSubtitle2;
    public TextView mTitle;

    public WallpaperInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.wallpaper_info_title);
        this.mSubtitle1 = (TextView) findViewById(R.id.wallpaper_info_subtitle1);
        this.mSubtitle2 = (TextView) findViewById(R.id.wallpaper_info_subtitle2);
        this.mExploreButton = (Button) findViewById(R.id.wallpaper_info_explore_button);
    }
}
