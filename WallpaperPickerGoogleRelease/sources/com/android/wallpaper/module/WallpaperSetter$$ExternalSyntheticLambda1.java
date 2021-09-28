package com.android.wallpaper.module;

import android.app.Activity;
import android.content.Context;
import com.android.wallpaper.widget.BottomActionBar;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class WallpaperSetter$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId = 0;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ WallpaperSetter$$ExternalSyntheticLambda1(WallpaperSetter wallpaperSetter, Activity activity) {
        this.f$0 = wallpaperSetter;
        this.f$1 = activity;
    }

    public /* synthetic */ WallpaperSetter$$ExternalSyntheticLambda1(BottomActionBar bottomActionBar, Context context) {
        this.f$0 = bottomActionBar;
        this.f$1 = context;
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [T extends android.view.View, android.view.View] */
    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                WallpaperSetter wallpaperSetter = (WallpaperSetter) this.f$0;
                Activity activity = (Activity) this.f$1;
                Integer num = (Integer) obj;
                Objects.requireNonNull(wallpaperSetter);
                if (activity.getRequestedOrientation() != num.intValue()) {
                    activity.setRequestedOrientation(num.intValue());
                }
                wallpaperSetter.mCurrentScreenOrientation = Optional.empty();
                return;
            default:
                BottomActionBar bottomActionBar = (BottomActionBar) this.f$0;
                BottomActionBar.BottomSheetContent bottomSheetContent = (BottomActionBar.BottomSheetContent) obj;
                int i = BottomActionBar.$r8$clinit;
                Objects.requireNonNull(bottomActionBar);
                bottomSheetContent.onRecreateView(bottomSheetContent.mContentView);
                bottomSheetContent.mContentView = bottomSheetContent.createView((Context) this.f$1);
                bottomSheetContent.setVisibility(bottomSheetContent.mIsVisible);
                bottomActionBar.mBottomSheetView.addView(bottomSheetContent.mContentView);
                return;
        }
    }
}
