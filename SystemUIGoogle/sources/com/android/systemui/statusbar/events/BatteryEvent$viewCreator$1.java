package com.android.systemui.statusbar.events;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
final class BatteryEvent$viewCreator$1 extends Lambda implements Function1<Context, ImageView> {
    public static final BatteryEvent$viewCreator$1 INSTANCE = new BatteryEvent$viewCreator$1();

    BatteryEvent$viewCreator$1() {
        super(1);
    }

    public final ImageView invoke(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(new ThemedBatteryDrawable(context, -1));
        imageView.setBackgroundDrawable(new ColorDrawable(-16711936));
        return imageView;
    }
}
