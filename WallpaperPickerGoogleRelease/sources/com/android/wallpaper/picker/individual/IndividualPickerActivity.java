package com.android.wallpaper.picker.individual;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.R$attr;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.model.PickerIntentFactory;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.BaseActivity;
import com.android.wallpaper.picker.PreviewActivity;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.widget.BottomActionBar;
/* loaded from: classes.dex */
public class IndividualPickerActivity extends BaseActivity implements BottomActionBar.BottomActionBarHost, IndividualPickerFragment.IndividualPickerFragmentHost {
    public Category mCategory;
    public String mCategoryCollectionId;
    public InlinePreviewIntentFactory mPreviewIntentFactory;
    public String mWallpaperId;
    public WallpaperPersister mWallpaperPersister;

    /* loaded from: classes.dex */
    public static class IndividualPickerActivityIntentFactory implements PickerIntentFactory {
    }

    @Override // com.android.wallpaper.widget.BottomActionBar.BottomActionBarHost
    public BottomActionBar getBottomActionBar() {
        return (BottomActionBar) findViewById(R.id.bottom_actionbar);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public boolean isHostToolbarShown() {
        return true;
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void moveToPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        boolean z = true;
        if (i == 1) {
            z = false;
        } else if (i != 4) {
            Log.e("IndividualPickerAct", "Invalid request code: " + i);
            if (this.mWallpaperId != null && !isFinishing()) {
                setResult(i2);
                finish();
                return;
            }
        }
        if (i2 == -1) {
            ((DefaultWallpaperPersister) this.mWallpaperPersister).onLiveWallpaperSet();
            if (z) {
                try {
                    Toast.makeText(this, (int) R.string.wallpaper_set_successfully_message, 0).show();
                } catch (Resources.NotFoundException e) {
                    Log.e("IndividualPickerAct", "Could not show toast " + e);
                }
            }
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            setResult(-1);
            finish();
        }
        if (this.mWallpaperId != null) {
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        this.mPreviewIntentFactory = new PreviewActivity.PreviewActivityIntentFactory();
        Injector injector = InjectorProvider.getInjector();
        this.mWallpaperPersister = injector.getWallpaperPersister(this);
        if (bundle == null) {
            str = getIntent().getStringExtra("com.android.wallpaper.category_collection_id");
        } else {
            str = bundle.getString("key_category_collection_id");
        }
        this.mCategoryCollectionId = str;
        String stringExtra = getIntent().getStringExtra("com.android.wallpaper.wallpaper_id");
        this.mWallpaperId = stringExtra;
        if (stringExtra == null) {
            setContentView(R.layout.activity_individual_picker);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            getDelegate().setSupportActionBar(toolbar);
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            Fragment findFragmentById = supportFragmentManager.findFragmentById(R.id.fragment_container);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setTint(R$attr.getColorAttr(this, 16842806));
            toolbar.getNavigationIcon().setAutoMirrored(true);
            if (findFragmentById == null) {
                IndividualPickerFragment individualPickerFragment = InjectorProvider.getInjector().getIndividualPickerFragment(this.mCategoryCollectionId);
                BackStackRecord backStackRecord = new BackStackRecord(supportFragmentManager);
                backStackRecord.add(R.id.fragment_container, individualPickerFragment);
                backStackRecord.commit();
            }
        } else {
            setContentView(R.layout.activity_loading);
        }
        final CategoryProvider categoryProvider = injector.getCategoryProvider(this);
        ((DefaultCategoryProvider) categoryProvider).fetchCategories(new CategoryReceiver() { // from class: com.android.wallpaper.picker.individual.IndividualPickerActivity.1
            @Override // com.android.wallpaper.model.CategoryReceiver
            public void doneFetchingCategories() {
                IndividualPickerActivity individualPickerActivity = IndividualPickerActivity.this;
                individualPickerActivity.mCategory = ((DefaultCategoryProvider) categoryProvider).getCategory(individualPickerActivity.mCategoryCollectionId);
                IndividualPickerActivity individualPickerActivity2 = IndividualPickerActivity.this;
                Category category = individualPickerActivity2.mCategory;
                if (category == null) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Failed to find the category: ");
                    m.append(IndividualPickerActivity.this.mCategoryCollectionId);
                    DiskBasedLogger.e("IndividualPickerAct", m.toString(), IndividualPickerActivity.this);
                    IndividualPickerActivity.this.finish();
                } else if (individualPickerActivity2.mWallpaperId != null) {
                    ((WallpaperCategory) category).fetchWallpapers(individualPickerActivity2.getApplicationContext(), new IndividualPickerActivity$1$$ExternalSyntheticLambda0(this), false);
                } else {
                    individualPickerActivity2.setTitle(category.mTitle);
                    individualPickerActivity2.getSupportActionBar().setTitle(individualPickerActivity2.mCategory.mTitle);
                }
            }

            @Override // com.android.wallpaper.model.CategoryReceiver
            public void onCategoryReceived(Category category) {
            }
        }, false);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("key_category_collection_id", this.mCategoryCollectionId);
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void removeToolbarMenu() {
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarMenu(int i) {
    }

    @Override // com.android.wallpaper.picker.individual.IndividualPickerFragment.IndividualPickerFragmentHost
    public void setToolbarTitle(CharSequence charSequence) {
        setTitle(charSequence);
        getSupportActionBar().setTitle(charSequence);
    }
}
