package com.android.wallpaper.picker;

import android.os.Bundle;
import android.os.RemoteException;
import android.service.wallpaper.IWallpaperEngine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.android.wallpaper.util.WallpaperConnection;
import com.android.wallpaper.widget.BottomActionBar;
import com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final /* synthetic */ class LivePreviewFragment$$ExternalSyntheticLambda2 implements View.OnTouchListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LivePreviewFragment$$ExternalSyntheticLambda2(ImagePreviewFragment imagePreviewFragment) {
        this.f$0 = imagePreviewFragment;
    }

    public /* synthetic */ LivePreviewFragment$$ExternalSyntheticLambda2(LivePreviewFragment livePreviewFragment) {
        this.f$0 = livePreviewFragment;
    }

    public /* synthetic */ LivePreviewFragment$$ExternalSyntheticLambda2(MicropaperPreviewFragmentGoogle micropaperPreviewFragmentGoogle) {
        this.f$0 = micropaperPreviewFragmentGoogle;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        switch (this.$r8$classId) {
            case 0:
                LivePreviewFragment livePreviewFragment = (LivePreviewFragment) this.f$0;
                WallpaperConnection wallpaperConnection = livePreviewFragment.mWallpaperConnection;
                if (!(wallpaperConnection == null || wallpaperConnection.mEngine == null)) {
                    float width = ((float) livePreviewFragment.mTouchForwardingLayout.getWidth()) / ((float) livePreviewFragment.mScreenSize.x);
                    int actionMasked = motionEvent.getActionMasked();
                    if (actionMasked == 0) {
                        ((PreviewFragment) livePreviewFragment).mBottomActionBar.hideBottomSheetAndDeselectButtonIfExpanded();
                    }
                    MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                    obtainNoHistory.setLocation(motionEvent.getX() / width, motionEvent.getY() / width);
                    try {
                        livePreviewFragment.mWallpaperConnection.mEngine.dispatchPointer(obtainNoHistory);
                        if (actionMasked == 1) {
                            livePreviewFragment.mWallpaperConnection.mEngine.dispatchWallpaperCommand("android.wallpaper.tap", (int) motionEvent.getX(), (int) motionEvent.getY(), 0, (Bundle) null);
                        } else if (actionMasked == 6) {
                            int actionIndex = motionEvent.getActionIndex();
                            livePreviewFragment.mWallpaperConnection.mEngine.dispatchWallpaperCommand("android.wallpaper.secondaryTap", (int) motionEvent.getX(actionIndex), (int) motionEvent.getY(actionIndex), 0, (Bundle) null);
                        }
                    } catch (RemoteException unused) {
                        Log.e("LivePreviewFragment", "Remote exception of wallpaper connection");
                    }
                }
                return false;
            case 1:
                Executor executor = ImagePreviewFragment.sExecutor;
                BottomActionBar bottomActionBar = ((PreviewFragment) ((ImagePreviewFragment) this.f$0)).mBottomActionBar;
                if (bottomActionBar == null) {
                    return false;
                }
                if (bottomActionBar.mBottomSheetBehavior.state == 4) {
                    return false;
                }
                bottomActionBar.hideBottomSheetAndDeselectButtonIfExpanded();
                return true;
            default:
                MicropaperPreviewFragmentGoogle micropaperPreviewFragmentGoogle = (MicropaperPreviewFragmentGoogle) this.f$0;
                int i = MicropaperPreviewFragmentGoogle.$r8$clinit;
                WallpaperConnection wallpaperConnection2 = micropaperPreviewFragmentGoogle.mWallpaperConnection;
                if (wallpaperConnection2 == null) {
                    return false;
                }
                synchronized (wallpaperConnection2) {
                    IWallpaperEngine iWallpaperEngine = micropaperPreviewFragmentGoogle.mWallpaperConnection.mEngine;
                    if (iWallpaperEngine == null) {
                        return false;
                    }
                    try {
                        iWallpaperEngine.dispatchPointer(MotionEvent.obtainNoHistory(motionEvent));
                    } catch (RemoteException e) {
                        Log.e("MicropaperPreviewFragmentGoogle", "Could not communicate with Engine: ", e);
                    }
                    return true;
                }
        }
    }
}
