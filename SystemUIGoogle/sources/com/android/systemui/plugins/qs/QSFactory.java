package com.android.systemui.plugins.qs;

import android.content.Context;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.plugins.annotations.Dependencies;
import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@Dependencies({@DependsOn(target = QSTile.class), @DependsOn(target = QSTileView.class)})
@ProvidesInterface(action = QSFactory.ACTION, version = 2)
/* loaded from: classes.dex */
public interface QSFactory extends Plugin {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_QS_FACTORY";
    public static final int VERSION = 2;

    QSTile createTile(String str);

    QSTileView createTileView(Context context, QSTile qSTile, boolean z);
}
