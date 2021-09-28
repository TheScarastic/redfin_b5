package com.android.wallpaper.picker;

import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import com.android.systemui.shared.R;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.widget.BottomActionBar;
/* loaded from: classes.dex */
public final /* synthetic */ class PreviewFragment$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ PreviewFragment f$0;

    public /* synthetic */ PreviewFragment$$ExternalSyntheticLambda1(PreviewFragment previewFragment, int i) {
        this.$r8$classId = i;
        this.f$0 = previewFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                PreviewFragment previewFragment = this.f$0;
                previewFragment.mFullScreenAnimation.startAnimation(true);
                previewFragment.mBottomActionBar.deselectAction(BottomActionBar.BottomAction.EDIT);
                return;
            case 1:
                PreviewFragment previewFragment2 = this.f$0;
                boolean z = previewFragment2.mFullScreenAnimation.mWorkspaceVisibility;
                ((Button) view).setText(z ? R.string.show_ui_preview_text : R.string.hide_ui_preview_text);
                FullScreenAnimation fullScreenAnimation = previewFragment2.mFullScreenAnimation;
                boolean z2 = !z;
                if (z2) {
                    fullScreenAnimation.mWorkspaceSurface.setClipBounds(new Rect(0, Math.round(((float) fullScreenAnimation.mWorkspaceHeight) * 0.2f), fullScreenAnimation.mWorkspaceWidth, Math.round(fullScreenAnimation.mFullScreenButtonsTranslation / fullScreenAnimation.mScale) + fullScreenAnimation.mWorkspaceHeight));
                    fullScreenAnimation.mView.findViewById(R.id.lock_screen_preview_container).setVisibility(0);
                } else {
                    int i = fullScreenAnimation.mWorkspaceHeight / 2;
                    fullScreenAnimation.mWorkspaceSurface.setClipBounds(new Rect(0, i, fullScreenAnimation.mWorkspaceWidth, i + 1));
                    fullScreenAnimation.mView.findViewById(R.id.lock_screen_preview_container).setVisibility(4);
                }
                if (fullScreenAnimation.mIsHomeSelected) {
                    fullScreenAnimation.mView.findViewById(R.id.lock_screen_preview_container).setVisibility(4);
                }
                fullScreenAnimation.mWorkspaceVisibility = z2;
                return;
            default:
                this.f$0.onSetWallpaperClicked(view);
                return;
        }
    }
}
