package com.android.customization.picker.grid;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import com.android.customization.model.grid.GridOption;
import com.android.customization.model.grid.GridOptionsManager;
import com.android.customization.model.grid.LauncherGridOptionsProvider;
import com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback;
import com.android.wallpaper.util.PreviewUtils;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.SurfaceViewUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class GridOptionPreviewer {
    public final GridOptionsManager mGridManager;
    public GridOption mGridOption;
    public SurfaceView mGridOptionSurface;
    public final ViewGroup mPreviewContainer;
    public GridOptionSurfaceHolderCallback mSurfaceCallback;

    /* loaded from: classes.dex */
    public class GridOptionSurfaceHolderCallback extends WorkspaceSurfaceHolderCallback {
        public GridOptionSurfaceHolderCallback(SurfaceView surfaceView, Context context, AnonymousClass1 r4) {
            super(surfaceView, context, false);
        }

        @Override // com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback
        public void requestPreview(SurfaceView surfaceView, PreviewUtils.WorkspacePreviewCallback workspacePreviewCallback) {
            GridOptionsManager gridOptionsManager = GridOptionPreviewer.this.mGridManager;
            Bundle createSurfaceViewRequest = SurfaceViewUtils.createSurfaceViewRequest(surfaceView);
            String str = GridOptionPreviewer.this.mGridOption.name;
            LauncherGridOptionsProvider launcherGridOptionsProvider = gridOptionsManager.mProvider;
            Objects.requireNonNull(launcherGridOptionsProvider);
            createSurfaceViewRequest.putString("name", str);
            PreviewUtils previewUtils = launcherGridOptionsProvider.mPreviewUtils;
            Objects.requireNonNull(previewUtils);
            PreviewUtils.sExecutorService.submit(new PreviewUtils$$ExternalSyntheticLambda1(previewUtils, createSurfaceViewRequest, workspacePreviewCallback));
        }

        @Override // com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback, android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (GridOptionPreviewer.this.mGridOption != null) {
                super.surfaceCreated(surfaceHolder);
            }
        }
    }

    public GridOptionPreviewer(GridOptionsManager gridOptionsManager, ViewGroup viewGroup) {
        this.mGridManager = gridOptionsManager;
        this.mPreviewContainer = viewGroup;
    }
}
