package com.android.systemui.people;

import android.app.Activity;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class PeopleSpaceActivity extends Activity {
    private int mAppWidgetId;
    private Context mContext;
    private PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private ViewOutlineProvider mViewOutlineProvider = new ViewOutlineProvider() { // from class: com.android.systemui.people.PeopleSpaceActivity.1
        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), PeopleSpaceActivity.this.mContext.getResources().getDimension(R$dimen.people_space_widget_radius));
        }
    };

    public PeopleSpaceActivity(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getApplicationContext();
        this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
        setResult(0);
    }

    private void buildActivity() {
        List<PeopleSpaceTile> arrayList = new ArrayList<>();
        List<PeopleSpaceTile> arrayList2 = new ArrayList<>();
        try {
            arrayList = this.mPeopleSpaceWidgetManager.getPriorityTiles();
            arrayList2 = this.mPeopleSpaceWidgetManager.getRecentTiles();
        } catch (Exception e) {
            Log.e("PeopleSpaceActivity", "Couldn't retrieve conversations", e);
        }
        if (!arrayList2.isEmpty() || !arrayList.isEmpty()) {
            setContentView(R$layout.people_space_activity);
            setTileViews(R$id.priority, R$id.priority_tiles, arrayList);
            setTileViews(R$id.recent, R$id.recent_tiles, arrayList2);
            return;
        }
        setContentView(R$layout.people_space_activity_no_conversations);
        ((GradientDrawable) ((LinearLayout) findViewById(16908288)).getBackground()).setColor(this.mContext.getTheme().obtainStyledAttributes(new int[]{17956909}).getColor(0, -1));
    }

    private void setTileViews(int i, int i2, List<PeopleSpaceTile> list) {
        if (list.isEmpty()) {
            ((LinearLayout) findViewById(i)).setVisibility(8);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(i2);
        viewGroup.setClipToOutline(true);
        viewGroup.setOutlineProvider(this.mViewOutlineProvider);
        int i3 = 0;
        while (i3 < list.size()) {
            PeopleSpaceTile peopleSpaceTile = list.get(i3);
            setTileView(new PeopleSpaceTileView(this.mContext, viewGroup, peopleSpaceTile.getId(), i3 == list.size() - 1), peopleSpaceTile);
            i3++;
        }
    }

    private void setTileView(PeopleSpaceTileView peopleSpaceTileView, PeopleSpaceTile peopleSpaceTile) {
        try {
            if (peopleSpaceTile.getUserName() != null) {
                peopleSpaceTileView.setName(peopleSpaceTile.getUserName().toString());
            }
            Context context = this.mContext;
            peopleSpaceTileView.setPersonIcon(PeopleTileViewHelper.getPersonIconBitmap(context, peopleSpaceTile, PeopleTileViewHelper.getSizeInDp(context, R$dimen.avatar_size_for_medium, context.getResources().getDisplayMetrics().density)));
            peopleSpaceTileView.setOnClickListener(new View.OnClickListener(peopleSpaceTile, new PeopleTileKey(peopleSpaceTile)) { // from class: com.android.systemui.people.PeopleSpaceActivity$$ExternalSyntheticLambda0
                public final /* synthetic */ PeopleSpaceTile f$1;
                public final /* synthetic */ PeopleTileKey f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeopleSpaceActivity.m147$r8$lambda$iaS2rtDGGYcZTPta7NUxFFxEA(PeopleSpaceActivity.this, this.f$1, this.f$2, view);
                }
            });
        } catch (Exception e) {
            Log.e("PeopleSpaceActivity", "Couldn't retrieve shortcut information", e);
        }
    }

    public /* synthetic */ void lambda$setTileView$0(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, View view) {
        storeWidgetConfiguration(peopleSpaceTile, peopleTileKey);
    }

    private void storeWidgetConfiguration(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey) {
        this.mPeopleSpaceWidgetManager.addNewWidget(this.mAppWidgetId, peopleTileKey);
        finishActivity();
    }

    private void finishActivity() {
        setActivityResult(-1);
        finish();
    }

    public void dismissActivity(View view) {
        finish();
    }

    private void setActivityResult(int i) {
        Intent intent = new Intent();
        intent.putExtra("appWidgetId", this.mAppWidgetId);
        setResult(i, intent);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        buildActivity();
    }
}
