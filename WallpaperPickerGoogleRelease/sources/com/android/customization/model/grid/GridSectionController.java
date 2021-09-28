package com.android.customization.model.grid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.grid.GridOptionsManager;
import com.android.customization.picker.grid.GridFragment$$ExternalSyntheticLambda1;
import com.android.customization.picker.grid.GridSectionView;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.widget.BottomActionBar$$ExternalSyntheticLambda1;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class GridSectionController implements CustomizationSectionController<GridSectionView> {
    public final GridOptionsManager mGridOptionsManager;
    public final CustomizationSectionController.CustomizationSectionNavigationController mSectionNavigationController;

    public GridSectionController(GridOptionsManager gridOptionsManager, CustomizationSectionController.CustomizationSectionNavigationController customizationSectionNavigationController) {
        this.mGridOptionsManager = gridOptionsManager;
        this.mSectionNavigationController = customizationSectionNavigationController;
    }

    /* Return type fixed from 'com.android.wallpaper.picker.SectionView' to match base method */
    @Override // com.android.wallpaper.model.CustomizationSectionController
    public GridSectionView createView(Context context) {
        GridSectionView gridSectionView = (GridSectionView) LayoutInflater.from(context).inflate(R.layout.grid_section_view, (ViewGroup) null);
        final TextView textView = (TextView) gridSectionView.findViewById(R.id.grid_section_description);
        final View findViewById = gridSectionView.findViewById(R.id.grid_section_tile);
        GridOptionsManager gridOptionsManager = this.mGridOptionsManager;
        AnonymousClass1 r5 = new CustomizationManager.OptionsFetchedListener<GridOption>() { // from class: com.android.customization.model.grid.GridSectionController.1
            @Override // com.android.customization.model.CustomizationManager.OptionsFetchedListener
            public void onError(Throwable th) {
                if (th != null) {
                    Log.e("GridSectionController", "Error loading grid options", th);
                }
                textView.setText(R.string.something_went_wrong);
                findViewById.setVisibility(8);
            }

            @Override // com.android.customization.model.CustomizationManager.OptionsFetchedListener
            public void onOptionsLoaded(List<GridOption> list) {
                TextView textView2 = textView;
                GridSectionController gridSectionController = GridSectionController.this;
                Objects.requireNonNull(gridSectionController);
                textView2.setText(list.stream().filter(new GridFragment$$ExternalSyntheticLambda1(gridSectionController)).findAny().orElse(list.get(0)).mTitle);
            }
        };
        Objects.requireNonNull(gridOptionsManager);
        new GridOptionsManager.FetchTask(gridOptionsManager.mProvider, r5, true, null).execute(new Void[0]);
        gridSectionView.setOnClickListener(new BottomActionBar$$ExternalSyntheticLambda1(this, context));
        return gridSectionView;
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public boolean isAvailable(Context context) {
        return this.mGridOptionsManager.mProvider.mPreviewUtils.mProviderInfo != null;
    }
}
