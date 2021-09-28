package com.android.systemui.people.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes.dex */
public class PeopleSpaceWidgetProvider extends AppWidgetProvider {
    public PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    /* access modifiers changed from: package-private */
    public PeopleSpaceWidgetProvider(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        super.onUpdate(context, appWidgetManager, iArr);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.updateWidgets(iArr);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int i, Bundle bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.onAppWidgetOptionsChanged(i, bundle);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDeleted(Context context, int[] iArr) {
        super.onDeleted(context, iArr);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.deleteWidgets(iArr);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onRestored(Context context, int[] iArr, int[] iArr2) {
        super.onRestored(context, iArr, iArr2);
        ensurePeopleSpaceWidgetManagerInitialized();
        this.mPeopleSpaceWidgetManager.remapWidgets(iArr, iArr2);
    }

    private void ensurePeopleSpaceWidgetManagerInitialized() {
        this.mPeopleSpaceWidgetManager.init();
    }

    @VisibleForTesting
    public void setPeopleSpaceWidgetManager(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }
}
