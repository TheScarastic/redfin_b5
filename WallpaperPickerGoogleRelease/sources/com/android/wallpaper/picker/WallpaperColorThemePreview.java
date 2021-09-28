package com.android.wallpaper.picker;

import android.app.WallpaperColors;
import android.content.Context;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.customization.model.color.ColorCustomizationManager;
import com.android.customization.model.color.ColorUtils;
import com.android.customization.model.theme.OverlayManagerCompat;
import com.android.customization.model.themedicon.ThemedIconSwitchProvider;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import com.google.android.material.resources.MaterialAttributes;
/* loaded from: classes.dex */
public interface WallpaperColorThemePreview {
    static /* synthetic */ void lambda$updateWorkspacePreview$0(SurfaceView surfaceView) {
        surfaceView.setTop(-1);
        surfaceView.animate().alpha(1.0f).setDuration(300).start();
    }

    default boolean determineThemedIconsSupport(Context context) {
        ThemedIconSwitchProvider instance = ThemedIconSwitchProvider.getInstance(context);
        return (instance.mThemedIconUtils.mProviderInfo != null) && instance.mCustomizationPreferences.getThemedIconEnabled();
    }

    default boolean shouldApplyWallpaperColors() {
        FragmentActivity activity = ((Fragment) this).getActivity();
        if (activity == null || activity.isFinishing()) {
            Log.w("WallpaperColorThemePreview", "shouldApplyWallpaperColors: activity is null or finishing");
            return false;
        } else if (!ColorUtils.isMonetEnabled(activity)) {
            Log.w("WallpaperColorThemePreview", "Monet is not enabled");
            return false;
        } else {
            ColorCustomizationManager instance = ColorCustomizationManager.getInstance(activity, new OverlayManagerCompat(activity));
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Current color source: ");
            m.append(instance.getCurrentColorSource());
            Log.i("WallpaperColorThemePreview", m.toString());
            return !"preset".equals(instance.getCurrentColorSource());
        }
    }

    boolean shouldUpdateWorkspaceColors();

    default void updateSystemBarColor(Context context) {
        int resolveOrThrow = MaterialAttributes.resolveOrThrow(context, 16843827, "android.R.attr.colorPrimary is not set in the current theme");
        Window window = ((Fragment) this).getActivity().getWindow();
        window.setStatusBarColor(resolveOrThrow);
        window.setNavigationBarColor(resolveOrThrow);
    }

    default void updateWorkspacePreview(SurfaceView surfaceView, WorkspaceSurfaceHolderCallback workspaceSurfaceHolderCallback, WallpaperColors wallpaperColors) {
        if (shouldUpdateWorkspaceColors()) {
            int visibility = surfaceView.getVisibility();
            surfaceView.setVisibility(8);
            if (workspaceSurfaceHolderCallback != null) {
                workspaceSurfaceHolderCallback.cleanUp();
                if (workspaceSurfaceHolderCallback.mShouldUseWallpaperColors) {
                    workspaceSurfaceHolderCallback.mWallpaperColors = wallpaperColors;
                    workspaceSurfaceHolderCallback.mIsWallpaperColorsReady = true;
                    workspaceSurfaceHolderCallback.maybeRenderPreview();
                }
                surfaceView.setUseAlpha();
                surfaceView.setAlpha(0.0f);
                workspaceSurfaceHolderCallback.mListener = new PreviewPager$$ExternalSyntheticLambda1(surfaceView);
            }
            surfaceView.setVisibility(visibility);
        }
    }
}
